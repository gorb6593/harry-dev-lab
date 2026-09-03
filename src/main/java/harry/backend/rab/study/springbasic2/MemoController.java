package harry.backend.rab.study.springbasic2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RestController: 반환값을 화면 이름이 아니라 HTTP 응답 Body로 보낸다.
@RestController("springBasic2MemoController")
// 이 Controller의 모든 URL 앞에 붙는 공통 경로다.
@RequestMapping("/study/spring-basic2/memos")
public class MemoController {

    private final MemoService memoService;

    // Spring이 MemoService Bean을 생성자에 넣어준다.
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    // @GetMapping: 메모 목록을 조회한다.
    // 조회만 하므로 서버의 메모 데이터는 바뀌지 않는다.
    @GetMapping
    public java.util.List<Memo> findAll() {
        return memoService.findAll();
    }

    // @PostMapping: 새로운 메모를 생성한다.
    // @RequestParam은 ?content=... 값을 가져온다.
    @PostMapping
    public Memo create(@RequestParam String content) {
        return memoService.create(content);
    }
}
