package com.akila.entity.data;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Notification {

    private Long id;

    private Customer customer;

    private String title;

    private String content;

    private String link;

    private String icon;

    private String color;

    private boolean isRead;

    private boolean isDeleted;

    private boolean isImportant;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
