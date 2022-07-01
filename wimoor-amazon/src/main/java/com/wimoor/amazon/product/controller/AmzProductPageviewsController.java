package com.wimoor.amazon.product.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.api.AdminClientOneFeign;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.service.IAmzProductPageviewsService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 * 流量报表 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-28
 */
@Api(tags = "产品任务接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/product/amzProductPageviews")
public class AmzProductPageviewsController {
	final IAmzProductPageviewsService iAmzProductPageviewsService;
	final IAmazonAuthorityService amazonAuthorityService;
	final AdminClientOneFeign adminClientOneFeign;
	@PostMapping("/auth/uploadSessionFile")  
	public Map<String,Object> uploadSessionFileAction(String type,String marketplaceid,String sellerid,String date,String data,String ftype)  {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("ISOK", "0");
		if(type!=null&&type.equals("uploadsessionbyextension")) {
			UserInfo user = new UserInfo();
			try {
				user.setId("1");
				iAmzProductPageviewsService.uploadSessionFile(user,data, marketplaceid, sellerid,GeneralUtil.getDatez(date),ftype);
				resultMap.put("ISOK", "1");
				return resultMap; 
			}catch(Exception e) {
				return resultMap; 
			}
		}
		return resultMap;
 
    }
	
	@GetMapping("/refreshDownload")  
	public Result<?> refreshDownloadAction()  {
				iAmzProductPageviewsService.refreshDownload();
	        return Result.success();
    }
	
	@GetMapping("/refreshSummary")  
	public Result<?> refreshSummaryAction()  {
		iAmzProductPageviewsService.refreshSummary();
	        return Result.success();
    }
}

