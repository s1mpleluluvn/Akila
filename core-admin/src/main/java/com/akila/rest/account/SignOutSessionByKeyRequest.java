/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author s1mpl
 */
@Getter
@Setter
public class SignOutSessionByKeyRequest {

    @Schema(description = "Token xác thực được trả về khi đăng nhập thành công", example = "bob:123456", required = true)
    @NotBlank
    private String key;
}
