/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 *
 * @author quangminh_ctin
 */
@Getter
public class DefaultStringResponse extends ApiResponse<ApiError> {

    @Schema(name = "Mã code", description = "Mã code")
    private String code;

    public DefaultStringResponse(String code) {
        super();
        this.code = code;
    }

    public DefaultStringResponse(ApiError errorCode) {
        super(errorCode);
    }
}
