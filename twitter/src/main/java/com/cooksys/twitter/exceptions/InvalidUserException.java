package com.cooksys.twitter.exceptions;

import javax.servlet.http.HttpServletResponse;

public class InvalidUserException extends Exception {
	
	public static final int STATUS_CODE = HttpServletResponse.SC_NOT_FOUND;

}
