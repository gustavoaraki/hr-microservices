package com.araki.hrgateway.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
public class KeyCloakConfig {

    @Value("${keycloak-client.server-url}")
    public String serverUrl;

    @Value("${keycloak-client.realm}")
    public String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    public String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    public String clientSecret;

    @Value("${user.admin}")
    private String userName;

    @Value("${password.admin}")
    private String password;

    public KeyCloakConfig() {

    }

    public Keycloak getBuilder() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .username(userName)
                .password(password)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(new ResteasyClientBuilder()
                        .connectionPoolSize(10)
                        .build()
                )
                .build();
    }

    public String getRealm(){
        return realm;
    }
}
