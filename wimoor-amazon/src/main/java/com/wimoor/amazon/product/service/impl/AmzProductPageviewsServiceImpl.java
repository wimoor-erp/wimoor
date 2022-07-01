package com.wimoor.amazon.product.service.impl;

import com.wimoor.amazon.product.pojo.entity.AmzProductPageviews;
import com.wimoor.amazon.product.pojo.vo.AmzProductPageviewsConditionVo;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.product.mapper.AmzProductPageviewsMapper;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.service.IAmzProductPageviewsService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 流量报表 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-28
 */
@Service
@RequiredArgsConstructor
public class AmzProductPageviewsServiceImpl extends ServiceImpl<AmzProductPageviewsMapper, AmzProductPageviews> implements IAmzProductPageviewsService {
	final IAmazonAuthorityService amazonAuthorityService;
	final IMarketplaceService marketplaceServcie;
    final ProductInOptMapper productInOptMapper;
	public void refreshDownload() {
		List<AmzProductPageviewsConditionVo> list = this.baseMapper.downloadAuth();
		for(AmzProductPageviewsConditionVo item:list) {
			   if(GeneralUtil.distanceOfMinutes(item.getOpttime(), new Date())>10) {
				   this.baseMapper.downloadRefresh(item);
				   this.baseMapper.deleteByMarketplaceid(item.getMarketplaceid(), item.getAmazonAuthid().toString(), GeneralUtil.getStrDate(item.getByday()));
			   }
		}
	}
	public String uploadSessionFile(UserInfo user, String data, String marketplaceid, String sellerid, Date day, String type)   {
		final Map<String, Integer> titleMap = new HashMap<String, Integer>();
		final String[] FILE_HEADER = { "﻿parent", "child", "title", "sku",  "sessions - total", "session percentage - total",
				"page views - total", "page views percentage - total", "buy box percentage", "units ordered", "units ordered - b2b",
				"unit session percentage", "unit session percentage - b2b", "ordered product sales",
				"ordered product sales - b2b", "total order items", "total order items - b2b" };
		AmazonAuthority auth=amazonAuthorityService.selectBySellerId(sellerid);
		if (auth == null) {
			 return null;
		}
			JSONObject result = GeneralUtil.getJsonObject(data);
			JSONObject dataobject=result.getJSONObject("data");
			JSONObject reportData=dataobject.getJSONObject("getReportData");
			JSONArray rows=reportData.getJSONArray("rows");
			JSONArray columns=reportData.getJSONArray("columns");
			if(rows==null||rows.size()<=0)return null;
			if(type!=null) {
				if(type.equals("week")) {
					day = GeneralUtil.getFirstDayofWeek(day);
					Calendar c=Calendar.getInstance();
					c.setTime(day);
					for(int i=1;i<=7;i++){
						this.baseMapper.deleteByMarketplaceid(marketplaceid, auth.getId(), GeneralUtil.getStrDate(c.getTime()));
						c.add(Calendar.DATE, 1);
					}
				}else {
					   this.baseMapper.deleteByMarketplaceid(marketplaceid, auth.getId(), GeneralUtil.getStrDate(day));
				}
			}
			for (int i = 0; i < columns.size(); i++) {
				  JSONObject key = columns.getJSONObject(i);
			      String mykey=key.getString("label");
			      if (mykey.indexOf("(") >= 0) {
			    	  mykey = mykey.substring(mykey.indexOf("(") + 1, mykey.lastIndexOf(")"));
					}
					titleMap.put(mykey.toLowerCase().trim(), i);
			}
			List<AmzProductPageviews> list = new ArrayList<AmzProductPageviews>();
			for (int i=0;i<rows.size();i++) {    
				JSONArray record = rows.getJSONArray(i);
				String parent = getSessionRecord(record, 0);
				String children = getSessionRecord(record, titleMap.get(FILE_HEADER[1]));
				String sku = getSessionRecord(record, titleMap.get(FILE_HEADER[3]));
				String session = getSessionRecord(record, titleMap.get(FILE_HEADER[4]));
				String sessionPercentage = getSessionRecord(record, titleMap.get(FILE_HEADER[5]));
				String pageView = getSessionRecord(record, titleMap.get(FILE_HEADER[6]));
				String pageViewsPercentage = getSessionRecord(record, titleMap.get(FILE_HEADER[7]));
				String buyBoxPercentage = getSessionRecord(record, titleMap.get(FILE_HEADER[8]));
				String unitsOrdered = getSessionRecord(record, titleMap.get(FILE_HEADER[9]));
				String unitsOrderedB2B = getSessionRecord(record, titleMap.get(FILE_HEADER[10]));
				String unitSessionPercentage = getSessionRecord(record, titleMap.get(FILE_HEADER[11]));
				String unitSessionPercentageB2B = getSessionRecord(record, titleMap.get(FILE_HEADER[12]));
				String orderedProductSales = getSessionRecord(record, titleMap.get(FILE_HEADER[13]));
				String orderedProductSalesB2B = getSessionRecord(record, titleMap.get(FILE_HEADER[14]));
				String totalOrderItems = getSessionRecord(record, titleMap.get(FILE_HEADER[15]));
				String totalOrderItemsB2B = getSessionRecord(record, titleMap.get(FILE_HEADER[16]));
                if(StrUtil.isEmpty(sku)) {
                	continue;
                }
                AmzProductPageviews pageViews = new AmzProductPageviews();
				pageViews.setMarketplaceid(marketplaceid);
				pageViews.setAmazonAuthid(new BigInteger(auth.getId()));
				pageViews.setByday(GeneralUtil.getDatez(GeneralUtil.getStrDate(day)));
				pageViews.setChildAsin(children);
				pageViews.setParentAsin(parent);
				pageViews.setSku(sku);
				pageViews.setSessions(GeneralUtil.getInteger(session));
				pageViews.setSessionPercentage(GeneralUtil.getBigDecimal(sessionPercentage));
				pageViews.setPageViews(GeneralUtil.getInteger(pageView));
				pageViews.setPageViewsPercentage(GeneralUtil.getBigDecimal(pageViewsPercentage));
				pageViews.setBuyBoxPercentage(GeneralUtil.getBigDecimal(buyBoxPercentage));
				pageViews.setUnitsOrdered(GeneralUtil.getInteger(unitsOrdered));
				pageViews.setUnitsOrderedB2b(GeneralUtil.getInteger(unitsOrderedB2B));
				pageViews.setUnitSessionPercentage(GeneralUtil.getBigDecimal(unitSessionPercentage));
				pageViews.setUnitSessionPercentageB2b(GeneralUtil.getBigDecimal(unitSessionPercentageB2B));
				pageViews.setOrderedProductSales(GeneralUtil.getBigDecimal(orderedProductSales));
				pageViews.setOrderedProductSalesB2b(GeneralUtil.getBigDecimal(orderedProductSalesB2B));
				pageViews.setTotalOrderItems(GeneralUtil.getInteger(totalOrderItems));
				pageViews.setTotalOrderItemsB2b(GeneralUtil.getInteger(totalOrderItemsB2B));
				pageViews.setOpttime(new Date());
				list.add(pageViews);
				if (list.size() >= 200) {
					this.baseMapper.saveBatch(list);
					list.clear();
				}
			}
			if (list.size() > 0) {
				this.baseMapper.saveBatch(list);
				list.clear();
				list = null;
			}
		return null;
	}
	
	private String getSessionRecord(JSONArray record, Integer integer) {
		if (integer != null) {
			return record.getString(integer);
		} else {
			return null;
		}
	}
 
	public void refreshSummary() {
		// TODO Auto-generated method stub
		Calendar c=Calendar.getInstance();
		c.add(Calendar.DATE, -3);
		List<AmazonAuthority> authlist = amazonAuthorityService.getAllAuth();
		for(AmazonAuthority amazonAuthority:authlist) {
			List<Marketplace> marketlist = marketplaceServcie.findbyauth(amazonAuthority.getId());
			for(Marketplace market:marketlist) {
	            Map<String, Object> param=new HashMap<String,Object>();
				param.put("amazonAuthid", amazonAuthority.getId());
				param.put("begin", GeneralUtil.formatDate(c.getTime()));
				param.put("marketplaceid", market.getMarketplaceid());
				this.baseMapper.refreshSummaryWeek(param);
				this.baseMapper.refreshSummaryMonth(param);
				productInOptMapper.updateBySessionRpt(market.getMarketplaceid(), amazonAuthority.getId());
			}
		}
	}
}
