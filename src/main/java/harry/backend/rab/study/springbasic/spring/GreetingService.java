package harry.backend.rab.study.springbasic.spring;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final MessageSender messageSender;
    private final GreetingFormatter greetingFormatter;

    public GreetingService(
            MessageSender messageSender,
            GreetingFormatter greetingFormatter
    ) {
        this.messageSender = messageSender;
        this.greetingFormatter = greetingFormatter;
    }

    public String greet(String name) {
        return messageSender.send(greetingFormatter.format(name));
    }
}
