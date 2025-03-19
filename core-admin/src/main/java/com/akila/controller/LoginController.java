/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akila.controller;

import com.akila.admin.service.CustomerDomainService;
import com.akila.entity.data.Customer;
import com.akila.redis.AuthService;
import com.akila.redis.AuthenticationSession;
import com.akila.rest.DefaultResponse;
import com.akila.rest.account.GetAllSessionsResponse;
import com.akila.rest.account.LoginRequest;
import com.akila.rest.account.LoginResponse;
import com.akila.rest.account.RegisterRequest;
import com.akila.rest.account.SignOutSessionByKeyRequest;
import com.akila.adapter.persistence.restService.EmailService;
import com.akila.adapter.persistence.restService.GoogleCaptchaClientService;
import com.akila.type.AccountStatus;
import com.akila.util.CryptoUtil;
import com.akila.util.JsonUtil;
import com.akila.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sherry
 */
@RestController
@Log4j2
@RequestMapping("login")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "LoginController", description = "Login Controller")
public class LoginController {

    @Autowired
    private CustomerDomainService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${com.akila.init.admin.username}")
    private String adminUsername;

    @Value("${com.akila.init.admin.password}")
    private String adminPassword;

    @Value("${auth.captcha.requireVerification}")
    private boolean captchaVerificationRequired;

    @Autowired
    private GoogleCaptchaClientService googleCaptchaClientService;

    @Autowired
    private EmailService emailService;

    @PostConstruct
    private void initData() {
        this.userService.initAdmin(adminUsername, passwordEncoder.encode(adminPassword));
        this.userService.initAdmin("akilaadmin2", passwordEncoder.encode("akila@123456"));
        this.userService.initAdmin("akilaadmin3", passwordEncoder.encode("akila@123456"));
    }

    @Operation(description = "Sign on")
    @PostMapping("signOn")
    public LoginResponse signOn(@Valid @RequestBody LoginRequest request) throws Exception {
        try {
            if (this.captchaVerificationRequired) {
                if (request.getDevice() != null && request.getDevice().startsWith("Avalonia")) {
                    var captcha = request.getCaptchaToken();
                    request.setCaptchaToken(null);
                    String jsonCheckSum = JsonUtil.getGson().toJson(request);
                    var dataEncrypt = CryptoUtil.deEncryptCustomize(captcha, request.getDevice());
                    if (!jsonCheckSum.equals(dataEncrypt)) {
                        return new LoginResponse("INVALID_CAPTCHA");
                    }
                } else {
                    if (!googleCaptchaClientService.verifyCaptcha(request.getCaptchaToken())) {
                        return new LoginResponse("INVALID_CAPTCHA");
                    }
                }
            }
            var result = userService.findByUserName(request.getUserName());
            if (result != null) {
                if (!this.passwordEncoder.matches(request.getPassword(), result.getPasswordHash())) {
                    return new LoginResponse("LOGIN_ERROR");
                }
                if (result.getStatus() != AccountStatus.ACTIVE) {
                    return new LoginResponse("LOGIN_ERROR");
                }
                LocalDateTime timeout = request.isRememberMe() ? LocalDateTime.now().plusDays(30) : LocalDateTime.now().plusMinutes(30);
                var token = authService.addAuthenticationSession(AuthenticationSession.builder().device(request.getDevice())
                        .userName(request.getUserName())
                        .email(result.getEmail()).name(result.getUserName())
                        .signOnAt(LocalDateTime.now()).timeout(timeout).build(), request.isRememberMe());
                return new LoginResponse(request.getUserName(), token, timeout, result.getEmail());
            } else {
                return new LoginResponse("LOGIN_ERROR");
            }
        } catch (Exception ex) {
            return new LoginResponse("LOGIN_ERROR");
        }
    }

    @Operation(description = "register")
    @PostMapping("register")
    public DefaultResponse register(@Valid @RequestBody RegisterRequest request) throws Exception {
        //var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            userService.register(Customer.builder().userName(request.getUserName())
                    .email(request.getEmail())
                    .passwordHash(passwordEncoder.encode(request.getPassword())).build());
        } catch (Exception ex) {
            return new DefaultResponse("DATA_ALREADY_EXISTS");
        }
        return new DefaultResponse();
    }

    @Operation(description = "Get all sessions")
    @GetMapping("getAllSessions")
    public GetAllSessionsResponse getAllSessions() {
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        var allSessions = this.authService.getUserAuthenticationSessions(userName);
        return new GetAllSessionsResponse(allSessions);
    }

    @Operation(description = "Sign out")
    @PostMapping("signOutSession")
    public DefaultResponse signOutSession(@Parameter(hidden = true)
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        this.authService.signOutSession(TokenUtil.getTokenFromAuthorization(token));
        return new DefaultResponse();
    }

    @Operation(description = "Sign out by key")
    @PostMapping("signOutSessionByKey")
    public DefaultResponse signOutSessionByKey(@Valid
            @RequestBody SignOutSessionByKeyRequest request) {
        this.authService.signOutSession(request.getKey());
        return new DefaultResponse();
    }

    @Operation(description = "Sign out all sessions")
    @GetMapping("signOutAllSession")
    public DefaultResponse signOutAllSession() {
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        this.authService.signOutAllSessions(userName);
        return new DefaultResponse();
    }
}
