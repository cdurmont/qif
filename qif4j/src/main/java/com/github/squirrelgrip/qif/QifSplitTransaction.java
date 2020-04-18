package com.github.squirrelgrip.qif;

import java.math.BigDecimal;

public class QifSplitTransaction {

	private final QifTransaction transaction;
	private String category;
	private String memo;
	private BigDecimal amount;

	public QifSplitTransaction(QifTransaction transaction) {
		this.transaction = transaction;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public QifTransaction getTransaction() {
		return transaction;
	}

}