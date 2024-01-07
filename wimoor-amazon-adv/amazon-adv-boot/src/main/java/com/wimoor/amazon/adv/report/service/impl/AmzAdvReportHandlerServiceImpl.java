package com.wimoor.amazon.adv.report.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.common.service.IAmzAdvStoresService;
import com.wimoor.amazon.adv.report.dao.AmzAdvReportRequestTypeMapper;
import com.wimoor.amazon.adv.report.dao.AmzAdvRequestMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvReportRequestType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportHandlerService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.util.SpringUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvReportHandlerService")
public class AmzAdvReportHandlerServiceImpl implements IAmzAdvReportHandlerService {
 
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvRequestMapper amzAdvRequestMapper;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatAdgroupsHsaService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatAdgroupsSDService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatAdgroupsService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatAsinSDService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatAsinsService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatCampaignsHsaService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatCampaignsPlaceHsaService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatCampaignsPlaceService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatCampaignsSDService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatCampaignsService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatCampaignsT00001SDService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatKeywordsHsaQuryService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatKeywordsHsaService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatKeywordsQuryService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatKeywordsService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatProductSDService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatProductService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatProductTargetHsaService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatProductTargetSDService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatProductTargetService;
	@Resource
	IAmzAdvReportTreatService amzAdvReportTreatTargetQueryService;
	@Resource
	IAmzAdvRemindService amzAdvRemindService;
	@Resource
	AmzAdvReportRequestTypeMapper amzAdvReportRequestTypeMapper;
	@Resource
	IAmzAdvStoresService amzAdvStoresService;
    @Resource
    AmzAdvProfileMapper amzAdvProfileMapper;
    @Resource
    ApiBuildService apiBuildService;
    
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

