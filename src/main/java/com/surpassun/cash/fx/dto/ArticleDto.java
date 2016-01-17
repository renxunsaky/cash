package com.surpassun.cash.fx.dto;

import com.surpassun.cash.domain.Product;
import com.surpassun.cash.util.StringPool;

public class ArticleDto {

	private long id;
	private String code;
	private String displayName;
	private String quantityInfo;
	private String originalPriceInfo;
	private String priceInfo;
	private String otherInfo;
	private Float discount;
	private float unitPrice;
	private float realPrice;
	private int quantity;
	
	public ArticleDto() {
	};
	
	public ArticleDto(Product product, Integer quantity, Float strickDiscount) {
		this.id = product.getId();
		this.code = product.getCode();
		this.displayName = product.getName();
		this.unitPrice = product.getPrice();
		this.discount = product.getDiscount();
		this.quantity = quantity;
		
		if (strickDiscount != null) {
			this.quantity = quantity = 1;
			this.discount = strickDiscount;
			this.code += ":" + strickDiscount;
		}
		correctInfo(this.quantity, strickDiscount != null);
	}
	
	public void correctInfo(Integer quantity, boolean strickModeOn) {
		this.quantity = quantity;
		float originalPrice = unitPrice * quantity;
		if (discount != null && discount > 0) {
			StringBuilder sb = new StringBuilder();
			originalPrice = this.unitPrice * this.quantity;
			sb.append(originalPrice).append(StringPool.EURO)
				.append(StringPool.RETURN_NEW_LINE)
				.append(StringPool.MINUS).append(String.format("%.0f", (discount * 100))).append(StringPool.PERCENT)
				.append(StringPool.RETURN_NEW_LINE)
				.append((String.format("%.2f", (1- discount) * originalPrice))).append(StringPool.EURO);
			realPrice = (1- discount) * originalPrice;
			this.priceInfo = sb.toString();
		} else {
			realPrice = originalPrice;
			this.priceInfo = originalPrice + " €";
		}
		
		if (quantity != null && quantity > 1) {
			this.quantityInfo = StringPool.MULTIPLE + StringPool.SPACE + quantity;
		} else if (quantity == 1) {
			this.quantityInfo = null;
		}
	}
	
	public void correctInfo(String discountText) {
		int disc = Integer.valueOf(discountText);
		this.discount = disc / 100F;
		
		correctInfo(quantity, false);
	}
	
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

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getQuantityInfo() {
		return quantityInfo;
	}
	public void setQuantityInfo(String quantityInfo) {
		this.quantityInfo = quantityInfo;
	}
	public String getOriginalPriceInfo() {
		return originalPriceInfo;
	}

	public void setOriginalPriceInfo(String originalPriceInfo) {
		this.originalPriceInfo = originalPriceInfo;
	}

	public String getPriceInfo() {
		return priceInfo;
	}

	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * If the product is the same, we treat it as the same one and ignore the other properties
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || ! (obj instanceof ArticleDto)) {
			return false;
		}
		
		ArticleDto dto = (ArticleDto)obj;
		if (dto.getCode() != null && this.getCode() != null && dto.getCode().equals(this.getCode())) {
			if (((discount != null && dto.getDiscount() != null && discount.equals(dto.getDiscount()))
					|| discount == null && dto.getDiscount() == null)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getCode().hashCode();
	}
}
