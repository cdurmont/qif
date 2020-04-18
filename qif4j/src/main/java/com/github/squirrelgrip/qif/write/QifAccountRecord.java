package com.github.squirrelgrip.qif.write;

import com.github.squirrelgrip.qif.QifAccount;

/**
 * Special case of QifRecord appears at the top of the output and determines the
 * header value for following records
 * 
 * @author carbontax@gmail.com
 */
public class QifAccountRecord {
	private QifAccount txn;

	public QifAccountRecord(QifAccount txn) {
		this.txn = txn;
	}

	public String asFormattedRecord() {
		StringBuilder sb = new StringBuilder();
		sb.append(QifHeaderEnum.ACCOUNT.getText());
		
		Utils.appendFieldValueToOutput("N", txn.getName(), sb);
		Utils.appendFieldValueToOutput("D", txn.getDescription(), sb);
		Utils.appendFieldValueToOutput("T", txn.getType().getLabel(), sb);
		Utils.appendCurrencyValueToOutput("B", txn.getBalance(), sb);

		sb.append(Utils.LINE_SEPARATOR).append(Utils.END_OF_RECORD)
				.append(Utils.LINE_SEPARATOR);

		return sb.toString();
	}
}
