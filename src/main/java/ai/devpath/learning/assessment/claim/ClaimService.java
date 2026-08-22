package ai.devpath.learning.assessment.claim;

import ai.devpath.learning.assessment.Assessment;
import ai.devpath.learning.assessment.AssessmentRepository;
import ai.devpath.learning.assessment.guest.GuestSession;
import ai.devpath.learning.assessment.guest.GuestSessionStore;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ClaimService {

  private static final Logger log = LoggerFactory.getLogger(ClaimService.class);
  private static final Pattern GUEST_ID = Pattern.compile(
      "(?i)^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

  private final GuestSessionStore guestStore;
  private final AssessmentRepository assessments;
  private final ClaimPersistenceService persistence;
  private final LegacyClaimHintStore legacyHints;

  public ClaimService(GuestSessionStore guestStore, AssessmentRepository assessments,
      ClaimPersistenceService persistence, LegacyClaimHintStore legacyHints) {
    this.guestStore = guestStore;
    this.assessments = assessments;
    this.persistence = persistence;
    this.legacyHints = legacyHints;
  }

  public long claim(long userId, String guestId) {
    if (guestId == null || !GUEST_ID.matcher(guestId).matches()) {
      throw new IllegalArgumentException("유효하지 않은 guest 진단 식별자");
    }
    String canonicalGuestId = guestId.toLowerCase(Locale.ROOT);
    var committed = assessments.findBySourceGuestId(canonicalGuestId);
    if (committed.isPresent()) {
      return ownedResultAndCleanup(userId, canonicalGuestId, committed.get());
    }

    var guestSession = guestStore.find(canonicalGuestId);
    if (guestSession.isEmpty()) {
      var legacyHint = legacyHints.take(canonicalGuestId);
      if (legacyHint.isPresent()) {
        return bindLegacyHint(userId, canonicalGuestId, legacyHint.getAsLong());
      }
      Assessment winner = assessments.findBySourceGuestId(canonicalGuestId)
          .orElseThrow(() -> new NoSuchElementException("guest 세션 없음/만료"));
      return ownedResultAndCleanup(userId, canonicalGuestId, winner);
    }
    GuestSession session = guestSession.get();
    if (!session.completed()) {
      throw new IllegalStateException("완료되지 않은 guest 진단");
    }

    try {
      long assessmentId = persistence.persist(userId, canonicalGuestId, session);
      cleanupBestEffort(canonicalGuestId, assessmentId);
      return assessmentId;
    } catch (DataIntegrityViolationException conflict) {
      Assessment winner = assessments.findBySourceGuestId(canonicalGuestId)
          .orElseThrow(() -> conflict);
      return ownedResultAndCleanup(userId, canonicalGuestId, winner);
    }
  }

  private long bindLegacyHint(long userId, String guestId, long assessmentId) {
    try {
      assessments.bindLegacyClaimSource(assessmentId, userId, guestId);
    } catch (DataIntegrityViolationException concurrentBinding) {
      // The unique source_guest_id constraint elected a winner. Resolve it below from PostgreSQL.
    }
    Assessment committed = assessments.findBySourceGuestId(guestId)
        .orElseThrow(() -> new AccessDeniedException("guest 진단을 이행할 권한이 없음"));
    return ownedResultAndCleanup(userId, guestId, committed);
  }

  private long ownedResultAndCleanup(long userId, String guestId, Assessment assessment) {
    if (!Objects.equals(assessment.getUserId(), userId)) {
      throw new AccessDeniedException("guest 진단을 이행할 권한이 없음");
    }
    long assessmentId = assessment.getId();
    cleanupBestEffort(guestId, assessmentId);
    return assessmentId;
  }

  private void cleanupBestEffort(String guestId, long assessmentId) {
    try {
      legacyHints.delete(guestId);
    } catch (RuntimeException markerCleanupFailure) {
      log.warn("legacy claim marker cleanup failed after commit; assessmentId={}; cause={}",
          assessmentId, markerCleanupFailure.getClass().getSimpleName());
    }
    try {
      guestStore.delete(guestId);
    } catch (RuntimeException guestCleanupFailure) {
      log.warn("guest session cleanup failed after commit; assessmentId={}; cause={}",
          assessmentId, guestCleanupFailure.getClass().getSimpleName());
    }
  }
}
