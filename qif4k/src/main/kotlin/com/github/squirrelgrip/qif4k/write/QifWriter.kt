package com.github.squirrelgrip.qif4k.write

import com.github.squirrelgrip.qif4k.QifAccount
import com.github.squirrelgrip.qif4k.QifTransaction
import java.io.IOException
import java.io.Writer

class QifWriter(private val writer: Writer) {
    @Throws(IOException::class)
    fun write(account: QifAccount, txnSource: List<QifTransaction>) {
        writer.append(QifAccountRecord(account).asFormattedRecord())
        val factory = QifRecordFactory(account)
        var header: QifHeaderEnum? = null
        txnSource.forEach {
            val record = factory.forTransaction(it)
            writer.append(record.asFormattedRecord(header))
            header = record.header
        }
    }

}