package com.wimoor.amazon.report.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.performance.mapper.AmzPromotionsAsinMapper;
import com.wimoor.amazon.performance.mapper.AmzPromotionsPerformanceReportMapper;
import com.wimoor.amazon.performance.pojo.entity.AmzCouponAsin;
import com.wimoor.amazon.performance.pojo.entity.AmzCouponPerformanceReport;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsAsin;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsPerformanceReport;
import com.wimoor.common.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Service("reportAmzPromotionsService")
public class ReportAmzPromotionsServiceImpl extends ReportServiceImpl {
	@Autowired
	AmzPromotionsPerformanceReportMapper amzPromotionsPerformanceReportMapper;
	@Autowired
	AmzPromotionsAsinMapper  amzPromotionsAsinMapper;
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		String log = "";
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				JSONObject result = GeneralUtil.getJsonObject(line);
				if(result!=null) {
					JSONArray promotions = result.getJSONArray("promotions");
					AmzPromotionsPerformanceReport rpt = new AmzPromotionsPerformanceReport();
					if (promotions != null) {
						for (int i = 0; i < promotions.size(); i++) {
							JSONObject promotion = promotions.getJSONObject(i);
							rpt.setPromotionid(promotion.getString("promotionId"));
							rpt.setMerchantid(promotion.getString("merchantId"));
							rpt.setAmazonauthid(amazonAuthority.getId());
							rpt.setMarketplaceid(promotion.getString("marketplaceId"));
							rpt.setCurrency(promotion.getString("revenueCurrencyCode"));
							rpt.setName(promotion.getString("promotionName"));
							rpt.setStatus(promotion.getString("status"));
							rpt.setType(promotion.getString("type"));
							rpt.setGlanceViews(promotion.getInteger("glanceViews"));
							rpt.setUnitsSold(promotion.getInteger("unitsSold"));
							rpt.setRevenue(promotion.getBigDecimal("revenue"));
							rpt.setStartDateTime(promotion.getDate("startDateTime"));
							rpt.setEndDateTime(promotion.getDate("endDateTime"));
							rpt.setCreatedDateTime(promotion.getDate("createdDateTime"));
							rpt.setLastUpdatedDateTime(promotion.getDate("lastUpdatedDateTime"));
							rpt.setRefreshtime(new Date());
							if(amzPromotionsPerformanceReportMapper.exists(new QueryWrapper<AmzPromotionsPerformanceReport>().eq("promotionid", rpt.getPromotionid()))){
								amzPromotionsPerformanceReportMapper.updateById(rpt);
							}else{
								amzPromotionsPerformanceReportMapper.insert(rpt);
							}
							JSONArray includedProducts = promotion.getJSONArray("includedProducts");
							if(includedProducts!=null) {
								for(int j=0;j<includedProducts.size();j++) {
									amzPromotionsAsinMapper.delete(new QueryWrapper<AmzPromotionsAsin>().eq("promotionid", rpt.getPromotionid()));
									JSONObject asin = includedProducts.getJSONObject(j);
									for(int k=0;k<asin.size();k++){
										AmzPromotionsAsin asinrpt=new AmzPromotionsAsin();
										asinrpt.setAsin(asin.getString("asin"));
										asinrpt.setPromotionid(rpt.getPromotionid());
										asinrpt.setCurrency(asin.getString("productRevenueCurrencyCode"));
										asinrpt.setProductGlanceViews(asin.getInteger("productGlanceViews"));
										asinrpt.setProductRevenue(asin.getBigDecimal("productRevenue"));
										asinrpt.setProductName(asin.getString("productName"));
										asinrpt.setProductUnitsSold(asin.getInteger("productUnitsSold"));
										amzPromotionsAsinMapper.delete(new QueryWrapper<AmzPromotionsAsin>()
												                          .eq("promotionid", rpt.getPromotionid())
												                          .eq("asin", asinrpt.getAsin()));
										amzPromotionsAsinMapper.insert(asinrpt);
									}
								}
							}
						}
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return log;
	}

	@Override
	public String myReportType() {
		return "GET_PROMOTION_PERFORMANCE_REPORT";
	}

	@Override
	public ReportOptions getMyOptions() {
		ReportOptions options=new ReportOptions();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -8);
		SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM-dd");
		options.put("promotionStartDateTo",FMT_YMD.format(calendar.getTime())+"T23:59:59Z");
		calendar.add(Calendar.DATE, -60);
		options.put("promotionStartDateFrom",FMT_YMD.format(calendar.getTime())+"T00:00:00Z");
		return options;
	}
}
