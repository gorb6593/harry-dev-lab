package harry.backend.rab.study.springbasic.manual;

public class ManualGreetingService {

    private final ManualMessageSender messageSender;

    public ManualGreetingService(ManualMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public String greet(String name) {
        return messageSender.send("안녕하세요, " + name + "님");
    }
}
