package ai.devpath.learning.knowledgegen;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

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
 */
public class KnowledgeLoader {

  public int load(JdbcTemplate jdbc, List<KnowledgeEmbeddingRecord> records, String sourceRepo) {
    // 문서별 "마지막으로 본 레코드" — 문서 행(title/category/doc_hash/source_commit)에 쓴다.
    Map<String, KnowledgeEmbeddingRecord> lastByDoc = new LinkedHashMap<>();
    // 문서별 · 청크 인덱스별 "마지막으로 본 레코드" — (docKey, chunkIndex) dedup.
    Map<String, Map<Integer, KnowledgeEmbeddingRecord>> chunksByDoc = new LinkedHashMap<>();

    for (KnowledgeEmbeddingRecord r : records) {
      lastByDoc.put(r.docKey(), r);
      chunksByDoc.computeIfAbsent(r.docKey(), k -> new LinkedHashMap<>())
          .put(r.chunkIndex(), r);
    }

    int inserted = 0;
    for (Map.Entry<String, Map<Integer, KnowledgeEmbeddingRecord>> entry : chunksByDoc.entrySet()) {
      String docKey = entry.getKey();
      Collection<KnowledgeEmbeddingRecord> chunks = entry.getValue().values();
      KnowledgeEmbeddingRecord docValues = lastByDoc.get(docKey);

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

      for (KnowledgeEmbeddingRecord chunk : chunks) {
        jdbc.update("""
            insert into knowledge_embeddings(document_id, chunk_index, chunk_text, embedding,
              chunk_hash, status)
            values (?, ?, ?, cast(? as vector), ?, 'ACTIVE')
            """,
            documentId, chunk.chunkIndex(), chunk.chunkText(),
            toVectorLiteral(chunk.embedding()), chunk.chunkHash());
        inserted++;
      }
    }
    return inserted;
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
