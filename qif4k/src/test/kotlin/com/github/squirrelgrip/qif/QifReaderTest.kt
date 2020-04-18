package com.github.squirrelgrip.qif

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.*
import java.math.BigDecimal
import java.time.LocalDate
import javax.xml.bind.annotation.XmlElement

class QifReaderTest {
    private lateinit var testObj: QifReader
    private var investmentCount = 0
    private var transactionCount = 0

    @BeforeEach
    fun setUp() {
        investmentCount = 0
        transactionCount = 0
        testObj = createQifReader(SIMPLE_BANK_QIF)
    }

    private fun createQifReader(qifString: String, dateFormat: String = QifReader.DEFAULT_DATE_FORMAT): QifReader {
        val buffer: InputStream = ByteArrayInputStream(qifString.toByteArray())
        val qifReader = QifReader(InputStreamReader(buffer), dateFormat)
        addListener(qifReader)
        return qifReader
    }

    private fun addListener(qifReader: QifReader) {
        qifReader.addTranasctionListener(object : TransactionListener {
            override fun onTransaction(transaction: QifTransaction) {
                if (transaction is QifCashTransaction) {
                    transactionCount++
                }
                if (transaction is QifInvestment) {
                    investmentCount++
                }
            }
        })
    }

    private fun createQifReaderFromFile(filename: String): QifReader {
        val qifReader = QifReader(filename)
        addListener(qifReader)
        return qifReader
    }

    @Test
    fun accountType_WhenCashAccount() {
        testObj = createQifReader(SIMPLE_CASH_QIF)
        Assertions.assertEquals(QifType.CASH, testObj!!.getQifType())
    }

    @Test
    fun transactions() {
        Assertions.assertEquals(QifType.BANK, testObj!!.getQifType())
        val listDecorator = QifReaderListDecorator(testObj!!)
        val transactions = listDecorator.transactions
        Assertions.assertEquals(1, transactions.size)
        val transaction = transactions[0] as QifCashTransaction
        Assertions.assertEquals(LocalDate.of(2010, 3, 3), transaction.date)
        Assertions.assertEquals(BigDecimal("-379.00"), transaction.total)
        Assertions.assertEquals("CITY OF SPRINGFIELD", transaction.payee)
    }

    @Test
    fun transactions_WhenComplexQif() {
        testObj = createQifReader(COMPLEX_BANK_QIF)
        val listDecorator = QifReaderListDecorator(testObj)
        val transactions = listDecorator.transactions
        Assertions.assertEquals(3, transactions.size)
        Assertions.assertEquals(3, transactionCount)
        Assertions.assertEquals(0, investmentCount)
        var transaction = transactions[0] as QifCashTransaction
        Assertions.assertEquals(LocalDate.of(2014, 6, 1), transaction.date)
        Assertions.assertEquals(BigDecimal("-1000.00"), transaction.total)
        Assertions.assertEquals("Bank Of Mortgage", transaction.payee)
        Assertions.assertEquals(2, transaction.splits.size)
        var splitTransaction = transaction.splits[0]
        Assertions.assertSame(transaction, splitTransaction.transaction)
        Assertions.assertEquals(null, splitTransaction.memo)
        Assertions.assertEquals(BigDecimal("-253.64"), splitTransaction.amount)
        Assertions.assertEquals("linda", splitTransaction.category)
        splitTransaction = transaction.splits[1]
        Assertions.assertSame(transaction, splitTransaction.transaction)
        Assertions.assertEquals(null, splitTransaction.memo)
        Assertions.assertEquals(BigDecimal("-746.36"), splitTransaction.amount)
        Assertions.assertEquals("Mort Int", splitTransaction.category)
        transaction = transactions[1] as QifCashTransaction
        Assertions.assertEquals(LocalDate.of(2014, 6, 2), transaction.date)
        Assertions.assertEquals(BigDecimal("75.00"), transaction.total)
        Assertions.assertEquals("Deposit", transaction.payee)
        Assertions.assertEquals(0, transaction.splits.size)
        transaction = transactions[2] as QifCashTransaction
        Assertions.assertEquals(LocalDate.of(2014, 6, 3), transaction.date)
        Assertions.assertEquals(BigDecimal("-10.00"), transaction.total)
        Assertions.assertEquals("Anthony Hopkins", transaction.payee)
        Assertions.assertEquals(6, transaction.address.size)
        Assertions.assertEquals("P.O. Box 27027", transaction.address[0])
        Assertions.assertEquals("Tucson, AZ", transaction.address[1])
        Assertions.assertEquals("85726", transaction.address[2])
        Assertions.assertEquals("", transaction.address[3])
        Assertions.assertEquals("", transaction.address[4])
        Assertions.assertEquals("", transaction.address[5])
        Assertions.assertEquals(0, transaction.splits.size)
        val writer = StringWriter()
        listDecorator.writeCsv(PrintWriter(writer))
        Assertions.assertEquals("DATE,CATEGORY,PAYEE,TOTAL,MEMO\n6/1/14,linda,Bank Of Mortgage,-1000.00,\n6/2/14,,Deposit,75.00,\n6/3/14,Entertain,Anthony Hopkins,-10.00,Film\n", writer
                .buffer.toString())
    }

    @Test
    fun investment_WithSimpleInvestment() {
        testObj = createQifReader(SIMPLE_INVESTMENT_QIF, "MM/dd/yy")
        val listDecorator = QifReaderListDecorator(testObj!!)
        val transactions = listDecorator.transactions
        Assertions.assertEquals(1, transactions.size)
        val investment = transactions[0] as QifInvestment
        Assertions.assertEquals(LocalDate.of(2007, 12, 21), investment.date)
        Assertions.assertEquals("Purchase of 100 shares of IBM stock on 21 December 2007 at $110.10 per share", investment.memo)
        Assertions.assertEquals(BigDecimal("100"), investment.quantity)
        Assertions.assertEquals(BigDecimal("110.10"), investment.price)
        Assertions.assertEquals(BigDecimal("11010.00"), investment.total)
        Assertions.assertEquals("Buy", investment.action)
        Assertions.assertEquals("IBM", investment.security)
    }

    companion object {
        const val SIMPLE_BANK_QIF = "!Type:Bank\nD03/03/10\nT-379.00\nPCITY OF SPRINGFIELD\n^\n"
        const val COMPLEX_BANK_QIF = "!Type:Bank\nD6/ 1/14\nT-1,000.00\nN1005\nPBank Of Mortgage\nLlinda\nSlinda\n$-253.64\nSMort Int\n$-746.36\n^\nD6/ 2/14\nT75.00\nPDeposit\n^\nD6/ 3/14\nT-10.00\nPAnthony Hopkins\nMFilm\nLEntertain\nAP.O. Box 27027\nATucson, AZ\nA85726\nA\nA\nA\n^\n"
        const val SIMPLE_CASH_QIF = "!Type:Cash\nD03/03/10\nT-379.00\nPCITY OF SPRINGFIELD\n^\n"
        const val SIMPLE_INVESTMENT_QIF = "!Type:Invst\nD12/21/07\nNBuy\nYIBM\nT11010.00\nI110.10\nQ100\nMPurchase of 100 shares of IBM stock on 21 December 2007 at $110.10 per share\n^\n"
    }
}