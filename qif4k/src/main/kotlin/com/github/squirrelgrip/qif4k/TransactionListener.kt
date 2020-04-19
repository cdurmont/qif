package com.github.squirrelgrip.qif4k

interface TransactionListener {
    fun onTransaction(transaction: QifTransaction)
}