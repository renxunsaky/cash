package com.surpassun.cash.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_MemebershipLevel")
public class MembershipLevel implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    private int level;

    @Size(min = 0, max = 100)
    @NotNull
    private String name;

	@NotNull
    private float discount;

    public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}
}
