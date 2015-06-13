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
	@JoinColumn(name = "productId", nullable = false)
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clientId", nullable = true)
    private Client client;

	@NotNull
	private float unitPrice;
	
	@NotNull
	private int quantity;
	
	@NotNull
	private float totalPrice;
    
	@NotNull
    private float discount;
	
	private float cash;
	
	private float bankCard;
	
	private float giftCard;
	
	@NotNull
	private String terminalId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
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

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
