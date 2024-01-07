package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.report.dao.AmzAdvRequestMapper;
import com.wimoor.amazon.adv.report.dao.AmzAdvRptQueryMapper;
import com.wimoor.amazon.adv.report.dao.AmzAdvSumProductAdsMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvReportRequestType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRptQuery;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumProductAds;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumAllTypeService;
import com.wimoor.amazon.adv.report.service.impl.AmzAdvReportHandlerServiceImpl.AdvRecordType;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvReportTreatService")
public abstract class AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProfileMapper amzAdvProfileMapper;
	@Resource
	AmzAdvSumProductAdsMapper amzAdvSumProductAdsMapper;
	@Resource
	IAmzAdvSumAllTypeService amzAdvSumAllTypeService;
	@Resource
	AmzAdvRptQueryMapper amzAdvRptQueryMapper;
    @Resource
    ApiBuildService apiBuildService;
    @Resource
    AmzAdvRequestMapper amzAdvRequestMapper;
    
	public AmzAdvRptQuery loadQuery(String query) {
		if(StrUtil.isBlankOrUndefined(query)) {
			return null;
		}
		Example example = new Example(AmzAdvRptQuery.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("query", query);
		List<AmzAdvRptQuery> list = amzAdvRptQueryMapper.selectByExampleAndRowBounds(example, new RowBounds(0, 1));
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			 AmzAdvRptQuery myquery =new AmzAdvRptQuery();
			 myquery.setQuery(query);
			 try {
				 int result = amzAdvRptQueryMapper.insert(myquery);
				 if(result>0) {
					 return myquery;
				 }
			 }catch(Exception e) {
				return amzAdvRptQueryMapper.selectByName(query);
			 }
			
			return null;
		}
	}
 
	
	void addSummary(AmzAdvRequest request,Map<String,AmzAdvSumProductAds> mapsum,Date date,String type,Integer clicks,String currency,Integer impressions,BigDecimal cost,
			BigDecimal attributedsales,Integer attributedunitsordered,Integer attributedConversions){
		clicks=clicks==null? 0:clicks;
		currency = currency==null?"":currency;
		impressions=impressions==null? 0:impressions;
		cost =cost==null? new BigDecimal("0"):cost;
		attributedsales =attributedsales==null? new BigDecimal("0"):attributedsales;
		attributedunitsordered =attributedunitsordered==null? 0:attributedunitsordered;
		attributedConversions=attributedConversions==null? 0:attributedConversions;
		String key = GeneralUtil.formatDate(date);
		AmzAdvSumProductAds record=mapsum.get(key);
		if(record==null) {
			record = new AmzAdvSumProductAds();
			record.setProfileid(request.getProfileid());
			record.setByday(date);
			record.setCurrency(currency);
			record.setCost(cost);
			record.setClicks(clicks);
			record.setCtype(type);
			record.setImpressions(impressions);
			record.setAttributedunitsordered(attributedunitsordered);
			record.setAttributedConversions(attributedConversions);
			record.setAttributedsales(attributedsales);
			
		}else {
			record.setProfileid(request.getProfileid());
			record.setByday(date);
			record.setCurrency(currency);
			record.setCost(record.getCost()!=null?record.getCost().add(cost):cost);
			record.setClicks(record.getClicks()!=null?record.getClicks()+clicks:clicks);
			record.setCtype(type);
			record.setImpressions(record.getImpressions()!=null?record.getImpressions()+ impressions:impressions);
			record.setAttributedunitsordered(record.getAttributedunitsordered()!=null?record.getAttributedunitsordered()+ attributedunitsordered:attributedunitsordered);
			record.setAttributedConversions(record.getAttributedConversions()!=null?record.getAttributedConversions()+ attributedConversions:attributedConversions);
			record.setAttributedsales(record.getAttributedsales()!=null?record.getAttributedsales().add(attributedsales):attributedsales );
		}
		mapsum.put(key,record);
	}
	
	
	public synchronized void saveAmzAdvSumAllType(int size, AmzAdvRequest request) {
		AmzAdvSumAllType typesum = new AmzAdvSumAllType();
		typesum.setProfileid(request.getProfileid());
		typesum.setCampaigntype(request.getCampaigntype());
		typesum.setRecordtype(request.getRecordtype());
		typesum.setByday(new Date());
		typesum.setOpttime(new Date());
		typesum.setState(AdvState.enabled);
		typesum.setQuantity(size);
		AmzAdvSumAllType old = amzAdvSumAllTypeService.selectByKey(typesum);
		if (old != null) {
			amzAdvSumAllTypeService.updateByKey(typesum);
		} else {
			amzAdvSumAllTypeService.insert(typesum);
		}
	}
	
	public List<AmzAdvRequest> getRequestList(AmzAdvProfile profile,AmzAdvReportRequestType type) {
		String campaignType=type.getCampaigntype();
		String reponsetype=type.getReponsetype();
		String segment=type.getSegment();
		String creativeType=type.getActivetype();
		Calendar c = Calendar.getInstance();
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
		c.add(Calendar.HOUR, -15);
		crit.andGreaterThan("requesttime", c.getTime());
		example.setOrderByClause("requesttime desc");
		List<AmzAdvRequest> list = amzAdvRequestMapper.selectByExample(example);
		return list;
	}
	public List<AmzAdvRequest> getRequestList(AmzAdvProfile profile,String campaignType,String reponsetype,
			String segment,String creativeType,Date day) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
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
		crit.andEqualTo("startDate", fmt2.format(c.getTime()));
		example.setOrderByClause("requesttime desc");
		List<AmzAdvRequest> list = amzAdvRequestMapper.selectByExample(example);
		return list;
	}
	
	public List<AmzAdvRequest> getRequestList63(AmzAdvProfile profile,String campaignType,String reponsetype,
	    String segment,String creativeType) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
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
 
		c.add(Calendar.DATE, -63);
		crit.andGreaterThanOrEqualTo("startDate", fmt2.format(c.getTime()));
		List<AmzAdvRequest> list = amzAdvRequestMapper.selectByExample(example);
		return list;
	}
 
	public String formatMetrics(AmzAdvReportRequestType type,Date start,Date end) {
		String metrics=type.getMetrics();
		metrics=metrics.replaceAll("\\r", "");
		metrics=metrics.replaceAll("\\n", "");
		metrics=metrics.replaceAll("\\t", "");
		metrics=metrics.replaceAll("\r", "");
		metrics=metrics.replaceAll("\n", "");
		metrics=metrics.replaceAll("\t", "");
		metrics=metrics.replaceAll(" ", "");
		return metrics;
	}
	
	public Date getBlankDay(AmzAdvProfile profile,AmzAdvReportRequestType type,Date day) {
		int i=1;
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
		String campaignType=type.getCampaigntype();
		String reponsetype=type.getReponsetype();
		String segment=type.getSegment();
		String creativeType=type.getActivetype();
		List<AmzAdvRequest>  allList=getRequestList63( profile, campaignType, reponsetype, segment, creativeType);
		Map<String,AmzAdvRequest> oldmap=new HashMap<String,AmzAdvRequest>();
		for(AmzAdvRequest item:allList) {
			oldmap.put(fmt2.format(item.getStartDate()), item);
		}
		Calendar c2 = Calendar.getInstance();
		while(i<60) {
			c2.add(Calendar.DATE, -1);
			AmzAdvRequest item = oldmap.get( fmt2.format(c2.getTime()));
			if(item==null) {
				break;
			}
			i++;
		}
	   if(i==60) {
		   return null;
	   }else {
		   return  c2.getTime();
	   }
	}
	
	public void requestReportParamBuildV2(AmzAdvAuth advauth,AmzAdvProfile profile,AmzAdvReportRequestType type,Date day,Boolean isold) {
		String metrics = formatMetrics(type,day,day);
		String campaignType=type.getCampaigntype();
		String reponsetype=type.getReponsetype();
		String segment=type.getSegment();
		String reporttype=type.getReporttype();
		String creativeType=type.getActivetype();
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
		String url = "";
		if (AdvRecordType.asins.equals(reporttype)&&!"sd".equals(campaignType)) {
			url = "/" + reporttype + "/report";
		} else {
			url = "/" + campaignType + "/" + reporttype + "/report";
		}
        List<AmzAdvRequest> list=getRequestList( profile, campaignType, reponsetype, segment, creativeType, day);
		if(list!=null&&list.size()>0) {
			   AmzAdvRequest oldone=list.get(0);
			   for(int i=1;i<list.size();i++) {
				   AmzAdvRequest old=list.get(i);
				   this.amzAdvRequestMapper.deleteByPrimaryKey(old);
			   }
	    	   if(!isold) {
	    		   if(oldone!=null&&GeneralUtil.distanceOfHour(oldone.getRequesttime(), new Date())<1) {
			    		return ;
			    	}else if(oldone!=null&&GeneralUtil.distanceOfHour(oldone.getRequesttime(), new Date())<15) {
			    		Date blankday = getBlankDay( profile, type, day);
			    		if(blankday!=null) {
			    			c.setTime(blankday);
			    			list=null;
			    		}else {
			    			return;
			    		}
			    	} 
	    	   } 
		}
		JSONObject param = new JSONObject();
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
		if (!"vendor".equals(profile.getType()) && (AdvRecordType.productAds.equals(reporttype) || AdvRecordType.asins.equals(reporttype))) {
				metrics = metrics + ",sku";
		}
		param.put("metrics", metrics);
		if(!StringUtil.isEmpty(creativeType)) {
			param.put("creativeType", creativeType);
		}
		Date byday = GeneralUtil.getDatez(fmt2.format(c.getTime()));
		if(!type.getCampaigntype().toLowerCase().equals("sd")) {
			url="/v2"+url;
		}
		runRequestReportV2( profile, param, url, byday,byday, type,list);
					
		 
	}
	 

    
		 
    public JSONObject  formatterParamV3(AmzAdvReportRequestType type,Date startDate,Date endDate) {
    	String metrics = formatMetrics(type,startDate,endDate);
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fmt2 = new SimpleDateFormat("MM/dd");
		JSONObject param = new JSONObject();
	    param.put("name", type.getCampaigntype()+" "+type.getReporttype()+" report "+fmt2.format(startDate)+"-"+fmt2.format(endDate));
	    param.put("startDate",fmt.format(startDate));
	    param.put("endDate",fmt.format(endDate));
		param.put("configuration",JSONObject.parse(metrics));
		return param;
    }
    
	public void requestReportParamBuildV3(AmzAdvAuth advauth,AmzAdvProfile profile,AmzAdvReportRequestType type,Date startDate,Date endDate,Boolean isold) {
		String url = "/reporting/reports";
        List<AmzAdvRequest> list=getRequestList( profile, type);
		if(list!=null&&list.size()>0) {  return;  }
		JSONObject param = formatterParamV3(  type,  startDate,  endDate);
		runRequestReportV3( profile, param, url, startDate,endDate, type,null);
	}
	
 
	
	public void handleResponse(AmzAdvProfile profile,AmzAdvReportRequestType type,JSONObject item,Date startDate,Date endDate) {
		String reportId = item.getString("reportId");
		String recordType = item.getString("recordType")!=null?item.getString("recordType"):type.getReponsetype();
		String status = item.getString("status");
		String statusDetails = item.getString("statusDetails");
		AmzAdvRequest record = new AmzAdvRequest();
		record.setReportid(reportId);
		record.setRecordtype(recordType);
		record.setSegment(type.getSegment());
		record.setCampaigntype(type.getCampaigntype());
		record.setStatus(status);
		record.setStatusdetails(statusDetails);
		record.setProfileid(profile.getId());
		record.setCreativeType(type.getActivetype());
		record.setRequesttime(new Date());
		record.setOpttime(new Date());
		record.setStartDate(startDate);
		record.setEndDate(endDate);
		record.setReportType(type.getId());
		record.setIsrun(false);
		amzAdvRequestMapper.insert(record);
	}
	
    public void runRequestReportV3(AmzAdvProfile profile,JSONObject param,String url,Date startDate,Date endDate,AmzAdvReportRequestType type,List<AmzAdvRequest> list) {
		    	Map<String,String> headerExt=new HashMap<String,String>();
				headerExt.put("Content-Type", "application/vnd.createasyncreportrequest.v3+json");
			    String response = apiBuildService.amzAdvPost(profile, url, param.toJSONString(),headerExt);
				if (StringUtil.isNotEmpty(response)) {
					JSONObject item = GeneralUtil.getJsonObject(response);
					if(list!=null&&list.size()>0) {
						 for(AmzAdvRequest  req:list) {
				    		   amzAdvRequestMapper.delete(req);
				        }
					}
					handleResponse(profile,type,item,startDate,endDate);
				}	 
    }
    
    public void runRequestReportV2(AmzAdvProfile profile,JSONObject param,String url,Date startDate,Date endDate,AmzAdvReportRequestType type,List<AmzAdvRequest> list) {
	    String response  = apiBuildService.amzAdvPost(profile, url, param.toJSONString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response);
			if(list!=null&&list.size()>0) {
				 for(AmzAdvRequest  req:list) {
		    		   amzAdvRequestMapper.delete(req);
		        }
			}
			handleResponse(profile,type,item,startDate,endDate);
		}	 

}
    
 
    
  public boolean isV3Report( AmzAdvReportRequestType type) {
	  return type.getCampaigntype().equals("sp");
  }
  
  public void  requestReport(AmzAdvAuth advauth, AmzAdvProfile profile, AmzAdvReportRequestType type) { 
    if(isV3Report(type)) {
		Calendar c=Calendar.getInstance();
		Date end = c.getTime();
		Calendar weekcal = Calendar.getInstance();
		int week_index = weekcal.get(Calendar.DAY_OF_WEEK) - 1; 
		if(week_index==0) {
			c.add(Calendar.DATE, -30);
		}else {
			c.add(Calendar.DATE, -8);	
		}
		requestReportParamBuildV3(advauth, profile, type, c.getTime() ,end, false);;
	}else {
		 Calendar c=Calendar.getInstance();
		 int[] days= {2,8,32};//
		 int index=c.get(Calendar.HOUR_OF_DAY)%1;
		 int day=days[index];
		 c.add(Calendar.DATE, day*-1);
		 requestReportParamBuildV2(advauth,profile,type,c.getTime(),false);
	}
	
  }

}
