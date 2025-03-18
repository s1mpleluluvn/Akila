/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest;

import lombok.Getter;

/**
 *
 * @author Minh
 */
@Getter
public class OneResponse<T> extends ApiResponse<ApiError> {

    T result;

    public OneResponse(T result) {
        this.result = result;
    }

    public OneResponse(ApiError error) {
        super(error);
    }
}
