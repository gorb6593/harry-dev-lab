package harry.backend.rab.day2_260604.service;

public record TransactionProbeResponse(
	boolean externalCallTransactionActive,
	boolean externalCallReadOnly,
	boolean selfInvocationTransactionActive,
	boolean selfInvocationReadOnly
) {
}
