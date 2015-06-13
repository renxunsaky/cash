package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
}