    //@Scheduled(cron = "0 0/5 * * * ?")
    public void readReport() {
         if(apiBuildService.isBusy()) {
       	     return;
         }
            Calendar c=Calendar.getInstance();
            int hour=c.get(Calendar.HOUR_OF_DAY);
            List<Runnable> runnables = new ArrayList<Runnable>();
			List<AmzAdvAuth> advauthlist =amzAdvAuthService.list();
			if(advauthlist==null || advauthlist.size()==0){
				return;
			}
			if(apiBuildService.isBusy()) {
				return ;
			}
			for(AmzAdvAuth item:advauthlist) {
				if(item.getDisableTime()!=null&&GeneralUtil.distanceOfHour(item.getDisableTime(), new Date())<=1) {
					continue;
				}
				  runnables.add(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						List<AmzAdvProfile> profilelist = amzAdvAuthService.getProfileByAuth(item.getId());
						for(AmzAdvProfile profile:profilelist) {
							if("agency".equals(profile.getType())||(profile.getErrorDay()!=null&&GeneralUtil.distanceOfMinutes(profile.getErrorDay(), new Date())<10)) {
								continue;
							}
							if(apiBuildService.isBusy()) {
								try {
									Thread.sleep(60000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} ;
							}
						   List<AmzAdvRequest> requestlist=amzAdvRequestMapper.getNeedTreatReport(profile.getId().toString());
						   for(AmzAdvRequest request:requestlist) {
							    if(hour>15&&hour<21) {
								   if(request.getCampaigntype().equals("sp")) {
									   continue;
								   }
							    }
							    request.setIsrun(true);
								request.setOpttime(new Date());
							    amzAdvRequestMapper.updateByPrimaryKey(request);
							    try {
							    	  readReport(profile,request);
							    }catch(Exception e) {
							    	   if(request.isIsrun()) {
							    		   request.setIsrun(false);
											request.setOpttime(new Date());
										    amzAdvRequestMapper.updateByPrimaryKey(request); 
							    	   }
							    	   
							    }
							  
						   }
						}
					}
				  });
				
			}
		if(runnables.size()>0){
			AdvUtils.executThreadForAdv(runnables);
		}
	}
    
    public void readReport(String reportid) {
                Example example=new Example(AmzAdvRequest.class);
                Criteria crit = example.createCriteria();
                crit.andEqualTo("reportid", reportid);
                AmzAdvRequest request=amzAdvRequestMapper.selectOneByExample(example);
			    AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(request.getProfileid());
			    request.setIsrun(true);
				request.setOpttime(new Date());
			    amzAdvRequestMapper.updateByPrimaryKey(request);
			    readReport(profile,request);
	}


 

	public void readReport(AmzAdvProfile profile,AmzAdvRequest request) {
		String response = null;
		try {
			if(request.getCampaigntype().equals("sp")) {
				String url = "/reporting/reports/" + request.getReportid();
	            response = apiBuildService.amzAdvGet(profile, url);	
			}else {
				String url = "/reports/" + request.getReportid();
	            response = apiBuildService.amzAdvGet_V2(profile, url);	
			}
		    if (StringUtil.isNotEmpty(response)) {
				JSONObject item = GeneralUtil.getJsonObject(response);
				String reportId = item.getString("reportId");
				String status = item.getString("status");
				String statusDetails = item.getString("statusDetails");
				String location = item.getString("location")!=null?item.getString("location"):item.getString("url");
				Integer fileSize = item.getInteger("fileSize");
				
				request.setReportid(reportId);
				request.setStatus(status);
				request.setStatusdetails(statusDetails);
				request.setFilesize(fileSize);
				request.setLocation(location);
				if("SUCCESS".equals(status)||"COMPLETED".equals(status)) {
					if (fileSize > 22) {
						request.setTreatStatus("");
						try {
							if(apiBuildService.amzAdvDownloadFile(profile, request, this)){
								request.setTreatNumber(request.getTreatNumber()+1);
								if(StrUtil.isBlank(request.getTreatStatus())){
									request.setTreatStatus("success");
									request.setLog("");
								}
							}
						} finally {
							request.setIsrun(false);
							request.setOpttime(new Date());
						    amzAdvRequestMapper.updateByPrimaryKey(request);
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
		} catch (BaseException e) {
			request.setTreatNumber(request.getTreatNumber()+1);
			request.setTreatStatus("error");
			request.setLog(e.getMessage());
			request.setOpttime(new Date());
			request.setIsrun(false);
			amzAdvRequestMapper.updateByPrimaryKeySelective(request);
			if (BaseException.AmazonBusy.equals(e.getCode())) {
				throw new BaseException(e.getMessage());
			}
			e.printStackTrace();
			return;
		}finally {
			if(request.isIsrun()) {
				request.setIsrun(false);
				amzAdvRequestMapper.updateByPrimaryKeySelective(request);
			}
		}
	}

	public Boolean treatReport(AmzAdvProfile profile, AmzAdvRequest request, JSONReader jsonReader) {
		try {
			AmzAdvReportRequestType type = amzAdvReportRequestTypeMapper.selectByPrimaryKey(request.getReportType());
			IAmzAdvReportTreatService reportTreat = SpringUtil.getBean(type.getBean());
			reportTreat.treatReport(profile,request, jsonReader);
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

 
 
 
	 
	public Runnable requestReportThread(final Map<String, Object> map) {
		return new Runnable() {
			public void run() {
					
			}
		};
	}

 
	public Map<String,Object> getNeedRequestReport(String profileid,String non_segment,String isnow){
		return   this.amzAdvRequestMapper.getNeedRequestReport(profileid,non_segment,isnow);
		 
	}
	  
	public boolean checkReport(AmzAdvProfile profile,String segment) {
		if(segment!=null) {
			if("T00001".equals(segment)) {
				if(!"vendor".equals(profile.getType())) {
					return false;
				}
				if(!"US, CA, UK, DE, FR, IT, ES, JP".contains(profile.getCountrycode())) {
					return false;
				} 
			}
			if("remarketing".equals(segment)) {
				if(!"US".equals(profile.getCountrycode())) {
					return false;
				}
			}
			if("T00001".equals(segment)) {
				if(profile.getMarketplaceid().equals("A1AM78C64UM0Y8")||!"vendor".equals(profile.getType())) {
					return false;
				}
				if("ATVPDKIKX0DER".equals(profile.getMarketplaceid())||"A2EUQ1WTGCTBG2".equals(profile.getMarketplaceid())) {
					return false;
				}
			}
		    if("remarketing".equals(segment)  &&"ATVPDKIKX0DER".equals(profile.getMarketplaceid())) {
		    	return false;
		    }
		    if("T00030".equals(segment)) {
		    	if("A1C3SOZRARQ6R3".equals(profile.getMarketplaceid())) {
		    		return false;
		    	}
		    }
		    if("T00020".equals(segment) &&"A1C3SOZRARQ6R3".equals(profile.getMarketplaceid())) {
		    	return false;
		    }
		}
		if(segment!=null&&segment.equals("T00001")&&!"vendor".equals(profile.getType())) {
			return false;
		}
		if("agency".equals(profile.getType())){
			return false;
		}
		return true;
	}
	  


	
	@Override
	public void requestReport() {
		        List<Runnable> runnables = new ArrayList<Runnable>();
				List<AmzAdvAuth> advauthlist =amzAdvAuthService.list();
				if(advauthlist==null || advauthlist.size()==0){
					return;
				}
				// TODO Auto-generated method stub
		        final Date start=new Date();
		        Example example=new Example(AmzAdvReportRequestType.class);
				List<AmzAdvReportRequestType> typeList = amzAdvReportRequestTypeMapper.selectByExample(example);
		        final List<AmzAdvAuth> authlist =advauthlist;
				        for(AmzAdvReportRequestType type:typeList) {
							IAmzAdvReportTreatService reportTreat = SpringUtil.getBean(type.getBean());
									// TODO Auto-generated method stub
									for (AmzAdvAuth advauth : authlist) {
							    		if (advauth.getDisable()||(advauth.getDisableTime()!=null&&GeneralUtil.distanceOfHour(advauth.getDisableTime(), new Date())<3)) {
							    			continue;
							    		}
							    	 	Runnable runnable =new Runnable() {
											@Override
											public void run() {
							    		List<AmzAdvProfile> profiles = amzAdvAuthService.getAmzAdvProfile(advauth);
										for (AmzAdvProfile profile : profiles) {
								        	if(checkReport(profile,type.getSegment())) {
								        		try {
								        			reportTreat.requestReport(advauth,profile,type);
								        		}catch(Exception e) {
								        			e.printStackTrace();
								        		}
								        		if(GeneralUtil.distanceOfHour(start, new Date())>=1) {
								        			return;
								        		}
								        		if(apiBuildService.isBusy()) {
								        			try {
														Thread.sleep(60000);
													} catch (InterruptedException e2) {
														// TODO Auto-generated catch block
														e2.printStackTrace();
													}
								        		}
								        	}
								        }
								   }
							     };
							   	runnables.add(runnable);	
								}
				       
		    		}
		    	
		    	
				if(runnables.size()>0){
					AdvUtils.executThreadForAdv(runnables);
				}
	}
	
	 
 
	 
 


}
