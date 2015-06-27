package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    
}
