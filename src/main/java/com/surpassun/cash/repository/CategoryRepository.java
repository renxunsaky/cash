package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    
}
