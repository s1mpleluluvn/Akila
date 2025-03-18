package com.akila.rest;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> extends ApiResponse<ApiError> {

    List<T> result;

    public ListResponse(List<T> result) {
        this.result = result;
    }

    public ListResponse(ApiError error) {
        super(error);
    }
}
