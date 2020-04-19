package com.github.squirrelgrip.qif4j.write;

public enum QifInvestmentAction {
	BUY("Buy"), SELL("Sell"), DIV("Div"), INTEREST_INCOME("IntInc"), TRANSFER_IN("XIn"), TRANSFER_OUT("XOut");
	
	private String text;
	
	private QifInvestmentAction(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public static QifInvestmentAction forText(String text) {
		for(QifInvestmentAction action: QifInvestmentAction.values()) {
			if(action.getText().equals(text)) {
				return action;
			}
		}
		throw new IllegalArgumentException(text + " is not a QIF Invst action");
	}
}
