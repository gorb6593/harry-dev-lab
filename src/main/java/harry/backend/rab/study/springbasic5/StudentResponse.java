package harry.backend.rab.study.springbasic5;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

// DB 모델 Student를 외부 API 응답 모양으로 변환한다.
// 내부 Java 필드명과 외부 JSON 필드명을 분리할 수 있다.
public record StudentResponse(
        @JsonProperty("student_id") Long studentId,
        @JsonProperty("student_number") String studentNumber,
        @JsonProperty("student_name") String studentName,
        @JsonProperty("created_at") LocalDateTime createdAt
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.id(),
                student.studentNumber(),
                student.studentName(),
                student.createdAt());
    }
}
