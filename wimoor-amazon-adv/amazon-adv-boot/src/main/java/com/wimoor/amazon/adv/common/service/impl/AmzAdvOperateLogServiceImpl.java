package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.dao.AmzAdvOperateLogMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsNegativaHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeNegativaHsa;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;

@Service("amzAdvOperateLogService")
public class AmzAdvOperateLogServiceImpl extends BaseService<AmzAdvOperateLog> implements IAmzAdvOperateLogService{
	@Resource
	AmzAdvOperateLogMapper amzAdvOperateLogMapper;
	
	public void saveOperateLog(String beanClasz,String userId,BigInteger profileId,Object afterObject,Object beforeObject) {
		// TODO Auto-generated method stub
		if("AmzAdvCampaigns".equals(beanClasz)) {
			AmzAdvCampaigns campaign = (AmzAdvCampaigns) afterObject;
			AmzAdvCampaigns oldCampaign = (AmzAdvCampaigns) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setCampaignid(campaign.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz("AmzAdvCampaigns");
			String campaignjson = GeneralUtil.toJSON(campaign);
			String oldCampaignjson = GeneralUtil.toJSON(oldCampaign);
			operateLog.setAfterobject(campaignjson);
			operateLog.setBeforeobject(oldCampaignjson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvCampaignsHsa".equals(beanClasz)) {
			AmzAdvCampaignsHsa campaign = (AmzAdvCampaignsHsa) afterObject;
			AmzAdvCampaignsHsa oldCampaign = (AmzAdvCampaignsHsa) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setCampaignid(campaign.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz("AmzAdvCampaignsHsa");
			String campaignjson = GeneralUtil.toJSON(campaign);
			String oldCampaignjson = GeneralUtil.toJSON(oldCampaign);
			operateLog.setAfterobject(campaignjson);
			operateLog.setBeforeobject(oldCampaignjson);
			amzAdvOperateLogMapper.insert(operateLog);
		}else if("AmzAdvCampaignsSD".equals(beanClasz)) {
			AmzAdvCampaignsSD campaign = (AmzAdvCampaignsSD) afterObject;
			AmzAdvCampaignsSD oldCampaign = (AmzAdvCampaignsSD) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setCampaignid(campaign.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz("AmzAdvCampaignsHsa");
			String campaignjson = GeneralUtil.toJSON(campaign);
			String oldCampaignjson = GeneralUtil.toJSON(oldCampaign);
			operateLog.setAfterobject(campaignjson);
			operateLog.setBeforeobject(oldCampaignjson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvCampKeywordsNegativa".equals(beanClasz)) {
			AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = (AmzAdvCampKeywordsNegativa) afterObject;
			AmzAdvCampKeywordsNegativa oldamzAdvCampKeywordsNegativa = (AmzAdvCampKeywordsNegativa) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setCampaignid(amzAdvCampKeywordsNegativa.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String campKeywordsNegativajson = GeneralUtil.toJSON(amzAdvCampKeywordsNegativa);
			String oldCampKeywordsNegativajson = GeneralUtil.toJSON(oldamzAdvCampKeywordsNegativa);
			operateLog.setAfterobject(campKeywordsNegativajson);
			operateLog.setBeforeobject(oldCampKeywordsNegativajson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvAdgroups".equals(beanClasz)) {
			AmzAdvAdgroups adgroup = (AmzAdvAdgroups) afterObject;
			AmzAdvAdgroups oldadgroup = (AmzAdvAdgroups) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(adgroup.getAdgroupid());
			operateLog.setCampaignid(adgroup.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String adgroupjson = GeneralUtil.toJSON(adgroup);
			String oldadgroupjson = GeneralUtil.toJSON(oldadgroup);
			operateLog.setAfterobject(adgroupjson);
			operateLog.setBeforeobject(oldadgroupjson);
			amzAdvOperateLogMapper.insert(operateLog);
		}	else if("AmzAdvAdgroupsSD".equals(beanClasz)) {
			AmzAdvAdgroupsSD adgroup = (AmzAdvAdgroupsSD) afterObject;
			AmzAdvAdgroupsSD oldadgroup = (AmzAdvAdgroupsSD) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(adgroup.getAdgroupid());
			operateLog.setCampaignid(adgroup.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String adgroupjson = GeneralUtil.toJSON(adgroup);
			String oldadgroupjson = GeneralUtil.toJSON(oldadgroup);
			operateLog.setAfterobject(adgroupjson);
			operateLog.setBeforeobject(oldadgroupjson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvKeywords".equals(beanClasz)) {
			AmzAdvKeywords keywords = (AmzAdvKeywords) afterObject;
			AmzAdvKeywords oldKeywords = (AmzAdvKeywords) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(keywords.getAdgroupid());
			operateLog.setCampaignid(keywords.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String keywordsjson = GeneralUtil.toJSON(keywords);
			String oldKeywordsjson = GeneralUtil.toJSON(oldKeywords);
			operateLog.setAfterobject(keywordsjson);
			operateLog.setBeforeobject(oldKeywordsjson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvKeywordsNegativa".equals(beanClasz)) {
			AmzAdvKeywordsNegativa keywordsNegativa = (AmzAdvKeywordsNegativa) afterObject;
			AmzAdvKeywordsNegativa oldKeywordsNegativa = (AmzAdvKeywordsNegativa) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(keywordsNegativa.getAdgroupid());
			operateLog.setCampaignid(keywordsNegativa.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String keywordsNegativajson = GeneralUtil.toJSON(keywordsNegativa);
			String oldKeywordsNegativajson = GeneralUtil.toJSON(oldKeywordsNegativa);
			operateLog.setAfterobject(keywordsNegativajson);
			operateLog.setBeforeobject(oldKeywordsNegativajson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvKeywordsNegativaHsa".equals(beanClasz)) {
			AmzAdvKeywordsNegativaHsa keywordsNegativa = (AmzAdvKeywordsNegativaHsa) afterObject;
			AmzAdvKeywordsNegativaHsa oldKeywordsNegativa = (AmzAdvKeywordsNegativaHsa) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(keywordsNegativa.getAdgroupid());
			operateLog.setCampaignid(keywordsNegativa.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String keywordsNegativajson = GeneralUtil.toJSON(keywordsNegativa);
			String oldKeywordsNegativajson = GeneralUtil.toJSON(oldKeywordsNegativa);
			operateLog.setAfterobject(keywordsNegativajson);
			operateLog.setBeforeobject(oldKeywordsNegativajson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvProductads".equals(beanClasz)) {
			AmzAdvProductads productAds = (AmzAdvProductads) afterObject;
			AmzAdvProductads oldProductAds = (AmzAdvProductads) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(productAds.getAdgroupid());
			operateLog.setCampaignid(productAds.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String productAdsjson = GeneralUtil.toJSON(productAds);
			String oldProductAdsjson = GeneralUtil.toJSON(oldProductAds);
			operateLog.setAfterobject(productAdsjson);
			operateLog.setBeforeobject(oldProductAdsjson);
			amzAdvOperateLogMapper.insert(operateLog);
		}else if("AmzAdvProductadsSD".equals(beanClasz)) {
			AmzAdvProductadsSD productAds = (AmzAdvProductadsSD) afterObject;
			AmzAdvProductadsSD oldProductAds = (AmzAdvProductadsSD) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(productAds.getAdgroupid());
			operateLog.setCampaignid(productAds.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String productAdsjson = GeneralUtil.toJSON(productAds);
			String oldProductAdsjson = GeneralUtil.toJSON(oldProductAds);
			operateLog.setAfterobject(productAdsjson);
			operateLog.setBeforeobject(oldProductAdsjson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvProductTarge".equals(beanClasz)) {
			AmzAdvProductTarge productTarge = (AmzAdvProductTarge) afterObject;
			AmzAdvProductTarge oldProductTarge = (AmzAdvProductTarge) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(productTarge.getAdgroupid());
			operateLog.setCampaignid(productTarge.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String productTargejson = GeneralUtil.toJSON(productTarge);
			String oldProductTargejson = GeneralUtil.toJSON(oldProductTarge);
			operateLog.setAfterobject(productTargejson);
			operateLog.setBeforeobject(oldProductTargejson);
			amzAdvOperateLogMapper.insert(operateLog);
		}else if("AmzAdvProductTargeSD".equals(beanClasz)) {
			AmzAdvProductTargeSD productTarge = (AmzAdvProductTargeSD) afterObject;
			AmzAdvProductTargeSD oldProductTarge = (AmzAdvProductTargeSD) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(productTarge.getAdgroupid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String productTargejson = GeneralUtil.toJSON(productTarge);
			String oldProductTargejson = GeneralUtil.toJSON(oldProductTarge);
			operateLog.setAfterobject(productTargejson);
			operateLog.setBeforeobject(oldProductTargejson);
			amzAdvOperateLogMapper.insert(operateLog);
		}else if("AmzAdvProductTargeHsa".equals(beanClasz)) {
			AmzAdvProductTargeHsa productTarge = (AmzAdvProductTargeHsa) afterObject;
			AmzAdvProductTargeHsa oldProductTarge = (AmzAdvProductTargeHsa) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(productTarge.getAdgroupid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String productTargejson = GeneralUtil.toJSON(productTarge);
			String oldProductTargejson = GeneralUtil.toJSON(oldProductTarge);
			operateLog.setAfterobject(productTargejson);
			operateLog.setBeforeobject(oldProductTargejson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
		else if("AmzAdvProductTargeNegativa".equals(beanClasz)) {
			AmzAdvProductTargeNegativa productTargeNegativa = (AmzAdvProductTargeNegativa) afterObject;
			AmzAdvProductTargeNegativa oldProductTargeNegativa = (AmzAdvProductTargeNegativa) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(productTargeNegativa.getAdgroupid());
			operateLog.setCampaignid(productTargeNegativa.getCampaignid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String productTargeNegativajson = GeneralUtil.toJSON(productTargeNegativa);
			String oldProductTargeNegativajson = GeneralUtil.toJSON(oldProductTargeNegativa);
			operateLog.setAfterobject(productTargeNegativajson);
			operateLog.setBeforeobject(oldProductTargeNegativajson);
			amzAdvOperateLogMapper.insert(operateLog);
		}	else if("AmzAdvProductTargeNegativaSD".equals(beanClasz)) {
			AmzAdvProductTargeNegativaSD productTargeNegativa = (AmzAdvProductTargeNegativaSD) afterObject;
			AmzAdvProductTargeNegativaSD oldProductTargeNegativa = (AmzAdvProductTargeNegativaSD) beforeObject;
			AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			operateLog.setAdgroupid(productTargeNegativa.getAdgroupid());
			operateLog.setProfileid(profileId);
			operateLog.setOperator(userId);
			operateLog.setOpttime(new Date());
			operateLog.setBeanclasz(beanClasz);
			String productTargeNegativajson = GeneralUtil.toJSON(productTargeNegativa);
			String oldProductTargeNegativajson = GeneralUtil.toJSON(oldProductTargeNegativa);
			operateLog.setAfterobject(productTargeNegativajson);
			operateLog.setBeforeobject(oldProductTargeNegativajson);
			amzAdvOperateLogMapper.insert(operateLog);
		}
	}

	public void saveBatchOperateLog(String beanClasz,String userId,BigInteger profileId,Map<BigInteger, List<Object>> map,Map<BigInteger, List<Object>> oldmap) {
		List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
		if("AmzAdvCampKeywordsNegativa".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				operateLog.setCampaignid(key);
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray campaignKeywordsjson = new JSONArray(map.get(key));
				operateLog.setAfterobject(campaignKeywordsjson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldCampaignKeywordsjson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldCampaignKeywordsjson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvProductads".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductads productads = (AmzAdvProductads) map.get(key).get(0);
				operateLog.setCampaignid(productads.getCampaignid());
				operateLog.setAdgroupid(productads.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray productadsjson = new JSONArray(map.get(key));
				operateLog.setAfterobject(productadsjson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldProductadsjson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldProductadsjson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvProductadsSD".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductadsSD productads = (AmzAdvProductadsSD) map.get(key).get(0);
				operateLog.setCampaignid(productads.getCampaignid());
				operateLog.setAdgroupid(productads.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray productadsjson = new JSONArray(map.get(key));
				operateLog.setAfterobject(productadsjson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldProductadsjson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldProductadsjson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvKeywordsNegativa".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvKeywordsNegativa keywordsNegativa = (AmzAdvKeywordsNegativa) map.get(key).get(0);
				operateLog.setCampaignid(keywordsNegativa.getCampaignid());
				operateLog.setAdgroupid(keywordsNegativa.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray keywordsNegativajson = new JSONArray(map.get(key));
				operateLog.setAfterobject(keywordsNegativajson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldKeywordsNegativajson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldKeywordsNegativajson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvKeywordsNegativaHsa".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvKeywordsNegativaHsa keywordsNegativa = (AmzAdvKeywordsNegativaHsa) map.get(key).get(0);
				operateLog.setCampaignid(keywordsNegativa.getCampaignid());
				operateLog.setAdgroupid(keywordsNegativa.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray keywordsNegativajson = new JSONArray(map.get(key));
				operateLog.setAfterobject(keywordsNegativajson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldKeywordsNegativajson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldKeywordsNegativajson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvKeywords".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvKeywords keywords = (AmzAdvKeywords) map.get(key).get(0);
				operateLog.setCampaignid(keywords.getCampaignid());
				operateLog.setAdgroupid(keywords.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray keywordsjson = new JSONArray(map.get(key));
				operateLog.setAfterobject(keywordsjson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldKeywordsjson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldKeywordsjson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvKeywordsHsa".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvKeywordsHsa keywords = (AmzAdvKeywordsHsa) map.get(key).get(0);
				operateLog.setCampaignid(keywords.getCampaignid());
				operateLog.setAdgroupid(keywords.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray keywordsjson = new JSONArray(map.get(key));
				operateLog.setAfterobject(keywordsjson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldKeywordsjson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldKeywordsjson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvProductTarge".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductTarge productTarge = (AmzAdvProductTarge) map.get(key).get(0);
				operateLog.setCampaignid(productTarge.getCampaignid());
				operateLog.setAdgroupid(productTarge.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray productTargejson = new JSONArray(map.get(key));
				operateLog.setAfterobject(productTargejson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldProductTargejson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldProductTargejson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvProductTargeSD".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductTargeSD productTarge = (AmzAdvProductTargeSD) map.get(key).get(0);
				operateLog.setAdgroupid(productTarge.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray productTargejson = new JSONArray(map.get(key));
				operateLog.setAfterobject(productTargejson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldProductTargejson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldProductTargejson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}else if("AmzAdvProductTargeHsa".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductTargeHsa productTarge = (AmzAdvProductTargeHsa) map.get(key).get(0);
				operateLog.setAdgroupid(productTarge.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray productTargejson = new JSONArray(map.get(key));
				operateLog.setAfterobject(productTargejson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldProductTargejson = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldProductTargejson.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvProductTargeNegativa".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductTargeNegativa productTargeNegativa = (AmzAdvProductTargeNegativa) map.get(key).get(0);
				operateLog.setCampaignid(productTargeNegativa.getCampaignid());
				operateLog.setAdgroupid(productTargeNegativa.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray productTargeNegativajson = new JSONArray(map.get(key));
				operateLog.setAfterobject(productTargeNegativajson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldProductTargeNegativa = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldProductTargeNegativa.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvProductTargeNegativaSB".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductTargeNegativaHsa productTargeNegativa = (AmzAdvProductTargeNegativaHsa) map.get(key).get(0);
				operateLog.setCampaignid(productTargeNegativa.getCampaignid());
				operateLog.setAdgroupid(productTargeNegativa.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray productTargeNegativajson = new JSONArray(map.get(key));
				operateLog.setAfterobject(productTargeNegativajson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldProductTargeNegativa = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldProductTargeNegativa.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		else if("AmzAdvProductTargeNegativaSD".equals(beanClasz)) {
			for(BigInteger key: map.keySet()) {
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductTargeNegativaSD productTargeNegativa = (AmzAdvProductTargeNegativaSD) map.get(key).get(0);
				operateLog.setAdgroupid(productTargeNegativa.getAdgroupid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(userId);
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz(beanClasz);
				JSONArray productTargeNegativajson = new JSONArray(map.get(key));
				operateLog.setAfterobject(productTargeNegativajson.toJSONString());
				if(oldmap == null) {
					operateLog.setBeforeobject(null);
				}else {
					JSONArray oldProductTargeNegativa = new JSONArray(oldmap.get(key));
					operateLog.setBeforeobject(oldProductTargeNegativa.toJSONString());
				}
				operateLogList.add(operateLog);
			}
		}
		amzAdvOperateLogMapper.insertList(operateLogList);
	}
	
	public PageList<Map<String,Object>> getOperateLogList(Map<String, Object> map, PageBounds pageBounds){
		
		return amzAdvOperateLogMapper.getOperateLogList(map, pageBounds);
	}

	public void insertList(List<AmzAdvOperateLog> operateLogList) {
		// TODO Auto-generated method stub
		if(operateLogList!=null&&operateLogList.size()>0) {
			this.amzAdvOperateLogMapper.insertList(operateLogList);
		}
	}
	
	public int updateOperateLogRemark(String id, String remark) {
		AmzAdvOperateLog operateLog = amzAdvOperateLogMapper.selectByPrimaryKey(id);
		operateLog.setRemark(remark);
		return this.updateNotNull(operateLog);
	}
}
