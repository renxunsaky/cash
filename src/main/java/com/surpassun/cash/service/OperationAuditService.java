package com.surpassun.cash.service;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.surpassun.cash.domain.OperationAudit;
import com.surpassun.cash.fx.dto.ArticleDto;
import com.surpassun.cash.repository.OperationAuditRepository;
import com.surpassun.cash.util.StringPool;

@Service
public class OperationAuditService {
	
	private final Logger log = LoggerFactory.getLogger(OperationAuditService.class);

	@Inject
	private OperationAuditRepository auditRepository;
	
	public void addAudit(String terminalId, String employeeName, ArticleDto articleDto, String operationType) {
		OperationAudit audit = new OperationAudit(terminalId, employeeName, 
				StringUtils.substringBefore(articleDto.getCode(), StringPool.COMMA),
				operationType, articleDto.getUnitPrice(), articleDto.getRealPrice());
		auditRepository.saveAndFlush(audit);
		log.info("Added operation audit to database, {0}", audit);
	}
}
