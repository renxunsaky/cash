package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.OperationAudit;

public interface OperationAuditRepository extends JpaRepository<OperationAudit, Long> {

}
