package com.freightcom.api.carrier.ups;

import java.util.ArrayList;
import java.util.List;

public class UPSException extends Exception {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    List<String> errorMessages = new ArrayList<String>();


	public UPSException() {
		super();
	}

	public UPSException(String msg) {
		super(msg);
		errorMessages.add(msg);
	}

	/**
	 * @return Returns the errorMessages.
	 */
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param errorMessages The errorMessages to set.
	 */
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
}
