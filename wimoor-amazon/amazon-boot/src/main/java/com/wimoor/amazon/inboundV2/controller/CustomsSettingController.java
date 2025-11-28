package com.wimoor.amazon.inboundV2.controller;


import com.wimoor.amazon.inboundV2.pojo.entity.ProductCustomsSetting;
import com.wimoor.amazon.inboundV2.service.IProductCustomsSettingService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "海关申报接口")
@RestController
@RequestMapping("/api/v1/customsSet")
@RequiredArgsConstructor
public class CustomsSettingController {

    final IProductCustomsSettingService iProductCustomsSettingService;
    @PostMapping("/saveRate")
    @Transactional
    public Result<?> saveRateAction(@RequestBody List<ProductCustomsSetting> dto) {
        UserInfo user = UserInfoContext.get();
        iProductCustomsSettingService.saveSettingRate(user,dto);
        return  Result.success();
    }


    @PostMapping("/getRate")
    public Result<?> getRateAction() {
        UserInfo user = UserInfoContext.get();
        List<Map<String,Object>> list=iProductCustomsSettingService.getSettingRate(user);
        return  Result.success(list);
    }



}
