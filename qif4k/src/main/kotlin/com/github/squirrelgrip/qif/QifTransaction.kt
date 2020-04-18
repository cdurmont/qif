package com.github.squirrelgrip.qif

import java.math.BigDecimal
import java.time.LocalDate

open class QifTransaction(
        var date: LocalDate? = null,
        var total: BigDecimal? = null,
        var memo: String? = null
)