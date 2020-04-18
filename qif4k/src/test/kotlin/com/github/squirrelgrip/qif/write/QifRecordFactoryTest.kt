package com.github.squirrelgrip.qif.write

import com.github.squirrelgrip.qif.QifAccount
import com.github.squirrelgrip.qif.QifInvestment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class QifRecordFactoryTest {
    @Test
    fun testInvestment() {
        val txn = QifInvestment()
        txn.action = "Buy"
        txn.security = "Foo Industries"
        txn.date = LocalDate.of(2014, 3, 21)
        txn.price = BigDecimal.valueOf(25.66)
        txn.quantity = BigDecimal.valueOf(100L)
        txn.commission = BigDecimal.valueOf(29.95)
        txn.memo = "Memo text"
        txn.total = BigDecimal.valueOf(2566.0 + 29.95)
        val factory = QifRecordFactory(TestUtils.buildPortfolioAccount())
        val invstRecord = factory.forTransaction(txn)
        Assertions.assertEquals(
                "!Type:Invst\nD3/21/14\nNBuy\nYFoo Industries\nI25.66\nQ100\nT2595.95\nO29.95\nMMemo text\n^\n",
                invstRecord.asFormattedRecord(null), "Invst record format")
    }

    @Test
    fun testAccountRecord() {
        val name = "My Portfolio"
        val desc = "Shares held in XYZ Broker account"
        val balance = BigDecimal.valueOf(10507.33)
        val acc = QifAccount(QifAccountTypeEnum.PORTFOLIO, name, desc,
                balance)
        val record = QifAccountRecord(acc)
        Assertions.assertEquals("""
    !Type:Account
    N$name
    D$desc
    T${QifAccountTypeEnum.PORTFOLIO.label}
    B$balance
    ^
    
    """.trimIndent(), record.asFormattedRecord(), "Account record format")
    }
}