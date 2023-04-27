package com.wimoor.amazon.adv.sb.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvCampaignHsaService")
public class AmzAdvCampaignHsaServiceImpl extends BaseService<AmzAdvCampaignsHsa> implements IAmzAdvCampaignHsaService {
	@Resource
	AmzAdvCampaignsHsaMapper amzAdvCampaignsHsaMapper;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	@Lazy
	IAmzAdvKeywordsService amzAdvKeywordsService;
	@Resource
	IAmzAdvKeywordsNegativaService amzAdvKeywordsNegativaService;
	@Resource
	IAmzAdvProductTargeNegativaService amzAdvProductTargeNegativaService;
	@Resource
	IAmzAdvProductTargeService amzAdvProductTargeService;
	@Resource
	AmzAdvKeywordsMapper amzAdvKeywordsMapper;
	
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

	public List<AmzAdvCampaignsHsa> getHsaCampaignsByprofile(BigInteger profileid) {
		if (profileid != null) {
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			example.setOrderByClause("name asc");
			List<AmzAdvCampaignsHsa> list = amzAdvCampaignsHsaMapper.selectByExample(example);
			return list;
		} else {
			throw new BaseException("请选择店铺与站点！");
		}
	}

	public AmzAdvCampaignsHsa getHsaCampaignsByName(BigInteger profileid, String name) {
		if (profileid != null) {
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			crit.andEqualTo("name", name);
			AmzAdvCampaignsHsa amzAdvCampaignsHsa = amzAdvCampaignsHsaMapper.selectOneByExample(example);
			return amzAdvCampaignsHsa;
		} else {
			throw new BaseException("请选择店铺与站点！");
		}
	}

	public List<AmzAdvCampaignsHsa> getHsaCampaignsNotArchivedByprofile(BigInteger profileid) {
		if (profileid != null) {
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			crit.andNotEqualTo("state", "archived");
			example.setOrderByClause("name asc");
			List<AmzAdvCampaignsHsa> list = amzAdvCampaignsHsaMapper.selectByExample(example);
			return list;
		} else {
			throw new BaseException("请选择店铺与站点！");
		}
	}

