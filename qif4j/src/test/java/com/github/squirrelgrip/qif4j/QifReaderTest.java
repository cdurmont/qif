package com.github.squirrelgrip.qif4j;

import com.github.squirrelgrip.qif4j.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class QifReaderTest {

	public static final String SIMPLE_BANK_QIF = "!Type:Bank\nD03/03/10\nT-379.00\nPCITY OF SPRINGFIELD\n^\n";
	public static final String COMPLEX_BANK_QIF = "!Type:Bank\nD6/ 1/14\nT-1,000.00\nN1005\nPBank Of Mortgage\nLlinda\nSlinda\n$-253.64\nSMort Int\n$-746.36\n^\nD6/ 2/14\nT75.00\nPDeposit\n^\nD6/ 3/14\nT-10.00\nPAnthony Hopkins\nMFilm\nLEntertain\nAP.O. Box 27027\nATucson, AZ\nA85726\nA\nA\nA\n^\n";
	public static final String SIMPLE_CASH_QIF = "!Type:Cash\nD03/03/10\nT-379.00\nPCITY OF SPRINGFIELD\n^\n";
	public static final String SIMPLE_INVESTMENT_QIF = "!Type:Invst\nD12/21/07\nNBuy\nYIBM\nT11010.00\nI110.10\nQ100\nMPurchase of 100 shares of IBM stock on 21 December 2007 at $110.10 per share\n^\n";
	private QifReader testObj;
	private int investmentCount;
	private int transactionCount;

	@BeforeEach
	public void setUp() throws Exception {
		investmentCount = 0;
		transactionCount = 0;
		testObj = createQifReader(SIMPLE_BANK_QIF);
	}

	private QifReader createQifReader(String qifString) {
		InputStream buffer = new ByteArrayInputStream(qifString.getBytes());
		QifReader qifReader = new QifReader(new InputStreamReader(buffer));
		addListener(qifReader);
		return qifReader;
	}

	private void addListener(QifReader qifReader) {
		qifReader.addTranasctionListener(new TransactionListener() {

			public void onTransaction(QifTransaction transaction) {
				if (transaction instanceof QifCashTransaction) {
					transactionCount++;
				}
				if (transaction instanceof QifInvestment) {
					investmentCount++;
				}
			}
		});
	}

	private QifReader createQifReaderFromFile(String filename) throws Exception {
		QifReader qifReader = new QifReader(filename);
		addListener(qifReader);
		return qifReader;
	}

	@Test
	public void getAccountType_WhenCashAccount() throws Exception {
		testObj = createQifReader(SIMPLE_CASH_QIF);
		assertEquals(QifType.CASH, testObj.getQifType());
	}

	@Test
	public void getTransactions() throws Exception {
		assertEquals(QifType.BANK, testObj.getQifType());
		QifReaderListDecorator listDecorator = new QifReaderListDecorator(testObj);
		List<QifTransaction> transactions = listDecorator.getTransactions();
		assertEquals(1, transactions.size());
		QifCashTransaction transaction = (QifCashTransaction)transactions.get(0);
		assertEquals(LocalDate.of(2010, 3, 3), transaction.getDate());
		assertEquals(new BigDecimal("-379.00"), transaction.getTotal());
		assertEquals("CITY OF SPRINGFIELD", transaction.getPayee());
	}

	@Test
	public void getTransactions_WhenComplexQif() throws Exception {
		testObj = createQifReader(COMPLEX_BANK_QIF);
		QifReaderListDecorator listDecorator = new QifReaderListDecorator(testObj);
		List<QifTransaction> transactions = listDecorator.getTransactions();
		assertEquals(3, transactions.size());
		assertEquals(3, transactionCount);
		assertEquals(0, investmentCount);

		QifCashTransaction transaction = (QifCashTransaction)transactions.get(0);
		assertEquals(LocalDate.of(2014, 6, 1), transaction.getDate());
		assertEquals(new BigDecimal("-1000.00"), transaction.getTotal());
		assertEquals("Bank Of Mortgage", transaction.getPayee());
		assertEquals(2, transaction.getSplits().size());

		QifSplitTransaction splitTransaction = transaction.getSplits().get(0);
		assertSame(transaction, splitTransaction.getTransaction());
		assertEquals(null, splitTransaction.getMemo());
		assertEquals(new BigDecimal("-253.64"), splitTransaction.getAmount());
		assertEquals("linda", splitTransaction.getCategory());

		splitTransaction = transaction.getSplits().get(1);
		assertSame(transaction, splitTransaction.getTransaction());
		assertEquals(null, splitTransaction.getMemo());
		assertEquals(new BigDecimal("-746.36"), splitTransaction.getAmount());
		assertEquals("Mort Int", splitTransaction.getCategory());

		transaction = (QifCashTransaction)transactions.get(1);
		assertEquals(LocalDate.of(2014, 6, 2), transaction.getDate());
		assertEquals(new BigDecimal("75.00"), transaction.getTotal());
		assertEquals("Deposit", transaction.getPayee());
		assertEquals(0, transaction.getSplits().size());

		transaction = (QifCashTransaction)transactions.get(2);
		assertEquals(LocalDate.of(2014, 6, 3), transaction.getDate());
		assertEquals(new BigDecimal("-10.00"), transaction.getTotal());
		assertEquals("Anthony Hopkins", transaction.getPayee());
		assertEquals(6, transaction.getAddress().size());
		assertEquals("P.O. Box 27027", transaction.getAddress().get(0));
		assertEquals("Tucson, AZ", transaction.getAddress().get(1));
		assertEquals("85726", transaction.getAddress().get(2));
		assertEquals("", transaction.getAddress().get(3));
		assertEquals("", transaction.getAddress().get(4));
		assertEquals("", transaction.getAddress().get(5));

		assertEquals(0, transaction.getSplits().size());

		StringWriter writer = new StringWriter();
		listDecorator.writeCsv(new PrintWriter(writer));
		assertEquals("DATE,CATEGORY,PAYEE,TOTAL,MEMO\n6/1/14,linda,Bank Of Mortgage,-1000.00,\n6/2/14,,Deposit,75.00,\n6/3/14,Entertain,Anthony Hopkins,-10.00,Film\n", writer
		        .getBuffer().toString());

	}

	@Test
	public void getInvestment_WithSimpleInvestment() throws Exception {
		testObj = createQifReader(SIMPLE_INVESTMENT_QIF);
		testObj.setDateFormat("MM/dd/yy");
		QifReaderListDecorator listDecorator = new QifReaderListDecorator(testObj);
		List<QifTransaction> transactions = listDecorator.getTransactions();

		assertEquals(1, transactions.size());

		QifInvestment investment = (QifInvestment)transactions.get(0);
		assertEquals(LocalDate.of(2007, 12, 21), investment.getDate());
		assertEquals("Purchase of 100 shares of IBM stock on 21 December 2007 at $110.10 per share", investment.getMemo());
		assertEquals(new BigDecimal("100"), investment.getQuantity());
		assertEquals(new BigDecimal("110.10"), investment.getPrice());
		assertEquals(new BigDecimal("11010.00"), investment.getTotal());
		assertEquals("Buy", investment.getAction());
		assertEquals("IBM", investment.getSecurity());
	}

}
