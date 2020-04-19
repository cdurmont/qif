package com.github.squirrelgrip.qif4k

import java.io.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class QifReader @JvmOverloads constructor(
        reader: Reader,
        dateFormat: String = DEFAULT_DATE_FORMAT
) : Reader() {
    enum class LoadStatus {
        LOADED, NOT_STARTED, QIF_TYPE_LOADED
    }

    private val reader: BufferedReader
    private val formatter: DateTimeFormatter
    private val investments: MutableList<QifInvestment> = ArrayList()
    private var loadStatus = LoadStatus.NOT_STARTED
    private var qifType = QifType.UNKNOWN
    private val listeners: MutableCollection<TransactionListener> = mutableSetOf()

    constructor(file: File) : this(FileReader(file), DEFAULT_DATE_FORMAT)
    constructor(file: File, dateFormat: String) : this(FileReader(file), dateFormat)
    constructor(filename: String, dateFormat: String) : this(File(filename))
    constructor(filename: String) : this(File(filename), DEFAULT_DATE_FORMAT)

    override fun close() {
        reader.close()
    }

    fun getInvestments(): List<QifInvestment> {
        load()
        return investments
    }

    fun getQifType(): QifType {
        readQifType()
        return qifType
    }

    fun load() {
        when (getQifType()) {
            QifType.INVST -> {
                loadInvestments()
                loadTransactions()
            }
            else -> loadTransactions()
        }
    }

    private fun loadInvestments(): List<QifInvestment> {
        try {
            var line = reader!!.readLine()
            var investment = QifInvestment()
            while (line != null) {
                if ("^" == line) {
                    investments.add(investment)
                    fireOnTransactionHandler(investment)
                    investment = QifInvestment()
                } else {
                    processInvestmentDetail(investment, line)
                }
                line = reader.readLine()
            }
        } catch (e: Exception) {
            throw QifReaderException(e)
        }
        return investments
    }

    private fun fireOnTransactionHandler(transaction: QifTransaction) {
        for (listener in listeners) {
            listener.onTransaction(transaction)
        }
    }

    private fun loadTransactions() {
        if (loadStatus != LoadStatus.LOADED) {
            try {
                var line = reader!!.readLine()
                var transaction = QifCashTransaction()
                while (line != null) {
                    if ("^" == line) {
                        fireOnTransactionHandler(transaction)
                        transaction = QifCashTransaction()
                    } else {
                        processTransactionDetail(transaction, line)
                    }
                    line = reader.readLine()
                }
                loadStatus = LoadStatus.LOADED
            } catch (e: Exception) {
                throw QifReaderException(e)
            }
        }
    }

    fun processInvestmentDetail(investment: QifInvestment, line: String) {
        val rowType = line[0]
        val rowData = line.substring(1)
        when (rowType) {
            'D' -> setDate(investment, rowData)
            'N' -> investment.action = rowData
            'Y' -> investment.security = rowData
            'I' -> setPrice(investment, rowData)
            'Q' -> setQuantity(investment, rowData)
            'T', '$' -> setTotal(investment, rowData)
            'M' -> investment.memo = rowData
        }
    }

    // D Date
    // T Amount
    // C Cleared status
    // N Num (check or reference number)
    // P Payee
    // M Memo
    // A Address (up to five lines;the sixth line is an optional message)
    // L Category (Category/Subcategory/Transfer/Class)
    // S Category in split (Category/Transfer/Class)
    // E Memo in split
    // $ Dollar amount of split
    // ^ End of entry
    fun processTransactionDetail(transaction: QifCashTransaction, line: String) {
        val rowType = line[0]
        val rowData = line.substring(1)
        when (rowType) {
            'D' -> setDate(transaction, rowData)
            'T' -> setTotal(transaction, rowData)
            'N' -> transaction.reference = rowData
            'C' -> transaction.clearedStatus = rowData
            'P' -> transaction.payee = rowData
            'M' -> transaction.memo = rowData
            'A' -> transaction.addAddress(rowData)
            'L' -> transaction.category = rowData
            'S' -> transaction.setSplitCategory(rowData)
            'E' -> transaction.setSplitMemo(rowData)
            '$' -> transaction.setSplitAmount(rowData)
        }
    }

    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        return reader.read(cbuf, off, len)
    }

    private fun readQifType() {
        if (loadStatus == LoadStatus.NOT_STARTED) {
            try {
                val line = reader!!.readLine()
                if (line.startsWith("!Type:")) {
                    qifType = QifType.valueOf(line.substring(6).toUpperCase())
                } else {
                    throw QifReaderException("File Format is not a QIF format")
                }
                loadStatus = LoadStatus.QIF_TYPE_LOADED
            } catch (e: QifReaderException) {
                throw e
            } catch (e: Exception) {
                throw QifReaderException(e)
            }
        }
    }

    private fun setDate(transaction: QifTransaction, dateString: String) {
        var dateString = dateString
        dateString = dateString.replace(" ", "")
        transaction.date = LocalDate.parse(dateString, formatter)
    }

    private fun setPrice(investment: QifInvestment, priceString: String) {
        var priceString = priceString
        priceString = priceString.replace(",", "")
        investment.price = BigDecimal(priceString)
    }

    private fun setQuantity(investment: QifInvestment, quantityString: String) {
        var quantityString = quantityString
        quantityString = quantityString.replace(",", "")
        investment.quantity = BigDecimal(quantityString)
    }

    private fun setTotal(transaction: QifTransaction, totalString: String) {
        var totalString = totalString
        totalString = totalString.replace(",", "")
        transaction.total = BigDecimal(totalString)
    }

    fun addTranasctionListener(transactionListener: TransactionListener) {
        listeners.add(transactionListener)
    }

    companion object {
        const val DEFAULT_DATE_FORMAT = "M/d/yy"
    }

    init {
        if (reader is BufferedReader) {
            this.reader = reader
        } else {
            this.reader = BufferedReader(reader)
        }
        formatter = DateTimeFormatter.ofPattern(dateFormat)
    }
}