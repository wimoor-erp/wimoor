package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.report.mapper.AmazonAuthMarketPerformanceMapper;
import com.wimoor.amazon.report.pojo.entity.AmazonAuthMarketPerformance;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.pojo.vo.PerformanceVo;
import com.wimoor.amazon.report.service.IReportAmzPerformanceService;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;



@Service("reportAmzPerformanceService")
public class ReportAmzPerformanceServiceImpl extends ReportServiceImpl implements IReportAmzPerformanceService {
 
	@Resource
	private ProductInfoMapper productMapper;
	@Resource
	private IProductInfoService productService;
 
    @Resource
    AmazonAuthMarketPerformanceMapper amazonAuthMarketPerformanceMapper;
    
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
    public PerformanceVo getPerformance(String shopid,String groupid) {
    	QueryWrapper<AmazonAuthority> queryauth=new QueryWrapper<AmazonAuthority>();
    	PerformanceVo vo =new PerformanceVo();
    	if(!StrUtil.isEmpty(groupid)) {
    		queryauth.eq("groupid", groupid);
    	}
    	if(!StrUtil.isEmpty(shopid)) {
    		queryauth.eq("shop_id", shopid);
    	}
    	List<AmazonAuthority> authlist = amazonAuthorityService.list(queryauth);
    	for(AmazonAuthority item:authlist) {
    		QueryWrapper<AmazonAuthMarketPerformance> query=new QueryWrapper<AmazonAuthMarketPerformance>();
    		query.eq("amazonauthid", item.getId());
    		List<AmazonAuthMarketPerformance> pflist = amazonAuthMarketPerformanceMapper.selectList(query);
    		for(AmazonAuthMarketPerformance pf:pflist) {
    			String pfjson = pf.getPerformance();
    			if(StrUtil.isEmpty(pfjson)) {
    				continue;
    			}
    			if(vo.getRefreshTime()==null||vo.getRefreshTime().before(pf.getRefreshtime()) ) {
    				vo.setRefreshTime(pf.getRefreshtime());
    			}
    			JSONObject json = GeneralUtil.getJsonObject(pfjson);
    			JSONObject mfn = json.getJSONObject("mfn");
    			if(mfn!=null) {
    				vo.setOrderDefectRate(vo.getOrderDefectRate().add(mfn.getBigDecimal("rate")));
        			if(vo.getOrderDefectRateTargetValue().compareTo(mfn.getBigDecimal("targetValue"))<0||vo.getOrderDefectRateTargetValue().compareTo(new BigDecimal("0"))==0) {
        				vo.setOrderDefectRateTargetValue(mfn.getBigDecimal("targetValue"));
        			}
    			}
    			JSONObject afn = json.getJSONObject("afn");
    			if(afn!=null) {
    				vo.setOrderDefectRate(vo.getOrderDefectRate().add(afn.getBigDecimal("rate")));
        			if(vo.getOrderDefectRateTargetValue().compareTo(afn.getBigDecimal("targetValue"))<0||vo.getOrderDefectRateTargetValue().compareTo(new BigDecimal("0"))==0) {
        				vo.setOrderDefectRateTargetValue(afn.getBigDecimal("targetValue"));
        			}
    			}
    			
    			JSONObject suspectedIntellectualPropertyViolations = json.getJSONObject("suspectedIntellectualPropertyViolations");
    			if(suspectedIntellectualPropertyViolations!=null) {
    				vo.setPolicyDefectsCount(vo.getPolicyDefectsCount()+suspectedIntellectualPropertyViolations.getInteger("defectsCount"));
    				vo.setPolicyTargetValue(vo.getPolicyTargetValue()+suspectedIntellectualPropertyViolations.getInteger("targetValue"));
    			}
    			
    			JSONObject listingPolicyViolations = json.getJSONObject("listingPolicyViolations");
    			if(listingPolicyViolations!=null) {
    				vo.setPolicyDefectsCount(vo.getPolicyDefectsCount()+listingPolicyViolations.getInteger("defectsCount"));
    				vo.setPolicyTargetValue(vo.getPolicyTargetValue()+listingPolicyViolations.getInteger("targetValue"));
    			}
    			
    			JSONObject validTrackingRate = json.getJSONObject("validTrackingRate");
    			if(validTrackingRate!=null) {
    				vo.setValidTrackingRate(vo.getValidTrackingRate().add(validTrackingRate.getBigDecimal("rate")));
        	        vo.setValidTrackingRateTargetValue(validTrackingRate.getBigDecimal("targetValue"));
        	        if(vo.getValidTrackingRateTargetValue().compareTo(validTrackingRate.getBigDecimal("targetValue"))<0||vo.getValidTrackingRateTargetValue().compareTo(new BigDecimal("0"))==0) {
        				vo.setValidTrackingRateTargetValue(validTrackingRate.getBigDecimal("targetValue"));
        			}
    			}
    			
    	        
    			JSONObject lateShipmentRate = json.getJSONObject("lateShipmentRate");
    			if(lateShipmentRate!=null) {
    				vo.setLateShipmentRate(vo.getLateShipmentRate().add(lateShipmentRate.getBigDecimal("rate")));
        			if(vo.getLateShipmentRateTargetValue().compareTo(lateShipmentRate.getBigDecimal("targetValue"))<0||vo.getLateShipmentRateTargetValue().compareTo(new BigDecimal("0"))==0) {
        				vo.setLateShipmentRateTargetValue(lateShipmentRate.getBigDecimal("targetValue"));
        			}
    			}
    			
    			JSONObject preFulfillmentCancellationRate = json.getJSONObject("preFulfillmentCancellationRate");
    			if(preFulfillmentCancellationRate!=null) {
    				vo.setCancelRate(vo.getCancelRate().add(preFulfillmentCancellationRate.getBigDecimal("rate")));
    				if(vo.getCancelRateTargetValue().compareTo(preFulfillmentCancellationRate.getBigDecimal("targetValue"))<0||vo.getCancelRateTargetValue().compareTo(new BigDecimal("0"))==0) {
    					vo.setCancelRateTargetValue(preFulfillmentCancellationRate.getBigDecimal("targetValue"));
    				}
    			}
    			JSONObject invoiceDefectRate = json.getJSONObject("invoiceDefectRate");
    			if(invoiceDefectRate!=null) {
    				vo.setInvoiceDefectRate(vo.getInvoiceDefectRate().add(invoiceDefectRate.getBigDecimal("rate")));
    				if(vo.getInvoiceDefectRateTargetValue().compareTo(invoiceDefectRate.getBigDecimal("targetValue"))<0||vo.getInvoiceDefectRateTargetValue().compareTo(new BigDecimal("0"))==0) {
    					vo.setInvoiceDefectRateTargetValue(invoiceDefectRate.getBigDecimal("targetValue"));
    				}
    			}
    			
    		}
    	}
    	
		return vo;
    	
    }

