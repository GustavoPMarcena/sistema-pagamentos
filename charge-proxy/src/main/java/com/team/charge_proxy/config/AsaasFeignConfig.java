package com.team.charge_proxy.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign configuration for calls to ASAAS.
 * Adds JSON content-type and authentication headers.
 *
 * ASAAS accepts the API key through header 'access_token'.
 * To keep compatibility with previous code, we also set 'Authorization: Bearer <key>'.
 */
@Configuration
public class AsaasFeignConfig {

    @Bean
    public RequestInterceptor contentTypeJson() {
        return requestTemplate -> requestTemplate.header("Content-Type", "application/json");
    }
}
