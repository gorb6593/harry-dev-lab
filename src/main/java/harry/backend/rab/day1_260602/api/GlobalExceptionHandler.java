package harry.backend.rab.day1_260602.api;

import java.util.stream.Collectors;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import harry.backend.rab.day1_260602.domain.InsufficientStockException;
import harry.backend.rab.day1_260602.service.StockItemNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
		MethodArgumentNotValidException exception,
		HttpServletRequest request
	) {
		String message = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining(", "));

		return createResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", message, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
		ConstraintViolationException exception,
		HttpServletRequest request
	) {
		String message = exception.getConstraintViolations()
			.stream()
			.map(violation -> violation.getMessage())
			.collect(Collectors.joining(", "));

		return createResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", message, request);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ApiErrorResponse> handleHandlerMethodValidation(
		HandlerMethodValidationException exception,
		HttpServletRequest request
	) {
		String message = exception.getParameterValidationResults()
			.stream()
			.flatMap(result -> result.getResolvableErrors().stream())
			.map(MessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining(", "));

		return createResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", message, request);
	}

	@ExceptionHandler(StockItemNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleStockItemNotFound(
		StockItemNotFoundException exception,
		HttpServletRequest request
	) {
		return createResponse(HttpStatus.NOT_FOUND, "STOCK_ITEM_NOT_FOUND", exception.getMessage(), request);
	}

	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<ApiErrorResponse> handleInsufficientStock(
		InsufficientStockException exception,
		HttpServletRequest request
	) {
		return createResponse(HttpStatus.CONFLICT, "INSUFFICIENT_STOCK", exception.getMessage(), request);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
		HttpServletRequest request
	) {
		return createResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "요청 본문을 읽을 수 없습니다.", request);
	}

	private ResponseEntity<ApiErrorResponse> createResponse(
		HttpStatus status,
		String code,
		String message,
		HttpServletRequest request
	) {
		return ResponseEntity
			.status(status)
			.body(ApiErrorResponse.of(code, message, request.getRequestURI()));
	}
}
