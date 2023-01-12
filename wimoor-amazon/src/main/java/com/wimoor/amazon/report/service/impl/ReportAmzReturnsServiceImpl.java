package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.orders.mapper.AmzOrderReturnsMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderReturns;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
 

@Service("reportAmzReturnsService")
public class ReportAmzReturnsServiceImpl extends ReportServiceImpl {
	@Resource
	private AmzOrderReturnsMapper amzOrderReturnsMapper;
 
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		String log = "";
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber != 0) {
					AmzOrderReturns returns = new AmzOrderReturns();
					Date returndate = GeneralUtil.getDatez(GeneralUtil.getIndexString(info, 0));
					returns.setReturnDate(returndate);
					String amzorderid = GeneralUtil.getIndexString(info, 1);
					returns.setOrderId(amzorderid);
					String marketplaceid = amazonAuthority.getMarketPlace().getMarketplaceid();
//					if(!amazonAuthority.getMarketPlace().getRegion().equals("JP")){
//						Map<String, Object> param = new HashMap<String, Object>();
//						param.put("amazonAuthId", amazonAuthority.getId());
//						param.put("amzOrderId", amzorderid);
//						List<AmzOrderMain> result = amzOrderMainService.selectByauthId(param);
//						param.clear();
//						param=null;
//						if (result != null && result.size() > 0) {
//							if (result.get(0).getMarketplaceid() != null) {
//								marketplaceid = result.get(0).getMarketplaceid();
//							}
//							result.clear();
//							result=null;
//						}
//					}
					returns.setMarketplaceid(marketplaceid);
					String sku = GeneralUtil.getIndexString(info, 2);
					if(StrUtil.isNotEmpty(sku)  &&  sku.length()>50) {
						sku=sku.substring(0, 49);
					}
					returns.setSku(sku);
					returns.setAsin(GeneralUtil.getIndexString(info, 3));
					returns.setFnsku(GeneralUtil.getIndexString(info, 4));
					// String product_name =GeneralUtil.getIndexString(info, 5);
					String quantity = GeneralUtil.getIndexString(info, 6);
					String sellerid = amazonAuthority.getSellerid();
					returns.setQuantity(quantity);
					returns.setFulfillmentCenterId(GeneralUtil.getIndexString(info, 7));
					returns.setDetailedDisposition(GeneralUtil.getIndexString(info, 8));
					returns.setReason(GeneralUtil.getIndexString(info, 9));
					returns.setStatus(GeneralUtil.getIndexString(info, 10));
					returns.setLicensePlateNumber(GeneralUtil.getIndexString(info, 11));
					returns.setCustomerComments(EmojiFilterUtils.filterEmoji(GeneralUtil.getIndexString(info, 12)));
					returns.setSellerid(sellerid);
					QueryWrapper<AmzOrderReturns> query = new QueryWrapper<AmzOrderReturns>();
					query.eq("sku", returns.getSku());
					query.eq("return_date", returns.getReturnDate());
					query.eq("order_id", returns.getOrderId());
					query.eq("sellerid", returns.getSellerid());
					AmzOrderReturns old = amzOrderReturnsMapper.selectOne(query);
					if (old == null) {
						amzOrderReturnsMapper.insert(returns);
					} else {
						amzOrderReturnsMapper.update(returns,query);
					}
					returns=null;
				}
				info=null;
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
		return ReportType.FBAReturnsReport;
	}

}
