package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmazonGroupService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCampaignsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvRecommendationsService;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;
@Api(tags = "广告活动接口")
@RestController 
@RequestMapping("/api/v1/advCampaignManager")
public class AdvertCampaignManagerController {

	 
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	@Resource
	IAmzAdvCampaignsSDService  amzAdvCampaignsSDService;
	@Resource
	IAmazonGroupService amazonGroupService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvRecommendationsService amzAdvRecommendationsService;
	
 
    @ApiOperation("查询广告活动")
	@PostMapping("/getCampaignList")
	public Result<PageList<Map<String, Object>>> getCampaignListAction(@ApiParam("查询广告活动") @RequestBody QueryForList query) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
		PageList<Map<String, Object>> list = amzAdvCampaignService.getCampaignList(map, query.getPageBounds());
		if (list == null || list.size() == 0) {
			return Result.success(new PageList<Map<String, Object>>());
		} else {
			AdvertController.convertMapRemoveNA(list);
			Boolean isZeroPage = (Boolean) map.get("isZeroPage");
			list.get(0).put("isZeroPage", isZeroPage);
			return Result.success(list);
		}
	}
	
    @ApiOperation("查询广告活动")
    @GetMapping("/getCampaignDetail/{profileid}/{CampaignType}")
	public Result<Object> getgetCampaignDetailAction(@PathVariable String profileid,@PathVariable String CampaignType) {
		   if(CampaignType.toLowerCase().equals("sp")) {
			   Example example=new Example(AmzAdvCampaigns.class);
			   example.createCriteria().andEqualTo("profileid",profileid);
			   List<AmzAdvCampaigns> list = amzAdvCampaignService.selectByExample(example);
			   return Result.success(list);
		   }
		   else if(CampaignType.toLowerCase().equals("sd")) {
			   Example example=new Example(AmzAdvCampaignsSD.class);
			   example.createCriteria().andEqualTo("profileid",profileid);
			   List<AmzAdvCampaignsSD> list = amzAdvCampaignsSDService.selectByExample(example);
			   return Result.success(list);
		   }
           else {
        	   Example example=new Example(AmzAdvCampaignsHsa.class);
			   example.createCriteria().andEqualTo("profileid",profileid);
			   List<AmzAdvCampaignsHsa> list = amzAdvCampaignHsaService.selectByExample(example);
			   return Result.success(list);
		   }
	}
	
    @PostMapping("/createCampaignList")
	public Result<?> createCampaignListAction(@RequestBody Map<String,Object> params) {
		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray campaignArray =  jsonobject.getJSONArray("campaignArray");
		Map<BigInteger,JSONArray> mapSP=new HashMap<BigInteger,JSONArray>();
		Map<BigInteger,JSONArray> mapSB=new HashMap<BigInteger,JSONArray>();
		Map<BigInteger,JSONArray> mapSD=new HashMap<BigInteger,JSONArray>();
		List<Object> result=new LinkedList<Object>();
		for (int i = 0; i < campaignArray.size(); i++) {
			JSONObject campaign = campaignArray.getJSONObject(i);
			String campaignType = campaign.getString("campaignType");
			BigInteger profileId = campaign.getBigInteger("profileid");
			if (campaignType!=null&&"SP".equals(campaignType.toUpperCase())) {
				if (mapSP.get(profileId) == null) {
					JSONArray list = new  JSONArray();
					list.add(campaign);
					mapSP.put(profileId, list);
				} else {
					mapSP.get(profileId).add(campaign);
				}
			} else if (campaignType!=null&&("HSA".equals(campaignType.toUpperCase())||"SB".equals(campaignType.toUpperCase()))) {
				if (mapSB.get(profileId) == null) {
					JSONArray list = new  JSONArray();
					list.add(campaign);
					mapSB.put(profileId, list);
				} else {
					mapSB.get(profileId).add(campaign);
				}
			} else if (campaignType!=null&&("SD".equals(campaignType.toUpperCase()))) {
				if (mapSD.get(profileId) == null) {
					JSONArray list = new  JSONArray();
					list.add(campaign);
					mapSD.put(profileId, list);
				} else {
					mapSD.get(profileId).add(campaign);
				}
			}
		}
		for (BigInteger key : mapSP.keySet()) {
			List<AmzAdvCampaigns> list = amzAdvCampaignService.amzCreateSpCampaigns(user, key, mapSP.get(key));
			if ( list== null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
			result.addAll(list);
		}
		for (BigInteger key : mapSB.keySet()) {
			List<AmzAdvCampaignsHsa> list = amzAdvCampaignHsaService.amzCreateSBCampaigns(user, key, mapSB.get(key));
			if ( list== null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
			result.addAll(list);
		}
		for (BigInteger key : mapSD.keySet()) {
			List<AmzAdvCampaignsSD> list = amzAdvCampaignsSDService.amzCreateSDCampaigns(user, key, mapSD.get(key));
			if ( list== null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
			result.addAll(list);
		}
		return Result.success(result);
	}
	
	@PostMapping("/updateCampaignList")
	public Result<String> updateCampaignListAction(@RequestBody Map<String,Object> params) {
		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray campaignArray =  jsonobject.getJSONArray("campaignArray");
		Map<BigInteger,JSONArray> mapSP=new HashMap<BigInteger,JSONArray>();
		Map<BigInteger,JSONArray> mapSB=new HashMap<BigInteger,JSONArray>();
		Map<BigInteger,JSONArray> mapSD=new HashMap<BigInteger,JSONArray>();
		for (int i = 0; i < campaignArray.size(); i++) {
			JSONObject campaign = campaignArray.getJSONObject(i);
			String campaignType = campaign.getString("campaignType");
			BigInteger profileId = campaign.getBigInteger("profileid");
			if (campaignType!=null&&"SP".equals(campaignType.toUpperCase())) {
				if (mapSP.get(profileId) == null) {
					JSONArray list = new  JSONArray();
					list.add(campaign);
					mapSP.put(profileId, list);
				} else {
					mapSP.get(profileId).add(campaign);
				}
			} else if (campaignType!=null&&("HSA".equals(campaignType.toUpperCase())||"SB".equals(campaignType.toUpperCase()))) {
				if (mapSB.get(profileId) == null) {
					JSONArray list = new  JSONArray();
					list.add(campaign);
					mapSB.put(profileId, list);
				} else {
					mapSB.get(profileId).add(campaign);
				}
			} else if (campaignType!=null&&("SD".equals(campaignType.toUpperCase()))) {
				if (mapSD.get(profileId) == null) {
					JSONArray list = new  JSONArray();
					list.add(campaign);
					mapSD.put(profileId, list);
				} else {
					mapSD.get(profileId).add(campaign);
				}
			}
		}
		for (BigInteger key : mapSP.keySet()) {
			if (amzAdvCampaignService.amzUpdateSpCampaigns(user, key, mapSP.get(key)) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		for (BigInteger key : mapSB.keySet()) {
			if (amzAdvCampaignHsaService.amzUpdateSBCampaigns(user, key, mapSB.get(key)) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		for (BigInteger key : mapSD.keySet()) {
			if (amzAdvCampaignsSDService.amzUpdateSDCampaigns(user, key, mapSD.get(key)) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		return Result.success("SUCCESS");
	}


	@ApiOperation("查询广告活动图表")
	@PostMapping("/getCampaignChart")
	public Result<Map<String, Object>> getCampaignChartAction(@ApiParam("查询广告活动") @RequestBody QueryForList query) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> map = AdvertController.amzAdvParameterMap(query);
		String bytime = query.getBytime();
		map.put("shopid", user.getCompanyid());
		map.put("bytime", bytime);
		Map<String, Object> mapList = amzAdvCampaignService.getCampaignChart(map);
		return Result.success(mapList);
	}

 
	@PostMapping("/getCampaignBudgetUsage/{profileid}/{CampaignType}")
	public Result<?> getCampaignBudgetUsageAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody List<String> ids) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		   if(CampaignType.toLowerCase().equals("sp")) {
				JSONObject param = new JSONObject();
				param.put("campaignIds", ids);
				return Result.success( amzAdvCampaignService.amzGetCampaignBudgetUsage(user, new BigInteger(profileid), param));
			}
			if(CampaignType.toLowerCase().equals("sd")) {
				JSONObject param = new JSONObject();
				param.put("campaignIds", ids);
				return Result.success( amzAdvCampaignsSDService.amzGetCampaignBudgetUsage(user, new BigInteger(profileid), param));
			}
			if(CampaignType.toLowerCase().equals("sb")) {
				JSONObject param = new JSONObject();
				param.put("campaignIds", ids);
				return Result.success( amzAdvCampaignHsaService.amzGetCampaignBudgetUsage(user, new BigInteger(profileid), param));
		  }
		return Result.failed();
	}
	
	@PostMapping("/getCampaigns/{profileid}/{CampaignType}")
	public Result<?> getCampaignListAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody List<String> ids) {
		UserInfo user = UserInfoContext.get();
		List<Object> result = null;
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
				result = new ArrayList<Object>();
				JSONObject param = new JSONObject();
				JSONObject campaignIdFilter = new JSONObject();
				campaignIdFilter.put("include", ids);
				param.put("campaignIdFilter", campaignIdFilter);
				param.put("includeExtendedDataFields", false);
				List<AmzAdvCampaigns> list = amzAdvCampaignService.amzListCampaigns(user, new BigInteger(profileid), param);
				if (list != null) {
					result.addAll(list);
				}
				 
			}
			if(CampaignType.toLowerCase().equals("sd")) {
					result = new ArrayList<Object>();
					if(ids!=null) {
						for(String id:ids) {
							AmzAdvCampaignsSD sd=amzAdvCampaignsSDService.amzGetCampaigns(user, new BigInteger(profileid), id);
							if(sd!=null) {
								result.add(sd);
							}
						}
					}
			}
			if(CampaignType.toLowerCase().equals("sb")) {
				result = new ArrayList<Object>();
				JSONObject param = new JSONObject();
				JSONObject campaignIdFilter = new JSONObject();
				campaignIdFilter.put("include", ids);
				param.put("campaignIdFilter", campaignIdFilter);
				param.put("includeExtendedDataFields", false);
				AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey( new BigInteger(profileid));
				List<AmzAdvCampaignsHsa> list = amzAdvCampaignHsaService.amzListSBCampaigns(profile, param);
				if (list != null) {
					result.addAll(list);
				}
		  }
		return Result.success(result) ;
	}
	
	@PostMapping("/deleteCampaigns/{profileid}/{CampaignType}")
	public Result<?> deleteCampaignListAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody List<String> ids) {
		UserInfo user = UserInfoContext.get();
		List<Object> result = null;
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
				result = new ArrayList<Object>();
				JSONObject param = new JSONObject();
				JSONObject campaignIdFilter = new JSONObject();
				campaignIdFilter.put("include", ids);
				param.put("campaignIdFilter", campaignIdFilter);
				List<AmzAdvCampaigns> list = amzAdvCampaignService.deleteCampaigns(user, new BigInteger(profileid), param);
				if (list != null) {
					result.addAll(list);
				}
				 
			}
			if(CampaignType.toLowerCase().equals("sd")) {
					result = new ArrayList<Object>();
					if(ids!=null) {
						for(String id:ids) {
							AmzAdvCampaignsSD sd=amzAdvCampaignsSDService.amzArchiveSpCampaigns(user, new BigInteger(profileid), id);
							if(sd!=null) {
								result.add(sd);
							}
						}
					}
			}
			if(CampaignType.toLowerCase().equals("sb")) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("campaignIdFilter", ids);
				AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey( new BigInteger(profileid));
				List<AmzAdvCampaignsHsa> list = amzAdvCampaignHsaService.amzDeleteSBCampaigns(profile, param);
				if (list != null) {
					result.addAll(list);
				}
		  }
		return Result.success(result) ;
	}
	
	@PostMapping("/getCampaignsExt/{profileid}/{CampaignType}/ignoreRepeat")
	public Result<?> getCampaignExtListAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody List<String> ids) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
				JSONObject param = new JSONObject();
				JSONObject campaignIdFilter = new JSONObject();
				campaignIdFilter.put("include", ids);
				param.put("campaignIdFilter", campaignIdFilter);
				param.put("includeExtendedDataFields", true);
				List<AmzAdvCampaigns> list = amzAdvCampaignService.amzListCampaigns(user, new BigInteger(profileid), param);
				return Result.success(list) ;
			}
			if(CampaignType.toLowerCase().equals("sd")) {
				List<Object> result =  new ArrayList<Object>();
					if(ids!=null) {
						for(String id:ids) {
							AmzAdvCampaignsSD sd=amzAdvCampaignsSDService.amzGetCampaigns(user, new BigInteger(profileid), id);
							if(sd!=null) {
								result.add(sd);
							}
						}
					}
			     return Result.success(result) ;
			}
			if(CampaignType.toLowerCase().equals("sb")) {
				JSONObject param = new JSONObject();
				JSONObject campaignIdFilter = new JSONObject();
				campaignIdFilter.put("include", ids);
				param.put("campaignIdFilter", campaignIdFilter);
				param.put("includeExtendedDataFields", true);
				AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey( new BigInteger(profileid));
				List<AmzAdvCampaignsHsa> list = amzAdvCampaignHsaService.amzListSBCampaigns(profile, param);
				return Result.success(list) ;
		  }
	    return Result.success();
	}

	 
	@GetMapping("/getCampaignPlacement")
	public Result<List<Map<String, Object>>> getCampaignPlacementAction(String profileid,String campaignId,String campaignType,
			String fromDate,String endDate) {
		 profileid = AdvUtils.getRequestValue(profileid);
		 campaignId = AdvUtils.getRequestValue(campaignId);
		 campaignType = AdvUtils.getRequestValue(campaignType);
		 fromDate = AdvUtils.getRequestValue(fromDate);
		 endDate = AdvUtils.getRequestValue(endDate);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isEmpty(fromDate) || StringUtil.isEmpty(endDate)) {
			throw new BaseException("日期区间不能为空！");
		}
		map.put("profileid", profileid);
		map.put("campaignid", campaignId);
		map.put("campaignType", campaignType);
		map.put("fromDate", fromDate);
		map.put("endDate", endDate);
		List<Map<String, Object>> list = amzAdvCampaignService.getCampaignPlacement(map);
		if(list!=null) {
			AdvertController.convertMapRemoveNA(list);
		}
		return Result.success(list);
	}

    @ApiOperation("查询广告活动汇总")
	@PostMapping("/getSumCampaign")
	public Result<Object> getSumCampaignAction(@ApiParam("查询广告活动") @RequestBody QueryForList query) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
		List<Map<String, Object>> param = amzAdvCampaignService.getSumCampaigns(map);
		return  Result.success(AdvertController.SumAdvertDate(param));
	}
    
    
    @GetMapping("/budgetRulesRecommendations/{profileid}/{CampaignType}/{campaignId}/ignoreRepeat")
   	public Result<?> budgetRulesRecommendationsAction(@PathVariable String profileid,@PathVariable String CampaignType,@PathVariable String campaignId) {
   		UserInfo user = UserInfoContext.get();
   		return Result.success(amzAdvRecommendationsService.budgetRulesRecommendations(user,new BigInteger(profileid), campaignId));
   	}
   	
    @PostMapping("/initialBudgetRecommendation/{profileid}/{CampaignType}")
   	public Result<?> initialBudgetRecommendationAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
   		UserInfo user = UserInfoContext.get();
   		return Result.success(amzAdvRecommendationsService.initialBudgetRecommendation(user,new BigInteger(profileid), param));
   	}
    
    @PostMapping("/budgetRecommendations/{profileid}/{CampaignType}")
   	public Result<?> budgetRecommendationsAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
   		UserInfo user = UserInfoContext.get();
   		return Result.success(amzAdvRecommendationsService.budgetRecommendations(user,new BigInteger(profileid), param));
   	}
     
    @PostMapping("/campaignRecommendations/{profileid}/{CampaignType}")
   	public Result<?> campaignRecommendationsAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
   		UserInfo user = UserInfoContext.get();
   		return Result.success(amzAdvRecommendationsService.campaignRecommendations(user,new BigInteger(profileid), param));
   	}
}
