package harry.backend.rab.jpaLevel7.exception;

public class StockConflictException extends RuntimeException {

    public StockConflictException(Long stockItemId) {
        super("Optimistic lock conflict occurred. stockItemId=" + stockItemId);
    }
}
