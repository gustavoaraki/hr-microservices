package com.araki.hrgateway.config;


import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private static final String[] OPERATOR = { "/hr-worker/**" };

    private static final String[] ADMIN = { "/hr-payroll/**", "/hr-user/**", "/actuator/**", "/hr-worker/actuator/**", "/hr-oauth/actuator/**" };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, OPERATOR).hasAnyRole("app-user", "app-admin")
                .pathMatchers(ADMIN).hasRole("app-admin")
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                    .jwtAuthenticationConverter(grantedAuthoritiesExtractor());;
        return http.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        CustomJwtAuthenticationConverter extractor = new CustomJwtAuthenticationConverter();
        return new ReactiveJwtAuthenticationConverterAdapter(extractor);
    }

    static class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        public CustomJwtAuthenticationConverter() {
        }

        @Override
        public AbstractAuthenticationToken convert(@NotNull final Jwt jwt) {
            Collection<GrantedAuthority> authorities = Stream
                    .concat(defaultGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream())
                    .collect(Collectors.toSet());

            return new JwtAuthenticationToken(jwt, authorities);
        }

        private static Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) {
            Map<String, Object> claims = jwt.getClaims();
            JSONObject realmAccess = (JSONObject) claims.get("realm_access");
            JSONArray roles = (JSONArray) realmAccess.get("roles");
            if (roles != null)
                return roles
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet());
            return Collections.emptySet();
        }
    }
}
