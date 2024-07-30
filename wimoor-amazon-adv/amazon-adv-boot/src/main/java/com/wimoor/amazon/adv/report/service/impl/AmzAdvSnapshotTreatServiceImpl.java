package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.report.dao.AmzAdvRptQueryMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllType;
import com.wimoor.amazon.adv.report.service.IAmzAdvSnapshotTreatService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumAllTypeService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdgroupsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvKeywordsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsHsa;
import com.wimoor.amazon.adv.sd.dao.AmzAdvAdgroupsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductTargeNegativaSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductTargeSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductadsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportAsinsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampKeywordsNegativaMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampaignsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsNegativaMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeNegativaMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductadsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportAdgroupsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportCompaignsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportKeywordsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.common.GeneralUtil;

@Service("amzAdvSnapshotTreatService")
public class AmzAdvSnapshotTreatServiceImpl implements IAmzAdvSnapshotTreatService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProfileMapper amzAdvProfileMapper;
	@Resource
	private IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
	@Resource
	AmzAdvCampaignsMapper amzAdvCampaignsMapper;
	@Resource
	AmzAdvAdgroupsMapper amzAdvAdgroupsMapper;
	@Resource
	AmzAdvReportCompaignsMapper amzAdvReportCompaignsMapper;
	@Resource
	AmzAdvReportAdgroupsMapper amzAdvReportAdgroupsMapper;
	@Resource
	AmzAdvReportKeywordsMapper amzAdvReportKeywordsMapper;
	@Resource
	AmzAdvCampKeywordsNegativaMapper amzAdvCampKeywordsNegativaMapper;
	@Resource
	AmzAdvKeywordsMapper amzAdvKeywordsMapper;
	@Resource
	AmzAdvKeywordsNegativaMapper amzAdvKeywordsNegativaMapper;
	@Resource
	AmzAdvProductadsMapper amzAdvProductadsMapper;
	@Resource
	AmzAdvProductTargeNegativaMapper amzAdvProductTargeNegativaMapper;
	@Resource
	AmzAdvProductTargeMapper amzAdvProductTargeMapper;
	@Resource
	IAmzAdvSumAllTypeService amzAdvSumAllTypeService;
	@Resource
	AmzAdvRptQueryMapper amzAdvRptQueryMapper;
	
	@Resource
	AmzAdvCampaignsSDMapper amzAdvCampaignsSDMapper;
	@Resource
	AmzAdvAdgroupsSDMapper amzAdvAdgroupsSDMapper;
	@Resource
	AmzAdvProductadsSDMapper amzAdvProductadsSDMapper;
	@Resource
	AmzAdvProductTargeSDMapper amzAdvProductTargeSDMapper;
	@Resource
	AmzAdvProductTargeNegativaSDMapper amzAdvProductTargeNegativaSDMapper;
	@Resource
	AmzAdvReportAsinsSDMapper amzAdvReportAsinsSDMapper;
	
	@Resource
	AmzAdvCampaignsHsaMapper amzAdvCampaignsHsaMapper;
    @Resource
    AmzAdvAdgroupsHsaMapper amzAdvAdgroupsHsaMapper;
    @Resource
	AmzAdvKeywordsHsaMapper amzAdvKeywordsHsaMapper;
 
	public synchronized   void treatAdvAdgroups(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvAdgroups> list = new LinkedList<AmzAdvAdgroups>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			AmzAdvAdgroups amzAdvAdgroups = new AmzAdvAdgroups();
			amzAdvAdgroups.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvAdgroups.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvAdgroups.setName(item.getString("name"));
			amzAdvAdgroups.setDefaultbid(item.getBigDecimal("defaultBid"));
			amzAdvAdgroups.setState(item.getString("state"));
			amzAdvAdgroups.setOpttime(new Date());
			amzAdvAdgroups.setProfileid(record.getProfileid());
			list.add(amzAdvAdgroups);
			Integer qty = stateqty.get(amzAdvAdgroups.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvAdgroups.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvAdgroupsMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvAdgroupsMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized   void treatAdvCampaigns(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvCampaigns> list = new LinkedList<AmzAdvCampaigns>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			String campaignType = item.getString("campaignType");
			BigInteger portfolioid = item.getBigInteger("portfolioid");
			BigDecimal dailyBudget = item.getBigDecimal("dailyBudget");
			BigInteger campaignId = item.getBigInteger("campaignId");
			String name = item.getString("name");
			String targetingType = item.getString("targetingType");
			String bidding = item.getString("bidding");
			String state = item.getString("state");
			String premiumBidAdjustment = item.getString("premiumBidAdjustment");
			String startDatestr = item.getString("startDate");
			String endDatestr = item.getString("endDate");
			Date startDate = null;
			Date endDate = null;
			if (startDatestr != null) {
				startDate = GeneralUtil.StringfromDate(startDatestr, "yyyyMMdd");
			}
			if (endDatestr != null) {
				if(endDatestr.equals("19700101")) {
					endDate=null;
				}else {
					endDate = GeneralUtil.StringfromDate(endDatestr, "yyyyMMdd");
				}
			}
			AmzAdvCampaigns amzAdvCampaigns = new AmzAdvCampaigns();
			amzAdvCampaigns.setCampaignid(campaignId);
			amzAdvCampaigns.setPortfolioid(portfolioid);
			amzAdvCampaigns.setCampaignType(campaignType);
			amzAdvCampaigns.setDailybudget(dailyBudget);
			amzAdvCampaigns.setName(name);
			amzAdvCampaigns.setTargetingType(targetingType);
			amzAdvCampaigns.setBidding(bidding);
			amzAdvCampaigns.setState(state);
			amzAdvCampaigns.setPremiumbidadjustment(premiumBidAdjustment);
			amzAdvCampaigns.setStartDate(startDate);
			Calendar c=Calendar.getInstance();
			c.add(Calendar.YEAR, -10);
			if(endDate!=null&&c.getTime().after(endDate)) {
				amzAdvCampaigns.setEndDate(null);
			}else {
				amzAdvCampaigns.setEndDate(endDate);
			 }
			
			amzAdvCampaigns.setProfileid(record.getProfileid());
			amzAdvCampaigns.setOpttime(new Date());
			list.add(amzAdvCampaigns);
			Integer qty = stateqty.get(amzAdvCampaigns.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvCampaigns.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvCampaignsMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvCampaignsMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized   void treatAdvCampKeywordsNegativa(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvCampKeywordsNegativa> list = new LinkedList<AmzAdvCampKeywordsNegativa>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger keywordId = item.getBigInteger("keywordId");
			String keywordText = item.getString("keywordText");
			String matchType = item.getString("matchType");
			String state = item.getString("state");

			AmzAdvCampKeywordsNegativa amzAdvCampKeywords = new AmzAdvCampKeywordsNegativa();
			amzAdvCampKeywords.setCampaignid(campaignId);
			amzAdvCampKeywords.setKeywordid(keywordId);
			amzAdvCampKeywords.setKeywordtext(keywordText);
			amzAdvCampKeywords.setMatchtype(matchType);
			amzAdvCampKeywords.setState(state);
			amzAdvCampKeywords.setProfileid(record.getProfileid());
			amzAdvCampKeywords.setOpttime(new Date());
			list.add(amzAdvCampKeywords);
			Integer qty = stateqty.get(amzAdvCampKeywords.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvCampKeywords.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvCampKeywordsNegativaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvCampKeywordsNegativaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized   void treatAdvKeywords(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvKeywords> list = new LinkedList<AmzAdvKeywords>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger keywordId = item.getBigInteger("keywordId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			String keywordText = item.getString("keywordText");
			String nativeLanguageKeyword = item.getString("nativeLanguageKeyword");
			String matchType = item.getString("matchType");
			String state = item.getString("state");
			BigDecimal bid = item.getBigDecimal("bid");

			AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
			amzAdvKeywords.setCampaignid(campaignId);
			amzAdvKeywords.setKeywordid(keywordId);
			amzAdvKeywords.setAdgroupid(adGroupId);
			amzAdvKeywords.setKeywordtext(keywordText);
			amzAdvKeywords.setMatchtype(matchType);
			amzAdvKeywords.setState(state);
			amzAdvKeywords.setBid(bid);
			amzAdvKeywords.setCampaigntype(record.getCampaigntype());
			amzAdvKeywords.setNativeLanguageKeyword(nativeLanguageKeyword);
			amzAdvKeywords.setProfileid(record.getProfileid());
			amzAdvKeywords.setOpttime(new Date());
			list.add(amzAdvKeywords);
			Integer qty = stateqty.get(amzAdvKeywords.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvKeywords.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvKeywordsMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvKeywordsMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized   void treatAdvKeywordsNegativa(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvKeywordsNegativa> list = new LinkedList<AmzAdvKeywordsNegativa>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger keywordId = item.getBigInteger("keywordId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			String keywordText = item.getString("keywordText");
			String matchType = item.getString("matchType");
			String state = item.getString("state");

			AmzAdvKeywordsNegativa amzAdvNegKeywords = new AmzAdvKeywordsNegativa();
			amzAdvNegKeywords.setCampaignid(campaignId);
			amzAdvNegKeywords.setKeywordid(keywordId);
			amzAdvNegKeywords.setAdgroupid(adGroupId);
			amzAdvNegKeywords.setCampaigntype(record.getCampaigntype());
			amzAdvNegKeywords.setKeywordtext(keywordText);
			amzAdvNegKeywords.setMatchtype(matchType);
			amzAdvNegKeywords.setState(state);
			amzAdvNegKeywords.setProfileid(record.getProfileid());
			amzAdvNegKeywords.setOpttime(new Date());
			list.add(amzAdvNegKeywords);
			Integer qty = stateqty.get(amzAdvNegKeywords.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvNegKeywords.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvKeywordsNegativaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvKeywordsNegativaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized   void treatAdvProductads(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		Map<String, Integer> stateqty2 = new HashMap<String, Integer>();
		List<AmzAdvProductads> list = new LinkedList<AmzAdvProductads>();
		Set<String> asinSet = new HashSet<String>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger adId = item.getBigInteger("adId");
			String sku = item.getString("sku");
			if(sku!=null&&sku.length()>50) {
				sku=sku.substring(0,49);
			}
			String asin = item.getString("asin");
			String state = item.getString("state");
			asinSet.add(asin);
			AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
			amzAdvProductads.setCampaignid(campaignId);
			amzAdvProductads.setAdgroupid(adGroupId);
			amzAdvProductads.setAdid(adId);
			amzAdvProductads.setSku(sku);
			amzAdvProductads.setAsin(asin);
			amzAdvProductads.setState(state);
			amzAdvProductads.setProfileid(record.getProfileid());
			amzAdvProductads.setOpttime(new Date());
			list.add(amzAdvProductads);
			Integer qty = stateqty.get(amzAdvProductads.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductads.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductadsMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductadsMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
		AmzAdvSnapshot record2 = new AmzAdvSnapshot();
		record2.setProfileid(record.getProfileid());
		record2.setCampaigntype(record.getCampaigntype());
		record2.setRecordtype("asin");
		stateqty2.put("enabled", asinSet.size());
		saveAmzAdvSumAllType(stateqty2, record2);
	}



	public synchronized   void treatAdvProductTarget(AmzAdvSnapshot record, JSONArray a) {
		List<AmzAdvProductTarge> list = new LinkedList<AmzAdvProductTarge>();
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger targetId = item.getBigInteger("targetId");
			String expressionType = item.getString("expressionType");
			JSONArray expression = item.getJSONArray("expression");
			String expressionstr = null;
			if (expression != null) {
				expressionstr = expression.toJSONString();
			}
			String state = item.getString("state");
			BigDecimal bid = item.getBigDecimal("bid");

			AmzAdvProductTarge amzAdvProductTarge = new AmzAdvProductTarge();
			amzAdvProductTarge.setCampaignid(campaignId);
			amzAdvProductTarge.setAdgroupid(adGroupId);
			amzAdvProductTarge.setTargetid(targetId);
			amzAdvProductTarge.setExpression(expressionstr);
			amzAdvProductTarge.setExpressiontype(expressionType);
			amzAdvProductTarge.setState(state);
			amzAdvProductTarge.setBid(bid);
			amzAdvProductTarge.setProfileid(record.getProfileid());
			amzAdvProductTarge.setOpttime(new Date());
			list.add(amzAdvProductTarge);
			Integer qty = stateqty.get(amzAdvProductTarge.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductTarge.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductTargeMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductTargeMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized void treatAdvProductTargetNegativa(AmzAdvSnapshot record, JSONArray a) {
		List<AmzAdvProductTargeNegativa> list = new LinkedList<AmzAdvProductTargeNegativa>();
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger targetId = item.getBigInteger("targetId");
			String expressionType = item.getString("expressionType");
			String expression = item.getJSONArray("expression").toJSONString();
			String state = item.getString("state");

			AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativa();
			amzAdvProductTargeNegativa.setCampaignid(campaignId);
			amzAdvProductTargeNegativa.setAdgroupid(adGroupId);
			amzAdvProductTargeNegativa.setTargetid(targetId);
			amzAdvProductTargeNegativa.setExpression(expression);
			amzAdvProductTargeNegativa.setExpressiontype(expressionType);
			amzAdvProductTargeNegativa.setState(state);
			amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
			amzAdvProductTargeNegativa.setOpttime(new Date());
			list.add(amzAdvProductTargeNegativa);
			Integer qty = stateqty.get(amzAdvProductTargeNegativa.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductTargeNegativa.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductTargeNegativaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductTargeNegativaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public void saveAmzAdvSumAllType(Map<String, Integer> stateqty, AmzAdvSnapshot record) {
		for (Entry<String, Integer> entry : stateqty.entrySet()) {
			AmzAdvSumAllType typesum = new AmzAdvSumAllType();
			typesum.setProfileid(record.getProfileid());
			typesum.setCampaigntype(record.getCampaigntype());
			typesum.setRecordtype(record.getRecordtype());
			typesum.setByday(new Date());
			typesum.setOpttime(new Date());
			typesum.setState(entry.getKey());
			typesum.setQuantity(entry.getValue());
			AmzAdvSumAllType old = amzAdvSumAllTypeService.selectByKey(typesum);
			if (old != null) {
				amzAdvSumAllTypeService.updateByKey(typesum);
			} else {
				amzAdvSumAllTypeService.insert(typesum);
			}
		}
		stateqty.clear();
		stateqty = null;
	}

	

	public synchronized   void treatAdvCampaignsSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvCampaignsSD> list = new LinkedList<AmzAdvCampaignsSD>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger portfolioId = item.getBigInteger("portfolioId");
			BigDecimal budget = item.getBigDecimal("budget");
			String name = item.getString("name");
			String budgetType = item.getString("budgetType");
			String costType = item.getString("costType");
			String state = item.getString("state");
			String tactic = item.getString("tactic");
			String startDatestr = item.getString("startDate");
			String endDatestr = item.getString("endDate");
			Date startDate = null;
			Date endDate = null;
			if (startDatestr != null) {
				startDate = GeneralUtil.StringfromDate(startDatestr, "yyyyMMdd");
			}
			if (endDatestr != null) {
				endDate = GeneralUtil.StringfromDate(endDatestr, "yyyyMMdd");
			}
			AmzAdvCampaignsSD amzAdvCampaigns = new AmzAdvCampaignsSD();
			amzAdvCampaigns.setCampaignid(campaignId);
			amzAdvCampaigns.setName(name);
			amzAdvCampaigns.setTactic(tactic); 
			amzAdvCampaigns.setProfileid(portfolioId);
			amzAdvCampaigns.setBudgettype(budgetType);
			amzAdvCampaigns.setBudget(budget);
			amzAdvCampaigns.setStartDate(startDate);
			amzAdvCampaigns.setEndDate(endDate);
			amzAdvCampaigns.setCosttype(costType);
			amzAdvCampaigns.setState(state);
			amzAdvCampaigns.setProfileid(record.getProfileid());
			amzAdvCampaigns.setOpttime(new Date());
			list.add(amzAdvCampaigns);
			Integer qty = stateqty.get(amzAdvCampaigns.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvCampaigns.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvCampaignsSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvCampaignsSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public  synchronized  void treatAdvAdgroupsSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvAdgroupsSD> list = new LinkedList<AmzAdvAdgroupsSD>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			AmzAdvAdgroupsSD amzAdvAdgroups = new AmzAdvAdgroupsSD();
			amzAdvAdgroups.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvAdgroups.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvAdgroups.setName(item.getString("name"));
			amzAdvAdgroups.setDefaultbid(item.getBigDecimal("defaultBid"));
			amzAdvAdgroups.setState(item.getString("state"));
			amzAdvAdgroups.setOpttime(new Date());
			amzAdvAdgroups.setProfileid(record.getProfileid());
			list.add(amzAdvAdgroups);
			Integer qty = stateqty.get(amzAdvAdgroups.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvAdgroups.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvAdgroupsSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvAdgroupsSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized   void treatAdvProductadsSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		Map<String, Integer> stateqty2 = new HashMap<String, Integer>();
		List<AmzAdvProductadsSD> list = new LinkedList<AmzAdvProductadsSD>();
		Set<String> asinSet = new HashSet<String>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger adId = item.getBigInteger("adId");
			String sku = item.getString("sku");
			String asin = item.getString("asin");
			String state = item.getString("state");
			asinSet.add(asin);
			AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
			amzAdvProductads.setCampaignid(campaignId);
			amzAdvProductads.setAdgroupid(adGroupId);
			amzAdvProductads.setAdid(adId);
			amzAdvProductads.setSku(sku);
			amzAdvProductads.setAsin(asin);
			amzAdvProductads.setState(state);
			amzAdvProductads.setProfileid(record.getProfileid());
			amzAdvProductads.setOpttime(new Date());
			list.add(amzAdvProductads);
			Integer qty = stateqty.get(amzAdvProductads.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductads.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductadsSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductadsSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
		AmzAdvSnapshot record2 = new AmzAdvSnapshot();
		record2.setProfileid(record.getProfileid());
		record2.setCampaigntype(record.getCampaigntype());
		record2.setRecordtype("asin");
		stateqty2.put("enabled", asinSet.size());
		saveAmzAdvSumAllType(stateqty2, record2);
	}

	public synchronized  void treatAdvProductTargetSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		List<AmzAdvProductTargeSD> list = new LinkedList<AmzAdvProductTargeSD>();
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger targetId = item.getBigInteger("targetId");
			String expressionType = item.getString("expressionType");
			JSONArray expression = item.getJSONArray("expression");
			String expressionstr = null;
			if (expression != null) {
				expressionstr = expression.toJSONString();
			}
			String state = item.getString("state");
			BigDecimal bid = item.getBigDecimal("bid");

			AmzAdvProductTargeSD amzAdvProductTarge = new AmzAdvProductTargeSD();
			amzAdvProductTarge.setAdgroupid(adGroupId);
			amzAdvProductTarge.setTargetid(targetId);
			amzAdvProductTarge.setExpression(expressionstr);
			amzAdvProductTarge.setExpressiontype(expressionType);
			amzAdvProductTarge.setState(state);
			amzAdvProductTarge.setBid(bid);
			amzAdvProductTarge.setProfileid(record.getProfileid());
			amzAdvProductTarge.setOpttime(new Date());
			list.add(amzAdvProductTarge);
			Integer qty = stateqty.get(amzAdvProductTarge.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductTarge.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductTargeSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductTargeSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized  void treatAdvProductTargetNegativaSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		List<AmzAdvProductTargeNegativaSD> list = new LinkedList<AmzAdvProductTargeNegativaSD>();
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			//BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger targetId = item.getBigInteger("targetId");
			String expressionType = item.getString("expressionType");
			String expression = item.getJSONArray("expression").toJSONString();
			String state = item.getString("state");

			AmzAdvProductTargeNegativaSD amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativaSD();
			amzAdvProductTargeNegativa.setAdgroupid(adGroupId);
			amzAdvProductTargeNegativa.setTargetid(targetId);
			amzAdvProductTargeNegativa.setExpression(expression);
			amzAdvProductTargeNegativa.setExpressiontype(expressionType);
			amzAdvProductTargeNegativa.setState(state);
			amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
			amzAdvProductTargeNegativa.setOpttime(new Date());
			list.add(amzAdvProductTargeNegativa);
			Integer qty = stateqty.get(amzAdvProductTargeNegativa.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductTargeNegativa.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductTargeNegativaSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductTargeNegativaSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	@Override
	public void treatAdvKeywordsHsa(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvKeywordsHsa> list = new LinkedList<AmzAdvKeywordsHsa>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger keywordId = item.getBigInteger("keywordId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			String keywordText = item.getString("keywordText");
			String nativeLanguageKeyword = item.getString("nativeLanguageKeyword");
			String matchType = item.getString("matchType");
			String state = item.getString("state");
			BigDecimal bid = item.getBigDecimal("bid");

			AmzAdvKeywordsHsa amzAdvKeywords = new AmzAdvKeywordsHsa();
			amzAdvKeywords.setCampaignid(campaignId);
			amzAdvKeywords.setKeywordid(keywordId);
			amzAdvKeywords.setAdgroupid(adGroupId);
			amzAdvKeywords.setKeywordtext(keywordText);
			amzAdvKeywords.setMatchtype(matchType);
			amzAdvKeywords.setState(state);
			amzAdvKeywords.setBid(bid);
			amzAdvKeywords.setCampaigntype(record.getCampaigntype());
			amzAdvKeywords.setNativeLanguageKeyword(nativeLanguageKeyword);
			amzAdvKeywords.setProfileid(record.getProfileid());
			amzAdvKeywords.setOpttime(new Date());
			list.add(amzAdvKeywords);
			Integer qty = stateqty.get(amzAdvKeywords.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvKeywords.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvKeywordsHsaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvKeywordsHsaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	@Override
	public void treatAdvAdgroupsHsa(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvAdgroupsHsa> list = new LinkedList<AmzAdvAdgroupsHsa>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			AmzAdvAdgroupsHsa amzAdvAdgroups = new AmzAdvAdgroupsHsa();
			amzAdvAdgroups.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvAdgroups.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvAdgroups.setName(item.getString("name"));
			amzAdvAdgroups.setState(item.getString("state"));
			amzAdvAdgroups.setOpttime(new Date());
			amzAdvAdgroups.setProfileid(record.getProfileid());
			list.add(amzAdvAdgroups);
			Integer qty = stateqty.get(amzAdvAdgroups.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvAdgroups.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvAdgroupsHsaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvAdgroupsHsaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	@Override
	public void treatAdvProductadsHsa(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		 
	}

	@Override
	public void treatAdvKeywordsNegativaHsa(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treatAdvCampKeywordsNegativaHsa(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treatAdvProductTargetHsa(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treatAdvProductTargetNegativaHsa(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		
	}


	
}
