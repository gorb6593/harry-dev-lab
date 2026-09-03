package harry.backend.rab.study.springbasic4;

import java.time.LocalDateTime;

public record Memo(Long id, String content, LocalDateTime createdAt) {
}
