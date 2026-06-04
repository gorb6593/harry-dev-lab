package harry.backend.rab.day2_260604.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class LifecycleTraceBean {

	private static final Logger log = LoggerFactory.getLogger(LifecycleTraceBean.class);

	private final List<LifecycleEvent> events = new ArrayList<>();

	public LifecycleTraceBean() {
		recordEvent("constructor");
	}

	@PostConstruct
	public void initialize() {
		recordEvent("postConstruct");
	}

	@PreDestroy
	public void destroy() {
		recordEvent("preDestroy");
	}

	public List<LifecycleEvent> events() {
		return List.copyOf(events);
	}

	private void recordEvent(String phase) {
		LifecycleEvent event = new LifecycleEvent(phase, getClass().getName(), LocalDateTime.now());
		events.add(event);
		log.info("Day2 lifecycle event phase={}, beanClass={}, occurredAt={}",
			event.phase(),
			event.beanClass(),
			event.occurredAt()
		);
	}
}
