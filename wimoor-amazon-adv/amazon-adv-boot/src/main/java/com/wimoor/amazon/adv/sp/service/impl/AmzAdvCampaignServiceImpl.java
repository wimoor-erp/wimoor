package com.wimoor.amazon.adv.sp.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCampaignsSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampaignsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;

import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvCampaignService")
public class AmzAdvCampaignServiceImpl extends BaseService<AmzAdvCampaigns> implements IAmzAdvCampaignService {
	@Resource
	AmzAdvCampaignsMapper amzAdvCampaignsMapper;
	@Resource
	@Lazy
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	@Resource
	@Lazy
	IAmzAdvCampaignsSDService amzAdvCampaignsSDService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
 

	public static final class CampaignType {
		public static final String sponsoredProducts = "sponsoredProducts";
		public static final String sp = "sp";
		public static final String hsa = "hsa";
		public static final String sb = "sb";
		public static final String sd = "sd";
		public static final String sponsoredBrands = "sponsoredBrands";
	}

	public static class CampaignTargetingType {
		public static final String manual = "manual";
		public static final String auto = "auto";
	}

	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

	public AmzAdvCampaigns selectOneByExample(Example example) {
		return amzAdvCampaignsMapper.selectOneByExample(example);
	}

	public List<AmzAdvCampaigns> getManualSpCampaignsByprofile(BigInteger profileid) {
		if (profileid != null) {
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			crit.andEqualTo("targetingtype", "manual");
			crit.andNotEqualTo("state", "archived");
			example.setOrderByClause("name asc");
			List<AmzAdvCampaigns> list = amzAdvCampaignsMapper.selectByExample(example);
			return list;
		} else {
			throw new BaseException("请选择店铺与站点！");
		}
	}

	public List<AmzAdvCampaigns> getSpCampaignsByprofile(BigInteger profileid) {
		if (profileid != null) {
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			example.setOrderByClause("name asc");
			List<AmzAdvCampaigns> list = amzAdvCampaignsMapper.selectByExample(example);
			return list;
		} else {
			throw new BaseException("请选择店铺与站点！");
		}
	}

	public List<AmzAdvCampaigns> getSpCampaignsNotArchivedByprofile(BigInteger profileid,String name) {
		if (profileid != null) {
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			crit.andNotEqualTo("state", "archived");
			if(StrUtil.isNotEmpty(name)) {
				crit.andLike("name", "%"+name+"%");
			}
			example.setOrderByClause("name asc");
			List<AmzAdvCampaigns> list = amzAdvCampaignsMapper.selectByExample(example);
			return list;
		} else {
			throw new BaseException("请选择店铺与站点！");
		}
	}

