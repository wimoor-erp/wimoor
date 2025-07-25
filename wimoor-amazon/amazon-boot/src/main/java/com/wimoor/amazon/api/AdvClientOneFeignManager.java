package com.wimoor.amazon.api;



import com.wimoor.amazon.adv.api.AmazonAdvFeignClient;
import com.wimoor.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class AdvClientOneFeignManager {
	@Autowired
    AmazonAdvFeignClient amazonAdvFeignClient ;

    public Map<String,Object> getInvoicesSummaryAction(String groupid,String marketplaceid, String fromDate,String toDate
    ){
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("groupid", groupid);
        param.put("marketplaceid", marketplaceid);
        param.put("fromDate", fromDate);
        param.put("toDate", toDate);
        Result<Map<String, Object>> result= amazonAdvFeignClient.getInvoicesSummaryAction(param);
        if(Result.isSuccess(result)&&result.getData()!=null) {
            return result.getData();
        }
        return new HashMap<String,Object>();
    }

}