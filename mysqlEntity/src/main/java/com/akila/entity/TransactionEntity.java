/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.entity;

import com.akila.entity.data.Transaction;
import com.akila.type.TransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author minh
 */
@Entity
//indexing the transactionId column
@Table(indexes = {
        @Index(name = "transaction_id_index", columnList = "transactionId", unique = true)
})
@Getter
@Setter
public class TransactionEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Comment("Transaction ID")
    private String transactionId;

    @Comment("Transaction Amount")
    private BigDecimal amount;

    @Comment("Transaction Fee")
    private BigDecimal fee;

    @Comment("Transaction Status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Comment("Transaction Description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fromAccountId", nullable = false)
    private AccountEntity fromAccount;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "toAccountId", nullable = false)
    private AccountEntity toAccount;

    @Comment("Completed At")
    private LocalDateTime completedAt;

    //to data
    public Transaction toData() {
        return Transaction.builder()
                .id(id)
                .transactionId(transactionId)
                .amount(amount)
                .fee(fee)
                .status(status)
                .description(description)
                .fromAccount(fromAccount != null ? fromAccount.toData() : null)
                .toAccount(toAccount != null ? toAccount.toData() : null)
                .completedAt(completedAt)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