	public int amzCreateSBCampaignsWithKeyword(UserInfo user,BigInteger  profileId, List<AmzAdvCampaignsHsa> campaignsList, 
			List<AmzAdvKeywords> biddableKeywordList,List<AmzAdvKeywordsNegativa> negetivaKeywordList) {
		if (campaignsList == null || campaignsList.size() <= 0) {
			throw new BaseException("创建的广告活动不存在！");
		}
		if(biddableKeywordList == null || biddableKeywordList.size() == 0) {
			throw new BaseException("创建改广告活动必须指定关键词！");
		}
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/campaigns";
		JSONArray array = new JSONArray();
		for (int i = 0; i < campaignsList.size(); i++) {
			AmzAdvCampaignsHsa campaigns = campaignsList.get(i);
			JSONObject param = new JSONObject();
			param.put("name", campaigns.getName());
			param.put("budget", campaigns.getBudget());
			param.put("budgetType", campaigns.getBudgettype());
			if("productCollection".equals(campaigns.getAdFormat())) {
				if(campaigns.getBidoptimization() != null) {
					param.put("bidOptimization", campaigns.getBidoptimization());
				}
				if(campaigns.getBidMultiplier() != null) {
					param.put("bidMultiplier", campaigns.getBidMultiplier());
				}
				if(campaigns.getBidAdjustments()!=null) {
					param.put("bidAdjustments", GeneralUtil.getJsonObject(campaigns.getBidAdjustments()));
				}
				JSONObject landingPage = new JSONObject();
				if("store".equals(campaigns.getPageType())) {
					landingPage.put("url", campaigns.getUrl());
				}else {
					landingPage.put("asins", campaigns.getAllAsins());
				}
				param.put("landingPage", landingPage);
			}
			if(campaigns.getPortfolioid() != null) {
				param.put("portfolioId", campaigns.getPortfolioid());
			}
			param.put("startDate", fmt.format(campaigns.getStartdate()));
			if(campaigns.getEnddate() != null) {
				param.put("endDate", fmt.format(campaigns.getEnddate()));
			}
			param.put("brandEntityId", campaigns.getBrandEntityId());
			

			param.put("creative",GeneralUtil.getJsonObject(campaigns.getCreative()));
			
			
			JSONArray arrayKeywords = new JSONArray();
			for (int j = 0; j < biddableKeywordList.size(); j++) {
				AmzAdvKeywords amzAdvKeywords = biddableKeywordList.get(j);
				JSONObject paramKeywords = new JSONObject();
				paramKeywords.put("keywordText", amzAdvKeywords.getKeywordtext());
				paramKeywords.put("matchType", amzAdvKeywords.getMatchtype().toLowerCase());
				paramKeywords.put("bid", amzAdvKeywords.getBid());
				arrayKeywords.add(paramKeywords);
			}
			param.put("keywords", arrayKeywords);
			
			JSONArray arrayNegetivaKeywords = new JSONArray();
			for (int j = 0; j < negetivaKeywordList.size(); j++) {
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(j);
				JSONObject paramKeywordsNegativa = new JSONObject();
				paramKeywordsNegativa.put("keywordText", amzAdvKeywordsNegativa.getKeywordtext());
				paramKeywordsNegativa.put("matchType", amzAdvKeywordsNegativa.getMatchtype());
				arrayNegetivaKeywords.add(paramKeywordsNegativa);
			}
			param.put("negativeKeywords", arrayNegetivaKeywords);
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost_V3(profile, url, array.toJSONString());
		if (StringUtil.isNotEmpty(response)) {
			Boolean negetiveFlag = false;
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for(int i = 0; i < a.size(); i++) {
				AmzAdvCampaignsHsa campaigns = campaignsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger campaignId = item.getBigInteger("campaignId");
					campaigns.setCampaignid(campaignId);
					campaigns.setState(AdvState.pending);
					campaigns.setProfileid(profileId);
					campaigns.setOpttime(new Date());
					AmzAdvCampaignsHsa Dbcampaigns = this.selectByKey(campaigns);
					if(Dbcampaigns == null) {
						this.save(campaigns);
						amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsHsa", user.getId(), profileId, campaigns, null);
					}
					
					JSONArray adgroupArray = item.getJSONArray("adGroupResponses");
					BigInteger adGroupId = adgroupArray.getJSONObject(0).getBigInteger("adGroupId");
					
					JSONArray keywordArray = item.getJSONArray("keywordResponses");
					for(int j = 0; j < keywordArray.size(); j++) {
						AmzAdvKeywords amzAdvKeywords = biddableKeywordList.get(j);
						JSONObject keyworditem = keywordArray.getJSONObject(j);
						if ("SUCCESS".equals(keyworditem.getString("code"))) {
							BigInteger keywordId = keyworditem.getBigInteger("keywordId");
							amzAdvKeywords.setKeywordid(keywordId);
							amzAdvKeywords.setCampaignid(campaignId);
							amzAdvKeywords.setAdgroupid(adGroupId);
							amzAdvKeywords.setState(AdvState.pending);
							amzAdvKeywords.setCampaigntype("hsa");
							amzAdvKeywords.setProfileid(profileId);
							amzAdvKeywords.setOpttime(new Date());
							AmzAdvKeywords DbamzAdvKeywords = amzAdvKeywordsService.selectByKey(amzAdvKeywords);
							if (DbamzAdvKeywords == null) {
								amzAdvKeywordsService.save(amzAdvKeywords);
							}
						} else {
							String keywordText = amzAdvKeywords.getKeywordtext();
							errormsg = errormsg.equals("") ? "" : errormsg + ",";
							errormsg = errormsg + item.getString("description");
							BaseException exception = new BaseException("关键词：'"+keywordText+"' 创建失败：" + errormsg);
							exception.setCode("ERROER");
							throw exception;
						}
					}
					
					JSONArray negativeKeywordArray = item.getJSONArray("negativeKeywordResponses");
					for(int k = 0; k < negativeKeywordArray.size(); k++) {
						AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(k);
						JSONObject keywordsNegativaitem = negativeKeywordArray.getJSONObject(k);
						if ("SUCCESS".equals(keywordsNegativaitem.getString("code"))) {
							BigInteger keywordId = keywordsNegativaitem.getBigInteger("keywordId");
							amzAdvKeywordsNegativa.setKeywordid(keywordId);
							amzAdvKeywordsNegativa.setCampaignid(campaignId);
							amzAdvKeywordsNegativa.setAdgroupid(adGroupId);
							amzAdvKeywordsNegativa.setState(AdvState.pending);
							amzAdvKeywordsNegativa.setCampaigntype("hsa");
							amzAdvKeywordsNegativa.setProfileid(profileId);
							amzAdvKeywordsNegativa.setOpttime(new Date());
							AmzAdvKeywordsNegativa DbamzAdvKeywordsNegativa = amzAdvKeywordsNegativaService.selectByKey(amzAdvKeywordsNegativa);
							if(DbamzAdvKeywordsNegativa == null) {
								negetiveFlag = true;
								amzAdvKeywordsNegativaService.save(amzAdvKeywordsNegativa);
							}
						}else {
							String keywordText = amzAdvKeywordsNegativa.getKeywordtext();
							errormsg = errormsg.equals("") ? "" : errormsg + ",";
							errormsg = errormsg + item.getString("description");
							BaseException exception = new BaseException("否定关键词：'"+keywordText+"' 创建失败：" + errormsg);
							exception.setCode("ERROER");
							throw exception;
						}
					}
				}else {
					String name = campaigns.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动：'"+name+"' 创建失败：" + errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger, List<Object>>();
			for (int i = 0; i < biddableKeywordList.size(); i++) {
				AmzAdvKeywords keywords = biddableKeywordList.get(i);
				BigInteger key = keywords.getCampaignid().subtract(keywords.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if (keywordslist == null) {
					keywordslist = new ArrayList<Object>();
					map.put(key, keywordslist);
				}
				keywordslist.add(keywords);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywords", user.getId(), profileId, map, null);
			if(negetiveFlag) {
				Map<BigInteger, List<Object>> negetivaKeywordmap = new HashMap<BigInteger,List<Object>>();
				for (int i = 0; i < negetivaKeywordList.size(); i++) {
					AmzAdvKeywordsNegativa keywordsNegativa = negetivaKeywordList.get(i);
					BigInteger key = keywordsNegativa.getCampaignid().subtract(keywordsNegativa.getAdgroupid());
					List<Object> keywordslist = negetivaKeywordmap.get(key);
					if(keywordslist==null) {
						keywordslist=new ArrayList<Object>();
						negetivaKeywordmap.put(key, keywordslist);
					}
					keywordslist.add(keywordsNegativa);
				}
				amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywordsNegativa", user.getId(), profileId, negetivaKeywordmap, null);
			}
			return 1;
		}
		return 0;
	}
	
	public int amzCreateSBCampaignsWithTarget(UserInfo user,BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList, 
			List<AmzAdvProductTarge> productTargeList,List<AmzAdvProductTargeNegativa> negetivaProductTargeList) {
		if (campaignsList == null || campaignsList.size() <= 0) {
			throw new BaseException("创建的广告活动不存在！");
		}
		if(productTargeList == null || productTargeList.size() == 0) {
			throw new BaseException("创建该广告活动必须指定商品投放Target！");
		}
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/campaigns";
		JSONArray array = new JSONArray();
		for (int i = 0; i < campaignsList.size(); i++) {
			AmzAdvCampaignsHsa campaigns = campaignsList.get(i);
			JSONObject param = new JSONObject();
			param.put("name", campaigns.getName());
			param.put("budget", campaigns.getBudget());
			param.put("budgetType", campaigns.getBudgettype());
			if("productCollection".equals(campaigns.getAdFormat())) {
				if(campaigns.getBidoptimization() != null) {
					param.put("bidOptimization", campaigns.getBidoptimization());
				}
				if(campaigns.getBidMultiplier() != null) {
					param.put("bidMultiplier", campaigns.getBidMultiplier());
				}
				if(campaigns.getBidAdjustments()!=null) {
					param.put("bidAdjustments", GeneralUtil.getJsonObject(campaigns.getBidAdjustments()));
				}
				JSONObject landingPage = new JSONObject();
				if("store".equals(campaigns.getPageType())) {
					landingPage.put("url", campaigns.getUrl());
				}else {
					landingPage.put("asins", campaigns.getAllAsins());
				}
				param.put("landingPage", landingPage);
			}
			
			if(campaigns.getPortfolioid() != null) {
				param.put("portfolioId", campaigns.getPortfolioid());
			}
			param.put("startDate", fmt.format(campaigns.getStartdate()));
			if(campaigns.getEnddate() != null) {
				param.put("endDate", fmt.format(campaigns.getEnddate()));
			}
			param.put("brandEntityId", campaigns.getBrandEntityId());
			
			param.put("creative",GeneralUtil.getJsonObject(campaigns.getCreative()));

			
			JSONArray arrayProductTarge = new JSONArray();
			for (int j = 0; j < productTargeList.size(); j++) {
				AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(j);
				JSONObject paramProductTarge = new JSONObject();
				paramProductTarge.put("expressions", GeneralUtil.getJsonArray(amzAdvProductTarge.getExpression()));
				paramProductTarge.put("bid", amzAdvProductTarge.getBid());
				arrayProductTarge.add(paramProductTarge);
			}
			param.put("targets", arrayProductTarge);
			
			JSONArray arrayNegetivaProductTarge = new JSONArray();
			for (int j = 0; j < negetivaProductTargeList.size(); j++) {
				AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = negetivaProductTargeList.get(j);
				JSONObject paramProductTargeNegativa = new JSONObject();
				paramProductTargeNegativa.put("expression", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
				arrayNegetivaProductTarge.add(paramProductTargeNegativa);
			}
			param.put("negativeTargets", arrayNegetivaProductTarge);
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost_V3(profile, url, array.toJSONString());
		if (StringUtil.isNotEmpty(response)) {
			Boolean negetiveFlag = false;
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for(int i = 0; i < a.size(); i++) {
				AmzAdvCampaignsHsa campaigns = campaignsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger campaignId = item.getBigInteger("campaignId");
					campaigns.setCampaignid(campaignId);
					campaigns.setState(AdvState.pending);
					campaigns.setProfileid(profileId);
					campaigns.setOpttime(new Date());
					AmzAdvCampaignsHsa Dbcampaigns = this.selectByKey(campaigns);
					if(Dbcampaigns == null) {
						this.save(campaigns);
						amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsHsa", user.getId(), profileId, campaigns, null);
					}
					
					JSONArray adgroupArray = item.getJSONArray("adGroupResponses");
					BigInteger adGroupId = adgroupArray.getJSONObject(0).getBigInteger("adGroupId");
					
					JSONArray targetArray = item.getJSONArray("targetingClauseResponses");
					for(int j = 0; j < targetArray.size(); j++) {
						AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(j);
						JSONObject targetitem = targetArray.getJSONObject(j);
						if ("SUCCESS".equals(targetitem.getString("code"))) {
							BigInteger targetId = targetitem.getBigInteger("targetId");
							amzAdvProductTarge.setCampaignid(campaignId);
							amzAdvProductTarge.setAdgroupid(adGroupId);
							amzAdvProductTarge.setTargetid(targetId);
							amzAdvProductTarge.setState(AdvState.pending);
							amzAdvProductTarge.setProfileid(profileId);
							amzAdvProductTarge.setOpttime(new Date());
							AmzAdvProductTarge DbamzAdvProductTarge = amzAdvProductTargeService.selectByKey(amzAdvProductTarge);
							if (DbamzAdvProductTarge == null) {
								amzAdvProductTargeService.save(amzAdvProductTarge);
							}
						} else {
							errormsg = errormsg.equals("") ? "" : errormsg + ",";
							errormsg = errormsg + item.getString("description");
							BaseException exception = new BaseException("商品投放创建失败：" + errormsg);
							exception.setCode("ERROER");
							throw exception;
						}
					}
					
					JSONArray negativeTargetArray = item.getJSONArray("negativeTargetingClauseResponses");
					for(int k = 0; k < negativeTargetArray.size(); k++) {
						AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = negetivaProductTargeList.get(k);
						JSONObject negativeTargetitem = negativeTargetArray.getJSONObject(k);
						if ("SUCCESS".equals(negativeTargetitem.getString("code"))) {
							BigInteger targetId = negativeTargetitem.getBigInteger("targetId");
							amzAdvProductTargeNegativa.setCampaignid(campaignId);
							amzAdvProductTargeNegativa.setAdgroupid(adGroupId);
							amzAdvProductTargeNegativa.setTargetid(targetId);
							amzAdvProductTargeNegativa.setState(AdvState.pending);
							amzAdvProductTargeNegativa.setProfileid(profileId);
							amzAdvProductTargeNegativa.setOpttime(new Date());
							AmzAdvKeywordsNegativa DbamzAdvProductTargeNegativa = amzAdvKeywordsNegativaService.selectByKey(amzAdvProductTargeNegativa);
							if(DbamzAdvProductTargeNegativa == null) {
								negetiveFlag = true;
								amzAdvProductTargeNegativaService.save(amzAdvProductTargeNegativa);
							}
						}else {
							errormsg = errormsg.equals("") ? "" : errormsg + ",";
							errormsg = errormsg + item.getString("description");
							BaseException exception = new BaseException("否定关键词创建失败：" + errormsg);
							exception.setCode("ERROER");
							throw exception;
						}
					}
				}else {
					String name = campaigns.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动：'"+name+"' 创建失败：" + errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
			}
			
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTarge productTarge = productTargeList.get(i);
				BigInteger key = productTarge.getCampaignid().subtract(productTarge.getAdgroupid());
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTarge", user.getId(), profileId, map, null);
			
			if(negetiveFlag) {
				Map<BigInteger, List<Object>> negetivamap = new HashMap<BigInteger,List<Object>>();
				for (int i = 0; i < negetivaProductTargeList.size(); i++) {
					AmzAdvProductTargeNegativa productTargeNegativa = negetivaProductTargeList.get(i);
					BigInteger key = productTargeNegativa.getCampaignid().subtract(productTargeNegativa.getAdgroupid());
					List<Object> targeNegativaList = negetivamap.get(key);
					if(targeNegativaList==null) {
						targeNegativaList=new ArrayList<Object>();
						negetivamap.put(key, targeNegativaList);
					}
					targeNegativaList.add(productTargeNegativa);
				}
				amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeNegativa", user.getId(), profileId, negetivamap, null);
			}
			return 1;
		}
		return 0;
	}
	
	public AmzAdvCampaignsHsa amzGetSBCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/campaigns/" + campaignId;
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvCampaignsHsa amzAdvCampaignsHsa = new AmzAdvCampaignsHsa();
			amzAdvCampaignsHsa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvCampaignsHsa.setName(item.getString("name"));
			amzAdvCampaignsHsa.setBudgettype(item.getString("budgetType"));
			amzAdvCampaignsHsa.setBudget(item.getBigDecimal("budget"));
			amzAdvCampaignsHsa.setState(item.getString("state"));
			amzAdvCampaignsHsa.setServingstatus(item.getString("servingStatus"));
			amzAdvCampaignsHsa.setBidoptimization(item.getString("bidOptimization"));
			amzAdvCampaignsHsa.setBidAdjustments(item.getString("bidAdjustments"));
			amzAdvCampaignsHsa.setBidMultiplier(item.getBigDecimal("bidMultiplier"));
			amzAdvCampaignsHsa.setAdFormat(item.getString("adFormat"));
			amzAdvCampaignsHsa.setSpendingpolicy(item.getString("spendingPolicy"));
			amzAdvCampaignsHsa.setPortfolioid(item.getBigInteger("portfolioId"));
			try {
				if (item.getString("startDate") != null) {
					amzAdvCampaignsHsa.setStartdate(fmt.parse(item.getString("startDate")));
				}
				if (item.getString("endDate") != null) {
					amzAdvCampaignsHsa.setEnddate(fmt.parse(item.getString("endDate")));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			JSONObject creative = item.getJSONObject("creative");
			JSONArray asins = creative.getJSONArray("asins");
			List<String> asinsList = new ArrayList<String>();
			for(int i = 0; i < asins.size(); i++) {
				asinsList.add(asins.getString(i));
			}
			amzAdvCampaignsHsa.setAsins(asinsList);
			amzAdvCampaignsHsa.setBrandName(creative.getString("brandName"));
			amzAdvCampaignsHsa.setBrandLogoAssetID(creative.getString("brandLogoAssetID"));
			amzAdvCampaignsHsa.setHeadline(creative.getString("headline"));
			amzAdvCampaignsHsa.setShouldOptimizeAsins(creative.getBoolean("shouldOptimizeAsins"));
			amzAdvCampaignsHsa.setBrandLogoUrl(creative.getString("brandLogoUrl"));
			
			amzAdvCampaignsHsa.setCreative(creative.toString());
			JSONObject landingPage = item.getJSONObject("landingPage");
			amzAdvCampaignsHsa.setPageType(landingPage.getString("pageType"));
			amzAdvCampaignsHsa.setUrl(landingPage.getString("url"));
			
			amzAdvCampaignsHsa.setProfileid(profileId);
			amzAdvCampaignsHsa.setOpttime(new Date());
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", amzAdvCampaignsHsa.getCampaignid());
			AmzAdvCampaignsHsa oldamzAdvCampaignsHsa = amzAdvCampaignsHsaMapper.selectOneByExample(example);
			if (oldamzAdvCampaignsHsa != null) {
				amzAdvCampaignsHsaMapper.updateByPrimaryKey(amzAdvCampaignsHsa);
			} else {
				amzAdvCampaignsHsaMapper.insert(amzAdvCampaignsHsa);
			}
			return amzAdvCampaignsHsa;
		}
		return null;
	}
	
	public List<AmzAdvCampaignsHsa> amzUpdateSBCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList) {
		if (campaignsList == null || campaignsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvCampaignsHsa> oldcampaignsList = new ArrayList<AmzAdvCampaignsHsa>();
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/campaigns";
		JSONArray array = new JSONArray();
		for (int i = 0; i < campaignsList.size(); i++) {
			JSONObject param = new JSONObject();
			// name, budgetType, startDate budget
			AmzAdvCampaignsHsa campaignsHsa = campaignsList.get(i);
			
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaignsHsa.getCampaignid());
			AmzAdvCampaignsHsa oldCampaigns = amzAdvCampaignsHsaMapper.selectOneByExample(example);
			// api 不常改字段
			param.put("campaignId", campaignsHsa.getCampaignid());
			if(campaignsHsa.getState().equals(oldCampaigns.getState())) {
				if(campaignsHsa.getPortfolioid() == null) {
					param.put("portfolioId", null);
				}else {
					param.put("portfolioId", campaignsHsa.getPortfolioid());
				}
				if (campaignsHsa.getEnddate() != null) {
					param.put("endDate", fmt.format(campaignsHsa.getEnddate()));
				}
				if(campaignsHsa.getName() != null) {
					param.put("name", campaignsHsa.getName());
				}
				if (campaignsHsa.getBudget() != null) {
					param.put("budget", campaignsHsa.getBudget());
				}
				if (campaignsHsa.getBidoptimization() != null) {
					param.put("bidOptimization", campaignsHsa.getBidoptimization());
				}
				if (campaignsHsa.getBidMultiplier() != null) {
					param.put("bidMultiplier", campaignsHsa.getBidMultiplier());
				}
			}else {
				param.put("state", campaignsHsa.getState());
			}
			array.add(param);

			oldcampaignsList.add(oldCampaigns);
		}
		String response = amzAdvAuthService.amzAdvPut_V3(profile, url, JSONArray.toJSONString(array,SerializerFeature.WriteMapNullValue));
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for (int i = 0; i < campaignsList.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaignsHsa campaignsHsa = campaignsList.get(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger campaignId = item.getBigInteger("campaignId");
					campaignsHsa.setCampaignid(campaignId);
					campaignsHsa.setProfileid(profileId);
					campaignsHsa.setOpttime(new Date());
					amzAdvCampaignsHsaMapper.updateByPrimaryKeySelective(campaignsHsa);

					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					AmzAdvCampaignsHsa oldCampaigns = oldcampaignsList.get(i);
					operateLog.setCampaignid(campaignsHsa.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvCampaignsHsa");
					String campaignsjson = GeneralUtil.toJSON(campaignsHsa);
					String oldCampaignsjson = GeneralUtil.toJSON(oldCampaigns);
					operateLog.setAfterobject(campaignsjson.replaceAll("\\\\", ""));
					operateLog.setBeforeobject(oldCampaignsjson.replaceAll("\\\\", ""));
					operateLogList.add(operateLog);
				} else {
					String name = campaignsHsa.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动：'"+name+"' 修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			amzAdvOperateLogService.insertList(operateLogList);
			return campaignsList;
		}
		return null;
	}
	
	public AmzAdvCampaignsHsa amzArchiveSBCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/campaigns/" + campaignId;
		String response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaignId);
			AmzAdvCampaignsHsa oldCampaigns = amzAdvCampaignsHsaMapper.selectOneByExample(example);
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example2 = new Example(AmzAdvCampaignsHsa.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvCampaignsHsa campaignHsa = amzAdvCampaignsHsaMapper.selectOneByExample(example2);
				campaignHsa.setState(AdvState.archived);
				campaignHsa.setOpttime(new Date());
				amzAdvCampaignsHsaMapper.updateByPrimaryKey(campaignHsa);
				amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsHsa", user.getId(), profileId, campaignHsa, oldCampaigns);
				return campaignHsa;
			} else {
				String errormsg = "";
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + a.getString("description");
				BaseException exception = new BaseException("广告组修改失败：" + errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
		}
		return null;
	}
	
	public List<AmzAdvCampaignsHsa> amzListSBCampaigns(UserInfo user, BigInteger profileId, Map<String, Object> campaignsParam) {
		if (campaignsParam == null) {
			return null;
		}
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/campaigns?";
		String param = "";
		if (campaignsParam != null) {
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "startIndex");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "count");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "stateFilter");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "name");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "portfolioIdFilter");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "campaignIdFilter");
			url = url + ("".equals(param) ? "" : param);
		}
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
		List<AmzAdvCampaignsHsa> list = new LinkedList<AmzAdvCampaignsHsa>();
		List<AmzAdvCampaignsHsa> insertList = new LinkedList<AmzAdvCampaignsHsa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaignsHsa campaignsHsa = new AmzAdvCampaignsHsa();
				campaignsHsa.setCampaignid(item.getBigInteger("campaignId"));
				campaignsHsa.setName(item.getString("name"));
				campaignsHsa.setBudget(item.getBigDecimal("budget"));
				campaignsHsa.setState(item.getString("state"));
				campaignsHsa.setBudgettype(item.getString("budgetType"));
				campaignsHsa.setServingstatus(item.getString("servingStatus"));
				campaignsHsa.setSpendingpolicy(item.getString("spendingPolicy"));
				campaignsHsa.setBidoptimization(item.getString("bidOptimization"));
				campaignsHsa.setBidAdjustments(item.getString("bidAdjustments"));
				campaignsHsa.setAdFormat(item.getString("adFormat"));
				campaignsHsa.setPortfolioid(item.getBigInteger("portfolioId"));
				campaignsHsa.setBidMultiplier(item.getBigDecimal("bidMultiplier"));
				try {
					campaignsHsa.setStartdate(fmt.parse(item.getString("startDate")));
					if(item.getString("endDate") != null) {
						campaignsHsa.setEnddate(fmt.parse(item.getString("endDate")));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				campaignsHsa.setProfileid(profileId);
				campaignsHsa.setOpttime(new Date());
				insertList.add(campaignsHsa);
				list.add(campaignsHsa);
				if(insertList.size() > 200) {
					amzAdvCampaignsHsaMapper.insertBatch(insertList);
					insertList.clear();
				}
			}
			if(insertList.size() > 0) {
				amzAdvCampaignsHsaMapper.insertBatch(insertList);
			}
			return list;
		}
		return null;
	}
	
