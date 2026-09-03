package harry.backend.rab.study.springbasic5;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("학생을 찾을 수 없습니다. student_id=" + id);
    }
}
