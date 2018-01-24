package com.freightcom.api.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class CsvMessageConverter extends AbstractHttpMessageConverter<CsvResponse> {
	public static final MediaType MEDIA_TYPE = new MediaType("text", "csv", Charset.forName("utf-8"));
	private static final Logger logger = LoggerFactory.getLogger(CsvMessageConverter.class);

	public CsvMessageConverter() {
		super(MEDIA_TYPE);
	}

	protected boolean supports(Class<?> clazz) {
		return CsvResponse.class.equals(clazz);
	}

	@Override
	protected CsvResponse readInternal(Class<? extends CsvResponse> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return null;
	}

	protected void writeInternal(CsvResponse CsvResponse, HttpOutputMessage output)
			throws IOException, HttpMessageNotWritableException {
		output.getHeaders().setContentType(MEDIA_TYPE);
		output.getHeaders().set("Content-Disposition", "attachment; filename=\"" + CsvResponse.getFilename() + "\"");
		final OutputStream out = output.getBody();

		writeColumnTitles(CsvResponse, out);

		if (CsvResponse.getRecords() != null && CsvResponse.getRecords().size() != 0) {
			writeRecords(CsvResponse, out);
		}

		out.close();
	}

	private void writeRecords(CsvResponse response, OutputStream out) throws IOException {
		List<String> getters = getObjectGetters(response);
		for (final Object record : response.getRecords()) {
			for (String getter : getters) {
				try {
					Method method = ReflectionUtils.findMethod(record.getClass(), getter);
					out.write(method.invoke(record).toString().getBytes(Charset.forName("utf-8")));
					out.write('\t');
				} catch (IllegalAccessException | InvocationTargetException e) {
					logger.error("Erro ao transformar em CSV", e);
				}
			}
			out.write('\n');
		}
	}

	private List<String> getObjectGetters(CsvResponse response) {
		List<String> getters = new ArrayList<>();

		for (Method method : ReflectionUtils.getAllDeclaredMethods(response.getRecords().get(0).getClass())) {
			String methodName = method.getName();
			if (methodName.startsWith("get") && !methodName.equals("getClass")) {
				getters.add(methodName);
			}
		}
		// sort(getters);
		return getters;
	}

	private void writeColumnTitles(CsvResponse response, OutputStream out) throws IOException {
		for (String columnTitle : response.getColumnTitles()) {
			out.write(columnTitle.getBytes());
			out.write('\t');
		}
		out.write('\n');
	}

	protected CsvResponse readInternalx(Class<? extends CsvResponse> arg0, HttpInputMessage arg1)
			throws IOException, HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return null;
	}

	protected void writeInternalx(CsvResponse arg0, HttpOutputMessage arg1)
			throws IOException, HttpMessageNotWritableException {
		// TODO Auto-generated method stub

	}
}
