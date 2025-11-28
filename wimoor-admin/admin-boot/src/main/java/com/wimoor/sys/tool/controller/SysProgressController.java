package com.wimoor.sys.tool.controller;


 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.common.mvc.ProgressHelper;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "获取进度")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sysprogress")
public class SysProgressController {
 

    @ApiOperation(value = "获取进度")
    @GetMapping
    public Result<Integer> progress(String key) {
    	UserInfo userInfo = UserInfoContext.get();
    	Integer progress = ProgressHelper.getProgress(userInfo.getId()+"#"+key);
    	if(progress==100) {
    		ProgressHelper.remove(userInfo.getId()+"#"+key);
    	}
        return Result.success(progress);
    }

     
    
}

