package com.surpassun.cash.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Transaction of every payment
 */
@Entity
@Table(name = "T_TRANSACTION")
public class Transaction extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
    @Id
    private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId", nullable = false)
	private Order order;

	@NotNull
	private float total;
	
	private float cash;
	
	private float bankCard;
	
	private float giftCard;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public float getCash() {
		return cash;
	}

	public void setCash(float cash) {
		this.cash = cash;
	}

	public float getBankCard() {
		return bankCard;
	}

	public void setBankCard(float bankCard) {
		this.bankCard = bankCard;
	}

	public float getGiftCard() {
		return giftCard;
	}

	public void setGiftCard(float giftCard) {
		this.giftCard = giftCard;
	}
}
