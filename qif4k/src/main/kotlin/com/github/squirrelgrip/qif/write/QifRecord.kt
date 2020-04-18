package com.github.squirrelgrip.qif.write

interface QifRecord {
    /**
     * Output record in QIF format. If the previous header matches this header
     * then printing of this header will be suppressed.
     *
     * @param previousHeader
     * the header of the previous QifRecord in the stream. May be
     * null
     * @return formatted record
     */
    fun asFormattedRecord(previousHeader: QifHeaderEnum?): String

    /**
     * Return the header of the QIF Record. For example the record describing
     * the purchase or sale of a security will have the header:
     * `!Type:Invst`
     *
     * @return header
     */
    val header: QifHeaderEnum?
}