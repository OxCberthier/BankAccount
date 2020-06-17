package com.cberthier.bankaccount.service;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.Account;
import com.cberthier.bankaccount.domain.model.AccountNotFoundException;

public interface AccountService {

    Account addOperation(OperationCommand operationCommand) throws AccountNotFoundException;
}
