package com.github.squirrelgrip.qif4j;

public class QifReaderException extends RuntimeException {

	public QifReaderException(Exception e) {
		super(e);
	}

	public QifReaderException(String message) {
		super(message);
	}

}
