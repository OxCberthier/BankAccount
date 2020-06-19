package com.cberthier.bankaccount.domain.repository;

import com.cberthier.bankaccount.domain.model.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OperationPagingAndSortingRepository extends PagingAndSortingRepository<Operation, Long> {

    Page<Operation> findAllByAccountId(Long accountId, Pageable pageable);
}

