package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
	public Product findByCode(String code);
}
