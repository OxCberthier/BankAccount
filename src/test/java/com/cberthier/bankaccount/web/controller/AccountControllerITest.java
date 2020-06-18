package com.cberthier.bankaccount.web.controller;

import com.cberthier.bankaccount.domain.model.Account;
import com.cberthier.bankaccount.domain.model.Client;
import com.cberthier.bankaccount.domain.model.OperationTypeEnum;
import com.cberthier.bankaccount.domain.repository.AccountCrudRepository;
import com.cberthier.bankaccount.domain.repository.ClientCrudRepository;
import com.cberthier.bankaccount.web.payload.OperationPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerITest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ClientCrudRepository clientCrudRepository;

    @Autowired
    AccountCrudRepository accountCrudRepository;

    private Client client;
    private Account account;

    @BeforeEach
    public void setup() {
        client = new Client("Cyril", "BERTHIER");
        clientCrudRepository.save(client);

        account = new Account("MyAccount", client, 0);
        accountCrudRepository.save(account);
    }

    @Test
    public void addDepositOperationToAccount() throws Exception {

        OperationPayload operationPayload = new OperationPayload(account.getId(), 100, OperationTypeEnum.DEPOSIT);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addDepositOperationToAccountNotFound() throws Exception {

        OperationPayload operationPayload = new OperationPayload(999999L, 100, OperationTypeEnum.DEPOSIT);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void addDepositOperationToAccountWithNegativeAmount() throws Exception {

        OperationPayload operationPayload = new OperationPayload(account.getId(), -10, OperationTypeEnum.DEPOSIT);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}