package harry.backend.rab.study.springbasic3;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 생성 요청 DTO. JSON Body {"content": "..."} 가 이 객체로 변환된다.
// 검증 어노테이션은 Controller에서 @Valid가 붙었을 때만 동작한다.
public record MemoCreateRequest(
        @NotBlank(message = "content는 비어 있을 수 없습니다.")
        @Size(max = 100, message = "content는 100자 이하여야 합니다.")
        String content
) {
}
