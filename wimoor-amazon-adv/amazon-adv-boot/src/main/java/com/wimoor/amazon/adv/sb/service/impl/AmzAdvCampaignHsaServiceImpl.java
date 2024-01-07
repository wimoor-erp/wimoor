package com.wimoor.amazon.adv.sb.service.impl;

import java.math.BigDecimal;
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
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvKeywordsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsMapper;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
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
	@Resource
	AmzAdvKeywordsHsaMapper amzAdvKeywordsHsaMapper;
	@Resource
	ApiBuildService apiBuildService;
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat fmtSplit = new SimpleDateFormat("yyyy-MM-dd");
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
	  public Map<String,String> getHeaderExt(){
	    	Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.sbcampaignresource.v4+json");
	    	header.put("Accept",       "application/vnd.sbcampaignresource.v4+json");
	    	return header;
	    }
	  
	public List<AmzAdvCampaignsHsa> amzUpdateSBCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList) {
		if (campaignsList == null || campaignsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/v4/campaigns";
		JSONObject dataRaw = new JSONObject();
		JSONArray array = new JSONArray();
		if (campaignsList == null || campaignsList.size() <= 0) {
			throw new BaseException("输入参数有误！");
		}
		for (int i = 0; i < campaignsList.size(); i++) {
			AmzAdvCampaignsHsa campaignsHsa = campaignsList.get(i);
			JSONObject param = new JSONObject();
			param.put("state", campaignsHsa.getState());
			param.put("campaignId", campaignsHsa.getCampaignid().toString());
			param.put("startDate", fmtSplit.format(campaignsHsa.getStartDate()));
			if (campaignsHsa.getBidding() != null) {
				param.put("bidding", GeneralUtil.getJsonObject(campaignsHsa.getBidding()));
			}
			param.put("budgetType", campaignsHsa.getBudgetType());
			if(campaignsHsa.getBrandEntityId()!=null) {
				param.put("brandEntityId", campaignsHsa.getBrandEntityId());
			}
			if(campaignsHsa.getPortfolioid() == null) {
				param.put("portfolioId", null);
			}else {
				param.put("portfolioId", campaignsHsa.getPortfolioid());
			}
			if (campaignsHsa.getEndDate() != null) {
				param.put("endDate", fmtSplit.format(campaignsHsa.getEndDate()));
			}
			if (campaignsHsa.getSmartDefault() != null) {
				param.put("smartDefault", campaignsHsa.getSmartDefault());
			}
			if (campaignsHsa.getGoal() != null) {
				param.put("goal", campaignsHsa.getGoal());
			}
			if(campaignsHsa.getName() != null) {
				param.put("name", campaignsHsa.getName());
			}
			if (campaignsHsa.getBudget() != null) {
				param.put("budget", campaignsHsa.getBudget());
			}
			if (campaignsHsa.getProductLocation() != null) {
				param.put("productLocation", campaignsHsa.getProductLocation());
			}
			if (campaignsHsa.getTags() != null) {
				param.put("tags", campaignsHsa.getTags() );
			}
			if (campaignsHsa.getCostType()!= null) {
				param.put("costType", campaignsHsa.getCostType() );
			} 
			array.add(param);
		}
		dataRaw.put("campaigns",array);
		String response = apiBuildService.amzAdvPut(profile, url, dataRaw.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			    List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
				String errormsg = "";
				JSONObject json = GeneralUtil.getJsonObject(response.toString());
				JSONObject campaignsJson = json.getJSONObject("campaigns");
				JSONArray success = campaignsJson.getJSONArray("success");
				JSONArray error = campaignsJson.getJSONArray("error");
				for(int i=0;i<success.size();i++) {
					JSONObject item=success.getJSONObject(i);
					Integer index = item.getInteger("index");
					AmzAdvCampaignsHsa campaigns = campaignsList.get(index);
			 
					BigInteger campaignId = item.getBigInteger("campaignId");
					campaigns.setCampaignid(campaignId);
					campaigns.setProfileid(profileId);
					campaigns.setOpttime(new Date());
					AmzAdvCampaignsHsa oldCampaigns = amzAdvCampaignsHsaMapper.selectByPrimaryKey(campaigns);
					if(oldCampaigns!=null) {
						amzAdvCampaignsHsaMapper.updateByPrimaryKeySelective(campaigns);
					}else {
						amzAdvCampaignsHsaMapper.insert(campaigns);
					}

					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					operateLog.setCampaignid(campaigns.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvCampaignsHsa");
					String campaignsjson = GeneralUtil.toJSON(campaigns);
					operateLog.setAfterobject(campaignsjson);
					if(oldCampaigns!=null) {
						String oldCampaignsjson = GeneralUtil.toJSON(oldCampaigns);
						operateLog.setBeforeobject(oldCampaignsjson);
					}
					operateLogList.add(operateLog);
				}
				amzAdvOperateLogService.insertList(operateLogList);
				for(int i=0;i<error.size();i++) {
					    JSONObject item=error.getJSONObject(i);
					    errormsg=errormsg+item.toJSONString();
					}
				if(StrUtil.isNotBlank(errormsg)) {
					BaseException exception = new BaseException(errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
			return campaignsList;
		}
		return null;
	} 
	
	public List<AmzAdvCampaignsHsa> amzCreateSBCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList) {
		if (campaignsList == null || campaignsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/v4/campaigns";
		JSONObject dataRaw = new JSONObject();
		JSONArray array = new JSONArray();
		if (campaignsList == null || campaignsList.size() <= 0)
			throw new BaseException("输入参数有误！");
		for (int i = 0; i < campaignsList.size(); i++) {
			AmzAdvCampaignsHsa campaignsHsa = campaignsList.get(i);
			JSONObject param = new JSONObject();
			param.put("state", campaignsHsa.getState());
			param.put("startDate", fmtSplit.format(campaignsHsa.getStartDate()));
			if (campaignsHsa.getBidding() != null) {
				param.put("bidding", GeneralUtil.getJsonObject(campaignsHsa.getBidding()));
			}
			param.put("budgetType", campaignsHsa.getBudgetType());
			if(campaignsHsa.getBrandEntityId()!=null) {
				param.put("brandEntityId", campaignsHsa.getBrandEntityId());
			}
			if(campaignsHsa.getPortfolioid() == null) {
				param.put("portfolioId", null);
			}else {
				param.put("portfolioId", campaignsHsa.getPortfolioid());
			}
			if (campaignsHsa.getEndDate() != null) {
				param.put("endDate", fmtSplit.format(campaignsHsa.getEndDate()));
			}
			if (StrUtil.isNotBlank(campaignsHsa.getSmartDefault()) ) {
				param.put("smartDefault", GeneralUtil.getJsonArray(campaignsHsa.getSmartDefault()));
			}else {
				param.put("smartDefault",null);
			}
			if (campaignsHsa.getGoal() != null) {
				param.put("goal", campaignsHsa.getGoal());
			}
			if(campaignsHsa.getName() != null) {
				param.put("name", campaignsHsa.getName());
			}
			if (campaignsHsa.getBudget() != null) {
				param.put("budget", campaignsHsa.getBudget());
			}
			if (campaignsHsa.getProductLocation() != null) {
				param.put("productLocation", campaignsHsa.getProductLocation());
			}
			if (campaignsHsa.getTags() != null) {
				param.put("tags", campaignsHsa.getTags() );
			}
			if (campaignsHsa.getCostType()!= null) {
				param.put("costType", campaignsHsa.getCostType() );
			} 
			array.add(param);
		}
		dataRaw.put("campaigns",array);
		String response = null;
		try {
			  response = apiBuildService.amzAdvPost(profile, url,  dataRaw.toString(),this.getHeaderExt());
		}catch(Exception e) {
			e.printStackTrace();
			throw new BizException("API调用错误"+e.getMessage());
		}
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject campaignsJson = json.getJSONObject("campaigns");
			JSONArray success = campaignsJson.getJSONArray("success");
			JSONArray error = campaignsJson.getJSONArray("error");
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvCampaignsHsa campaigns = campaignsList.get(index);
				BigInteger campaignId = item.getBigInteger("campaignId");
				campaigns.setCampaignid(campaignId);
				campaigns.setProfileid(profileId);
				campaigns.setOpttime(new Date());
				AmzAdvCampaignsHsa Dbcampaigns = this.selectByKey(campaigns);
				if (Dbcampaigns == null) {
					this.save(campaigns);
					amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsHsa", user.getId(), profileId, campaigns, null);
				}
			}
			for(int i=0;i<error.size();i++) {
				    JSONObject item=error.getJSONObject(i);
				    errormsg=errormsg+item.toJSONString();
				}
			if(StrUtil.isNotBlank(errormsg)) {
				BaseException exception = new BaseException(errormsg);
				exception.setCode("ERROR");
				throw exception;
			}
			return campaignsList;
		}
		return null;
	}
	
	public List<AmzAdvCampaignsHsa> amzUpdateSBCampaigns(UserInfo user, BigInteger profileId, JSONArray campaigns) {
		
		 return this.amzUpdateSBCampaigns(user, profileId, this.convertJsonToBean(user, profileId, campaigns));
	}
	
	@Override
	public List<AmzAdvCampaignsHsa> amzCreateSBCampaigns(UserInfo user, BigInteger profileId, JSONArray campaigns) {
		// TODO Auto-generated method stub
		 return this.amzCreateSBCampaigns(user, profileId, this.convertJsonToBean(user, profileId, campaigns));
	}
	

public List<AmzAdvCampaignsHsa> amzDeleteSBCampaigns(AmzAdvProfile profile, Map<String, Object> campaignsParam) {
	if (campaignsParam == null) {
		return null;
	}
	String url = "/sb/v4/campaigns/delete";
	JSONObject param = new JSONObject();
	String response = apiBuildService.amzAdvPost(profile, url, param.toString());
	List<AmzAdvCampaignsHsa> list = new LinkedList<AmzAdvCampaignsHsa>();
	if (StringUtil.isNotEmpty(response)) {
		  JSONObject result = GeneralUtil.getJsonObject(response.toString());
		  JSONArray a = result.getJSONArray("campaigns");
		  if(result.containsKey("nextToken")) {
			  campaignsParam.put("newNextToken", result.getString("nextToken"));
		  }
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			AmzAdvCampaignsHsa campaignsHsa = new AmzAdvCampaignsHsa();
			campaignsHsa.setCampaignid(item.getBigInteger("campaignId"));
			campaignsHsa.setName(item.getString("name"));
			campaignsHsa.setBudget(item.getBigDecimal("budget"));
			campaignsHsa.setState(item.getString("state"));
			if(item.containsKey("portfolioId")) {
				campaignsHsa.setPortfolioid(item.getBigInteger("portfolioId"));
			}
			try {
				if(item.containsKey("startDate")&&item.getString("startDate") != null) {
				campaignsHsa.setStartDate(fmtSplit.parse(item.getString("startDate")));
				}
				if(item.containsKey("endDate")&&item.getString("endDate") != null) {
					campaignsHsa.setEndDate(fmtSplit.parse(item.getString("endDate")));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			campaignsHsa.setBudgetType(item.getString("budgetType"));
			campaignsHsa.setBrandEntityId(item.getString("brandEntityId"));
		    campaignsHsa.setBidding(item.getString("bidding"));
			campaignsHsa.setTags(item.getString("tags"));
			campaignsHsa.setCostType(item.getString("costType"));
			campaignsHsa.setSmartDefault(item.getString("smartDefault"));
			if(item.containsKey("extendedData")) {
				JSONObject extendedData = item.getJSONObject("extendedData");
				campaignsHsa.setServingStatus(extendedData.getString("servingStatus"));
			}else {
				campaignsHsa.setServingStatus(item.getString("servingStatus"));
			}
			campaignsHsa.setProfileid(profile.getId());
			campaignsHsa.setOpttime(new Date());
			list.add(campaignsHsa);
		}
		if(list.size() > 0) {
			insertBatch(list);
		}
		return list;
		
	}
	return null;
}
	public List<AmzAdvCampaignsHsa> amzListSBCampaigns(AmzAdvProfile profile, JSONObject campaignsParam) {
		if (campaignsParam == null) {
			return null;
		}
		String url = "/sb/v4/campaigns/list";
		String response = apiBuildService.amzAdvPost(profile, url, campaignsParam.toString(),this.getHeaderExt());
		List<AmzAdvCampaignsHsa> list = new LinkedList<AmzAdvCampaignsHsa>();
		if (StringUtil.isNotEmpty(response)) {
			  JSONObject result = GeneralUtil.getJsonObject(response.toString());
			  JSONArray a = result.getJSONArray("campaigns");
			  if(result.containsKey("nextToken")) {
				  campaignsParam.put("nextToken", result.getString("nextToken"));
			  }else {
				  campaignsParam.put("nextToken", null);
			  }
			 for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaignsHsa campaignsHsa = new AmzAdvCampaignsHsa();
				campaignsHsa.setCampaignid(item.getBigInteger("campaignId"));
				campaignsHsa.setName(item.getString("name"));
				campaignsHsa.setBudget(item.getBigDecimal("budget"));
				campaignsHsa.setState(item.getString("state"));
				if(item.containsKey("portfolioId")) {
					campaignsHsa.setPortfolioid(item.getBigInteger("portfolioId"));
				}
				try {
					if(item.containsKey("startDate")&&item.getString("startDate") != null) {
					campaignsHsa.setStartDate(fmtSplit.parse(item.getString("startDate")));
					}
					if(item.containsKey("endDate")&&item.getString("endDate") != null) {
						campaignsHsa.setEndDate(fmtSplit.parse(item.getString("endDate")));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				campaignsHsa.setBudgetType(item.getString("budgetType"));
				campaignsHsa.setBrandEntityId(item.getString("brandEntityId"));
				campaignsHsa.setBidding(item.getString("bidding"));
				campaignsHsa.setTags(item.getString("tags"));
				campaignsHsa.setCostType(item.getString("costType"));
				campaignsHsa.setGoal(item.getString("goal"));
				campaignsHsa.setSmartDefault(item.getString("smartDefault"));
				if(item.containsKey("extendedData")) {
					JSONObject extendedData = item.getJSONObject("extendedData");
					campaignsHsa.setServingStatus(extendedData.getString("servingStatus"));
				}else {
					campaignsHsa.setServingStatus(item.getString("servingStatus"));
				}
				campaignsHsa.setProfileid(profile.getId());
				campaignsHsa.setOpttime(new Date());
				list.add(campaignsHsa);
			}
			if(list.size() > 0) {
				insertBatch(list);
			}
			return list;
		}else {
			  campaignsParam.put("nextToken", null);
		  }
		return null;
	}
	public synchronized void insertBatch(List<AmzAdvCampaignsHsa> insertList) {
		amzAdvCampaignsHsaMapper.insertBatch(insertList);
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
		List<Map<String, Object>> list = amzAdvKeywordsHsaMapper.getKeywordsList(map);
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

	@Override
	public PageList<AmzAdvCampaignsHsa> getHsaCampaignsNotArchivedByprofile(QueryForList query){
		BigInteger profileid=new BigInteger(query.getProfileid());
		String name=query.getName();
		PageBounds pageBounds=query.getPageBounds();
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("profileid", profileid);
		if(StrUtil.isNotEmpty(name)) {
			params.put("name", "%"+name+"%");
		}else {
			params.put("name", null);
		}
		if(StrUtil.isNotEmpty(query.getCampaignStatus())&&!query.getCampaignStatus().equals("all")) {
			params.put("campaignStatus", query.getCampaignStatus());
		}else {
			params.put("campaignStatus", null);
		}
		PageList<AmzAdvCampaignsHsa> list = amzAdvCampaignsHsaMapper.getCampaignsNotArchived(params, pageBounds);
		return list;
	}
	
	@Override
	public Map<String,Object> getSBCampaignVideo(String campaignId,String bydate){
		Date date = GeneralUtil.getDatez(bydate);
		String dateStr = GeneralUtil.getStrDate(date);
		Map<String,Object> result=amzAdvCampaignsHsaMapper.getSBCampaignVideo(campaignId,dateStr);
		return result;
	}

	public List<AmzAdvCampaignsHsa> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray campaignsArray){
		if (campaignsArray == null || campaignsArray.size() <= 0)
			return null;
		List<AmzAdvCampaignsHsa> campaignsList= new ArrayList<AmzAdvCampaignsHsa>();
		for (int i = 0; i < campaignsArray.size(); i++) {
			JSONObject campaign = campaignsArray.getJSONObject(i);
			BigInteger portfolioid = campaign.getBigInteger("portfolioid");
			String budgetType = campaign.getString("budgetType");
			String tags = campaign.getString("tags");
			String smartDefault = campaign.getString("smartDefault");
			String brandEntityId = campaign.getString("brandEntityId");
			String goal = campaign.getString("goal");
			String name = campaign.getString("name");
			String state = campaign.getString("state");
			Date startDate = campaign.getDate("startDate");
			Date endDate = campaign.getDate("endDate");
			BigInteger campaignid = campaign.getBigInteger("campaignid");
			String budget=campaign.getString("budget");
			String productLocation=campaign.getString("productLocation");
			String costType=campaign.getString("costType");
			String bidding=campaign.getString("bidding");
			
			AmzAdvCampaignsHsa amzAdvCampaignsHsa = new AmzAdvCampaignsHsa();
			amzAdvCampaignsHsa.setBidding(bidding);
			amzAdvCampaignsHsa.setBudgetType(budgetType);
			amzAdvCampaignsHsa.setBrandEntityId(brandEntityId);
			amzAdvCampaignsHsa.setSmartDefault(smartDefault);
			amzAdvCampaignsHsa.setGoal(goal);
			amzAdvCampaignsHsa.setProductLocation(productLocation);
			amzAdvCampaignsHsa.setTags(tags) ;
		    amzAdvCampaignsHsa.setCostType(costType);
			amzAdvCampaignsHsa.setCampaignid(campaignid);
			amzAdvCampaignsHsa.setPortfolioid(portfolioid);
			amzAdvCampaignsHsa.setName(name);
			amzAdvCampaignsHsa.setState(state);
			amzAdvCampaignsHsa.setOpttime(new Date());
			amzAdvCampaignsHsa.setProfileid(profileId);
			amzAdvCampaignsHsa.setStartDate(startDate);
			amzAdvCampaignsHsa.setEndDate(endDate);
			amzAdvCampaignsHsa.setBudget(new BigDecimal(budget));
			campaignsList.add(amzAdvCampaignsHsa);
		}
		return campaignsList;
	    }
	
	
	
	@Override
	public String amzGetCampaignBudgetUsage(UserInfo user, BigInteger profileid, JSONObject param) {
		// TODO Auto-generated method stub
		String url=  "/sb/campaigns/budget/usage";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
		Map<String, String> header=new HashMap<String,String>();
		header.put("Content-Type", "application/vnd.sbcampaignbudgetusage.v1+json");
    	header.put("Accept",       "application/vnd.sbcampaignbudgetusage.v1+json");
		String response = apiBuildService.amzAdvPost(profile, url,param.toJSONString(),header);
		return response;
	}



}
