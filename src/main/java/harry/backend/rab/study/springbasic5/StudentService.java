package harry.backend.rab.study.springbasic5;

import org.springframework.stereotype.Service;

import java.util.List;

// Service는 '학생을 등록·조회·수정·삭제한다'라는 업무 흐름을 담당한다.
// HTTP 상태 코드나 SQL 문장은 이 계층의 책임이 아니다.
@Service("springBasic5StudentService")
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student create(String studentNumber, String studentName) {
        return studentRepository.save(studentNumber, studentName);
    }

    public Student update(Long id, String studentNumber, String studentName) {
        if (!studentRepository.update(id, studentNumber, studentName)) {
            throw new StudentNotFoundException(id);
        }
        return findById(id);
    }

    public void delete(Long id) {
        if (!studentRepository.delete(id)) {
            throw new StudentNotFoundException(id);
        }
    }
}
