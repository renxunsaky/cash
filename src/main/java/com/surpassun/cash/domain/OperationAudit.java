package com.surpassun.cash.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Audit.
 */
@Entity
@Table(name = "OPERATION_AUDIT")
public class OperationAudit extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

	private String terminalId;
	private String user;
	private String productCode;
	private String operationType;
	private float unitPrice;
	private float realPrice;
	
	public OperationAudit() {
	}

	public OperationAudit(String terminalId, String user, String productCode,
			String operationType, float unitPrice, float realPrice) {
		super();
		super.setCreatedBy("1");
		super.setLastModifiedBy("1");
		this.terminalId = terminalId;
		this.user = user;
		this.productCode = productCode;
		this.operationType = operationType;
		this.unitPrice = unitPrice;
		this.realPrice = realPrice;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	public float getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(float realPrice) {
		this.realPrice = realPrice;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(terminalId).append(",").append(user).append(",")
			.append(productCode).append(",").append(unitPrice).append(",").append(realPrice);
		return sb.toString();
	}
}
