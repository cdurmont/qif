package com.github.squirrelgrip.qif4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class QifReader extends Reader {

	public enum LoadStatus {
		LOADED, NOT_STARTED, QIF_TYPE_LOADED
	}

	public static final String DEFAULT_DATE_FORMAT = "M/d/yy";
	private DateTimeFormatter formatter;
	private final List<QifInvestment> investments = new ArrayList<QifInvestment>();
	private LoadStatus loadStatus = LoadStatus.NOT_STARTED;
	private QifType qifType = QifType.UNKNOWN;
	private final BufferedReader reader;
	private Collection<TransactionListener> listeners = new HashSet<TransactionListener>();

	public QifReader(File file) throws FileNotFoundException {
		this(new FileReader(file), DEFAULT_DATE_FORMAT);
	}

	public QifReader(File file, String dateFormat) throws FileNotFoundException {
		this(new FileReader(file), dateFormat);
	}

	public QifReader(Reader reader) {
		this(reader, DEFAULT_DATE_FORMAT);
	}

	public QifReader(Reader reader, String dateFormat) {
		if (reader instanceof BufferedReader) {
			this.reader = (BufferedReader) reader;
		} else {
			this.reader = new BufferedReader(reader);
		}
		setDateFormat(dateFormat);
	}

	public QifReader(String filename, String dateFormat) throws FileNotFoundException {
		this(new File(filename));
		setDateFormat(dateFormat);
	}

	public QifReader(String filename) throws FileNotFoundException {
		this(new File(filename), DEFAULT_DATE_FORMAT);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	public List<QifInvestment> getInvestments() {
		load();
		return investments;
	}

	public QifType getQifType() {
		readQifType();
		return qifType;
	}

	public void load() {
		switch (getQifType()) {
		case INVST:
			loadInvestments();
		default:
			loadTransactions();
		}
	}

	private List<QifInvestment> loadInvestments() {
		try {
			String line = reader.readLine();
			QifInvestment investment = new QifInvestment();
			while (line != null) {
				if ("^".equals(line)) {
					investments.add(investment);
					fireOnTransactionHandler(investment);
					investment = new QifInvestment();
				} else {
					processInvestmentDetail(investment, line);
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
			throw new QifReaderException(e);
		}
		return investments;
	}

	private void fireOnTransactionHandler(QifTransaction transaction) {
		for (TransactionListener listener : listeners) {
			listener.onTransaction(transaction);
		}
	}

	private void loadTransactions() {
		if (loadStatus != LoadStatus.LOADED) {
			try {
				String line = reader.readLine();
				QifCashTransaction transaction = new QifCashTransaction();
				while (line != null) {
					if ("^".equals(line)) {
						fireOnTransactionHandler(transaction);
						transaction = new QifCashTransaction();
					} else {
						processTransactionDetail(transaction, line);
					}
					line = reader.readLine();
				}
				loadStatus = LoadStatus.LOADED;
			} catch (Exception e) {
				throw new QifReaderException(e);
			}
		}
	}

	public void processInvestmentDetail(QifInvestment investment, String line) {
		char rowType = line.charAt(0);
		String rowData = line.substring(1);
		switch (rowType) {
		case 'D':
			setDate(investment, rowData);
			break;
		case 'N':
			investment.setAction(rowData);
			break;
		case 'Y':
			investment.setSecurity(rowData);
			break;
		case 'I':
			setPrice(investment, rowData);
			break;
		case 'Q':
			setQuantity(investment, rowData);
			break;
		case 'T':
		case '$':
			setTotal(investment, rowData);
			break;
		case 'M':
			investment.setMemo(rowData);
			break;
		}

	}

	// D Date
	// T Amount
	// C Cleared status
	// N Num (check or reference number)
	// P Payee
	// M Memo
	// A Address (up to five lines;the sixth line is an optional message)
	// L Category (Category/Subcategory/Transfer/Class)
	// S Category in split (Category/Transfer/Class)
	// E Memo in split
	// $ Dollar amount of split
	// ^ End of entry
	public void processTransactionDetail(QifCashTransaction transaction, String line) {
		char rowType = line.charAt(0);
		String rowData = line.substring(1);
		switch (rowType) {
		case 'D':
			setDate(transaction, rowData);
			break;
		case 'T':
			setTotal(transaction, rowData);
			break;
		case 'N':
			transaction.setReference(rowData);
			break;
		case 'C':
			transaction.setClearedStatus(rowData);
			break;
		case 'P':
			transaction.setPayee(rowData);
			break;
		case 'M':
			transaction.setMemo(rowData);
			break;
		case 'A':
			transaction.addAddress(rowData);
			break;
		case 'L':
			transaction.setCategory(rowData);
			break;
		case 'S':
			transaction.setSplitCategory(rowData);
			break;
		case 'E':
			transaction.setSplitMemo(rowData);
			break;
		case '$':
			transaction.setSplitAmount(rowData);
			break;
		}

	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	private void readQifType() {
		if (loadStatus == LoadStatus.NOT_STARTED) {
			try {
				String line = this.reader.readLine();
				if (line.startsWith("!Type:")) {
					this.qifType = QifType.valueOf(line.substring(6).toUpperCase());
				} else {
					throw new QifReaderException("File Format is not a QIF format");
				}
				loadStatus = LoadStatus.QIF_TYPE_LOADED;
			} catch (QifReaderException e) {
				throw e;
			} catch (Exception e) {
				throw new QifReaderException(e);
			}
		}
	}

	private void setDate(QifTransaction transaction, String dateString) throws QifReaderException {
		dateString = dateString.replace(" ", "");
		transaction.setDate(LocalDate.parse(dateString, formatter));
	}

	public void setDateFormat(String dateFormat) {
		formatter = DateTimeFormatter.ofPattern(dateFormat);
	}

	private void setPrice(QifInvestment investment, String priceString) {
		priceString = priceString.replace(",", "");
		investment.setPrice(new BigDecimal(priceString));
	}

	private void setQuantity(QifInvestment investment, String quantityString) {
		quantityString = quantityString.replace(",", "");
		investment.setQuantity(new BigDecimal(quantityString));
	}

	private void setTotal(QifTransaction transaction, String totalString) {
		totalString = totalString.replace(",", "");
		transaction.setTotal(new BigDecimal(totalString));
	}

	public void addTranasctionListener(TransactionListener transactionListener) {
		listeners.add(transactionListener);
	}

}
