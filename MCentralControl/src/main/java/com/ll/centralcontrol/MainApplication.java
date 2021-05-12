package com.ll.centralcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/10
 */

@SpringBootApplication
public class MainApplication {


    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);
    }


    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplateBuilder().build();
    }
}
