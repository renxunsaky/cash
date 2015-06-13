package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.MembershipLevel;

public interface MembershipLevelRepository extends JpaRepository<MembershipLevel, Integer> {
    
}
