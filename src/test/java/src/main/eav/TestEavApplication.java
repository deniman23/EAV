package src.main.eav;

import org.springframework.boot.SpringApplication;

public class TestEavApplication {

    public static void main(String[] args) {
        SpringApplication.from(EavApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
