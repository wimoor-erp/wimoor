package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.pojo.*;
import com.wimoor.amazon.adv.common.service.*;
import com.wimoor.amazon.adv.exports.service.IAmzAdvExportsHandlerService;
import com.wimoor.amazon.adv.report.dao.AmzAdvReportRequestTypeMapper;
import com.wimoor.amazon.adv.report.dao.AmzAdvRequestMapper;
import com.wimoor.amazon.adv.report.dao.AmzAdvSnapshotMapper;
import com.wimoor.amazon.adv.report.dao.AmzAdvSumAllTypeMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.exports.service.IAmzAdvSnapshotHandlerApiService;
import com.wimoor.amazon.adv.exports.service.IAmzAdvSnapshotHandlerService;
import com.wimoor.amazon.adv.exports.service.IAmzAdvSnapshotTreatService;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllTypeKey;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.common.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;
import com.alibaba.fastjson.JSONReader;
import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/*@Slf4j*/
@Service("amzAdvSnapshotHandlerService")
public class AmzAdvSnapshotHandlerServiceImpl implements IAmzAdvSnapshotHandlerService {
    @Value("${spring.profiles.active}")
    String profile;
    
    @Value("${spring.application.name}")
    String server;
    
    @Autowired
    @Lazy
	IAmzAdvAuthService amzAdvAuthService;
    @Autowired
    @Lazy
	AmzAdvRequestMapper amzAdvRequestMapper;
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
	AmzAdvReportRequestTypeMapper amzAdvReportMtericsMapper;
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
    @Resource
    IAmzAdvSnapshotHandlerApiService amzAdvSnapshotHandlerApiService;
    @Autowired
    @Lazy
    AmzAdvProfileMapper amzAdvProfileMapper;
    @Resource
    ApiBuildService apiBuildService;
	@Resource
	IAmzAdvExportsHandlerService amzAdvExportsAdsHandlerService;
	@Resource
	IAmzAdvExportsHandlerService amzAdvExportsAdGroupsHandlerService;
	@Resource
	IAmzAdvExportsHandlerService amzAdvExportsCampaignsHandlerService;
	@Resource
	IAmzAdvExportsHandlerService amzAdvExportsTargetsHandlerService;
    @Resource
	AmzAdvSumAllTypeMapper amzAdvSumAllTypeMapper;
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

