package com.ccsw.tutorialauthor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TutorialAuthorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TutorialAuthorApplication.class, args);
    }

}
