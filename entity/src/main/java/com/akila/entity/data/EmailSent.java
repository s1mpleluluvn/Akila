package com.akila.entity.data;

import com.akila.type.EmailStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class EmailSent {

    private Long id;

    private String sentTo;

    private String sentFrom;

    private String subject;

    private String content;

    private Customer customer;

    private EmailStatus status;

    public LocalDateTime  createdAt;

    public LocalDateTime updatedAt;
}
