package harry.backend.rab.study.springbasic3;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 수정 요청 DTO. 생성과 필드가 같아도 별도 클래스로 두면
// 나중에 요구사항이 갈라질 때 서로 영향을 주지 않는다.
public record MemoUpdateRequest(
        @NotBlank(message = "content는 비어 있을 수 없습니다.")
        @Size(max = 100, message = "content는 100자 이하여야 합니다.")
        String content
) {
}
