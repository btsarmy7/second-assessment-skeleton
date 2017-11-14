package com.cooksys.twitter.exceptions;

import javax.servlet.http.HttpServletResponse;

public class InvalidIdException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private String message = null;
	
	public InvalidIdException(String message) {
		super(new Throwable());
	}
	
	public InvalidIdException(Throwable cause) {
		super(cause);
	}
	
	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	public static final int STATUS_CODE = HttpServletResponse.SC_NOT_FOUND;
}
