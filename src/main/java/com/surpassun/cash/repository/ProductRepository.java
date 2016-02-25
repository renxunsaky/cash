package com.surpassun.cash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.Category;
import com.surpassun.cash.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
	public Product findByCode(String code);
	
	public List<Product> findByCategory(Category category);

	public int countByCategory(Category category);
}
