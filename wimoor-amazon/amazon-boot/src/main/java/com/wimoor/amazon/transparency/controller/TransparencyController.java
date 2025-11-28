package com.wimoor.amazon.transparency.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.feed.pojo.entity.AmazonFeedStatus;
import com.wimoor.amazon.feed.pojo.entity.Submitfeed;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyAuthority;
import com.wimoor.amazon.transparency.service.ITransparencyAuthorityService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "transparency接口")
@RestController
@RequiredArgsConstructor
@Component("transparencyController")
@RequestMapping("/api/v0/transparency")
public class TransparencyController {
    final ITransparencyAuthorityService transparencyAuthorityService;

    @ApiOperation(value = "报表分类")
    @PostMapping("/saveAuthority")
    public Result<?> saveAuthorityAction(@RequestBody TransparencyAuthority auth) {
        UserInfo userinfo = UserInfoContext.get();
        if(auth.getDisabled()==null){
            auth.setDisabled(false);
        }
        return Result.success(transparencyAuthorityService.saveAuthority(userinfo,auth));
    }


    @ApiOperation(value = "报表分类")
    @GetMapping("/listAuthority")
    public Result<?> listAuthorityAction(String name) {
        UserInfo userinfo = UserInfoContext.get();
        LambdaQueryChainWrapper<TransparencyAuthority> query = transparencyAuthorityService.lambdaQuery();
        query.eq(TransparencyAuthority::getShopid,userinfo.getCompanyid());
        query.eq(TransparencyAuthority::getDisabled,false);
        if(StrUtil.isNotBlank(name)){
            name="%"+name.trim()+"%";
            query.like(TransparencyAuthority::getClientName,name);
        }
        return Result.success(query.list());
    }
}

