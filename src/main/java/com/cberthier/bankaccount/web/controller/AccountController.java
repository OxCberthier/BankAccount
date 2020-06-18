package com.cberthier.bankaccount.web.controller;

import com.cberthier.bankaccount.domain.model.Account;
import com.cberthier.bankaccount.domain.model.AccountNotFoundException;
import com.cberthier.bankaccount.domain.model.InsufficientFundsException;
import com.cberthier.bankaccount.domain.model.InvalidOperationException;
import com.cberthier.bankaccount.service.AccountService;
import com.cberthier.bankaccount.web.payload.OperationPayload;
import com.cberthier.bankaccount.web.result.AccountResponseEntity;
import com.cberthier.bankaccount.web.result.ApiResult;
import com.cberthier.bankaccount.web.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/operations")
    public ResponseEntity<ApiResult> addOperation(@Validated @RequestBody OperationPayload operationPayload) throws AccountNotFoundException {

        Account account;
        try {
            account = accountService.addOperation(operationPayload.toCommand());
        } catch (InvalidOperationException e) {
            return Result.failure("Invalid Operation");
        } catch (InsufficientFundsException e) {
            return Result.failure("Insufficient Funds");
        }

        return AccountResponseEntity.build(account);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResult> handleAccountNotFoundException() {
        return Result.notFound();
    }
}
