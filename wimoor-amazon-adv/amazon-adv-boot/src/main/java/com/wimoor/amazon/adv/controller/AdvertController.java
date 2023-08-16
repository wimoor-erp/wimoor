package com.wimoor.amazon.adv.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzRegion;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCampaignsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductAdsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.adv.sp.service.IAmzAdvAdGroupService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductAdsService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;

@RestController 
@RequestMapping("/api/v1/advert") 
public class AdvertController extends ERPBaseController<AmzAdvAuth> {
	@Resource
	IAmzAdvAdGroupService amzAdvAdGroupService;
	@Resource
	IAmzAdvAdgroupsSDService amzAdvAdgroupsSDService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	@Resource
	IAmzAdvCampaignsSDService amzAdvCampaignsSDService;
	@Resource
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvKeywordsService amzAdvKeywordsService;
	@Resource
	IAmzAdvProductAdsService amzAdvProductAdsService;
	@Resource
	IAmzAdvProductAdsSDService amzAdvProductAdsSDService;
	
	@Override
	public IService<AmzAdvAuth> getServeice() {
		return amzAdvAuthService;
	}

	@RequestMapping("/view")
	public String viewAction(HttpServletRequest request, Model model) {
	//	UserInfo user = UserInfoContext.get();
//		List<AmazonGroup> advGroupList = userService.getCurrentUserAdvGroupList(request);
//		if (advGroupList == null || advGroupList.size() == 0) {
//			advGroupList = amazonGroupService.findAdvShopNameByUser(user.getCompanyid());
//		}
//		request.setAttribute("groupList", advGroupList);
		return "pages/advertising/advert_search_list";
	}

	@Override
	public Object getListData(HttpServletRequest request, Model model) throws BizException {
		return null;
	}

