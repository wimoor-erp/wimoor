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
import com.wimoor.amazon.adv.sb.dao.AmzAdvKeywordsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportKeywordsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaVideo;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvReportTreatKeywordsHsaService")
public class AmzAdvReportTreatKeywordsHsaServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportKeywordsHsaMapper amzAdvReportKeywordsHsaMapper;
	@Resource
	AmzAdvKeywordsHsaMapper amzAdvKeywordsHsaMapper;
	 public void runRequestReportV2(AmzAdvProfile profile,JSONObject param,String url,Date startDate,Date endDate,AmzAdvReportRequestType type,List<AmzAdvRequest> list) {
			String metrics=param.get("metrics").toString();
			double distday = GeneralUtil.distanceOfDay(startDate, new Date());
			String log="";
			if(type.getId()==227&&distday>=2&&distday<3) {
				metrics=metrics+",keywordBid,keywordId,keywordStatus,keywordText,matchType";
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
			final List<AmzAdvReportKeywordsHsa> list = new LinkedList<AmzAdvReportKeywordsHsa>();
			final List<AmzAdvReportKeywordsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportKeywordsHsaAttributed>();
			final List<AmzAdvReportKeywordsHsaBrand> listBrand = new LinkedList<AmzAdvReportKeywordsHsaBrand>();
			final List<AmzAdvReportKeywordsHsaVideo> listVideo = new LinkedList<AmzAdvReportKeywordsHsaVideo>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportKeywordsHsa amzAdvReportHasKeyword = new AmzAdvReportKeywordsHsa();
					AmzAdvReportKeywordsHsaAttributed amzAdvReportKeywordsHsaAttributed = new AmzAdvReportKeywordsHsaAttributed();
					
					AmzAdvReportKeywordsHsaVideo amzAdvReportHasKeywordVideo = null;
					AmzAdvReportKeywordsHsaBrand amzAdvReportHasKeywordBrand = null;
					amzAdvReportHasKeyword.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportHasKeyword.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportHasKeyword.setKeywordid(item.getBigInteger("keywordId"));
					amzAdvReportHasKeyword.setKeywordbid(item.getBigDecimal("keywordBid"));
					 if(request.getLog()!=null&&request.getLog().equals("hasName")) {
		            	   AmzAdvKeywordsHsa keyword = amzAdvKeywordsHsaMapper.selectByPrimaryKey(amzAdvReportHasKeyword.getKeywordid());
		            	   if(keyword==null) {
		            		   keyword= new AmzAdvKeywordsHsa();
		            		   keyword.setProfileid(request.getProfileid());
		            		   keyword.setCampaignid(amzAdvReportHasKeyword.getCampaignid());
		            		   keyword.setAdgroupid(amzAdvReportHasKeyword.getAdgroupid());
		            		   keyword.setKeywordid(amzAdvReportHasKeyword.getKeywordid());
		            		   keyword.setCampaigntype("hsa");
		            		   keyword.setKeywordtext(item.getString("keywordText"));
		            		   keyword.setState(item.getString("keywordStatus"));
		            		   keyword.setBid(item.getBigDecimal("keywordBid"));
		            		   keyword.setMatchtype(item.getString("matchType"));
		            		   if(StrUtil.isNotBlank(keyword.getKeywordtext())) {
		            			   amzAdvKeywordsHsaMapper.insert(keyword);
		            		   }
		            	   }
		               }
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportHasKeyword.setBydate(request.getStartDate());
					}else {
						amzAdvReportHasKeyword.setBydate(date);
					}
					amzAdvReportHasKeyword.setProfileid(request.getProfileid());
					amzAdvReportHasKeyword.setOpttime(new Date());
					amzAdvReportHasKeyword.setClicks(item.getInteger("clicks"));
					amzAdvReportHasKeyword.setImpressions(item.getInteger("impressions"));
					amzAdvReportHasKeyword.setCost(item.getBigDecimal("cost"));
					if ((amzAdvReportHasKeyword.getClicks() == null || amzAdvReportHasKeyword.getClicks() == 0)
							&& (amzAdvReportHasKeyword.getImpressions() == null || amzAdvReportHasKeyword.getImpressions() == 0)
							&& (amzAdvReportHasKeyword.getCost() == null || amzAdvReportHasKeyword.getCost().compareTo(new BigDecimal("0")) == 0)
							) {
						continue;
					}
					amzAdvReportKeywordsHsaAttributed.setBydate(amzAdvReportHasKeyword.getBydate());
					amzAdvReportKeywordsHsaAttributed.setKeywordid(amzAdvReportHasKeyword.getKeywordid());
					amzAdvReportKeywordsHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportKeywordsHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportKeywordsHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportKeywordsHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					 if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
		                	amzAdvReportHasKeywordVideo=	new AmzAdvReportKeywordsHsaVideo();
		                	amzAdvReportHasKeywordVideo.setKeywordid(amzAdvReportHasKeyword.getKeywordid());
		                	amzAdvReportHasKeywordVideo.setBydate(amzAdvReportHasKeyword.getBydate());
		                	amzAdvReportHasKeywordVideo.setOpttime(amzAdvReportHasKeyword.getOpttime());
		                	amzAdvReportHasKeywordVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
		                	amzAdvReportHasKeywordVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
		                	amzAdvReportHasKeywordVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
		                	amzAdvReportHasKeywordVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
		                	amzAdvReportHasKeywordVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
		                	amzAdvReportHasKeywordVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
		                	amzAdvReportHasKeywordVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
		                	amzAdvReportHasKeywordVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
		                	amzAdvReportHasKeywordVideo.setVtr(item.getBigDecimal("vtr"));
		                	amzAdvReportHasKeywordVideo.setVctr(item.getBigDecimal("vctr"));
		                	if(!amzAdvReportHasKeywordVideo.iszero()) {
		                		listVideo.add(amzAdvReportHasKeywordVideo);
		                	}
		                }else {
		                	amzAdvReportHasKeywordBrand=	new AmzAdvReportKeywordsHsaBrand();
		                	amzAdvReportHasKeywordBrand.setKeywordid(amzAdvReportHasKeyword.getKeywordid());
		                	amzAdvReportHasKeywordBrand.setBydate(amzAdvReportHasKeyword.getBydate());
		                	amzAdvReportHasKeywordBrand.setOpttime(amzAdvReportHasKeyword.getOpttime());
		                	amzAdvReportHasKeywordBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
		                	amzAdvReportHasKeywordBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
		                	amzAdvReportHasKeywordBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
		                	amzAdvReportHasKeywordBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
		                	amzAdvReportHasKeywordBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
		                	amzAdvReportHasKeywordBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
		                	amzAdvReportHasKeywordBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
		                	amzAdvReportHasKeywordBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
		                	amzAdvReportHasKeywordBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
		                	amzAdvReportHasKeywordBrand.setDpv14d(item.getInteger("dpv14d"));
		                	if(!amzAdvReportHasKeywordBrand.iszero()) {
		                		listBrand.add(amzAdvReportHasKeywordBrand);
		                	}
		                }
					if(!amzAdvReportKeywordsHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportKeywordsHsaAttributed);
					}
					
					list.add(amzAdvReportHasKeyword);
					if (list.size() >= 2000) {
						amzAdvReportKeywordsHsaMapper.insertBatch(list);
						list.clear();
					}
					if (listVideo.size() >= 2000) {
						amzAdvReportKeywordsHsaMapper.insertBatchVideo(listVideo);
						listVideo.clear();
					}
					if (listBrand.size() >= 2000) {
						amzAdvReportKeywordsHsaMapper.insertBatchBrand(listBrand);
						listBrand.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportKeywordsHsaMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportKeywordsHsaMapper.insertBatch(list);
				}
				if (listVideo.size() > 0) {
					amzAdvReportKeywordsHsaMapper.insertBatchVideo(listVideo);
				}
				if (listBrand.size() > 0) {
					amzAdvReportKeywordsHsaMapper.insertBatchBrand(listBrand);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportKeywordsHsaMapper.insertBatchAttributed(listAttributed);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}  
		}

	 
}
