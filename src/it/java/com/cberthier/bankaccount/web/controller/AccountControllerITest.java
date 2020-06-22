package com.cberthier.bankaccount.web.controller;

import com.cberthier.bankaccount.BankAccountApplication;
import com.cberthier.bankaccount.domain.model.*;
import com.cberthier.bankaccount.domain.repository.AccountCrudRepository;
import com.cberthier.bankaccount.domain.repository.ClientCrudRepository;
import com.cberthier.bankaccount.domain.repository.OperationPagingAndSortingRepository;
import com.cberthier.bankaccount.domain.repository.RoleCrudRepository;
import com.cberthier.bankaccount.web.payload.OperationPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@SpringBootTest(classes = BankAccountApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerITest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ClientCrudRepository clientCrudRepository;

    @Autowired
    AccountCrudRepository accountCrudRepository;

    @Autowired
    OperationPagingAndSortingRepository operationPagingAndSortingRepository;

    @Autowired
    RoleCrudRepository roleCrudRepository;

    Client client;
    Account account;

    /**
     * Create client with one account
     */
    @BeforeEach
    public void setup() {
        Role roleClient = roleCrudRepository.save(new Role(RoleEnum.ROLE_CLIENT));
        client = new Client("cberthier@oxiane.com", "$2a$10$rZHn6UFjrQWHWFi6sBmrJeykmybnRzW3t7p5ZHDyxHUG3UKxZ2nNC", "Cyril", "BERTHIER");
        client.setRoles(Set.of(roleClient));
        clientCrudRepository.save(client);

        account = new Account("MyAccount", client, 100);
        accountCrudRepository.save(account);
    }

    /**
     * Clean database for next test
     */
    @AfterEach
    public void clean() {
        operationPagingAndSortingRepository.deleteAll();
        clientCrudRepository.deleteAll();
        roleCrudRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void addDepositOperationToAccount() throws Exception {

        double operationAmount = 100.0;
        OperationPayload operationPayload = new OperationPayload(account.getId(), operationAmount, OperationTypeEnum.DEPOSIT);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(account.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.getName()));
    }

    @Test
    @WithMockUser
    public void addDepositOperationToAccountNotFound() throws Exception {

        OperationPayload operationPayload = new OperationPayload(999999L, 100, OperationTypeEnum.DEPOSIT);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser
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

    @Test
    @WithMockUser
    public void addWithdrawalOperationToAccount() throws Exception {

        double operationAmount = 10.0;
        OperationPayload operationPayload = new OperationPayload(account.getId(), operationAmount, OperationTypeEnum.WITHDRAWAL);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(account.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(90))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.getName()));
    }


    @Test
    @WithMockUser
    public void addWithdrawalOperationToAccountInsufficientFunds() throws Exception {

        OperationPayload operationPayload = new OperationPayload(account.getId(), 10000, OperationTypeEnum.WITHDRAWAL);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/accounts/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(operationPayload))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    @WithMockUser
    public void retrieveOperationAccount() throws Exception {
        Operation operation1 = new Operation(account, 100.0, OperationTypeEnum.DEPOSIT, LocalDateTime.now().minusDays(2), 200);
        Operation operation2 = new Operation(account, 100.0, OperationTypeEnum.DEPOSIT, LocalDateTime.now().minusDays(1), 300);
        Operation operation3 = new Operation(account, 100.0, OperationTypeEnum.WITHDRAWAL, LocalDateTime.now().minusHours(5), 200);
        LocalDateTime now = LocalDateTime.now();
        Operation operation4 = new Operation(account, 100.0, OperationTypeEnum.DEPOSIT, now, 300);

        operationPagingAndSortingRepository.save(operation1);
        operationPagingAndSortingRepository.save(operation2);
        operationPagingAndSortingRepository.save(operation3);
        operationPagingAndSortingRepository.save(operation4);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/accounts/operations")
                        .param("accountId", account.getId().toString())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "date,desc")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operations[0].date").value(now.format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operations[0].accountBalance").value(300));
    }
}