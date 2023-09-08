package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvRequestMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvSnapshotMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvReportMtericsMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvBrand;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzAdvReportMterics;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.common.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAssetsService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBrandService;
import com.wimoor.amazon.adv.common.service.IAmzAdvPortfoliosService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportHandlerService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.common.service.IAmzAdvStoresService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.amazon.adv.utils.UUIDUtil;
import com.wimoor.common.GeneralUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;
@Slf4j
@Service("amzAdvReportHandlerService")
public class AmzAdvReportHandlerServiceImpl implements IAmzAdvReportHandlerService {
 
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvRequestMapper amzAdvRequestMapper;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatService;
	@Resource
	AmzAdvSnapshotMapper amzAdvSnapshotMapper;
	@Resource
	IAmzAdvRemindService amzAdvRemindService;
	@Resource
	AmzAdvAdgroupsMapper amzAdvAdgroupsMapper;
	@Resource
	AmzAdvReportMtericsMapper amzAdvReportMtericsMapper;
	@Resource
	IAmzAdvStoresService amzAdvStoresService;
	@Resource
	IAmzAdvBrandService amzAdvBrandService;
	@Resource
	IAmzAdvAssetsService amzAdvAssetsService;
	@Resource
	IAmzAdvPortfoliosService amzAdvPortfoliosService;
    @Resource
    IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
    @Resource
    IAmzAdvKeywordsService amzAdvKeywordsService;
    @Resource
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
 
	
	public List<AmzAdvRequest> getNeedReport() {
		List<AmzAdvRequest> list = amzAdvRequestMapper.getAllNeedTreatReport();
		return list;
	}

    
    public void readReport(String shopid) {
         if(amzAdvAuthService.isBusy()) {
       	     return;
         }
		 List<Runnable> runnables = new ArrayList<Runnable>();
			Example example=new Example(AmzAdvAuth.class);
	        Criteria crit = example.createCriteria();
			crit.andEqualTo("disable", false);
			if(StrUtil.isNotEmpty(shopid)) {
				crit.andEqualTo("shopid", shopid);
			}else {
				shopid="";
			}
			List<AmzAdvAuth> advauthlist =amzAdvAuthService.selectByExample(example);
			if(advauthlist==null || advauthlist.size()==0){
				return;
			}
			if(amzAdvAuthService.isBusy()) {
				return ;
			}
			for(AmzAdvAuth item:advauthlist) {
				if(item.getDisableTime()!=null&&GeneralUtil.distanceOfHour(item.getDisableTime(), new Date())<=1) {
					continue;
				}
				List<AmzAdvProfile> profilelist = amzAdvAuthService.getProfileByAuth(item.getId());
				for(AmzAdvProfile profile:profilelist) {
					if("agency".equals(profile.getType())||(profile.getErrorDay()!=null&&GeneralUtil.distanceOfMinutes(profile.getErrorDay(), new Date())<10)) {
						continue;
					}
				   List<AmzAdvRequest> requestlist=amzAdvRequestMapper.getNeedTreatReport(profile.getId().toString());
				   for(AmzAdvRequest request:requestlist) {
			           runnables.add(readReportThread(profile,request));
				   }
				}
			}
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv(runnables,"readReport"+shopid);
		}
	}
    



	public Runnable readReportThread(final AmzAdvProfile profile,final AmzAdvRequest request) {
		return new Runnable() {
			public void run() {
					   readReport(profile,request);
			}
		};
	}

	public void readReport(AmzAdvProfile profile,AmzAdvRequest request) {
		String response = null;
		try {
			if(request==null||request.isIsrun()==true) {
				return;
			}
			String url = "/reports/" + request.getReportid();
            response = amzAdvAuthService.amzAdvGet(profile, url);
		} catch (BaseException e) {
			if (BaseException.AmazonBusy.equals(e.getCode())) {
				throw new BaseException(e.getMessage());
			}
			//无法获取所在站点权限
			e.printStackTrace();
			request.setTreatNumber(request.getTreatNumber()+1);
			request.setTreatStatus("error");
			request.setLog(e.getMessage());
			request.setOpttime(new Date());
			request.setIsrun(false);
			amzAdvRequestMapper.updateByPrimaryKeySelective(request);
			return;
		}
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response);
			String reportId = item.getString("reportId");
			String status = item.getString("status");
			String statusDetails = item.getString("statusDetails");
			String location = item.getString("location");
			Integer fileSize = item.getInteger("fileSize");
			
			request.setReportid(reportId);
			request.setStatus(status);
			request.setStatusdetails(statusDetails);
			request.setFilesize(fileSize);
			request.setLocation(location);
			if("SUCCESS".equals(status)) {
				request.setIsrun(true);
				request.setOpttime(new Date());
				amzAdvRequestMapper.updateByPrimaryKeySelective(request);
				if (fileSize > 22) {
					request.setTreatStatus("");
					request.setLog("");
					try {
						if(amzAdvAuthService.amzAdvDownloadFile(profile, request, this)){
							request.setTreatNumber(request.getTreatNumber()+1);
							if(StringUtil.isEmpty(request.getTreatStatus())){
								request.setTreatStatus("success");
							}
						}
					} finally {
						request.setIsrun(false);
						request.setOpttime(new Date());
						if("success".equals(request.getTreatStatus())) {
							amzAdvRequestMapper.updateByPrimaryKey(request);
							//删除之前没有处理的旧数据
 
						}else {
							amzAdvRequestMapper.updateByPrimaryKeySelective(request);
						}
					}
				} else {
					request.setTreatNumber(request.getTreatNumber()+1);
					request.setTreatStatus("success");
					request.setLog("no data");
					request.setIsrun(false);
					request.setOpttime(new Date());
					amzAdvRequestMapper.updateByPrimaryKey(request);
					
			
				}
			} else if("FAILURE".equals(status)){
				request.setTreatStatus("failed");
				request.setOpttime(new Date());
				request.setIsrun(false);
				amzAdvRequestMapper.updateByPrimaryKeySelective(request);
			}
		}
	}

	public Boolean treatReport(AmzAdvProfile profile, AmzAdvRequest request, JSONReader jsonReader) {
		try {

			if (CampaignType.sp.equals(request.getCampaigntype())) {
				if (AdvResponseRecordType.campaign.equals(request.getRecordtype())) {
					if (AdvSegment.PlacementSegment.equals(request.getSegment())) {
						amzAdvReportTreatService.treatCampaignsPlaceAdvertReport(request, jsonReader);
					} else {
						amzAdvReportTreatService.treatCampaignsAdvertReport(request, jsonReader);
					}
				} else if (AdvResponseRecordType.adGroup.equals(request.getRecordtype())) {
					amzAdvReportTreatService.treatAdroupsAdvertReport(request, jsonReader);
				} else if (AdvResponseRecordType.productAd.equals(request.getRecordtype())) {
					amzAdvReportTreatService.treatProductAdvertReport(request, jsonReader);
				} else if (AdvResponseRecordType.otherAsin.equals(request.getRecordtype())) {
					amzAdvReportTreatService.treatAsinsAdvertReport(request, jsonReader);
				} else if (AdvResponseRecordType.keyword.equals(request.getRecordtype())) {
					if (AdvSegment.QuerySegment.equals(request.getSegment())) {
						amzAdvReportTreatService.treatKeywordsQuryAdvertReport(request, jsonReader);
					} else {
						amzAdvReportTreatService.treatKeywordsAdvertReport(request, jsonReader);
					}
				} else if (AdvResponseRecordType.target.equals(request.getRecordtype())) {
					if (AdvSegment.QuerySegment.equals(request.getSegment())) {
						amzAdvReportTreatService.treatTargetQueryAdvertReport(request, jsonReader);
					} else {
						amzAdvReportTreatService.treatProductTargetAdvertReport(request, jsonReader);
					}
				}
			} else if (CampaignType.sd.equals(request.getCampaigntype())) {
				if (AdvRecordType.campaigns.equals(request.getRecordtype())) {
					if("T00001".equals(request.getSegment())) {
						amzAdvReportTreatService.treatCampaignsT00001SDAdvertReport(request, jsonReader);
					}else {
						amzAdvReportTreatService.treatCampaignsSDAdvertReport(request, jsonReader);
					}
				}else if (AdvRecordType.adGroups.equals(request.getRecordtype())) {
					amzAdvReportTreatService.treatAdroupsSDAdvertReport(request, jsonReader);
				}else if (AdvRecordType.productAds.equals(request.getRecordtype())) {
					amzAdvReportTreatService.treatProductSDAdvertReport(request, jsonReader);
				}else if (AdvRecordType.targets.equals(request.getRecordtype())) {
					amzAdvReportTreatService.treatAdvProductTargetSDAdvertReport(request, jsonReader);
				}else if(AdvRecordType.asins.equals(request.getRecordtype())) {
					amzAdvReportTreatService.treatAdvAsinSDAdvertReport(request, jsonReader);
				}
			} else {
				if (AdvResponseRecordType.campaign.equals(request.getRecordtype())) {
					if (AdvSegment.PlacementSegment.equals(request.getSegment())) {
						amzAdvReportTreatService.treatCampaignsPlaceHsaAdvertReport(request, jsonReader);
					} else {
						amzAdvReportTreatService.treatCampaignsHsaAdvertReport(request, jsonReader);
					}
				}  else if (AdvResponseRecordType.keyword.equals(request.getRecordtype())) {
					if (AdvSegment.QuerySegment.equals(request.getSegment())) {
						amzAdvReportTreatService.treatKeywordsHsaQuryAdvertReport(request, jsonReader);
					}  else {
						amzAdvReportTreatService.treatKeywordsHsaAdvertReport(request, jsonReader);
					}
				} else if (AdvResponseRecordType.target.equals(request.getRecordtype())) {
					amzAdvReportTreatService.treatProductTargetHsaAdvertReport(request, jsonReader);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage()!=null&&e.getMessage().contains("syntax error")) {
				return true;
			}
			request.setTreatStatus("error");
			String log = "处理报表内容异常"+e.getMessage();
			if(log.length()>5000){
				log = log.substring(0, 5000);
			}
			request.setLog(log);
		}
		return true;
	}

 
 
 
	public void requestReport() {
		Example example=new Example(AmzAdvAuth.class);
        Criteria crit = example.createCriteria();
		crit.andEqualTo("disable", false);
		List<AmzAdvAuth> advauthlist =amzAdvAuthService.selectByExample(example);
		if(advauthlist==null || advauthlist.size()==0){
			return;
		}
		if(amzAdvAuthService.isBusy()) {
			return ;
		}
		List<Runnable> runnables = new ArrayList<Runnable>();
		for(AmzAdvAuth item:advauthlist) {
			if(item.getDisableTime()!=null&&GeneralUtil.distanceOfDay(item.getDisableTime(), new Date())<=1) {
				continue;
			}
		   runnables.add(requestReportThread2(item));
		}
	   
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv( runnables, "requestReport");
		}
	}
	
	
 
	
 
	public Runnable requestReportThread(final Map<String, Object> map) {
		return new Runnable() {
			public void run() {
					requestReport(map);
			}
		};
	}

	public Runnable requestReportThread2(final AmzAdvAuth item) {
		return new Runnable() {
			public void run() {
					requestReport(item);
			}
		};
	}
	public Map<String,Object> getNeedRequestReport(String profileid,String non_segment,String isnow){
		return   this.amzAdvRequestMapper.getNeedRequestReport(profileid,non_segment,isnow);
		 
	}
	
	public void reuqestReportByParam(AmzAdvProfile profile,String non_segment,String isnow,Date byday,Map<String,Object> map ) {
		if(map==null) {
		    map = this.amzAdvRequestMapper.getNeedRequestReport(profile.getId().toString(),non_segment,isnow);
		}
		boolean needRequest=true;
		String reporttype = (String) map.get("reporttype");
		String campaignType = (String) map.get("campaigntype");
		String segment =map.get("segment")!=null?(String) map.get("segment"):null;
		String creativeType =map.get("activeType")!=null?(String) map.get("activeType"):null;
		String metrics = map.get("metrics").toString();
		metrics=metrics.replaceAll("\\n", "");
		metrics=metrics.replaceAll("\\t", "");
		metrics=metrics.replaceAll("\\r", "");
		metrics=metrics.replaceAll("\r", "");
		metrics=metrics.replaceAll("\n", "");
		metrics=metrics.replaceAll("\t", "");
		String reponsetype=(String)map.get("reponsetype");
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
		String url = "";
		if (AdvRecordType.asins.equals(reporttype)&&!"sd".equals(campaignType)) {
			url = "/" + reporttype + "/report";
		} else {
			url = "/" + campaignType + "/" + reporttype + "/report";
		}
		Example example=new Example(AmzAdvRequest.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", profile.getId());
		crit.andEqualTo("campaigntype", campaignType);
		crit.andEqualTo("recordtype", reporttype);
		if(segment==null) {
			crit.andIsNull("segment");	
		}else {
			crit.andEqualTo("segment", segment);
		}
		if(creativeType==null) {
			crit.andIsNull("creativeType");	
		}else {
			crit.andEqualTo("creativeType", creativeType);
		}
		crit.andEqualTo("byday", fmt2.format(byday));
		example.setOrderByClause("requesttime desc");
		List<AmzAdvRequest> list = amzAdvRequestMapper.selectByExample(example);
		if(list!=null&&list.size()>0) {
			AmzAdvRequest oldone = list.get(0);
		    for(int i_delete=1;i_delete<list.size();i_delete++) {
	    		   AmzAdvRequest item = list.get(i_delete);
	    		   amzAdvRequestMapper.delete(item);
	    	}
	       if(oldone!=null&&(oldone.getFilesize()==null||oldone.getFilesize()==22)) {
	    	   oldone.setRequesttime(new Date());
	    	   oldone.setOpttime(new Date());
	    	   oldone.setTreatStatus("success");
	    	   amzAdvRequestMapper.updateByPrimaryKey(oldone);
	    	   return;
	       }
	       if(GeneralUtil.distanceOfDay(oldone.getRequesttime(),new Date())<1) {
	    	   return;
	       } 
		}
		JSONObject param = new JSONObject();
		if (AdvRecordType.asins.equals(reporttype)&&!"sd".equals(campaignType)) {
			param.put("campaignType", "sponsoredProducts");
		}
		if (segment != null) {
			if("sd".equals(campaignType)) {
				param.put("tactic", segment);
			}else {
				param.put("segment", segment);
			}
		}
		param.put("reportDate", fmt.format(byday));
		if (!"vendor".equals(profile.getType()) && (AdvRecordType.productAds.equals(reporttype) || AdvRecordType.asins.equals(reporttype))) {
				metrics = metrics + ",sku";
		}
		param.put("metrics", metrics);
		if(!StringUtil.isEmpty(creativeType)) {
			param.put("creativeType", creativeType);
		}

//      删除今天之前没处理的记录Tactic T00001
		String response = null;
		try {
			if(needRequest) {
				if("sd".equals(campaignType)) {
					response = amzAdvAuthService.amzAdvPost_V3(profile, url, param.toJSONString());
				} else {
					response = amzAdvAuthService.amzAdvPost(profile, url, param.toJSONString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//无法获取所在站点权限
			if(e!=null&&e.getMessage()!=null&&e.getMessage().contains("invalid_grant")) {
				AmzAdvAuth auth = amzAdvAuthService.selectByKey(profile.getAdvauthid());
				auth.setDisableTime(new Date());
				amzAdvAuthService.updateAll(auth);
			}
			if(e.getMessage()!=null&&(e.getMessage().contains("Remote host closed")||e.getMessage().contains("SSL peer shut down"))) {
			     this.amzAdvAuthService.setBusyTime(new Date());
			}
		}
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response);
			String reportId = item.getString("reportId");
			String recordType = item.getString("recordType");
			String status = item.getString("status");
			String statusDetails = item.getString("statusDetails");
			AmzAdvRequest record = new AmzAdvRequest();
			record.setReportid(reportId);
			record.setRecordtype(recordType);
			record.setSegment(segment);
			record.setCampaigntype(campaignType);
			record.setStatus(status);
			record.setStatusdetails(statusDetails);
			record.setProfileid(profile.getId());
			record.setCreativeType(creativeType);
			record.setRequesttime(new Date());
			record.setOpttime(new Date());
			record.setByday(GeneralUtil.getDatez(fmt2.format(byday)));
			record.setIsrun(false);
			amzAdvRequestMapper.insert(record);
		}else {
	
			AmzAdvRequest record = new AmzAdvRequest();
			record.setReportid("none-"+UUIDUtil.getUUIDshort());
			record.setRecordtype(reponsetype);
			record.setSegment(segment);
			record.setCampaigntype(campaignType);
			record.setStatus("SUCCESS");
			record.setTreatStatus("success");
			record.setLog("no request");
			record.setStatusdetails("can not request report");
			record.setProfileid(profile.getId());
			record.setCreativeType(creativeType);
			record.setRequesttime(new Date());
			record.setOpttime(new Date());
			record.setByday(GeneralUtil.getDatez(fmt2.format(byday)));
			record.setIsrun(false);
			amzAdvRequestMapper.insert(record);
		}
	}
	public void reuqestReportByParam(AmzAdvProfile profile,String non_segment,String isnow) {
	    Calendar c = Calendar.getInstance(); 
		String counrtynow = GeneralUtil.getTimezone(profile.getCountrycode(), new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		try {
			today = sdf.parse(counrtynow);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(today);
	    Map<String,Object> map = this.amzAdvRequestMapper.getNeedRequestReport(profile.getId().toString(),non_segment,isnow);
		if(map==null||map.get("byday")==null||map.get("byday").toString().equals("6666-01-01")) {
			c.add(Calendar.DATE, -1);
		}else {
			  Date byday  = GeneralUtil.getDatez(map.get("byday").toString());
			  long day = (today.getTime()-byday.getTime())/(24*60*60*1000);
	          if(day>=30&&map!=null) { 
	        	  Object opttimeobj = map.get("opttime");
	        	  if(opttimeobj!=null) {
	        		  Date opttime = GeneralUtil.getDate(opttimeobj);
	        		  long hour = ((new Date()).getTime()-opttime.getTime())/(60*60*1000);
	        		  if(hour<3) {
	        			 return;
	        		  }
	        	   }
	        	  c.add(Calendar.DATE, -1); 
	          }else if(day>4) {
	         	  c.add(Calendar.DATE, -30); 
	          }else if(day>4){
	        	  c.add(Calendar.DATE, -7); 
	          }else {
	        	  int myday=Long.valueOf(day).intValue()*-1;
	        	  myday=myday-1;
	        	  c.add(Calendar.DATE,myday); 
	          }
		}
		reuqestReportByParam(profile,  non_segment,  isnow,  c.getTime(),map) ;
	}
	public void requestReport(AmzAdvAuth advauth) {
			if (advauth != null) {
				List<AmzAdvProfile>  profiles = amzAdvAuthService.getAmzAdvProfile(advauth);
				for (AmzAdvProfile profile : profiles) {
					String non_segment=null;
						if(!"vendor".equals(profile.getType())||"US,CA,UK,DE,FR,IT,ES,JP,MX".contains(profile.getCountrycode())) {
							non_segment="T00001";
						}
						if("SG".equals(profile.getCountrycode())||"SE".equals(profile.getCountrycode())||"PL".equals(profile.getCountrycode())) {
							non_segment=non_segment+",T00020";
						}
						if("US".equals(profile.getCountrycode())) {
							non_segment=non_segment+",remarketing";
						}
						if("A2NODRKZP88ZB9".equals(profile.getMarketplaceid())||"A19VAU5U5O7RUS".equals(profile.getMarketplaceid())) {
							non_segment=non_segment+",T00030";
						}
						if("agency".equals(profile.getType())){
							continue;
						}
						reuqestReportByParam( profile, non_segment, null);
				}
		}
	}
	
	public void requestReport(Map<String, Object> map) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
		String url = "";
		String reporttype = (String) map.get("reporttype");
		String reponsetype = (String) map.get("reponsetype");
		String campaignType = (String) map.get("campaignType");
		Date day = GeneralUtil.getDate( map.get("day"));
		AmzAdvAuth advauth = map.get("advauth") == null ? null : (AmzAdvAuth)map.get("advauth");
		String segment = (String) map.get("segment");
		String metrics = (String) map.get("metrics");
		String creativeType = map.get("creativeType")==null?null:(String)map.get("creativeType");
		if (AdvRecordType.asins.equals(reporttype)&&!"sd".equals(campaignType)) {
			url = "/" + reporttype + "/report";
		} else {
			url = "/" + campaignType + "/" + reporttype + "/report";
		}
		
			if (advauth != null) {
				List<AmzAdvProfile> profiles =null;
				if(map.get("profileid")!=null) {
					AmzAdvProfile profile = amzAdvProfileMapper.selectByPrimaryKey(map.get("profileid"));
					profiles=new ArrayList<AmzAdvProfile>();
					profiles.add(profile);
				}else {
					profiles = amzAdvAuthService.getAmzAdvProfile(advauth);
				}
				 c.setTime(day);
				for (AmzAdvProfile profile : profiles) {
					Example example=new Example(AmzAdvRequest.class);
					Criteria crit = example.createCriteria();
					crit.andEqualTo("profileid", profile.getId());
					crit.andEqualTo("campaigntype", campaignType);
					crit.andEqualTo("recordtype", reponsetype);
					if(segment==null) {
						crit.andIsNull("segment");	
					}else {
						crit.andEqualTo("segment", segment);
					}
					if(creativeType==null) {
						crit.andIsNull("creativeType");	
					}else {
						crit.andEqualTo("creativeType", creativeType);
					}

					crit.andEqualTo("byday", fmt2.format(c.getTime()));
					example.setOrderByClause("requesttime desc");
					List<AmzAdvRequest> list = amzAdvRequestMapper.selectByExample(example);
					if(list!=null&&list.size()>0) {
						AmzAdvRequest oldone=list.get(0);
				    	   for(int i_delete=1;i_delete<list.size();i_delete++) {
				    		   AmzAdvRequest item = list.get(i_delete);
				    		   amzAdvRequestMapper.delete(item);
				       }
				    	   Boolean  isold=(Boolean) map.get("isold");
				    	   if(isold) {
				    		   if(oldone!=null) {
						    		continue;
						    	}
				    	   }else {
				    		   if(oldone!=null&&GeneralUtil.distanceOfHour(oldone.getRequesttime(), new Date())<12) {
						    		continue;
						    	}else if(oldone!=null&&oldone.getFilesize()<=22) {
						    		oldone.setRequesttime(new Date());
						    		oldone.setOpttime(new Date());
						    		amzAdvRequestMapper.updateByPrimaryKey(oldone);
						    		continue;
						    	}
				    	   }
				    	
					}
					if(segment!=null) {
						if("T00001".equals(segment)) {
							if(!"vendor".equals(profile.getType())) {
								continue;
							}
							if(!"US, CA, UK, DE, FR, IT, ES, JP".contains(profile.getCountrycode())) {
								continue;
							}
						}
						if("remarketing".equals(segment)) {
							if(!"US".equals(profile.getCountrycode())) {
								continue;
							}
						}
						if("T00001".equals(segment)) {
							if(profile.getMarketplaceid().equals("A1AM78C64UM0Y8")||!"vendor".equals(profile.getType())) {
								continue;
							}
							if("ATVPDKIKX0DER".equals(profile.getMarketplaceid())||"A2EUQ1WTGCTBG2".equals(profile.getMarketplaceid())) {
								continue;
							}
						}
					    if("remarketing".equals(segment)  &&"ATVPDKIKX0DER".equals(profile.getMarketplaceid())) {
					    	continue;
					    }
					    if("T00030".equals(segment)) {
					    	if("A1C3SOZRARQ6R3".equals(profile.getMarketplaceid())) {
					    		continue;
					    	}
					    }
					    if("T00020".equals(segment) &&"A1C3SOZRARQ6R3".equals(profile.getMarketplaceid())) {
					    	continue;
					    }
					}
					JSONObject param = new JSONObject();
					if (AdvRecordType.asins.equals(reporttype)&&!"sd".equals(campaignType)) {
						param.put("campaignType", "sponsoredProducts");
					}
					 if(c.get(Calendar.HOUR_OF_DAY)==0&&c.get(Calendar.MINUTE)==0&&c.get(Calendar.SECOND)==0) {
						 param.put("reportDate", fmt.format(c.getTime()));
					 }else {
							SimpleDateFormat fmtzone = new SimpleDateFormat("yyyyMMdd");
							fmtzone.setTimeZone(TimeZone.getTimeZone(profile.getTimezone()));
							param.put("reportDate", fmt.format(c.getTime()));
					 }
					
					if (segment != null) {
						if("sd".equals(campaignType)) {
							param.put("tactic", segment);
						}else {
							param.put("segment", segment);
						}
					}
					if(segment!=null&&segment.equals("T00001")&&!"vendor".equals(profile.getType())) {
						continue;
					}
					
					if("agency".equals(profile.getType())){
						continue;
					}
					if (!"vendor".equals(profile.getType()) && (AdvRecordType.productAds.equals(reporttype) || AdvRecordType.asins.equals(reporttype))) {
							metrics = metrics + ",sku";
					}
					param.put("metrics", metrics);
					if(!StringUtil.isEmpty(creativeType)) {
						param.put("creativeType", creativeType);
					}
//                  删除今天之前没处理的记录
					String response = null;
					try {
						if("sd".equals(campaignType)) {
							response = amzAdvAuthService.amzAdvPost_V3(profile, url, param.toJSONString());
						} else {
							response = amzAdvAuthService.amzAdvPost(profile, url, param.toJSONString());
						}
					} catch (Exception e) {
						e.printStackTrace();
						//无法获取所在站点权限
						if(e.getMessage().contains("UNAUTHORIZED")) {
						   log.info( url+param.toJSONString());
						}
							
						if(e.getMessage()!=null&&(e.getMessage().contains("Remote host closed")||e.getMessage().contains("SSL peer shut down"))) {
							break;
						}
					}
					if (StringUtil.isNotEmpty(response)) {
						JSONObject item = GeneralUtil.getJsonObject(response);
						String reportId = item.getString("reportId");
						String recordType = item.getString("recordType");
						String status = item.getString("status");
						String statusDetails = item.getString("statusDetails");
						AmzAdvRequest record = new AmzAdvRequest();
						record.setReportid(reportId);
						record.setRecordtype(recordType);
						record.setSegment(segment);
						record.setCampaigntype(campaignType);
						record.setStatus(status);
						record.setStatusdetails(statusDetails);
						record.setProfileid(profile.getId());
						record.setCreativeType(creativeType);
						record.setRequesttime(new Date());
						record.setOpttime(new Date());
						record.setByday(GeneralUtil.getDatez(fmt2.format(c.getTime())));
						record.setIsrun(false);
						amzAdvRequestMapper.insert(record);
						Map<String, Object> myparam = new HashMap<String, Object>();
						myparam.put("amzsnap", record);
						myparam.put("advProfile", profile);
					}
					
				}
			}
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


	public void requestReportByProfile(AmzAdvProfile profiles, AmzAdvAuth advauth) {
		int mydays[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 
				19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
		requestReportByProfile(profiles, advauth, mydays);
	}
	
	public void request7DaysReportByProfile(AmzAdvProfile profiles, AmzAdvAuth advauth) {
		int mydays[] = { 0, 1, 2, 3, 4, 5, 6, 7 };
		requestReportByProfile(profiles, advauth, mydays);
	}




	
	@Override
	public void requestReport(Date date,String shopid,boolean isold) {
		        Example example=new Example(AmzAdvReportMterics.class);
		        example.orderBy("level").asc();
				// TODO Auto-generated method stub
		        List<AmzAdvReportMterics> typeList = amzAdvReportMtericsMapper.selectByExample(example);
		        Double days = GeneralUtil.distanceOfDay(date, new Date());
		        int day=days.intValue();
		        for(AmzAdvReportMterics type:typeList) {
		     	   if(day==30&&!type.getMetrics().contains("30")) {
		              	continue;
		              }
		              else if(day==7&&!type.getMetrics().contains("7")) {
		              	continue;
		              }
		              else if(day==14&&!type.getMetrics().contains("14")) {
		              	continue;
		              }
		     	    if(StrUtil.isNotEmpty(shopid)) {
		     	    	if(type.getLevel()>=4) {
		     	    		continue;
		     	    	}
		     	    }
		        	requestReport(type,date,shopid,isold);
		        }
	}
	
	public void requestReport(AmzAdvReportMterics type,Date day,String shopid,Boolean isold) {
		// TODO Auto-generated method stub
		List<AmzAdvAuth> advauthlist =null;
		Example example=new Example(AmzAdvAuth.class);
        Criteria crit = example.createCriteria();
		crit.andEqualTo("disable", false);
		if(StrUtil.isNotEmpty(shopid)) {
			crit.andEqualTo("shopid", shopid);
		}
		advauthlist=amzAdvAuthService.selectByExample(example);
		if(advauthlist==null || advauthlist.size()==0){
			return;
		}
		String key="";
		String metrics = type.getMetrics();
		metrics=metrics.replaceAll("\\r", "");
		metrics=metrics.replaceAll("\\n", "");
		metrics=metrics.replaceAll("\\t", "");
		metrics=metrics.replaceAll("\r", "");
		metrics=metrics.replaceAll("\n", "");
		metrics=metrics.replaceAll("\t", "");
	
		String reponsetype=type.getReponsetype();
		String segment=type.getSegment();
		String activeType=type.getActivetype();
		key=reponsetype+type.getCampaigntype()+(segment==null?"":segment)+(activeType==null?"":activeType)+(isold?"true":"false");
		List<Runnable> runnables = new ArrayList<Runnable>();
		for (AmzAdvAuth advauth : advauthlist) {
			if (advauth.getDisable() || advauth.getRegion().equals("TT")||(advauth.getDisableTime()!=null&&GeneralUtil.distanceOfHour(advauth.getDisableTime(), new Date())<3)) {
				continue;
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("campaignType", type.getCampaigntype());
			param.put("advauth", advauth);
			param.put("reporttype", type.getReporttype());
			param.put("metrics", metrics);
			param.put("reponsetype", reponsetype);
			param.put("isold", isold);
			if(segment!=null) {
				param.put("segment",segment);
			}
			if(activeType!=null) {
				param.put("creativeType",activeType);
			}
			param.put("day", day);
			Runnable runnable = requestReportThread(param);
			runnables.add(runnable);
		}
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv( runnables, "requestReport"+key);
		}
	}
 
	@Override
	public void requestReportByProfile(AmzAdvProfile profiles, AmzAdvAuth advauth, int[] mydays) {
		// TODO Auto-generated method stub
        List<AmzAdvReportMterics> typeList = amzAdvReportMtericsMapper.selectAll();
    	 List<Runnable> runnables = new ArrayList<Runnable>();
        for(AmzAdvReportMterics type:typeList) {
        	String metrics = type.getMetrics();
    		metrics=metrics.replaceAll("\\r", "");
    		metrics=metrics.replaceAll("\\n", "");
    		metrics=metrics.replaceAll("\\t", "");
    		metrics=metrics.replaceAll("\r", "");
    		metrics=metrics.replaceAll("\n", "");
    		metrics=metrics.replaceAll("\t", "");
    		String reponsetype=type.getReponsetype();
    		String segment=type.getSegment();
    		String activeType=type.getActivetype();
    		Map<String, Object> param = new HashMap<String, Object>();
			param.put("campaignType", type.getCampaigntype());
			param.put("advauth", advauth);
			param.put("reporttype", type.getReporttype());
			param.put("metrics", metrics);
			param.put("reponsetype", reponsetype);
			if(segment!=null) {
				param.put("segment",segment);
			}
			if(activeType!=null) {
				param.put("creativeType",activeType);
			}
			param.put("days", mydays);
			runnables.add(requestReportThread(param));
        }
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv( runnables, "requestReport");
		}
	}
 


}
