package com.wimoor.amazon.adv.common.pojo;

import com.wimoor.common.mvc.BizException;

public class BaseException extends BizException{
	public BaseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public BaseException(String message,String code) {
		super(message);
		this.Code=code;
		// TODO Auto-generated constructor stub
	}
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	public String Code;
    public static String AmazonBusy="amzbusy";
    public static String SystemBusy="sysbusy";
	public static String Expired="expired";
	public void setCode(String string) {
		// TODO Auto-generated method stub
		this.Code=string;
	}
	
	public String getCode() {
		return Code;
	}
 
}
