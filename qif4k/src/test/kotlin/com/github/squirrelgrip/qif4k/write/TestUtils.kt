package com.github.squirrelgrip.qif4k.write

import com.github.squirrelgrip.qif4k.QifAccount
import com.github.squirrelgrip.qif4k.QifInvestment
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

object TestUtils {
    fun buildInvst(price: Double, quantity: Double,
                   commission: Double): QifInvestment {
        val invst = QifInvestment()
        invst.action = QifInvestmentAction.BUY.text
        invst.commission = BigDecimal.valueOf(commission)
        invst.date = LocalDate.of(2014, 3, 30)
        invst.memo = "Hello memo"
        invst.price = BigDecimal.valueOf(price)
        invst.quantity = BigDecimal.valueOf(quantity)
        invst.security = "Foo Bar Chemicals"
        invst.total = BigDecimal.valueOf(quantity * price + commission)
        return invst
    }

    fun readTestSampleFile(path: String?): String {
        val encoding = StandardCharsets.ISO_8859_1
        val encoded = Files.readAllBytes(Paths.get(path))
        return String(encoded, encoding)
    }

    fun buildPortfolioAccount(): QifAccount {
        return QifAccount(QifAccountTypeEnum.PORTFOLIO,
                "Foo Portfolio", "My description", BigDecimal.valueOf(1000.07))
    }

    fun buildDividend(price: Double, total: Double, memo: String?): QifInvestment {
        val div = buildInvst(price, 0.0, 0.0)
        div.action = QifInvestmentAction.DIV.text
        div.total = BigDecimal.valueOf(total)
        div.memo = memo
        return div
    }
}