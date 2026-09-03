package harry.backend.rab.study.springbasic5;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

// 예외를 HTTP 응답으로 바꾸는 마지막 변환 계층이다.
@RestControllerAdvice(basePackages = "harry.backend.rab.study.springbasic5")
public class SpringBasic5ExceptionHandler {
    @ExceptionHandler(StudentNotFoundException.class)
    public ProblemDetail handleNotFound(StudentNotFoundException exception, HttpServletRequest request) {
        return problem(HttpStatus.NOT_FOUND, "Student Not Found", exception.getMessage(), request);
    }

    @ExceptionHandler(StudentNumberAlreadyExistsException.class)
    public ProblemDetail handleDuplicate(StudentNumberAlreadyExistsException exception, HttpServletRequest request) {
        return problem(HttpStatus.CONFLICT, "Student Number Already Exists", exception.getMessage(), request);
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }
}
