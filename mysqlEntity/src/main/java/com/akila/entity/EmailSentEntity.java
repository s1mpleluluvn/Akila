package com.akila.entity;

import com.akila.entity.data.EmailSent;
import com.akila.type.EmailStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
public class EmailSentEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sentTo;

    private String sentFrom;

    private String subject;

    private String content;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private CustomerEntity customer;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    public EmailSent toData() {
        return EmailSent.builder()
                .id(this.id)
                .sentTo(this.sentTo)
                .sentFrom(this.sentFrom)
                .subject(this.subject)
                .content(this.content)
                .customer(this.customer.toData())
                .status(this.status)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
