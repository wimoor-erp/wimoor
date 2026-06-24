package com.wimoor.feishu.controller;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.feishu.pojo.entity.Auth;
import com.wimoor.feishu.service.IAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 飞书授权控制器
 */
@RestController
@RequestMapping("/api/v1/feishu")
@Api(tags = "飞书授权管理")
public class AuthController {

    @Autowired
    private IAuthService authService;

    /**
     * 获取绑定信息
     */
    @GetMapping("/getBindInfo")
    @ApiOperation("获取飞书绑定信息")
    public Result<Auth> getBindInfo() {
        UserInfo userinfo = UserInfoContext.get();
        // 这里应该根据当前店铺ID查询，暂时使用默认逻辑
        Auth auth = authService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        return Result.success(auth);
    }

    /**
     * 保存绑定信息
     */
    @PostMapping("/save")
    @ApiOperation("保存飞书绑定信息")
    public Result<String> save(@RequestBody Auth auth) {
        auth.setOpttime(new Date());
        UserInfo userinfo = UserInfoContext.get();
        // 设置操作人和店铺ID，这里需要从上下文获取
        auth.setOperator(userinfo.getId());
        auth.setShopid(userinfo.getCompanyid());
        Auth old = authService.lambdaQuery().eq(Auth::getShopid, auth.getShopid()).one();
        if(old!=null){
            authService.lambdaUpdate()
                      .eq(Auth::getShopid, auth.getShopid())
                      .set(Auth::getAppId,auth.getAppId())
                      .set(Auth::getAppSecret,auth.getAppSecret())
                      .set(Auth::getEncryptKey,auth.getEncryptKey())
                      .set(Auth::getVerificationToken,auth.getVerificationToken())
                      .set(Auth::getOperator,auth.getOperator())
                      .set(Auth::getOpttime,new Date())
                      .update();
        }else{
            authService.save(auth);
        }
        return Result.success("保存成功");
    }

    /**
     * 更新绑定信息
     */
    @PostMapping("/update")
    @ApiOperation("更新飞书绑定信息")
    public Result<String> update(@RequestBody Auth auth) {
        auth.setOpttime(new Date());
        // 设置操作人和店铺ID，这里需要从上下文获取
        UserInfo userinfo = UserInfoContext.get();
        // 设置操作人和店铺ID，这里需要从上下文获取
        auth.setOperator(userinfo.getId());
        auth.setShopid(userinfo.getCompanyid());
        auth.setAppId(auth.getAppId().trim());
        auth.setAppSecret(auth.getAppSecret().trim());
        auth.setEncryptKey(auth.getEncryptKey().trim());
        auth.setVerificationToken(auth.getVerificationToken().trim());
        Auth old = authService.lambdaQuery().eq(Auth::getShopid, auth.getShopid()).one();
        if(old!=null){
            authService.lambdaUpdate()
                    .eq(Auth::getShopid, auth.getShopid())
                    .set(Auth::getAppId,auth.getAppId())
                    .set(Auth::getAppSecret,auth.getAppSecret())
                    .set(Auth::getEncryptKey,auth.getEncryptKey())
                    .set(Auth::getVerificationToken,auth.getVerificationToken())
                    .set(Auth::getOperator,auth.getOperator())
                    .set(Auth::getOpttime,new Date())
                    .update();
        }else{
            authService.save(auth);
        }
        return Result.success("更新成功");
    }

}
