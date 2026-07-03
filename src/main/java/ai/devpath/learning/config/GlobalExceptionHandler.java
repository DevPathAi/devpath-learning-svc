package ai.devpath.learning.config;

import ai.devpath.learning.content.ContentNotFoundException;
import ai.devpath.learning.content.InvalidContentIdException;
import ai.devpath.learning.content.InvalidProgressException;
import ai.devpath.learning.path.ActivePathConflictException;
import ai.devpath.learning.path.AiServiceUnavailableException;
import ai.devpath.learning.path.NoCompletedAssessmentException;
import ai.devpath.learning.path.PathContractException;
import ai.devpath.shared.error.ErrorCode;
import ai.devpath.shared.error.ErrorResponse;
import java.time.Instant;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 스펙 §3.4 공통 에러 envelope. learning은 예외 종류가 많고 프레임워크 예외(IllegalState 409·
 * NoSuchElement 404)·비카탈로그 상태(PathContract 502·AiServiceUnavailable 503)에 의존하므로,
 * 공용 {@code ApiExceptionHandler}를 @Import하는 대신 svc-local 핸들러를 유지하되 공용
 * {@link ErrorResponse}로 envelope를 통일한다(전 상태 보존, 회귀 0).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    IllegalArgumentException.class,
    InvalidProgressException.class,
    InvalidContentIdException.class
  })
  public ResponseEntity<ErrorResponse> validation(RuntimeException e) {
    return envelope(ErrorCode.VALIDATION_FAILED, 400, e.getMessage());
  }

  @ExceptionHandler({ContentNotFoundException.class, NoSuchElementException.class})
  public ResponseEntity<ErrorResponse> notFound(RuntimeException e) {
    return envelope(ErrorCode.RESOURCE_NOT_FOUND, 404, e.getMessage());
  }

  @ExceptionHandler({
    IllegalStateException.class,
    NoCompletedAssessmentException.class,
    ActivePathConflictException.class
  })
  public ResponseEntity<ErrorResponse> conflict(RuntimeException e) {
    return envelope(ErrorCode.CONFLICT, 409, e.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> forbidden(AccessDeniedException e) {
    return envelope(ErrorCode.FORBIDDEN, 403, e.getMessage());
  }

  // 비카탈로그 업스트림 실패 — HTTP 상태(502/503)는 보존하고 code는 INTERNAL_ERROR로 표기.
  @ExceptionHandler(PathContractException.class)
  public ResponseEntity<ErrorResponse> badGateway(PathContractException e) {
    return envelope(ErrorCode.INTERNAL_ERROR, 502, e.getMessage());
  }

  @ExceptionHandler(AiServiceUnavailableException.class)
  public ResponseEntity<ErrorResponse> serviceUnavailable(AiServiceUnavailableException e) {
    return envelope(ErrorCode.INTERNAL_ERROR, 503, e.getMessage());
  }

  private static ResponseEntity<ErrorResponse> envelope(ErrorCode code, int status, String message) {
    return ResponseEntity.status(status)
        .body(ErrorResponse.of(code, message, null, Instant.now().toString()));
  }
}
