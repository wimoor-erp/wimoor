package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvRequestMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvSnapshotMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvReportMtericsMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvBrand;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAssetsService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBrandService;
import com.wimoor.amazon.adv.common.service.IAmzAdvPortfoliosService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.common.service.IAmzAdvSnapshotHandlerService;
import com.wimoor.amazon.adv.common.service.IAmzAdvStoresService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.common.GeneralUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

/*@Slf4j*/
@Service("amzAdvSnapshotHandlerService")
public class AmzAdvSnapshotHandlerServiceImpl implements IAmzAdvSnapshotHandlerService {
 
    @Autowired
    @Lazy
	IAmzAdvAuthService amzAdvAuthService;
    @Autowired
    @Lazy
	AmzAdvRequestMapper amzAdvRequestMapper;
    @Autowired
    @Lazy
	IAmzAdvReportTreatService amzAdvReportTreatService;
    @Autowired
    @Lazy
	AmzAdvSnapshotMapper amzAdvSnapshotMapper;
    @Autowired
    @Lazy
	IAmzAdvRemindService amzAdvRemindService;
    @Autowired
    @Lazy
	AmzAdvAdgroupsMapper amzAdvAdgroupsMapper;
    @Autowired
    @Lazy
	AmzAdvReportMtericsMapper amzAdvReportMtericsMapper;
    @Autowired
    @Lazy
	IAmzAdvStoresService amzAdvStoresService;
    @Autowired
    @Lazy
	IAmzAdvBrandService amzAdvBrandService;
    @Autowired
    @Lazy
	IAmzAdvAssetsService amzAdvAssetsService;
    @Autowired
    @Lazy
	IAmzAdvPortfoliosService amzAdvPortfoliosService;
    @Autowired
    @Lazy
    IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
    @Autowired
    @Lazy
    IAmzAdvKeywordsService amzAdvKeywordsService;
    @Autowired
    @Lazy
    AmzAdvProfileMapper amzAdvProfileMapper;
	public static class AdvSegment {
		public static final String QuerySegment = "query";
		public static final String PlacementSegment = "placement";
	}
 

	public static class AdvRecordType {
		public static final String keywords = "keywords";
		public static final String campaigns = "campaigns";
		public static final String productAds = "productAds";
		public static final String adGroups = "adGroups";
		public static final String campaignNegativeKeywords = "campaignNegativeKeywords";
		public static final String negativeKeywords = "negativeKeywords";
		public static final String asins = "asins";
		public static final String targets = "targets";
		public static final String negativeTargets = "negativeTargets";
	}

	public static class AdvResponseRecordType {
		public static final String keyword = "keyword";
		public static final String campaign = "campaign";
		public static final String productAd = "productAd";
		public static final String adGroup = "adGroup";
		public static final String campaignNegativeKeyword = "campaignNegativeKeyword";
		public static final String negativeKeyword = "negativeKeyword";
		public static final String target = "targets";
		public static final String negativeTarget = "negativeTargets";
		public static final String otherAsin = "otherAsin";
		public static final String headlineSearch = "headlineSearch";
		public static int threadnumber = 0;
	}
	
