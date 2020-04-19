package com.github.squirrelgrip.qif4k

import java.math.BigDecimal

class QifSplitTransaction(
        val transaction: QifTransaction,
        var category: String? = null,
        var memo: String? = null,
        var amount: BigDecimal? = null
)