package com.github.squirrelgrip.qif;

import java.math.BigDecimal;

public class QifInvestment extends QifTransaction {

	private String action;
	private BigDecimal price;
	private BigDecimal quantity;
	private String security;
	private BigDecimal commission;
	private String category;

	public String getAction() {
		return action;
	}

	public String getCategory() {
		return category;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public String getSecurity() {
		return security;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Note: Transfer In or Transfer Out actions use this field to indicate the
	 * destination when the value is wrapped like this: <code>[Checking]</code>
	 * 
	 * @param category The category assigned to the Investment
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public void setSecurity(String security) {
		this.security = security;
	}
}
