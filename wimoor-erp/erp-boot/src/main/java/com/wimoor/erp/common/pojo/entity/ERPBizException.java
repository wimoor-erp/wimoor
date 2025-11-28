package com.wimoor.erp.common.pojo.entity;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.IResultCode;

public class ERPBizException extends BizException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6137301348981050765L;

	public ERPBizException(IResultCode errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}
	
    public ERPBizException(String message){
        super(message);
    }
 
}
