package harry.backend.rab.study.springbasic.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBasicConfig {

    @Bean
    public GreetingFormatter greetingFormatter() {
        return new GreetingFormatter("안녕하세요");
    }
}
