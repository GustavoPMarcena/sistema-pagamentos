package com.team.charge_proxy.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsaasFeignConfig {

    @Bean
    public RequestInterceptor asaasAuthInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(
                    "Authorization",
                    "Bearer ${API_KEY}"
            );
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}