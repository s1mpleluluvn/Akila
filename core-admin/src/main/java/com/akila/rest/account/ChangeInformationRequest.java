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
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author minh
 */
@Getter
@Setter
public class ChangeInformationRequest {

    @Schema(description = "ID (Sử dụng nếu dùng khi thay đổi ở admin)", example = "1")
    private Long id;

    @Schema(description = "Email", example = "s1mpleluluvn@gmail.com", required = true)
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Số điện thoại", example = "0346814796", required = true)
    @Pattern(regexp = "^0\\d{9}$")
    @NotBlank
    private String phoneNumber;

    @Schema(description = "Tên", example = "Minh", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Giới tính", example = "FEMALE", required = true)
    @NotNull
    private GenderType gender;

}
