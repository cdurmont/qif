package com.github.squirrelgrip.qif4j;

import java.math.BigDecimal;
import java.time.LocalDate;

public class QifTransaction {

	private LocalDate date;
	private BigDecimal total;
	private String memo;

	public String getMemo() {
		return memo;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public LocalDate getDate() {
		return date;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
