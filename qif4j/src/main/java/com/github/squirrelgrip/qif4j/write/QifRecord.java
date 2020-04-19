package com.github.squirrelgrip.qif4j.write;

public interface QifRecord {
	/**
	 * Output record in QIF format. If the previous header matches this header
	 * then printing of this header will be suppressed.
	 * 
	 * @param previousHeader
	 *            the header of the previous QifRecord in the stream. May be
	 *            null
	 * @return formatted record
	 */
	String asFormattedRecord(QifHeaderEnum previousHeader);

	/**
	 * Return the header of the QIF Record. For example the record describing
	 * the purchase or sale of a security will have the header:
	 * <code>!Type:Invst</code>
	 * 
	 * @return header
	 */
	QifHeaderEnum getHeader();

}
