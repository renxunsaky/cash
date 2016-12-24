package com.surpassun.cash.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Category of products.
 */
@Entity
@Table(name = "T_CATEGORY")
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
    @Size(min = 1, max = 50)
    @Id
    private String name;

    @Size(min = 0, max = 100)
    @Column(unique = true)
    private String code;

    private Float discount;
    
    private boolean shortcutButtonEnabled;
    
    private int displayOrder;
    
    public Category() {
	}
    
    public Category(String name, String code, boolean shortcutButtonEnabled) {
    	this.name = name;
    	this.code = code;
    	this.shortcutButtonEnabled = shortcutButtonEnabled;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public boolean isShortcutButtonEnabled() {
		return shortcutButtonEnabled;
	}

	public void setShortcutButtonEnabled(boolean shortcutButtonEnabled) {
		this.shortcutButtonEnabled = shortcutButtonEnabled;
	}
	
	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
