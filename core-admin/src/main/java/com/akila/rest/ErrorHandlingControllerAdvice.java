package com.akila.rest;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    static class BadInputApiError extends ApiError {

        @Getter
        private final Map<String, List<String>> errors;

        public BadInputApiError() {
            super("BAD_INPUT");
            this.errors = new HashMap<>();
        }
    }

    static class BadInputApiResponse extends ApiResponse<BadInputApiError> {

        public BadInputApiResponse(BadInputApiError error) {
            super(error);
        }
    }

    static class MaxUploadSizeExceededResponse extends ApiResponse<ApiError> {

        @Getter
        private final String message;

        public MaxUploadSizeExceededResponse(String message) {
            super(new ApiError("MAX_UPLOAD_SIZE_EXCEEDED_ERROR"));
            this.message = message;

        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    BadInputApiResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var error = new BadInputApiError();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            var currentErrors = error.getErrors().computeIfAbsent(fieldError.getField(), k -> new ArrayList<>());
            currentErrors.add(fieldError.getDefaultMessage());
        }

        return new BadInputApiResponse(error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    MaxUploadSizeExceededResponse onMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return new MaxUploadSizeExceededResponse("The field file exceeds its maximum permitted.");
    }
}
