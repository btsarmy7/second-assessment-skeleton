package com.cooksys.twitter.exceptions;

import javax.servlet.http.HttpServletResponse;

public class InvalidIdException extends Exception {
	
	public static final int NOT_FOUND = HttpServletResponse.SC_NOT_FOUND;
	public static final int EXP_FAIL = HttpServletResponse.SC_EXPECTATION_FAILED;
	public static final int BAD_REQ = HttpServletResponse.SC_BAD_REQUEST;

}
