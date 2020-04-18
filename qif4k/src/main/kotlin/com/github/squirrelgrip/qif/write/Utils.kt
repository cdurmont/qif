package com.github.squirrelgrip.qif.write

import com.github.squirrelgrip.qif.QifReader
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Utils {
    const val LINE_SEPARATOR = "\n"
    const val END_OF_RECORD = "^"
    var formatter = DateTimeFormatter.ofPattern(QifReader.Companion.DEFAULT_DATE_FORMAT)
    private fun appendStringValueToOutput(key: String, value: String?,
                                          sb: StringBuilder) {
        if (value != null && !value.isEmpty()) {
            if (sb.length > 0) {
                sb.append(LINE_SEPARATOR)
            }
            sb.append(key).append(value)
        }
    }

    private fun appendDateValueToOutput(key: String, date: LocalDate,
                                        sb: StringBuilder) {
        val value = formatter.format(date)
        appendStringValueToOutput(key, value, sb)
    }

    private fun appendBigDecimalValueToOutput(key: String, value: BigDecimal,
                                              sb: StringBuilder) {
        val stringValue = value.toPlainString()
        appendStringValueToOutput(key, stringValue, sb)
    }

    fun appendFieldValueToOutput(key: String, value: Any?,
                                 sb: StringBuilder) {
        if (value is LocalDate) {
            appendDateValueToOutput(key, value, sb)
        } else if (value is BigDecimal) {
            appendBigDecimalValueToOutput(key, value, sb)
        } else {
            appendStringValueToOutput(key, value.toString(), sb)
        }
    }

    @kotlin.jvm.JvmStatic
	fun printCurrency(currency: BigDecimal?): String {
        val df = DecimalFormat("#0.00###")
        return df.format(currency)
    }

    fun appendCurrencyValueToOutput(key: String,
                                    value: BigDecimal?, sb: StringBuilder) {
        val stringValue = printCurrency(value)
        appendStringValueToOutput(key, stringValue, sb)
    }
}