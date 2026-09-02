package harry.backend.rab.study.springbasic.spring;

import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    public String send(String message) {
        return message;
    }
}
