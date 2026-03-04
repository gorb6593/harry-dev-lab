package harry.backend.rab.jpa.api;

public record ProductCreateRequest(
        String name,
        int price
) {
}
