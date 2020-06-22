package com.cberthier.bankaccount.web.controller;

import com.cberthier.bankaccount.domain.OperationCommand;
import com.cberthier.bankaccount.domain.model.*;
import com.cberthier.bankaccount.service.AccountService;
import com.cberthier.bankaccount.service.security.AuthEntryPointJwt;
import com.cberthier.bankaccount.service.security.ClientDetailsServiceImpl;
import com.cberthier.bankaccount.service.security.JwtUtils;
import com.cberthier.bankaccount.web.payload.OperationPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    private static final Long ACCOUNT_ID = 1L;
    private static final Double INIT_ACCOUNT_BALANCE = 0.0;
    private static final String ACCOUNT_NAME = "MyAccount";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    // Mock for spring security configuration
    @MockBean
    JwtUtils jwtUtils;

    // Mock for spring security configuration
    @MockBean
    ClientDetailsServiceImpl clientDetailsService;

    // Mock for spring security configuration
    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @Mock
    Account accountMock;

    @BeforeEach
    public void setup() {
        //Configure accountMock
        when(accountMock.getId()).thenReturn(ACCOUNT_ID);
        when(accountMock.getBalance()).thenReturn(INIT_ACCOUNT_BALANCE);
        when(accountMock.getName()).thenReturn(ACCOUNT_NAME);
    }

    @Test
    @WithMockUser
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
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(ACCOUNT_ID))
                //Balance is not updated because account service is mocking
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(INIT_ACCOUNT_BALANCE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ACCOUNT_NAME));

        ArgumentCaptor<OperationCommand> argumentCaptor = ArgumentCaptor.forClass(OperationCommand.class);
        verify(accountService).addOperation(argumentCaptor.capture());
        OperationCommand captorValue = argumentCaptor.getValue();
        assertEquals(operationPayload.getAccountId(), captorValue.getAccountId());
        assertEquals(operationPayload.getAmount(), captorValue.getAmount());
        assertEquals(operationPayload.getOperationType(), captorValue.getOperationType());
    }

    @Test
    @WithMockUser
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
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(accountService).addOperation(any(OperationCommand.class));
    }

    @Test
    @WithMockUser
    public void addDepositOperationToAccountWithNegativeAmount() throws Exception {

        OperationPayload operationPayload = new OperationPayload(1L, -10, OperationTypeEnum.DEPOSIT);
        ObjectMapper objectMapper = new ObjectMapper();

        when(accountService.addOperation(any(OperationCommand.class))).thenThrow(new InvalidOperationException());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(accountService).addOperation(any(OperationCommand.class));
    }

    @Test
    @WithMockUser
    public void addWithdrawalOperationToAccount() throws Exception {

        OperationPayload operationPayload = new OperationPayload(1L, 100, OperationTypeEnum.WITHDRAWAL);
        ObjectMapper objectMapper = new ObjectMapper();

        when(accountService.addOperation(any(OperationCommand.class))).thenReturn(accountMock);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(ACCOUNT_ID))
                //Balance is not updated because account service is mocking
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(INIT_ACCOUNT_BALANCE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ACCOUNT_NAME));

        ArgumentCaptor<OperationCommand> argumentCaptor = ArgumentCaptor.forClass(OperationCommand.class);
        verify(accountService).addOperation(argumentCaptor.capture());
        OperationCommand captorValue = argumentCaptor.getValue();
        assertEquals(operationPayload.getAccountId(), captorValue.getAccountId());
        assertEquals(operationPayload.getAmount(), captorValue.getAmount());
        assertEquals(operationPayload.getOperationType(), captorValue.getOperationType());
    }

    @Test
    @WithMockUser
    public void addWithdrawalOperationToAccountInsufficientFunds() throws Exception {

        OperationPayload operationPayload = new OperationPayload(1L, 1000, OperationTypeEnum.WITHDRAWAL);
        ObjectMapper objectMapper = new ObjectMapper();

        when(accountService.addOperation(any(OperationCommand.class))).thenThrow(new InsufficientFundsException());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(accountService).addOperation(any(OperationCommand.class));
    }

    @Test
    @WithMockUser
    public void retrieveOperationAccount() throws Exception {
        Operation operation1 = new Operation(accountMock, 100.0, OperationTypeEnum.DEPOSIT, LocalDateTime.now(), 100);
        PageImpl page = new PageImpl(Collections.singletonList(operation1));
        when(accountService.getOperations(anyLong(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/accounts/operations")
                        .param("accountId", "1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "date,desc")
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<Pageable> argumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(accountService).getOperations(anyLong(), argumentCaptor.capture());
        Pageable pageableCaptorValue = argumentCaptor.getValue();
        assertEquals(0, pageableCaptorValue.getPageNumber());
        assertEquals(10, pageableCaptorValue.getPageSize());
        assertTrue(pageableCaptorValue.getSort().get().findFirst().get().isDescending());
        assertEquals("date", pageableCaptorValue.getSort().get().findFirst().get().getProperty());
    }
}