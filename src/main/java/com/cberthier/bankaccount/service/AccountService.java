package com.cberthier.bankaccount.service;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    Account addOperation(OperationCommand operationCommand) throws AccountNotFoundException, InvalidOperationException, InsufficientFundsException;

    Page<Operation> getOperations(Long accountId, Pageable pageable);
}

