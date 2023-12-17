package com.kaskademindbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ZiyuanZhou
 */
@SpringBootApplication
@EnableConfigurationProperties
public class KaskadeMindBankApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(KaskadeMindBankApplication.class, args);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/23Fall/JAVA/upload/");
    }

}
