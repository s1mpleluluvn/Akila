/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.entity.data;

import com.akila.type.AccountStatus;
import com.akila.type.GenderType;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * @author minh
 */
@Builder
@Data
public class Customer {

    private Long id;

    private String userName;

    private String passwordHash;

    private String email;

    private GenderType gender;

    private String address;

    private AccountStatus status;

    private LocalDateTime activatedAt;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
