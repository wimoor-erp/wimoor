package com.wimoor.amazon.report.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.performance.mapper.AmzOrderFeedbackMapper;
import com.wimoor.amazon.performance.mapper.AmzPromotionsAsinMapper;
import com.wimoor.amazon.performance.mapper.AmzPromotionsPerformanceReportMapper;
import com.wimoor.amazon.performance.pojo.entity.AmzOrderFeedback;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsAsin;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsPerformanceReport;
import com.wimoor.common.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Service("reportAmzFeedbackService")
public class ReportAmzFeedbackServiceImpl extends ReportServiceImpl {
    @Autowired
	AmzOrderFeedbackMapper amzOrderFeedbackMapper;
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		String log = "";
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber != 0) {
					String date = GeneralUtil.getIndexString(info, 0);
					AmzOrderFeedback feedback=new AmzOrderFeedback();
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
					SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");
					if(amazonAuthority.getRegion().equals("NA")){
                        if(Integer.parseInt(date.split("/")[1])>12){
							sdf2 = new SimpleDateFormat("MM/dd/yy");
						}
					}
					SimpleDateFormat sdf3 = new SimpleDateFormat("dd.MM.yy");
                    try {
						if(date.length()==10){
							feedback.setFeeddate(sdf1.parse(date));
						}else if(date.length()==8&&date.indexOf("/")>0){
							feedback.setFeeddate(sdf2.parse(date));
						}else if(date.length()==8&&date.indexOf(".")>0){
							feedback.setFeeddate(sdf3.parse(date));
						}
                    } catch (ParseException e) {
						 feedback.setFeeddate(GeneralUtil.getDatez(date));
                    }
                    BigDecimal rate = GeneralUtil.getIndexBigDecimal(info, 1);
					feedback.setRating(rate!=null?rate.intValue():null);
					feedback.setComments(GeneralUtil.getIndexString(info, 2));
					feedback.setResponse(GeneralUtil.getIndexString(info, 3));
					feedback.setAmazonOrderId(GeneralUtil.getIndexString(info, 4));
					feedback.setAmazonauthid(amazonAuthority.getId());
					feedback.setRefreshtime(new Date());
					feedback.setEmail(GeneralUtil.getIndexString(info, 5));
					if(amzOrderFeedbackMapper.exists(new LambdaQueryWrapper<AmzOrderFeedback>().eq(AmzOrderFeedback::getAmazonOrderId, feedback.getAmazonOrderId()))){
						amzOrderFeedbackMapper.updateById(feedback);
					}else{
						amzOrderFeedbackMapper.insert(feedback);
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
		return "GET_SELLER_FEEDBACK_DATA";
	}


}
