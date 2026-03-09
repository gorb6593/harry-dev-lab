package harry.backend.rab.jpa.api;

public record ProductPersistenceContextResponse(
        Long productId,
        boolean sameInstanceInTransaction,
        int firstIdentityHash,
        int secondIdentityHash,
        String name,
        int price
) {
}
