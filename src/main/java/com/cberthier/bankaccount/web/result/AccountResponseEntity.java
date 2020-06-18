package com.cberthier.bankaccount.web.result;

import com.cberthier.bankaccount.domain.model.Account;
import org.springframework.http.ResponseEntity;

public class AccountResponseEntity {

    public static ResponseEntity<ApiResult> build(Account account) {
        ApiResult apiResult = ApiResult.blank()
                .add("accountId", account.getId())
                .add("balance", account.getBalance())
                .add("name", account.getName());
        return Result.ok(apiResult);
    }
}
