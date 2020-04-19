package com.github.squirrelgrip.qif4j.write;

import com.github.squirrelgrip.qif4j.write.Utils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

	@Test
	public void testPrintCurrency() {
		double amount = 7d;
		assertEquals("7.00", Utils.printCurrency(BigDecimal.valueOf(amount)), "Two zeros added");
		amount = 7.5d;
		assertEquals("7.50", Utils.printCurrency(BigDecimal.valueOf(amount)), "One zero added");
		amount = 7.051d;
		assertEquals("7.051", Utils.printCurrency(BigDecimal.valueOf(amount)), "Three decimal places");
		amount = 7.0517d;
		assertEquals("7.0517", Utils.printCurrency(BigDecimal.valueOf(amount)), "Four decimal places");
		amount = 7.05171d;
		assertEquals("7.05171", Utils.printCurrency(BigDecimal.valueOf(amount)), "Five decimal places");
		amount = 7.051719d;
		assertEquals("7.05172", Utils.printCurrency(BigDecimal.valueOf(amount)), "Rounded after five decimal places");
	}

}
