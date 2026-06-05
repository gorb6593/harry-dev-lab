package harry.backend.rab.day3_260604.service;

public record PersistenceObservationResponse(
	Long productId,
	String transactionCase,
	boolean containsAfterFind,
	boolean containsAfterChange,
	int priceBeforeChange,
	int requestedPrice,
	int priceInsideTransaction,
	int priceAfterTransaction,
	String explanation
) {
}
