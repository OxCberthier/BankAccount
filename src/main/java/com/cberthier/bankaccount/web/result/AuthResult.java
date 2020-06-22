package com.cberthier.bankaccount.web.result;

import com.cberthier.bankaccount.domain.model.ClientDetailsImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class AuthResult {
    public static ResponseEntity<ApiResult> build(String accessToken, ClientDetailsImpl clientDetails, List<String> roles) {
        ApiResult apiResult = ApiResult.blank()
                .add("accessToken", accessToken)
                .add("id", clientDetails.getId())
                .add("username", clientDetails.getUsername())
                .add("email", clientDetails.getEmail())
                .add("roles", roles.toString());

        return Result.ok(apiResult);
    }
}
