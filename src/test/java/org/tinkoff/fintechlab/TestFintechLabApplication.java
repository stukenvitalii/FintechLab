package org.tinkoff.fintechlab;

import org.springframework.boot.SpringApplication;

public class TestFintechLabApplication {

    public static void main(String[] args) {
        SpringApplication.from(FintechLabApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