	@GetMapping("/loadProfile")
	public Result<List<Map<String,Object>>> getProfileAction(String groupid) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar fijiCalendar = Calendar.getInstance();
		List<Map<String, Object>> list = amzAdvAuthService.getProfileByGroup(groupid);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String name = map.get("name") == null ? null : map.get("name").toString();
				if (name != null) {
					if ("美国".equals(name) || "加拿大".equals(name)) {
						format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
					} else if ("法国".equals(name)) {
						format.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
					} else if ("日本".equals(name)) {
						format.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
					} else {
						format.setTimeZone(TimeZone.getTimeZone("Europe/London"));
					}
					String ftime = format.format(fijiCalendar.getTime());
					map.put("ftime", ftime);
				}
			}
		}
		return Result.success(list);
	}

	@ResponseBody
	@RequestMapping("/loadCampaigns")
	public Object getCampaignsAction(HttpServletRequest request, Model model) {
		String profileid = request.getParameter("profileid");
		List<AmzAdvCampaigns> listsp = null;
		List<AmzAdvCampaignsHsa> listhsa = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (StringUtil.isNotEmpty(profileid)) {
			listsp = amzAdvCampaignService.getSpCampaignsByprofile(new BigInteger(profileid));
			listhsa = amzAdvCampaignHsaService.getHsaCampaignsByprofile(new BigInteger(profileid));
		}
		if (listsp != null) {
			for (AmzAdvCampaigns item : listsp) {
				Map<String, Object> amzadvCampaigns = new HashMap<String, Object>();
				amzadvCampaigns.put("campaignid", item.getCampaignid());
				amzadvCampaigns.put("campaigntype", item.getCampaigntype());
				amzadvCampaigns.put("name", item.getName());
				amzadvCampaigns.put("profileid", item.getProfileid());
				amzadvCampaigns.put("targetingType", item.getTargetingtype());
				resultList.add(amzadvCampaigns);
			}
		}
		if (listhsa != null) {
			for (AmzAdvCampaignsHsa item : listhsa) {
				Map<String, Object> amzadvCampaigns = new HashMap<String, Object>();
				amzadvCampaigns.put("campaignid", item.getCampaignid());
				amzadvCampaigns.put("campaigntype", "hsa");
				amzadvCampaigns.put("name", item.getName());
				amzadvCampaigns.put("profileid", item.getProfileid());
				amzadvCampaigns.put("targetingType", "");
				resultList.add(amzadvCampaigns);
			}
		}
		return resultList;
	}

	@ResponseBody
	@RequestMapping("/loadCampaignsNotArchived")
	public Object getCampaignsNotArchivedAction(HttpServletRequest request, Model model) {
		String profileid = request.getParameter("profileid");
		List<AmzAdvCampaigns>    listsp = null;
		List<AmzAdvCampaignsHsa> listsb = null;
		List<AmzAdvCampaignsSD> listsd = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (StringUtil.isNotEmpty(profileid)) {
			listsp = amzAdvCampaignService.getSpCampaignsNotArchivedByprofile(new BigInteger(profileid));
			listsb = amzAdvCampaignHsaService.getHsaCampaignsNotArchivedByprofile(new BigInteger(profileid));
			listsd = amzAdvCampaignsSDService.getSDCampaignsNotArchivedByprofile(new BigInteger(profileid));
		}
		if (listsp != null) {
			for (AmzAdvCampaigns item : listsp) {
				Map<String, Object> amzadvCampaigns = new HashMap<String, Object>();
				amzadvCampaigns.put("campaignid", item.getCampaignid());
				amzadvCampaigns.put("campaigntype", item.getCampaigntype());
				amzadvCampaigns.put("name", item.getName());
				amzadvCampaigns.put("profileid", item.getProfileid());
				amzadvCampaigns.put("targetingType", item.getTargetingtype());
				resultList.add(amzadvCampaigns);
			}
		}
		if (listsb != null) {
			for (AmzAdvCampaignsHsa item : listsb) {
				Map<String, Object> amzadvCampaigns = new HashMap<String, Object>();
				amzadvCampaigns.put("campaignid", item.getCampaignid());
				amzadvCampaigns.put("campaigntype", "hsa");
				amzadvCampaigns.put("name", item.getName());
				amzadvCampaigns.put("profileid", item.getProfileid());
				amzadvCampaigns.put("targetingType", "");
				resultList.add(amzadvCampaigns);
			}
		}
		if (listsd != null) {
			for (AmzAdvCampaignsSD item : listsd) {
				Map<String, Object> amzadvCampaigns = new HashMap<String, Object>();
				amzadvCampaigns.put("campaignid", item.getCampaignid());
				amzadvCampaigns.put("campaigntype", "sd");
				amzadvCampaigns.put("name", item.getName());
				amzadvCampaigns.put("profileid", item.getProfileid());
				amzadvCampaigns.put("targetingType", "");
				resultList.add(amzadvCampaigns);
			}
		}
		return resultList;
	}

	@ResponseBody
	@RequestMapping("/loadSPManualCampaigns")
	public Object loadSPManualCampaignsAction(HttpServletRequest request, Model model) {
		String profileid = request.getParameter("profileid");
		List<AmzAdvCampaigns> listsp = null;
		if (StringUtil.isNotEmpty(profileid)) {
			listsp = amzAdvCampaignService.getManualSpCampaignsByprofile(new BigInteger(profileid));
		}
		return listsp;
	}

	@ResponseBody
	@RequestMapping("/loadAdGroup")
	public Object getAdGroupAction(HttpServletRequest request, Model model) {
		String profileid = request.getParameter("profileid");
		String campaignsid = request.getParameter("campaignsid");
		String campaignType = request.getParameter("campaignType");
				UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(profileid) || "all".equals(profileid))
			return null;
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			return amzAdvAdgroupsSDService.getAdGroupByCampaignsId(user, new BigInteger(profileid), campaignsid);
		}else {
		     return amzAdvAdGroupService.getAdGroupByCampaignsId(user, new BigInteger(profileid), campaignsid);
		}
	}

	@ResponseBody
	@RequestMapping("/loadKeywords")
	public Object getKeywordsAction(HttpServletRequest request, Model model) {
		String profileid = request.getParameter("profileid");
		String adgroupid = request.getParameter("adgroupid");
		if (StringUtil.isEmpty(profileid))
			return null;
		List<AmzAdvKeywords> list = amzAdvKeywordsService.getKeywordsByAdgroupId(new BigInteger(profileid), adgroupid, null);
		return list;
	}

	@ResponseBody
	@RequestMapping("/loadProductAds")
	public Object getloadProductAdsAction(HttpServletRequest request, Model model) {
		String profileid = request.getParameter("profileid");
		String adgroupid = request.getParameter("adgroupid");
		String campaignType = request.getParameter("campaignType");
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(profileid))
			return null;
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			List<AmzAdvProductadsSD> list = amzAdvProductAdsSDService.getProductadByAdgroupId(user, new BigInteger(profileid), adgroupid);
			return list;
		}else {
			List<AmzAdvProductads> list = amzAdvProductAdsService.getProductadByAdgroupId(user, new BigInteger(profileid), adgroupid);
			return list;
		}
		
	}

	@ResponseBody
	@RequestMapping("/submitKeyword")
	public Object getadvDataByKeywordAction(HttpServletRequest request, Model model) {
		String matchType = request.getParameter("matchType");
		String keywords = request.getParameter("keywords");
		String adGroupId = request.getParameter("adGroupid");
		String profileid = request.getParameter("profileid");
		String groupid = request.getParameter("groupid");
		if (StringUtil.isNotEmpty(matchType) && StringUtil.isNotEmpty(keywords) && StringUtil.isNotEmpty(adGroupId)
				&& StringUtil.isNotEmpty(profileid) && StringUtil.isNotEmpty(groupid)) {
			String data = amzAdvBidReCommendService.bidRecommendations(new BigInteger(profileid), adGroupId, keywords, matchType);
			return data;
		} else {
			throw new BaseException("你输入的信息有误！");
		}
	}

	public static void conventMapRemoveNA(Map<String, Object> map, String key) {
		Object value1 = map.get(key);
		if (value1 != null && Double.parseDouble(value1.toString()) > Double.parseDouble("1000000000")) {
			map.put(key, null);
		}
	}

	public static void convertMapRemoveNA(List<Map<String, Object>> mapList) {
		if(mapList!=null) {
			for (Map<String, Object> item : mapList) {
				conventMapRemoveNA(item, "ACOS");
				conventMapRemoveNA(item, "ACOS1d");
				conventMapRemoveNA(item, "ACOS7d");
				conventMapRemoveNA(item, "ACOS14d");
				conventMapRemoveNA(item, "ACOS30d");
				conventMapRemoveNA(item, "ACOSPlacement");
			}
		}
	}

	public static Map<String, Object> amzAdvParameterMap(QueryForList query) {
		Map<String, Object> map = new HashMap<String, Object>();
		String profileid = query.getProfileid();
		String campaignid = query.getCampaignid();
		String adGroupid = query.getAdGroupid();
		String marketplaceid = query.getMarketplaceid();
		String groupid =query.getGroupid();
		String fromDate =query.getFromDate();
		String endDate = query.getEndDate();
		String paralist = query.getParalist();
		String campaignStatus = query.getCampaignStatus();
		String targetingType = query.getTargetingType();
		String campaignType = query.getCampaignType();
		String campaignName =query.getCampaignName();
		String serchlist =query.getSerchlist();
		if (StringUtil.isNotEmpty(campaignName)) {
			campaignName = "%"+ campaignName + "%";
		} else {
			campaignName = null;
		}
		if (campaignType == null)
			campaignType = "all";
		map.put("campaignName", campaignName);
		map.put("campaignStatus", campaignStatus);
		map.put("targetingType", targetingType);
		map.put("campaignType", campaignType);
		String adGroupsName =query.getAdGroupsName();
		String adGroupsStatus =query.getAdGroupsStatus();
		if (StringUtil.isNotEmpty(adGroupsName)) {
			adGroupsName =  "%"+ adGroupsName + "%";
		} else {
			adGroupsName = null;
		}
		if(StrUtil.isEmpty(profileid)) {
			throw new BaseException("店铺站点不能为空！");
		}
		map.put("adGroupsName", adGroupsName);
		map.put("adGroupsStatus", adGroupsStatus);
		if (StringUtil.isEmpty(fromDate) || StringUtil.isEmpty(endDate)) {
			throw new BaseException("日期区间不能为空！");
		}
		String portfolios =query.getPortfolios();
		if (portfolios != null && StringUtil.isNotEmpty(portfolios)) {
			map.put("portfolios", portfolios);
		}
		Boolean isZeroPage = query.getIsZeroPage();
		 
		map.put("adGroupid", adGroupid);
		map.put("profileid", profileid);
		map.put("campaignid", campaignid);
		map.put("marketplaceid", marketplaceid);
		map.put("groupid", groupid);
		if(fromDate!=null) {
			fromDate=fromDate.replaceAll("/","-");
		    map.put("fromDate", fromDate);
		}
		if(endDate!=null) {
			endDate=endDate.replaceAll("/","-");
			map.put("endDate", endDate);
		}
		map.put("serchlist", serchlist);
		map.put("paralist", paralist);
		map.put("isZeroPage", isZeroPage);
		return map;
	}

	public static BigDecimal operateMapsDouble(Map<String, Object> map, String name) {
		Object acos = map.get(name);
		if (acos != null) {
			String acosstr = acos.toString().trim();
			if (acosstr.equals("--") || StringUtil.isEmpty(acosstr) || Double.parseDouble(acosstr) > Double.parseDouble("1000000000")) {
				return new BigDecimal("0");
			} else {
				return new BigDecimal(acosstr);
			}
		} else {
			return new BigDecimal("0");
		}
	}

	public static Map<String, Object> SumAdvertDate(List<Map<String, Object>> list) {
		Map<String, Object> maps = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			BigDecimal totalclicks = new BigDecimal("0");
			BigDecimal totalcost = new BigDecimal("0");
			BigDecimal totalimpressions = new BigDecimal("0");
			BigDecimal totalsumUnits = new BigDecimal("0");
			BigDecimal totalsumSales = new BigDecimal("0");
			BigDecimal totalattributedUnitsOrdered = new BigDecimal("0");
			for (Map<String, Object> item : list) {
				totalclicks = totalclicks.add(AdvertController.operateMapsDouble(item, "clicks"));
				totalcost = totalcost.add(AdvertController.operateMapsDouble(item, "cost"));
				totalimpressions = totalimpressions.add(AdvertController.operateMapsDouble(item, "impressions"));
				totalsumUnits = totalsumUnits.add(AdvertController.operateMapsDouble(item, "sumUnits"));
				totalsumSales = totalsumSales.add(AdvertController.operateMapsDouble(item, "sumSales"));
				totalattributedUnitsOrdered = totalattributedUnitsOrdered.add(AdvertController.operateMapsDouble(item, "attributedUnitsOrdered"));
			}
			if (totalsumSales.compareTo(BigDecimal.ZERO) == 1) {
				BigDecimal totalacos = totalcost.multiply(new BigDecimal("100")).divide(totalsumSales, 2, RoundingMode.HALF_UP);
				maps.put("totalacos", totalacos + "%");
			} else {
				maps.put("totalacos", 0);
			}
			if (totalclicks.compareTo(BigDecimal.ZERO) == 1) {
				maps.put("totalavgcost", totalcost.divide(totalclicks, 2, RoundingMode.HALF_UP));
				BigDecimal totalcsrt = totalattributedUnitsOrdered.multiply(new BigDecimal("100")).divide(totalclicks, 2, RoundingMode.HALF_UP);
				maps.put("totalcsrt", totalcsrt + "%");
			} else {
				maps.put("totalavgcost", 0);
				maps.put("totalcsrt", 0);
			}
			if (totalimpressions.compareTo(BigDecimal.ZERO) == 1) {
				BigDecimal totalctr = totalclicks.multiply(new BigDecimal("100")).divide(totalimpressions, 2, RoundingMode.HALF_UP);
				maps.put("totalctr", totalctr + "%");
			} else {
				maps.put("totalctr", 0);
			}
			if (totalcost.compareTo(BigDecimal.ZERO) == 1) {
				BigDecimal totalroas = totalsumSales.divide(totalcost, 2, RoundingMode.HALF_UP);
				maps.put("totalroas", totalroas);
			} else {
				maps.put("totalroas", 0);
			}
			maps.put("totalcost", totalcost);
			maps.put("totalclicks", totalclicks);
			maps.put("totalimpressions", totalimpressions);
			maps.put("totalsumunits", totalsumUnits);
			maps.put("totalsumsales", totalsumSales);
			maps.put("totalattributedunitsordered", totalattributedUnitsOrdered);
		} else {
			maps.put("totalacos", 0);
			maps.put("totalavgcost", 0);
			maps.put("totalcsrt", 0);
			maps.put("totalctr", 0);
			maps.put("totalroas", 0);
			maps.put("totalcost", 0);
			maps.put("totalclicks", 0);
			maps.put("totalimpressions", 0);
			maps.put("totalsumunits", 0);
			maps.put("totalsumsales", 0);
			maps.put("totalattributedunitsordered", 0);
		}
		return maps;
	}
	
	@GetMapping("/disableAuth")
	public Result<Map<String,Object>> disableAuth(String id) {
		Map<String, Object> result = amzAdvAuthService.updateAmzAdvAuthForDisable(id);
		return Result.success(result);
	}
	
	@GetMapping(value = "/bindAdvAuthData")
	public Result<String> bindAdvAuthDataAction(String code,String state)  {
		UserInfo userinfo = UserInfoContext.get();
		if (StringUtil.isEmpty(code)) {
			 throw new BaseException("无法获取到对应Code");
		}
		String[] mystate = state.split("_");
		String region = mystate[0];
		String groupid = mystate[1];
		String userid = userinfo.getId();
		Boolean result = amzAdvAuthService.captureAmzAdvAuthByCode(userid, code, region, groupid);
		if (result == true) {
			return Result.success();
		} else {
			return Result.failed();
		}
	}	 
	@GetMapping(value = "/bindUrl")
	public Result<String> bindUrlAction(String groupid,String region)  {
		String prefix = null;
		if("NA".equals(region)){
			prefix = "https://www.amazon.com/ap/oa";
		}else if("EU".equals(region)){
			prefix = "https://eu.account.amazon.com/ap/oa";
		}else if("FE".equals(region)){
			prefix = "https://apac.account.amazon.com/ap/oa";
		} 
		AmzRegion authRegion = amzAdvAuthService.getRegion(region);
		String myurl = prefix + "?client_id=" + authRegion.getClientId()
				  + "&scope=cpc_advertising:campaign_management&response_type=code&state=" + region 
				  + "_" + groupid + "&redirect_uri=" + amzAdvAuthService.getRedirecturl();
	    return Result.success(myurl);
	 
	}
	
	
	
}
