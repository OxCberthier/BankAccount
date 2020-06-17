package com.cberthier.bankaccount.web.controller;

import com.cberthier.bankaccount.domain.model.AccountNotFoundException;
import com.cberthier.bankaccount.domain.model.InvalidOperationException;
import com.cberthier.bankaccount.service.AccountService;
import com.cberthier.bankaccount.web.payload.OperationPayload;
import com.cberthier.bankaccount.web.result.ApiResult;
import com.cberthier.bankaccount.web.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/operations")
    public ResponseEntity<ApiResult> addOperation(@Validated @RequestBody OperationPayload operationPayload) throws InvalidOperationException, AccountNotFoundException {

        accountService.addOperation(operationPayload.toCommand());

        return Result.ok();
    }
}
