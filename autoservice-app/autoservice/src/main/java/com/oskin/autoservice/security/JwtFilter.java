package com.oskin.autoservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oskin.autoservice.exception.ExceptionResponse;
import com.oskin.autoservice.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            if (jwt.isEmpty() || jwt.split("\\.").length != 3) {
                response.setStatus(401);
                response.setContentType("application/json");
                ExceptionResponse exception = new ExceptionResponse(401, "Неверный jwt токен");
                response.getWriter().write(mapper.writeValueAsString(exception));
                return;
            }
            try {
                username = jwtUtils.getUsername(jwt);
            } catch (SignatureException e) {
                response.setStatus(401);
                response.setContentType("application/json");
                ExceptionResponse exception = new ExceptionResponse(401, "Неверный jwt токен");
                response.getWriter().write(mapper.writeValueAsString(exception));
                return;
            } catch (ExpiredJwtException e) {
                response.setStatus(401);
                response.setContentType("application/json");
                ExceptionResponse exception = new ExceptionResponse(401, "Время сессии истекло");
                response.getWriter().write(mapper.writeValueAsString(exception));
                return;
            }
        } else {
            response.setStatus(403);
            response.setContentType("application/json");
            ExceptionResponse exception = new ExceptionResponse(403, "Пустой jwt токен");
            response.getWriter().write(mapper.writeValueAsString(exception));
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
