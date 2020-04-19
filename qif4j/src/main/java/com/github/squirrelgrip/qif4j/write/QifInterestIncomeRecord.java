package com.github.squirrelgrip.qif4j.write;

import com.github.squirrelgrip.qif4j.QifTransaction;
import com.github.squirrelgrip.qif4j.QifInterestIncome;

public class QifInterestIncomeRecord extends AbstractQifRecord {
	private QifInterestIncome txn;
	private QifHeaderEnum header;

	public QifInterestIncomeRecord(QifHeaderEnum header, QifTransaction txn) {
		this.header = header;
		this.txn = (QifInterestIncome) txn;
	}
	
	@Override
	public QifHeaderEnum getHeader() {
		return header;
	}

	@Override
	public String formatTransaction() {
		StringBuilder sb = new StringBuilder();
		Utils.appendFieldValueToOutput("D", txn.getDate(), sb);
		Utils.appendFieldValueToOutput("N", txn.getAction(), sb);
		Utils.appendCurrencyValueToOutput("T", txn.getTotal(), sb);
		return sb.toString();
	}
}
