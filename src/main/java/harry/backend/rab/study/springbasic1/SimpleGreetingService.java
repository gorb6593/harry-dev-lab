package harry.backend.rab.study.springbasic1;

import org.springframework.stereotype.Service;

// @Service: 이 클래스를 Spring이 관리하는 Bean으로 등록한다.
// 다른 클래스에서 생성자 주입으로 사용할 수 있게 된다.
@Service
public class SimpleGreetingService {

    // Service는 실제 기능이나 비즈니스 로직을 담당한다.
    public String greet(String name) {
        return "안녕하세요, " + name + "님";
    }
}
