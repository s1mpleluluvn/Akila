/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest.account;

import com.akila.type.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RegisterRequest {

    @Schema(description = "userName", example = "admin", required = true)
    @NotBlank
    @Size(min = 5, max = 20)
    private String userName;

    @Schema(description = "password", example = "password", required = true)
    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[!@#$&*])(?=.*[0-9])(?=.*[A-z]).{8,20}$")
    private String password;

    @Schema(description = "Email", example = "s1mpleluluvn@gmail.com", required = true)
    @NotBlank
    @Email
    private String email;
//
//    @Schema(description = "address", example = "Minh", required = true)
//    private String address;
//
//    @Schema(description = "Giới tính", example = "FEMALE", required = true)
//    @NotNull
//    private GenderType gender;
}
