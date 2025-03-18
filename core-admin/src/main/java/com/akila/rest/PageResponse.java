package com.akila.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResponse<T> extends ApiResponse<ApiError> {

    @Schema(description = "Dữ liệu phân trang trả về")
    Page<T> page;

    public PageResponse(Page<T> page) {
        this.page = page;
    }

    public PageResponse(ApiError error) {
        super(error);
    }
}
