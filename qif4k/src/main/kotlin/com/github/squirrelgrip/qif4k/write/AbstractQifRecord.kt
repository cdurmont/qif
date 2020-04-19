package com.github.squirrelgrip.qif4k.write

abstract class AbstractQifRecord : QifRecord {
    override fun asFormattedRecord(previousHeader: QifHeaderEnum?): String {
        val sb = StringBuilder()
        if (previousHeader == null) {
            sb.append(header!!.text).append(Utils.LINE_SEPARATOR)
        }
        sb.append(formatTransaction()).append(Utils.LINE_SEPARATOR)
        sb.append(Utils.END_OF_RECORD).append(Utils.LINE_SEPARATOR)
        return sb.toString()
    }

    abstract override val header: QifHeaderEnum?
    abstract fun formatTransaction(): String?
}