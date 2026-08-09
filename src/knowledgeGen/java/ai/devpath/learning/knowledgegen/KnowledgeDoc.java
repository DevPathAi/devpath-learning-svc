package ai.devpath.learning.knowledgegen;

/**
 * 스캔된 학습 문서 하나.
 *
 * @param docKey   레포 루트 기준 상대경로. 항상 '/' 구분자. 증분 갱신 키다.
 * @param title    첫 H1. 없으면 확장자 없는 파일명.
 * @param category 최상위 디렉토리명.
 * @param docHash  원문 SHA-256 hex(64자).
 * @param markdown 원문 전체.
 */
public record KnowledgeDoc(
    String docKey, String title, String category, String docHash, String markdown) {}
