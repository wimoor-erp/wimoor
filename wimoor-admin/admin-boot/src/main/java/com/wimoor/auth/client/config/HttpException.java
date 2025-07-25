package com.wimoor.auth.client.config;

public class HttpException extends  Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Code;

	public HttpException() {
	}

	public HttpException(String message) {

		super(message);
	}
	public HttpException(String code,String message) {

		super(message);
	}
	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

}
