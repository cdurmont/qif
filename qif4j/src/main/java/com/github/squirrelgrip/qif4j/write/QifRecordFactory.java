package com.github.squirrelgrip.qif4j.write;

import com.github.squirrelgrip.qif4j.QifAccount;
import com.github.squirrelgrip.qif4j.QifInvestment;
import com.github.squirrelgrip.qif4j.QifTransaction;
import com.github.squirrelgrip.qif4j.QifInterestIncome;
import com.github.squirrelgrip.qif4j.QifReaderException;

/**
 * A factory for creating QifRecord objects from QifTransaction objects. The
 * factory state holds the value of the QIF header, for example
 * <code>!Type:Invst</code> so that each record produced by the factory instance
 * will belong to an account described by the header.
 * 
 * <pre><code>
 *     QifRecordFactory factory = new QifRecordFactory(QifHeaderEnum.INVESTMENT);
 *     QifTransaction txn = new QifInvestment(...);
 *     QifRecord invstRecord = factory.forTransaction(txn);
 * </code></pre>
 * 
 * @author carbontax@gmail.com
 *
 */
public class QifRecordFactory {
	private QifHeaderEnum header;

	public QifRecordFactory(QifAccount account) {
		if(account == null) {
			throw new IllegalArgumentException("Account required to create records");
		}
		this.header = account.getHeader();
	}

	public QifRecord forTransaction(QifTransaction txn) {
		QifRecord record = null;
		if (txn instanceof QifInvestment) {
			QifInvestment invst = (QifInvestment) txn;
			record = new QifInvestmentRecord(invst);
		} else if (txn instanceof QifInterestIncome) {
			record = new QifInterestIncomeRecord(header, txn);
		} else {
			throw new QifReaderException("Transaction type not implemented");
		}
		return record;
	}

}
