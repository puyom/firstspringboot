package com.springboot.firstspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;


@SpringBootApplication
public class FirstspringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstspringbootApplication.class, args);
    }
}
