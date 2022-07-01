package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.report.mapper.AmzProductActiveDayNumMapper;
import com.wimoor.amazon.report.mapper.AmzProductActiveMapper;
import com.wimoor.amazon.report.pojo.entity.AmzProductActive;
import com.wimoor.amazon.report.pojo.entity.AmzProductActiveDayNum;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
 
@Service("reportAmzProductActiveListService")
public class ReportAmzProductActiveListServiceImpl extends ReportServiceImpl{
	@Resource
	private AmzProductActiveMapper amzProductActiveMapper;
	@Resource
	private AmzProductActiveDayNumMapper amzProductActiveDayNumMapper;
	/**
	 * item-name	
	 * item-description	
	 * listing-id	
	 * seller-sku	
	 * price	
	 * quantity	
	 * open-date	
	 * image-url	
	 * item-is-marketplace	
	 * product-id-type	
	 * zshop-shipping-fee	
	 * item-note	
	 * item-condition	
	 * zshop-category1	
	 * zshop-browse-path	
	 * zshop-storefront-feature	
	 * asin1	
	 * asin2	
	 * asin3	
	 * will-ship-internationally	
	 * expedited-shipping	
	 * zshop-boldface	
	 * product-id	
	 * bid-for-featured-placement	
	 * add-delete	
	 * pending-quantity	
	 * fulfillment-channel	
	 * Business Price	
	 * Quantity Price 
	 * Type	
	 * Quantity Lower Bound 1	
	 * Quantity Price 1	
	 * Quantity Lower Bound 2	
	 * Quantity Price 2	
	 * Quantity Lower Bound 3	
	 * Quantity Price 3	
	 * Quantity Lower Bound 4	
	 * Quantity Price 4	
	 * Quantity Lower Bound 5	
	 * Quantity Price 5	
	 * @throws IOException 
	 */
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		StringBuffer mlog = new StringBuffer();
		Marketplace market = amazonAuthority.getMarketPlace();
 
	 
		int lineNumber = 0;
		String line;
		List<AmzProductActive> list = new ArrayList<AmzProductActive>();
		Map<String, Integer> indexMap = new HashMap<String, Integer>();
		Set<String> skuset = new HashSet<String>();
		Boolean isJP = "A1VC38T7YXB528".equals(amazonAuthority.getMarketPlace().getMarketplaceid());
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				String asin = null;
				String sku = null;
				String price = null;
				String opendate = null;
				Date openDate = null;
				if (lineNumber == 0) {
					QueryWrapper<AmzProductActive> query = new QueryWrapper<AmzProductActive>();
					query.eq("amazonauthid", amazonAuthority.getId());
					query.eq("marketplaceid", market.getMarketplaceid());
					amzProductActiveMapper.delete(query);
					for (int i = 0; i < info.length; i++) {
						indexMap.put(info[i], i);
					}
				} else {
					if (info.length < indexMap.size()) {
						continue;
					}
					if(info.length>indexMap.size()) {
						if (indexMap.containsKey("seller-sku")) {
							sku = info[indexMap.get("seller-sku")+1];
						} else {
							continue;
						}
						if (indexMap.containsKey("price")) {
							price = info[indexMap.get("price")+1];
						} else {
							price = "0";
						}
						if (indexMap.containsKey("open-date")) {
							opendate = info[indexMap.get("open-date")+1];
						} else {
							opendate = null;
						}
						if (indexMap.containsKey("asin1")) {
							asin = info[indexMap.get("asin1")+1];
						} else {
							asin = "";
						}
					} else {
						if (indexMap.containsKey("seller-sku")) {
							sku = info[indexMap.get("seller-sku")];
						} else {
							continue;
						}
						if (indexMap.containsKey("price")) {
							price = info[indexMap.get("price")];
						} else {
							price = "0";
						}
						if (indexMap.containsKey("open-date")) {
							opendate = info[indexMap.get("open-date")];
						} else {
							opendate = null;
						}
						if (indexMap.containsKey("asin1")) {
							asin = info[indexMap.get("asin1")];
						} else {
							asin = "";
						}
					}
					AmzProductActive active = new AmzProductActive();
					active.setAmazonauthid(amazonAuthority.getId());
					active.setMarketplaceid(market.getMarketplaceid());
					active.setAsin(asin);
					active.setSku(sku);
					if (StrUtil.isNotEmpty(opendate)) {
						SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");// 2019-12-06 03:30:06 PST, 05/09/2018 09:58:06 MEST
						if (isJP) {
							sdf1 = new SimpleDateFormat("yyyy/MM/dd");
							sdf1.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
						}
						try {
							if(opendate.length()>10){
								openDate = sdf1.parse(opendate.substring(0, 10));// 05/09/2018
							} else {
								System.out.println(opendate);
							}
						} catch (ParseException e) {
							if(opendate.length()>10){
								openDate = GeneralUtil.getDatez(opendate.substring(0, 10));
							} else {
								System.out.println(opendate);
							}
						}

					}
					if (openDate!=null) {
						active.setOpendate(openDate);
					}
					if (StrUtil.isNotEmpty(price)){
						try {
							active.setPrice(new BigDecimal(price));
						} catch (Exception e) {
							mlog.append("SKU:"+sku+"的价格("+price+")格式化错误！");
							continue;
						}
					}
					if (sku == null || skuset.contains(sku.trim().toUpperCase())) {
						continue;
					}
					skuset.add(sku.trim().toUpperCase());
					list.add(active);
					amzProductActiveMapper.insert(active);

				}
				info=null;
				lineNumber++;
			}
		 
			AmzProductActiveDayNum record = new AmzProductActiveDayNum();
			record.setAmazonauthid(amazonAuthority.getId());
			record.setMarketplaceid(market.getMarketplaceid());
			record.setByday(new Date());
			if (lineNumber != 0) {
				record.setNum(lineNumber - 1);
			} else
				record.setNum(0);
			AmzProductActiveDayNum old = amzProductActiveDayNumMapper.selectById(record);
			if (old == null) {
				amzProductActiveDayNumMapper.insert(record);
			} else {
				amzProductActiveDayNumMapper.updateById(record);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mlog.append(e.getMessage());
		} finally {
		    indexMap.clear();
		    indexMap=null;
			if (skuset != null) {
				skuset.clear();
				skuset = null;
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mlog.append(e.getMessage());
				}
			}
		}
		return mlog.toString();

	}

	@Override
	public String myReportType() {
		return ReportType.ProductActiveListings;
	}

	
}
