package com.cberthier.bankaccount.domain.repository;

import com.cberthier.bankaccount.domain.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountCrudRepository extends CrudRepository<Account, Long> {
}
