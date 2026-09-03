package harry.backend.rab.study.springbasic5;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Controller는 HTTP 세계와 Java 애플리케이션 세계의 입구다.
@RestController("springBasic5StudentController")
@RequestMapping("/study/spring-basic5/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponse> findAll() {
        // 1. GET 요청이 이 메서드에 도착한다.
        // 2. Service가 Repository를 호출한다.
        // 3. 결과를 API 응답 DTO로 변환한다.
        return studentService.findAll().stream().map(StudentResponse::from).toList();
    }

    @GetMapping("/{id}")
    public StudentResponse findById(@PathVariable Long id) {
        return StudentResponse.from(studentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(
            @Valid @RequestBody StudentCreateRequest request) {
        // JSON Body → StudentCreateRequest 변환과 @Valid 검증은 Controller 진입 전에 처리된다.
        Student student = studentService.create(request.studentNumber(), request.studentName());
        StudentResponse response = StudentResponse.from(student);
        URI location = URI.create("/study/spring-basic5/students/" + response.studentId());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/update")
    public StudentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody StudentCreateRequest request) {
        Student student = studentService.update(id, request.studentNumber(), request.studentName());
        return StudentResponse.from(student);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
