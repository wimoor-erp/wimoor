package com.wimoor.amazon.adv.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.wimoor.amazon.adv.common.pojo.BasePageQuery;
import com.wimoor.amazon.adv.common.service.IAmazonGroupService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.common.service.IAmzAdvWarningDateService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForSumProductDTO;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumAllTypeService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumProductAdsService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
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

 

	@GetMapping("/getGroup")
	public Result<List<AmazonGroup>> getGroupAction() {
		UserInfo user = UserInfoContext.get();
		List<AmazonGroup> advGroupList = null;
		if (advGroupList == null || advGroupList.size() == 0) {
			advGroupList = amazonGoupService.findAdvShopNameByUser(user.getCompanyid());
		}
		return Result.success(advGroupList);
	}

	@PostMapping("/getsumproduct")
	public Result<Map<String, Object>> getSumProductAction(@RequestBody QueryForSumProductDTO dto){
		String groupid = dto.getGroupid();
		String profileid = dto.getProfileid();
		String marketplaceid=dto.getMarketplaceid();
		String begin = dto.getBegin();
		String end = dto.getEnd();
		UserInfo user = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", user.getCompanyid());
		if(StrUtil.isNotBlank(groupid)) {
			param.put("groupid", groupid);
		}else {
			param.put("groupid", null);
		}
		if(StrUtil.isNotBlank(profileid)) {
			param.put("profileid", profileid);
		}else {
			param.put("profileid", null);
		}
		if(StrUtil.isNotBlank(marketplaceid)) {
			param.put("marketplaceId", marketplaceid);
		}else {
			param.put("marketplaceId", null);
		}
		
		if (StringUtil.isNotEmpty(begin)) {
			param.put("begin", begin);
		}
		if (StringUtil.isNotEmpty(end)) {
			param.put("end", end);
		}
		return Result.success(amzAdvSumProductAdsService.getSumProduct(param)) ;
	}

	@GetMapping("/getallsumtype")
	public Result<Map<String, Object>> getTypeNumberAction() {
		UserInfo user = UserInfoContext.get();
		return Result.success(amzAdvSumAllTypeService.getTypeNumber(user.getCompanyid()));
	}

	@GetMapping("/getenablesumtype")
	public Result<Map<String, Object>> getEnableTypeNumberAction() {
		UserInfo user = UserInfoContext.get();
		return Result.success(amzAdvSumAllTypeService.getEnableNumber(user.getCompanyid()));
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
	
	@GetMapping("/getKeywordsWarningIndicator")
	public Result<Object> getKeywordsWarningIndicatorAction(String ftype) {
		UserInfo user = UserInfoContext.get();
		String shopid =user.getCompanyid();
		AmzAdvWarningDate amzAdvWarningDate = amzAdvWarningDateService.getWarningDateNumForFtype("keyword", ftype, shopid);
		Map<String, Object> param = setParamForAmzAdvWarningDate(amzAdvWarningDate);
		param.put("shopid", shopid);
		param.put("ftype", ftype);
		return Result.success(amzAdvReportService.getKeywordsWarningIndicator(param)) ;
	}

	@PostMapping("/getKeywordsWarningIndicatorDetail/{ftype}/{downtype}")
	public Result<PageList<Map<String, Object>>> getKeywordsWarningIndicatorDetailAction(@PathVariable String ftype,@PathVariable String downtype,@RequestBody BasePageQuery query) {
		UserInfo user = UserInfoContext.get();
		String shopid =user.getCompanyid();
		PageBounds pagebounds = query.getPageBounds();
		AmzAdvWarningDate amzAdvWarningDate = amzAdvWarningDateService.getWarningDateNumForFtype("keyword", downtype, shopid);
		Map<String, Object> param = setParamForAmzAdvWarningDate(amzAdvWarningDate);
		param.put("shopid", shopid);
		param.put("type", ftype);
		param.put("downtype", downtype);
		return Result.success(amzAdvReportService.getKeywordsWarningIndicator(param, pagebounds)) ;
	}

	@GetMapping("/getProductWarningIndicator")
	public Result<Map<String, Object>> getProductWarningIndicatorAction(String ftype) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		AmzAdvWarningDate amzAdvWarningDate = amzAdvWarningDateService.getWarningDateNumForFtype("product", ftype, shopid);
		Map<String, Object> param = setParamForAmzAdvWarningDate(amzAdvWarningDate);
		param.put("shopid", shopid);
		param.put("ftype", ftype);
		return Result.success(amzAdvReportService.getProductAdsWarningIndicator(param));
	}

	@PostMapping("/getProductWarningIndicatorDetail/{ftype}/{downtype}")
	public Result<PageList<Map<String, Object>>> getProductWarningIndicatorDetailAction(@PathVariable String ftype,@PathVariable String downtype,@RequestBody BasePageQuery query) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		AmzAdvWarningDate amzAdvWarningDate = amzAdvWarningDateService.getWarningDateNumForFtype("product", downtype, shopid);
		Map<String, Object> param = setParamForAmzAdvWarningDate(amzAdvWarningDate);
		PageBounds pagebounds = query.getPageBounds();
		param.put("shopid", shopid);
		param.put("type", ftype);
		param.put("downtype", downtype);
		return Result.success(amzAdvReportService.getProductAdsWarningIndicator(param, pagebounds));
	}
	
	@GetMapping("/getAdvWrningDate")
	public Result<AmzAdvWarningDate> getAdvWrningDateAction(String ftype,String recordType) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		return Result.success(amzAdvWarningDateService.getWarningDateNumForFtype(recordType, ftype, shopid));
	}
	
	@PostMapping("/saveAdvWrningDate")
	public Result<String> saveAdvWrningDateAction(@RequestBody AmzAdvWarningDate amzAdvWarningDate) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		if(amzAdvWarningDate.getNumImpressions()==null) {
			throw new BaseException("曝光量下降比率设置不能为空!");
		}
		if(amzAdvWarningDate.getNumClicks()==null) {
			throw new BaseException("点击量下降比率设置不能为空!");
		}
		if(amzAdvWarningDate.getNumCSRT()==null) {
			throw new BaseException("转化率下降比率设置不能为空!");
		}
		if(amzAdvWarningDate.getNumACOS()==null) {
			throw new BaseException("ACOS下降比率设置不能为空!");
		}
		if(amzAdvWarningDate.getAbsoluteNumImpressions()==null) {
			throw new BaseException("曝光量下降值设置不能为空!");
		}
		if(amzAdvWarningDate.getAbsoluteNumClicks()==null) {
			throw new BaseException("点击量下降值设置不能为空!");
		}
		if(amzAdvWarningDate.getAbsoluteNumCSRT()==null) {
			throw new BaseException("转化率下降值设置不能为空!");
		}
		if(amzAdvWarningDate.getAbsoluteNumACOS()==null) {
			throw new BaseException("ACOS下降值设置不能为空!");
		}
		amzAdvWarningDate.setShopid(shopid);
		int result = amzAdvWarningDateService.saveAmzAdvWarningDate(amzAdvWarningDate);
		if(result > 0) {
			return Result.success("SUCESS");
		}
		return Result.failed("ERROR") ;
	}

	@GetMapping("/getTop5")
	public Result<Object> getTop5Action(String profileid,String ftype) {
		if (StringUtil.isEmpty(profileid))
			return null;
		BigInteger profile = new BigInteger(profileid);
		List<Map<String, Object>> result = amzAdvReportService.getTop5(profile, ftype);
		return Result.success(result) ;
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
