package com.cberthier.bankaccount.domain.repository;

import com.cberthier.bankaccount.domain.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleCrudRepository extends CrudRepository<Role, Long> {
}
