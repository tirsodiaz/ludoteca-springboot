package com.ccsw.tutorialbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TutorialbookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TutorialbookingApplication.class, args);
    }

}
