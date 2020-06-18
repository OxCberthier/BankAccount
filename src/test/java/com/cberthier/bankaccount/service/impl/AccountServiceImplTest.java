package com.cberthier.bankaccount.service.impl;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.*;
import com.cberthier.bankaccount.domain.repository.AccountCrudRepository;
import com.cberthier.bankaccount.domain.repository.OperationPagingAndSortingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private final double INIT_BALANCE_ACCOUNT = 100;
    private AccountServiceImpl accountService;
    @Mock
    private AccountCrudRepository accountCrudRepositoryMock;
    @Mock
    private OperationPagingAndSortingRepository operationPagingAndSortingRepository;
    @Mock
    private Client clientMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImpl(accountCrudRepositoryMock, operationPagingAndSortingRepository);
    }

    @Test
    public void addDepositOperationOnAccountToUpdateBalanceAccount() throws AccountNotFoundException, InvalidOperationException, InsufficientFundsException {
        Long accountId = 1L;
        double operationAmount = 99.9;
        Account account = new Account("MyAccount", clientMock, INIT_BALANCE_ACCOUNT);
        ArgumentCaptor<Operation> operationCaptor = ArgumentCaptor.forClass(Operation.class);
        when(accountCrudRepositoryMock.findById(accountId)).thenReturn(Optional.of(account));

        OperationCommand operationCommand = new OperationCommand(accountId, operationAmount, OperationTypeEnum.DEPOSIT);
        Account accountReturned = accountService.addOperation(operationCommand);

        verify(operationPagingAndSortingRepository).save(operationCaptor.capture());
        Operation operationCaptorValue = operationCaptor.getValue();
        //Verify balance account returned is updated
        assertEquals(199.9, accountReturned.getBalance());

        //Verify balance account save in db is updated
        assertEquals(199.9, operationCaptorValue.getAccount().getBalance());
    }

    @Test
    public void addDepositOperationOnAccountToUpdateBalanceAccountNotFound() {
        Long accountId = 1L;
        double operationAmount = 99.9;
        when(accountCrudRepositoryMock.findById(accountId)).thenReturn(Optional.empty());

        OperationCommand operationCommand = new OperationCommand(accountId, operationAmount, OperationTypeEnum.DEPOSIT);

        assertThrows(AccountNotFoundException.class, () -> accountService.addOperation(operationCommand));

        verify(operationPagingAndSortingRepository, never()).save(any(Operation.class));
    }

    @Test
    public void addDepositOperationOnAccountToUpdateBalanceNegativeAmount() {
        Long accountId = 1L;
        double operationAmount = -9.99;
        Account account = new Account("MyAccount", clientMock, INIT_BALANCE_ACCOUNT);
        when(accountCrudRepositoryMock.findById(accountId)).thenReturn(Optional.of(account));

        OperationCommand operationCommand = new OperationCommand(accountId, operationAmount, OperationTypeEnum.DEPOSIT);
        assertThrows(InvalidOperationException.class, () -> accountService.addOperation(operationCommand));

        verify(operationPagingAndSortingRepository, never()).save(any(Operation.class));
    }

    @Test
    public void addWithdrawalOperationOnAccountToUpdateBalanceAccount() throws AccountNotFoundException, InvalidOperationException, InsufficientFundsException {
        Long accountId = 1L;
        double operationAmount = 99.9;
        Account account = new Account("MyAccount", clientMock, INIT_BALANCE_ACCOUNT);
        ArgumentCaptor<Operation> operationCaptor = ArgumentCaptor.forClass(Operation.class);
        when(accountCrudRepositoryMock.findById(accountId)).thenReturn(Optional.of(account));

        OperationCommand operationCommand = new OperationCommand(accountId, operationAmount, OperationTypeEnum.WITHDRAWAL);
        Account accountReturned = accountService.addOperation(operationCommand);

        verify(operationPagingAndSortingRepository).save(operationCaptor.capture());
        Operation operationCaptorValue = operationCaptor.getValue();
        //Verify balance account returned is updated
        assertEquals(0.1, accountReturned.getBalance());

        //Verify balance account save in db is updated
        assertEquals(0.1, operationCaptorValue.getAccount().getBalance());
    }

    @Test
    public void addWithdrawalOperationOnAccountToUpdateBalanceAccountInsufficientFunds() {
        Long accountId = 1L;
        double operationAmount = 200;
        Account account = new Account("MyAccount", clientMock, INIT_BALANCE_ACCOUNT);
        ArgumentCaptor<Operation> operationCaptor = ArgumentCaptor.forClass(Operation.class);
        when(accountCrudRepositoryMock.findById(accountId)).thenReturn(Optional.of(account));

        OperationCommand operationCommand = new OperationCommand(accountId, operationAmount, OperationTypeEnum.WITHDRAWAL);
        assertThrows(InsufficientFundsException.class, () -> accountService.addOperation(operationCommand));

        verify(operationPagingAndSortingRepository, never()).save(any(Operation.class));
    }
}