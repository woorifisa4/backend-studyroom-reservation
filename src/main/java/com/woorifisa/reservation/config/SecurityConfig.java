package com.woorifisa.reservation.config;

import com.woorifisa.reservation.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO: 프로토타입 개발을 위해 임시로 CSRF 설정 비활성화 및 CORS 설정을 모두 허용하는 설정을 적용.
        //  CORS origin 수정 및 특정 URL에 대한 접근 제한 등의 설정 필요.

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
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/users/login", "/users/signup", "/users/token/refresh").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 사용으로 세션 비활성화.
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
