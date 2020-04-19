package com.github.squirrelgrip.qif4j.write;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.github.squirrelgrip.qif4j.QifReader;

public class Utils {
	public final static String LINE_SEPARATOR = "\n";
	public final static String END_OF_RECORD = "^";

	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(QifReader.DEFAULT_DATE_FORMAT);
	
	private static void appendStringValueToOutput(String key, String value,
			StringBuilder sb) {
		if (value != null && !value.isEmpty()) {
			if (sb.length() > 0) {
				sb.append(Utils.LINE_SEPARATOR);
			}
			sb.append(key).append(value);
		}
	}

	private static void appendDateValueToOutput(String key, LocalDate date,
			StringBuilder sb) {
			String value = Utils.formatter.format(date);
		appendStringValueToOutput(key, value, sb);
	}

	private static void appendBigDecimalValueToOutput(String key, BigDecimal value,
			StringBuilder sb) {
		String stringValue = value.toPlainString();
		appendStringValueToOutput(key, stringValue, sb);
	}

	public static void appendFieldValueToOutput(String key, Object value,
			StringBuilder sb) {
		if (value instanceof LocalDate) {
			appendDateValueToOutput(key, (LocalDate) value, sb);
		} else if (value instanceof BigDecimal) {
			appendBigDecimalValueToOutput(key, (BigDecimal) value, sb);
		} else {
			appendStringValueToOutput(key, value.toString(), sb);
		}
	}

	public static String printCurrency(BigDecimal currency) {
		DecimalFormat df = new DecimalFormat("#0.00###");
		return df.format(currency);
	}

	public static void appendCurrencyValueToOutput(String key,
			BigDecimal value, StringBuilder sb) {
		String stringValue = printCurrency(value);
		appendStringValueToOutput(key, stringValue, sb);
	}
}
