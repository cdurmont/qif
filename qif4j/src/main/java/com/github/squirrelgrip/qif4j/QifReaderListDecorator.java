package com.github.squirrelgrip.qif4j;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class QifReaderListDecorator {
	private final List<QifTransaction> transactions = new ArrayList<QifTransaction>();
	private final QifReader qifReader;

	public QifReaderListDecorator(QifReader qifReader) {
		this.qifReader = qifReader;
		qifReader.addTranasctionListener(new TransactionListener() {
			
			public void onTransaction(QifTransaction transaction) {
				transactions.add(transaction);
			}
		});
	}
	
	public List<QifTransaction> getTransactions() {
		qifReader.load();
		return transactions;
	}

	public void writeCsv(PrintWriter writer) {
		writeCsv(writer, QifReader.DEFAULT_DATE_FORMAT);
	}

	public void writeCsv(PrintWriter writer, String dateFormat) {
		writer.println("DATE,CATEGORY,PAYEE,TOTAL,MEMO");
		for (QifTransaction transaction : getTransactions()) {
			if (transaction instanceof QifCashTransaction) {
				writer.println(((QifCashTransaction) transaction).toCsv(dateFormat));
			}
		}
	}


}
