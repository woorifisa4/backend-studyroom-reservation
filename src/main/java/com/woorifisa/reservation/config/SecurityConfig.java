package com.woorifisa.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화.
                .csrf(AbstractHttpConfigurer::disable) // CSRF 설정 비활성화.
                .cors(cors -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("*")); // 모든 도메인 허용.
                        config.setAllowedMethods(List.of("*")); // 모든 HTTP 메서드 허용.
                        config.setAllowedHeaders(List.of("*")); // 모든 헤더 허용.
                        return config;
                    };

                    // CORS 설정 적용.
                    cors.configurationSource(source);
                })
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // X-Frame-Options 설정 비활성화.
                )
                .authorizeHttpRequests(request ->
                        request.anyRequest().permitAll() // 임시) 모든 요청에 대해 인증 불필요.
                )
                .build();

    }

}
