package com.wimoor.admin.common.exception;
 
import com.wimoor.common.result.IResultCode;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2341787046466752565L;
	public IResultCode resultCode;

    public BizException(IResultCode errorCode) {
        super(errorCode.getMsg());
        this.resultCode = errorCode;
    }

    public BizException(String message){
        super(message);
    }

    public BizException(String message, Throwable cause){
        super(message, cause);
    }

    public BizException(Throwable cause){
        super(cause);
    }
}
