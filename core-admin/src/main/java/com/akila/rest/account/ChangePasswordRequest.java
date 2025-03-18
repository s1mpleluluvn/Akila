/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author minh
 */
@Getter
@Setter
public class ChangePasswordRequest {

    @Schema(description = "Tên đăng nhập (cần thiết khi đổi mật khẩu lần đầu)", example = "admin")
    private String userName;

    @Schema(description = "Mật khẩu đăng nhập", example = "password", required = true)
    @NotBlank
    private String password;

    @Schema(description = "Mật khẩu mới", example = "ctin@123456", required = true)
    @NotBlank
    @Size(min = 6, max = 50)
    //@Pattern(regexp = "^(?=.*[!@#$&*])(?=.*[0-9])(?=.*[A-z]).{10,50}$")
    private String newPassword;

    @Schema(description = "Thiết bị đăng nhập", example = "Chrome 90")
    private String device;

    @Schema(description = "Mã captcha", example = "abcxyz")
    private String captchaToken;
}
