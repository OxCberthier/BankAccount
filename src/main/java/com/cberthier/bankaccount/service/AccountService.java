package com.cberthier.bankaccount.service;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.Account;

public interface AccountService {

    Account addOperation(OperationCommand operationCommand);
}
