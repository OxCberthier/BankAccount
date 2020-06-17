package com.cberthier.bankaccount.domain.repository;

import com.cberthier.bankaccount.domain.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientCrudRepository extends CrudRepository<Client, Long> {
}
