package com.github.squirrelgrip.qif.write;

/**
 * Account types in QIF specification
 * 
 * @author carbontax@gmail.com
 *
 */
public enum QifAccountTypeEnum {
	PORTFOLIO("Port");
	
	private String label;
	
	private QifAccountTypeEnum(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
