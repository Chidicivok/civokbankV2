package com.chidicivok.customerservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(
                        AbstractHttpConfigurer::disable
                )
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers("/api/internal/customer/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/admin/customer/**").hasAnyRole("OWNER", "AUDITOR", "STAFF")
                                .requestMatchers(HttpMethod.POST, "/api/admin/customer/**").hasAnyRole("OWNER", "STAFF")
                                .requestMatchers(HttpMethod.PATCH, "/api/admin/customer/**").hasAnyRole("OWNER", "STAFF")
                                .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        oAuth2 -> oAuth2
                                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();


    }


    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String secretPhrase) {

        SecretKey secretKey = new SecretKeySpec(secretPhrase.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();

        authorities.setAuthoritiesClaimName("role");
        authorities.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(authorities);

        return converter;
    }


}
