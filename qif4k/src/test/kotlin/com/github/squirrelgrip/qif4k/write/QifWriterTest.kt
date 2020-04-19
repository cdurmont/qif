package com.github.squirrelgrip.qif4k.write

import com.github.squirrelgrip.qif4k.QifAccount
import com.github.squirrelgrip.qif4k.QifInterestIncome
import com.github.squirrelgrip.qif4k.QifInvestment
import com.github.squirrelgrip.qif4k.QifTransaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.StringWriter
import java.io.Writer
import java.math.BigDecimal
import java.time.LocalDate

class QifWriterTest {
    @Test
    fun testEmptyAccountRegister() {
        val writer: Writer = StringWriter()
        val qifWriter = QifWriter(writer)
        val txnList: List<QifTransaction> = mutableListOf()
        val account = TestUtils.buildPortfolioAccount()
        qifWriter.write(account, txnList)
        val expected = TestUtils.readTestSampleFile("src/test/resources/writer/invst_account_empty.qif")
        Assertions.assertEquals(expected, writer.toString())
    }

    @Test
    fun testAccountRegisterWithSingleTxn() {
        val writer: Writer = StringWriter()
        val qifWriter = QifWriter(writer)
        val txnList: MutableList<QifTransaction> = mutableListOf()
        val invst = TestUtils.buildInvst(10.952, 100.0, 9.95)
        txnList.add(invst)
        val account = QifAccount(QifAccountTypeEnum.PORTFOLIO,
                "Foo Portfolio", "My description", BigDecimal.valueOf(1000.07))
        qifWriter.write(account, txnList)
        val expected = TestUtils.readTestSampleFile("src/test/resources/writer/invst_account_single.qif")
        Assertions.assertEquals(expected, writer.toString())
    }

    @Test
    fun testAccountRegisterWithMultipleTxn() {
        val writer: Writer = StringWriter()
        val qifWriter = QifWriter(writer)
        val txnList: MutableList<QifTransaction> = mutableListOf()
        var invst = TestUtils.buildInvst(10.952, 100.0, 9.95)
        invst.memo = "Buy 100 FOOB for $10.952. Commission: $9.95"
        txnList.add(invst)
        invst = TestUtils.buildInvst(13.05, 100.0, 9.95)
        invst.memo = "Sell 100 FOOB for capital gain. Commission: $9.95"
        invst.action = QifInvestmentAction.SELL.text
        invst.date = LocalDate.of(2014, 4, 5)
        txnList.add(invst)
        val ii = QifInterestIncome(
                QifAccountTypeEnum.PORTFOLIO, LocalDate.of(2014, 4, 6),
                BigDecimal.valueOf(0.12))
        txnList.add(ii)
        invst = TestUtils.buildDividend(12.99, 7.5, ".15 on 50 shares")
        invst.date = LocalDate.of(2014, 5, 30)
        txnList.add(invst)
        invst = QifInvestment()
        invst.date = LocalDate.of(2014, 6, 13)
        invst.action = QifInvestmentAction.TRANSFER_IN.text
        invst.total = BigDecimal.valueOf(500.0)
        invst.category = "[Test Checking]"
        txnList.add(invst)
        val account = QifAccount(QifAccountTypeEnum.PORTFOLIO,
                "Foo Portfolio", "My description", BigDecimal.valueOf(1000.07))
        qifWriter.write(account, txnList)
        val expected = TestUtils.readTestSampleFile("src/test/resources/writer/invst_account_multi.qif")
        Assertions.assertEquals(expected, writer.toString())
    }
}