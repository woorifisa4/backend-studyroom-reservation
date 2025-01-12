package com.woorifisa.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO: 프로토타입 개발을 위해 임시로 CSRF 설정 비활성화 및 CORS 설정을 모두 허용하는 설정을 적용.
        //  추후 Session 방식과 JWT 방식 중 선택하여 적용할 경우, 해당 방식에 맞게 설정을 변경할 필요가 있음.

        return http
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
                .authorizeHttpRequests(request ->
                        request.anyRequest().permitAll() // 임시) 모든 요청에 대해 인증 불필요.
                )
                .build();

    }

}
