package com.wimoor.amazon.adv.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmazonGroupService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCampaignsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
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
	
	
	
	
	@RequestMapping("/sbviewlist")
	public String viewAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
//		List<AmazonGroup> advGroupList = userService.getCurrentUserAdvGroupList(request);
//		if (advGroupList == null || advGroupList.size() == 0) {
//			advGroupList = amazonGroupService.findAdvShopNameByUser(user.getCompanyId());
//		}
//		request.setAttribute("groupList", advGroupList);
		return "pages/advertising/managerList/advert_manager_sb_listCampaigns";
	}
	
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
	
    @ApiOperation("查询广告活动SB")
    @PostMapping("/getCampaignHsaDetail")
	public Result<Object> getgetCampaignDetailAction(@ApiParam("查询广告活动") @RequestBody QueryForList query) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
	    Map<String, Object> result = amzAdvCampaignHsaService.getCampaignDetail(map);
	    return Result.success(result);
	}
	
	
	@ResponseBody
	@RequestMapping("/updateCampaignList")
	public String updateCampaignListAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray campaignArray = jsonobject.getJSONArray("campaignArray");
		Map<BigInteger, List<AmzAdvCampaigns>> map = new HashMap<BigInteger, List<AmzAdvCampaigns>>();
		Map<BigInteger, List<AmzAdvCampaignsHsa>> mapHsa = new HashMap<BigInteger, List<AmzAdvCampaignsHsa>>();
		Map<BigInteger, List<AmzAdvCampaignsSD>> mapSD = new HashMap<BigInteger, List<AmzAdvCampaignsSD>>();		
		for (int i = 0; i < campaignArray.size(); i++) {
			JSONObject campaign = campaignArray.getJSONObject(i);
			String campaignType = campaign.getString("campaignType");
			BigInteger profileId = campaign.getBigInteger("profileid");
			BigInteger portfolioid = campaign.getBigInteger("portfolioid");
			String ftype = campaign.getString("ftype");
			String name = campaign.getString("name");
			String state = campaign.getString("status");
			Date startDate = campaign.getDate("startDate");
			Date endDate = campaign.getDate("endDate");
			BigInteger campaignid = campaign.getBigInteger("id");
			BigDecimal budget = campaign.getBigDecimal("budget");
			if (campaignType!=null&&"SP".equals(campaignType.toUpperCase())) {
				String bidding = campaign.getString("bidding");
				String targetingType = campaign.getString("targetingType");
				String premiumBidAdjustment = campaign.getString("premiumBidAdjustment");
				AmzAdvCampaigns amzAdvCampaigns = new AmzAdvCampaigns();
				amzAdvCampaigns.setCampaignid(campaignid);
				amzAdvCampaigns.setName(name);
				amzAdvCampaigns.setCampaigntype(ftype);
				amzAdvCampaigns.setDailybudget(budget);
				amzAdvCampaigns.setTargetingtype(targetingType);
				amzAdvCampaigns.setBidding(bidding);
				amzAdvCampaigns.setState(state);
				amzAdvCampaigns.setPremiumbidadjustment(premiumBidAdjustment);
				amzAdvCampaigns.setOpttime(new Date());
				amzAdvCampaigns.setProfileid(profileId);
				amzAdvCampaigns.setPortfolioid(portfolioid);
				amzAdvCampaigns.setStartdate(startDate);
				amzAdvCampaigns.setEnddate(endDate);
				if (map.get(profileId) == null) {
					List<AmzAdvCampaigns> list = new ArrayList<AmzAdvCampaigns>();
					list.add(amzAdvCampaigns);
					map.put(profileId, list);
				} else {
					map.get(profileId).add(amzAdvCampaigns);
				}
			} else if (campaignType!=null&&("HSA".equals(campaignType.toUpperCase())||"SB".equals(campaignType.toUpperCase()))) {
				String budgetType = campaign.getString("budgetType");
				String servingStatus = campaign.getString("servingStatus");
				String spendingPolicy = campaign.getString("spendingPolicy");
				String bidOptimization = campaign.getString("bidOptimization");
				BigDecimal bidMultiplier = campaign.getBigDecimal("bidMultiplier");
				AmzAdvCampaignsHsa amzAdvCampaignsHsa = new AmzAdvCampaignsHsa();
				amzAdvCampaignsHsa.setCampaignid(campaignid);
				amzAdvCampaignsHsa.setPortfolioid(portfolioid);
				amzAdvCampaignsHsa.setName(name);
				amzAdvCampaignsHsa.setState(state);
				amzAdvCampaignsHsa.setOpttime(new Date());
				amzAdvCampaignsHsa.setProfileid(profileId);
				amzAdvCampaignsHsa.setStartdate(startDate);
				amzAdvCampaignsHsa.setEnddate(endDate);
				amzAdvCampaignsHsa.setBudgettype(budgetType);
				amzAdvCampaignsHsa.setBudget(budget);
				amzAdvCampaignsHsa.setServingstatus(servingStatus);
				amzAdvCampaignsHsa.setSpendingpolicy(spendingPolicy);
				amzAdvCampaignsHsa.setBidoptimization(bidOptimization);
				amzAdvCampaignsHsa.setBidMultiplier(bidMultiplier);
				if (mapHsa.get(profileId) == null) {
					List<AmzAdvCampaignsHsa> list = new ArrayList<AmzAdvCampaignsHsa>();
					list.add(amzAdvCampaignsHsa);
					mapHsa.put(profileId, list);
				} else {
					mapHsa.get(profileId).add(amzAdvCampaignsHsa);
				}
			} else if (campaignType!=null&&("SD".equals(campaignType.toUpperCase()))) {
				String budgetType = campaign.getString("budgetType");
				AmzAdvCampaignsSD amzAdvCampaignsSD = new AmzAdvCampaignsSD();
				amzAdvCampaignsSD.setCampaignid(campaignid);
				amzAdvCampaignsSD.setName(name);
				amzAdvCampaignsSD.setState(state);
				amzAdvCampaignsSD.setOpttime(new Date());
				amzAdvCampaignsSD.setProfileid(profileId);
				amzAdvCampaignsSD.setStartdate(startDate);
				amzAdvCampaignsSD.setEnddate(endDate);
				amzAdvCampaignsSD.setBudgettype(budgetType);
				amzAdvCampaignsSD.setBudget(budget);
				if (mapHsa.get(profileId) == null) {
					List<AmzAdvCampaignsSD> list = new ArrayList<AmzAdvCampaignsSD>();
					list.add(amzAdvCampaignsSD);
					mapSD.put(profileId, list);
				} else {
					mapSD.get(profileId).add(amzAdvCampaignsSD);
				}
			}
		}
		for (BigInteger key : map.keySet()) {
			if (amzAdvCampaignService.amzUpdateSpCampaigns(user, key, map.get(key)) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		for (BigInteger key : mapHsa.keySet()) {
			if (amzAdvCampaignHsaService.amzUpdateSBCampaigns(user, key, mapHsa.get(key)) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		for (BigInteger key : mapSD.keySet()) {
			if (amzAdvCampaignsSDService.amzUpdateSDCampaigns(user, key, mapSD.get(key)) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		return "SUCCESS";
	}

	@ResponseBody
	@RequestMapping("/updateCampaignName")
	public String updateCampaignNameAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject campaign = GeneralUtil.getJsonObject(json);
		String campaignType = campaign.getString("campaignType");
		BigInteger profileId = campaign.getBigInteger("profileid");
		String ftype = campaign.getString("ftype");
		String name = campaign.getString("name");
		String state = campaign.getString("status");
		Date startDate = campaign.getDate("startDate");
		Date endDate = campaign.getDate("endDate");
		BigInteger campaignid = campaign.getBigInteger("id");
		BigDecimal budget = campaign.getBigDecimal("budget");
		if ("SP".equals(campaignType)) {
			String bidding = campaign.getString("bidding");
			String targetingType = campaign.getString("targetingType");
			String premiumBidAdjustment = campaign.getString("premiumBidAdjustment");
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", name);
			criter.andEqualTo("profileid", profileId);
			List<AmzAdvCampaigns> oldamzAdvCampaigns = amzAdvCampaignService.selectByExample(example);
			if (oldamzAdvCampaigns != null && oldamzAdvCampaigns.size() > 0) {
				throw new BaseException("在该站点下,广告活动名称已经存在！");
			}
			AmzAdvCampaigns amzAdvCampaigns = new AmzAdvCampaigns();
			amzAdvCampaigns.setCampaignid(campaignid);
			amzAdvCampaigns.setName(name);
			amzAdvCampaigns.setCampaigntype(ftype);
			amzAdvCampaigns.setDailybudget(budget);
			amzAdvCampaigns.setBidding(bidding);
			amzAdvCampaigns.setTargetingtype(targetingType);
			amzAdvCampaigns.setState(state);
			amzAdvCampaigns.setPremiumbidadjustment(premiumBidAdjustment);
			amzAdvCampaigns.setOpttime(new Date());
			amzAdvCampaigns.setProfileid(profileId);
			amzAdvCampaigns.setStartdate(startDate);
			amzAdvCampaigns.setEnddate(endDate);
			List<AmzAdvCampaigns> campaignsList = new ArrayList<AmzAdvCampaigns>();
			campaignsList.add(amzAdvCampaigns);
			if (amzAdvCampaignService.amzUpdateSpCampaigns(user, profileId, campaignsList) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		} else if("SB".equals(campaignType)) {
			String budgetType = campaign.getString("budgetType");
			String servingStatus = campaign.getString("servingStatus");
			String spendingPolicy = campaign.getString("spendingPolicy");
			String bidOptimization = campaign.getString("bidOptimization");
			AmzAdvCampaignsHsa oldamzAdvCampaigns = amzAdvCampaignHsaService.getHsaCampaignsByName(profileId, name);
			if (oldamzAdvCampaigns != null) {
				throw new BaseException("在该站点下,广告活动名称已经存在！");
			}
			AmzAdvCampaignsHsa amzAdvCampaignsHsa = new AmzAdvCampaignsHsa();
			amzAdvCampaignsHsa.setCampaignid(campaignid);
			amzAdvCampaignsHsa.setName(name);
			amzAdvCampaignsHsa.setState(state);
			amzAdvCampaignsHsa.setOpttime(new Date());
			amzAdvCampaignsHsa.setProfileid(profileId);
			amzAdvCampaignsHsa.setStartdate(startDate);
			amzAdvCampaignsHsa.setEnddate(endDate);
			amzAdvCampaignsHsa.setBudgettype(budgetType);
			amzAdvCampaignsHsa.setBudget(budget);
			amzAdvCampaignsHsa.setServingstatus(servingStatus);
			amzAdvCampaignsHsa.setSpendingpolicy(spendingPolicy);
			amzAdvCampaignsHsa.setBidoptimization(bidOptimization);
			List<AmzAdvCampaignsHsa> list = new ArrayList<AmzAdvCampaignsHsa>();
			list.add(amzAdvCampaignsHsa);
			if (amzAdvCampaignHsaService.amzUpdateSBCampaigns(user, profileId, list) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		} else if("SD".equals(campaignType)) {
			String budgetType = campaign.getString("budgetType");
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", name);
			criter.andEqualTo("profileid", profileId);
			List<AmzAdvCampaignsSD> oldamzAdvCampaigns = amzAdvCampaignsSDService.selectByExample(example);
			if (oldamzAdvCampaigns!= null&&oldamzAdvCampaigns.size()>0) {
				throw new BaseException("在该站点下,广告活动名称已经存在！");
			}
			AmzAdvCampaignsSD amzAdvCampaignsSD = new AmzAdvCampaignsSD();
			amzAdvCampaignsSD.setCampaignid(campaignid);
			amzAdvCampaignsSD.setName(name);
			amzAdvCampaignsSD.setState(state);
			amzAdvCampaignsSD.setOpttime(new Date());
			amzAdvCampaignsSD.setProfileid(profileId);
			amzAdvCampaignsSD.setStartdate(startDate);
			amzAdvCampaignsSD.setEnddate(endDate);
			amzAdvCampaignsSD.setBudgettype(budgetType);
			amzAdvCampaignsSD.setBudget(budget);
			List<AmzAdvCampaignsSD> list = new ArrayList<AmzAdvCampaignsSD>();
			list.add(amzAdvCampaignsSD);
			if (amzAdvCampaignsSDService.amzUpdateSDCampaigns(user, profileId, list) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		return "SUCCESS";
	}

	@ResponseBody
	@RequestMapping("/archiveCampaignList")
	public String archiveCampaignListAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String profileId = request.getParameter("profileid");
		String campaignId = request.getParameter("campaignId");
		String campaignType = request.getParameter("campaignType");
		AmzAdvCampaigns amzAdvCampaigns = null;
		AmzAdvCampaignsHsa amzAdvCampaignsHsa = null;
		if ("HSA".equals(campaignType)) {
			amzAdvCampaignsHsa = amzAdvCampaignHsaService.amzArchiveSBCampaigns(user, new BigInteger(profileId), campaignId);
			if (amzAdvCampaignsHsa == null) {
				return "ERROR";
			}
		} else {
			amzAdvCampaigns = amzAdvCampaignService.amzArchiveSpCampaigns(user, new BigInteger(profileId), campaignId);
			if (amzAdvCampaigns == null) {
				return "ERROR";
			}
		}
		return "SUCCESS";
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

	@ResponseBody
	@RequestMapping("/getCampaignExt")
	public Result<AmzAdvCampaigns> getCampaignExtAction(String profileid,String id) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(profileid))
			return Result.failed();
		AmzAdvCampaigns result = amzAdvCampaignService.amzGetCampaignEx(user, new BigInteger(profileid), id);
		return Result.success(result);
	}

	@ResponseBody
	@RequestMapping("/getCampaignExtList")
	public Object getCampaignExtListAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String idsp = request.getParameter("idsp");
		String idsb = request.getParameter("idsb");
		String idsd = request.getParameter("idsd");
		List<Object> result = null;
		if (StringUtil.isNotEmpty(idsp)) {
			JSONObject idsjson = GeneralUtil.getJsonObject(idsp);
			result = new ArrayList<Object>();
			for (Entry<String, Object> item : idsjson.entrySet()) {
				String profileid = item.getKey();
				idsp = (String) item.getValue();
				if (StringUtil.isEmpty(idsp) || StringUtil.isEmpty(profileid)) {
					continue;
				}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("campaignIdFilter", idsp);
				List<AmzAdvCampaigns> list = amzAdvCampaignService.amzListCampaignsEx(user, new BigInteger(profileid), param);
				if (list != null) {
					result.addAll(list);
				}
			}
		}
		if (StringUtil.isNotEmpty(idsd)) {
			JSONObject idsjson = GeneralUtil.getJsonObject(idsd);
			result = new ArrayList<Object>();
			for (Entry<String, Object> item : idsjson.entrySet()) {
				String profileid = item.getKey();
				idsd = (String) item.getValue();
				if (StringUtil.isEmpty(idsd) || StringUtil.isEmpty(profileid)) {
					continue;
				}
				String[] ids = idsd.split(",");
				if(ids!=null) {
					for(String id:ids) {
						AmzAdvCampaignsSD sd=amzAdvCampaignsSDService.amzGetSpCampaigns(user, new BigInteger(profileid), id);
						if(sd!=null) {
							result.add(sd);
						}
					}
				}
			}
		}
		if (StringUtil.isNotEmpty(idsb)) {
			JSONObject idsjson = GeneralUtil.getJsonObject(idsb);
			if(result == null) {
				result = new ArrayList<Object>();
			}
			for (Entry<String, Object> item : idsjson.entrySet()) {
				String profileid = item.getKey();
				idsb = (String) item.getValue();
				if (StringUtil.isEmpty(idsb) || StringUtil.isEmpty(profileid)) {
					continue;
				}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("campaignIdFilter", idsb);
				List<AmzAdvCampaignsHsa> list = amzAdvCampaignHsaService.amzListSBCampaigns(user, new BigInteger(profileid), param);
				if (list != null) {
					result.addAll(list);
				}
			}
		}
		return result;
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

}
