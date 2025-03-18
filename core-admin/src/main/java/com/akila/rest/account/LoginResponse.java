/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest.account;

import com.akila.rest.ApiError;
import com.akila.rest.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 * @author s1mpl
 */
@Getter
@Setter
public class LoginResponse extends ApiResponse<ApiError> {

    @Schema(description = "user name")
    private String userName;

    @Schema(description = "token")
    private String authToken;

    @Schema(description = "timeout")
    private LocalDateTime timeout;
    
    private String email;

    public LoginResponse(String userName, String authToken, LocalDateTime timeout,String email) {
        super();
        this.userName = userName;
        this.authToken = authToken;
        this.timeout = timeout;
        this.email = email;
    }

    public LoginResponse(String userName, String authToken) {
        super();
        this.userName = userName;
        this.authToken = authToken;
    }

    public LoginResponse(String errorCode) {
        super(new ApiError(errorCode));
    }
}
