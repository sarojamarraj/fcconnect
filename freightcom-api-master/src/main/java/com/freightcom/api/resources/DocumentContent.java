package com.freightcom.api.resources;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class DocumentContent {
	private InputStream stream;
	private Map<String, List<String>> headers;

	public DocumentContent(InputStream stream, Map<String, List<String>> headers) {
		this.stream = stream;
		this.headers = headers;
	}

	public InputStream getStream() {
		return stream;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}
}
