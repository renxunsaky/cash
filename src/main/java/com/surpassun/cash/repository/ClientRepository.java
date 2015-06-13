package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    
}
