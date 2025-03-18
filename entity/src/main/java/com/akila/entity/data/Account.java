package com.akila.entity.data;

import com.akila.type.AccountStatus;
import com.akila.type.CurrencyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {

    private Long id;

    private Customer customer;

    private String accountNumber;

    private String accountName;

    private BigDecimal balance;

    private AccountStatus status;

    private LocalDateTime activatedAt;

    private LocalDateTime closedAt;

    private CurrencyType currency;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
