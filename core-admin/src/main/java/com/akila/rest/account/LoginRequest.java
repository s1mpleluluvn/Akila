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
 * @author quangminh
 */
@Getter
@Setter
public class LoginRequest {

    @Schema(description = "Tên đăng nhập", example = "admin", required = true)
    @NotBlank
    private String userName;

    @Schema(description = "Mật khẩu đăng nhập", example = "password", required = true)
    @NotBlank
    private String password;

    @Schema(description = "Thiết bị đăng nhập", example = "Chrome 90")
    private String device;

    @Schema(description = "Mã captcha", example = "abcxyz")
    private String captchaToken;

    @Schema(description = "remember me", example = "true")
    private boolean rememberMe;
}
