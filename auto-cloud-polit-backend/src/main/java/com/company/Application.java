package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.company", "com.company.plugin"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}