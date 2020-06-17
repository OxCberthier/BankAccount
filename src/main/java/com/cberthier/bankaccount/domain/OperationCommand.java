package com.cberthier.bankaccount.domain;

import com.cberthier.bankaccount.domain.model.OperationTypeEnum;

public class OperationCommand {

    private Long accountId;

    private double amount;

    private OperationTypeEnum operationType;

    public OperationCommand(Long accountId, double amount, OperationTypeEnum operationType) {
        this.accountId = accountId;
        this.amount = amount;
        this.operationType = operationType;
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
