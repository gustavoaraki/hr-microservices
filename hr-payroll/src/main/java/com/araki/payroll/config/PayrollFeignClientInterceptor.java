package com.araki.payroll.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.util.Collection;
import java.util.Collections;

public class PayrollFeignClientInterceptor implements RequestInterceptor {

    private OAuth2AuthorizedClientManager manager;

    public PayrollFeignClientInterceptor(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }

    @Override
    public void apply(RequestTemplate template) {
        OAuth2AuthorizedClient client = this.manager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId("keycloak").principal(createPrincipal()).build());
        String accessToken = client.getAccessToken().getTokenValue();
        template.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

    private Authentication createPrincipal() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptySet();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return this;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public String getName() {
                return "keycloak";
            }
        };
    }
}