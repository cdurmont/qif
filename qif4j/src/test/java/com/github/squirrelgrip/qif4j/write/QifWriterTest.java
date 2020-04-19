package com.github.squirrelgrip.qif4j.write;

import com.github.squirrelgrip.qif4j.QifAccount;
import com.github.squirrelgrip.qif4j.QifInterestIncome;
import com.github.squirrelgrip.qif4j.QifInvestment;
import com.github.squirrelgrip.qif4j.QifTransaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QifWriterTest {

	@Test
	public void testEmptyAccountRegister() throws IOException {
		Writer writer = new StringWriter();
		QifWriter qifWriter = new QifWriter(writer);
		List<QifTransaction> txnList = new ArrayList<QifTransaction>();
		QifAccount account = TestUtils.buildPortfolioAccount();
		qifWriter.write(account, txnList);
		String expected = TestUtils
				.readTestSampleFile("src/test/resources/writer/invst_account_empty.qif");
		assertEquals(expected, writer.toString());
	}

	@Test
	public void testAccountRegisterWithSingleTxn() throws IOException {
		Writer writer = new StringWriter();
		QifWriter qifWriter = new QifWriter(writer);
		List<QifTransaction> txnList = new ArrayList<QifTransaction>();
		QifInvestment invst = TestUtils.buildInvst(10.952d, 100d, 9.95d);
		txnList.add(invst);
		QifAccount account = new QifAccount(QifAccountTypeEnum.PORTFOLIO,
				"Foo Portfolio", "My description", BigDecimal.valueOf(1000.07d));
		qifWriter.write(account, txnList);

		String expected = TestUtils
				.readTestSampleFile("src/test/resources/writer/invst_account_single.qif");
		assertEquals(expected, writer.toString());
	}

	@Test
	public void testAccountRegisterWithMultipleTxn() throws IOException {
		Writer writer = new StringWriter();
		QifWriter qifWriter = new QifWriter(writer);
		List<QifTransaction> txnList = new ArrayList<QifTransaction>();
		QifInvestment invst = TestUtils.buildInvst(10.952d, 100d, 9.95d);
		invst.setMemo("Buy 100 FOOB for $10.952. Commission: $9.95");
		txnList.add(invst);

		invst = TestUtils.buildInvst(13.05d, 100d, 9.95d);
		invst.setMemo("Sell 100 FOOB for capital gain. Commission: $9.95");
		invst.setAction(QifInvestmentAction.SELL.getText());
		invst.setDate(LocalDate.of(2014, 4, 5));
		txnList.add(invst);

		QifInterestIncome ii = new QifInterestIncome(
				QifAccountTypeEnum.PORTFOLIO, LocalDate.of(2014, 4, 6),
				BigDecimal.valueOf(0.12d));
		txnList.add(ii);

		invst = TestUtils.buildDividend(12.99d, 7.5d, ".15 on 50 shares");
		invst.setDate(LocalDate.of(2014, 5, 30));
		txnList.add(invst);

		invst = new QifInvestment();
		invst.setDate(LocalDate.of(2014, 6, 13));
		invst.setAction(QifInvestmentAction.TRANSFER_IN.getText());
		invst.setTotal(BigDecimal.valueOf(500d));
		invst.setCategory("[Test Checking]");
		txnList.add(invst);

		QifAccount account = new QifAccount(QifAccountTypeEnum.PORTFOLIO,
				"Foo Portfolio", "My description", BigDecimal.valueOf(1000.07d));
		qifWriter.write(account, txnList);

		String expected = TestUtils
				.readTestSampleFile("src/test/resources/writer/invst_account_multi.qif");
		assertEquals(expected, writer.toString());
	}
}
