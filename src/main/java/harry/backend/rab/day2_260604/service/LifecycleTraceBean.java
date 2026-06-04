package harry.backend.rab.day2_260604.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class LifecycleTraceBean {

	private final List<LifecycleEvent> events = new ArrayList<>();

	public LifecycleTraceBean() {
		events.add(new LifecycleEvent("constructor", getClass().getName(), LocalDateTime.now()));
	}

	@PostConstruct
	public void initialize() {
		events.add(new LifecycleEvent("postConstruct", getClass().getName(), LocalDateTime.now()));
	}

	@PreDestroy
	public void destroy() {
		events.add(new LifecycleEvent("preDestroy", getClass().getName(), LocalDateTime.now()));
	}

	public List<LifecycleEvent> events() {
		return List.copyOf(events);
	}
}