	public AmzAdvCampaigns amzGetSpCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + CampaignType.sp + "/campaigns/" + campaignId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvCampaigns campaigns = new AmzAdvCampaigns();
			campaigns.setCampaignid(item.getBigInteger("campaignId"));
			campaigns.setName(item.getString("name"));
			campaigns.setPortfolioid(item.getBigInteger("portfolioId"));
			campaigns.setTargetingtype(item.getString("targetingType"));
			campaigns.setState(item.getString("state"));
			campaigns.setDailybudget(item.getBigDecimal("dailyBudget"));
			campaigns.setPremiumbidadjustment(item.getString("premiumBidAdjustment"));
			campaigns.setCampaigntype(item.getString("campaignType"));
			campaigns.setBidding(item.getString("bidding"));
			try {
				campaigns.setStartdate(fmt.parse(item.getString("startDate")));
				if (item.getString("endDate") != null) {
					campaigns.setEnddate(fmt.parse(item.getString("endDate")));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			campaigns.setProfileid(profileId);
			campaigns.setOpttime(new Date());
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaigns.getCampaignid());
			AmzAdvCampaigns oldcampaigns = amzAdvCampaignsMapper.selectOneByExample(example);
			if (oldcampaigns != null) {
				this.updateAll(campaigns);
			} else {
				this.save(campaigns);
			}
			return campaigns;
		}
		return null;
	}

	public AmzAdvCampaigns amzGetCampaignEx(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaigns/extended/" + campaignId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvCampaigns campaigns = new AmzAdvCampaigns();
			campaigns.setCampaignid(item.getBigInteger("campaignId"));
			campaigns.setName(item.getString("name"));
			campaigns.setCampaigntype(item.getString("campaignType"));
			campaigns.setTargetingtype(item.getString("targetingType"));
			campaigns.setPremiumbidadjustment(item.getString("premiumBidAdjustment"));
			campaigns.setDailybudget(item.getBigDecimal("dailyBudget"));
			campaigns.setState(item.getString("state"));
			campaigns.setPortfolioid(item.getBigInteger("portfolioId"));
			campaigns.setBidding(item.getString("bidding"));
			try {
				if (item.getString("startDate") != null) {
					campaigns.setStartdate(fmt.parse(item.getString("startDate")));
				}
				if (item.getString("endDate") != null) {
					campaigns.setEnddate(fmt.parse(item.getString("endDate")));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			campaigns.setPlacement(item.getString("placement"));
			campaigns.setCreationDate(item.getDate("creationDate"));
			campaigns.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			campaigns.setServingStatus(item.getString("servingStatus"));
			campaigns.setProfileid(profileId);
			campaigns.setOpttime(new Date());
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaigns.getCampaignid());
			AmzAdvCampaigns oldcampaigns = amzAdvCampaignsMapper.selectOneByExample(example);
			if (oldcampaigns != null) {
				this.updateAll(campaigns);
			} else {
				this.save(campaigns);
			}
			return campaigns;
		}
		return null;
	}

	public List<AmzAdvCampaigns> amzCreateCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaigns> campaignsList) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaigns";
		JSONArray array = new JSONArray();
		if (campaignsList == null || campaignsList.size() <= 0)
			throw new BaseException("输入参数有误！");
		for (int i = 0; i < campaignsList.size(); i++) {
			AmzAdvCampaigns campaigns = campaignsList.get(i);
			JSONObject param = new JSONObject();
			param.put("name", campaigns.getName());
			param.put("targetingType", campaigns.getTargetingtype());
			param.put("campaignType", campaigns.getCampaigntype());
			param.put("state", campaigns.getState());
			param.put("dailyBudget", campaigns.getDailybudget());
			param.put("startDate", fmt.format(campaigns.getStartdate()));
			if (campaigns.getPortfolioid() != null) {
				param.put("portfolioId", campaigns.getPortfolioid());
			}
			if (campaigns.getEnddate() != null) {
				param.put("endDate", fmt.format(campaigns.getEnddate()));
			}
			if (campaigns.getBidding() != null) {
				if (!"vendor".equals(profile.getType())) {
					param.put("bidding", GeneralUtil.getJsonObject(campaigns.getBidding()));
				}
			}
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < campaignsList.size(); i++) {
				AmzAdvCampaigns campaigns = campaignsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger campaignId = item.getBigInteger("campaignId");
					campaigns.setCampaignid(campaignId);
					campaigns.setProfileid(profileId);
					campaigns.setOpttime(new Date());
					AmzAdvCampaigns Dbcampaigns = this.selectByKey(campaigns);
					if (Dbcampaigns == null) {
						this.save(campaigns);
						amzAdvOperateLogService.saveOperateLog("AmzAdvCampaigns", user.getId(), profileId, campaigns, null);
					}
				} else {
					String name = campaigns.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动：'" + name + "' 创建失败：" + errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
			}
			return campaignsList;
		}
		return null;
	}

	public List<AmzAdvCampaigns> amzUpdateSpCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaigns> campaignsList) {
		if (campaignsList == null || campaignsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvCampaigns> oldcampaignsList = new ArrayList<AmzAdvCampaigns>();
		String url = "/" + CampaignType.sp + "/campaigns";
		JSONArray array = new JSONArray();
		for (int i = 0; i < campaignsList.size(); i++) {
			JSONObject param = new JSONObject();
			// name, state, dailyBudget, startDate, premiumBidAdjustment
			AmzAdvCampaigns campaigns = campaignsList.get(i);
			// api 不常改字段
			param.put("campaignId", campaigns.getCampaignid());
			param.put("targetingType", campaigns.getTargetingtype());
			param.put("campaignType", campaigns.getCampaigntype());
			param.put("name", campaigns.getName());
			param.put("state", campaigns.getState());
			
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaigns.getCampaignid());
			AmzAdvCampaigns oldCampaigns = amzAdvCampaignsMapper.selectOneByExample(example);
			
			if (campaigns.getPortfolioid() == null) {
				param.put("portfolioId", null);
			} else {
				param.put("portfolioId", campaigns.getPortfolioid());
			}
			if (campaigns.getDailybudget() != null) {
				param.put("dailyBudget", campaigns.getDailybudget());
			}
			if (campaigns.getStartdate() != null&&campaigns.getState().equals(oldCampaigns.getState())) {
				param.put("startDate", fmt.format(campaigns.getStartdate()));
			}
			if (campaigns.getEnddate() != null) {
				param.put("endDate", fmt.format(campaigns.getEnddate()));
			}
			if (campaigns.getBidding() != null) {
				if (!"vendor".equals(profile.getType())) {
					param.put("bidding", GeneralUtil.getJsonObject(campaigns.getBidding()));
				}
			}
			array.add(param);


			oldcampaignsList.add(oldCampaigns);
		}
		String response = amzAdvAuthService.amzAdvPut(profile, url, JSONArray.toJSONString(array, SerializerFeature.WriteMapNullValue));
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for (int i = 0; i < campaignsList.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaigns campaigns = campaignsList.get(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger campaignId = item.getBigInteger("campaignId");
					campaigns.setCampaignid(campaignId);
					campaigns.setProfileid(profileId);
					campaigns.setOpttime(new Date());
					amzAdvCampaignsMapper.updateByPrimaryKeySelective(campaigns);

					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					AmzAdvCampaigns oldCampaigns = oldcampaignsList.get(i);
					operateLog.setCampaignid(campaigns.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvCampaigns");
					String campaignsjson = GeneralUtil.toJSON(campaigns);
					String oldCampaignsjson = GeneralUtil.toJSON(oldCampaigns);
					operateLog.setAfterobject(campaignsjson);
					operateLog.setBeforeobject(oldCampaignsjson);
					operateLogList.add(operateLog);
				} else {
					String name = campaigns.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动：'" + name + "' 修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			amzAdvOperateLogService.insertList(operateLogList);
			return campaignsList;
		}
		return null;
	}

	public AmzAdvCampaigns amzArchiveSpCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + CampaignType.sp + "/campaigns/" + campaignId;
		String response = amzAdvAuthService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaignId);
			AmzAdvCampaigns oldCampaign = amzAdvCampaignsMapper.selectOneByExample(example);
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example2 = new Example(AmzAdvCampaigns.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvCampaigns campaign = amzAdvCampaignsMapper.selectOneByExample(example2);
				campaign.setState(AdvState.archived);
				campaign.setOpttime(new Date());
				amzAdvCampaignsMapper.updateByPrimaryKey(campaign);
				amzAdvOperateLogService.saveOperateLog("AmzAdvCampaigns", user.getId(), profileId, campaign, oldCampaign);
				return campaign;
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

	public List<AmzAdvCampaigns> amzListSpCampaigns(UserInfo user, BigInteger profileId, Map<String, Object> campaignsParam) {
		if (campaignsParam == null)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + CampaignType.sp + "/campaigns?";
		String param = "";
		if (campaignsParam != null) {
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "startIndex");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "count");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "stateFilter");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "name");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "campaignIdFilter");
			param = GeneralUtil.addParamToUrl(param, campaignsParam, "portfolioIdFilter");
			url = url + ("".equals(param) ? "" : param);
		}
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvCampaigns> list = new LinkedList<AmzAdvCampaigns>();
		List<AmzAdvCampaigns> insertList = new LinkedList<AmzAdvCampaigns>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaigns campaigns = new AmzAdvCampaigns();
				campaigns.setCampaignid(item.getBigInteger("campaignId"));
				campaigns.setName(item.getString("name"));
				campaigns.setDailybudget(item.getBigDecimal("dailyBudget"));
				campaigns.setState(item.getString("state"));
				campaigns.setCampaigntype(item.getString("campaignType"));
				campaigns.setTargetingtype(item.getString("targetingType"));
				campaigns.setPremiumbidadjustment(item.getString("premiumBidAdjustment"));
				campaigns.setPortfolioid(item.getBigInteger("portfolioId"));
				campaigns.setBidding(item.getString("bidding"));
				try {
					if (item.getString("endDate") != null) {
						//campaigns.setEnddate(fmt.parse(item.getString("endDate")));
						Calendar c=Calendar.getInstance();
						c.add(Calendar.YEAR, -10);
						Date dates = fmt.parse(item.getString("endDate"));
						if(dates!=null&&c.getTime().after(dates)) {
							campaigns.setEnddate(null);
						}else {
							campaigns.setEnddate(dates);
						 }
					}
					if (item.getString("startDate") != null) {
						campaigns.setStartdate(fmt.parse(item.getString("startDate")));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				campaigns.setProfileid(profileId);
				campaigns.setOpttime(new Date());
				list.add(campaigns);
				insertList.add(campaigns);
				if (insertList.size() > 200) {
					amzAdvCampaignsMapper.insertBatch(insertList);
					insertList.clear();
				}
			}
			if (insertList.size() > 0) {
				amzAdvCampaignsMapper.insertBatch(insertList);
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvCampaigns> amzListCampaignsEx(UserInfo user, BigInteger profileId, Map<String, Object> campaignsParam) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaigns/extended?";
		String param = "";
		if (campaignsParam != null) {
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "startIndex");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "count");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "stateFilter");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "name");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "campaignIdFilter");
		}
		url = url + ("".equals(param) ? "" : param);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvCampaigns> list = new LinkedList<AmzAdvCampaigns>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaigns campaigns = new AmzAdvCampaigns();
				campaigns.setCampaignid(item.getBigInteger("campaignId"));
				campaigns.setName(item.getString("name"));
				campaigns.setCampaigntype(item.getString("campaignType"));
				campaigns.setTargetingtype(item.getString("targetingType"));
				campaigns.setPremiumbidadjustment(item.getString("premiumBidAdjustment"));
				campaigns.setDailybudget(item.getBigDecimal("dailyBudget"));
				campaigns.setState(item.getString("state"));
				campaigns.setPortfolioid(item.getBigInteger("portfolioId"));
				campaigns.setBidding(item.getString("bidding"));
				try {
					if (item.getString("endDate") != null) {
						campaigns.setEnddate(fmt.parse(item.getString("endDate")));
					}
					if (item.getString("startDate") != null) {
						campaigns.setStartdate(fmt.parse(item.getString("startDate")));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				campaigns.setProfileid(profileId);
				campaigns.setPlacement(item.getString("placement"));
				campaigns.setServingStatus(item.getString("servingStatus"));
				campaigns.setCreationDate(item.getDate("creationDate"));
				campaigns.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				campaigns.setOpttime(new Date());
				Example example = new Example(AmzAdvCampaigns.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("campaignid", campaigns.getCampaignid());
				AmzAdvCampaigns oldcampaigns = amzAdvCampaignsMapper.selectOneByExample(example);
				if (oldcampaigns != null) {
					campaigns.setBidding(oldcampaigns.getBidding());
					this.updateAll(campaigns);
				} else {
					this.save(campaigns);
				}
				list.add(campaigns);
			}
			return list;
		}
		return null;
	}

	public PageList<Map<String, Object>> getCampaignList(Map<String, Object> map, PageBounds pageBounds) {
		String campaignType = (String) map.get("campaignType");
		PageList<Map<String, Object>> list = null;
		if ("SP".equals(campaignType)) {
			list = amzAdvCampaignsMapper.getCampaignList(map, pageBounds);
		} else if ("HSA".equals(campaignType) || "SB".equals(campaignType)) {
			list = amzAdvCampaignHsaService.getCampaignList(map, pageBounds);
		} else if ("all".equals(campaignType)) {
			list = amzAdvCampaignsMapper.getAllCampaignList(map, pageBounds);
		} else if ("SD".equals(campaignType)) {
			list = amzAdvCampaignsSDService.getCampaignList(map, pageBounds);
		}
		return list;
	}

	public Map<String, Object> getCampaignChart(Map<String, Object> map) {
		String campaignType = (String) map.get("campaignType");
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> listHsa = null;
		List<Map<String, Object>> listSD = null;
		getSerchStr(map);
		if(map.get("serchlist")==null) {
			throw new BizException("数据列未找到！");
		}
		if ("SP".equals(campaignType)) {
			list = amzAdvCampaignsMapper.getCampaignChart(map);
		} else if ("HSA".equals(campaignType) || "SB".equals(campaignType)) {
			listHsa = amzAdvCampaignHsaService.getCampaignChart(map);
		} else if ("all".equals(campaignType)) {
			list = amzAdvCampaignsMapper.getCampaignChart(map);
			listHsa = amzAdvCampaignHsaService.getCampaignChart(map);
			listSD = amzAdvCampaignsSDService.getCampaignChart(map);
		}else if ("SD".equals(campaignType)) {
			listSD = amzAdvCampaignsSDService.getCampaignChart(map);
		}
		return getChartData(list, listHsa, listSD,map);
	}

	public Map<String, Object> getChartData(List<Map<String, Object>> list, List<Map<String, Object>> listHsa,List<Map<String, Object>> listSD, Map<String, Object> map) {
		if (list == null && listHsa == null&& listSD == null) {
			return null;
		}
		String serch1 = (String) map.get("value1");
	
		Calendar c = Calendar.getInstance();
		Calendar cTime = Calendar.getInstance();
		String fromDate = (String) map.get("fromDate");
		String endDate = (String) map.get("endDate");
		if(fromDate!=null&&fromDate.contains("-")) {
			cTime.setTime(GeneralUtil.StringfromDate(fromDate, "yyyy-MM-dd"));
		}else {
			cTime.setTime(GeneralUtil.StringfromDate(fromDate, "yyyy/MM/dd"));
		}
		Date beginDateplus = null;
		beginDateplus = cTime.getTime();
		if(endDate!=null&&endDate.contains("-")) {
			cTime.setTime(GeneralUtil.StringfromDate(endDate, "yyyy-MM-dd"));
		}else {
			cTime.setTime(GeneralUtil.StringfromDate(endDate, "yyyy/MM/dd"));
		}
		Date endDateplus = null;
		endDateplus = cTime.getTime();
		List<String> listLabel = new ArrayList<String>();
		List<Object> listData1 = new ArrayList<Object>();
	 
		Map<String, Object> mapSp = new HashMap<String, Object>();
		Map<String, Object> mapHsa = new HashMap<String, Object>();
		Map<String, Object> mapSD = new HashMap<String, Object>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				String bydate = list.get(i).get("bydate") == null ? null : list.get(i).get("bydate").toString();
				String value = list.get(i).get(serch1) == null ? null : list.get(i).get(serch1).toString();
				 
				if (value == null) {
					mapSp.put(bydate, null);
				} else {
					mapSp.put(bydate, value );
				}
			}
		}
		if (listHsa != null) {
			for (int i = 0; i < listHsa.size(); i++) {
				String bydate = listHsa.get(i).get("bydate") == null ? null : listHsa.get(i).get("bydate").toString();
				String value = listHsa.get(i).get(serch1) == null ? null : listHsa.get(i).get(serch1).toString();
				if (value == null) {
					mapHsa.put(bydate, null);
				} else {
					mapHsa.put(bydate, value );
				}
			}
		}
		if (listSD != null) {
			for (int i = 0; i < listSD.size(); i++) {
				String bydate = listSD.get(i).get("bydate") == null ? null : listSD.get(i).get("bydate").toString();
				String value = listSD.get(i).get(serch1) == null ? null : listSD.get(i).get(serch1).toString();
			
				if (value == null) {
					mapSD.put(bydate, null);
				} else {
					mapSD.put(bydate, value );
				}
			}
		}
		for (c.setTime(beginDateplus); AdvUtils.checkTimeType(map, c, beginDateplus,
				endDateplus); AdvUtils.step(map, c, beginDateplus, endDateplus)) {
			String tempkey = AdvUtils.getKeyByTimeType(map, c);
			String value = (String) mapSp.get(tempkey);
			String value2 = (String) mapHsa.get(tempkey);
			String value3 = (String) mapSD.get(tempkey);
			listLabel.add(tempkey);
			BigDecimal sp=new BigDecimal("0");
			BigDecimal sb=new BigDecimal("0");
			BigDecimal sd=new BigDecimal("0");
            if ( value != null) {
					sp=new BigDecimal(value);
			} 
			if ( value2 != null) {
					sb=new BigDecimal(value2);
			} 
			if (value3 != null) {
				sd=new BigDecimal(value3);
			} 
		    listData1.add(sp.add(sb).add(sd));
		}
		Map<String, Object> allmap = new HashMap<String, Object>();
		allmap.put("labels", listLabel);
		allmap.put("listdata1", listData1);
		return allmap;
	}

	public void getSerchStr(Map<String, Object> map) {
		String campaignType = (String) map.get("campaignType");
		String serch = (String) map.get("searchlist");
		String[] serchArray = serch.split(",");
		String serchlist = "";
		String HSAcsrt = "";
		for (int i = 0; i < serchArray.length; i++) {
				if ("ACOS".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(cost) / sum(attributedSales14d),0) ACOS ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(cost) / sum(attributedSales7d),0) ACOS ,";
				} else if ("ROAS".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(attributedSales14d) / sum(cost),0) ROAS ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(attributedSales7d) / sum(cost),0) ROAS ,";
				} else if ("CSRT".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(attributedConversions14d) / sum(clicks),0) CSRT ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(attributedConversions7d) / sum(clicks),0) CSRT ,";
				} else if ("avgcost".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
				} else if ("CTR".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
				}else if ("sumUnits".equals(serchArray[i])) {
					if ("all".equals(campaignType)) {
						serchlist = serchlist +"sum(attributedUnitsOrdered7d) sumUnits,";
						HSAcsrt = HSAcsrt+"sum(attributedConversions14d) sumUnits,";
					} else if ("HSA".equals(campaignType)) {
						HSAcsrt = HSAcsrt+"sum(attributedConversions14d) sumUnits,";
					} else if ("SP".equals(campaignType)) {
						serchlist = serchlist +"sum(attributedUnitsOrdered7d) sumUnits,";
					}
				} else {
					serchlist = serchlist +"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
					HSAcsrt = HSAcsrt+"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
				}
		}
		if(serchlist.contains(",")) {
			serchlist=serchlist.substring(0, serchlist.length()-1);
		}
		if(HSAcsrt.contains(",")) {
			HSAcsrt=HSAcsrt.substring(0, HSAcsrt.length()-1);
		}
		map.put("HSAserchlist", HSAcsrt);
		map.put("serchlist", serchlist);
		map.put("value1", serchArray[0]);
	}

	public List<Map<String, Object>> getCampaignPlacement(Map<String, Object> map) {
		String campaignType = (String) map.get("campaignType");
		List<Map<String, Object>> PlacementList = null;
		if ("SP".equals(campaignType)) {
			PlacementList = amzAdvCampaignsMapper.getCampaignPlacement(map);
		} else if ("HSA".equals(campaignType) || "SB".equals(campaignType)) {
			PlacementList = amzAdvCampaignHsaService.getCampaignPlacement(map);
		}
		return PlacementList;
	}

	public List<Map<String, Object>> getSumCampaigns(Map<String, Object> map) {
		String campaignType = (String) map.get("campaignType");
		Object paralist = map.get("paralist");
		if (paralist != null) {
			paralist = paralist.toString().replace("CSRT", "attributedConversions7d / clicks")
					.replace("ACOS", "cost / attributedSales7d")
					.replace("ROAS", "attributedSales7d / cost");
			map.put("paralist", paralist);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if ("SP".equals(campaignType)) {
			Map<String, Object> sumMap = amzAdvCampaignsMapper.getSumCampaigns(map);
			if (sumMap != null && sumMap.size() > 0) {
				list.add(sumMap);
			}
		}else if ("SD".equals(campaignType)) {
			Map<String, Object> sumMap = amzAdvCampaignsSDService.getSumCampaigns(map);
			if (sumMap != null && sumMap.size() > 0) {
				list.add(sumMap);
			}
		} else if ("HSA".equals(campaignType) || "SB".equals(campaignType)) {
			Map<String, Object> sumMap = amzAdvCampaignHsaService.getSumCampaigns(map);
			if (sumMap != null && sumMap.size() > 0) {
				list.add(sumMap);
			}
		} else if ("all".equals(campaignType)) {
			Map<String, Object> sumMap = amzAdvCampaignsMapper.getSumCampaigns(map);
			Map<String, Object> sumMapSB = amzAdvCampaignHsaService.getSumCampaigns(map);
			Map<String, Object> sumMapSD = amzAdvCampaignsSDService.getSumCampaigns(map);
			if (sumMap != null && sumMap.size() > 0) {
				list.add(sumMap);
			}
			if (sumMapSB != null && sumMapSB.size() > 0) {
				list.add(sumMapSB);
			}
			if (sumMapSD != null && sumMapSD.size() > 0) {
				list.add(sumMapSD);
			}
		}
		return list;
	}

}
