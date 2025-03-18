/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest;

/**
 * @author quangminh
 */
public enum ApiCode {
    POST_NOT_FOUND("Post not found"),
    UNKNOWN_ERROR("Unknown error");
    public final String message;

    private ApiCode(String message) {
        this.message = message;
    }
}
