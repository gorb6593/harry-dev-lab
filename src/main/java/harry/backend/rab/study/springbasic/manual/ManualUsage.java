package harry.backend.rab.study.springbasic.manual;

public class ManualUsage {

    public String run(String name) {
        ManualMessageSender sender = new ManualMessageSender();
        ManualGreetingService service = new ManualGreetingService(sender);

        return service.greet(name);
    }
}
