package com.wimoor.amazon.adv.sb.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
@Api(tags = "SB广告活动接口")
@RestController 
@RequestMapping("/api/v1/sb/campaigns")
public class AdvertSBCampaignManagerController {

	// TODO Auto-generated method stub
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	
    @ApiOperation("查询广告活动")
	@PostMapping("/getCampaignList")
	public Result<List<AmzAdvCampaignsHsa>> getCampaignListAction(@ApiParam("查询广告活动") @RequestBody QueryForList query) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(query.getProfileid()));
		if("agency".equals(profile.getType())) {
			return Result.failed();
		}
		HashMap<String,Object> campaignsParam=new HashMap<String,Object>();
		campaignsParam.put("creativeType", "video");
		campaignsParam.put("count", query.getPagesize());
		campaignsParam.put("startIndex", query.getCurrentpage());
		try {
			List<AmzAdvCampaignsHsa>  list=amzAdvCampaignHsaService.amzListSBCampaigns(profile,campaignsParam);	
			return Result.success(list);
		}catch (Exception e) {
			e.printStackTrace();
			//无法获取所在站点权限
		}
		return null;
		
    }
}
