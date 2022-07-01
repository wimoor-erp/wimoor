package com.wimoor.amazon.util;

import java.math.BigDecimal;
import java.util.List;

import com.amazon.spapi.model.orders.Money;

public class AmzUtil {

    public static BigDecimal getMoneny(Money value) {
    	if(value==null||value.getAmount()==null)return new BigDecimal("0");
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
