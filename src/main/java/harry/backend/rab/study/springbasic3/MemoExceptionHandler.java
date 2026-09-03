package harry.backend.rab.study.springbasic3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

// 이 패키지의 Controller에서 예외가 나면 여기로 모인다.
// 에러 응답은 IETF 표준인 RFC 9457 Problem Details 형식(ProblemDetail)으로 통일한다.
//   Content-Type: application/problem+json
//   {"type": ..., "title": ..., "status": ..., "detail": ..., "instance": ...}
// basePackages로 범위를 제한해 다른 학습 패키지에는 영향을 주지 않는다.
@RestControllerAdvice(basePackages = "harry.backend.rab.study.springbasic3")
public class MemoExceptionHandler {

    // 없는 메모 → 404 Not Found
    @ExceptionHandler(MemoNotFoundException.class)
    public ProblemDetail handleNotFound(MemoNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Memo Not Found");
        problem.setType(URI.create("https://example.com/problems/memo-not-found"));
        problem.setProperty("memoId", ex.getId()); // RFC 9457이 허용하는 확장 필드
        return problem;
    }

    // @Valid 검증 실패 → 400 Bad Request + 필드별 메시지
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다.");
        problem.setTitle("Validation Failed");

        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        problem.setProperty("errors", errors);
        return problem;
    }

    // JSON 자체가 깨졌거나 Body가 없음 → 400 Bad Request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "요청 Body를 읽을 수 없습니다. JSON 형식을 확인하세요.");
        problem.setTitle("Malformed Request Body");
        return problem;
    }
}
