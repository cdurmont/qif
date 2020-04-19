package com.github.squirrelgrip.qif4k.write

import com.github.squirrelgrip.qif4k.QifAccount

/**
 * Special case of QifRecord appears at the top of the output and determines the
 * header value for following records
 *
 * @author carbontax@gmail.com
 */
class QifAccountRecord(private val txn: QifAccount) {
    fun asFormattedRecord(): String {
        val sb = StringBuilder()
        sb.append(QifHeaderEnum.ACCOUNT.text)
        Utils.appendFieldValueToOutput("N", txn.name, sb)
        Utils.appendFieldValueToOutput("D", txn.description, sb)
        Utils.appendFieldValueToOutput("T", txn.type.label, sb)
        Utils.appendCurrencyValueToOutput("B", txn.balance, sb)
        sb.append(Utils.LINE_SEPARATOR).append(Utils.END_OF_RECORD)
                .append(Utils.LINE_SEPARATOR)
        return sb.toString()
    }

}