	public static Map<String, String> advRecordTypeMap = new HashMap<String, String>();
	public static String getAdvResponseRecordType(String recordType){
		if(advRecordTypeMap.get(recordType)==null){
			advRecordTypeMap.put("keywords","keyword");
			advRecordTypeMap.put("campaigns","campaign");
			advRecordTypeMap.put("productAds","productAd");
			advRecordTypeMap.put("adGroups","adGroup");
			advRecordTypeMap.put("campaignNegativeKeywords","campaignNegativeKeyword");
			advRecordTypeMap.put("negativeKeywords","negativeKeyword");
			advRecordTypeMap.put("targets","targets");
			advRecordTypeMap.put("negativeTargets","negativeTargets");
		}
		if(advRecordTypeMap.get(recordType)==null) {
			return recordType;
		}
		return advRecordTypeMap.get(recordType);
	}
 
	 
	public void requestSnapshot(String recordType, String campaignType) {//线程数量=20*9
		List<AmzAdvAuth> advauthlist = amzAdvAuthService.selectAll(null);
		if(advauthlist==null || advauthlist.size()==0){
			return;
		}
		List<Map<String, Object>> paramList = new ArrayList<Map<String,Object>>();
		for (AmzAdvAuth advauth : advauthlist) {
			if (advauth.getDisable()) {
				continue;
			}
			if ("TT".equals(advauth.getRegion())) {
				continue;
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("campaignType", campaignType);
			param.put("advauth", advauth);
			param.put("recordType", recordType);
			paramList.add(param);
		}
		List<Runnable> runnables = new ArrayList<Runnable>();
		if(paramList.size() > 10) {
			List<List<Map<String, Object>>> acerageList = GeneralUtil.averageAssign(paramList, 20);
			for(List<Map<String, Object>> runnableList : acerageList) {
				runnables.add(requestSnapshotThread(runnableList));
			}
		} else if(paramList.size() > 0){
			runnables.add(requestSnapshotThread(paramList));
		}
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv(runnables, "requestSnapshot"+recordType+campaignType);
		}
	}

	public Runnable requestSnapshotThread(final List<Map<String, Object>> list) { 
		return new Runnable() {
			public void run() {
				for(Map<String, Object> map : list) {
					requestSnapshot(map);
				}
			}
		};
	}

