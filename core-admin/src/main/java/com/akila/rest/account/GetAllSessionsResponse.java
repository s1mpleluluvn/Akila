/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest.account;

import com.akila.redis.AuthenticationSession;
import com.akila.rest.ApiError;
import com.akila.rest.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Getter;

/**
 *
 * @author s1mpl
 */
@Getter
public class GetAllSessionsResponse extends ApiResponse<ApiError> {

    @Schema(description = "Danh sách các thiết bị đang đăng nhập của một tài khoản")
    private final Map<String, AuthenticationSession> allSessions;

    public GetAllSessionsResponse(Map<String, AuthenticationSession> allSessions) {
        super();
        this.allSessions = allSessions;
    }

    public GetAllSessionsResponse(ApiError error) {
        super(error);
        this.allSessions = null;
    }
}
