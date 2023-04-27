package com.wimoor.amazon.adv.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmazonGroup;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRemind;
import com.wimoor.amazon.adv.common.pojo.AmzAdvWarningDate;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmazonGroupService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportService;
import com.wimoor.amazon.adv.common.service.IAmzAdvSumAllTypeService;
import com.wimoor.amazon.adv.common.service.IAmzAdvSumProductAdsService;
import com.wimoor.amazon.adv.common.service.IAmzAdvWarningDateService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
 

import tk.mybatis.mapper.util.StringUtil;

@RestController 
@RequestMapping("/api/v1/advManager")
public class AdvertManagerController {
 
	@Resource
	IAmzAdvSumProductAdsService amzAdvSumProductAdsService;
	@Resource
	IAmzAdvSumAllTypeService amzAdvSumAllTypeService;
	@Resource
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
	@Resource
	IAmzAdvRemindService amzAdvRemindService;
	@Resource
	IAmzAdvReportService amzAdvReportService;
	@Resource
	IAmzAdvWarningDateService amzAdvWarningDateService;
	@Resource
	IAmazonGroupService amazonGoupService;

	@RequestMapping("/view")
	public String viewAction(HttpServletRequest request, Model model) {
		String plumparam = request.getParameter("plumparam");
		JSONObject plumparamjson = GeneralUtil.getJsonObject(plumparam);
		if (plumparamjson != null) {
			model.addAttribute("plumparam", plumparamjson);
		}
		return "pages/advertising/managerList/advert_manager_list";
	}

	@GetMapping("/getGroup")
	public Result<List<AmazonGroup>> getGroupAction() {
		UserInfo user = UserInfoContext.get();
		List<AmazonGroup> advGroupList = null;
		if (advGroupList == null || advGroupList.size() == 0) {
			advGroupList = amazonGoupService.findAdvShopNameByUser(user.getCompanyid());
		}
		return Result.success(advGroupList);
	}

	@ResponseBody
	@RequestMapping("/getsumproduct")
	public Map<String, Object> getSumProductAction(HttpServletRequest request, Model model) {
		String groupid = request.getParameter("groupid");
		String profileid = request.getParameter("profileid");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		UserInfo user = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", user.getCompanyid());
		param.put("groupid", groupid);
		param.put("profileid", profileid);
		if (StringUtil.isNotEmpty(begin)) {
			param.put("begin", begin);
		}
		if (StringUtil.isNotEmpty(end)) {
			param.put("end", end);
		}
		return amzAdvSumProductAdsService.getSumProduct(param);
	}

