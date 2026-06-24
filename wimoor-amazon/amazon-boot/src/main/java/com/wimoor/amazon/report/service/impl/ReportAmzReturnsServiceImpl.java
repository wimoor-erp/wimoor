package com.wimoor.amazon.report.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.orders.mapper.AmzOrderReturnsMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderReturns;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("reportAmzReturnsService")
public class ReportAmzReturnsServiceImpl extends ReportServiceImpl {
	@Resource
	private AmzOrderReturnsMapper amzOrderReturnsMapper;


	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		int lineNumber = 0;
		String line;
		String log = null;
		AmzOrderReturns returns = null;
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		List<AmzOrderReturns> linkedlist = new LinkedList<AmzOrderReturns>();
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber == 0) {
					for (int i = 0; i < info.length; i++) {
						titleList.put(info[i], i);
					}
				}
				if (lineNumber > 0) {
					returns = new AmzOrderReturns();
					Date returndate = GeneralUtil.getDatez(getStrValue(info, titleList, "return-date"));
					returns.setReturnDate(returndate);
					String amzorderid = getStrValue(info, titleList, "order-id");
					returns.setOrderId(amzorderid);
					String marketplaceid = amazonAuthority.getMarketPlace().getMarketplaceid();
					returns.setMarketplaceid(marketplaceid);
					String sku = getStrValue(info, titleList, "sku");
					if(StrUtil.isNotEmpty(sku) && sku.length() > 50) {
						sku = sku.substring(0, 49);
					}
					returns.setSku(sku);
					returns.setAsin(getStrValue(info, titleList, "asin"));
					returns.setFnsku(getStrValue(info, titleList, "fnsku"));
					String quantity = getStrValue(info, titleList, "quantity");
					String sellerid = amazonAuthority.getSellerid();
					returns.setQuantity(quantity);
					returns.setFulfillmentCenterId(getStrValue(info, titleList, "fulfillment-center-id"));
					returns.setDetailedDisposition(getStrValue(info, titleList, "detailed-disposition"));
					returns.setReason(getStrValue(info, titleList, "reason"));
					String licensePlateNumber = getStrValue(info, titleList, "license-plate-number");
					if(StrUtil.isEmpty(licensePlateNumber)) {
						licensePlateNumber = "";
					}
					String customerComments = EmojiFilterUtils.filterEmoji(getStrValue(info, titleList, "customer-comments"));
					returns.setLicensePlateNumber(licensePlateNumber);
					returns.setCustomerComments(customerComments);
					returns.setSellerid(sellerid);
					QueryWrapper<AmzOrderReturns> query = new QueryWrapper<AmzOrderReturns>();
					query.eq("sku", returns.getSku());
					query.eq("return_date", returns.getReturnDate());
					query.eq("license_plate_number", returns.getLicensePlateNumber());
					query.eq("order_id", returns.getOrderId());
					query.eq("sellerid", returns.getSellerid());
					amzOrderReturnsMapper.delete(query);
					linkedlist.add(returns);
					if(linkedlist.size() > 800) {
						amzOrderReturnsMapper.insertBatch(linkedlist);
						linkedlist.clear();
					}
					returns = null;
				}
				info = null;
				lineNumber++;
			}
			if(br != null) {
				br.close();
			}
			try {
				if(linkedlist.size() > 0) {
					amzOrderReturnsMapper.insertBatch(linkedlist);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(log == null){
					log = "AmzOrderReturns新增失敗！";
				}
				log += e.getMessage();
			} finally {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			titleList.clear();
			titleList = null;
		}
		return log;
	}

	private BigDecimal getBigDecimalValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			try {
				if(info[position]==null||info[position].equals("--")) {
					return null;
				}
				return new BigDecimal(info[position]);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			return info[position];
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
	public String myReportType() {
		return ReportType.FBAReturnsReport;
	}

}
