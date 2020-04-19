package com.github.squirrelgrip.qif4k.write

import com.github.squirrelgrip.qif4k.write.Utils.printCurrency
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class UtilsTest {
    @Test
    fun testPrintCurrency() {
        var amount = 7.0
        Assertions.assertEquals("7.00", printCurrency(BigDecimal.valueOf(amount)), "Two zeros added")
        amount = 7.5
        Assertions.assertEquals("7.50", printCurrency(BigDecimal.valueOf(amount)), "One zero added")
        amount = 7.051
        Assertions.assertEquals("7.051", printCurrency(BigDecimal.valueOf(amount)), "Three decimal places")
        amount = 7.0517
        Assertions.assertEquals("7.0517", printCurrency(BigDecimal.valueOf(amount)), "Four decimal places")
        amount = 7.05171
        Assertions.assertEquals("7.05171", printCurrency(BigDecimal.valueOf(amount)), "Five decimal places")
        amount = 7.051719
        Assertions.assertEquals("7.05172", printCurrency(BigDecimal.valueOf(amount)), "Rounded after five decimal places")
    }
}