	@ResponseBody
	@RequestMapping("/getallsumtype")
	public Map<String, Object> getTypeNumberAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		return amzAdvSumAllTypeService.getTypeNumber(user.getCompanyid());
	}

	@ResponseBody
	@RequestMapping("/getenablesumtype")
	public Map<String, Object> getEnableTypeNumberAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		return amzAdvSumAllTypeService.getEnableNumber(user.getCompanyid());
	}

	@ResponseBody
	@RequestMapping("/getAdvRemindList")
	public Object getAdvRemindListAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String group = request.getParameter("groupid");
		String profileid = request.getParameter("profileid");
		String campaingsid = request.getParameter("campaingsid");
		String adgroupsid = request.getParameter("adgroupsid");
		String creatorid = request.getParameter("creatorid");
		String search = request.getParameter("search");

		Map<String, Object> param = new HashMap<String, Object>();
		if (StringUtil.isEmpty(group) || "0".equals(group) || "all".equals(group)) {
			group = null;
		}
		if (StringUtil.isEmpty(profileid) || "0".equals(profileid) || "all".equals(profileid)) {
			profileid = null;
		}
		if (StringUtil.isEmpty(campaingsid) || "0".equals(campaingsid) || "all".equals(campaingsid)) {
			campaingsid = null;
		}
		if (StringUtil.isEmpty(adgroupsid) || "0".equals(adgroupsid) || "all".equals(adgroupsid)) {
			adgroupsid = null;
		}
		if (StringUtil.isEmpty(adgroupsid) || "0".equals(adgroupsid) || "all".equals(adgroupsid)) {
			adgroupsid = null;
		}
		if (StringUtil.isEmpty(creatorid) || "0".equals(creatorid) || "all".equals(creatorid)) {
			creatorid = null;
		}
		if (StringUtil.isEmpty(search) || "0".equals(search) || "all".equals(search)) {
			search = null;
		} else {
			search = search.trim() + "%";
		}
		param.put("shopid", shopid);
		param.put("groupid", group);
		if (group == null && profileid != null) {
			param.put("marketplaceid", profileid);
		} else {
			param.put("profileid", profileid);
		}
		param.put("campaignid", campaingsid);
		param.put("adgroupid", adgroupsid);
		param.put("shopid", shopid);
		param.put("search", search);
		param.put("creatorid", creatorid);
		PageBounds pagebounds = ListController.getPageBounds(request);
		PageList<Map<String, Object>> list = amzAdvRemindService.selectByCondition(param, pagebounds);
		return list;
	}

	@RequestMapping("/viewIndicatorDetail")
	public String viewIndicatorDetailAction(HttpServletRequest request, Model model) {
		String plumparam = request.getParameter("plumparam");
		JSONObject plumparamjson = GeneralUtil.getJsonObject(plumparam);
		if (plumparamjson != null) {
			model.addAttribute("plumparam", plumparamjson);
		}
		return "pages/advertising/advert_indicator";
	}
	
	@ResponseBody
	@RequestMapping("/getKeywordsWarningIndicator")
	public Object getKeywordsWarningIndicatorAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		String shopid =user.getCompanyid();
		String ftype = request.getParameter("ftype");
		AmzAdvWarningDate amzAdvWarningDate = amzAdvWarningDateService.getWarningDateNumForFtype("keyword", ftype, shopid);
		Map<String, Object> param = setParamForAmzAdvWarningDate(amzAdvWarningDate);
		param.put("shopid", shopid);
		param.put("ftype", ftype);
		return amzAdvReportService.getKeywordsWarningIndicator(param);
	}

	@ResponseBody
	@RequestMapping("/getKeywordsWarningIndicatorDetail")
	public PageList<Map<String, Object>> getKeywordsWarningIndicatorDetailAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		String shopid =user.getCompanyid();
		String ftype = request.getParameter("ftype");
		String downtype = request.getParameter("downtype");
		PageBounds pagebounds = ListController.getPageBounds(request);
		AmzAdvWarningDate amzAdvWarningDate = amzAdvWarningDateService.getWarningDateNumForFtype("keyword", downtype, shopid);
		Map<String, Object> param = setParamForAmzAdvWarningDate(amzAdvWarningDate);
		param.put("shopid", shopid);
		param.put("type", ftype);
		param.put("downtype", downtype);
		return amzAdvReportService.getKeywordsWarningIndicator(param, pagebounds);
	}

	@ResponseBody
	@RequestMapping("/getProductWarningIndicator")
	public Object getProductWarningIndicatorAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String ftype = request.getParameter("ftype");
		AmzAdvWarningDate amzAdvWarningDate = amzAdvWarningDateService.getWarningDateNumForFtype("product", ftype, shopid);
		Map<String, Object> param = setParamForAmzAdvWarningDate(amzAdvWarningDate);
		param.put("shopid", shopid);
		param.put("ftype", ftype);
		return amzAdvReportService.getProductAdsWarningIndicator(param);
	}

	@ResponseBody
	@RequestMapping("/getProductWarningIndicatorDetail")
	public PageList<Map<String, Object>> getProductWarningIndicatorDetailAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String ftype = request.getParameter("ftype");
		String downtype = request.getParameter("downtype");
		AmzAdvWarningDate amzAdvWarningDate = amzAdvWarningDateService.getWarningDateNumForFtype("product", downtype, shopid);
		Map<String, Object> param = setParamForAmzAdvWarningDate(amzAdvWarningDate);
		PageBounds pagebounds = ListController.getPageBounds(request);
		param.put("shopid", shopid);
		param.put("type", ftype);
		param.put("downtype", downtype);
		return amzAdvReportService.getProductAdsWarningIndicator(param, pagebounds);
	}
	
	@ResponseBody
	@RequestMapping("/getAdvWrningDate")
	public Object getAdvWrningDateAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String ftype = request.getParameter("ftype");
		String recordType = request.getParameter("recordType");
		return amzAdvWarningDateService.getWarningDateNumForFtype(recordType, ftype, shopid);
	}
	
	@ResponseBody
	@RequestMapping("/saveAdvWrningDate")
	public Object saveAdvWrningDateAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String ftype = request.getParameter("ftype");
		String recordType = request.getParameter("recordType");
		String impressionsNumDate = request.getParameter("impressionsNumDate");
		String clicksNumDate = request.getParameter("clicksNumDate");
		String CSRTNumDate = request.getParameter("CSRTNumDate");
		String ACOSNumDate = request.getParameter("ACOSNumDate");
		String absoluteNumImpressions = request.getParameter("absoluteNumImpressions");
		String absoluteNumClicks = request.getParameter("absoluteNumClicks");
		String absoluteNumCSRT = request.getParameter("absoluteNumCSRT");
		String absoluteNumACOS = request.getParameter("absoluteNumACOS");
		if(StringUtil.isEmpty(impressionsNumDate)) {
			throw new BaseException("曝光量下降比率设置不能为空!");
		}
		if(StringUtil.isEmpty(clicksNumDate)) {
			throw new BaseException("点击量下降比率设置不能为空!");
		}
		if(StringUtil.isEmpty(CSRTNumDate)) {
			throw new BaseException("转化率下降比率设置不能为空!");
		}
		if(StringUtil.isEmpty(ACOSNumDate)) {
			throw new BaseException("ACOS下降比率设置不能为空!");
		}
		if(StringUtil.isEmpty(absoluteNumImpressions)) {
			throw new BaseException("曝光量下降值设置不能为空!");
		}
		if(StringUtil.isEmpty(absoluteNumClicks)) {
			throw new BaseException("点击量下降值设置不能为空!");
		}
		if(StringUtil.isEmpty(absoluteNumCSRT)) {
			throw new BaseException("转化率下降值设置不能为空!");
		}
		if(StringUtil.isEmpty(absoluteNumACOS)) {
			throw new BaseException("ACOS下降值设置不能为空!");
		}
		AmzAdvWarningDate amzAdvWarningDate = new AmzAdvWarningDate();
		amzAdvWarningDate.setFtype(ftype);
		amzAdvWarningDate.setShopid(shopid);
		amzAdvWarningDate.setRecordtype(recordType);
		amzAdvWarningDate.setNumImpressions(new BigDecimal(impressionsNumDate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
		amzAdvWarningDate.setNumClicks(new BigDecimal(clicksNumDate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
		amzAdvWarningDate.setNumCSRT(new BigDecimal(CSRTNumDate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
		amzAdvWarningDate.setNumACOS(new BigDecimal(ACOSNumDate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
		amzAdvWarningDate.setAbsoluteNumImpressions(Integer.parseInt(absoluteNumImpressions));
		amzAdvWarningDate.setAbsoluteNumClicks(Integer.parseInt(absoluteNumClicks));
		amzAdvWarningDate.setAbsoluteNumCSRT(new BigDecimal(absoluteNumCSRT.toString()));
		amzAdvWarningDate.setAbsoluteNumACOS(new BigDecimal(absoluteNumACOS.toString()));
		int result = amzAdvWarningDateService.saveAmzAdvWarningDate(amzAdvWarningDate);
		if(result > 0) {
			return "SUCESS";
		}
		return "ERROR";
	}

	@ResponseBody
	@RequestMapping("/getTop5")
	public Object getTop5Action(HttpServletRequest request) {
		String profileid = request.getParameter("profileid");
		String ftype = request.getParameter("ftype");
		if (StringUtil.isEmpty(profileid))
			return null;
		BigInteger profile = new BigInteger(profileid);
		List<Map<String, Object>> result = amzAdvReportService.getTop5(profile, ftype);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/saveRemaind")
	public Object saveRemaindAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		String profileid = request.getParameter("profileid");
		String campaingsid = request.getParameter("campaingsid");
		String adgroupid = request.getParameter("adgroupid");
		String productadsid = request.getParameter("productadsid");
		String keywordsid = request.getParameter("keywordsid");
		String targetid = request.getParameter("targetid");
		String days = request.getParameter("days");
		String quota = request.getParameter("quota");
		String fcondition = request.getParameter("fcondition");
		String subtrahend = request.getParameter("subtrahend");
		String amount = request.getParameter("amount");
		if (profileid == null) {
			return null;
		}
		AmzAdvRemind entity = new AmzAdvRemind();
		if (StringUtil.isEmpty(profileid) || "0".equals(profileid) || "all".equals(profileid)) {
			throw new BaseException("必须对应站点");
		}
		if (StringUtil.isEmpty(amount)) {
			throw new BaseException("请输入对应条件值！");
		}
		if (StringUtil.isEmpty(campaingsid) || "0".equals(campaingsid) || "all".equals(campaingsid)) {
			throw new BaseException("必须对应广告活动");
		}
		entity.setProfileid(new BigInteger(profileid));
		entity.setCampaignid(new BigInteger(campaingsid));
		if (StringUtil.isEmpty(adgroupid) || "all".equals(adgroupid)) {
			entity.setAdgroupid(new BigInteger("0"));
		} else {
			entity.setAdgroupid(new BigInteger(adgroupid));
		}
		if (StringUtil.isEmpty(productadsid) || "all".equals(productadsid)) {
			entity.setAdid(new BigInteger("0"));
		} else {
			entity.setAdid(new BigInteger(productadsid));
		}
		if (StringUtil.isEmpty(keywordsid) || "all".equals(keywordsid)) {
			entity.setKeywordid(new BigInteger("0"));
		} else {
			entity.setKeywordid(new BigInteger(keywordsid));
		}
		if (StringUtil.isEmpty(targetid) || "all".equals(targetid)) {
			entity.setTargetid(new BigInteger("0"));
		} else {
			entity.setTargetid(new BigInteger(targetid));
		}
		if ("lg".equals(fcondition)) {
			entity.setCondition("1");
		} else {
			entity.setCondition("2");
		}
		entity.setCycle(Integer.parseInt(days));
		entity.setOperator(user.getId());
		entity.setOpttime(new Date());
		entity.setQuota(quota);
		entity.setSubtrahend(subtrahend);
		try {
			entity.setAmount(new BigDecimal(amount));
		} catch (Exception e) {
			throw new BaseException("请输入正确的条件值！如果数值带%，请确认系统输入框后是否存在%，若存在，请将其在输入框中去掉。");
		}
		String recordType = "";
		if (new BigInteger("0").equals(entity.getAdgroupid()) && new BigInteger("0").equals(entity.getKeywordid())
				&& new BigInteger("0").equals(entity.getTargetid()) && new BigInteger("0").equals(entity.getAdid())
				&& !new BigInteger("0").equals(entity.getCampaignid())) {
			recordType = "campaign";
		} else if (!new BigInteger("0").equals(entity.getAdgroupid())
				&& new BigInteger("0").equals(entity.getKeywordid()) && new BigInteger("0").equals(entity.getTargetid())
				&& new BigInteger("0").equals(entity.getAdid())
				&& !new BigInteger("0").equals(entity.getCampaignid())) {
			recordType = "adGroup";
		} else if (!new BigInteger("0").equals(entity.getAdgroupid())
				&& new BigInteger("0").equals(entity.getKeywordid()) && new BigInteger("0").equals(entity.getTargetid())
				&& !new BigInteger("0").equals(entity.getAdid())
				&& !new BigInteger("0").equals(entity.getCampaignid())) {
			recordType = "productAd";
		} else if (!new BigInteger("0").equals(entity.getAdgroupid())
				&& new BigInteger("0").equals(entity.getKeywordid())
				&& !new BigInteger("0").equals(entity.getTargetid()) && new BigInteger("0").equals(entity.getAdid())
				&& !new BigInteger("0").equals(entity.getCampaignid())) {
			recordType = "target";
		} else if (!new BigInteger("0").equals(entity.getAdgroupid())
				&& !new BigInteger("0").equals(entity.getKeywordid())
				&& new BigInteger("0").equals(entity.getTargetid()) && new BigInteger("0").equals(entity.getAdid())
				&& !new BigInteger("0").equals(entity.getCampaignid())) {
			recordType = "keyword";
		}
		entity.setRecordtype(recordType);
		return amzAdvRemindService.save(entity);
	}
	
	@ResponseBody
	@RequestMapping("/deleteRemaind")
	public Object deleteRemaindAction(HttpServletRequest request) {
		String profileid = request.getParameter("profileid");
		String campaignid = request.getParameter("campaignid");
		String adgroupid = request.getParameter("adgroupid");
		String adid = request.getParameter("adid");
		String keywordid = request.getParameter("keywordid");
		String targetid = request.getParameter("targetid");
		if (profileid == null) {
			return null;
		}
		AmzAdvRemind entity = new AmzAdvRemind();
		if (StringUtil.isEmpty(profileid) || "0".equals(profileid) || "all".equals(profileid)) {
			throw new BaseException("必须对应站点");
		}
		entity.setProfileid(new BigInteger(profileid));
		if (StringUtil.isEmpty(campaignid) || "0".equals(campaignid) || "all".equals(campaignid)) {
			throw new BaseException("必须对应广告活动");
		}
		entity.setCampaignid(new BigInteger(campaignid));
		if (StringUtil.isEmpty(adgroupid) || "all".equals(adgroupid)) {
			entity.setAdgroupid(new BigInteger("0"));
		} else {
			entity.setAdgroupid(new BigInteger(adgroupid));
		}
		if (StringUtil.isEmpty(adid) || "all".equals(adid)) {
			entity.setAdid(new BigInteger("0"));
		} else {
			entity.setAdid(new BigInteger(adid));
		}
		if (StringUtil.isEmpty(keywordid) || "all".equals(keywordid)) {
			entity.setKeywordid(new BigInteger("0"));
		} else {
			entity.setKeywordid(new BigInteger(keywordid));
		}
		if (StringUtil.isEmpty(targetid) || "all".equals(targetid)) {
			entity.setTargetid(new BigInteger("0"));
		} else {
			entity.setTargetid(new BigInteger(targetid));
		}
		return amzAdvRemindService.delete(entity);
	}

	@ResponseBody
	@RequestMapping("/remaindUserList")
	public Map<String, Object> remaindUserListAction(HttpServletRequest request) {
		UserInfo user = UserInfoContext.get();
		List<Map<String, Object>> itemlist = amzAdvRemindService.getRemaindUserList(user);
		String defaultValue = "all";
		for (Map<String, Object> item : itemlist) {
			String id = item.get("id").toString();
			if (user.getId().equals(id)) {
				defaultValue = user.getId();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itemlist", itemlist);
		map.put("defaultValue", defaultValue);
		return map;
	}
	
	public static Map<String,Object> setParamForAmzAdvWarningDate(AmzAdvWarningDate amzAdvWarningDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		if(amzAdvWarningDate != null) {
			param.put("numImpressions", amzAdvWarningDate.getNumImpressions());
			param.put("numClicks", amzAdvWarningDate.getNumClicks());
			param.put("numCSRT", amzAdvWarningDate.getNumCSRT());
			param.put("numACOS", amzAdvWarningDate.getNumACOS());
			param.put("absoluteNumImpressions", amzAdvWarningDate.getAbsoluteNumImpressions());
			param.put("absoluteNumClicks", amzAdvWarningDate.getAbsoluteNumClicks());
			param.put("absoluteNumCSRT", amzAdvWarningDate.getAbsoluteNumCSRT());
			param.put("absoluteNumACOS", amzAdvWarningDate.getAbsoluteNumACOS());
		}else {
			param.put("numImpressions", new BigDecimal("0.2"));
			param.put("numClicks", new BigDecimal("0.2"));
			param.put("numCSRT", new BigDecimal("0.2"));
			param.put("numACOS", new BigDecimal("0.2"));
			param.put("absoluteNumImpressions", 0);
			param.put("absoluteNumClicks", 0);
			param.put("absoluteNumCSRT", new BigDecimal("0"));
			param.put("absoluteNumACOS", new BigDecimal("0"));
		}
		return param;
	}
}
