package com.github.squirrelgrip.qif.write

import com.github.squirrelgrip.qif.QifInterestIncome
import com.github.squirrelgrip.qif.QifTransaction

class QifInterestIncomeRecord(override val header: QifHeaderEnum?, txn: QifTransaction) : AbstractQifRecord() {
    private val txn: QifInterestIncome

    override fun formatTransaction(): String? {
        val sb = StringBuilder()
        Utils.appendFieldValueToOutput("D", txn.date, sb)
        Utils.appendFieldValueToOutput("N", txn.action, sb)
        Utils.appendCurrencyValueToOutput("T", txn.total, sb)
        return sb.toString()
    }

    init {
        this.txn = txn as QifInterestIncome
    }
}