package com.github.squirrelgrip.qif4j.write;

import com.github.squirrelgrip.qif4j.QifInvestment;

public class QifInvestmentRecord extends AbstractQifRecord {
	private QifInvestment txn;

	public QifInvestmentRecord(QifInvestment txn) {
		this.txn = txn;
	}

	@Override
	public QifHeaderEnum getHeader() {
		return QifHeaderEnum.INVESTMENT;
	}

	@Override
	public String formatTransaction() {
		String txnString = null;
		QifInvestmentAction action = QifInvestmentAction.forText(txn
				.getAction());
		switch (action) {
		case DIV:
			txnString = formatDividendTransaction();
			break;
		case TRANSFER_IN:
		case TRANSFER_OUT:
			txnString = formatTransferTransaction();
			break;
		default:
			txnString = formatDefaultTransaction();
			break;
		}
		return txnString;
	}

	private String formatTransferTransaction() {
		StringBuilder sb = new StringBuilder();
		Utils.appendFieldValueToOutput("D", txn.getDate(), sb);
		Utils.appendFieldValueToOutput("N", txn.getAction(), sb);
		Utils.appendFieldValueToOutput("P", "Transfer", sb);
		Utils.appendCurrencyValueToOutput("T", txn.getTotal(), sb);
		Utils.appendFieldValueToOutput("L", txn.getCategory(), sb);
		Utils.appendCurrencyValueToOutput("$", txn.getTotal(), sb);
		return sb.toString();
	}

	private String formatDefaultTransaction() {
		StringBuilder sb = new StringBuilder();
		Utils.appendFieldValueToOutput("D", txn.getDate(), sb);
		Utils.appendFieldValueToOutput("N", txn.getAction(), sb);
		Utils.appendFieldValueToOutput("Y", txn.getSecurity(), sb);
		Utils.appendCurrencyValueToOutput("I", txn.getPrice(), sb);
		Utils.appendFieldValueToOutput("Q", txn.getQuantity(), sb);
		Utils.appendCurrencyValueToOutput("T", txn.getTotal(), sb);
		Utils.appendCurrencyValueToOutput("O", txn.getCommission(), sb);
		Utils.appendFieldValueToOutput("M", txn.getMemo(), sb);
		return sb.toString();
	}

	private String formatDividendTransaction() {
		StringBuilder sb = new StringBuilder();
		Utils.appendFieldValueToOutput("D", txn.getDate(), sb);
		Utils.appendFieldValueToOutput("N", txn.getAction(), sb);
		Utils.appendFieldValueToOutput("Y", txn.getSecurity(), sb);
		Utils.appendCurrencyValueToOutput("I", txn.getPrice(), sb);
		Utils.appendCurrencyValueToOutput("T", txn.getTotal(), sb);
		Utils.appendFieldValueToOutput("M", txn.getMemo(), sb);
		return sb.toString();
	}

}
