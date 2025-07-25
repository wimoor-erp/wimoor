package com.wimoor.amazon.adv.controller;

import com.wimoor.amazon.adv.common.service.IAmzAdvertInvoicesService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "广告发票接口")
@RestController
@RequestMapping("/api/v1/ads/invoices")
public class AdvertInvoicesController {

    @Resource
    IAmzAdvertInvoicesService amzAdvertInvoicesService;

    //获取月份汇总数据
    @PostMapping("/getInvoicesSummary")
    public Result<Map<String,Object>> getInvoicesSummaryAction(@RequestBody Map<String,Object> param) {
        String groupid=param.get("groupid")!=null?param.get("groupid").toString():null;
        String marketplaceid=param.get("marketplaceid")!=null?param.get("marketplaceid").toString():null;
        String fromDate=param.get("fromDate").toString();
        String toDate=param.get("toDate").toString();
        UserInfo user = UserInfoContext.get();
        List<Map<String,Object>> maps=amzAdvertInvoicesService.getInvoicesSummary(user,groupid, marketplaceid, fromDate, toDate);
        Map<String,Object> result=new HashMap<String,Object>();
        if(maps!=null){
            for(Map<String,Object> map:maps){
                result.put(map.get("times").toString(),map.get("amount"));
            }
        }
        return Result.success(result);
    }

}
