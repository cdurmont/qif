package com.github.squirrelgrip.qif4k.write

import com.github.squirrelgrip.qif4k.QifInvestment

class QifInvestmentRecord(private val txn: QifInvestment) : AbstractQifRecord() {
    override val header: QifHeaderEnum
        get() = QifHeaderEnum.INVESTMENT

    override fun formatTransaction(): String? {
        var txnString: String? = null
        val action: QifInvestmentAction = QifInvestmentAction.forText(txn.action)
        txnString = when (action) {
            QifInvestmentAction.DIV -> formatDividendTransaction()
            QifInvestmentAction.TRANSFER_IN, QifInvestmentAction.TRANSFER_OUT -> formatTransferTransaction()
            else -> formatDefaultTransaction()
        }
        return txnString
    }

    private fun formatTransferTransaction(): String {
        val sb = StringBuilder()
        Utils.appendFieldValueToOutput("D", txn.date, sb)
        Utils.appendFieldValueToOutput("N", txn.action, sb)
        Utils.appendFieldValueToOutput("P", "Transfer", sb)
        Utils.appendCurrencyValueToOutput("T", txn.total, sb)
        Utils.appendFieldValueToOutput("L", txn.category, sb)
        Utils.appendCurrencyValueToOutput("$", txn.total, sb)
        return sb.toString()
    }

    private fun formatDefaultTransaction(): String {
        val sb = StringBuilder()
        Utils.appendFieldValueToOutput("D", txn.date, sb)
        Utils.appendFieldValueToOutput("N", txn.action, sb)
        Utils.appendFieldValueToOutput("Y", txn.security, sb)
        Utils.appendCurrencyValueToOutput("I", txn.price, sb)
        Utils.appendFieldValueToOutput("Q", txn.quantity, sb)
        Utils.appendCurrencyValueToOutput("T", txn.total, sb)
        Utils.appendCurrencyValueToOutput("O", txn.commission, sb)
        Utils.appendFieldValueToOutput("M", txn.memo, sb)
        return sb.toString()
    }

    private fun formatDividendTransaction(): String {
        val sb = StringBuilder()
        Utils.appendFieldValueToOutput("D", txn.date, sb)
        Utils.appendFieldValueToOutput("N", txn.action, sb)
        Utils.appendFieldValueToOutput("Y", txn.security, sb)
        Utils.appendCurrencyValueToOutput("I", txn.price, sb)
        Utils.appendCurrencyValueToOutput("T", txn.total, sb)
        Utils.appendFieldValueToOutput("M", txn.memo, sb)
        return sb.toString()
    }

}