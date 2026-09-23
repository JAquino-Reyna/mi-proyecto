package com.studyprogress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StudyprogressApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyprogressApplication.class, args);
    }
}