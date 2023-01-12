package com.wimoor.amazon.report.service.impl;


import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.orders.mapper.AmzOrderRemovesMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderRemoves;
import com.wimoor.amazon.report.pojo.entity.ReportType;

import cn.hutool.core.util.StrUtil;
 

@Service("reportAmzFBAShipmentRemoveService")
public class ReportAmzFBAShipmentRemoveServiceImpl extends ReportServiceImpl{
	@Resource
	AmzOrderRemovesMapper amzOrderRemovesMapper;

	private String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			return info[position];
		}
		return null;
	}
	
	private BigDecimal getBigDecimalValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			if(isNumericzidai(info[position])) {
				return new BigDecimal(info[position]);
			}
		}
		return null;
	}
	
	public static boolean isNumericzidai(String str) {
		if (str == null)
			return false;
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	@Override
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
		int lineNumber = 0;
		String line;
		String mlog="";
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber == 0) {
					for (int i = 0; i < info.length; i++) {
						titleList.put(info[i],i);
					}
				}else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					AmzOrderRemoves report = new AmzOrderRemoves();
					String purchasedateStr = getStrValue(info,titleList,"request-date");
					String lastUpdateStr=getStrValue(info,titleList,"last-updated-date");
					purchasedateStr = purchasedateStr.replace("/", "-");
					purchasedateStr = purchasedateStr.replace("UTC", "");
					lastUpdateStr = lastUpdateStr.replace("/", "-");
					lastUpdateStr = lastUpdateStr.replace("UTC", "");
					if(purchasedateStr.length()>19) {
						purchasedateStr=purchasedateStr.replace("T", " ");
						purchasedateStr=purchasedateStr.substring(0, 19);
					}
					if(lastUpdateStr.length()>19) {
						lastUpdateStr=lastUpdateStr.replace("T", " ");
						lastUpdateStr=lastUpdateStr.substring(0, 19);
					}
					report.setOrderId(getStrValue(info,titleList,"order-id"));
					report.setOrderType(getStrValue(info,titleList,"order-type"));
					report.setOrderStatus(getStrValue(info,titleList,"order-status"));
					report.setSku(getStrValue(info,titleList,"sku"));
					if(report.getSku().length()>50) {
						report.setSku(report.getSku().substring(0, 49));
					}
					report.setFnsku(getStrValue(info,titleList,"fnsku"));
					report.setCurrency(getStrValue(info,titleList,"currency"));
					report.setDisposition(getStrValue(info,titleList,"disposition"));
					report.setPurchaseDate(sdf.parse(purchasedateStr));
					report.setLastUpdatedDate(sdf.parse(lastUpdateStr));
					if(StrUtil.isEmpty(getStrValue(info,titleList,"requested-quantity"))) {
						report.setRequestedQuantity(null);
					}else {
						report.setRequestedQuantity(Integer.parseInt(getStrValue(info,titleList,"requested-quantity")));
					}
					if(StrUtil.isEmpty(getStrValue(info,titleList,"cancelled-quantity"))) {
						report.setCancelledQuantity(null);
					}else {
						report.setCancelledQuantity(Integer.parseInt(getStrValue(info,titleList,"cancelled-quantity")));
					}
					if(StrUtil.isEmpty(getStrValue(info,titleList,"disposed-quantity"))) {
						report.setDisposedQuantity(null);
					}else {
						report.setDisposedQuantity(Integer.parseInt(getStrValue(info,titleList,"disposed-quantity")));
					}
					if(StrUtil.isEmpty(getStrValue(info,titleList,"shipped-quantity"))) {
						report.setShippedQuantity(null);
					}else {
						report.setShippedQuantity(Integer.parseInt(getStrValue(info,titleList,"shipped-quantity")));
					}
					if(StrUtil.isEmpty(getStrValue(info,titleList,"in-process-quantity"))) {
						report.setInProcessQuantity(null);
					}else {
						report.setInProcessQuantity(Integer.parseInt(getStrValue(info,titleList,"in-process-quantity")));
					}
					report.setRemovalFee(getBigDecimalValue(info,titleList,"removal-fee"));
					report.setAmazonauthid(amazonAuthority.getId());
					QueryWrapper<AmzOrderRemoves> query=new QueryWrapper<AmzOrderRemoves>();
					query.eq("order_id", report.getOrderId());
					query.eq("sku", report.getSku());
					AmzOrderRemoves old = amzOrderRemovesMapper.selectOne(query);
					if(old!=null) {
						amzOrderRemovesMapper.update(report,query);
					}else {
						amzOrderRemovesMapper.insert(report);
					}
				}
				lineNumber++;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mlog+=e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mlog+=e.getMessage();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mlog+=e.getMessage();
		} finally {
				if(br!=null) {
				    try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mlog+=e.getMessage();
					}
				}
	     }
		return mlog;
	}
	 
	@Override
	public String myReportType() {
		return ReportType.FbaShipmentDetailRemoveReport;
	}
	
}
