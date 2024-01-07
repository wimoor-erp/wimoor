package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmazonGroupMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvAuthMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.dao.AmzRegionMapper;
import com.wimoor.amazon.adv.common.pojo.AmazonGroup;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzRegion;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.pojo.Marketplace;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportHandlerService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSnapshotHandlerService;
import com.wimoor.amazon.adv.task.service.IAdvSchedulePlanItemService;
import com.wimoor.amazon.adv.task.service.IAdvSchedulePlanService;
import com.wimoor.amazon.adv.utils.HttpClientUtil;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

import lombok.Setter;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvAuthService")
@ConfigurationProperties(prefix = "auth")
public class AmzAdvAuthServiceImpl extends BaseService<AmzAdvAuth> implements IAmzAdvAuthService {
	@Resource
	AmzAdvAuthMapper amzAdvAuthMapper;
	@Resource
	AmzAdvProfileMapper amzAdvProfileMapper;
	@Resource
	AmzRegionMapper amzRegionMapper;
	@Resource
	AmazonGroupMapper amazonGroupMapper;
	@Resource
	@Lazy
	IAmzAdvRemindService amzAdvRemindService;
	@Resource
	@Lazy
	IAmzAdvReportHandlerService amzAdvReportHandlerService;
	@Resource
	IAdvSchedulePlanItemService advSchedulePlanItemService;
	@Resource
	@Lazy
	IAdvSchedulePlanService advSchedulePlanService;
	@Autowired
	@Lazy
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	@Lazy
	IAmzAdvSnapshotHandlerService iAmzAdvSnapshotHandlerService;
	@Setter
	private String redirecturl;
    @Value("${spring.profiles.active}")
    String profile;
	public String getRedirecturl() {
		return redirecturl;
	}


	@Override
	public synchronized int updateAll(AmzAdvAuth entity) throws BizException {
		return super.updateAll(entity);
	}

	ConcurrentHashMap<String, AmzRegion> regionMap = null;
	
	public List<Map<String, Object>> getAdvDateStatus(String sellerid, String status){
		return amzAdvProfileMapper.getAdvDateStatus(sellerid, status);
	}
	
	public List<Map<String, Object>> getProfileBySellerid(String sellerid){
		return amzAdvProfileMapper.getProfileBySellerid(sellerid);
	}

	public Integer findBindAdvCount(String shopid) {
		return amzAdvAuthMapper.findBindAdvCount(shopid);
	}
	
	public List<Map<String, Object>> getNoBindAdvRegion(String shopid, String groupid) {
		return amzRegionMapper.getNoBindAdvRegion(shopid, groupid);
	}
	
	public AmzRegion getClientIdByRegion(String region) {
		return amzRegionMapper.selectByPrimaryKey(region);
	}

	public List<AmzRegion> getAllRegion() {
		List<AmzRegion> regionList = null;
		if (regionMap != null && !regionMap.values().isEmpty()) {
			Collection<AmzRegion> values = regionMap.values();
			return new ArrayList<AmzRegion>(values);
		} else {
			regionList = amzRegionMapper.selectAll();
			regionMap = new ConcurrentHashMap<String, AmzRegion>();
			for (int i = 0; i < regionList.size(); i++) {
		       regionMap.put(regionList.get(i).getCode(), regionList.get(i));
			}
		}
		return regionList;
	}

	public synchronized AmzRegion getRegion(String region) {
		if (regionMap != null && !regionMap.isEmpty())
			return regionMap.get(region);
		else {
			getAllRegion();
			return regionMap.get(region);
		}
	}

