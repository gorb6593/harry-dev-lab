package harry.backend.rab.day2_260604.service;

import java.util.List;

public record LifecycleReportResponse(
	String lifecycleBeanRuntimeClass,
	boolean lifecycleBeanIsProxy,
	String lifecycleBeanTargetClass,
	List<LifecycleEvent> events
) {
}
