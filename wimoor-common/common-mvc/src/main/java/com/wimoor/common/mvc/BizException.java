package com.wimoor.common.mvc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.result.IResultCode;

import feign.FeignException;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
 
	private static final long serialVersionUID = 5460882618698803685L;
	
	public IResultCode resultCode;

    public BizException(IResultCode errorCode) {
        super(errorCode.getMsg());
        this.resultCode = errorCode;
    }
	public static  JSONArray getJsonArray(String value) {
		if(value==null)return null;
		try {
			return JSONArray.parseArray(value);
		} catch (JSONException e2) {
			return null;
		}
	}
    public BizException(String message){
        super(message);
    }

    public static String getMessage(FeignException e,String defaultMsg){
    	String message="";
    	 if(e!=null&&e.getMessage()!=null&&e.getMessage().contains("]:")) {
    		      String[] arraymsg = e.getMessage().split("]:");
    		      if(arraymsg.length>1) {
    		    	    JSONArray errors = getJsonArray(arraymsg[1]);
    		     		JSONObject errorObj =errors!=null? errors.getJSONObject(0):null;
    		     		String msg=errorObj!=null?errorObj.getString("msg"):null;
    		     		if(msg!=null) {
    		     			message=msg;
    		     		}else {
    		     			 message=arraymsg[1];  
    		     		}
    		      }else {
    		    	  message=e.getMessage();  
    		      }
 		   }else if(e!=null) {
 			  message=e.getMessage();  
 		   }else {
 			 return defaultMsg;
 		   }
         return message;
    }
    
    public BizException(String message, Throwable cause){
        super(message, cause);
    }

    public BizException(Throwable cause){
        super(cause);
    }
}
