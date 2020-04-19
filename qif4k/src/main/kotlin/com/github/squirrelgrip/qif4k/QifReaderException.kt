package com.github.squirrelgrip.qif4k

class QifReaderException : RuntimeException {
    constructor(e: Exception) : super(e)
    constructor(message: String) : super(message)
}