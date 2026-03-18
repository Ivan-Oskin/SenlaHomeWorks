package com.oskin.config;

import com.oskin.autoservice.exception.CustomAccessDeniedHandler;
import com.oskin.autoservice.security.UserDetailService;
import com.oskin.autoservice.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailService userDetailService;
    private final JwtFilter jwtFilter;

    @Autowired
    SecurityConfig(UserDetailService userDetailService, JwtFilter jwtFilter) {
        this.userDetailService = userDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.
                csrf(CsrfConfigurer::disable).
                authorizeHttpRequests(
                        aut -> aut
                                .requestMatchers("/autoservice/auth").permitAll()
                                .requestMatchers("/autoservice/reg").permitAll()
                                .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exc -> exc.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailService)
                .build();

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}