package com.ll.framework.config;

import com.ll.framework.aop.MCollectDataAop;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/2
 */


@Configuration
public class MClientAutoComponentScan extends WebMvcConfigurerAdapter {

    @Bean
    public MCollectDataAop mCollectDataAop(){return new MCollectDataAop();}
    /**
     * 跨域 习惯性跨域
     */
    static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods(ORIGINS)
                .maxAge(3600);
    }
    @Value("${mvf4ms.version}")
    private String version;


    @Qualifier("stringVersion")
    @Bean
    public String getVersiob(){
        return version;
    }
}
