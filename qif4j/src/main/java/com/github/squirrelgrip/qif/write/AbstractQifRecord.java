package com.github.squirrelgrip.qif.write;

public abstract class AbstractQifRecord implements QifRecord {
	public String asFormattedRecord(QifHeaderEnum previousHeader) {
		StringBuilder sb = new StringBuilder();
		if (previousHeader == null) {
			sb.append(getHeader().getText()).append(Utils.LINE_SEPARATOR);
		}
		sb.append(formatTransaction()).append(Utils.LINE_SEPARATOR);

		sb.append(Utils.END_OF_RECORD).append(Utils.LINE_SEPARATOR);
		return sb.toString();
	}

	public abstract QifHeaderEnum getHeader();

	public abstract String formatTransaction();
}
