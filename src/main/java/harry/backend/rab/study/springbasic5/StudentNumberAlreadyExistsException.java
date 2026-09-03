package harry.backend.rab.study.springbasic5;

public class StudentNumberAlreadyExistsException extends RuntimeException {
    public StudentNumberAlreadyExistsException(String studentNumber) {
        super("이미 등록된 학번입니다. student_number=" + studentNumber);
    }
}
