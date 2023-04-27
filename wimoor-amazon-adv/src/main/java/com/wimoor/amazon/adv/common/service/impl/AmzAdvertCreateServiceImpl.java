package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvertCreateService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdgroupsHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCampaignsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductAdsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.adv.sp.service.IAmzAdvAdGroupService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductAdsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvertCreateService")
public class AmzAdvertCreateServiceImpl implements IAmzAdvertCreateService{
	@Resource
	IAmzAdvAdGroupService amzAdvAdGroupService;
	@Resource
	IAmzAdvAdgroupsHsaService amzAdvAdgroupsHsaService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	@Resource
	IAmzAdvCampaignsSDService amzAdvCampaignsSDService;
	@Resource
	IAmzAdvProductAdsService amzAdvProductAdsService;
	@Resource
	IAmzAdvKeywordsService amzAdvKeywordsService;
	@Resource
	IAmzAdvProductTargeService amzAdvProductTargeService;
	@Resource
	IAmzAdvKeywordsNegativaService amzAdvKeywordsNegativaService;
	@Resource
	IAmzAdvProductTargeNegativaService amzAdvProductTargeNegativaService;
	@Resource
	IAmzAdvAdgroupsSDService amzAdvAdgroupsSDService;
	@Resource
	IAmzAdvProductAdsSDService amzAdvProductAdsSDService;
	public Object insertFirstAdv(JSONObject jsonobject,UserInfo user) {
		BigInteger profileid = jsonobject.getBigInteger("profileid");
		BigInteger campaignid = jsonobject.getBigInteger("campaignid");
		BigInteger portfolioid = jsonobject.getBigInteger("portfolioid");
		JSONArray adGroupArray = jsonobject.getJSONArray("adGroupArray");
		String campaignType = jsonobject.getString("campaignType");
		if(campaignType!=null&&"sp".equals(campaignType.toLowerCase())) {
			if(campaignid == null) {
				Date startDate = jsonobject.getDate("startDate");
				Date endDate = jsonobject.getDate("endDate");
				String premiumbidadjustment = jsonobject.getString("premiumbidadjustment");
				BigDecimal dailyBudget = jsonobject.getBigDecimal("dailyBudget");
				String campaignname = jsonobject.getString("campaignname");
				String targetingType = jsonobject.getString("targetingType");
				String bidding = jsonobject.getString("bidding");
				Example example = new Example(AmzAdvCampaigns.class);
				Criteria criter = example.createCriteria();
				criter.andEqualTo("name", campaignname);
				criter.andEqualTo("profileid", profileid);
				criter.andNotEqualTo("state", AdvState.archived);
				AmzAdvCampaigns oldamzAdvCampaigns = amzAdvCampaignService.selectOneByExample(example);
				if(oldamzAdvCampaigns != null) {
					throw new BaseException("在该站点下,广告活动名称已经存在！");
				}
				List<AmzAdvCampaigns> campaignsList = new ArrayList<AmzAdvCampaigns>();
				AmzAdvCampaigns amzAdvCampaigns = new AmzAdvCampaigns();
				amzAdvCampaigns.setName(campaignname);
				amzAdvCampaigns.setPortfolioid(portfolioid);
				amzAdvCampaigns.setDailybudget(dailyBudget);
				amzAdvCampaigns.setState(AdvState.enabled);
				amzAdvCampaigns.setStartdate(startDate);
				amzAdvCampaigns.setEnddate(endDate);
				amzAdvCampaigns.setCampaigntype("sponsoredProducts");
				amzAdvCampaigns.setTargetingtype(targetingType);
				amzAdvCampaigns.setBidding(bidding);
				amzAdvCampaigns.setPremiumbidadjustment(premiumbidadjustment);
				campaignsList.add(amzAdvCampaigns);
				campaignsList = amzAdvCampaignService.amzCreateCampaigns(user,profileid,campaignsList);
				if(campaignsList == null) {
					throw new BaseException("亚马逊连接异常，请刷新重试！");
				}
				campaignid = campaignsList.get(0).getCampaignid();
			}
			List<AmzAdvAdgroups> adgroupsList = new ArrayList<AmzAdvAdgroups>();
			List<AmzAdvProductads> productAdsList = new ArrayList<AmzAdvProductads>();
			if (adGroupArray != null) {
				for (int i = 0; i < adGroupArray.size(); i++) {
					JSONObject adGroup = adGroupArray.getJSONObject(i);
					String adGroupName = adGroup.getString("adGroupName");
					BigDecimal defaultbid = adGroup.getBigDecimal("defaultbid");
					Example example = new Example(AmzAdvAdgroups.class);
					Criteria criter = example.createCriteria();
					criter.andEqualTo("name", adGroupName);
					criter.andEqualTo("profileid", profileid);
					criter.andEqualTo("campaignid", campaignid);
					criter.andNotEqualTo("state", AdvState.archived);
					AmzAdvAdgroups oldamzAdvAdgroups = amzAdvAdGroupService.selectOneByExample(example);
					if(oldamzAdvAdgroups != null) {
						throw new BaseException("在该站点的广告活动下,广告组名称已经存在！");
					}
					AmzAdvAdgroups amzAdvAdgroups = new AmzAdvAdgroups();
					amzAdvAdgroups.setCampaignid(campaignid);
					amzAdvAdgroups.setName(adGroupName);
					amzAdvAdgroups.setState(AdvState.enabled);
					amzAdvAdgroups.setDefaultbid(defaultbid);
					adgroupsList.add(amzAdvAdgroups);
				}
				adgroupsList = amzAdvAdGroupService.amzCreateAdGroups(user,profileid,adgroupsList);
				if(adgroupsList!=null && adgroupsList.size()>0){
					for (int i = 0; i < adGroupArray.size(); i++) {
						JSONObject adGroup = adGroupArray.getJSONObject(i);
						JSONArray skuArray = adGroup.getJSONArray("skuArray");
						JSONArray asinArray = adGroup.getJSONArray("asinArray");
						if(skuArray != null) {
							for(int j = 0; j < skuArray.size(); j++) {
								AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
								amzAdvProductads.setCampaignid(campaignid);
								amzAdvProductads.setAdgroupid(adgroupsList.get(0).getAdgroupid());
								amzAdvProductads.setState(AdvState.enabled);
								amzAdvProductads.setSku(skuArray.getJSONObject(j).getString("sku"));
								amzAdvProductads.setAsin(skuArray.getJSONObject(j).getString("asin"));
								productAdsList.add(amzAdvProductads);
							}
						} else {
							for(int j = 0; j < asinArray.size(); j++) {
								AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
								amzAdvProductads.setCampaignid(campaignid);
								amzAdvProductads.setAdgroupid(adgroupsList.get(0).getAdgroupid());
								amzAdvProductads.setState(AdvState.enabled);
								amzAdvProductads.setAsin(asinArray.getJSONObject(j).getString("asin"));
								productAdsList.add(amzAdvProductads);
							}
						}
					}
				}
				productAdsList = amzAdvProductAdsService.amzCreateProductAds(user, profileid, productAdsList);
			}
			return productAdsList;
		}else if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			if(campaignid == null) {
				Date startDate = jsonobject.getDate("startDate");
				Date endDate = jsonobject.getDate("endDate");
				BigDecimal budget = jsonobject.getBigDecimal("budget");
				String campaignname = jsonobject.getString("campaignname");
				String targetingType = jsonobject.getString("targetingType");
				Example example = new Example(AmzAdvCampaignsSD.class);
				Criteria criter = example.createCriteria();
				criter.andEqualTo("name", campaignname);
				criter.andEqualTo("profileid", profileid);
				criter.andNotEqualTo("state", AdvState.archived);
				AmzAdvCampaignsSD oldamzAdvCampaigns = amzAdvCampaignsSDService.selectOneByExample(example);
				if(oldamzAdvCampaigns != null) {
					throw new BaseException("在该站点下,广告活动名称已经存在！");
				}
				List<AmzAdvCampaignsSD> campaignsList = new ArrayList<AmzAdvCampaignsSD>();
				AmzAdvCampaignsSD amzAdvCampaigns = new AmzAdvCampaignsSD();
				amzAdvCampaigns.setName(campaignname);
				amzAdvCampaigns.setState(AdvState.enabled);
				amzAdvCampaigns.setStartdate(startDate);
				amzAdvCampaigns.setEnddate(endDate);
				amzAdvCampaigns.setBudget(budget);
				amzAdvCampaigns.setBudgettype(targetingType);
				amzAdvCampaigns.setTactic("T00020");
				campaignsList.add(amzAdvCampaigns);
				campaignsList = amzAdvCampaignsSDService.amzCreateCampaigns(user,profileid,campaignsList);
				if(campaignsList == null) {
					throw new BaseException("亚马逊连接异常，请刷新重试！");
				}
				campaignid = campaignsList.get(0).getCampaignid();
			}
			List<AmzAdvAdgroupsSD> adgroupsList = new ArrayList<AmzAdvAdgroupsSD>();
			List<AmzAdvProductadsSD> productAdsList = new ArrayList<AmzAdvProductadsSD>();
			if (adGroupArray != null) {
				for (int i = 0; i < adGroupArray.size(); i++) {
					JSONObject adGroup = adGroupArray.getJSONObject(i);
					String adGroupName = adGroup.getString("adGroupName");
					BigDecimal defaultbid = adGroup.getBigDecimal("defaultbid");
					Example example = new Example(AmzAdvAdgroups.class);
					Criteria criter = example.createCriteria();
					criter.andEqualTo("name", adGroupName);
					criter.andEqualTo("profileid", profileid);
					criter.andEqualTo("campaignid", campaignid);
					criter.andNotEqualTo("state", AdvState.archived);
					AmzAdvAdgroups oldamzAdvAdgroups = amzAdvAdGroupService.selectOneByExample(example);
					if(oldamzAdvAdgroups != null) {
						throw new BaseException("在该站点的广告活动下,广告组名称已经存在！");
					}
					AmzAdvAdgroupsSD amzAdvAdgroups = new AmzAdvAdgroupsSD();
					amzAdvAdgroups.setCampaignid(campaignid);
					amzAdvAdgroups.setName(adGroupName);
					amzAdvAdgroups.setState(AdvState.enabled);
					amzAdvAdgroups.setDefaultbid(defaultbid);
					adgroupsList.add(amzAdvAdgroups);
				}
				adgroupsList = amzAdvAdgroupsSDService.amzCreateAdGroups(user,profileid,adgroupsList);
				if(adgroupsList!=null && adgroupsList.size()>0){
					for (int i = 0; i < adGroupArray.size(); i++) {
						JSONObject adGroup = adGroupArray.getJSONObject(i);
						JSONArray skuArray = adGroup.getJSONArray("skuArray");
						JSONArray asinArray = adGroup.getJSONArray("asinArray");
						if(skuArray != null) {
							for(int j = 0; j < skuArray.size(); j++) {
								AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
								amzAdvProductads.setCampaignid(campaignid);
								amzAdvProductads.setAdgroupid(adgroupsList.get(0).getAdgroupid());
								amzAdvProductads.setState(AdvState.enabled);
								amzAdvProductads.setSku(skuArray.getJSONObject(j).getString("sku"));
								amzAdvProductads.setAsin(skuArray.getJSONObject(j).getString("asin"));
								productAdsList.add(amzAdvProductads);
							}
						} else {
							for(int j = 0; j < asinArray.size(); j++) {
								AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
								amzAdvProductads.setCampaignid(campaignid);
								amzAdvProductads.setAdgroupid(adgroupsList.get(0).getAdgroupid());
								amzAdvProductads.setState(AdvState.enabled);
								amzAdvProductads.setAsin(asinArray.getJSONObject(j).getString("asin"));
								productAdsList.add(amzAdvProductads);
							}
						}
					}
				}
				productAdsList = amzAdvProductAdsSDService.amzCreateProductAds(user, profileid, productAdsList);
			}
			return  productAdsList;
		}
		return null;
	}

	public List<Map<String, Object>> UpdateFirstAdv(JSONObject jsonobject, UserInfo user) {
		// TODO Auto-generated method stub
		BigInteger profileid = jsonobject.getBigInteger("profileid");
		BigInteger campaignid = jsonobject.getBigInteger("campaignid");
		BigInteger portfolioid = jsonobject.getBigInteger("portfolioid");
		JSONArray adGroupArray = jsonobject.getJSONArray("adGroupArray");
		Date startDate = jsonobject.getDate("startDate");
		Date endDate = jsonobject.getDate("endDate");
		String premiumbidadjustment = jsonobject.getString("premiumbidadjustment");
		BigDecimal dailyBudget = jsonobject.getBigDecimal("dailyBudget");
		String campaignname = jsonobject.getString("campaignname");
		String targetingType = jsonobject.getString("targetingType");
		String campaigntype = jsonobject.getString("campaigntype");
		String bidding = jsonobject.getString("bidding");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<AmzAdvCampaigns> campaignsList = new ArrayList<AmzAdvCampaigns>();
		
		Example example = new Example(AmzAdvCampaigns.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("campaignid", campaignid);
		crit.andEqualTo("profileid", profileid);
		crit.andEqualTo("name", campaignname);
		crit.andEqualTo("campaigntype", campaigntype);
		crit.andEqualTo("dailybudget", dailyBudget);
		crit.andEqualTo("targetingtype", targetingType);
		crit.andEqualTo("state", AdvState.enabled);
		crit.andEqualTo("premiumbidadjustment", premiumbidadjustment);
		crit.andEqualTo("startdate", startDate);
		crit.andEqualTo("bidding", bidding);
		if(endDate != null) {
			crit.andEqualTo("enddate", endDate);
		}
		if(portfolioid != null) {
			crit.andEqualTo("portfolioid", portfolioid);
		}
		AmzAdvCampaigns amzAdvCampaigns = amzAdvCampaignService.selectOneByExample(example);
		if(amzAdvCampaigns == null) {
			amzAdvCampaigns = new AmzAdvCampaigns();
			amzAdvCampaigns.setCampaignid(campaignid);
			amzAdvCampaigns.setName(campaignname);
			amzAdvCampaigns.setDailybudget(dailyBudget);
			amzAdvCampaigns.setState(AdvState.enabled);
			amzAdvCampaigns.setStartdate(startDate);
			amzAdvCampaigns.setEnddate(endDate);
			amzAdvCampaigns.setBidding(bidding);
			amzAdvCampaigns.setCampaigntype(campaigntype);
			amzAdvCampaigns.setTargetingtype(targetingType);
			amzAdvCampaigns.setPremiumbidadjustment(premiumbidadjustment);
			amzAdvCampaigns.setPortfolioid(portfolioid);
			
			campaignsList.add(amzAdvCampaigns);
			campaignsList = amzAdvCampaignService.amzUpdateSpCampaigns(user, profileid, campaignsList);
			if(campaignsList == null) {
				throw new BaseException("亚马逊链接异常，修改失败！");
			}
			map.put("AmzAdvCampaigns", campaignsList.get(0));
		}
		List<AmzAdvAdgroups> adgroupsList = new ArrayList<AmzAdvAdgroups>();
		if (adGroupArray != null) {
			for (int i = 0; i < adGroupArray.size(); i++) {
				JSONObject adGroup = adGroupArray.getJSONObject(i);
				BigInteger adGroupid = adGroup.getBigInteger("adGroupid");
				String adGroupName = adGroup.getString("adGroupName");
				BigDecimal defaultbid = adGroup.getBigDecimal("defaultbid");
				
				Example example2 = new Example(AmzAdvAdgroups.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("adgroupid", adGroupid);
				crit2.andEqualTo("campaignid", campaignid);
				crit2.andEqualTo("profileid", profileid);
				crit2.andEqualTo("name", adGroupName);
				crit2.andEqualTo("defaultbid", defaultbid);
				AmzAdvAdgroups amzAdvAdgroups = amzAdvAdGroupService.selectOneByExample(example2);
				if(amzAdvAdgroups == null) {
					amzAdvAdgroups = new AmzAdvAdgroups();
					amzAdvAdgroups.setAdgroupid(adGroupid);
					amzAdvAdgroups.setCampaignid(campaignid);
					amzAdvAdgroups.setName(adGroupName);
					amzAdvAdgroups.setState(AdvState.enabled);
					amzAdvAdgroups.setDefaultbid(defaultbid);
					adgroupsList.add(amzAdvAdgroups);
				}
			}
			if(adgroupsList.size() > 0) {
				adgroupsList = amzAdvAdGroupService.amzUpdateAdGroups(user, profileid, adgroupsList);
				if(adgroupsList == null) {
					throw new BaseException("亚马逊链接异常，修改失败！");
				}
				map.put("AmzAdvAdgroups", adgroupsList.get(0));
			}
			List<AmzAdvProductads> productAdsList = new ArrayList<AmzAdvProductads>();
			List<BigInteger> deleteproductAdsList = new ArrayList<BigInteger>();
			for (int i = 0; i < adGroupArray.size(); i++) {
				JSONObject adGroup = adGroupArray.getJSONObject(i);
				BigInteger adGroupid = adGroup.getBigInteger("adGroupid");
				JSONArray skuArray = adGroup.getJSONArray("skuArray");
				JSONArray oldskuArray = adGroup.getJSONArray("oldskuArray");
				JSONArray asinArray = adGroup.getJSONArray("asinArray");
				JSONArray oldasinArray = adGroup.getJSONArray("oldasinArray");
				List<Object> keyList = new ArrayList<Object>();
				if(skuArray != null) {
					for(int k = 0; k < oldskuArray.size(); k++) {
						if(skuArray.contains(oldskuArray.getJSONObject(k))) {
							skuArray.remove(oldskuArray.getJSONObject(k));
							//oldskuArray.remove(k);
							keyList.add(oldskuArray.getJSONObject(k));
						}
					}
					for(int m = 0; m < keyList.size(); m++) {
						oldskuArray.remove(keyList.get(m));
					}
					for(int j = 0; j < skuArray.size(); j++) {
						AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
						amzAdvProductads.setCampaignid(campaignid);
						amzAdvProductads.setAdgroupid(adGroupid);
						amzAdvProductads.setState(AdvState.enabled);
						amzAdvProductads.setSku(skuArray.getJSONObject(j).getString("sku"));
						amzAdvProductads.setAsin(skuArray.getJSONObject(j).getString("asin"));
						productAdsList.add(amzAdvProductads);
					}
					for(int h = 0; h < oldskuArray.size(); h++) {
						AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
						amzAdvProductads.setCampaignid(campaignid);
						amzAdvProductads.setAdgroupid(adGroupid);
						amzAdvProductads.setState(AdvState.enabled);
						amzAdvProductads.setSku(oldskuArray.getJSONObject(h).getString("sku"));
						amzAdvProductads.setAsin(oldskuArray.getJSONObject(h).getString("asin"));
						amzAdvProductads = amzAdvProductAdsService.selectOne(amzAdvProductads);
						if(amzAdvProductads != null && amzAdvProductads.getAdid() != null) {
							deleteproductAdsList.add(amzAdvProductads.getAdid());
						}
					}
				}
				else {
					for(int k = 0; k < oldasinArray.size(); k++) {
						if(asinArray.contains(oldasinArray.getJSONObject(k))) {
							asinArray.remove(oldasinArray.getJSONObject(k));
							//oldasinArray.remove(k);
							keyList.add(oldasinArray.getJSONObject(k));
						}
					}
					for(int m = 0; m < keyList.size(); m++) {
						oldasinArray.remove(keyList.get(m));
					}
					for(int j = 0; j < asinArray.size(); j++) {
						AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
						amzAdvProductads.setCampaignid(campaignid);
						amzAdvProductads.setAdgroupid(adGroupid);
						amzAdvProductads.setState(AdvState.enabled);
						amzAdvProductads.setAsin(asinArray.getJSONObject(j).getString("asin"));
						productAdsList.add(amzAdvProductads);
					}
					for(int h = 0; h < oldasinArray.size(); h++) {
						AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
						amzAdvProductads.setCampaignid(campaignid);
						amzAdvProductads.setAdgroupid(adGroupid);
						amzAdvProductads.setState(AdvState.enabled);
						amzAdvProductads.setAsin(oldasinArray.getJSONObject(h).getString("asin"));
						amzAdvProductads = amzAdvProductAdsService.selectOne(amzAdvProductads);
						if(amzAdvProductads != null && amzAdvProductads.getAdid() != null) {
							deleteproductAdsList.add(amzAdvProductads.getAdid());
						}
					}
				}
			}
			if(productAdsList.size() > 0) {
				productAdsList = amzAdvProductAdsService.amzCreateProductAds(user, profileid, productAdsList);
				map.put("AmzAdvProductAds", productAdsList);
			}
			if(deleteproductAdsList.size() > 0) {
				for(int i = 0; i < deleteproductAdsList.size(); i++) {
					AmzAdvProductads amzAdvProductads  = amzAdvProductAdsService.amzArchiveProductAd(user, profileid, deleteproductAdsList.get(i).toString());
					if(amzAdvProductads != null) {
						map.put("delete", "success");
					}
				}
			}
		}
		if(map.size() == 0) {
			throw new BaseException("请确认修改内容！");
		}
		list.add(map);
		return list;
	}

	public void insertAdvcrt(JSONObject jsonobject,UserInfo user) {
		// TODO Auto-generated method stub
		BigInteger profileid = jsonobject.getBigInteger("profileid");
		BigInteger campaignid = jsonobject.getBigInteger("campaignid");
		String targetbuton = jsonobject.getString("targetbuton");
		JSONArray adGroupArray = jsonobject.getJSONArray("adGroupArray");
		if (adGroupArray != null) {
			if ("keyword".equals(targetbuton)) {
				List<AmzAdvKeywords> biddableKeywords = new ArrayList<AmzAdvKeywords>();
				List<AmzAdvKeywordsNegativa> negetivaKeywords = new ArrayList<AmzAdvKeywordsNegativa>();
				for (int i = 0; i < adGroupArray.size(); i++) {
					JSONObject adGroup = adGroupArray.getJSONObject(i);
					JSONArray adGroupidArray = adGroup.getJSONArray("adGroupidArray");
					JSONArray keywordtextArray = adGroup.getJSONArray("keywordtextArray");
					JSONArray matchtypeArray = adGroup.getJSONArray("matchtypeArray");
					JSONArray keywordbidArray = adGroup.getJSONArray("keywordbidArray");
					if(matchtypeArray != null && adGroupidArray != null && keywordtextArray != null && keywordbidArray != null) {
						for(int j = 0; j < matchtypeArray.size(); j++) {
							AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
							amzAdvKeywords.setCampaignid(campaignid);
							amzAdvKeywords.setAdgroupid(adGroupidArray.getBigInteger(i));
							amzAdvKeywords.setCampaigntype(CampaignType.sp);
							amzAdvKeywords.setKeywordtext(keywordtextArray.getString(j));
							amzAdvKeywords.setMatchtype(matchtypeArray.getString(j));
							amzAdvKeywords.setBid(keywordbidArray.getBigDecimal(j));
							biddableKeywords.add(amzAdvKeywords);
						}
					}
					JSONArray negativaKeywordTextArray = adGroup.getJSONArray("negativaKeywordTextArray");
					JSONArray negativaMatchTypeArray = adGroup.getJSONArray("negativaMatchTypeArray");
					if(negativaMatchTypeArray != null && negativaMatchTypeArray != null) {
						for(int j = 0; j < negativaKeywordTextArray.size(); j++) {
							AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
							amzAdvKeywordsNegativa.setCampaignid(campaignid);
							amzAdvKeywordsNegativa.setAdgroupid(adGroupidArray.getBigInteger(i));
							amzAdvKeywordsNegativa.setKeywordtext(negativaKeywordTextArray.getString(j));
							amzAdvKeywordsNegativa.setMatchtype(negativaMatchTypeArray.getString(j));
							negetivaKeywords.add(amzAdvKeywordsNegativa);
						}
					}
				}
				amzAdvKeywordsService.amzCreateBiddableKeywords(user,profileid,biddableKeywords);
				amzAdvKeywordsNegativaService.amzCreateNegativaKeywords(user,profileid,negetivaKeywords);
			} else if ("targets".equals(targetbuton)) {
				List<AmzAdvProductTarge> productTargeList = new ArrayList<AmzAdvProductTarge>();
				List<AmzAdvProductTargeNegativa> negetivaProductTargeList = new ArrayList<AmzAdvProductTargeNegativa>();
				for (int i = 0; i < adGroupArray.size(); i++) {
					JSONObject adGroup = adGroupArray.getJSONObject(i);
					JSONArray adGroupidArray = adGroup.getJSONArray("adGroupidArray");
					JSONArray expressionArray = adGroup.getJSONArray("expressionArray");
					JSONArray expressiontypeArray = adGroup.getJSONArray("expressiontypeArray");
					JSONArray targetbidArray = adGroup.getJSONArray("targetbidArray");
					if(expressionArray != null && expressiontypeArray != null && targetbidArray != null) {
						for(int j = 0; j < expressiontypeArray.size(); j++) {
							AmzAdvProductTarge amzAdvProductTarge = new AmzAdvProductTarge();
							amzAdvProductTarge.setCampaignid(campaignid);
							amzAdvProductTarge.setAdgroupid(adGroupidArray.getBigInteger(i));
							amzAdvProductTarge.setState(AdvState.enabled);
							StringBuffer expression = new StringBuffer();
							String expressionstr = expressionArray.getString(j);
							if(StringUtil.isNotEmpty(expressionstr)) {
								String[] expre = expressionstr.split(",");
								if(expre.length > 0) {
									for(int k = 0; k < expre.length; k++) {
										String[] expre2 = expre[k].split(":");
										if(expre.length == 1) {
											expression.append("[{\"type\":" + expre2[0].replaceAll("\\{", "") + ",\"value\":" + expre2[1] + "]");
										}else {
											if(k == 0) {
												expression.append("[{\"type\":" + expre2[0].replaceAll("\\{", "") + ",\"value\":" + expre2[1] + "},");
											}else if(k < expre.length-1) {
												expression.append("{\"type\":" + expre2[0] + ",\"value\":" + expre2[1] + "},");
											}else {
												expression.append("{\"type\":" + expre2[0] + ",\"value\":" + expre2[1] + "]");
											}
										}
									}
								}
							}
							amzAdvProductTarge.setExpression(expression.toString());
							amzAdvProductTarge.setExpressiontype(expressiontypeArray.getString(j));
							amzAdvProductTarge.setBid(targetbidArray.getBigDecimal(j));
							productTargeList.add(amzAdvProductTarge);
						}
					}
					JSONArray negitivaExpressionArray = adGroup.getJSONArray("negitivaExpressionArray");
					JSONArray negitivaExpressiontypeArray = adGroup.getJSONArray("negitivaExpressiontypeArray");
					if(negitivaExpressionArray != null && negitivaExpressiontypeArray != null) {
						for(int j = 0; j < negitivaExpressiontypeArray.size(); j++) {
							AmzAdvProductTargeNegativa negitivaProductTarge = new AmzAdvProductTargeNegativa();
							negitivaProductTarge.setCampaignid(campaignid);
							negitivaProductTarge.setAdgroupid(adGroupidArray.getBigInteger(i));
							negitivaProductTarge.setState(AdvState.enabled);
							String negitivaexpressionstr = negitivaExpressionArray.getString(j);
							StringBuffer negitivaexpression = new StringBuffer();
							if(StringUtil.isNotEmpty(negitivaexpressionstr)) {
								String[] negitivaexpre = negitivaexpressionstr.split(",");
								if(negitivaexpre.length > 0) {
									for(int k = 0; k < negitivaexpre.length; k++) {
										String[] negitivaexpre2 = negitivaexpre[k].split(":");
										if(negitivaexpre.length == 1) {
											negitivaexpression.append("[{\"type\":" + negitivaexpre2[0].replaceAll("\\{", "") + ",\"value\":" + negitivaexpre2[1] + "]");
										}else {
											if(k == 0) {
												negitivaexpression.append("[{\"type\":" + negitivaexpre2[0].replaceAll("\\{", "") + ",\"value\":" +  negitivaexpre2[1] + "},");
											}else if(k < negitivaexpre.length-1) {
												negitivaexpression.append("{\"type\":" + negitivaexpre2[0] + ",\"value\":" + negitivaexpre2[1] + "},");
											}else {
												negitivaexpression.append("{\"type\":" + negitivaexpre2[0] + ",\"value\":" + negitivaexpre2[1] + "]");
											}
										}
									}
								}
							}
							negitivaProductTarge.setExpression(negitivaexpression.toString());
							negitivaProductTarge.setExpressiontype(negitivaExpressiontypeArray.getString(j));
							negetivaProductTargeList.add(negitivaProductTarge);
						}
					}
				}
				amzAdvProductTargeService.amzCreateTargetingClauses(user,profileid,productTargeList);
				amzAdvProductTargeNegativaService.amzCreateNegativeTargetingClauses(user, profileid, negetivaProductTargeList);
			}
		}
	}
	
	public void insertSBAdv(JSONObject jsonobject, UserInfo user) {
		BigInteger profileid = jsonobject.getBigInteger("profileid");
		BigInteger campaignid = jsonobject.getBigInteger("campaignid");
		BigInteger portfolioid = jsonobject.getBigInteger("portfolioid");
		String targetbuton = jsonobject.getString("targetbuton");
		JSONArray keywordArray = jsonobject.getJSONArray("keywordArray");
		JSONArray negativaKeywordArray = jsonobject.getJSONArray("negativaKeywordArray");
		JSONArray targetArray = jsonobject.getJSONArray("targetArray");
		JSONArray negitivaTargetArray = jsonobject.getJSONArray("negitivaTargetArray");

		if(campaignid == null) {
			String campaignname = jsonobject.getString("campaignname");
			BigDecimal budget = jsonobject.getBigDecimal("budget");
			String budgetType = jsonobject.getString("budgetType");
			Date startDate = jsonobject.getDate("startDate");
			Date endDate = jsonobject.getDate("endDate");
			String brandEntityId = jsonobject.getString("brandEntityId");
			String brandName = jsonobject.getString("brandName");
			String brandLogoAssetID = jsonobject.getString("brandLogoAssetID");
			String headline = jsonobject.getString("headline");
			String pageType = jsonobject.getString("pageType");
			String adFormat = jsonobject.getString("adFormat");
			String bidOptimization = jsonobject.getString("bidOptimization");
			BigDecimal bidMultiplier = jsonobject.getBigDecimal("bidMultiplier");
			String bidAdjustments=jsonobject.getString("bidAdjustments");
			String videoMediaIds = jsonobject.getString("mediaId");
			String url = jsonobject.getString("url");
			String allAsinStr = jsonobject.getString("allAsinStr");
			List<String> allAsinsList = null;
			if(StringUtil.isNotEmpty(allAsinStr)) {
				allAsinsList = new ArrayList<String>();
				String[] asinsArray = allAsinStr.split(",");
				for(int i = 0; i < asinsArray.length; i++) {
					allAsinsList.add(asinsArray[i]);
				}
			} 
			JSONArray asinsArray = jsonobject.getJSONArray("asins");
			List<String> asinsList = new ArrayList<String>();
			for(int i = 0; i < asinsArray.size(); i++) {
				asinsList.add(i,asinsArray.getString(i));
			}
			
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", campaignname);
			criter.andEqualTo("profileid", profileid);
			criter.andNotEqualTo("state", AdvState.archived);
			AmzAdvCampaignsHsa oldamzAdvCampaigns = amzAdvCampaignHsaService.selectOneByExample(example);
			if(oldamzAdvCampaigns != null) {
				throw new BaseException("在该站点下,广告活动名称已经存在！");
			}
			
			int result = 0;
			List<AmzAdvCampaignsHsa> campaignsList = new ArrayList<AmzAdvCampaignsHsa>();
			AmzAdvCampaignsHsa amzAdvCampaignsHsa = new AmzAdvCampaignsHsa();
			amzAdvCampaignsHsa.setName(campaignname);
			amzAdvCampaignsHsa.setBudgettype(budgetType);
			amzAdvCampaignsHsa.setBudget(budget);
			amzAdvCampaignsHsa.setBidoptimization(bidOptimization);
			amzAdvCampaignsHsa.setBidMultiplier(bidMultiplier);
		    amzAdvCampaignsHsa.setBidAdjustments(bidAdjustments);
			amzAdvCampaignsHsa.setPortfolioid(portfolioid);
			amzAdvCampaignsHsa.setStartdate(startDate);
			amzAdvCampaignsHsa.setEnddate(endDate);
			amzAdvCampaignsHsa.setBrandEntityId(brandEntityId);
			amzAdvCampaignsHsa.setBrandName(brandName);
			amzAdvCampaignsHsa.setBrandLogoAssetID(brandLogoAssetID);
			amzAdvCampaignsHsa.setHeadline(headline);
			amzAdvCampaignsHsa.setPageType(pageType);
			amzAdvCampaignsHsa.setUrl(url);
			amzAdvCampaignsHsa.setAdFormat(adFormat);
			amzAdvCampaignsHsa.setPortfolioid(portfolioid);
			amzAdvCampaignsHsa.setAsins(asinsList);
			if(allAsinsList != null) {
				amzAdvCampaignsHsa.setAllAsins(allAsinsList);
			}else {
				amzAdvCampaignsHsa.setAllAsins(asinsList);
			}
			JSONObject creative = new JSONObject();
			if("productCollection".equals(adFormat)) {
				creative.put("brandName",brandName);
				creative.put("brandLogoAssetID", brandLogoAssetID);
				creative.put("headline",headline);
				creative.put("asins", asinsList);
			}else {
				creative.put("type", "video");
                if(StrUtil.isNotBlank(videoMediaIds)) {
    				List<String> videoMediaIdList = new ArrayList<String>();
    				String[] videoMediaIdArray = videoMediaIds.split(",");
    				for(int index=0;index<videoMediaIdArray.length;index++) {
    					String videoid=videoMediaIdArray[index];
    					if(StrUtil.isNotEmpty(videoid)) {
    						videoMediaIdList.add(videoid);
    					}
    				}
    				creative.put("videoMediaIds",videoMediaIdList);
                }else {
                	throw new BaseException("没有正确的视频ID，请确认您已经正常上传视频文件");
                }
				creative.put("asins", asinsList);
			}
			amzAdvCampaignsHsa.setCreative(creative.toJSONString());
			campaignsList.add(amzAdvCampaignsHsa);
			if ("keyword".equals(targetbuton)) {
				List<AmzAdvKeywords> biddableKeywords = new ArrayList<AmzAdvKeywords>();
				if (keywordArray != null) {
					for (int i = 0; i < keywordArray.size(); i++) {
						JSONObject keyword = keywordArray.getJSONObject(i);
						AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
						amzAdvKeywords.setKeywordtext(keyword.getString("keywordtext"));
						amzAdvKeywords.setMatchtype(keyword.getString("matchtype"));
						amzAdvKeywords.setBid(keyword.getBigDecimal("keywordbid"));
						biddableKeywords.add(amzAdvKeywords);
					}
				}
				List<AmzAdvKeywordsNegativa> negetivaKeywords = new ArrayList<AmzAdvKeywordsNegativa>();
				if(negativaKeywordArray != null) {
					for(int j = 0; j < negativaKeywordArray.size(); j++) {
						JSONObject negativaKeyword = negativaKeywordArray.getJSONObject(j);
						AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
						amzAdvKeywordsNegativa.setKeywordtext(negativaKeyword.getString("keywordtext"));
						amzAdvKeywordsNegativa.setMatchtype(negativaKeyword.getString("matchtype"));
						negetivaKeywords.add(amzAdvKeywordsNegativa);
					}
				}
				result = amzAdvCampaignHsaService.amzCreateSBCampaignsWithKeyword(user,profileid,campaignsList,biddableKeywords,negetivaKeywords);
			}else if("targets".equals(targetbuton)){
				List<AmzAdvProductTarge> productTargeList = new ArrayList<AmzAdvProductTarge>();
				if(targetArray != null) {
					for(int j = 0; j < targetArray.size(); j++) {
						JSONObject target = targetArray.getJSONObject(j);
						AmzAdvProductTarge amzAdvProductTarge = new AmzAdvProductTarge();
						StringBuffer expression = new StringBuffer();
						String expressionstr = target.getString("expressions");
						if(StringUtil.isNotEmpty(expressionstr)) {
							String[] expre = expressionstr.split(",");
							if(expre.length > 0) {
								for(int k = 0; k < expre.length; k++) {
									String[] expre2 = expre[k].split(":");
									if(expre.length == 1) {
										expression.append("[{\"type\":" + expre2[0].replaceAll("\\{", "") + ",\"value\":" + expre2[1] + "]");
									}else {
										if(k == 0) {
											expression.append("[{\"type\":" + expre2[0].replaceAll("\\{", "") + ",\"value\":" + expre2[1] + "},");
										}else if(k < expre.length-1) {
											expression.append("{\"type\":" + expre2[0] + ",\"value\":" + expre2[1] + "},");
										}else {
											expression.append("{\"type\":" + expre2[0] + ",\"value\":" + expre2[1] + "]");
										}
									}
								}
							}
						}
						amzAdvProductTarge.setExpression(expression.toString());
						amzAdvProductTarge.setBid(target.getBigDecimal("bid"));
						productTargeList.add(amzAdvProductTarge);
					}
				}
				
				List<AmzAdvProductTargeNegativa> negetivaProductTargeList = new ArrayList<AmzAdvProductTargeNegativa>();
				if(negitivaTargetArray != null) {
					for(int j = 0; j < negitivaTargetArray.size(); j++) {
						JSONObject negitivaTarget = negitivaTargetArray.getJSONObject(j);
						AmzAdvProductTargeNegativa negitivaProductTarge = new AmzAdvProductTargeNegativa();
						String negitivaexpressionstr = negitivaTarget.getString("expressions");
						StringBuffer negitivaexpression = new StringBuffer();
						if(StringUtil.isNotEmpty(negitivaexpressionstr)) {
							String[] negitivaexpre = negitivaexpressionstr.split(",");
							if(negitivaexpre.length > 0) {
								for(int k = 0; k < negitivaexpre.length; k++) {
									String[] negitivaexpre2 = negitivaexpre[k].split(":");
									if(negitivaexpre.length == 1) {
										negitivaexpression.append("[{\"type\":" + negitivaexpre2[0].replaceAll("\\{", "") + ",\"value\":" + negitivaexpre2[1] + "]");
									}else {
										if(k == 0) {
											negitivaexpression.append("[{\"type\":" + negitivaexpre2[0].replaceAll("\\{", "") + ",\"value\":" +  negitivaexpre2[1] + "},");
										}else if(k < negitivaexpre.length-1) {
											negitivaexpression.append("{\"type\":" + negitivaexpre2[0] + ",\"value\":" + negitivaexpre2[1] + "},");
										}else {
											negitivaexpression.append("{\"type\":" + negitivaexpre2[0] + ",\"value\":" + negitivaexpre2[1] + "]");
										}
									}
								}
							}
						}
						negitivaProductTarge.setExpression(negitivaexpression.toString());
						negetivaProductTargeList.add(negitivaProductTarge);
					}
				}
				result = amzAdvCampaignHsaService.amzCreateSBCampaignsWithTarget(user,profileid,campaignsList,productTargeList,negetivaProductTargeList);
			}
			
			if(result == 0) {
				throw new BaseException("亚马逊连接异常，请刷新重试！");
			}
		}
	}

	public void amzInsertSDAdv(JSONObject jsonobject, UserInfo user) {
		// TODO Auto-generated method stub
		BigInteger profileid = jsonobject.getBigInteger("profileid");
		BigInteger campaignid = jsonobject.getBigInteger("campaignid");
		String adgroupname = jsonobject.getString("adgroupname");
		BigDecimal adgroupbid = jsonobject.getBigDecimal("groupbid");
		String targetbuton = jsonobject.getString("targetbuton");
		JSONArray targetArray = jsonobject.getJSONArray("targetArray");
		JSONArray negitivaTargetArray = jsonobject.getJSONArray("negitivaTargetArray");
		AmzAdvCampaignsSD amzAdvCampaignsSD =null;
		int result = 0;
		if(campaignid == null) {
			String campaignname = jsonobject.getString("campaignname");
			BigDecimal budget = jsonobject.getBigDecimal("budget");
			String budgetType = "daily";
			Date startDate = jsonobject.getDate("startDate");
			Date endDate = jsonobject.getDate("endDate");
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", campaignname);
			criter.andEqualTo("profileid", profileid);
			criter.andNotEqualTo("state", AdvState.archived);
			  List<AmzAdvCampaignsSD> oldamzAdvCampaigns = amzAdvCampaignsSDService.selectByExample(example);
			if(oldamzAdvCampaigns != null&&oldamzAdvCampaigns.size()>0) {
				throw new BaseException("在该站点下,广告活动名称已经存在！");
			}
			amzAdvCampaignsSD = new AmzAdvCampaignsSD();
			amzAdvCampaignsSD.setName(campaignname);
			amzAdvCampaignsSD.setBudgettype(budgetType);
			amzAdvCampaignsSD.setBudget(budget);
			amzAdvCampaignsSD.setStartdate(startDate);
			amzAdvCampaignsSD.setEnddate(endDate);
			amzAdvCampaignsSD.setTactic("T00020");
			amzAdvCampaignsSD.setState(AdvState.enabled);

		}else {
			  amzAdvCampaignsSD = amzAdvCampaignsSDService.selectByKey(campaignid);
			  amzAdvCampaignsSD.setState("isold");
		}
			AmzAdvAdgroupsSD amzAdvAdgroupsSD = new AmzAdvAdgroupsSD();
			amzAdvAdgroupsSD.setDefaultbid(adgroupbid);
			amzAdvAdgroupsSD.setName(adgroupname);
			amzAdvAdgroupsSD.setState(AdvState.enabled);
			JSONArray skuOrAsinArray = jsonobject.getJSONArray("skuOrAsinArray");
			List<AmzAdvProductadsSD> productAdsList=null;
			if(skuOrAsinArray != null) {
				productAdsList=new ArrayList<AmzAdvProductadsSD>();
				for(int j = 0; j < skuOrAsinArray.size(); j++) {
					AmzAdvProductadsSD psd=new AmzAdvProductadsSD();
					JSONObject productAds = skuOrAsinArray.getJSONObject(j);
					if(productAds.getString("sku")!=null) {
						psd.setSku(productAds.getString("sku"));
					}else {
						psd.setAsin(productAds.getString("asin"));
					}
					productAdsList.add(psd);
				}
			}
		   if("targets".equals(targetbuton)){
				List<AmzAdvProductTargeSD> productTargeList = new ArrayList<AmzAdvProductTargeSD>();
				if(targetArray != null) {
					for(int j = 0; j < targetArray.size(); j++) {
						JSONObject target = targetArray.getJSONObject(j);
						AmzAdvProductTargeSD amzAdvProductTarge = new AmzAdvProductTargeSD();
						StringBuffer expression = new StringBuffer();
						String expressionstr = target.getString("expressions");
						if(StringUtil.isNotEmpty(expressionstr)) {
							String[] expre = expressionstr.split(",");
							if(expre.length > 0) {
								for(int k = 0; k < expre.length; k++) {
									String[] expre2 = expre[k].split(":");
									if(expre.length == 1) {
										expression.append("[{\"type\":" + expre2[0].replaceAll("\\{", "") + ",\"value\":" + expre2[1] + "]");
									}else {
										if(k == 0) {
											expression.append("[{\"type\":" + expre2[0].replaceAll("\\{", "") + ",\"value\":" + expre2[1] + "},");
										}else if(k < expre.length-1) {
											expression.append("{\"type\":" + expre2[0] + ",\"value\":" + expre2[1] + "},");
										}else {
											expression.append("{\"type\":" + expre2[0] + ",\"value\":" + expre2[1] + "]");
										}
									}
								}
							}
						}
						amzAdvProductTarge.setExpressiontype("manual");
						amzAdvProductTarge.setExpression(expression.toString());
						amzAdvProductTarge.setBid(target.getBigDecimal("bid"));
						productTargeList.add(amzAdvProductTarge);
					}
				}
				
				List<AmzAdvProductTargeNegativaSD> negetivaProductTargeList = new ArrayList<AmzAdvProductTargeNegativaSD>();
				if(negitivaTargetArray != null) {
					for(int j = 0; j < negitivaTargetArray.size(); j++) {
						JSONObject negitivaTarget = negitivaTargetArray.getJSONObject(j);
						AmzAdvProductTargeNegativaSD negitivaProductTarge = new AmzAdvProductTargeNegativaSD();
						String negitivaexpressionstr = negitivaTarget.getString("expressions");
						StringBuffer negitivaexpression = new StringBuffer();
						if(StringUtil.isNotEmpty(negitivaexpressionstr)) {
							String[] negitivaexpre = negitivaexpressionstr.split(",");
							if(negitivaexpre.length > 0) {
								for(int k = 0; k < negitivaexpre.length; k++) {
									String[] negitivaexpre2 = negitivaexpre[k].split(":");
									if(negitivaexpre.length == 1) {
										negitivaexpression.append("[{\"type\":" + negitivaexpre2[0].replaceAll("\\{", "") + ",\"value\":" + negitivaexpre2[1] + "]");
									}else {
										if(k == 0) {
											negitivaexpression.append("[{\"type\":" + negitivaexpre2[0].replaceAll("\\{", "") + ",\"value\":" +  negitivaexpre2[1] + "},");
										}else if(k < negitivaexpre.length-1) {
											negitivaexpression.append("{\"type\":" + negitivaexpre2[0] + ",\"value\":" + negitivaexpre2[1] + "},");
										}else {
											negitivaexpression.append("{\"type\":" + negitivaexpre2[0] + ",\"value\":" + negitivaexpre2[1] + "]");
										}
									}
								}
							}
						}
						negitivaProductTarge.setExpression(negitivaexpression.toString());
						negitivaProductTarge.setExpressiontype("manual");
						negetivaProductTargeList.add(negitivaProductTarge);
					}
				}
	 

				result = amzAdvCampaignsSDService.amzCreateSDCampaignsWithTarget(user,profileid,
						amzAdvCampaignsSD,amzAdvAdgroupsSD,
						productAdsList,
						productTargeList,negetivaProductTargeList);
			}
			
			if(result == 0) {
				throw new BaseException("亚马逊连接异常，请刷新重试！");
			}
		
	}
}
