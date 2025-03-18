package com.akila.entity;

import com.akila.entity.data.Notification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
public class NotificationEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private CustomerEntity customer;

    private String title;

    @Column(length = 300)
    private String content;

    private String link;

    private String icon;

    private String color;

    private boolean isRead;

    private boolean isDeleted;

    private boolean isImportant;

    public Notification toData() {
        return Notification.builder()
                .id(this.id)
                .customer(this.customer.toData())
                .title(this.title)
                .content(this.content)
                .link(this.link)
                .icon(this.icon)
                .color(this.color)
                .isRead(this.isRead)
                .isDeleted(this.isDeleted)
                .isImportant(this.isImportant)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
