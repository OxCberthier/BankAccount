package com.cberthier.bankaccount.service.impl;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.Account;
import com.cberthier.bankaccount.domain.model.Client;
import com.cberthier.bankaccount.domain.model.Operation;
import com.cberthier.bankaccount.domain.model.OperationTypeEnum;
import com.cberthier.bankaccount.domain.repository.AccountCrudRepository;
import com.cberthier.bankaccount.domain.repository.OperationPagingAndSortingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    private static final double INIT_BALANCE_ACCOUNT = 0;
    private static AccountServiceImpl accountService;
    @Mock
    private static AccountCrudRepository accountCrudRepositoryMock;
    @Mock
    private static OperationPagingAndSortingRepository operationPagingAndSortingRepository;
    @Mock
    private static Client clientMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImpl(accountCrudRepositoryMock, operationPagingAndSortingRepository);
    }

    @Test
    public void addDepositOperationOnAccountToUpdateBalanceAccount() {
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
        assertEquals(Double.sum(INIT_BALANCE_ACCOUNT, operationAmount), accountReturned.getBalance());

        //Verify balance account save in db is updated
        assertEquals(Double.sum(INIT_BALANCE_ACCOUNT, operationAmount), operationCaptorValue.getAccount().getBalance());
    }
}