package com.ll.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/26
 */

@SpringBootApplication
public class ServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class);
    }


    @Bean
    public RestTemplate get(){
        return new RestTemplateBuilder().build();
    }
}
