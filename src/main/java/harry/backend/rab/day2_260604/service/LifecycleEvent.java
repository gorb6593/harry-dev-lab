package harry.backend.rab.day2_260604.service;

import java.time.LocalDateTime;

public record LifecycleEvent(
	String phase,
	String beanClass,
	LocalDateTime occurredAt
) {
}
