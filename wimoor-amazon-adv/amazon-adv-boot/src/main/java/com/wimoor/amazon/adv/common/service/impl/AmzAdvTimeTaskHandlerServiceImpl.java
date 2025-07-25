package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.quartz.CronExpression;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.common.service.IAmzAdvTimeTaskHandlerService;
import com.wimoor.amazon.adv.report.service.impl.AmzAdvReportHandlerServiceImpl.AdvRecordType;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.adv.sp.service.IAmzAdvAdGroupService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductAdsService;
import com.wimoor.amazon.adv.task.dao.AmzAdvSchedulePlanDataMapper;
import com.wimoor.amazon.adv.task.dao.AmzAdvSchedulePlanItemMapper;
import com.wimoor.amazon.adv.task.pojo.AmzAdvSchedulePlanItem;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvTimeTaskHandlerService")
public class AmzAdvTimeTaskHandlerServiceImpl implements IAmzAdvTimeTaskHandlerService {
	@Resource
	IAmzAdvAdGroupService amzAdvAdGroupService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	@Resource
	IAmzAdvProductAdsService amzAdvProductAdsService;
	@Resource
	IAmzAdvKeywordsService amzAdvKeywordsService;
	@Resource
	IAmzAdvKeywordsNegativaService amzAdvKeywordsNegativaService;
	@Resource
	IAmzAdvCampKeywordsNegativaService amzAdvCampKeywordsNegativaService;
	@Resource
	AmzAdvSchedulePlanItemMapper amzAdvSchedulePlanItemMapper;
	@Resource
	AmzAdvSchedulePlanDataMapper amzAdvSchedulePlanDataMapper;
 
	@Resource
	@Lazy
	IAmzAdvRemindService amzAdvRemindService;
//	@Autowired
//	private SchedulerFactoryBean schedulerFactoryBean;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	public void timeTask(String param) {
		// TODO Auto-generated method stub
		
