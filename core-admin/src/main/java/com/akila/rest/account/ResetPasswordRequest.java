/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author s1mpl
 */
@Getter
@Setter
public class ResetPasswordRequest {

    @Schema(description = "Id", example = "14796", required = true)
    @NotNull
    private Long id;

    @Schema(description = "Mật khẩu mới", example = "ctin@123456", required = true)
    @NotBlank
    @Size(min = 6, max = 50)
    //@Pattern(regexp = "^(?=.*[!@#$&*])(?=.*[0-9])(?=.*[A-z]).{10,50}$")
    private String newPassword;
}
