package com.training.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"classpath:application-base.properties"})
public class DemoSpringAjaxApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringAjaxApplication.class, args);
    }

}
