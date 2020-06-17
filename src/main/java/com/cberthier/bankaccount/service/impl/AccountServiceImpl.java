package com.cberthier.bankaccount.service.impl;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.Account;
import com.cberthier.bankaccount.domain.model.Operation;
import com.cberthier.bankaccount.domain.repository.AccountCrudRepository;
import com.cberthier.bankaccount.domain.repository.OperationPagingAndSortingRepository;
import com.cberthier.bankaccount.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private AccountCrudRepository accountCrudRepository;

    private OperationPagingAndSortingRepository operationPagingAndSortingRepository;

    public AccountServiceImpl(AccountCrudRepository accountCrudRepository,
                              OperationPagingAndSortingRepository operationPagingAndSortingRepository) {
        this.accountCrudRepository = accountCrudRepository;
        this.operationPagingAndSortingRepository = operationPagingAndSortingRepository;
    }

    @Override
    public Account addOperation(OperationCommand operationCommand) {
        Optional<Account> optionalAccount = accountCrudRepository.findById(operationCommand.getAccountId());
        Account account = optionalAccount.get();

        logger.debug("Add Operation -> Account {}, Amount: {}, Type: {}",
                account.getName(), operationCommand.getAmount(), operationCommand.getOperationType());
        logger.debug("Old balance -> {}", account.getBalance());

        account.setBalance(Double.sum(account.getBalance(), operationCommand.getAmount()));

        logger.debug("New balance -> {}", account.getBalance());

        Operation operation = new Operation(account, operationCommand.getAmount(), operationCommand.getOperationType(),
                LocalDateTime.now());
        operationPagingAndSortingRepository.save(operation);
        return operation.getAccount();
    }
}
