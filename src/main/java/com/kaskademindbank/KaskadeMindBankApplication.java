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
        /*
         *  资源映射路径
         *  addResourceHandler:访问映射路径
         *  addResourceLocations:资源的绝对路径
         */
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/23Fall/JAVA/upload/");
    }

}
