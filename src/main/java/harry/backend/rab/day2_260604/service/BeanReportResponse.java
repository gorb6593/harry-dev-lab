package harry.backend.rab.day2_260604.service;

public record BeanReportResponse(
	boolean controllerBeanExists,
	boolean serviceBeanExists,
	boolean repositoryBeanExists,
	boolean discountPolicyBeanExists,
	String repositoryRuntimeClass,
	String discountPolicyRuntimeClass,
	String transactionProbeRuntimeClass,
	boolean transactionProbeIsProxy,
	String transactionProbeTargetClass,
	String transactionMode
) {
}
