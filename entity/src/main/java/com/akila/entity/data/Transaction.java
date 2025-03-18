package com.akila.entity.data;


import com.akila.type.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
public class Transaction {

    private Long id;

    private String transactionId;

    private BigDecimal amount;

    private BigDecimal fee;

    private TransactionStatus status;

    private String description;

    private Account fromAccount;

    private Account toAccount;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
