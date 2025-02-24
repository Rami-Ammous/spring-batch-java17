package com.ammous.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBatchJava17Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchJava17Application.class, args);
    }

}
