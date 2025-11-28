package com.wimoor.amazon.product.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.pojo.dto.AmzProductPageviewsDTO;
import com.wimoor.amazon.product.pojo.vo.AmzProductPageviewsVo;
import com.wimoor.amazon.product.service.IAmzProductPageviewsService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;
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
@Component("amzProductPageviewsController")
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/product/amzProductPageviews")
public class AmzProductPageviewsController {
	final IAmzProductPageviewsService iAmzProductPageviewsService;
	final IAmazonAuthorityService amazonAuthorityService;
	final AdminClientOneFeignManager adminClientOneFeign;
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
	@PostMapping("/getPageViewsList") 
	public Result<IPage<AmzProductPageviewsVo>> getPageViewsListAction(@RequestBody AmzProductPageviewsDTO dto){
		UserInfo user = new UserInfo();
		dto.setShopid(user.getCompanyid());
		if(StrUtil.isEmpty(dto.getSearch())) {
			dto.setSearch(null);
		}else {
			dto.setSearch("%"+dto.getSearch().trim()+"%");
		}
		if(user.isLimit(UserLimitDataType.operations)) {
    		dto.setOwner(user.getId());
    	}
		IPage<AmzProductPageviewsVo> result=iAmzProductPageviewsService.getPageViewsList(dto);
		return Result.success(result) ; 
	}
	
	@GetMapping("/refreshDownload")  
	public Result<?> refreshDownloadAction()  {
		   new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					iAmzProductPageviewsService.refreshDownload();
				}
		   }).start();
	        return Result.success();
    }
	
	@GetMapping("/refreshSummary")  
	public Result<?> refreshSummaryAction()  {
		   new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					iAmzProductPageviewsService.refreshSummary();
				}
		   }).start();
	        return Result.success();
    }
}

