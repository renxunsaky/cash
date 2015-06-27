package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
