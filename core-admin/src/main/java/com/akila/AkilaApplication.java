package com.akila;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication(scanBasePackages = {"com.akila"})
@EnableScheduling
@Log4j2
@EnableAsync
public class AkilaApplication {

    @Value("${allowed.origin}")
    private String allowedOrigin;

    @Value("${system.environment}")
    private String environment;

    private static final Class<AkilaApplication> applicationClass = AkilaApplication.class;

    public static void main(String[] args) {
        // SpringApplication.run(SherryApplication.class, args);
        SpringApplication.run(applicationClass, args);
    }

//    @Bean
//    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
//        return CookieSameSiteSupplier.ofLax();
//        //return CookieSameSiteSupplier.ofNone();
//    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        if (environment.equalsIgnoreCase("DEV")) {
            config.addAllowedOrigin("*");
            config.setAllowCredentials(false);
        } else {
            config.setAllowCredentials(true);
            String[] lstOrigin = allowedOrigin.split(",");
            for (var origin : lstOrigin) {
                config.addAllowedOrigin(origin);
            }
        }
        config.addAllowedHeader("*");
        config.addExposedHeader("XSRF-TOKEN");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
//        final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
        return new CorsFilter(source);
    }
}
