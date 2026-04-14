package harry.backend.rab.jpaLevel7.dto;

public record OptimisticLockResponse(
        String question,
        Long stockItemId,
        Long versionBeforeUpdate,
        int beforeQuantity,
        int requestedDecrease,
        int afterQuantity,
        Long versionAfterUpdate,
        String observation,
        String nextQuestion
) {
}
