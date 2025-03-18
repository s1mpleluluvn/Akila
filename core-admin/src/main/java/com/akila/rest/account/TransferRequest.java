package com.akila.rest.account;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

    private String fromAccount;

    private String toAccount;

    private BigDecimal amount;

    private String description;
}
