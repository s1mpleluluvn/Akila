/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.redis;

import com.akila.util.SignatureUtil;
import com.akila.util.TokenUtil;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author minh
 */
@Component
public class AuthService {

    @Value("${auth.session.expirationTimeMinutes}")
    private int expirationTimeMinutes;

    @Autowired
    private RedisAuthSessionStorageService redisAuthSessionStorageService;

    public String addAuthenticationSession(AuthenticationSession authSession, boolean rememberMe) {
        if (authSession.getUserName() == null) {
            throw new IllegalArgumentException("username must not be null");
        }
        if (authSession.getUserName().contains(":")) {
            throw new IllegalArgumentException("username must not contains ':'");
        }

        var uuid = UUID.randomUUID();

        // create token with format <username>:<uuid>
        var token = String.format("%s:%s", authSession.getUserName(), uuid);

        try {
            // session storage key with format <accountId>:<hmacHexString>
            var sessionStorageKey = SignatureUtil.calculateSessionStorageKey(authSession.getUserName(), uuid);

            // store session
            var now = OffsetDateTime.now();
            if (rememberMe) {
                this.redisAuthSessionStorageService.putSession(sessionStorageKey, authSession, now.plusDays(30));
            } else {
                this.redisAuthSessionStorageService.putSession(sessionStorageKey, authSession, now.plusMinutes(this.expirationTimeMinutes));
            }

            this.redisAuthSessionStorageService.putSession(sessionStorageKey, authSession, now.plusMinutes(this.expirationTimeMinutes));

            // return session token
            return token;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error adding authentication session", e);
        }
    }

    public Optional<AuthenticationSession> authenticateToken(String token) {
        var pair = SignatureUtil.parseAuthToken(token);
        if (pair == null) {
            return Optional.empty();
        }
        var username = pair.getValue0();
        try {
            var storageKey = SignatureUtil.calculateSessionStorageKey(username, pair.getValue1());
            var session = this.redisAuthSessionStorageService.getSession(storageKey);
            if (session == null) {
                return Optional.empty();
            }

            return Optional.of(session);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // invalid HMAC calculation, might be due to invalid uuid/id
            return Optional.empty();
        }
    }

    public Map<String, AuthenticationSession> getUserAuthenticationSessions(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username must not be null");
        }

        var prefix = String.format("%s:", username);
        return this.redisAuthSessionStorageService.getSessions(prefix);
    }

    public void signOutSession(String token) {
        var pair = SignatureUtil.parseAuthToken(token);
        if (pair == null) {
            return;
        }

        try {
            var storageKey = SignatureUtil.calculateSessionStorageKey(pair.getValue0(), pair.getValue1());
            this.redisAuthSessionStorageService.deleteSession(storageKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // invalid HMAC calculation, might be due to invalid uuid/id, ignore
        }
    }

    public void signOutSessionFromKey(String key) {
        if (key == null) {
            return;
        }
        try {
            this.redisAuthSessionStorageService.deleteSession(key);
        } catch (Exception e) {
//            log.info(e.getMessage());
        }
    }

    public void signOutAllSessions(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("username must not be null");
        }

        this.redisAuthSessionStorageService.deleteAllSessionsMatch(userName);
    }

    public String getUserNameFromToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken)) {
            var authResult = this.authenticateToken(TokenUtil.getTokenFromAuthorization(bearerToken));
            if (authResult.isPresent()) {
                return authResult.get().getUserName();
            }
        }
        return null;
    }
}
