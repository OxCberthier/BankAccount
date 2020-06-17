package com.cberthier.bankaccount.web.controller;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.Account;
import com.cberthier.bankaccount.domain.model.AccountNotFoundException;
import com.cberthier.bankaccount.domain.model.OperationTypeEnum;
import com.cberthier.bankaccount.service.AccountService;
import com.cberthier.bankaccount.web.payload.OperationPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Mock
    Account accountMock;

    @Test
    public void addDepositOperationToAccount() throws Exception {

        OperationPayload operationPayload = new OperationPayload(1L, 100, OperationTypeEnum.DEPOSIT);
        ObjectMapper objectMapper = new ObjectMapper();

        when(accountService.addOperation(any(OperationCommand.class))).thenReturn(accountMock);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<OperationCommand> argumentCaptor = ArgumentCaptor.forClass(OperationCommand.class);
        verify(accountService).addOperation(argumentCaptor.capture());
        OperationCommand captorValue = argumentCaptor.getValue();
        assertEquals(operationPayload.getAccountId(), captorValue.getAccountId());
        assertEquals(operationPayload.getAmount(), captorValue.getAmount());
        assertEquals(operationPayload.getOperationType(), captorValue.getOperationType());
    }

    @Test
    public void addDepositOperationToAccountNotFound() throws Exception {

        OperationPayload operationPayload = new OperationPayload(1L, 100, OperationTypeEnum.DEPOSIT);
        ObjectMapper objectMapper = new ObjectMapper();

        when(accountService.addOperation(any(OperationCommand.class))).thenThrow(new AccountNotFoundException());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(accountService).addOperation(any(OperationCommand.class));
    }
}