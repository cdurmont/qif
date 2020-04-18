package com.github.squirrelgrip.qif.write;

public enum QifHeaderEnum {
	ACCOUNT("!Type:Account"), INVESTMENT("!Type:Invst"), CASH("!Type:Cash");
	private String text;

	private QifHeaderEnum(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
