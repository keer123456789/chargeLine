package com.trustchain.chargeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class
ChargelineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChargelineApplication.class, args);
    }

}

