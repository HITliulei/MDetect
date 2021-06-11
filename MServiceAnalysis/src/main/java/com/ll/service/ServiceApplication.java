package com.ll.service;

import com.ll.framework.ano.MClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/26
 */

@SpringBootApplication
public class ServiceApplication extends WebMvcConfigurerAdapter {


    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class);
    }


    @Bean
    public RestTemplate get(){
        return new RestTemplateBuilder().build();
    }


    static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods(ORIGINS)
                .maxAge(3600);
    }
}
