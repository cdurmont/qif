package com.github.squirrelgrip.qif

class QifReaderException : RuntimeException {
    constructor(e: Exception) : super(e)
    constructor(message: String) : super(message)
}