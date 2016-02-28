package com.surpassun.cash.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Products
 */
@Entity
@Table(name = "T_PRODUCT")
public class Product extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Size(min = 0, max = 100)
	@NotNull
	private String name;

	@NotNull
	@Column(unique = true)
	private String code;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category", nullable = false)
	private Category category;

	@NotNull
	private float price;

	private Float discount;

	private Integer quantity;
	
	private boolean shortcutButtonEnabled;

	public Product() {
	}

	public Product(String name, String barcode, String priceInfo) {
		this.name = name;
		this.code = barcode;
		this.price = Float.valueOf(priceInfo);
		this.id = -1;
	}

	public Product(String name, String barcode, float price, Category category, boolean shortcutButtonEnabled) {
		this.name = name;
		this.code = barcode;
		this.price = price;
		this.category = category;
		this.shortcutButtonEnabled = shortcutButtonEnabled;
		super.setCreatedBy("1");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public boolean isShortcutButtonEnabled() {
		return shortcutButtonEnabled;
	}

	public void setShortcutButtonEnabled(boolean shortcutButtonEnabled) {
		this.shortcutButtonEnabled = shortcutButtonEnabled;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		Product product = (Product)obj;
		if (product.getCode() == null) {
			return false;
		}
		if (this.getCode().equals(product.getCode())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.getCode().hashCode();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
