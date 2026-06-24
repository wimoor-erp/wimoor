package com.wimoor.feishu.controller;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.feishu.pojo.entity.Auth;
import com.wimoor.feishu.service.IAuthService;
import com.wimoor.feishu.service.IDocxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/feishu/docx")
@Api(tags = "飞书文档管理")
public class DocxController {
    @Autowired
    private IDocxService docxService;
    @Autowired
    IAuthService iAuthService;
    /**
     * 上传文档
     */
    @PostMapping("/create")
    @ApiOperation("创建文档")
    public Result<String> create(@RequestBody Map<String, Object> data) {
        String title = (String) data.get("title");
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        try {
            docxService.create(auth, title);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }

}
