package com.wimoor.amazon.adv.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmazonGroup;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvertCreateService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdgroupsHsaService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductAdsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductAdsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;

@Controller
@RequestMapping("/Advertcreate")
public class AdvertCreateController {
 
	@Resource
	IAmzAdvertCreateService amzAdvertCreateService;
	@Resource
	IAmzAdvProductAdsService amzAdvProductAdsService;
	@Resource
	IAmzAdvProductAdsSDService amzAdvProductAdsSDService;
	@Resource
	IAmzAdvKeywordsService amzAdvKeywordsService;
	@Resource
	IAmzAdvProductTargeService amzAdvProductTargeService;
	@Resource
	IAmzAdvProductTargeSDService amzAdvProductTargeSDService;
	@Resource
	IAmzAdvKeywordsNegativaService amzAdvKeywordsNegativaService;
	@Resource
	IAmzAdvCampKeywordsNegativaService amzAdvCampKeywordsNegativaService;
	@Resource
	IAmzAdvProductTargeNegativaService amzAdvProductTargeNegativaService;
	@Resource
	IAmzAdvAdgroupsHsaService amzAdvAdgroupsHsaService;
	@Resource
	IAmzAdvAdgroupsSDService amzAdvAdgroupsSDService;
 

	@RequestMapping("/view")
	public String viewAction(HttpServletRequest request, Model model) {
//		UserInfo user = UserInfoContext.get();
//		List<AmazonGroup> advGroupList = userService.getCurrentUserAdvGroupList(request);
//		if(advGroupList == null || advGroupList.size() == 0) {
//			advGroupList = amazonGoupService.findAdvShopNameByUser(user.getCompanyid());
//		}
//		request.setAttribute("groupList", advGroupList);
		return "pages/advertising/advert_create";
	}

