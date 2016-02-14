package com.surpassun.cash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
	
    public List<Category> findByShortcutButtonEnabled(boolean enabled);
}
