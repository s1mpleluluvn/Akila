/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.security;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;

/**
 *
 * @author minh
 */
public class CsrfGrantingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        CsrfToken csrf = (CsrfToken) servletRequest.getAttribute(CsrfToken.class.getName());
        String token = csrf.getToken();
        if (token != null && isAuthenticating(servletRequest)) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
//            Cookie cookie = new Cookie("CSRF-TOKEN", token);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//            response.setHeader("Access-Control-Allow-Origin",request.getHeader(""));
//            response.setHeader("Access-Control-Allow-Origin", "http://10.20.30.153:9699");
//            response.setHeader("Access-Control-Allow-Origin", "http://10.20.30.153:9699");
//            response.setHeader("Access-Control-Allow-Credentials", "http://10.20.30.153:9699");
            response.setHeader("XSRF-TOKEN", token);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isAuthenticating(ServletRequest servletRequest) {
        return true;
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        return request.getRequestURI().startsWith("/login");
    }

    @Override
    public void destroy() {
    }
}
