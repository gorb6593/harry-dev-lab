package harry.backend.rab.study.springbasic1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RestController: @Controller와 @ResponseBody를 합친 역할이다.
// 메서드 반환값을 HTML View가 아니라 HTTP 응답 Body로 바로 보낸다.
@RestController
// Controller가 처리할 URL의 공통 앞부분이다.
@RequestMapping("/study/spring-basic1")
public class SimpleGreetingController {

    private final SimpleGreetingService greetingService;

    // 생성자 주입: Spring이 SimpleGreetingService Bean을 넣어준다.
    // Controller에서 직접 new SimpleGreetingService()를 하지 않는다.
    public SimpleGreetingController(SimpleGreetingService greetingService) {
        this.greetingService = greetingService;
    }

    // @GetMapping: GET 요청이 들어왔을 때 실행한다.
    // GET은 보통 데이터를 조회할 때 사용한다.
    // @PathVariable: URL 안의 {name} 값을 메서드 파라미터로 가져온다.
    @GetMapping("/greetings/{name}")
    public String getGreeting(@PathVariable String name) {
        return greetingService.greet(name);
    }

    // @PostMapping: POST 요청이 들어왔을 때 실행한다.
    // POST는 보통 서버에 데이터를 전달하거나 새 데이터를 만들 때 사용한다.
    // @RequestParam: URL의 ?name=harry 값을 메서드 파라미터로 가져온다.
    @PostMapping("/greetings")
    public String postGreeting(@RequestParam String name) {
        return greetingService.greet(name) + " (POST 요청)";
    }
}
