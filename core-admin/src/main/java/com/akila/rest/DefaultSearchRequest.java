/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author s1mpl
 */
@Getter
@Setter
public class DefaultSearchRequest {

    @Schema(description = "Tìm kiếm", example = "14796")
    private String searchKey;
}
