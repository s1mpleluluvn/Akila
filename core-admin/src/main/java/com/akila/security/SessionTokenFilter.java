package com.akila.security;

import com.akila.config.ApiConfig;
import com.akila.redis.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionTokenFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Get authorization header and validate
        var token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
            var authResult = this.authService.authenticateToken(token);
            if (authResult.isPresent()) {
                var auth = authResult.get();
                List<String> permissions = new ArrayList<>();
                permissions.addAll(ApiConfig.PERMISSIONS.keySet());

                var authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                var authentication = new UsernamePasswordAuthenticationToken(auth.getUserName(), auth.getEmail(), authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
