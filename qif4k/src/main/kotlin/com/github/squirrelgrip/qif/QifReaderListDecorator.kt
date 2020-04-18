package com.github.squirrelgrip.qif

import java.io.PrintWriter
import java.util.*

class QifReaderListDecorator(private val qifReader: QifReader) {
    private val _transactions: MutableList<QifTransaction> = mutableListOf()

    init {
        qifReader.addTranasctionListener(object : TransactionListener {
            override fun onTransaction(transaction: QifTransaction) {
                _transactions.add(transaction)
            }
        })
        qifReader.load()
    }

    val transactions: List<QifTransaction>
        get() = _transactions.toList()

    @JvmOverloads
    fun writeCsv(writer: PrintWriter, dateFormat: String? = QifReader.DEFAULT_DATE_FORMAT) {
        writer.println("DATE,CATEGORY,PAYEE,TOTAL,MEMO")
        transactions.forEach {
            if (it is QifCashTransaction) {
                writer.println(it.toCsv(dateFormat))
            }
        }
    }

}