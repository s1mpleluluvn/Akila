package com.akila.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Response of an API call.
 *
 * @param <TError> Type of error.
 */
@Getter
public abstract class ApiResponse<TError extends ApiError> {

    @Schema(name = "_error", description = "Error information. Null if the request is successful.")
    private final TError _error;

    /**
     * Initialize a successful response.
     */
    public ApiResponse() {
        this._error = null;
    }

    /**
     * Initialize a failed response with an error.
     *
     * @param error Error.
     */
    public ApiResponse(TError error) {
        this._error = error;
    }
}
