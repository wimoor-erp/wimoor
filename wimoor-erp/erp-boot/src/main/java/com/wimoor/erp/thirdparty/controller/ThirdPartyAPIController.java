package com.wimoor.erp.thirdparty.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyQuoteBuyer;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartySystem;
import com.wimoor.erp.thirdparty.service.IThirdPartyQuoteBuyerService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.wimoor.erp.thirdparty.service.IThirdPartyAPIService;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Api(tags = "第三方API测试接口")
@RestController
@RequestMapping("/api/v1/thirdparty")
@RequiredArgsConstructor
public class ThirdPartyAPIController {
    final IThirdPartyQuoteBuyerService thirdPartyQuoteBuyerService;
    @Autowired
    IThirdPartyAPIService thirdPartyApiService;
    @GetMapping("/getAllSupportSystem")
    public Result<?> getAllSupportSystemAction(){
        UserInfo user = UserInfoContext.get();
        return  Result.success(thirdPartyApiService.getAllSupportSystem());
    }
    @GetMapping("/getSupportApi")
    public Result<?> getAllSupportSystemAction(String support){
        UserInfo user = UserInfoContext.get();
        return  Result.success(thirdPartyApiService.getSupportApi(user,support));
    }

    @GetMapping("/getList")
    public Result<?> getListAction(){
        UserInfo user = UserInfoContext.get();
        LambdaQueryWrapper<ThirdPartyAPI> query=new LambdaQueryWrapper<ThirdPartyAPI>();
        query.eq(ThirdPartyAPI::getShopid,user.getCompanyid());
        List<ThirdPartyAPI> list = thirdPartyApiService.list(query);
        if(list!=null){
            for (ThirdPartyAPI item:list){
                if(item.getSystem()!=null){
                    ThirdPartySystem apiSystem = thirdPartyApiService.getApiSystemById(item.getSystem());
                    if(apiSystem!=null){
                        item.setSystemName(apiSystem.getName());
                        item.setSystemEntity(apiSystem);
                    }
                }
            }
        }
        return  Result.success(list);
    }

    @PostMapping("/update")
    public Result<?> updateAction(@RequestBody ThirdPartyAPI api){
        UserInfo user = UserInfoContext.get();
        api.setOperator(new BigInteger(user.getId()));
        api.setOpttime(new Date());
        return  Result.success(thirdPartyApiService.updateById(api));
    }

    @PostMapping("/save")
    public Result<?> saveAction(@RequestBody ThirdPartyAPI api){
        UserInfo user = UserInfoContext.get();
        api.setShopid(user.getCompanyid());
        api.setOperator(new BigInteger(user.getId()));
        api.setOpttime(new Date());
        return  Result.success(thirdPartyApiService.save(api));
    }

    @GetMapping("/disable")
    public Result<?> disableAction(String id){
        ThirdPartyAPI entity = thirdPartyApiService.getById(id);
        if(entity!=null){
            entity.setIsdelete(true);
            thirdPartyApiService.updateById(entity);
        }
        return  Result.success("ok");
    }

    @GetMapping("/enable")
    public Result<?> enableAction(String id){
        ThirdPartyAPI entity = thirdPartyApiService.getById(id);
        if(entity!=null){
            entity.setIsdelete(false);
            thirdPartyApiService.updateById(entity);
        }
        return  Result.success("ok");
    }

    @GetMapping("/info")
    public Result<?> infoAction(String id){
        ThirdPartyAPI entity = thirdPartyApiService.getById(id);
        return  Result.success(entity);
    }

    @GetMapping("/getQuoteToken")
    public Result<ThirdPartyQuoteBuyer> getQuoteTokenAction(){
        UserInfo user = UserInfoContext.get();
        String shopid=user.getCompanyid();
        return  Result.success(thirdPartyQuoteBuyerService.getQuoteToken(shopid));
    }

    @GetMapping("/unbindQuoteToken")
    public Result<?> unbindQuoteTokenAction(){
        UserInfo user = UserInfoContext.get();
        String shopId=user.getCompanyid();
        return  Result.judge(thirdPartyQuoteBuyerService.removeQuoteToken(shopId));
    }

    @PostMapping("/saveQuoteToken")
    public Result<ThirdPartyQuoteBuyer> setQuoteTokenAction(@RequestBody ThirdPartyQuoteBuyer token){
        UserInfo user = UserInfoContext.get();
        String shopId=user.getCompanyid();
        token.setShopid(shopId);
        return  Result.success(thirdPartyQuoteBuyerService.saveQuoteToken(token));
    }

}
