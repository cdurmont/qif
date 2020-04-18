package com.github.squirrelgrip.qif;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class QifCashTransaction extends QifTransaction {

	private List<String> address = new ArrayList<String>();
	private String category;
	private String clearedStatus;
	private QifSplitTransaction currentSplitTransaction;
	private String payee;
	private String reference;
	private List<QifSplitTransaction> splitTransactions = new ArrayList<QifSplitTransaction>();

	public void addAddress(String address) {
		this.address.add(address);
	}

	private void checkSplitTransaction() {
		if (currentSplitTransaction == null) {
			createSplitTransaction();
		}
	}
	
	private void createSplitTransaction() {
		currentSplitTransaction = new QifSplitTransaction(this);
		splitTransactions.add(currentSplitTransaction);
	}
	
	public List<String> getAddress() {
		return address;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getClearedStatus() {
		return clearedStatus;
	}

	public QifSplitTransaction getCurrentSplitTransaction() {
		return currentSplitTransaction;
	}

	public String getPayee() {
		return payee;
	}

	public String getReference() {
		return reference;
	}

	public List<QifSplitTransaction> getSplits() {
		return splitTransactions;
	}

	public List<QifSplitTransaction> getSplitTransactions() {
		return splitTransactions;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setClearedStatus(String clearedStatus) {
		this.clearedStatus = clearedStatus;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public void setSplitAmount(String splitAmount) {
		checkSplitTransaction();
		if(currentSplitTransaction.getAmount() != null) {
			createSplitTransaction();
		}
		currentSplitTransaction.setAmount(new BigDecimal(splitAmount));
	}
	
	public void setSplitCategory(String splitCategory) {
		checkSplitTransaction();
		if(currentSplitTransaction.getCategory() != null) {
			createSplitTransaction();
		}
		currentSplitTransaction.setCategory(splitCategory);
	}

	public void setSplitMemo(String splitMemo) {
		checkSplitTransaction();
		if(currentSplitTransaction.getMemo() != null) {
			createSplitTransaction();
		}
		currentSplitTransaction.setMemo(splitMemo);
	}
	
	public String toCsv(String dateFormat) {
		return String.format("%s,%s,%s,%s,%s", getDate().format(DateTimeFormatter.ofPattern(dateFormat)), empty(getCategory()),
		        empty(getPayee()), getTotal(), empty(getMemo()));
	}
	
	private String empty(String value) {
		return value == null ? "" : value;
	}

}
