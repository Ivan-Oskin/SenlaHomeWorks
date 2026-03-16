package com.oskin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.
                csrf(CsrfConfigurer::disable).
                authorizeHttpRequests(
                        aut -> aut
                                .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("ADMIN", "USER")
                        ).httpBasic(httpBasic -> {}).build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