		JSONObject json = GeneralUtil.getJsonObject(param);
		String recordType = json.getString("recordType");
		String campaignType = json.getString("campaignType");
		String profileId = json.getString("profileId");
		BigInteger planId = json.getBigInteger("planId");
		String taskId = json.getString("taskId");
		//String type = json.getString("type");
		BigInteger adGroupid = json.getBigInteger("adGroupId");
		BigInteger campaignId = json.getBigInteger("campaignId");
		UserInfo user =new UserInfo();
		user.setId("1");
		AmzAdvSchedulePlanItem amzAdvSchedulePlanItem = new AmzAdvSchedulePlanItem();
		amzAdvSchedulePlanItem.setPlanid(planId);
		amzAdvSchedulePlanItem.setTaskid(taskId);
		amzAdvSchedulePlanItem = amzAdvSchedulePlanItemMapper.selectByPrimaryKey(amzAdvSchedulePlanItem);
		String status = amzAdvSchedulePlanItem.getStatus();
		BigDecimal bid = amzAdvSchedulePlanItem.getBid();
		AmzAdvProfile advprofile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileId));
		if(advprofile==null) {
			return;
		}
		if (AdvRecordType.campaigns.equals(recordType)) {
			if ("sponsoredProducts".equals(campaignType)) {
				List<AmzAdvCampaigns> campaignsList = new ArrayList<AmzAdvCampaigns>();
				Example example2 = new Example(AmzAdvCampaigns.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvCampaigns campaigns = amzAdvCampaignService.selectOneByExample(example2);
				if (campaigns != null) {
					if (bid != null) {
						campaigns.setDailybudget(bid);
					}
					if(status != null) {
						campaigns.setState(status);
					}
					campaignsList.add(campaigns);
					amzAdvCampaignService.amzUpdateSpCampaigns(user, new BigInteger(profileId), campaignsList);
				}
			} else {
				List<AmzAdvCampaignsHsa> campaignsList = new ArrayList<AmzAdvCampaignsHsa>();
				Example example2 = new Example(AmzAdvCampaignsHsa.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvCampaignsHsa campaignsHsa = amzAdvCampaignHsaService.selectOneByExample(example2);
				if (campaignsHsa != null) {
					if (bid != null) {
						campaignsHsa.setBudget(bid);
					}
					if(status != null) {
						campaignsHsa.setState(status);
					}
					campaignsList.add(campaignsHsa);
					amzAdvCampaignHsaService.amzUpdateSBCampaigns(user, new BigInteger(profileId), campaignsList);
				}
			}
		} else if (AdvRecordType.adGroups.equals(recordType)) {
			List<AmzAdvAdgroups> adgroupsList = new ArrayList<AmzAdvAdgroups>();
			Example example2 = new Example(AmzAdvAdgroups.class);
			Criteria crit2 = example2.createCriteria();
			crit2.andEqualTo("adgroupid", adGroupid);
			crit2.andEqualTo("profileid", profileId);
			crit2.andEqualTo("campaignid", campaignId);
			AmzAdvAdgroups amzAdvAdgroups = amzAdvAdGroupService.selectOneByExample(example2);
			if (amzAdvAdgroups != null) {
				if (bid != null) {
					amzAdvAdgroups.setDefaultbid(bid);
				}
				if(status != null) {
					amzAdvAdgroups.setState(status);
				}
				adgroupsList.add(amzAdvAdgroups);
				amzAdvAdGroupService.amzUpdateAdGroups(user, new BigInteger(profileId), adgroupsList);
			}
		} else if (AdvRecordType.productAds.equals(recordType)) {
			List<AmzAdvProductads> productAdsList = new ArrayList<AmzAdvProductads>();
			JSONArray idArray = json.getJSONArray("adIdArray");
			BigInteger id = null;
			for (int i = 0; i < idArray.size(); i++) {
				id = idArray.getJSONObject(i).getBigInteger("adId");
				Example example2 = new Example(AmzAdvProductads.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("adgroupid", adGroupid);
				crit2.andEqualTo("adid", id);
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvProductads amzAdvProductads = amzAdvProductAdsService.selectOneByExample(example2);
				if (amzAdvProductads != null) {
					if(status != null) {
						amzAdvProductads.setState(status);
					}
					productAdsList.add(amzAdvProductads);
				}
			}
			amzAdvProductAdsService.amzUpdateProductAds(user, new BigInteger(profileId), productAdsList);
		} else if (AdvRecordType.keywords.equals(recordType)) {
			JSONArray idArray = json.getJSONArray("keywordIdArray");
			List<AmzAdvKeywords> biddableKeywords = new ArrayList<AmzAdvKeywords>();
			BigInteger id = null;
			for (int i = 0; i < idArray.size(); i++) {
				id = idArray.getJSONObject(i).getBigInteger("keywordId");
				Example example2 = new Example(AmzAdvKeywords.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("adgroupid", adGroupid);
				crit2.andEqualTo("keywordid", id);
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvKeywords amzAdvKeywords = amzAdvKeywordsService.selectOneByExample(example2);
				if (amzAdvKeywords != null) {
					if (bid != null) {
						amzAdvKeywords.setBid(bid);
					}
					if(status != null) {
						amzAdvKeywords.setState(status);
					}
					biddableKeywords.add(amzAdvKeywords);
				}
			}
			amzAdvKeywordsService.amzUpdateKeywords(user, new BigInteger(profileId), biddableKeywords);
		} else if (AdvRecordType.negativeKeywords.equals(recordType)) {
			List<AmzAdvKeywordsNegativa> negetivaKeywords = new ArrayList<AmzAdvKeywordsNegativa>();
			List<AmzAdvCampKeywordsNegativa> campKeywordsNegativa = new ArrayList<AmzAdvCampKeywordsNegativa>();
			JSONArray idArray = json.getJSONArray("keywordIdArray");
			BigInteger id = null;
			for (int i = 0; i < idArray.size(); i++) {
				id = idArray.getJSONObject(i).getBigInteger("keywordId");
				if (adGroupid != null) {
					Example example2 = new Example(AmzAdvKeywordsNegativa.class);
					Criteria crit2 = example2.createCriteria();
					crit2.andEqualTo("adgroupid", adGroupid);
					crit2.andEqualTo("keywordid", id);
					crit2.andEqualTo("profileid", profileId);
					crit2.andEqualTo("campaignid", campaignId);
					AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = amzAdvKeywordsNegativaService.selectOneByExample(example2);
					if (amzAdvKeywordsNegativa != null) {
						if(status != null) {
							amzAdvKeywordsNegativa.setState(status);
						}
						negetivaKeywords.add(amzAdvKeywordsNegativa);
					}
				} else {
					Example example3 = new Example(AmzAdvCampKeywordsNegativa.class);
					Criteria crit3 = example3.createCriteria();
					crit3.andEqualTo("keywordid", id);
					crit3.andEqualTo("profileid", profileId);
					crit3.andEqualTo("campaignid", campaignId);
					AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = amzAdvCampKeywordsNegativaService.selectOneByExample(example3);
					if (amzAdvCampKeywordsNegativa != null) {
						if(status != null) {
							amzAdvCampKeywordsNegativa.setState(status);
						}
						campKeywordsNegativa.add(amzAdvCampKeywordsNegativa);
					}
				}
			}
			if (negetivaKeywords != null && negetivaKeywords.size() > 0) {
				amzAdvKeywordsNegativaService.amzUpdateNegativaKeywords(user, new BigInteger(profileId),
						negetivaKeywords);
			}
			if (campKeywordsNegativa != null && campKeywordsNegativa.size() > 0) {
				amzAdvCampKeywordsNegativaService.amzUpdateCampaignNegativeKeywords(user, new BigInteger(profileId),
						campKeywordsNegativa);
			}
		}
		 
	 
	}

	public Date getNextFireTime(String cronExpression, AmzAdvProfile advprofile) {
		if (StringUtil.isEmpty(cronExpression)) {
			return null;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone(advprofile.getTimezone()));
			CronExpression cron = new CronExpression(cronExpression);
			Date nextFireDate = cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
			String startDay = format.format(nextFireDate.getTime());
			return GeneralUtil.StringfromDate(startDay, "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
