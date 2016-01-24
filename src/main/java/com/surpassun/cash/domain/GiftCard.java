package com.surpassun.cash.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "GIFTCARD")
public class GiftCard implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

	@NotNull
	@Size(min = 6, max = 20)
	private String code;
	
	private boolean active;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime activateDate;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime lastUsedDate;
	
	private float total;

	private float balance;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public DateTime getActivateDate() {
		return activateDate;
	}

	public void setActivateDate(DateTime activateDate) {
		this.activateDate = activateDate;
	}

	public DateTime getLastUsedDate() {
		return lastUsedDate;
	}

	public void setLastUsedDate(DateTime lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
}
