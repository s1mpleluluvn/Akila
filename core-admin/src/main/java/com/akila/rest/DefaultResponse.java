/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author minhtrinh_monex
 */
@Getter
@Setter
public class DefaultResponse extends ApiResponse<ApiError> {

    @Schema(name = "result", description = "Kết quả")
    private String result;

    public DefaultResponse() {
        super();
        this.result = "Success";
    }

    public DefaultResponse(String errorCode) {
        super(new ApiError(errorCode));
        this.result = "Error";
    }
}
