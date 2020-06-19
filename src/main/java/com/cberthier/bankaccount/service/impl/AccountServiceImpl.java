package com.cberthier.bankaccount.service.impl;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.*;
import com.cberthier.bankaccount.domain.repository.AccountCrudRepository;
import com.cberthier.bankaccount.domain.repository.OperationPagingAndSortingRepository;
import com.cberthier.bankaccount.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
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
    public Account addOperation(OperationCommand operationCommand) throws AccountNotFoundException, InvalidOperationException, InsufficientFundsException {
        logger.debug("Add Operation -> AccountId {}, Amount: {}, Type: {}",
                operationCommand.getAccountId(), operationCommand.getAmount(), operationCommand.getOperationType());

        if (Double.compare(operationCommand.getAmount(), 0) < 0) {
            logger.debug("Operation rejected for negative amount");
            throw new InvalidOperationException();
        }

        Optional<Account> optionalAccount = accountCrudRepository.findById(operationCommand.getAccountId());

        if (optionalAccount.isEmpty()) {
            logger.debug("Operation rejected for account not found");
            throw new AccountNotFoundException();
        }

        Account account = optionalAccount.get();
        logger.debug("Old balance -> {}", account.getBalance());

        BigDecimal newBalance;
        switch (operationCommand.getOperationType()) {
            case DEPOSIT:
                logger.debug("Add {} to account", operationCommand.getAmount());
                newBalance = BigDecimal.valueOf(account.getBalance()).add(BigDecimal.valueOf(operationCommand.getAmount()));
                break;
            case WITHDRAWAL:
                logger.debug("Subtract {} to account", operationCommand.getAmount());
                newBalance = BigDecimal.valueOf(account.getBalance()).subtract(BigDecimal.valueOf(operationCommand.getAmount()));
                break;
            default:
                logger.error("Invalid Operation type");
                throw new InvalidOperationException();
        }

        //Account balance can't be negative
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            logger.debug("Operation rejected for insufficient funds");
            throw new InsufficientFundsException();
        }
        //Update balance account
        account.setBalance(newBalance.doubleValue());

        logger.debug("New balance -> {}", account.getBalance());

        Operation operation = new Operation(account, operationCommand.getAmount(), operationCommand.getOperationType(),
                LocalDateTime.now());
        operationPagingAndSortingRepository.save(operation);
        return operation.getAccount();
    }

    @Override
    public Page<Operation> getOperations(Long accountId, Pageable pageable) {
        return null;
    }
}
