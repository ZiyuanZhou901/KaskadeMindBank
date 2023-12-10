package com.kaskademindbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author ZiyuanZhou
 */
@SpringBootApplication
@EnableConfigurationProperties
public class KaskadeMindBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(KaskadeMindBankApplication.class, args);
    }

}
