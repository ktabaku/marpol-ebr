package com.oceanpro.core.exception;


public class DocumentException extends Exception {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2464987173735902202L;
	
	public DocumentException(String message) {
		super(message);
	}

	public DocumentException(Throwable cause) {
		super(cause);
	}
}
