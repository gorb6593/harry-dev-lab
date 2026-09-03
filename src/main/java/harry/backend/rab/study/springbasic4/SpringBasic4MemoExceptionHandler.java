package harry.backend.rab.study.springbasic4;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "harry.backend.rab.study.springbasic4")
public class SpringBasic4MemoExceptionHandler {
    @ExceptionHandler(MemoNotFoundException.class)
    public ProblemDetail handleNotFound(MemoNotFoundException exception, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Memo Not Found");
        problem.setProperty("instance", request.getRequestURI());
        return problem;
    }
}
