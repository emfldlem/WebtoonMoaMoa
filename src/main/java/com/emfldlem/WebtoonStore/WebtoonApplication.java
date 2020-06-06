package com.emfldlem.WebtoonStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@ComponentScan("com.emfldlem.*")
public class WebtoonApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebtoonApplication.class, args);
    }

}
