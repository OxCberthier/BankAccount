package com.cberthier.bankaccount.web.result;

import com.cberthier.bankaccount.domain.model.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.stream.Collectors;

public class OperationsResponseEntity {

    public static ResponseEntity<ApiResult> build(Page<Operation> operations) {
        ApiResult apiResult = ApiResult.blank()
                .add("page", operations.getNumber())
                .add("totalPages", operations.getTotalPages())
                .add("totalElements", operations.getTotalElements())
                .add("size", operations.getSize())
                .add("operations", operations.stream()
                        .map(operation -> ApiResult.blank()
                                .add("date", operation.getDate())
                                .add("amount", operation.getAmount())
                                .add("type", operation.getOperationType())
                                .add("accountBalance", operation.getAccountBalance())
                        )
                        .collect(Collectors.toList())
                );

        return Result.ok(apiResult);
    }
}
