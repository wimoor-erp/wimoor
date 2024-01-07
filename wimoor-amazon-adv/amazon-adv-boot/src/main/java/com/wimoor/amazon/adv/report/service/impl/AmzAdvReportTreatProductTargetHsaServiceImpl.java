package com.wimoor.amazon.adv.report.service.impl;

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
import com.wimoor.amazon.adv.sb.dao.AmzAdvProductTargeHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportProductTargeHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaVideo;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvReportTreatProductTargetHsaService")
public class AmzAdvReportTreatProductTargetHsaServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportProductTargeHsaMapper amzAdvReportProductTargeHsaMapper;
	@Resource
	AmzAdvProductTargeHsaMapper amzAdvProductTargeHsaMapper;
	 public void runRequestReportV2(AmzAdvProfile profile,JSONObject param,String url,Date startDate,Date endDate,AmzAdvReportRequestType type,List<AmzAdvRequest> list) {
			String metrics=param.get("metrics").toString();
			double distday = GeneralUtil.distanceOfDay(startDate, new Date());
			String log="";
			if(type.getId()==225&&distday>=2&&distday<3) {
				metrics=metrics+",targetId,targetingExpression,targetingText,targetingType";
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
			final List<AmzAdvReportProductTargetsHsa> list = new LinkedList<AmzAdvReportProductTargetsHsa>();
			final List<AmzAdvReportProductTargetsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportProductTargetsHsaAttributed>();

			final List<AmzAdvReportProductTargetsHsaBrand> listBrand = new LinkedList<AmzAdvReportProductTargetsHsaBrand>();
			final List<AmzAdvReportProductTargetsHsaVideo> listVideo = new LinkedList<AmzAdvReportProductTargetsHsaVideo>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportProductTargetsHsa amzAdvReportProductTargeHsa = new AmzAdvReportProductTargetsHsa();
					AmzAdvReportProductTargetsHsaBrand amzAdvReportProductTargetsHsaBrand=new AmzAdvReportProductTargetsHsaBrand();
					AmzAdvReportProductTargetsHsaVideo amzAdvReportProductTargetsHsaVideo=new AmzAdvReportProductTargetsHsaVideo();
					AmzAdvReportProductTargetsHsaAttributed amzAdvReportProductTargeHsaAttributed=new AmzAdvReportProductTargetsHsaAttributed();
					amzAdvReportProductTargeHsa.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportProductTargeHsa.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportProductTargeHsa.setTargetid(item.getBigInteger("targetId"));
					 if(request.getLog()!=null&&request.getLog().equals("hasName")) {
		            	     AmzAdvProductTargeHsa target = amzAdvProductTargeHsaMapper.selectByPrimaryKey(amzAdvReportProductTargeHsa.getTargetid());
		            	   if(target==null) {
		            		   target= new AmzAdvProductTargeHsa();
		            		   target.setProfileid(request.getProfileid());
		            		   target.setCampaignid(amzAdvReportProductTargeHsa.getCampaignid());
		            		   target.setAdgroupid(amzAdvReportProductTargeHsa.getAdgroupid());
		            		   target.setTargetid(amzAdvReportProductTargeHsa.getTargetid());
		            		   target.setExpression(item.getString("targetingExpression"));
		            		   target.setExpressiontype(item.getString("targetingType"));
		            		   if(StrUtil.isNotBlank(target.getExpression())) {
		            			   amzAdvProductTargeHsaMapper.insert(target);
		            		   }
		            	   }
		               }
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportProductTargeHsa.setBydate(request.getStartDate());
					}else {
						amzAdvReportProductTargeHsa.setBydate(date);
					}
					amzAdvReportProductTargeHsa.setProfileid(request.getProfileid());
					amzAdvReportProductTargeHsa.setClicks(item.getInteger("clicks"));
					amzAdvReportProductTargeHsa.setImpressions(item.getInteger("impressions"));
					amzAdvReportProductTargeHsa.setCost(item.getBigDecimal("cost"));
					amzAdvReportProductTargeHsa.setOpttime(new Date());
					amzAdvReportProductTargeHsaAttributed.setBydate(amzAdvReportProductTargeHsa.getBydate());
					amzAdvReportProductTargeHsaAttributed.setTargetid(amzAdvReportProductTargeHsa.getTargetid());
					amzAdvReportProductTargeHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportProductTargeHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportProductTargeHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportProductTargeHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));

					 if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
						 amzAdvReportProductTargetsHsaVideo=	new AmzAdvReportProductTargetsHsaVideo();
						 amzAdvReportProductTargetsHsaVideo.setTargetid(amzAdvReportProductTargeHsa.getTargetid());
						 amzAdvReportProductTargetsHsaVideo.setBydate(amzAdvReportProductTargeHsa.getBydate());
						 amzAdvReportProductTargetsHsaVideo.setOpttime(amzAdvReportProductTargeHsa.getOpttime());
						 amzAdvReportProductTargetsHsaVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
						 amzAdvReportProductTargetsHsaVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
						 amzAdvReportProductTargetsHsaVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
		                 amzAdvReportProductTargetsHsaVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
		                 amzAdvReportProductTargetsHsaVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
		                 amzAdvReportProductTargetsHsaVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
		                 amzAdvReportProductTargetsHsaVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
		                 amzAdvReportProductTargetsHsaVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
		                 amzAdvReportProductTargetsHsaVideo.setVtr(item.getBigDecimal("vtr"));
		                 amzAdvReportProductTargetsHsaVideo.setVctr(item.getBigDecimal("vctr"));
		                	if(amzAdvReportProductTargetsHsaVideo.iszero()) {
		                		listVideo.add(amzAdvReportProductTargetsHsaVideo);
		                	}
		                }else {
		                	amzAdvReportProductTargetsHsaBrand=	new AmzAdvReportProductTargetsHsaBrand();
		                	amzAdvReportProductTargetsHsaBrand.setTargetid(amzAdvReportProductTargeHsa.getTargetid());
		                	amzAdvReportProductTargetsHsaBrand.setBydate(amzAdvReportProductTargeHsa.getBydate());
		                	amzAdvReportProductTargetsHsaBrand.setOpttime(amzAdvReportProductTargeHsa.getOpttime());
		                	amzAdvReportProductTargetsHsaBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
		                	amzAdvReportProductTargetsHsaBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
		                	amzAdvReportProductTargetsHsaBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
		                	amzAdvReportProductTargetsHsaBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
		                	amzAdvReportProductTargetsHsaBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
		                	amzAdvReportProductTargetsHsaBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
		                	amzAdvReportProductTargetsHsaBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
		                	amzAdvReportProductTargetsHsaBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
		                	amzAdvReportProductTargetsHsaBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
		                	amzAdvReportProductTargetsHsaBrand.setDpv14d(item.getInteger("dpv14d"));
		                	if(!amzAdvReportProductTargetsHsaBrand.iszero()) {
		                		listBrand.add(amzAdvReportProductTargetsHsaBrand);
		                	}
		                }
					 
					if ((amzAdvReportProductTargeHsa.getImpressions() == null || amzAdvReportProductTargeHsa.getImpressions() == 0)) {
						continue;
					}
					list.add(amzAdvReportProductTargeHsa);
					if(!amzAdvReportProductTargeHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportProductTargeHsaAttributed);	
					}
					if (list.size() >= 2000) {
						amzAdvReportProductTargeHsaMapper.insertBatch(list);
						list.clear();
					}
					if (listVideo.size() >= 2000) {
						amzAdvReportProductTargeHsaMapper.insertBatchVideo(listVideo);
						listVideo.clear();
					}
					if (listBrand.size() >= 2000) {
						amzAdvReportProductTargeHsaMapper.insertBatchBrand(listBrand);
						listBrand.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportProductTargeHsaMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportProductTargeHsaMapper.insertBatch(list);
				}
				if (listVideo.size() > 0) {
					amzAdvReportProductTargeHsaMapper.insertBatchVideo(listVideo);
				}
				if (listBrand.size() > 0) {
					amzAdvReportProductTargeHsaMapper.insertBatchBrand(listBrand);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportProductTargeHsaMapper.insertBatchAttributed(listAttributed);
				}
			}catch(Exception e) {
				e.printStackTrace();
			} 
		}

	 
}
