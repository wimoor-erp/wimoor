package com.wimoor.amazon.report.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.performance.mapper.AmzCouponAsinMapper;
import com.wimoor.amazon.performance.mapper.AmzCouponPerformanceReportMapper;
import com.wimoor.amazon.performance.pojo.entity.AmzCouponAsin;
import com.wimoor.amazon.performance.pojo.entity.AmzCouponPerformanceReport;
import com.wimoor.amazon.util.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Service("reportAmzCouponService")
public class ReportAmzCouponServiceImpl extends ReportServiceImpl {
	@Autowired
	AmzCouponPerformanceReportMapper amzCouponPerformanceReportMapper;
	@Autowired
	AmzCouponAsinMapper amzCouponAsinMapper;
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		String log = "";
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				JSONObject result = GeneralUtil.getJsonObject(line);
				if(result!=null) {
					JSONArray coupons = result.getJSONArray("coupons");
						AmzCouponPerformanceReport rpt=new AmzCouponPerformanceReport();
						if(coupons!=null) {
							for(int i=0;i<coupons.size();i++) {
								JSONObject coupon = coupons.getJSONObject(i);
								rpt.setCouponid(coupon.getString("couponId"));
								rpt.setMerchantid(coupon.getString("merchantId"));
								rpt.setAmazonauthid(amazonAuthority.getId());
								rpt.setMarketplaceid(coupon.getString("marketplaceId"));
								rpt.setCurrency(coupon.getString("currencyCode"));
								rpt.setName(EmojiFilterUtils.filterEmoji(coupon.getString("name")));
								rpt.setWebsitMessage(EmojiFilterUtils.filterEmoji(coupon.getString("websiteMessage")));
								rpt.setStartDateTime(coupon.getDate("startDateTime"));
								rpt.setEndDateTime(coupon.getDate("endDateTime"));
								rpt.setDiscountType(coupon.getString("discountType"));
								rpt.setDiscountAmount(coupon.getBigDecimal("discountAmount"));
								rpt.setTotalDiscount(coupon.getBigDecimal("totalDiscount"));
								rpt.setClips(coupon.getInteger("clips"));
								rpt.setRedemptions(coupon.getInteger("redemptions"));
								rpt.setBudget(coupon.getBigDecimal("budget"));
								rpt.setBudgetSpend(coupon.getBigDecimal("budgetSpent"));
								rpt.setBudgetRemaining(coupon.getBigDecimal("budgetRemaining"));
								rpt.setBudgetPercentageUsed(coupon.getBigDecimal("budgetPercentageUsed"));
								rpt.setSales(coupon.getBigDecimal("sales"));
								rpt.setRefreshtime(new Date());
								if(amzCouponPerformanceReportMapper.exists(new QueryWrapper<AmzCouponPerformanceReport>().eq("couponid", rpt.getCouponid()))){
									amzCouponPerformanceReportMapper.updateById(rpt);
								}else{
									amzCouponPerformanceReportMapper.insert(rpt);
								}
								JSONArray asins = coupon.getJSONArray("asins");
								if(asins!=null) {
									for(int j=0;j<asins.size();j++) {
										amzCouponAsinMapper.delete(new QueryWrapper<AmzCouponAsin>().eq("couponid", rpt.getCouponid()));
										JSONObject asin = asins.getJSONObject(j);
										 for(int k=0;k<asin.size();k++){
											 AmzCouponAsin asinrpt=new AmzCouponAsin();
											 asinrpt.setAsin(asin.getString("asin"));
											 asinrpt.setCouponid(rpt.getCouponid());
											 amzCouponAsinMapper.insert(asinrpt);
										}
									}
								}
							}
					}
				}
				lineNumber++;
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
		return "GET_COUPON_PERFORMANCE_REPORT";
	}

	@Override
	public ReportOptions getMyOptions() {
		ReportOptions options=new ReportOptions();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -8);
		SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM-dd");
		options.put("campaignStartDateTo",FMT_YMD.format(calendar.getTime())+"T23:59:59Z");
		calendar.add(Calendar.DATE, -60);
		options.put("campaignStartDateFrom",FMT_YMD.format(calendar.getTime())+"T00:00:00Z");
		return options;
	}
}
