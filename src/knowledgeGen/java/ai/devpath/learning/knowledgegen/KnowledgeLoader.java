package ai.devpath.learning.knowledgegen;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 지식베이스 적재. 문서 단위 upsert이며 재실행해도 중복이 생기지 않는다.
 * 문서가 바뀌면(doc_hash 변경) 그 문서의 청크를 통째로 지우고 다시 넣는다 —
 * 개정으로 청크 수가 줄어들 때 옛 청크가 남지 않게 하려는 것이다.
 *
 * <p>입력 레코드는 (docKey, chunkIndex) 기준으로 dedup한다. Task 5의 배치 임베딩 커맨드는
 * 중단 후 재실행 시 한 문서의 부분 기록 뒤에 완전한 재실행분을 그대로 append하므로,
 * embeddings.jsonl 한 파일 안에 같은 (docKey, chunkIndex)가 여러 번 나타날 수 있다.
 * 이때 파일에서 나중에 나온 레코드(재실행이 만든 완전한 기록)를 채택한다 —
 * 그렇지 않으면 knowledge_embeddings의 (document_id, chunk_index) UNIQUE 제약을 위반한다.
 * 문서 행(title·category·doc_hash·source_commit)도 같은 이유로 그 문서의 마지막 레코드
 * 기준으로 정한다.
 *
 * <p><b>문서 단위 트랜잭션.</b> 문서 행 upsert + 그 문서의 기존 청크 delete + 새 청크 insert
 * 전체를 문서마다 하나의 트랜잭션으로 묶는다(전체 실행을 통째로 묶지 않는다) — 743개 문서 ×
 * 19,109청크를 단일 트랜잭션으로 묶으면 중간 실패 시 이미 끝난 문서까지 전부 롤백돼 재실행이
 * 처음부터 다시 시작해야 하기 때문이다. 문서 단위 원자성이면 "완료된 문서는 남고, 실패한
 * 문서만 이전 상태로 되돌아간다"가 되어, 이 클래스의 멱등 재실행 설계와 맞아떨어진다.
 * 문서 행 upsert도 그 문서의 청크 delete/insert와 같은 트랜잭션에 넣는다 — 그렇지 않으면
 * 청크 insert가 실패해 롤백돼도 문서 행(title/category/doc_hash)만 새 값으로 남아, 문서
 * 행과 청크가 서로 다른 버전을 가리키는 불일치가 생긴다.
 */
public class KnowledgeLoader {

  public int load(JdbcTemplate jdbc, List<KnowledgeEmbeddingRecord> records, String sourceRepo) {
    // 문서별 "마지막으로 본 레코드" — 문서 행(title/category/doc_hash/source_commit)에 쓴다.
    Map<String, KnowledgeEmbeddingRecord> lastByDoc = new LinkedHashMap<>();
    // 문서별 · 청크 인덱스별 "마지막으로 본 레코드" — (docKey, chunkIndex) dedup.
    Map<String, Map<Integer, KnowledgeEmbeddingRecord>> chunksByDoc = new LinkedHashMap<>();

    for (KnowledgeEmbeddingRecord r : records) {
      KnowledgeEmbeddingRecord prev = lastByDoc.put(r.docKey(), r);
      var byIndex = chunksByDoc.computeIfAbsent(r.docKey(), k -> new LinkedHashMap<>());
      // 같은 문서의 docHash가 이전 레코드와 달라졌다면 문서 버전이 바뀐 것이다. 증분 재실행에서
      // embeddings.jsonl에 옛 버전 전체 뒤에 새 버전 전체가 통째로 append되므로(한 버전의 레코드는
      // 파일에서 연속이다), 새 버전이 시작되는 순간 그때까지 누적한 옛 버전 청크를 통째로 버려야
      // 개정으로 청크 수가 줄었을 때 옛 청크가 살아남지 않는다.
      if (prev != null && !prev.docHash().equals(r.docHash())) {
        byIndex.clear();
      }
      byIndex.put(r.chunkIndex(), r);
    }

    var txTemplate = new TransactionTemplate(
        new DataSourceTransactionManager(jdbc.getDataSource()));

    int inserted = 0;
    for (Map.Entry<String, Map<Integer, KnowledgeEmbeddingRecord>> entry : chunksByDoc.entrySet()) {
      String docKey = entry.getKey();
      Collection<KnowledgeEmbeddingRecord> chunks = entry.getValue().values();
      KnowledgeEmbeddingRecord docValues = lastByDoc.get(docKey);

      int insertedForDoc = txTemplate.execute(status -> loadOneDocument(jdbc, docValues, chunks, sourceRepo));
      inserted += insertedForDoc;
    }
    return inserted;
  }

  private int loadOneDocument(JdbcTemplate jdbc, KnowledgeEmbeddingRecord docValues,
      Collection<KnowledgeEmbeddingRecord> chunks, String sourceRepo) {
    Long documentId = jdbc.queryForObject("""
        insert into knowledge_documents(doc_key, title, category, source_repo, source_commit,
          doc_hash, status)
        values (?, ?, ?, ?, ?, ?, 'ACTIVE')
        on conflict (doc_key) do update set
          title = excluded.title,
          category = excluded.category,
          source_repo = excluded.source_repo,
          source_commit = excluded.source_commit,
          doc_hash = excluded.doc_hash,
          status = 'ACTIVE'
        returning id
        """, Long.class,
        docValues.docKey(), docValues.title(), docValues.category(), sourceRepo,
        docValues.sourceCommit(), docValues.docHash());

    jdbc.update("delete from knowledge_embeddings where document_id = ?", documentId);

    int insertedForDoc = 0;
    for (KnowledgeEmbeddingRecord chunk : chunks) {
      jdbc.update("""
          insert into knowledge_embeddings(document_id, chunk_index, chunk_text, embedding,
            chunk_hash, status)
          values (?, ?, ?, cast(? as vector), ?, 'ACTIVE')
          """,
          documentId, chunk.chunkIndex(), chunk.chunkText(),
          toVectorLiteral(chunk.embedding()), chunk.chunkHash());
      insertedForDoc++;
    }
    return insertedForDoc;
  }

  private String toVectorLiteral(List<Double> embedding) {
    if (embedding == null || embedding.size() != 768) {
      throw new IllegalArgumentException("embedding must be 768 dimensions");
    }
    var sb = new StringBuilder("[");
    for (int i = 0; i < embedding.size(); i++) {
      if (i > 0) sb.append(',');
      Double v = embedding.get(i);
      if (v == null || v.isNaN() || v.isInfinite()) {
        throw new IllegalArgumentException("embedding contains invalid value");
      }
      sb.append(v);
    }
    return sb.append(']').toString();
  }
}
