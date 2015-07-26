package com.surpassun.cash.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

/**
 * diverse configurations
 */
@Entity
@Table(name = "T_CONFIG")
public class Config implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(min = 0, max = 100)
    private String name;
    
    @Size(min = 0, max = 200)
    private String value;
    
    @Size(min = 0, max = 500)
    private String description;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean getBooleanValue() {
		return Boolean.valueOf(value);
	}
	
	public int getIntegerValue() {
		return Integer.valueOf(value);
	}

	public Float[] getArrayInFloat(char splitter) {
		if (value == null || StringUtils.isEmpty(value)) {
			return null;
		}
		
		String[] values = StringUtils.split(value, splitter);
		Float[] result = new Float[values.length];
		int counter = 0;
		for (String temp : values) {
			result[counter] = Float.valueOf(temp);
			counter++;
		}
		return result;
	}

}