	@RequestMapping("/viewSchedule")
	public String viewScheduleAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String planid = request.getParameter("planid");
		List<AmazonGroup> advGroupList = null;
//		if(advGroupList == null || advGroupList.size() == 0) {
//			advGroupList = amazonGoupService.findAdvShopNameByUser(user.getCompanyid());
//		} 
//		request.setAttribute("groupList", advGroupList);
		request.setAttribute("planid", planid);
		return "pages/advertising/advert_createschedule";
	}

	@ResponseBody
	@RequestMapping("/amzCreateFirstAdv")
	public Object CreateAdGroupAction(HttpServletRequest request, Model model) {
		String json = request.getParameter("jsonstr");
		if(StringUtil.isEmpty(json)) return null;
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		UserInfo user = UserInfoContext.get();
		Object list = amzAdvertCreateService.insertFirstAdv(jsonobject, user);
		return list;
	}
	
	@ResponseBody
	@RequestMapping("/amzUpdateFirstAdv")
	public List<Map<String, Object>> UpdateProductAdsAction(HttpServletRequest request, Model model) {
		String json = request.getParameter("jsonstr");
		if(StringUtil.isEmpty(json)) return null;
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		UserInfo user = UserInfoContext.get();
		List<Map<String, Object>> list = amzAdvertCreateService.UpdateFirstAdv(jsonobject, user);
		return list;
	}
	
	@ResponseBody
	@RequestMapping("/amzCreateAdvcrt")
	public String CreateAdvcrtAction(HttpServletRequest request, Model model) {
		String json = request.getParameter("jsonstr");
		if(StringUtil.isEmpty(json)) return null;
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		UserInfo user = UserInfoContext.get();
		amzAdvertCreateService.insertAdvcrt(jsonobject, user);
		return "SUCCESS";
	}

	@ResponseBody
	@RequestMapping("/amzCreateSBAdv")
	public String CreateSBAdvAction(HttpServletRequest request, Model model) {
		String json = request.getParameter("jsonstr");
		if(StringUtil.isEmpty(json)) return null;
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		UserInfo user = UserInfoContext.get();
		amzAdvertCreateService.insertSBAdv(jsonobject, user);
		return "SUCCESS";
	}

	@ResponseBody
	@RequestMapping("/amzCreateSDAdv")
	public String CreateSDAdvAction(HttpServletRequest request, Model model) {
		String json = request.getParameter("jsonstr");
		if(StringUtil.isEmpty(json)) return null;
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		UserInfo user = UserInfoContext.get();
		amzAdvertCreateService.amzInsertSDAdv(jsonobject, user);
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/amzCreateProductAds")
	public Object CreateProductAdsAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String adGroupid = request.getParameter("adGroupid");
		String campaignid = request.getParameter("campaignid");
		String profileId = request.getParameter("profileId");
		String sku = request.getParameter("sku");
		String asin = request.getParameter("asin");
		String campaignType = request.getParameter("campaignType");
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			List<AmzAdvProductadsSD> productAdsList = new ArrayList<AmzAdvProductadsSD>();
			String[] skuArray = null;
			String[] asinArray = null;
			if (StringUtil.isNotEmpty(sku) && StringUtil.isNotEmpty(asin)) {
				skuArray = sku.split(",");
				asinArray = asin.split(",");
				for (int i = 0; i < skuArray.length; i++) {
					AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
					amzAdvProductads.setAdgroupid(new BigInteger(adGroupid));
					amzAdvProductads.setCampaignid(new BigInteger(campaignid));
					amzAdvProductads.setState(AdvState.enabled);
					amzAdvProductads.setSku(skuArray[i]);
					amzAdvProductads.setAsin(asinArray[i]);
					productAdsList.add(amzAdvProductads);
				}
			} else {
				asinArray = asin.split(",");
				for (int i = 0; i < asinArray.length; i++) {
					AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
					amzAdvProductads.setAdgroupid(new BigInteger(adGroupid));
					amzAdvProductads.setCampaignid(new BigInteger(campaignid));
					amzAdvProductads.setState(AdvState.enabled);
					amzAdvProductads.setAsin(asinArray[i]);
					productAdsList.add(amzAdvProductads);
				}
			}
			productAdsList = amzAdvProductAdsSDService.amzCreateProductAds(user, new BigInteger(profileId), productAdsList);
			return productAdsList;
		}else {
			List<AmzAdvProductads> productAdsList = new ArrayList<AmzAdvProductads>();
			String[] skuArray = null;
			String[] asinArray = null;
			if (StringUtil.isNotEmpty(sku) && StringUtil.isNotEmpty(asin)) {
				skuArray = sku.split(",");
				asinArray = asin.split(",");
				for (int i = 0; i < skuArray.length; i++) {
					AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
					amzAdvProductads.setAdgroupid(new BigInteger(adGroupid));
					amzAdvProductads.setCampaignid(new BigInteger(campaignid));
					amzAdvProductads.setState(AdvState.enabled);
					amzAdvProductads.setSku(skuArray[i]);
					amzAdvProductads.setAsin(asinArray[i]);
					productAdsList.add(amzAdvProductads);
				}
			} else {
				asinArray = asin.split(",");
				for (int i = 0; i < asinArray.length; i++) {
					AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
					amzAdvProductads.setAdgroupid(new BigInteger(adGroupid));
					amzAdvProductads.setCampaignid(new BigInteger(campaignid));
					amzAdvProductads.setState(AdvState.enabled);
					amzAdvProductads.setAsin(asinArray[i]);
					productAdsList.add(amzAdvProductads);
				}
			}
			productAdsList = amzAdvProductAdsService.amzCreateProductAds(user, new BigInteger(profileId), productAdsList);
			return productAdsList;
		}
		
	}

	@ResponseBody
	@RequestMapping("/amzCreateKeyword")
	public List<AmzAdvKeywords> CreateKeywordAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String adGroupid = request.getParameter("adGroupid");
		String campaignid = request.getParameter("campaignid");
		String profileId = request.getParameter("profileId");
		String keywordText = request.getParameter("keywordText");
		String matchType = request.getParameter("matchType");
		String bid = request.getParameter("bid");
		String campaignType = request.getParameter("campaignType");
		List<AmzAdvKeywords> list = new ArrayList<AmzAdvKeywords>();
		String[] keywordTextArray = keywordText.split(",");
		String[] matchTypeArray = matchType.split(",");
		String[] bidArray = bid.split(",");
		if("hsa".equals(campaignType) || "sb".equals(campaignType)) {
			Map<String, Object> adgroupParam = new HashMap<String, Object>();
			adgroupParam.put("campaignIdFilter", campaignid);
			List<AmzAdvAdgroupsHsa> adgroupList = amzAdvAdgroupsHsaService.amzGetSBAdGroupList(user, new BigInteger(profileId), adgroupParam);
			if(adgroupList == null || adgroupList.size() == 0) {
				throw new BaseException("广告活动不存在");
			}
			adGroupid = adgroupList.get(0).getAdgroupid().toString();
		}
		for (int i = 0; i < keywordTextArray.length; i++) {
			if(StrUtil.isEmpty(keywordTextArray[i]))continue;
			AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
			amzAdvKeywords.setProfileid(new BigInteger(profileId));
			amzAdvKeywords.setAdgroupid(new BigInteger(adGroupid));
			amzAdvKeywords.setCampaignid(new BigInteger(campaignid));
			amzAdvKeywords.setKeywordtext(keywordTextArray[i]);
			amzAdvKeywords.setMatchtype(matchTypeArray[i]);
			if(bidArray!=null &&bidArray.length>i&& !"null".equals(bidArray[i]) && StringUtil.isNotEmpty(bidArray[i])){
				amzAdvKeywords.setBid(new BigDecimal(bidArray[i]));
			}
			list.add(amzAdvKeywords);
		}
		list = amzAdvKeywordsService.amzCreateBiddableKeywords(user, new BigInteger(profileId), list);
		return list;
	}

	@ResponseBody
	@RequestMapping("/amzCreateKeywordNegativa")
	public List<AmzAdvKeywordsNegativa> CreateKeywordNegativaAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String adGroupid = request.getParameter("adGroupid");
		String campaignid = request.getParameter("campaignid");
		String profileId = request.getParameter("profileId");
		String keywordText = request.getParameter("keywordText");
		String matchType = request.getParameter("matchType");
		String campaignType = request.getParameter("campaignType");
		List<AmzAdvKeywordsNegativa> list = new ArrayList<AmzAdvKeywordsNegativa>();
		String[] keywordTextArray = keywordText.split(",");
		String[] matchTypeArray = matchType.split(",");
		
		if("hsa".equals(campaignType) || "sb".equals(campaignType)) {
			Map<String, Object> adgroupParam = new HashMap<String, Object>();
			adgroupParam.put("campaignIdFilter", campaignid);
			List<AmzAdvAdgroupsHsa> adgroupList = amzAdvAdgroupsHsaService.amzGetSBAdGroupList(user, new BigInteger(profileId), adgroupParam);
			if(adgroupList == null || adgroupList.size() == 0) {
				throw new BaseException("广告活动不存在！");
			}
			adGroupid = adgroupList.get(0).getAdgroupid().toString();
		}
		
		for (int i = 0; i < keywordTextArray.length; i++) {
			AmzAdvKeywordsNegativa keywordsNegativa = new AmzAdvKeywordsNegativa();
			keywordsNegativa.setAdgroupid(new BigInteger(adGroupid));
			keywordsNegativa.setCampaignid(new BigInteger(campaignid));
			keywordsNegativa.setState(AdvState.enabled);
			keywordsNegativa.setKeywordtext(keywordTextArray[i]);
			keywordsNegativa.setMatchtype(matchTypeArray[i]);
			list.add(keywordsNegativa);
		}
		list = amzAdvKeywordsNegativaService.amzCreateNegativaKeywords(user, new BigInteger(profileId), list);
		return list;
	}

	@ResponseBody
	@RequestMapping("/amzCreateCampKeywordNegativa")
	public List<AmzAdvCampKeywordsNegativa> CreateCampKeywordsNegativaAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String campaignid = request.getParameter("campaignid");
		String profileId = request.getParameter("profileId");
		String keywordText = request.getParameter("keywordText");
		String matchType = request.getParameter("matchType");
		List<AmzAdvCampKeywordsNegativa> list = new ArrayList<AmzAdvCampKeywordsNegativa>();
		String[] keywordTextArray = keywordText.split(",");
		String[] matchTypeArray = matchType.split(",");
		for (int i = 0; i < keywordTextArray.length; i++) {
			AmzAdvCampKeywordsNegativa campKeywordsNegativa = new AmzAdvCampKeywordsNegativa();
			campKeywordsNegativa.setCampaignid(new BigInteger(campaignid));
			campKeywordsNegativa.setState(AdvState.enabled);
			campKeywordsNegativa.setKeywordtext(keywordTextArray[i]);
			campKeywordsNegativa.setMatchtype(matchTypeArray[i]);
			list.add(campKeywordsNegativa);
		}
		list = amzAdvCampKeywordsNegativaService.amzCreateCampaignNegativeKeywords(user, new BigInteger(profileId), list);
		return list;
	}

	@ResponseBody
	@RequestMapping("/amzCreateProductTarge")
	public Object CreateProductTargeAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String adGroupid = request.getParameter("adGroupid");
		String campaignid = request.getParameter("campaignid");
		String profileId = request.getParameter("profileId");
		String campaignType = request.getParameter("campaignType");
		String expressionArray = request.getParameter("expressionArray");
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			List<AmzAdvProductTargeSD> list = new ArrayList<AmzAdvProductTargeSD>();
			JSONArray expressArray = GeneralUtil.getJsonArray(expressionArray);
			if (StringUtil.isNotEmpty(expressionArray)) {
				if(StringUtil.isEmpty(adGroupid)) {
					Map<String, Object> adgroupParam = new HashMap<String, Object>();
					adgroupParam.put("campaignIdFilter", campaignid);
					List<AmzAdvAdgroupsSD> adgroupList = amzAdvAdgroupsSDService.amzGetSDAdGroupList(user, new BigInteger(profileId), adgroupParam);
					if(adgroupList == null || adgroupList.size() == 0) {
						throw new BaseException("广告活动不存在");
					}
					adGroupid = adgroupList.get(0).getAdgroupid().toString();
				}
				
				for (int i = 0; i < expressArray.size(); i++) {
					AmzAdvProductTargeSD productTarge = new AmzAdvProductTargeSD();
					productTarge.setAdgroupid(new BigInteger(adGroupid));
					StringBuffer expressionBuffer = new StringBuffer();
					JSONObject expression = expressArray.getJSONObject(i);
					for(Entry<String, Object> entry:expression.entrySet()) {
						if("bid".equals(entry.getKey()))continue;
						expressionBuffer.append(",{type:\""+entry.getKey()+"\",value:\""+entry.getValue()+"\"}"); 
					}
					productTarge.setExpression("["+expressionBuffer.substring(1, expressionBuffer.length())+"]");
					productTarge.setExpressiontype("manual");
					productTarge.setBid(expression.getBigDecimal("bid"));
					productTarge.setState(AdvState.enabled);
					productTarge.setProfileid(new BigInteger(profileId));
					list.add(productTarge);
				}
				list = amzAdvProductTargeSDService.amzCreateTargetingClauses_V3(user, new BigInteger(profileId), list);
			}
			return list;
		}else {
			List<AmzAdvProductTarge> list = new ArrayList<AmzAdvProductTarge>();
			JSONArray expressArray = GeneralUtil.getJsonArray(expressionArray);
			if (StringUtil.isNotEmpty(expressionArray)) {
				if(StringUtil.isEmpty(adGroupid)) {
					Map<String, Object> adgroupParam = new HashMap<String, Object>();
					adgroupParam.put("campaignIdFilter", campaignid);
					List<AmzAdvAdgroupsHsa> adgroupList = amzAdvAdgroupsHsaService.amzGetSBAdGroupList(user, new BigInteger(profileId), adgroupParam);
					if(adgroupList == null || adgroupList.size() == 0) {
						throw new BaseException("广告活动不存在");
					}
					adGroupid = adgroupList.get(0).getAdgroupid().toString();
				}
				
				for (int i = 0; i < expressArray.size(); i++) {
					AmzAdvProductTarge productTarge = new AmzAdvProductTarge();
					productTarge.setAdgroupid(new BigInteger(adGroupid));
					productTarge.setCampaignid(new BigInteger(campaignid));
					StringBuffer expressionBuffer = new StringBuffer();
					JSONObject expression = expressArray.getJSONObject(i);
					for(Entry<String, Object> entry:expression.entrySet()) {
						if("bid".equals(entry.getKey()))continue;
						expressionBuffer.append(",{type:\""+entry.getKey()+"\",value:\""+entry.getValue()+"\"}"); 
					}
					productTarge.setExpression("["+expressionBuffer.substring(1, expressionBuffer.length())+"]");
					productTarge.setExpressiontype("manual");
					productTarge.setBid(expression.getBigDecimal("bid"));
					productTarge.setState(AdvState.enabled);
					list.add(productTarge);
				}
				list = amzAdvProductTargeService.amzCreateTargetingClauses(user, new BigInteger(profileId), list);
			}
			return list;
		}
	}

	@ResponseBody
	@RequestMapping("/amzCreateProductTargeNegativa")
	public List<AmzAdvProductTargeNegativa> CreateProductTargeNegativaAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String adGroupid = request.getParameter("adGroupid");
		String campaignid = request.getParameter("campaignid");
		String profileId = request.getParameter("profileId");
		String expressionArray = request.getParameter("expressionArray");
		List<AmzAdvProductTargeNegativa> list = new ArrayList<AmzAdvProductTargeNegativa>();
		JSONArray expressArray = GeneralUtil.getJsonArray(expressionArray);
		if (StringUtil.isNotEmpty(expressionArray)) {
			if(StringUtil.isEmpty(adGroupid)) {
				Map<String, Object> adgroupParam = new HashMap<String, Object>();
				adgroupParam.put("campaignIdFilter", campaignid);
				List<AmzAdvAdgroupsHsa> adgroupList = amzAdvAdgroupsHsaService.amzGetSBAdGroupList(user, new BigInteger(profileId), adgroupParam);
				if(adgroupList == null || adgroupList.size() == 0) {
					throw new BaseException("广告活动不存在");
				}
				adGroupid = adgroupList.get(0).getAdgroupid().toString();
			}
			
			for (int i = 0; i < expressArray.size(); i++) {
				AmzAdvProductTargeNegativa productTargeNegativa = new AmzAdvProductTargeNegativa();
				productTargeNegativa.setAdgroupid(new BigInteger(adGroupid));
				productTargeNegativa.setCampaignid(new BigInteger(campaignid));
				StringBuffer expressionBuffer = new StringBuffer();
				String expression = expressArray.getString(i);
				String[] expre = expression.split(",");
				if (expre.length > 0) {
					for (int k = 0; k < expre.length; k++) {
						String[] expre2 = expre[k].split(":");
						if (expre.length == 1) {
							expressionBuffer.append(
									"[{\"type\":" + expre2[0].replaceAll("\\{", "") + ",\"value\":" + expre2[1] + "]");
						} else {
							if (k == 0) {
								expressionBuffer.append("[{\"type\":" + expre2[0].replaceAll("\\{", "") + ",\"value\":"
										+ expre2[1] + "},");
							} else if (k < expre.length - 1) {
								expressionBuffer.append("{\"type\":" + expre2[0] + ",\"value\":" + expre2[1] + "},");
							} else {
								expressionBuffer.append("{\"type\":" + expre2[0] + ",\"value\":" + expre2[1] + "]");
							}
						}
					}
				}
				productTargeNegativa.setExpression(expressionBuffer.toString());
				productTargeNegativa.setExpressiontype("manual");
				productTargeNegativa.setState(AdvState.enabled);
				list.add(productTargeNegativa);
			}
		}
		list = amzAdvProductTargeNegativaService.amzCreateNegativeTargetingClauses(user, new BigInteger(profileId), list);
		return list;
	}

}
