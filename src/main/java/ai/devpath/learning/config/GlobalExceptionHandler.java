package ai.devpath.learning.config;

import ai.devpath.learning.content.ContentNotFoundException;
import ai.devpath.learning.content.InvalidContentIdException;
import ai.devpath.learning.content.InvalidProgressException;
import ai.devpath.learning.path.ActivePathConflictException;
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

  @ExceptionHandler(ActivePathConflictException.class)
  public ResponseEntity<Map<String, String>> activePathConflict(ActivePathConflictException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("errorCode", "PATH_GENERATION_CONFLICT", "error", e.getMessage()));
  }

  @ExceptionHandler(PathContractException.class)
  public ResponseEntity<Map<String, String>> badGateway(PathContractException e) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(AiServiceUnavailableException.class)
  public ResponseEntity<Map<String, String>> serviceUnavailable(AiServiceUnavailableException e) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(ContentNotFoundException.class)
  public ResponseEntity<Map<String, String>> contentNotFound(ContentNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("errorCode", "CONTENT_NOT_FOUND", "error", e.getMessage()));
  }

  @ExceptionHandler(InvalidProgressException.class)
  public ResponseEntity<Map<String, String>> invalidProgress(InvalidProgressException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("errorCode", "INVALID_PROGRESS", "error", e.getMessage()));
  }

  @ExceptionHandler(InvalidContentIdException.class)
  public ResponseEntity<Map<String, String>> invalidContentId(InvalidContentIdException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("errorCode", "INVALID_CONTENT_ID", "error", e.getMessage()));
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
