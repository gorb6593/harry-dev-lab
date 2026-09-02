package harry.backend.rab.study.springbasic.spring;

public class GreetingFormatter {

    private final String prefix;

    public GreetingFormatter(String prefix) {
        this.prefix = prefix;
    }

    public String format(String name) {
        return prefix + ", " + name + "님";
    }
}
