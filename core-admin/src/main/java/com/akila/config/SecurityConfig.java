package com.akila.config;

import com.akila.security.SessionTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SessionTokenFilter sessionTokenFilter;

    private final String[] patterns = new String[]{
            "/login/**",
            "/post/getAllPost",
            "/post/getPostById",
            "/login/**",
            "/actuator/health",
            "/adminRole/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/ws",
            "/ws/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/login/**",
            "/api/actuator/health",
            "/api/adminRole/**",
            "/api/ws",
            "/api/v3/api-docs",
            "/api/v3/api-docs/**",
            "/api/v3/api-docs.yaml",
            "/api/swagger-ui.html",
            "/api/swagger-ui/**",
            "/api/swagger-resources",
            "/api/swagger-resources/**",
            "/api/webjars/**"
    };

    @Value("${system.environment}")
    private String environment;

    public SecurityConfig(SessionTokenFilter sessionTokenFilter) {
        this.sessionTokenFilter = sessionTokenFilter;
    }

    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authz) -> authz
//                .anyRequest().authenticated())
//                .addFilterAfter(sessionTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .httpBasic(withDefaults());
//        return http.build();
//    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authz) -> authz
//                .requestMatchers(patterns).permitAll().anyRequest().authenticated())
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterAfter(sessionTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .httpBasic(withDefaults());
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(patterns).permitAll().anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(sessionTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults())
                .formLogin(form -> form.disable());
        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(patterns);
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and()
//                //product
//                ////.csrfTokenRepository(csrf).ignoringAntMatchers(patterns).and()
//                .authorizeRequests()
//                .antMatchers("/login/**").permitAll()
//                .antMatchers("/swagger-ui/**").permitAll()
//                .antMatchers("/v3/api-docs**").permitAll()
//                .antMatchers("/v3/api-docs/**").permitAll()
//                //Actuator
//                .antMatchers("/actuator/health").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterAfter(this.sessionTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                //.addFilterAfter(new CsrfGrantingFilter(), SessionManagementFilter.class)
//                // this disables session creation on Spring Security
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
////        if (environment.equalsIgnoreCase("PROD")) {
////            var csrf = CookieCsrfTokenRepository.withHttpOnlyFalse();
////            csrf.setSecure(Boolean.TRUE);
////            http.csrf().csrfTokenRepository(csrf).ignoringAntMatchers(patterns).and().addFilterAfter(new CsrfGrantingFilter(), SessionManagementFilter.class);
////        } else {
////            http.csrf().disable();
////        }
//        http.csrf().disable();
//    }
}
