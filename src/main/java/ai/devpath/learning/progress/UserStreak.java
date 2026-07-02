package ai.devpath.learning.progress;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

/** 스키마: devpath-shared {@code V202607021001__user_streak.sql}. */
@Entity
@Table(name = "user_streak")
public class UserStreak {

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "current_days", nullable = false)
	private Integer currentDays = 0;

	@Column(name = "longest_days", nullable = false)
	private Integer longestDays = 0;

	@Column(name = "last_active_date")
	private LocalDate lastActiveDate;

	@Column(name = "updated_at")
	private Instant updatedAt;

	public Long getUserId() { return userId; }
	public void setUserId(Long v) { this.userId = v; }
	public Integer getCurrentDays() { return currentDays; }
	public void setCurrentDays(Integer v) { this.currentDays = v; }
	public Integer getLongestDays() { return longestDays; }
	public void setLongestDays(Integer v) { this.longestDays = v; }
	public LocalDate getLastActiveDate() { return lastActiveDate; }
	public void setLastActiveDate(LocalDate v) { this.lastActiveDate = v; }
	public Instant getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Instant v) { this.updatedAt = v; }
}
