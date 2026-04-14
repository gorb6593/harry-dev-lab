package harry.backend.rab.jpaLevel7.dto;

public record ConcurrencyObservationResponse(
        String question,
        Long stockItemId,
        int beforeQuantity,
        int requestedDecrease,
        int afterQuantity,
        String observation,
        String nextQuestion
) {
}
