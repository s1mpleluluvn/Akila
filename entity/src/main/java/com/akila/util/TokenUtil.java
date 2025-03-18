/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.util;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author minh
 */
public class TokenUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Get token from authorization
     *
     * @param token: Bearer token
     * @return token
     */
    public static String getTokenFromAuthorization(String token) {
        return StringUtils.isNotBlank(token) && token.startsWith(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : null;
    }
}
