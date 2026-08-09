package ai.devpath.learning.knowledgegen;

import java.util.List;

/** 배치 임베딩. 단건 호출은 청크당 2.3초라 19,109청크에 12시간이 걸린다(실측). */
public interface BatchEmbeddingClient {
  List<List<Double>> embedAll(List<String> texts) throws Exception;
}