	public String treatResponse(AmazonAuthority amazonAuthority,BufferedReader br) {
		StringBuffer log = new StringBuffer();
		StringBuffer bf=new StringBuffer();
		try {
			while(br.ready()) { bf.append(br.readLine());}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bf.length()<=0)return "nodata";
		String jsonString=bf.toString();
		JSONObject json = GeneralUtil.getJsonObject(jsonString);
		JSONArray accountStatuses = json.getJSONArray("accountStatuses");
		JSONArray performanceMetrics = json.getJSONArray("performanceMetrics");
		for(int i=0;i<accountStatuses.size();i++) {
			JSONObject accountStatu = accountStatuses.getJSONObject(i);
			String amazonauthid=amazonAuthority.getId();
			String marketplaceid=accountStatu.getString("marketplaceId");
			String status=accountStatu.getString("status");
			QueryWrapper<AmazonAuthMarketPerformance> query= new QueryWrapper<AmazonAuthMarketPerformance>();
			query.eq("amazonauthid",amazonauthid );
			query.eq("marketplaceid", marketplaceid);
			AmazonAuthMarketPerformance performance = amazonAuthMarketPerformanceMapper.selectOne(query);
			if(performance==null) {
				performance=new AmazonAuthMarketPerformance();
			}
			performance.setPerformance(performanceMetrics.getString(i));
			performance.setAccountstatus(status);
			performance.setRefreshtime(new Date());
			performance.setSellerid(amazonAuthority.getSellerid());
			if(performance.getAmazonauthid()!=null) {
				amazonAuthMarketPerformanceMapper.update(performance,query);
			}else {
				performance.setAmazonauthid(amazonauthid);
				performance.setMarketplaceid(marketplaceid);
				amazonAuthMarketPerformanceMapper.insert(performance);
			}
		}

		return log.toString();
	}

	@Override
	public String myReportType() {
		return ReportType.PerformanceReport;
	}

}
