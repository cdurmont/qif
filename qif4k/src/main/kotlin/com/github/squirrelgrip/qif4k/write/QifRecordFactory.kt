package com.github.squirrelgrip.qif4k.write

import com.github.squirrelgrip.qif4k.*

/**
 * A factory for creating QifRecord objects from QifTransaction objects. The
 * factory state holds the value of the QIF header, for example
 * `!Type:Invst` so that each record produced by the factory instance
 * will belong to an account described by the header.
 *
 * <pre>`
 * QifRecordFactory factory = new QifRecordFactory(QifHeaderEnum.INVESTMENT);
 * QifTransaction txn = new QifInvestment(...);
 * QifRecord invstRecord = factory.forTransaction(txn);
`</pre> *
 *
 * @author carbontax@gmail.com
 */
class QifRecordFactory(account: QifAccount?) {
    private val header: QifHeaderEnum?
    fun forTransaction(txn: QifTransaction): QifRecord {
        return if (txn is QifInvestment) {
            QifInvestmentRecord(txn)
        } else if (txn is QifInterestIncome) {
            QifInterestIncomeRecord(header, txn)
        } else {
            throw QifReaderException("Transaction type not implemented")
        }
    }

    init {
        requireNotNull(account) { "Account required to create records" }
        header = account.header
    }
}