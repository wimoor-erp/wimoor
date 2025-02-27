package com.wimoor.amazon.adv.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.adv.common.pojo.AmzAdvPortfolios;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvPortfoliosService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
@Api(tags = "组合接口")
@RestController 
@RequestMapping("/api/v1/AdvertPortfolios")
public class AdvertPortfoliosController {

 
	@Resource
	IAmzAdvPortfoliosService amzAdvPortfoliosService;
	
	@GetMapping("/findPortfoliosForProfileId")
	public Result<List<AmzAdvPortfolios>> findPortfoliosForProfileIdAction(String groupid,String profileid) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("profileid", profileid);
		param.put("marketplaceid", null);
		param.put("groupid", groupid);
		param.put("shopid", user.getCompanyid());
		return Result.success(amzAdvPortfoliosService.getPortfoliosForProfileId(param));
	}
	
	@GetMapping("/getPortfoliosById")
	public Result<AmzAdvPortfolios> getPortfoliosByIdAction(String profileId,String id) {
		Example example = new Example(AmzAdvPortfolios.class);
		Criteria cri = example.createCriteria();
		cri.andEqualTo("id", id);
		cri.andEqualTo("profileid", profileId);
		return Result.success(amzAdvPortfoliosService.selectByExample(example).get(0));
	}
	
	@PostMapping("/createPortfolios")
	public Result<List<AmzAdvPortfolios>> createPortfoliosAction(@RequestBody AmzAdvPortfolios amzAdvPortfolios ) {
		UserInfo user = UserInfoContext.get();
		List<AmzAdvPortfolios> portfoliosList = new ArrayList<AmzAdvPortfolios>();
		if(amzAdvPortfolios.getProfileid() == null) {
			throw new BaseException("请选择正确的店铺站点！");
		}
		portfoliosList.add(amzAdvPortfolios);
		return Result.success(amzAdvPortfoliosService.amzCreatePortfolios(user, amzAdvPortfolios.getProfileid(), portfoliosList));
	}
	
	@PostMapping("/updatePortfolios")
	public Result<List<AmzAdvPortfolios>> updatePortfoliosAction(@RequestBody AmzAdvPortfolios amzAdvPortfolios ) {
		UserInfo user = UserInfoContext.get();
		List<AmzAdvPortfolios> portfoliosList = new ArrayList<AmzAdvPortfolios>();
		portfoliosList.add(amzAdvPortfolios);
		amzAdvPortfoliosService.amzUpdatePortfolios(user, amzAdvPortfolios.getProfileid(), portfoliosList);
		return Result.success(portfoliosList);
	}
	
}
