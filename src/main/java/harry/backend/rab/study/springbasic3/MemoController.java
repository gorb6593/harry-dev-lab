package harry.backend.rab.study.springbasic3;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

// 이번 단계의 핵심:
//  1. 요청 데이터를 JSON Body(@RequestBody + DTO)로 받는다.
//  2. 응답을 ResponseEntity로 감싸 HTTP 상태 코드와 헤더를 직접 정한다.
//  3. 조회는 GET, 상태 변경은 전부 POST로 처리한다(POST-only 방식).
// Bean 이름을 명시한다. 기본 이름은 클래스명(memoController)인데 springbasic2에도 같은 클래스명이 있어
// 패키지가 달라도 Bean 이름이 충돌한다(ConflictingBeanDefinitionException).
@RestController("springBasic3MemoController")
@RequestMapping("/study/spring-basic3/memos")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    // GET /memos → 200 OK + 목록
    // ResponseEntity 없이 객체만 반환하면 Spring이 200으로 보낸다.
    @GetMapping
    public List<MemoResponse> findAll() {
        return memoService.findAll().stream()
                .map(MemoResponse::from)
                .toList();
    }

    // GET /memos/{id} → 200 OK + 단건
    // 없는 id면 Service가 MemoNotFoundException을 던지고,
    // MemoExceptionHandler가 404로 바꿔 응답한다.
    @GetMapping("/{id}")
    public MemoResponse findById(@PathVariable Long id) {
        return MemoResponse.from(memoService.findById(id));
    }

    // POST /memos → 201 Created + Location 헤더 + 생성된 메모
    // @RequestBody: JSON Body를 MemoCreateRequest로 변환한다.
    // @Valid: 변환된 DTO의 @NotBlank, @Size 검증을 실행한다. 실패하면 400.
    @PostMapping
    public ResponseEntity<MemoResponse> create(@RequestBody @Valid MemoCreateRequest request) {
        Memo created = memoService.create(request.content());
        // RFC 9110 9.3.3: 리소스를 만들었으면 201과 Location으로 위치를 알려준다.
        URI location = URI.create("/study/spring-basic3/memos/" + created.id());
        return ResponseEntity.created(location).body(MemoResponse.from(created));
    }

    // POST /memos/{id}/update → 200 OK + 수정된 메모
    // PUT 대신 POST + 경로 동사(update)로 의도를 표현하는 POST-only 방식이다.
    @PostMapping("/{id}/update")
    public MemoResponse update(@PathVariable Long id, @RequestBody @Valid MemoUpdateRequest request) {
        return MemoResponse.from(memoService.update(id, request.content()));
    }

    // POST /memos/{id}/delete → 204 No Content (Body 없음)
    // DELETE 대신 POST + 경로 동사(delete). 성공했지만 돌려줄 내용이 없을 때 204를 쓴다.
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
