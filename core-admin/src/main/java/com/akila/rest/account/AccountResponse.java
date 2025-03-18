/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest.account;

import com.akila.rest.ApiError;
import com.akila.rest.ApiResponse;
import com.akila.entity.data.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Admin
 */
@Getter
@Setter
public class AccountResponse extends ApiResponse<ApiError> {

    @Schema(description = "Thông tin")
    private final Customer account;

    public AccountResponse(Customer account) {
        super();
        this.account = account;
    }

    public AccountResponse(ApiError error) {
        super(error);
        this.account = null;
    }
}
