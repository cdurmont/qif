package com.github.squirrelgrip.qif

import com.github.squirrelgrip.qif.write.QifAccountTypeEnum
import com.github.squirrelgrip.qif.write.QifInvestmentAction
import java.math.BigDecimal
import java.time.LocalDate

class QifInterestIncome(
        val type: QifAccountTypeEnum,
        date: LocalDate? = null,
        total: BigDecimal = BigDecimal.ZERO,
        memo: String? = null
) : QifTransaction(date, total, memo) {
    val action: String = QifInvestmentAction.INTEREST_INCOME.text
}