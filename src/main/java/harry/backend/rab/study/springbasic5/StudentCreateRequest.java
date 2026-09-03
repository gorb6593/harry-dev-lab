package harry.backend.rab.study.springbasic5;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

// HTTP JSON Body를 Java 객체로 받는 입력 DTO다.
// API 이름은 camel_case 규칙을 사용한다.
public record StudentCreateRequest(
        @JsonProperty("student_number")
        @NotBlank(message = "student_number는 비어 있을 수 없습니다.")
        String studentNumber,

        @JsonProperty("student_name")
        @NotBlank(message = "student_name은 비어 있을 수 없습니다.")
        String studentName
) {
}
