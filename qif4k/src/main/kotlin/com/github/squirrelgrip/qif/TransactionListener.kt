package com.github.squirrelgrip.qif

interface TransactionListener {
    fun onTransaction(transaction: QifTransaction)
}