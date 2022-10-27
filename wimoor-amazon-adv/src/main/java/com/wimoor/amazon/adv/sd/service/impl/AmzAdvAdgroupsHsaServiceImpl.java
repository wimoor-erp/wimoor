package com.wimoor.amazon.adv.sd.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdgroupsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdgroupsHsaService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
 

import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvAdgroupsHsaService")
public class AmzAdvAdgroupsHsaServiceImpl extends BaseService<AmzAdvAdgroupsHsa> implements IAmzAdvAdgroupsHsaService {
	
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvAdgroupsHsaMapper amzAdvAdgroupsHsaMapper;
	
	public AmzAdvAdgroupsHsa amzGetSBAdGroup(UserInfo user,BigInteger profileId, String adgroupid) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/adGroups/" + adgroupid;
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			AmzAdvAdgroupsHsa adgroup = null;
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			adgroup = new AmzAdvAdgroupsHsa();
			adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
			adgroup.setName(item.getString("name"));
			adgroup.setCampaignid(item.getBigInteger("campaignId"));
			adgroup.setProfileid(profileId);
			adgroup.setOpttime(new Date());
			AmzAdvAdgroupsHsa oldAdgroup = amzAdvAdgroupsHsaMapper.selectByPrimaryKey(adgroup);
			if (oldAdgroup != null) {
				this.updateAll(adgroup);
			} else {
				this.save(adgroup);
			}
			return adgroup;
		}
		return null;
	}
	
	public List<AmzAdvAdgroupsHsa> amzGetSBAdGroupList(UserInfo user,BigInteger profileId, Map<String, Object> adgroupParam) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/adGroups?";
		String param = "";
		if (adgroupParam != null) {
			param = GeneralUtil.addParamToUrl(param, adgroupParam, "startIndex");
			param = GeneralUtil.addParamToUrl(param, adgroupParam, "count");
			param = GeneralUtil.addParamToUrl(param, adgroupParam, "stateFilter");
			param = GeneralUtil.addParamToUrl(param, adgroupParam, "name");
			param = GeneralUtil.addParamToUrl(param, adgroupParam, "campaignIdFilter");
			url = url + ("".equals(param) ? "" : param);
		}
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvAdgroupsHsa> list = new ArrayList<AmzAdvAdgroupsHsa>();
			AmzAdvAdgroupsHsa adgroup = null;
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				adgroup = new AmzAdvAdgroupsHsa();
				adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
				adgroup.setName(item.getString("name"));
				adgroup.setCampaignid(item.getBigInteger("campaignId"));
				adgroup.setProfileid(profileId);
				adgroup.setOpttime(new Date());
				AmzAdvAdgroupsHsa oldAdgroup = amzAdvAdgroupsHsaMapper.selectByPrimaryKey(adgroup);
				if (oldAdgroup != null) {
					this.updateAll(adgroup);
				} else {
					this.save(adgroup);
				}
				list.add(adgroup);
			}
			return list;
		}
		return null;
	}
}
