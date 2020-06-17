package com.cberthier.bankaccount.web.payload;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.OperationTypeEnum;
import com.sun.istack.NotNull;

public class OperationPayload {

    @NotNull
    private Long accountId;

    @NotNull
    private double amount;

    @NotNull
    private OperationTypeEnum operationType;

    public OperationPayload(Long accountId, double amount, OperationTypeEnum operationType) {
        this.accountId = accountId;
        this.amount = amount;
        this.operationType = operationType;
    }

    public OperationCommand toCommand() {
        return new OperationCommand(accountId, amount, operationType);
    }

    public Long getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public OperationTypeEnum getOperationType() {
        return operationType;
    }
}
