package com.shike.beistmvc.webmvc.common.exception;

/**
 * 非法session
 */
public class InvalidSessionException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InvalidSessionException(String s) {
        super(s);
    }

}
