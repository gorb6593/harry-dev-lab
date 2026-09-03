package harry.backend.rab.study.springbasic4;

import jakarta.validation.constraints.NotBlank;

public record MemoCreateRequest(@NotBlank(message = "content는 비어 있을 수 없습니다.") String content) {
}
