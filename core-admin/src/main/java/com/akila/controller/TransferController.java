package com.akila.controller;

import com.akila.admin.service.TransactionDomainService;
import com.akila.entity.data.Transaction;
import com.akila.rest.ApiError;
import com.akila.rest.ApiPageable;
import com.akila.rest.OneResponse;
import com.akila.rest.PageResponse;
import com.akila.rest.account.FindRequest;
import com.akila.rest.account.TransferRequest;
import com.akila.util.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("transfer")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "TransferController", description = "Transfer Controller")
public class TransferController {

    @Autowired
    private TransactionDomainService transactionDomainService;

    @Operation(description = "Transfer money between accounts")
    @PostMapping("transfer")
    public OneResponse<Transaction> transfer(@Valid @RequestBody TransferRequest request) {
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            var transaction = transactionDomainService.transferMoney(userName, request.getFromAccount(), request.getToAccount(), request.getAmount(), request.getDescription());
            return new OneResponse<>(transaction);
        } catch (Exception e) {
            log.error("Error in transfer", e);
            return new OneResponse<>(new ApiError("Error in transfer", e.getMessage()));
        }
    }

    //link Transaction
    @ApiPageable
    @Operation(description = "Get transactions")
    @PostMapping("getTransactions")
    public PageResponse<Transaction> getTransactions(@Valid @RequestBody FindRequest request, @Parameter(name = "pageable", hidden = true) @NonNull Pageable pageable) {
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            var transaction = transactionDomainService.getTransactionsByAccountNumber(userName, request.getAccountNumber(), pageable);
            var count = transactionDomainService.countTransactionsByAccountNumber(userName, request.getAccountNumber());
            return new PageResponse<>(PageableExecutionUtils.getPage(transaction, pageable, () -> count));
        } catch (Exception e) {
            log.error("Error in find", e);
            return new PageResponse<>(new ApiError("Error find", e.getMessage()));
        }
    }
}
