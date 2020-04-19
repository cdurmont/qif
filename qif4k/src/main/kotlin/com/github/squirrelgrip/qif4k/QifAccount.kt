package com.github.squirrelgrip.qif4k

import com.github.squirrelgrip.qif4k.write.QifAccountTypeEnum
import com.github.squirrelgrip.qif4k.write.QifHeaderEnum
import java.math.BigDecimal

/**
 * Immutable class represents an Account in QIF specification
 */
class QifAccount(
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