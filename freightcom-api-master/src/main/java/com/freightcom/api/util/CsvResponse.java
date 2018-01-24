package com.freightcom.api.util;

import java.util.List;

public class CsvResponse {
	private final String filename;
	private final List<Object> records;
	private final String[] columnTitles;

	public CsvResponse(List<Object> records, String filename, String... columnTitles) {
		this.records = records;
		this.filename = filename;
		this.columnTitles = columnTitles;
	}

	public String getFilename() {
		return filename;
	}

	public List<Object> getRecords() {
		return records;
	}

	public String[] getColumnTitles() {
		return columnTitles;
	}

}
