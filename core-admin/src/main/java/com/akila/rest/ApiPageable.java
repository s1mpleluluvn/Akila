package com.akila.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameters({
    @Parameter(schema = @Schema(type = "integer"), name = "page", required = true, example = "0", in = ParameterIn.QUERY, description = "Trang dữ liệu muốn truy vấn (0..N)"),
    @Parameter(schema = @Schema(type = "integer"), name = "size", required = true, example = "20", in = ParameterIn.QUERY, description = "Số lượng bản ghi truy cấp trên mỗi trang"),
    @Parameter(schema = @Schema(type = "string"), name = "sort", example = "property,desc", in = ParameterIn.QUERY, description = "Sắp xếp thứ tự truy vấn với format: property(,asc|desc).")})
public @interface ApiPageable {
}
