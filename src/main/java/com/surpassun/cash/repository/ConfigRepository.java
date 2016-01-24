package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.Config;

public interface ConfigRepository extends JpaRepository<Config, Long> {
    
	public Config findByName(String name);

}
