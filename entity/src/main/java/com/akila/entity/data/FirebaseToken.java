package com.akila.entity.data;

import com.akila.type.DeviceType;
import com.akila.type.IdentifyDeviceTokenType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;


@Data
@Builder
public class FirebaseToken {

    private Long id;

    private String token;

    private IdentifyDeviceTokenType type;

    private DeviceType deviceType;

    private String uuid;

    private Customer customer;

    private String fireBaseToken;

    private String ipAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
