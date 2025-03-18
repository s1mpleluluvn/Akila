/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.entity;

import com.akila.entity.data.FirebaseToken;
import com.akila.type.DeviceType;
import com.akila.type.IdentifyDeviceTokenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

/**
 * @author minh
 */
@Entity
@Table
@Getter
@Setter
public class FirebaseTokenEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300)
    private String token;

    @Enumerated(EnumType.STRING)
    private IdentifyDeviceTokenType type;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private String uuid;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private CustomerEntity customer;

    private String fireBaseToken;

    private String ipAddress;


    public FirebaseToken toData() {
        return FirebaseToken.builder()
                .id(this.id)
                .token(this.token)
                .type(this.type)
                .deviceType(this.deviceType)
                .uuid(this.uuid)
                .customer(this.customer.toData())
                .fireBaseToken(this.fireBaseToken)
                .ipAddress(this.ipAddress)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
