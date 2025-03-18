package com.akila.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
public class ApiError {

    @Schema(description = "error code")
    private final String errorCode;

    @Schema(description = "message")
    private final String message;

    public ApiError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ApiError(String errorCode) {
        if (EnumUtils.isValidEnumIgnoreCase(ApiCode.class, errorCode)) {
            ApiCode apiCode = ApiCode.valueOf(errorCode.toUpperCase());
            this.errorCode = errorCode;
            this.message = apiCode.message;
        } else {
            if (!StringUtils.isBlank(errorCode)) {
                this.errorCode = errorCode;
                this.message = errorCode;
            } else {
                var apiCode = ApiCode.UNKNOWN_ERROR;
                this.errorCode = apiCode.name();
                this.message = apiCode.message;
            }
        }
    }
}