	public List<AmzAdvCampaignsHsa> amzListSBCampaigns(AmzAdvProfile profile, Map<String, Object> campaignsParam) {
		if (campaignsParam == null) {
			return null;
		}
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/campaigns?";
		String param = "";
		if (campaignsParam != null) {
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "startIndex");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "count");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "stateFilter");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "name");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "portfolioIdFilter");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "campaignIdFilter");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "adFormatFilter");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "creativeType");
			url = url + ("".equals(param) ? "" : param);
		}
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
		List<AmzAdvCampaignsHsa> list = new LinkedList<AmzAdvCampaignsHsa>();
		List<AmzAdvCampaignsHsa> insertList = new LinkedList<AmzAdvCampaignsHsa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaignsHsa campaignsHsa = new AmzAdvCampaignsHsa();
				campaignsHsa.setCampaignid(item.getBigInteger("campaignId"));
				campaignsHsa.setName(item.getString("name"));
				campaignsHsa.setBudget(item.getBigDecimal("budget"));
				campaignsHsa.setState(item.getString("state"));
				campaignsHsa.setBudgettype(item.getString("budgetType"));
				campaignsHsa.setServingstatus(item.getString("servingStatus"));
				campaignsHsa.setSpendingpolicy(item.getString("spendingPolicy"));
				campaignsHsa.setBidoptimization(item.getString("bidOptimization"));
				campaignsHsa.setPortfolioid(item.getBigInteger("portfolioId"));
				campaignsHsa.setBidMultiplier(item.getBigDecimal("bidMultiplier"));
				campaignsHsa.setLandingPage(item.getString("landingPage"));
				campaignsHsa.setCreative(item.getString("creative"));
				campaignsHsa.setBidAdjustments(item.getString("bidAdjustments"));
				campaignsHsa.setAdFormat(item.getString("adFormat"));
				try {
					campaignsHsa.setStartdate(fmt.parse(item.getString("startDate")));
					if(item.getString("endDate") != null) {
						campaignsHsa.setEnddate(fmt.parse(item.getString("endDate")));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				campaignsHsa.setProfileid(profile.getId());
				campaignsHsa.setOpttime(new Date());
				insertList.add(campaignsHsa);
				list.add(campaignsHsa);
			}
			if(insertList.size() > 0) {
				amzAdvCampaignsHsaMapper.insertBatch(insertList);
				insertList.clear();
			}
			return list;
			
		}
		return null;
	}
	
	public List<AmzAdvCampaignsHsa> amzCreateDraftsSBCampaigns(UserInfo user,BigInteger  profileId, List<AmzAdvCampaignsHsa> campaignsList) {
		if (campaignsList == null || campaignsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/drafts/campaigns";
		JSONArray array = new JSONArray();
		for (int i = 0; i < campaignsList.size(); i++) {
			AmzAdvCampaignsHsa campaigns = campaignsList.get(i);
			JSONObject param = new JSONObject();
			param.put("name", campaigns.getName());
			param.put("budget", campaigns.getBudget());
			param.put("budgetType", campaigns.getBudgettype());
			param.put("bidOptimization", campaigns.getBidoptimization());
			if(campaigns.getBidMultiplier() != null) {
				param.put("bidMultiplier", campaigns.getBidMultiplier());
			}
			if(campaigns.getPortfolioid() != null) {
				param.put("portfolioId", campaigns.getPortfolioid());
			}
			param.put("startDate", fmt.format(campaigns.getStartdate()));
			if(campaigns.getEnddate() != null) {
				param.put("endDate", fmt.format(campaigns.getEnddate()));
			}
			param.put("brandEntityId", campaigns.getBrandEntityId());
			
			JSONObject creative = new JSONObject();
			creative.put("brandName", campaigns.getBrandName());
			creative.put("brandLogoAssetID", campaigns.getBrandLogoAssetID());
			creative.put("headline", campaigns.getHeadline());
			creative.put("asins", campaigns.getAsins());
			param.put("creative", creative);
			
			JSONObject landingPage = new JSONObject();
			if("store".equals(campaigns.getPageType())) {
				landingPage.put("url", campaigns.getUrl());
			}else {
				landingPage.put("asins", campaigns.getAsins());
			}
			param.put("landingPage", landingPage);
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost_V3(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < campaignsList.size(); i++) {
				AmzAdvCampaignsHsa campaigns = campaignsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger campaignId = item.getBigInteger("draftCampaignId");
					campaigns.setCampaignid(campaignId);
					campaigns.setProfileid(profileId);
					campaigns.setOpttime(new Date());
					AmzAdvCampaignsHsa Dbcampaigns = this.selectByKey(campaigns);
					if(Dbcampaigns == null) {
						this.save(campaigns);
						amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsHsa", user.getId(), profileId, campaigns, null);
					}
				}else {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动创建失败："+errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
			}
			return campaignsList;
		}
		return null;
	}
	
	public AmzAdvCampaignsHsa amzGetDraftsSBCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/drafts/campaigns/" + campaignId;
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvCampaignsHsa amzAdvCampaignsHsa = new AmzAdvCampaignsHsa();
			amzAdvCampaignsHsa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvCampaignsHsa.setName(item.getString("name"));
			amzAdvCampaignsHsa.setBudgettype(item.getString("budgetType"));
			amzAdvCampaignsHsa.setBudget(item.getBigDecimal("budget"));
			amzAdvCampaignsHsa.setState(item.getString("state"));
			amzAdvCampaignsHsa.setServingstatus(item.getString("servingStatus"));
			amzAdvCampaignsHsa.setBidoptimization(item.getString("bidOptimization"));
			amzAdvCampaignsHsa.setBidAdjustments(item.getString("bidAdjustments"));
			amzAdvCampaignsHsa.setSpendingpolicy(item.getString("spendingPolicy"));
			amzAdvCampaignsHsa.setPortfolioid(item.getBigInteger("portfolioId"));
			amzAdvCampaignsHsa.setBidMultiplier(item.getBigDecimal("bidMultiplier"));
			amzAdvCampaignsHsa.setAdFormat(item.getString("adFormat"));
			try {
				if (item.getString("startDate") != null) {
					amzAdvCampaignsHsa.setStartdate(fmt.parse(item.getString("startDate")));
				}
				if (item.getString("endDate") != null) {
					amzAdvCampaignsHsa.setEnddate(fmt.parse(item.getString("endDate")));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			JSONObject creative = item.getJSONObject("creative");
			JSONArray asins = creative.getJSONArray("asins");
			List<String> asinsList = new ArrayList<String>();
			for(int i = 0; i < asins.size(); i++) {
				asinsList.add(asins.getString(i));
			}
			amzAdvCampaignsHsa.setAsins(asinsList);
			amzAdvCampaignsHsa.setBrandName(creative.getString("brandName"));
			amzAdvCampaignsHsa.setBrandLogoAssetID(creative.getString("brandLogoAssetID"));
			amzAdvCampaignsHsa.setHeadline(creative.getString("headline"));
			amzAdvCampaignsHsa.setShouldOptimizeAsins(creative.getBoolean("shouldOptimizeAsins"));
			amzAdvCampaignsHsa.setBrandLogoUrl(creative.getString("brandLogoUrl"));
			
			JSONObject landingPage = item.getJSONObject("landingPage");
			amzAdvCampaignsHsa.setPageType(landingPage.getString("pageType"));
			amzAdvCampaignsHsa.setUrl(landingPage.getString("url"));
			
			amzAdvCampaignsHsa.setProfileid(profileId);
			amzAdvCampaignsHsa.setOpttime(new Date());
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", amzAdvCampaignsHsa.getCampaignid());
			AmzAdvCampaignsHsa oldamzAdvCampaignsHsa = amzAdvCampaignsHsaMapper.selectOneByExample(example);
			if (oldamzAdvCampaignsHsa != null) {
				amzAdvCampaignsHsaMapper.updateByPrimaryKey(amzAdvCampaignsHsa);
			} else {
				amzAdvCampaignsHsaMapper.insert(amzAdvCampaignsHsa);
			}
			return amzAdvCampaignsHsa;
		}
		return null;
	}
	
	public AmzAdvCampaignsHsa amzArchiveDraftsSBCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/drafts/campaigns/" + campaignId;
		String response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaignId);
			AmzAdvCampaignsHsa oldCampaigns = amzAdvCampaignsHsaMapper.selectOneByExample(example);
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example2 = new Example(AmzAdvCampaignsHsa.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvCampaignsHsa campaignHsa = amzAdvCampaignsHsaMapper.selectOneByExample(example2);
				campaignHsa.setState(AdvState.archived);
				campaignHsa.setOpttime(new Date());
				amzAdvCampaignsHsaMapper.updateByPrimaryKey(campaignHsa);
				amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsHsa", user.getId(), profileId, campaignHsa, oldCampaigns);
				return campaignHsa;
			} else {
				String errormsg = "";
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + a.getString("description");
				BaseException exception = new BaseException("广告组修改失败：" + errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
		}
		return null;
	}
	
	public List<AmzAdvCampaignsHsa> amzUpdateDraftsSBCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList) {
		if (campaignsList == null || campaignsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvCampaignsHsa> oldcampaignsList = new ArrayList<AmzAdvCampaignsHsa>();
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/drafts/campaigns";
		JSONArray array = new JSONArray();
		for (int i = 0; i < campaignsList.size(); i++) {
			JSONObject param = new JSONObject();
			// name, budgetType, startDate budget
			AmzAdvCampaignsHsa campaignsHsa = campaignsList.get(i);
			// api 不常改字段
			param.put("campaignId", campaignsHsa.getCampaignid());
			param.put("state", campaignsHsa.getState());
			if (campaignsHsa.getEnddate() != null) {
				param.put("endDate", fmt.format(campaignsHsa.getEnddate()));
			}
			if(campaignsHsa.getName() != null) {
				param.put("name", campaignsHsa.getName());
			}
			if (campaignsHsa.getBudget() != null) {
				param.put("budget", campaignsHsa.getBudget());
			}
			if (campaignsHsa.getBidoptimization() != null) {
				param.put("bidOptimization", campaignsHsa.getBidoptimization());
			}
			if (campaignsHsa.getBidMultiplier() != null) {
				param.put("bidMultiplier", campaignsHsa.getBidMultiplier());
			}
			if(campaignsHsa.getPortfolioid() != null) {
				param.put("portfolioId", campaignsHsa.getPortfolioid());
			}
			array.add(param);

			Example example = new Example(AmzAdvCampaignsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaignsHsa.getCampaignid());
			AmzAdvCampaignsHsa oldCampaigns = amzAdvCampaignsHsaMapper.selectOneByExample(example);
			oldcampaignsList.add(oldCampaigns);
		}
		String response = amzAdvAuthService.amzAdvPut_V3(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for (int i = 0; i < campaignsList.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					AmzAdvCampaignsHsa campaignsHsa = (AmzAdvCampaignsHsa) campaignsList.get(i);
					BigInteger campaignId = item.getBigInteger("draftCampaignId");
					campaignsHsa.setCampaignid(campaignId);
					campaignsHsa.setProfileid(profileId);
					campaignsHsa.setOpttime(new Date());
					amzAdvCampaignsHsaMapper.updateByPrimaryKeySelective(campaignsHsa);

					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					AmzAdvCampaignsHsa oldCampaigns = oldcampaignsList.get(i);
					operateLog.setCampaignid(campaignsHsa.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvCampaignsHsa");
					String campaignsjson = GeneralUtil.toJSON(campaignsHsa);
					String oldCampaignsjson = GeneralUtil.toJSON(oldCampaigns);
					operateLog.setAfterobject(campaignsjson.replaceAll("\\\\", ""));
					operateLog.setBeforeobject(oldCampaignsjson.replaceAll("\\\\", ""));
					operateLogList.add(operateLog);
				} else {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			amzAdvOperateLogService.insertList(operateLogList);
			return campaignsList;
		}
		return null;
	}
	
	public PageList<Map<String, Object>> getCampaignList(Map<String, Object> map, PageBounds pageBounds) {
		return amzAdvCampaignsHsaMapper.getCampaignList(map, pageBounds);
	}

	public List<Map<String, Object>> getCampaignChart(Map<String, Object> map) {
		return amzAdvCampaignsHsaMapper.getCampaignChart(map);
	}

	public List<Map<String, Object>> getCampaignPlacement(Map<String, Object> map) {
		return amzAdvCampaignsHsaMapper.getCampaignPlacement(map);
	}

	public AmzAdvCampaignsHsa selectOneByExample(Example example) {
		return amzAdvCampaignsHsaMapper.selectOneByExample(example);
	}

	public Map<String, Object> getSumCampaigns(Map<String, Object> map) {
		Object paralist = map.get("paralist");
		if(paralist != null) {
			if(paralist != null) {
				map.put("paralist", map.get("paralist").toString().replace("7d", "14d"));
			}
		}
		return amzAdvCampaignsHsaMapper.getSumCampaigns(map);
	}

	@Override
	public Map<String, Object> getCampaignDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result1 = amzAdvCampaignsHsaMapper.getCampaignPlaceDetail(map);
		Map<String, Object> result2 = amzAdvCampaignsHsaMapper.getCampaignBrandDetail(map);
		Map<String, Object> result3 = amzAdvCampaignsHsaMapper.getCampaignVideoDetail(map);
		List<Map<String, Object>> list = amzAdvKeywordsMapper.getKeywordsListForSB(map);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("brand",result2);
		result.put("video",result3);
		result.put("place",result1);
		result.put("list",list);
		return result;
	}

	@Override
	public Date getOldDateSBCampaigns(AmzAdvProfile profile) {
		// TODO Auto-generated method stub
		return amzAdvCampaignsHsaMapper.getOldDateSBCampaigns(profile.getId());
	}

}
