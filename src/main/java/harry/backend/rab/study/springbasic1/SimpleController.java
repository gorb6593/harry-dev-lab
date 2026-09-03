package harry.backend.rab.study.springbasic1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// @Controller: 주로 HTML 화면(View)을 반환하는 Controller에 사용한다.
@Controller
@RequestMapping("/study/spring-basic1/controller")
public class SimpleController {

    // @ResponseBody가 없으면 반환값 "hello"를 View 이름으로 해석한다.
    // 즉, templates/hello.html을 찾으려고 한다.
    // 이 프로젝트에는 해당 화면이 없으므로 실행 시 View를 찾지 못한다.
    @GetMapping("/view")
    public String view() {
        return "hello";
    }

    // @ResponseBody: 반환값을 View 이름이 아니라 HTTP 응답 Body로 보낸다.
    @ResponseBody
    @GetMapping("/body")
    public String body() {
        return "일반 @Controller에서 응답 Body를 반환합니다.";
    }
}
