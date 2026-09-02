package harry.backend.rab.study.springbasic;

import harry.backend.rab.study.springbasic.manual.ManualGreetingService;
import harry.backend.rab.study.springbasic.manual.ManualMessageSender;
import harry.backend.rab.study.springbasic.spring.GreetingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBasicComparison {

    @Bean
    CommandLineRunner compareOnStartup(GreetingService springService) {
        return args -> {
            ManualGreetingService manualService = new ManualGreetingService(new ManualMessageSender());

            System.out.println("before = " + manualService.greet("수동 객체"));
            System.out.println("after  = " + springService.greet("Spring Bean"));
        };
    }
}