	public void requestSnapshot(Map<String, Object> parammap) { 
		String recordType = (String) parammap.get("recordType");
		String campaignType = (String) parammap.get("campaignType");
		String url = "/" + campaignType + "/" + recordType + "/snapshot";
		AmzAdvAuth advauth = parammap.get("advauth") == null ? null : (AmzAdvAuth)parammap.get("advauth"); 
		List<AmzAdvProfile> profiles = amzAdvAuthService.getAmzAdvProfile(advauth);
		for (AmzAdvProfile profile : profiles) {
			if(profile.isError()) {
				continue;
			}
			if("agency".equals(profile.getType())){
				continue;
			}
			if ("vendor".equals(profile.getType())
					&& (AdvRecordType.productAds.equals(recordType) || AdvRecordType.asins.equals(recordType))) {
				continue;
			}
			String advResponseRecordType = getAdvResponseRecordType(recordType);
			AmzAdvSnapshot snapshot = amzAdvSnapshotMapper.selectByProfileAndType(profile.getId().toString(),advResponseRecordType, campaignType);
			if (snapshot != null&&(snapshot.getTreatstatus()==null||!"expired".equals(snapshot.getTreatstatus()))) {// 今天存在请求记录，则不再请求
				continue;
			}
			JSONObject map = new JSONObject();
			map.put("stateFilter", "enabled,paused,archived");
			if("SD".equalsIgnoreCase(campaignType)) {
				map.put("tacticFilter", "T00020,T00030");
			}
			String response=null;
			try {
				if(campaignType.equals(CampaignType.sd)) {
					response = amzAdvAuthService.amzAdvPost_V3(profile, url, map.toJSONString());
				}else {
					response = amzAdvAuthService.amzAdvPost(profile, url, map.toJSONString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				//无法获取所在站点权限
			}
			if (StringUtil.isNotEmpty(response)) {
				JSONObject item = GeneralUtil.getJsonObject(response);
				if(!advResponseRecordType.equals(item.getString("recordType"))){
					advRecordTypeMap.put(recordType, item.getString("recordType"));
				}
				AmzAdvSnapshot record = new AmzAdvSnapshot();
				record.setRequesttime(new Date());
				record.setOpttime(new Date());
				record.setProfileid(profile.getId());
				record.setRegion(advauth.getRegion());
				record.setSnapshotid(item.getString("snapshotId"));
				record.setStatus(item.getString("status"));
				record.setRecordtype(item.getString("recordType"));
				record.setCampaigntype(campaignType);
				amzAdvSnapshotMapper.insert(record);
			}
		}
	}

    public void addRunSnapshot(List<AmzAdvSnapshot> list,List<Runnable> runnables) {
    	List<Runnable> runs = getReadSnapShotRunnable(list);
    	if(runs!=null&&runs.size()>0) {
			runnables.addAll(runs);
		}
    }
	public void readSnapshot() {
		try {
			List<AmzAdvSnapshot> list1 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvResponseRecordType.campaign,
					CampaignType.sp);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list1),
					"readSnapshot" + AdvResponseRecordType.campaign + CampaignType.sp);
			
			List<AmzAdvSnapshot> list2 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvResponseRecordType.campaign,
					CampaignType.hsa);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list2),
					"readSnapshot" + AdvResponseRecordType.campaign + CampaignType.hsa);
			
			List<AmzAdvSnapshot> list3 = amzAdvSnapshotMapper
					.selectAvailableByAdType(AdvResponseRecordType.negativeKeyword, CampaignType.sp);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list3),
					"readSnapshot" + AdvResponseRecordType.negativeKeyword + CampaignType.sp);
			
			List<AmzAdvSnapshot> list4 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvResponseRecordType.keyword,
					CampaignType.sp);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list4),
					"readSnapshot" + AdvResponseRecordType.keyword + CampaignType.sp);
			
			List<AmzAdvSnapshot> list5 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvResponseRecordType.keyword,
					CampaignType.hsa);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list5),
					"readSnapshot" + AdvResponseRecordType.keyword + CampaignType.hsa);
			
			List<AmzAdvSnapshot> list6 = amzAdvSnapshotMapper
					.selectAvailableByAdType(AdvResponseRecordType.campaignNegativeKeyword, CampaignType.sp);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list6),
					
					"readSnapshot" + AdvResponseRecordType.campaignNegativeKeyword + CampaignType.sp);
			List<AmzAdvSnapshot> list7 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvResponseRecordType.adGroup,
					CampaignType.sp);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list7),
					"readSnapshot" + AdvResponseRecordType.adGroup + CampaignType.sp);
			
			List<AmzAdvSnapshot> list8 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvResponseRecordType.productAd,
					CampaignType.sp);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list8),
					"readSnapshot" + AdvResponseRecordType.productAd + CampaignType.sp);
			
			List<AmzAdvSnapshot> list9 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvResponseRecordType.target,
					CampaignType.sp);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list9),
					"readSnapshot" + AdvResponseRecordType.target + CampaignType.sp);
			
			List<AmzAdvSnapshot> list14 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvRecordType.negativeTargets,
					CampaignType.sp);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list14),
					"readSnapshot" + AdvRecordType.negativeTargets + CampaignType.sp);
			
			List<AmzAdvSnapshot> list10 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvRecordType.campaigns,
					CampaignType.sd);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list10),
					"readSnapshot" + AdvRecordType.campaigns + CampaignType.sd);
			
			List<AmzAdvSnapshot> list11 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvRecordType.adGroups,
					CampaignType.sd);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list11),
					"readSnapshot" + AdvRecordType.adGroups + CampaignType.sd);
			
			List<AmzAdvSnapshot> list12 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvRecordType.productAds,
					CampaignType.sd);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list12),
					"readSnapshot" + AdvRecordType.productAds + CampaignType.sd);
			
			List<AmzAdvSnapshot> list13 = amzAdvSnapshotMapper.selectAvailableByAdType(AdvRecordType.targets,
					CampaignType.sd);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list13),
					"readSnapshot" + AdvRecordType.targets + CampaignType.sd);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readSnapshot(String id) {
		AmzAdvSnapshot sn = amzAdvSnapshotMapper.selectByPrimaryKey(id);
		AmzAdvProfile advProfile = amzAdvAuthService.getAmzAdvProfileByKey(sn.getProfileid());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("amzsnap", sn);
		param.put("advProfile", advProfile);
		readSnapshot(param);
	}
	
	public List<Runnable>  getReadSnapShotRunnable(List<AmzAdvSnapshot> list) {
		if(list==null || list.size()==0){
			return null;
		}
		List<Map<String, Object>> paramList = new ArrayList<Map<String,Object>>();
		for (AmzAdvSnapshot amzsnap : list) {
			AmzAdvProfile advProfile = amzAdvAuthService.getAmzAdvProfileByKey(amzsnap.getProfileid());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("amzsnap", amzsnap);
			param.put("advProfile", advProfile);
			paramList.add(param);
		}
	    return readSnapShotThread(paramList);
	}
	
	public int readSnapshot(String recordType, String campaignType) { 
		List<AmzAdvSnapshot> list = amzAdvSnapshotMapper.selectAvailableByAdType(recordType, campaignType);
		if(list==null || list.size()==0){
			return 0;
		}
		List<Map<String, Object>> paramList = new ArrayList<Map<String,Object>>();
		for (AmzAdvSnapshot amzsnap : list) {
			AmzAdvProfile advProfile = amzAdvAuthService.getAmzAdvProfileByKey(amzsnap.getProfileid());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("amzsnap", amzsnap);
			param.put("advProfile", advProfile);
			paramList.add(param);
		}
		List<Runnable> runnables = new ArrayList<Runnable>();
		if(paramList.size() > 10) {
			List<List<Map<String, Object>>> acerageList = GeneralUtil.averageAssign(paramList, 20);
			for(List<Map<String, Object>> runnableList : acerageList) {
				runnables.addAll(readSnapShotThread(runnableList));
			}
		} else if(paramList.size() > 0) {
			runnables.addAll(readSnapShotThread(paramList));
		}
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv( runnables,"readSnapshot");
		}
		return paramList.size();
	}

	public 	List<Runnable> readSnapShotThread(final List<Map<String, Object>> list) {
		List<Runnable> runs=new ArrayList<Runnable>();
		for(Map<String, Object> map : list) {
			runs.add(new Runnable() {
			           public void run() {
				             readSnapshot(map);
				        }
			   });  
		}
		return runs;
	}

	public void readSnapshot(Map<String, Object> map) { 
		AmzAdvSnapshot amzsnap = (AmzAdvSnapshot) map.get("amzsnap");
		amzsnap.setTreatnumber(amzsnap.getTreatnumber()+1);
		amzsnap.setOpttime(new Date());
		AmzAdvProfile advProfile = (AmzAdvProfile) map.get("advProfile");
		if(amzsnap.getStatus()==null||!"SUCCESS".equals(amzsnap.getStatus())) {
			String url = "/snapshots/" + amzsnap.getSnapshotid();
			String response=null;
			try {
				if(amzAdvAuthService.isBusy()) {return;}
				if(amzsnap.getCampaigntype().equals(CampaignType.sd)) {
					response = amzAdvAuthService.amzAdvGet_V3(advProfile, "/sd"+url);
				}else {
				    response = amzAdvAuthService.amzAdvGet(advProfile, url);
				}
			} catch (BaseException e) {
				if (BaseException.AmazonBusy.equals(e.getCode())) {
					throw new BaseException(e.getMessage());
				}
			   if(e.getMessage().equals("Snapshot is expired")) {
				   amzsnap.setTreatstatus("expired");
				   amzsnap.setOpttime(new Date());
				   amzsnap.setTreatnumber(1);
				}else {
					amzsnap.setTreatstatus("error");
				    amzsnap.setOpttime(new Date());
					amzsnap.setLog("获取快照异常，"+e.getMessage());
				}
				e.printStackTrace();
				amzAdvSnapshotMapper.updateByPrimaryKeySelective(amzsnap);
			}
			if (StringUtil.isNotEmpty(response)) {
				JSONObject item = GeneralUtil.getJsonObject(response);
				Integer fileSize = item.getInteger("fileSize");
				amzsnap.setSnapshotid(item.getString("snapshotId"));
				amzsnap.setStatus(item.getString("status"));
				amzsnap.setLocation(item.getString("location"));
				amzsnap.setFilesize(fileSize);
				if ("SUCCESS".equals(amzsnap.getStatus())) {
					if (fileSize <= 22) {
						amzsnap.setTreatstatus("success");
						amzsnap.setLog("no date");
						amzAdvSnapshotMapper.updateByPrimaryKeySelective(amzsnap);
					} else {
						amzsnap.setTreatstatus("");
						amzsnap.setLog("");
						if (treatAmzAdvSnapshot(amzsnap, advProfile)) {
							amzAdvSnapshotMapper.updateByPrimaryKeySelective(amzsnap);
						}else {
							amzAdvSnapshotMapper.updateByPrimaryKeySelective(amzsnap);
						}
						Example example=new Example(AmzAdvSnapshot.class);
						Criteria crit = example.createCriteria();
						crit.andEqualTo("profileid", amzsnap.getProfileid());
						crit.andEqualTo("recordtype", amzsnap.getRecordtype());
						crit.andEqualTo("campaigntype", amzsnap.getCampaigntype());
						crit.andEqualTo("region", amzsnap.getRegion());
						crit.andLessThan("requesttime", amzsnap.getRequesttime());
						amzAdvSnapshotMapper.deleteByExample(example);
					}
				} else if("FAILURE".equals(amzsnap.getStatus())){
					amzsnap.setTreatstatus("failed");
					amzAdvSnapshotMapper.updateByPrimaryKeySelective(amzsnap);
				}
			}
		}else {
					if (amzsnap.getFilesize() <= 22) {
						amzsnap.setTreatstatus("success");
						amzsnap.setLog("no date");
						amzAdvSnapshotMapper.updateByPrimaryKeySelective(amzsnap);
					} else {
						amzsnap.setTreatstatus("");
						amzsnap.setLog("");
						if (treatAmzAdvSnapshot(amzsnap, advProfile)) {
							amzAdvSnapshotMapper.updateByPrimaryKeySelective(amzsnap);
						}else {
							amzAdvSnapshotMapper.updateByPrimaryKeySelective(amzsnap);
						}
						Example example=new Example(AmzAdvSnapshot.class);
						Criteria crit = example.createCriteria();
						crit.andEqualTo("profileid", amzsnap.getProfileid());
						crit.andEqualTo("recordtype", amzsnap.getRecordtype());
						crit.andEqualTo("campaigntype", amzsnap.getCampaigntype());
						crit.andEqualTo("region", amzsnap.getRegion());
						crit.andLessThan("requesttime", amzsnap.getRequesttime());
						amzAdvSnapshotMapper.deleteByExample(example);
					}
		}
	
	}

	private boolean treatAmzAdvSnapshot(AmzAdvSnapshot record, AmzAdvProfile advProfile) {
		String log="";
		String responseJson=null;
		try {
			if(CampaignType.sd.equals(record.getCampaigntype())) {
				responseJson = amzAdvAuthService.amzAdvDownloadSD(advProfile, record.getLocation(),record.getSnapshotid());
			}else {
				responseJson = amzAdvAuthService.amzAdvDownload(advProfile, record.getLocation());
			}
		} catch (BaseException e1) {
			if (BaseException.AmazonBusy.equals(e1.getCode())) {
				throw new BaseException(e1.getMessage());
			}
			if(BaseException.Expired.equals(e1.getCode())) {
				record.setTreatstatus(BaseException.Expired);
				amzAdvSnapshotMapper.updateByPrimaryKey(record);
			}
			e1.printStackTrace();
			log=e1.getMessage();
		}
		if (StringUtil.isNotEmpty(responseJson) && !"[]".equals(responseJson)) {
			JSONArray a = GeneralUtil.getJsonArray(responseJson);
			if (a == null) {
				record.setLog("response转换JsonArray异常");
				record.setTreatstatus("error");
				return true;
			}
			try {
				if(CampaignType.sd.equals(record.getCampaigntype())) {
					if (AdvRecordType.campaigns.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvCampaignsSD(record, a);
					} else if (AdvRecordType.adGroups.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvAdgroupsSD(record, a);
					} else if (AdvRecordType.productAds.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductadsSD(record, a);
					} else if (AdvRecordType.targets.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductTargetSD(record, a);
					} else if (AdvRecordType.negativeKeywords.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductTargetNegativaSD(record, a);
					}
				 }else if (CampaignType.hsa.equals(record.getCampaigntype())) {
					if (AdvResponseRecordType.campaign.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvCampaignsHsa(record, a);
					} else if (AdvResponseRecordType.adGroup.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvAdgroups(record, a);
					} else if (AdvResponseRecordType.keyword.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvKeywords(record, a);
					} else if (AdvResponseRecordType.productAd.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductads(record, a);
					} else if (AdvResponseRecordType.negativeKeyword.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvKeywordsNegativa(record, a);
					} else if (AdvResponseRecordType.campaignNegativeKeyword.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvCampKeywordsNegativa(record, a);
					} else if (AdvResponseRecordType.target.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductTarget(record, a);
					} else if (AdvResponseRecordType.negativeTarget.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductTargetNegativa(record, a);
					}
				}else {
					if (AdvResponseRecordType.campaign.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvCampaigns(record, a);
					} else if (AdvResponseRecordType.adGroup.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvAdgroups(record, a);
					} else if (AdvResponseRecordType.keyword.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvKeywords(record, a);
					} else if (AdvResponseRecordType.productAd.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductads(record, a);
					} else if (AdvResponseRecordType.negativeKeyword.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvKeywordsNegativa(record, a);
					} else if (AdvResponseRecordType.campaignNegativeKeyword.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvCampKeywordsNegativa(record, a);
					} else if (AdvResponseRecordType.target.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductTarget(record, a);
					} else if (AdvResponseRecordType.negativeTarget.equals(record.getRecordtype())) {
						amzAdvReportTreatService.treatAdvProductTargetNegativa(record, a);
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log="处理异常，"+e.getMessage();
			}
		} else {
			return false;
		}
		record.setLog(log);
		if(log.contains("异常")){
			record.setTreatstatus("error");
		} else {
			record.setTreatstatus("success");
		}
		return true;
	}

 

 
	public void requestSnapshotByProfile(AmzAdvProfile profile, String recordType, String campaignType, AmzAdvAuth advauth) {
		String url = "/" + campaignType + "/" + recordType + "/snapshot";
		JSONObject map = new JSONObject();
		map.put("stateFilter", "enabled,paused,archived");
		String response = amzAdvAuthService.amzAdvPost(profile, url, map.toJSONString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response);
			AmzAdvSnapshot record = new AmzAdvSnapshot();
			record.setRequesttime(new Date());
			record.setOpttime(new Date());
			record.setProfileid(profile.getId());
			record.setRegion(profile.getAdvAuth().getRegion());
			record.setSnapshotid(item.getString("snapshotId"));
			record.setStatus(item.getString("status"));
			record.setRecordtype(item.getString("recordType"));
			record.setCampaigntype(campaignType);
			amzAdvSnapshotMapper.insert(record);
//			if ("SUCCESS".equals(record.getStatus())) {
//				treatAmzAdvSnapshot(record, profile);
//			}
		}
	}
	

	public void requestSnapshotByProfile(AmzAdvProfile profiles, AmzAdvAuth advauth) {
		if("agency".equals(profiles.getType())){
			return;
		}
		requestSnapshotByProfile(profiles, AdvRecordType.campaigns, CampaignType.sp, advauth);
		requestSnapshotByProfile(profiles, AdvRecordType.campaigns, CampaignType.hsa, advauth);
		requestSnapshotByProfile(profiles, AdvRecordType.adGroups, CampaignType.sp, advauth);
		requestSnapshotByProfile(profiles, AdvRecordType.keywords, CampaignType.sp, advauth);
		requestSnapshotByProfile(profiles, AdvRecordType.keywords, CampaignType.hsa, advauth);
		if (!"vendor".equals(profiles.getType())) {
			requestSnapshotByProfile(profiles, AdvRecordType.productAds, CampaignType.sp, advauth);
		}
		requestSnapshotByProfile(profiles, AdvRecordType.negativeKeywords, CampaignType.sp, advauth);
		requestSnapshotByProfile(profiles, AdvRecordType.campaignNegativeKeywords, CampaignType.sp, advauth);
		requestSnapshotByProfile(profiles, AdvRecordType.targets, CampaignType.sp, advauth);
	}

	public void requestStoreBrand(AmzAdvProfile amzAdvProfile) {
		if(amzAdvProfile==null||"agency".equals(amzAdvProfile.getType())) {
			return;
		}
		Map<String, Object> portfoliosParam = new HashMap<String, Object>();
		portfoliosParam.put("portfolioStateFilter", "enabled");
		BigInteger pro = amzAdvProfile.getId();
		amzAdvStoresService.amzGetlistStores(null, pro);
		List<AmzAdvBrand> list = amzAdvBrandService.amzGetBrands(null, pro);
		if (list != null) {
			for (int j = 0; j < list.size(); j++) {
				AmzAdvBrand brand = list.get(j);
				amzAdvAssetsService.amzGetStoresAssets(null, brand.getProfileid(), brand.getBrandentityid(), "brandLogo");
			}
		}
		amzAdvPortfoliosService.amzGetListPortfolios(null, pro, portfoliosParam);
	}
	   

	public void requestHsaVideoSnapshot() {
		// TODO Auto-generated method stub
		List<AmzAdvAuth> advauthlist =null;
		Example example=new Example(AmzAdvAuth.class);
        Criteria crit = example.createCriteria();
		crit.andEqualTo("disable", false);
		advauthlist=amzAdvAuthService.selectByExample(example);
		if(advauthlist==null || advauthlist.size()==0){
			return;
		}
		List<Runnable> runnables = new ArrayList<Runnable>();
		if(advauthlist.size() > 10) {
			List<List<AmzAdvAuth>> acerageList = GeneralUtil.averageAssign(advauthlist, 20);
			for(List<AmzAdvAuth> runnableList : acerageList) {
				runnables.add(requestHsaVideoSnapshotThread(runnableList));
			}
		} else if(advauthlist.size() > 0){
			runnables.add(requestHsaVideoSnapshotThread(advauthlist));
		}
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv( runnables, "requestHsaVideoSnapshot");
		}
	}

	private Runnable requestHsaVideoSnapshotThread(final List<AmzAdvAuth> runnableList) {
		// TODO Auto-generated method stub
		return new Runnable() {
			public void run() {
				for(AmzAdvAuth map : runnableList) {
					requestHsaVideoSnapshotThread(map);
				}
			}

		
		};
	}
	public void requestHsaVideoSnapshotThread(AmzAdvAuth map) {
		// TODO Auto-generated method stub
		List<AmzAdvProfile> profilelist = amzAdvAuthService.getProfileByAuth(map.getId());
		for(AmzAdvProfile profile:profilelist) {
			if("agency".equals(profile.getType())) {
				continue;
			}
		
			Date olddate=	amzAdvCampaignHsaService.getOldDateSBCampaigns(profile);
			if(olddate!=null) {
				double day = GeneralUtil.distanceOfDay(olddate, new Date());
				if(day>40||day<=1) {
					continue;
				}
			}
			HashMap<String,Object> campaignsParam=new HashMap<String,Object>();
			campaignsParam.put("creativeType", "video");
			campaignsParam.put("count", 10);
			campaignsParam.put("startIndex", 0);
			try {
				amzAdvCampaignHsaService.amzListSBCampaigns(profile,campaignsParam);	
			}catch (Exception e) {
				e.printStackTrace();
				//无法获取所在站点权限
			}
			try {
				amzAdvKeywordsService.amzListSBKeywords(profile,campaignsParam);
			}catch (Exception e) {
				e.printStackTrace();
				//无法获取所在站点权限
			}
		}
	}

}
