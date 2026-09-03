package harry.backend.rab.study.springbasic5;

import java.time.LocalDateTime;

// DB 한 행을 Java 애플리케이션 안에서 표현하는 객체다.
// 아직 JPA Entity가 아니다. JdbcTemplate으로 조회한 결과를 담는 단순한 record다.
public record Student(
        Long id,
        String studentNumber,
        String studentName,
        LocalDateTime createdAt
) {
}
