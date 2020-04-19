package com.github.squirrelgrip.qif4k

import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.util.*

class QifCashTransaction : QifTransaction() {
    private val _address: MutableList<String> = mutableListOf()
    var category: String? = null
    var clearedStatus: String? = null
    private var currentSplitTransaction: QifSplitTransaction? = null
    var payee: String? = null
    var reference: String? = null
    private val splitTransactions: MutableList<QifSplitTransaction> = ArrayList()

    fun addAddress(address: String) {
        this._address.add(address)
    }

    private fun checkSplitTransaction() {
        if (currentSplitTransaction == null) {
            createSplitTransaction()
        }
    }

    private fun createSplitTransaction() {
        currentSplitTransaction = QifSplitTransaction(this)
        splitTransactions.add(currentSplitTransaction!!)
    }

    val address: List<String>
        get() = _address.toList()


    val splits: List<QifSplitTransaction>
        get() = splitTransactions

    val getSplitTransactions: List<QifSplitTransaction>
        get() = splitTransactions


    fun setSplitAmount(splitAmount: String) {
        checkSplitTransaction()
        if (currentSplitTransaction!!.amount != null) {
            createSplitTransaction()
        }
        currentSplitTransaction!!.amount = BigDecimal(splitAmount)
    }

    fun setSplitCategory(splitCategory: String?) {
        checkSplitTransaction()
        if (currentSplitTransaction!!.category != null) {
            createSplitTransaction()
        }
        currentSplitTransaction!!.category = splitCategory
    }

    fun setSplitMemo(splitMemo: String?) {
        checkSplitTransaction()
        if (currentSplitTransaction!!.memo != null) {
            createSplitTransaction()
        }
        currentSplitTransaction!!.memo = splitMemo
    }

    fun toCsv(dateFormat: String?): String {
        return String.format("%s,%s,%s,%s,%s", date?.format(DateTimeFormatter.ofPattern(dateFormat)), empty(category),
                empty(payee), total, empty(memo))
    }

    private fun empty(value: String?): String {
        return value ?: ""
    }
}