package harry.backend.rab.day1_260602.service.command;

public record StockCreateCommand(
	String name,
	int quantity
) {
}
