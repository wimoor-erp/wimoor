package com.wimoor.amazon.adv.api;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wimoor.amazon.api.AmazonClientOneFeign;
import com.wimoor.common.result.Result;
 


 
@Component
public class AmazonClientOneFeignManager {
	@Autowired
	AmazonClientOneFeign api;
	 
    public List<Map<String,Object>> getProductInfoSimple(Map<String,Object> param) {
    	Result<List<Map<String, Object>>> result = api.getProductInfoSimple(param);
    	if(Result.isSuccess(result)&&result.getData()!=null) {
    		return result.getData();
    	}else {
    		return null;
    	}
    }
    
    public  Map<String,Object> orderDaysSummaryAll(Map<String,Object> param){
    	Result<Map<String, Object>> result = api.orderDaysSummaryAll(param);
    	if(Result.isSuccess(result)&&result.getData()!=null) {
    		return result.getData();
    	}else {
    		return null;
    	}
    }
    
    public Map<String, Object> orderMonthsSummaryAll(Map<String,Object> param) {
    	Result<Map<String, Object>> result = api.orderMonthsSummaryAll(param);
    	if(Result.isSuccess(result)&&result.getData()!=null) {
    		return result.getData();
    	}else {
    		return null;
    	}
    }
    
    public BigDecimal orderSummaryAll(Map<String,Object> param) {
    	Result<BigDecimal> result = api.orderSummaryAll(param);
    	if(Result.isSuccess(result)&&result.getData()!=null) {
    		return result.getData();
    	}else {
    		return null;
    	}
    }
    
    public List<Map<String,Object>> getMonthSumNumAction(Map<String,Object> param) {
    	  Result<List<Map<String,Object>>> result = api.getMonthSumNumAction(param);
    	if(Result.isSuccess(result)&&result.getData()!=null) {
    		return result.getData();
    	}else {
    		return null;
    	}
    }
}