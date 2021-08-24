package com.araki.payroll.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
public class FeignClientConfig {

    @Bean
    public PayrollFeignClientInterceptor repositoryClientOAuth2Interceptor(@Lazy OAuth2AuthorizedClientManager manager) {
        return new PayrollFeignClientInterceptor(manager);
    }
}