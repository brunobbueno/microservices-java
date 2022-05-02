package com.msapplicationbyprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class MsApplicationByprocessApplication {

    public static void main(String[] args) throws InterruptedException{

        SpringApplication.run(MsApplicationByprocessApplication.class, args);
    }

}