	public AmzAdvAuth getAdvAuth(String groupid, String region) {
		Example example = new Example(AmzAdvAuth.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("groupid", groupid);
		crit.andEqualTo("region", region);
		crit.andEqualTo("disable", false);
		crit.andIsNotNull("refreshToken");
		List<AmzAdvAuth> list = this.selectByExample(example);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public AmzAdvAuth getAdvAuthDisable(String groupid, String region) {
		Example example = new Example(AmzAdvAuth.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("groupid", groupid);
		crit.andEqualTo("region", region);
		crit.andIsNotNull("refreshToken");
		List<AmzAdvAuth> list = this.selectByExample(example);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	

	public AmzAdvAuth generateToken(String groupid, String region) {
		AmzAdvAuth advauth = getAdvAuth(groupid, region);
		return generateToken(advauth);
	}

	
	public AmzAdvAuth generateToken(AmzAdvAuth advauth) {
		if (advauth != null) {
			long diff = new Date().getTime() - advauth.getOpttime().getTime();
			if (diff / (1000 * 60 * 55) >= 1) {
				return this.refreshToken(advauth);
			} else {
				return advauth;
			}
		}
		return null;
	}

	public AmzAdvAuth refreshToken(AmzAdvAuth advauth) {
		AmzRegion regionObject = this.getRegion(advauth.getRegion());
		String url = "https://api.amazon.com/auth/o2/token";
		Map<String, String> header = new HashMap<String, String>();
		header.put("X-Requested-With", "XMLHttpRequest");
		header.put("Content-Type", URLEncodedUtils.CONTENT_TYPE);
		if (advauth != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("grant_type", "refresh_token");
			map.put("client_id", regionObject.getClientId());
			map.put("client_secret", regionObject.getClientSecret());
			map.put("refresh_token", advauth.getRefreshToken());
			advauth.setOpttime(new Date());
			String response = null;
			 try {
					response = HttpClientUtil.postUrl(url, map, header, URLEncodedUtils.CONTENT_TYPE);
				} catch (Exception e) {
					e.printStackTrace();
					if(e.getMessage()!=null&&e.getMessage().contains("invalid_grant")) {
						advauth.setDisable(true);
					}
					advauth.setDisableTime(new Date());
					this.updateNotNull(advauth);
					throw new BizException("亚马逊授权获取失败"+advauth.getId());
				} 
			if (StringUtil.isNotEmpty(response)) {
				com.alibaba.fastjson.JSONObject a = com.alibaba.fastjson.JSON.parseObject(response);
				Object access_token = a.get("access_token");
				if (access_token != null)
					advauth.setAccessToken(access_token.toString());
				this.updateNotNull(advauth);
				return advauth;
			}
		}
		return null;
	}
	
	@CacheEvict(value = { "AmzAdvAuthorityCache" }, allEntries = true)
	public Boolean captureAmzAdvAuthByCode(String code, String region, String groupid) {
		String url = "https://api.amazon.com/auth/o2/token";
		String redirect_uri = "https://app.wimoor.com/advresult";
		AmzRegion regionObject = amzRegionMapper.selectByPrimaryKey(region);
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> header = new HashMap<String, String>();
		header.put("X-Requested-With", "XMLHttpRequest");
		header.put("charset", "UTF-8");
		header.put("Content-Type", URLEncodedUtils.CONTENT_TYPE);
		map.put("grant_type", "authorization_code");
		map.put("client_id", regionObject.getClientId().trim());
		map.put("client_secret", regionObject.getClientSecret().trim());
		map.put("code", code);
		map.put("redirect_uri", redirect_uri);
		String response = null;
		try {
			response = HttpClientUtil.postUrl(url, map, header, URLEncodedUtils.CONTENT_TYPE);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		if (response != null) {
			AmazonGroup group = amazonGroupMapper.selectByPrimaryKey(groupid);
			com.alibaba.fastjson.JSONObject item = com.alibaba.fastjson.JSON.parseObject(response);
			AmzAdvAuth advauth = new AmzAdvAuth();
			advauth.setAccessToken(item.getString("access_token"));
			advauth.setRefreshToken(item.getString("refresh_token"));
			advauth.setTokenType(item.getString("token_type"));
			advauth.setCode(code);
			advauth.setShopid(group.getShopid());
			advauth.setCreator(group.getCreator());
			advauth.setGroupid(groupid);
			advauth.setOpttime(new Date());
			advauth.setRegion(region);
			advauth.setDisable(false);
			AmzAdvAuth old = getAdvAuthDisable(groupid, region);
			if (old == null) {
				advauth.setCreatetime(advauth.getOpttime());
				this.save(advauth);
				captureProfiles(groupid, region);
				return true;
			} else {
				advauth.setId(old.getId());
				this.updateNotNull(advauth);
				captureProfiles(groupid, region);
				return true;
			}
		}
		return false;
	}

	public void createProfiles(String groupid, String region, String country) {
		AmzRegion regionObject = amzRegionMapper.selectByPrimaryKey(region);
		// String url="https://"+regionObject.getAdvpoint()+"/v1/profiles/register";
		String url = "https://" + regionObject.getAdvpoint() + "/testAccounts";
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		AmzAdvAuth advauth = generateToken(groupid, region);
		if (advauth != null) {
			header.put("Authorization", advauth.getAccessToken());
			advauth.setOpttime(new Date());
			Map<String, String> data = new HashMap<String, String>();
			data.put("countryCode", country);
			try {
			 String response = HttpClientUtil.putUrl(url, data, header, contentType);
			 System.out.println(response);
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}
	}

	public void createBrand(String groupid, String region, String country, String brand) {
		AmzRegion regionObject = amzRegionMapper.selectByPrimaryKey(region);
		// String url="https://"+regionObject.getAdvpoint()+"/v1/profiles/register";
		String url = "https://" + regionObject.getAdvpoint() + "/v2/profiles/registerBrand";
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		AmzAdvAuth advauth = generateToken(groupid, region);
		if (advauth != null) {
			header.put("Authorization", advauth.getAccessToken());
			advauth.setOpttime(new Date());
			Map<String, String> data = new HashMap<String, String>();
			data.put("countryCode", country);
			data.put("brand", brand);
			try {
				HttpClientUtil.putUrl(url, data, header, contentType);
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}
	}
	
	@CacheEvict(value = { "amzAdvProfileCache"  ,"AmzAdvAuthorityCache"}, allEntries = true)
	public String captureProfiles(String groupid, String region) {
		AmzRegion regionObject = amzRegionMapper.selectByPrimaryKey(region);
		String url = "https://" + regionObject.getAdvpoint() + "/v2/profiles";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		final AmzAdvAuth advauth = generateToken(groupid, region);
		if (advauth != null) {
			header.put("Authorization", advauth.getAccessToken());
			advauth.setOpttime(new Date());
			String response = null;
			try {
				response = HttpClientUtil.getUrl(url, header);
			} catch (BaseException e) {
				e.printStackTrace();
			}
			if (StringUtil.isNotEmpty(response)) {
				com.alibaba.fastjson.JSONArray a = com.alibaba.fastjson.JSON.parseArray(response);
				if (a != null && a.size() > 0) {
					final List<AmzAdvProfile> addList = new ArrayList<AmzAdvProfile>();
					for (int i = 0; i < a.size(); i++) {
						com.alibaba.fastjson.JSONObject item = a.getJSONObject(i);
						BigInteger profileId = item.getBigInteger("profileId");
						String countryCode = item.getString("countryCode");
						String currencyCode = item.getString("currencyCode");
						String timezone = item.getString("timezone");
						BigDecimal dailyBudget = item.getBigDecimal("dailyBudget");

						JSONObject accountInfo = item.getJSONObject("accountInfo");
						String sellerid = accountInfo.getString("id");
						String marketplaceid = accountInfo.getString("marketplaceStringId");
						String type = accountInfo.getString("type");

						AmzAdvProfile amzAdvProfile = new AmzAdvProfile();
						amzAdvProfile.setId(profileId);
						amzAdvProfile.setCountrycode(countryCode);
						amzAdvProfile.setCurrencycode(currencyCode);
						amzAdvProfile.setMarketplaceid(marketplaceid);
						amzAdvProfile.setSellerid(sellerid);
						amzAdvProfile.setAdvauthid(advauth.getId());
						amzAdvProfile.setTimezone(timezone);
						amzAdvProfile.setOpttime(new Date());
						amzAdvProfile.setType(type);
						amzAdvProfile.setDailyBudget(dailyBudget);
						if (amzAdvProfileMapper.existsWithPrimaryKey(profileId)) {
							AmzAdvProfile oldprofile = amzAdvProfileMapper.selectByPrimaryKey(profileId);
							if(!oldprofile.getAdvauthid().equals(advauth.getId())) {
								amzAdvAuthMapper.deleteByPrimaryKey(oldprofile.getAdvauthid());
							}
							amzAdvProfileMapper.updateByPrimaryKey(amzAdvProfile);
						} else { 
							amzAdvProfileMapper.insert(amzAdvProfile);
							addList.add(amzAdvProfile);
						}
						
					}
					 Thread thread = new Thread(new Runnable() {
							public void run() {
								for (AmzAdvProfile item : addList) {
									iAmzAdvSnapshotHandlerService.requestSnapshotByProfile(item, advauth);
									//amzAdvReportHandlerService.requestReportByProfile(item, advauth);
									iAmzAdvSnapshotHandlerService.requestStoreBrand(item);
								}
							}
						});
						threadPoolTaskExecutor.execute(thread);
					return response;
				}
			}
		}
		return "error";
	}
	@Cacheable(value = "amzAdvProfileCache", key = "#amzAdvAuth.id")
	public List<AmzAdvProfile> getAmzAdvProfile(AmzAdvAuth amzAdvAuth) {
		Example example = new Example(AmzAdvProfile.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("advauthid", amzAdvAuth.getId());
 		List<AmzAdvProfile> list = amzAdvProfileMapper.selectByExample(example);
		for (AmzAdvProfile item : list) {
			item.setAdvAuth(amzAdvAuth);
		}
		return list;
	}

	@Cacheable(value = "amzAdvProfileCache", key = "#profileid")
	public AmzAdvProfile getAmzAdvProfileByKey(BigInteger profileid) {
		AmzAdvProfile profile = amzAdvProfileMapper.selectByPrimaryKey(profileid);
		AmzAdvAuth auth = this.getAuth(profile);
		if (auth != null)
			return profile;
		return null;
	}

	public List<Map<String, Object>> getSelleridBygroup(String groupid) {
		return amzAdvProfileMapper.getSelleridBygroup(groupid);
	}

    public void checkProfileMessage(AmzAdvProfile profile,String msg) {
    	if(msg==null) {
    		if(profile.getErrorDay()!=null) {
    			profile.setErrorDay(null);
    			profile.setErrorLog("");
    			amzAdvProfileMapper.updateByPrimaryKey(profile);
    		}
    		return ;
    	}
    	if(msg.contains("Vendor not found")
		   ||msg.contains("No matching advertiser found for scope")
		   ||msg.contains("Not authorized to access scope")
					) {
    		profile.setErrorDay(new Date());
    		profile.setErrorLog(msg);
    		amzAdvProfileMapper.updateByPrimaryKey(profile);
			}
     
    }
    
	
	@Cacheable(value = "AmzAdvAuthorityCache", key = "#profile.id")
	public AmzAdvAuth getAuth(AmzAdvProfile profile) {
		if (profile == null) {
				throw new BaseException("无法获取所在站点权限");
		}
		if (profile.getAdvAuth() != null) {
			AmzAdvAuth amzadvauth = profile.getAdvAuth();
			AmzRegion regionObject = this.getRegion(amzadvauth.getRegion());
			amzadvauth.setRegionObject(regionObject);
			return profile.getAdvAuth();
		}
		else {
			AmzAdvAuth amzadvauth = this.selectByKey(profile.getAdvauthid());
			if (amzadvauth==null || amzadvauth.getDisable()) {
			     throw new BaseException("无法获取所在站点权限");
			}
			if(amzadvauth.getDisableTime()!=null&&GeneralUtil.distanceOfHour(amzadvauth.getDisableTime(), new Date())<2) {
				 throw new BizException("授权异常");
			}
			AmzRegion regionObject = this.getRegion(amzadvauth.getRegion());
			amzadvauth.setRegionObject(regionObject);
			profile.setAdvAuth(amzadvauth);
			return amzadvauth;
		}
		
	}

	

	@Cacheable(value = "AmzAdvAuthorityCache", key = "#groupid")
	public List<Map<String, Object>> getProfileByGroup(String groupid) {
		if (StringUtil.isNotEmpty(groupid)) {
			List<Map<String, Object>> list = amzAdvAuthMapper.selectProfileByGroup(groupid);
			return list;
		} else {
			return null;
		}
	}

	public Map<String, Object> getProfileByGroupAndmarkplace(String groupid, String marketplaceid) {
		return amzAdvAuthMapper.getProfileByGroupAndmarkplace(groupid, marketplaceid);
	}

	public List<Marketplace> getRegionByAdvGroup(String shopid, String advgroupid) {
		List<Marketplace> list = amzAdvAuthMapper.getRegionByAdvgroup(shopid, advgroupid);
		return list;
	}

	public List<AmzAdvProfile> getProfileIdByGroup(String shopid, String advgroupid) {
		List<AmzAdvProfile> list = amzAdvAuthMapper.getProfileIdByGroup(shopid, advgroupid);
		return list;
	}

	@Cacheable(value = "amzAdvProfileCache", key = "#id")
	public List<AmzAdvProfile> getProfileByAuth(String id) {
		Example example = new Example(AmzAdvProfile.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("advauthid", id);
		List<AmzAdvProfile> list = amzAdvProfileMapper.selectByExample(example);
		return list;
	}

	public List<Map<String, Object>> findAmzAdvAuthWithDisable() {
		return amzAdvAuthMapper.findAmzAdvAuthWithDisable();
	}

	@CacheEvict(value = { "AmzAdvAuthorityCache"}, allEntries = true)
	public void deleteAmzAdvAuthDateWithDisable() {
		List<Map<String, Object>> listMap = findAmzAdvAuthWithDisable();
		if (listMap != null && listMap.size() > 0) {
			for (Map<String, Object> map : listMap) {
				String sellerId = map.get("sellerId").toString();
				try {
					BigInteger profileid = (BigInteger) map.get("profileid");
					amzAdvAuthMapper.deleteAdventDateForProfileId(profileid);
					amzAdvAuthMapper.deleteAdventDateForSellerId(sellerId);
					amzAdvAuthMapper.deleteByPrimaryKey(map.get("authid"));
				}catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	@CacheEvict(value = { "AmzAdvAuthorityCache"}, allEntries = true)
	public Map<String, Object> updateAmzAdvAuthForDisable(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		AmzAdvAuth amzAdvAuth = amzAdvAuthMapper.selectByPrimaryKey(id);
		if (amzAdvAuth == null) {
			map.put("msg", "该广告店铺不存在！");
			map.put("code", "error");
		} else {
			amzAdvAuth.setDisable(true);
			amzAdvAuth.setDisableTime(new Date());
			amzAdvAuthMapper.updateByPrimaryKey(amzAdvAuth);
			List<AmzAdvProfile> list = getProfileByAuth(amzAdvAuth.getId());
			if (list != null && list.size() > 0) {
				for (AmzAdvProfile amzAdvProfile : list) {
					amzAdvRemindService.deleteReminForProfileId(amzAdvProfile.getId());
					List<Map<String, Object>> planIdList = advSchedulePlanItemService.findPlanIdForProfile(amzAdvProfile.getId());
					if (planIdList != null && planIdList.size() > 0) {
						for (Map<String, Object> planIdMap : planIdList) {
							String planid = planIdMap.get("planid").toString();
							advSchedulePlanService.deletePlan(planid);
						}
					}
				}
			}
			map.put("msg", "广告店铺授权已经停用！");
			map.put("code", "success");
		}
		return map;
	}
	
	@CacheEvict(value = { "AmzAdvAuthorityCache" }, allEntries = true)
	public int updateAdvAuthDisable(Map<String, Object> map) {
		return amzAdvAuthMapper.updateAdvAuthDisable(map);
	}

	@CacheEvict(value = { "AmzAdvAuthorityCache" }, allEntries = true)
	public int updateAdvAuthUndisable(String groupid, String region) {
		AmzAdvAuth auth = getAdvAuthDisable(groupid,region);
		auth.setDisable(false);
		return this.updateAll(auth);
	}




	@Override
	@Cacheable(value = "AmzAdvAuthorityCache#6000")
	public List<AmzAdvAuth> list() {
		// TODO Auto-generated method stub
		Example query=new Example(AmzAdvAuth.class);
		Criteria crit = query.createCriteria();
		crit.andEqualTo("disable", Boolean.FALSE);
		return this.amzAdvAuthMapper.selectByExample(query);
	}

	@Override
	@Cacheable(value = "AmzAdvAuthorityCache#6000")
	public AmzAdvAuth selectByKey(Object key) {
		// TODO Auto-generated method stub
		return super.selectByKey(key);
	}

	@Override
	@CacheEvict(value = { "AmzAdvAuthorityCache" }, allEntries = true)
	public int save(AmzAdvAuth entity) throws BizException {
		// TODO Auto-generated method stub
		return super.save(entity);
	}


	@Override
	@CacheEvict(value = { "AmzAdvAuthorityCache" }, allEntries = true)
	public int updateNotNull(AmzAdvAuth entity) throws BizException {
		// TODO Auto-generated method stub
		return super.updateNotNull(entity);
	}


	@Override
	public List<AmzAdvAuth> selectNotDisableList() {
		// TODO Auto-generated method stub
		return amzAdvAuthMapper.selectNotDisableList();
	}

	
}
