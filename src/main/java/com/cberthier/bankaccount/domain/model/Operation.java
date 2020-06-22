package com.cberthier.bankaccount.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_operation_account_id"), nullable = false)
    private Account account;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private OperationTypeEnum operationType;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(nullable = false)
    private double accountBalance;

    //Default Hibernate constructor
    public Operation() {
    }

    public Operation(Account account, double amount, OperationTypeEnum operationType, LocalDateTime date, double accountBalance) {
        this.account = account;
        this.amount = amount;
        this.operationType = operationType;
        this.date = date;
        this.accountBalance = accountBalance;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }

    public OperationTypeEnum getOperationType() {
        return operationType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getAccountBalance() {
        return accountBalance;
    }
}
