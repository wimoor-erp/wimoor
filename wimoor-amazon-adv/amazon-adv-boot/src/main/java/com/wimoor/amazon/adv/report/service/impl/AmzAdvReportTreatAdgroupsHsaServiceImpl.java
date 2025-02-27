package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvReportRequestType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdgroupsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportAdgroupsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdgroupsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdgroupsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdgroupsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdgroupsHsaVideo;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvReportTreatAdgroupsHsaService")
public class AmzAdvReportTreatAdgroupsHsaServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportAdgroupsHsaMapper amzAdvReportAdgroupsHsaMapper;
	@Resource
	AmzAdvAdgroupsHsaMapper amzAdvAdgroupsHsaMapper;
	 public void runRequestReportV2(AmzAdvProfile profile,JSONObject param,String url,Date startDate,Date endDate,AmzAdvReportRequestType type,List<AmzAdvRequest> list) {
			String metrics=param.get("metrics").toString();
			double distday = GeneralUtil.distanceOfDay(startDate, new Date());
			String log="";
			if(distday>=2&&distday<3) {
				metrics=metrics+",adGroupName";
				param.put("metrics",metrics);
				log="hasName";
			}			
		    String response  = apiBuildService.amzAdvPost(profile, url, param.toJSONString());
			if (StringUtil.isNotEmpty(response)) {
				JSONObject item = GeneralUtil.getJsonObject(response);
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
				record.setLog(log);
				if(list!=null&&list.size()>0) {
					 for(AmzAdvRequest  req:list) {
			    		   amzAdvRequestMapper.delete(req);
			        }
				}
				
				amzAdvRequestMapper.insert(record);
				Map<String, Object> myparam = new HashMap<String, Object>();
				myparam.put("amzsnap", record);
				myparam.put("advProfile", profile);
			}	

}
	 
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportAdgroupsHsa> list = new LinkedList<AmzAdvReportAdgroupsHsa>();
			final List<AmzAdvReportAdgroupsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportAdgroupsHsaAttributed>();
			final List<AmzAdvReportAdgroupsHsaBrand> listBrand = new LinkedList<AmzAdvReportAdgroupsHsaBrand>();
			final List<AmzAdvReportAdgroupsHsaVideo> listVideo = new LinkedList<AmzAdvReportAdgroupsHsaVideo>();
			try {
				jsonReader.startArray();
				Integer clicks = 0;
				String currency = "";
				Integer impressions = 0;
				BigDecimal cost = new BigDecimal("0");
				BigDecimal attributedsales = new BigDecimal("0");
				Integer attributedunitsordered = 0;
				Integer attributedConversions = 0;
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportAdgroupsHsa amzAdvReportHas = new AmzAdvReportAdgroupsHsa();
					AmzAdvReportAdgroupsHsaAttributed amzAdvReportAdgroupsHsaAttributed=new AmzAdvReportAdgroupsHsaAttributed();
					
					AmzAdvReportAdgroupsHsaVideo amzAdvReportHasVideo = null;
					AmzAdvReportAdgroupsHsaBrand amzAdvReportHasBrand = null;
					amzAdvReportHas.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportHas.setCampaignid(item.getBigInteger("campaignId"));
					 if(request.getLog()!=null&&request.getLog().equals("hasName")) {
		            	    AmzAdvAdgroupsHsa adgroup = amzAdvAdgroupsHsaMapper.selectByPrimaryKey(amzAdvReportHas.getAdgroupid());
		            	   if(adgroup==null) {
		            		   adgroup= new AmzAdvAdgroupsHsa();
		            		   adgroup.setProfileid(request.getProfileid());
		            		   adgroup.setCampaignid(amzAdvReportHas.getCampaignid());
		            		   adgroup.setAdgroupid(amzAdvReportHas.getAdgroupid());
		            		   adgroup.setName(item.getString("adGroupName"));
		            		   if(StrUtil.isNotBlank(adgroup.getName())) {
		            			   amzAdvAdgroupsHsaMapper.insert(adgroup);
		            		   }
		            	   }
		               }
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportHas.setBydate(request.getStartDate());
					}else {
						amzAdvReportHas.setBydate(date);
					}
					amzAdvReportHas.setProfileid(request.getProfileid());
					amzAdvReportHas.setOpttime(new Date());
					amzAdvReportHas.setClicks(item.getInteger("clicks"));
					amzAdvReportHas.setImpressions(item.getInteger("impressions"));
					amzAdvReportHas.setCost(item.getBigDecimal("cost"));

					amzAdvReportAdgroupsHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportAdgroupsHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportAdgroupsHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportAdgroupsHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					amzAdvReportAdgroupsHsaAttributed.setAdgroupid(amzAdvReportHas.getAdgroupid());
					amzAdvReportAdgroupsHsaAttributed.setBydate(amzAdvReportHas.getBydate());
					
					if(!amzAdvReportAdgroupsHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportAdgroupsHsaAttributed);
	            	}
					
	                if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
	                	amzAdvReportHasVideo=	new AmzAdvReportAdgroupsHsaVideo();
	                	amzAdvReportHasVideo.setAdgroupid(amzAdvReportHas.getAdgroupid());
	                	amzAdvReportHasVideo.setBydate(amzAdvReportHas.getBydate());
	                	amzAdvReportHasVideo.setOpttime(amzAdvReportHas.getOpttime());
	                	amzAdvReportHasVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
	                	amzAdvReportHasVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
	                	amzAdvReportHasVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
	                	amzAdvReportHasVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
	                	amzAdvReportHasVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
	                	amzAdvReportHasVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
	                	amzAdvReportHasVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
	                	amzAdvReportHasVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
	                	amzAdvReportHasVideo.setVtr(item.getBigDecimal("vtr"));
	                	amzAdvReportHasVideo.setVctr(item.getBigDecimal("vctr"));
	                	if(!amzAdvReportHasVideo.iszero()) {
	                		listVideo.add(amzAdvReportHasVideo);
	                	}
	                }else {
	                	amzAdvReportHasBrand=	new AmzAdvReportAdgroupsHsaBrand();
	                	amzAdvReportHasBrand.setAdgroupid(amzAdvReportHas.getAdgroupid());
	                	amzAdvReportHasBrand.setBydate(amzAdvReportHas.getBydate());
	                	amzAdvReportHasBrand.setOpttime(amzAdvReportHas.getOpttime());
	                	amzAdvReportHasBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
	                	amzAdvReportHasBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
	                	amzAdvReportHasBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
	                	amzAdvReportHasBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
	                	amzAdvReportHasBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
	                	amzAdvReportHasBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
	                	amzAdvReportHasBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
	                	amzAdvReportHasBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
	                	amzAdvReportHasBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
	                	amzAdvReportHasBrand.setDpv14d(item.getInteger("dpv14d"));
	                	if(!amzAdvReportHasBrand.iszero()) {
	                		listBrand.add(amzAdvReportHasBrand);
	                	}
	                }
					
					
					list.add(amzAdvReportHas);
					clicks = clicks + amzAdvReportHas.getClicks();
					cost = cost.add(amzAdvReportHas.getCost());
					
					if (currency == null && StringUtil.isNotEmpty(profile.getCurrencycode())) {
						currency = profile.getCurrencycode();
					}
					impressions = impressions + amzAdvReportHas.getImpressions();
					if(amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d() != null) {
						attributedsales = attributedsales.add(amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d());
					}
					if(amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d() != null) {
						attributedunitsordered = attributedunitsordered + 
								(amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d().intValue():0);
					}
					if (amzAdvReportAdgroupsHsaAttributed.getAttributedconversions14d() != null) {
						attributedConversions = attributedConversions +
								(amzAdvReportAdgroupsHsaAttributed.getAttributedconversions14d()!=null?amzAdvReportAdgroupsHsaAttributed.getAttributedconversions14d().intValue():0);
					}
					if (list.size() >= 2000) {
						amzAdvReportAdgroupsHsaMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportAdgroupsHsaMapper.insertBatchAttributed(listAttributed);
						listBrand.clear();
					}
					if (listVideo.size() >= 2000) {
						amzAdvReportAdgroupsHsaMapper.insertBatchVideo(listVideo);
						listVideo.clear();
					}
					if (listBrand.size() >= 2000) {
						amzAdvReportAdgroupsHsaMapper.insertBatchBrand(listBrand);
						listBrand.clear();
					}
				
				}
				if (list.size() > 0) {
					amzAdvReportAdgroupsHsaMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportAdgroupsHsaMapper.insertBatchAttributed(listAttributed);
				}
				if (listVideo.size() > 0) {
					amzAdvReportAdgroupsHsaMapper.insertBatchVideo(listVideo);
				}
				if (listBrand.size() > 0) {
					amzAdvReportAdgroupsHsaMapper.insertBatchBrand(listBrand);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}  
		}

	 
}
