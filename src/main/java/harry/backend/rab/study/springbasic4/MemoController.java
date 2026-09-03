package harry.backend.rab.study.springbasic4;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController("springBasic4MemoController")
@RequestMapping("/study/spring-basic4/memos")
public class MemoController {
    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @GetMapping
    public List<MemoResponse> findAll() {
        return memoService.findAll().stream().map(MemoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public MemoResponse findById(@PathVariable Long id) {
        return MemoResponse.from(memoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MemoResponse> create(@Valid @RequestBody MemoCreateRequest request) {
        MemoResponse response = MemoResponse.from(memoService.create(request.content()));
        return ResponseEntity.created(URI.create("/study/spring-basic4/memos/" + response.id())).body(response);
    }

    @PostMapping("/{id}/update")
    public MemoResponse update(@PathVariable Long id, @Valid @RequestBody MemoCreateRequest request) {
        return MemoResponse.from(memoService.update(id, request.content()));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
