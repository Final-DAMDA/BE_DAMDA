package com.damda.back.config;


import com.damda.back.utils.SolapiUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolapiConfig {
    @Value("${solapi.access.key}")
    public String apiKey;

    @Value("${solapi.secret.key}")
    public String secretKey;


    @Bean
    public SolapiUtils solapiUtils(){
        return new SolapiUtils(apiKey,secretKey);
    }

}
