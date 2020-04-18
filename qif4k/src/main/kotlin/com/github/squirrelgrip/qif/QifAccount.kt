package com.github.squirrelgrip.qif

import com.github.squirrelgrip.qif.write.QifAccountTypeEnum
import com.github.squirrelgrip.qif.write.QifHeaderEnum
import java.math.BigDecimal

/**
 * Immutable class represents an Account in QIF specification
 */
data class QifAccount(
        val type: QifAccountTypeEnum,
        val name: String,
        val description: String,
        val balance: BigDecimal
) : QifTransaction() {

    val header: QifHeaderEnum
        get() = if (type == QifAccountTypeEnum.PORTFOLIO) {
            QifHeaderEnum.INVESTMENT
        } else {
            throw IllegalStateException("Unknown account")
        }

}