	public void requestSnapshot(String exportType, String campaignType) {//线程数量=20*9
		List<AmzAdvAuth> advauthlist = amzAdvAuthService.selectLastAuthList();
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
			param.put("exportType", exportType);
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
			AdvUtils.executThreadForAdv(runnables, "requestSnapshot"+exportType+campaignType);
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
		String exportType = (String) parammap.get("exportType");
		String url = "/" + exportType + "/export";
		AmzAdvAuth advauth = parammap.get("advauth") == null ? null : (AmzAdvAuth)parammap.get("advauth"); 
		List<AmzAdvProfile> profiles = amzAdvAuthService.getAmzAdvProfile(advauth);
		for (AmzAdvProfile profile : profiles) {
			if(profile.isError()) {
				continue;
			}
			if("agency".equals(profile.getType())){
				continue;
			}
			Example example=new Example(AmzAdvSnapshot.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profile.getId());
			crit.andEqualTo("recordtype", exportType);
			crit.andEqualTo("campaigntype", "all");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR, -6);
			crit.andGreaterThan("requesttime", calendar.getTime());
			int count = amzAdvSnapshotMapper.selectCountByExample(example);
			if(count>0){
				continue;
			}
			JSONObject map = new JSONObject();
			List<String> stateList= new ArrayList<String>();
			stateList.add("ENABLED");
			stateList.add("PAUSED");
			stateList.add("ARCHIVED");
			map.put("stateFilter", stateList);
			List<String> adProductList= new ArrayList<String>();
			adProductList.add("SPONSORED_PRODUCTS");
			adProductList.add("SPONSORED_BRANDS");
			adProductList.add("SPONSORED_DISPLAY");
			map.put("adProductFilter",adProductList);
			String response=null;
			try {
				response = apiBuildService.amzAdvPost(profile, url, map.toJSONString(),getHeaderExt(exportType));
			} catch (Exception e) {
				e.printStackTrace();
				//无法获取所在站点权限
			}
			if (StringUtil.isNotEmpty(response)) {
				JSONObject item = GeneralUtil.getJsonObject(response);
				AmzAdvSnapshot record = new AmzAdvSnapshot();
				record.setRequesttime(new Date());
				record.setOpttime(new Date());
				record.setProfileid(profile.getId());
				record.setRegion(advauth.getRegion());
				record.setSnapshotid(item.getString("exportId"));
				record.setStatus(item.getString("status"));
				record.setRecordtype(exportType);
				record.setCampaigntype("all");
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
			List<AmzAdvSnapshot> list1 = amzAdvSnapshotMapper.selectAvailableByAdType( null,
					CampaignType.all);
			AdvUtils.executThreadForAdv( getReadSnapShotRunnable(list1),
					"readSnapshot" + AdvResponseRecordType.campaign + CampaignType.all);
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

	public Map<String,String> getHeaderExt(String recordType){
		Map<String,String> header= new HashMap<String,String>();
		if(recordType.equals(AdvExportType.campaigns)){
			header.put("Accept", "application/vnd.campaignsexport.v1+json");
			header.put("Content-Type", "application/vnd.campaignsexport.v1+json");
		}else if(recordType.equals(AdvExportType.adGroups)){
			header.put("Accept", "application/vnd.adgroupsexport.v1+json");
			header.put("Content-Type", "application/vnd.adgroupsexport.v1+json");
		}else if(recordType.equals(AdvExportType.ads)){
			header.put("Accept", "application/vnd.adsexport.v1+json");
			header.put("Content-Type", "application/vnd.adsexport.v1+json");
		}else if(recordType.equals(AdvExportType.targets)){
			header.put("Accept", "application/vnd.targetsexport.v1+json");
			header.put("Content-Type", "application/vnd.targetsexport.v1+json");
		}
		return header;
	}

	public void readSnapshot(Map<String, Object> map) { 
		AmzAdvSnapshot amzsnap = (AmzAdvSnapshot) map.get("amzsnap");
		amzsnap.setTreatnumber(amzsnap.getTreatnumber()+1);
		amzsnap.setOpttime(new Date());
		AmzAdvProfile advProfile = (AmzAdvProfile) map.get("advProfile");
		if(amzsnap.getStatus()==null||!"COMPLETED".equals(amzsnap.getStatus())) {
			String url = "/exports/" + amzsnap.getSnapshotid();
			String response=null;
			try {
				if(apiBuildService.isBusy()) {return;}
				response = apiBuildService.amzAdvGet(advProfile, url,getHeaderExt(amzsnap.getRecordtype()));
			} catch (BaseException e) {
				if (BaseException.AmazonBusy.equals(e.getCode())) {
					throw new BaseException(e.getMessage());
				}
			   if(e.getMessage().equals("Exports is expired")) {
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
				amzsnap.setSnapshotid(item.getString("exportId"));
				amzsnap.setStatus(item.getString("status"));
				amzsnap.setLocation(item.getString("url"));
				amzsnap.setFilesize(fileSize);
				if ("COMPLETED".equals(amzsnap.getStatus())) {
					if (fileSize <= 22) {
						amzsnap.setTreatstatus("success");
						amzsnap.setLog("no data");
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
						amzsnap.setLog("no data");
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
			  apiBuildService.amzAdvDownloadGZIP(advProfile, record,this);
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
		profile.setAdvAuth(advauth);
		String response = apiBuildService.amzAdvPost_V2(profile, url, map.toJSONString());
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

	public void requestStoreBrand() {
		   List<Runnable> runnables = new ArrayList<Runnable>();
			List<AmzAdvAuth>  advauthlist=amzAdvAuthService.list();
			if(advauthlist==null || advauthlist.size()==0){
				return;
			}
	    	for (AmzAdvAuth advauth : advauthlist) {
	    		if (advauth.getDisable() || advauth.getRegion().equals("TT")||(advauth.getDisableTime()!=null&&GeneralUtil.distanceOfHour(advauth.getDisableTime(), new Date())<3)) {
	    			continue;
	    		}
	    		List<AmzAdvProfile> profiles = amzAdvAuthService.getAmzAdvProfile(advauth);
	    		for (AmzAdvProfile profile : profiles) {
	    			runnables.add(new Runnable() {
	    				public void run() {
	    					try{
								requestStoreBrand(profile);
							}catch (Exception e){
								e.printStackTrace();
							}
	    				}
	    			
	    			});
	    		}
	    	}
			if(runnables.size()>0){
				AdvUtils.executThreadForAdv(runnables);
			}
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
	   
	@Scheduled(cron = "0 0/3 * * * ?")
	public void requestApiSnapshot() {
		// TODO Auto-generated method stub
	    if(!"prod".equals(profile)) {return;}
		List<AmzAdvAuth> advauthlist = amzAdvAuthService.list();
		if(advauthlist==null || advauthlist.size()==0){
			return;
		}
		List<Runnable> runnables = new ArrayList<Runnable>();
		if(advauthlist.size() > 10) {
			List<List<AmzAdvAuth>> acerageList = GeneralUtil.averageAssign(advauthlist, 20);
			for(List<AmzAdvAuth> runnableList : acerageList) {
				runnables.add(requestApiSnapshotThread(runnableList));
			}
		} else if(advauthlist.size() > 0){
			runnables.add(requestApiSnapshotThread(advauthlist));
		}
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv( runnables, "requestHsaVideoSnapshot");
		}
	}

	private Runnable requestApiSnapshotThread(final List<AmzAdvAuth> runnableList) {
		// TODO Auto-generated method stub
		return new Runnable() {
			public void run() {
				for(AmzAdvAuth map : runnableList) {
					try {
						if(map.getDisableTime()!=null&&GeneralUtil.distanceOfHour(map.getDisableTime(), new Date())<1) {
							continue;
						}
						requestApiSnapshotThread(map);
						
					}catch(Exception e) {
                            map.setDisableTime(new Date());
                            
					}
				}
			}

		
		};
	}
	
	
	public void requestApiCampBudgetRuleSnapshot() {
		// TODO Auto-generated method stub
	    if(!"prod".equals(profile)) {return;}
		List<AmzAdvAuth> advauthlist = amzAdvAuthService.list();
		if(advauthlist==null || advauthlist.size()==0){
			return;
		}
		List<Runnable> runnables = new ArrayList<Runnable>();
		if(advauthlist.size() > 10) {
			List<List<AmzAdvAuth>> acerageList = GeneralUtil.averageAssign(advauthlist, 20);
			for(List<AmzAdvAuth> runnableList : acerageList) {
				runnables.add(requestApiCampBudgetRuleSnapshotThread(runnableList));
			}
		} else if(advauthlist.size() > 0){
			runnables.add(requestApiCampBudgetRuleSnapshotThread(advauthlist));
		}
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv( runnables, "requestHsaVideoSnapshot");
		}
	}

	@Override
	public boolean readSnapshotJSON(AmzAdvProfile profile, AmzAdvSnapshot record, JSONReader jsonReader) {
		try {
				jsonReader.startArray();
			    AmzAdvSumAllType typeSum=new AmzAdvSumAllType();
				typeSum.setOpttime(new Date());
				typeSum.setProfileid(profile.getId());
				typeSum.setByday(GeneralUtil.Dateformatter(record.getRequesttime(),"yyyy-MM-dd") );
				typeSum.setCampaigntype(record.getCampaigntype());
				typeSum.setRecordtype(record.getRecordtype());
				typeSum.setState("enabled");
			    int num=0;
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					if( AdvExportType.campaigns.equals(record.getRecordtype())) {
						amzAdvExportsCampaignsHandlerService.treatJSON(record, item);
					}else if (AdvExportType.ads.equals(record.getRecordtype())) {
						amzAdvExportsAdsHandlerService.treatJSON(record, item);
					}else if (AdvExportType.adGroups.equals(record.getRecordtype())) {
						amzAdvExportsAdGroupsHandlerService.treatJSON(record, item);
					}else{
						amzAdvExportsTargetsHandlerService.treatJSON(record, item);
					}
					String state=item.getString("state");
					if(state!=null&&state.equalsIgnoreCase("enabled")){
						num++;
					}
				}
			     typeSum.setQuantity(num);
			    AmzAdvSumAllType old = amzAdvSumAllTypeMapper.selectByKey(typeSum);
				if(old!=null){
					amzAdvSumAllTypeMapper.updateByPrimaryKey(typeSum);
				}else{
					amzAdvSumAllTypeMapper.insert(typeSum);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		return true;
	}


	public Runnable requestApiCampBudgetRuleSnapshotThread(List<AmzAdvAuth> runnableList) {
		return new Runnable() {
			public void run() {
				for(AmzAdvAuth map : runnableList) {
					try {
						if(map.getDisableTime()!=null&&GeneralUtil.distanceOfHour(map.getDisableTime(), new Date())<1) {
							continue;
						}
						List<AmzAdvProfile> profilelist = amzAdvAuthService.getProfileByAuth(map.getId());
						for(AmzAdvProfile profile:profilelist) {
							if("agency".equals(profile.getType())) {
								continue;
							}
							try {
								amzAdvSnapshotHandlerApiService.requestCampBudgetRule(profile);
							}catch(Exception e) {
								e.printStackTrace();//全局异常收集
							}
						}
				    }catch(Exception e) {
	                        map.setDisableTime(new Date());
					}
			}
		}

	};
 }
	public void requestApiSnapshotThread(AmzAdvAuth map) {
		// TODO Auto-generated method stub
		List<AmzAdvProfile> profilelist = amzAdvAuthService.getProfileByAuth(map.getId());
		for(AmzAdvProfile profile:profilelist) {
			if("agency".equals(profile.getType())) {
				continue;
			}
			try {
				amzAdvSnapshotHandlerApiService.requestHsaTarget(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestHsaAdgroups(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestHsaCampaign(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestHsaAds(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestHsaKeyword(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestSPCampProductTargeNegative(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestSDProductTargeNegative(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestSPBudgetRule(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestSBBudgetRule(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			try {
				amzAdvSnapshotHandlerApiService.requestSDBudgetRule(profile);
			}catch(Exception e) {
				e.printStackTrace();//全局异常收集
			}
			
		}
		
	}

}
