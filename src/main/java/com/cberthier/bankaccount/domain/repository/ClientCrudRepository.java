package com.cberthier.bankaccount.domain.repository;

import com.cberthier.bankaccount.domain.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientCrudRepository extends CrudRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}
