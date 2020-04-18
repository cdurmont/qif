package com.github.squirrelgrip.qif;

import com.github.squirrelgrip.qif.write.QifAccountTypeEnum;
import com.github.squirrelgrip.qif.write.QifInvestmentAction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Immutable class represents an Account in QIF specification
 * 
 * @author carbontax@gmail.com
 *
 */
public class QifInterestIncome extends QifTransaction {
	private QifAccountTypeEnum type;

	public QifInterestIncome(QifAccountTypeEnum type, LocalDate date, BigDecimal total) {
		super();
		this.type = type;
		this.setDate(date);
		this.setTotal(total);
	}

	public QifAccountTypeEnum getType() {
		return type;
	}
	
	public String getAction() {
		return QifInvestmentAction.INTEREST_INCOME.getText();
	}
}
