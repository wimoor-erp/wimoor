package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.orders.mapper.AmzOrderFBMReturnsMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderFBMReturns;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
 

@Service("reportFBMAmzReturnsService")
public class ReportAmzFBMReturnsServiceImpl extends ReportServiceImpl {
	@Autowired
	private AmzOrderFBMReturnsMapper amzOrderFBMReturnsMapper;
 
	
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		String log = "";
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber != 0) {
					AmzOrderFBMReturns returns = new AmzOrderFBMReturns();
					returns.setOrderId(GeneralUtil.getIndexString(info, 0));
					Date orderdate = GeneralUtil.getDatez(GeneralUtil.getIndexString(info, 1));
					returns.setPurchaseDate(orderdate);
					Date returndate = GeneralUtil.getDatez(GeneralUtil.getIndexString(info, 2));
					returns.setReturnDate(returndate);
					returns.setStatus(GeneralUtil.getIndexString(info, 3));
					returns.setAmazonRmaId(GeneralUtil.getIndexString(info, 4));
					returns.setMerchantRmaId(GeneralUtil.getIndexString(info, 5));
					returns.setLabelType(GeneralUtil.getIndexString(info, 6));
					returns.setLabelCost(GeneralUtil.getIndexString(info, 7));
					returns.setCurrency(GeneralUtil.getIndexString(info, 8));
					returns.setCarrier(GeneralUtil.getIndexString(info, 9));
					returns.setTrackingid(GeneralUtil.getIndexString(info, 10));
					returns.setLabelpaidby(GeneralUtil.getIndexString(info, 11));
					returns.setAzclaim(GeneralUtil.getIndexString(info, 12));
					returns.setIsprime( GeneralUtil.getIndexString(info, 13));
					returns.setAsin(GeneralUtil.getIndexString(info, 14));
					returns.setSku(GeneralUtil.getIndexString(info, 15));
					// String product_name =GeneralUtil.getIndexString(info, 16);
					String quantity = GeneralUtil.getIndexString(info,17);
					if(StrUtil.isNotBlank(quantity)) {
						returns.setQuantity(Integer.parseInt(quantity.trim()));
					}
					returns.setReason(GeneralUtil.getIndexString(info, 18));
					returns.setInpolicy(GeneralUtil.getIndexString(info, 19));
					returns.setReturntype(GeneralUtil.getIndexString(info, 20));
					returns.setResolution(GeneralUtil.getIndexString(info, 21));
					returns.setInvoiceNumber(GeneralUtil.getIndexString(info, 22));
					Date returnDeliveryDate = GeneralUtil.getDatez(GeneralUtil.getIndexString(info, 23));
					returns.setReturnDeliveryDate(returnDeliveryDate);
					quantity=GeneralUtil.getIndexString(info, 24);
					if(StrUtil.isNotBlank(quantity)) {
						returns.setOrderAmount(new BigDecimal(quantity.trim()));
					}
					quantity=GeneralUtil.getIndexString(info, 25);
					if(StrUtil.isNotBlank(quantity)) {
						returns.setOrderQuantity(Integer.parseInt(quantity.trim()));
					}
					returns.setSafetActionReason(GeneralUtil.getIndexString(info, 26));
					returns.setSafetClaimId(GeneralUtil.getIndexString(info, 27));
					returns.setSafetClaimState(GeneralUtil.getIndexString(info, 28));
					Date safetClaimCreationTime = GeneralUtil.getDatez(GeneralUtil.getIndexString(info, 29));
					returns.setSafetClaimCreationTime(safetClaimCreationTime);
					returns.setSafetClaimReimbursementAmount(GeneralUtil.getIndexString(info, 30));
					quantity=GeneralUtil.getIndexString(info, 31);
					if(StrUtil.isNotBlank(quantity)) {
						returns.setAmount(new BigDecimal(quantity.trim()));
					}
					String marketplaceid = amazonAuthority.getMarketPlace().getMarketplaceid();
 
					returns.setMarketplaceid(marketplaceid);
					returns.setRefreshtime(new Date());
					returns.setAmazonauthid(amazonAuthority.getId());
					returns.setSellerid(amazonAuthority.getSellerid());
					QueryWrapper<AmzOrderFBMReturns> query = new QueryWrapper<AmzOrderFBMReturns>();
					query.eq("sku", returns.getSku());
					query.eq("order_id", returns.getOrderId());
					query.eq("sellerid", returns.getSellerid());
					AmzOrderFBMReturns old = amzOrderFBMReturnsMapper.selectOne(query);
					if (old == null) {
						amzOrderFBMReturnsMapper.insert(returns);
					} else {
						amzOrderFBMReturnsMapper.update(returns,query);
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
		return ReportType.FBMReturnsReport;
	}

}
