/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.entity;

import com.akila.entity.data.Customer;
import com.akila.type.AccountStatus;
import com.akila.type.GenderType;

import java.io.Serializable;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * @author minh
 */
@Entity
//indexing the userName column
@Table(indexes = {
        @Index(name = "customer_user_name_index", columnList = "userName", unique = true),
        @Index(name = "customer_email_index", columnList = "email", unique = true)
})
@Getter
@Setter
public class CustomerEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("Username")
    @Column(unique = true)
    private String userName;

    @Comment("Password hash of customer")
    private String passwordHash;

    @Comment("Gender of customer")
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Comment("Email of customer")
    @Column(unique = true)
    private String email;

    @Comment("Address of customer")
    private String address;

    @Comment("Status of customer")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Comment("Activated at")
    private LocalDateTime activatedAt;

    @Comment("Image URL")
    private String imageUrl;

    // This method is used to convert the entity to a data object
    public Customer toData() {
        return Customer.builder()
                .id(id)
                .userName(userName)
                .passwordHash(passwordHash)
                .status(status)
                .email(email)
                .activatedAt(activatedAt)
                .imageUrl(imageUrl)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

    // This method is used to convert the entity to a data object without the password hash
    public Customer toDataOnly() {
        return Customer.builder()
                .id(id)
                .userName(userName)
                .email(email)
                .imageUrl(imageUrl)
                .build();
    }
}
