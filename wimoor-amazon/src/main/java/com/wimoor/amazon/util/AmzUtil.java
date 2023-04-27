package com.wimoor.amazon.util;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.Money;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

public class AmzUtil {

    public static  BizException getException(ApiException e) {
    	if(e==null||e.getResponseBody()==null) {
			throw new BizException("亚马逊API错误：网络超时请重新提交！");
		}else if(e.getResponseBody()!=null&&e.getResponseBody().contains("message")) {
    		JSONObject errojson = GeneralUtil.getJsonObject(e.getResponseBody());
    		JSONArray array = errojson.getJSONArray("errors");
    		if(array.size()>0) {
    			return new BizException("亚马逊API错误："+array.getJSONObject(0).getString("message"));	
    		}
    	}
		return new BizException("亚马逊API错误："+e.getMessage());
    }
    
    public static BigDecimal getMoneny(Money value) {
    	if(value==null||value.getAmount()==null)return null;
    	else {
    		return new BigDecimal(value.getAmount());
    	}
    }
    public static  String getIds(List<String> ids) {
    	if(ids!=null&&ids.size()>0) {
   		 StringBuilder sb = new StringBuilder();
	    		ids.forEach(id->{
	    			sb.append(id+",");
	    		});
   	    return sb.toString();
   	}else {
   		return null;
   	}
   }

}
