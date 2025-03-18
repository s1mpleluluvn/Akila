package com.akila.entity;

import com.akila.entity.data.Account;
import com.akila.type.AccountStatus;
import com.akila.type.CurrencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;


/**
 * @author minh
 */
@Entity
//indexing the accountNumber column
@Table(indexes = {
        @Index(name = "account_number_index", columnList = "accountNumber", unique = true)
})
@Getter
@Setter
public class AccountEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private CustomerEntity customer;

    @Comment("Account number")
    @Column(unique = true)
    private String accountNumber;

    @Comment("Account name")
    private String accountName;

    private BigDecimal balance;

    @Comment("Status of account")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Comment("Activated at")
    private LocalDateTime activatedAt;

    @Comment("Closed at")
    private LocalDateTime closedAt;

    @Comment("Currency at")
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @PrePersist
    private void generateAccountNumber() {
        if (this.accountNumber == null) {
            this.accountNumber = generateRandomAccountNumber();
        }
    }

    private String generateRandomAccountNumber() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            accountNumber.append(secureRandom.nextInt(10));
        }
        return accountNumber.toString();
    }

    //to data
    public Account toData() {
        return Account.builder()
                .id(id)
                .customer(customer != null ? customer.toData() : null)
                .accountNumber(accountNumber)
                .accountName(accountName)
                .status(status)
                .activatedAt(activatedAt)
                .closedAt(closedAt)
                .currency(currency)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
