package com.chidicivok.notificationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Value("${jwt.secret}")
    String secretPhrase;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(
                        AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests(
                        requests -> requests
                                // login is here so no jwt yet so permit all
                                .requestMatchers("/api/notification/send").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        oAuth2 -> oAuth2
                                .jwt(jwt -> {})
                )
                .build();


    }


    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String secretPhrase) {

        SecretKey secretKey = new SecretKeySpec(secretPhrase.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

//
//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//
//        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
//
//        authorities.setAuthoritiesClaimName("role");
//        authorities.setAuthorityPrefix("ROLE_");
//
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//
//        converter.setJwtGrantedAuthoritiesConverter(authorities);
//
//        return converter;
//    }


}
