package com.github.squirrelgrip.qif.write;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import com.github.squirrelgrip.qif.QifAccount;

import com.github.squirrelgrip.qif.QifInvestment;

public class TestUtils {

	public static QifInvestment buildInvst(double price, double quantity,
			double commission) {
		QifInvestment invst = new QifInvestment();
		invst.setAction(QifInvestmentAction.BUY.getText());
		invst.setCommission(BigDecimal.valueOf(commission));
		invst.setDate(LocalDate.of(2014, 3, 30));
		invst.setMemo("Hello memo");
		invst.setPrice(BigDecimal.valueOf(price));
		invst.setQuantity(BigDecimal.valueOf(quantity));
		invst.setSecurity("Foo Bar Chemicals");
		invst.setTotal(BigDecimal.valueOf((quantity * price) + commission));
		return invst;
	}

	/**
	 * http://stackoverflow.com/a/326440/980454
	 */
	public static String readTestSampleFile(String path) throws IOException {
		Charset encoding = StandardCharsets.ISO_8859_1;
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static QifAccount buildPortfolioAccount() {
		QifAccount account = new QifAccount(QifAccountTypeEnum.PORTFOLIO,
				"Foo Portfolio", "My description", BigDecimal.valueOf(1000.07d));
		return account;
	}

	public static QifInvestment buildDividend(double price, double total, String memo) {
		QifInvestment div = buildInvst(price, 0, 0);
		div.setAction(QifInvestmentAction.DIV.getText());
		div.setTotal(BigDecimal.valueOf(total));
		div.setMemo(memo);
		return div;
	}
}
