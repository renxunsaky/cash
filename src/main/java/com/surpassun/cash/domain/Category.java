package com.surpassun.cash.domain;

import java.io.Serializable;

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
    private String code;

    private float discount;

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

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}
    
}
