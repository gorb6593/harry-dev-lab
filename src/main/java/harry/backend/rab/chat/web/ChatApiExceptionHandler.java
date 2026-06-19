package harry.backend.rab.chat.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * chat 패키지 전용 예외 처리기.
 *
 * 응답은 RFC 7807 표준인 {@link ProblemDetail}(application/problem+json)로 통일한다.
 * Spring Boot 3의 기본 에러 표현이며, 별도 응답 DTO를 만들지 않아도 일관된 에러 계약을 제공한다.
 *
 * - basePackages로 chat에만 적용 → 다른(day*) 컨트롤러의 기존 처리기와 충돌하지 않는다.
 * - HIGHEST_PRECEDENCE → 전역으로 떠 있는 처리기보다 우선해 chat 응답 포맷을 보장한다.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "harry.backend.rab.chat")
public class ChatApiExceptionHandler {

	/** @Valid 본문 검증 실패: 필드별 메시지를 errors에 담아 400으로 응답. */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
			HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다.");
		problem.setTitle("Validation Failed");

		Map<String, String> errors = new LinkedHashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		problem.setProperty("errors", errors);
		return problem;
	}

	/** 도메인 불변식 위반(생성자/메서드의 IllegalArgumentException) → 400. */
	@ExceptionHandler(IllegalArgumentException.class)
	public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
			HttpStatus.BAD_REQUEST, ex.getMessage());
		problem.setTitle("Bad Request");
		return problem;
	}

	/** 서비스가 의도적으로 던진 상태 예외(404/409/403 등). */
	@ExceptionHandler(ResponseStatusException.class)
	public ProblemDetail handleStatus(ResponseStatusException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
			ex.getStatusCode(), ex.getReason());
		problem.setTitle(HttpStatus.valueOf(ex.getStatusCode().value()).getReasonPhrase());
		return problem;
	}

	/** 예상하지 못한 그 외 예외 → 500. 내부 메시지는 숨기고 일반 문구만 노출. */
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleUnexpected(Exception ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
			HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
		problem.setTitle("Internal Server Error");
		return problem;
	}
}
