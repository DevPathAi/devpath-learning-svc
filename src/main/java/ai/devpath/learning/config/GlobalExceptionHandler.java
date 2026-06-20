package ai.devpath.learning.config;

import ai.devpath.learning.path.AiServiceUnavailableException;
import ai.devpath.learning.path.NoCompletedAssessmentException;
import ai.devpath.learning.path.PathContractException;
import java.util.NoSuchElementException;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Map<String, String>> conflict(IllegalStateException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(NoCompletedAssessmentException.class)
  public ResponseEntity<Map<String, String>> noCompletedAssessment(NoCompletedAssessmentException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("errorCode", "NO_COMPLETED_ASSESSMENT", "error", e.getMessage()));
  }

  @ExceptionHandler(PathContractException.class)
  public ResponseEntity<Map<String, String>> badGateway(PathContractException e) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(AiServiceUnavailableException.class)
  public ResponseEntity<Map<String, String>> serviceUnavailable(AiServiceUnavailableException e) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, String>> forbidden(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Map<String, String>> notFound(NoSuchElementException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
  }
}
