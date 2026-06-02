package harry.backend.rab.day1_260602.api;

import java.time.LocalDateTime;

public record ApiErrorResponse(
	String code,
	String message,
	String path,
	LocalDateTime timestamp
) {

	public static ApiErrorResponse of(String code, String message, String path) {
		return new ApiErrorResponse(code, message, path, LocalDateTime.now());
	}
}
