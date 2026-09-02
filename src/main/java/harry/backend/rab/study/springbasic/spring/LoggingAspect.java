package harry.backend.rab.study.springbasic.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* harry.backend.rab.study.springbasic.spring..*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.info("study_start method={}", method);

        try {
            Object result = joinPoint.proceed();
            log.info("study_end method={}", method);
            return result;
        } catch (Throwable exception) {
            log.error("study_error method={}", method, exception);
            throw exception;
        }
    }
}
