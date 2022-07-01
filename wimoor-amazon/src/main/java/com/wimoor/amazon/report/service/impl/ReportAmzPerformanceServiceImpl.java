package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.report.mapper.AmazonAuthMarketPerformanceMapper;
import com.wimoor.amazon.report.pojo.entity.AmazonAuthMarketPerformance;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.common.GeneralUtil;

 

@Service("reportAmzPerformanceService")
public class ReportAmzPerformanceServiceImpl extends ReportServiceImpl {
 
	@Resource
	private ProductInfoMapper productMapper;
	@Resource
	private IProductInfoService productService;
 
    @Resource
    AmazonAuthMarketPerformanceMapper amazonAuthMarketPerformanceMapper;
 

	public String treatResponse(AmazonAuthority amazonAuthority,BufferedReader br) {
		/**
		 * 
		 * 0 item-name 1 item-description 2 listing-id 3 seller-sku 4 price 5 quantity 6
		 * open-date 7 image-url 8 item-is-marketplace 9 product-id-type 10
		 * zshop-shipping-fee 11 item-note 12 item-condition 13 zshop-category1 14
		 * zshop-browse-path 15 zshop-storefront-feature 16 asin1 17 asin2 18 asin3 19
		 * will-ship-internationally 20 expedited-shipping 21 zshop-boldface 22
		 * product-id 23 bid-for-featured-placement 24 add-delete 25 pending-quantity 26
		 * fulfillment-channel 27 Business Price 28 Quantity Price Type 29 Quantity
		 * Lower Bound 1 30 Quantity Price 1 31 Quantity Lower Bound 2 32 Quantity Price
		 * 2 33 Quantity Lower Bound 3 34 Quantity Price 3 35 Quantity Lower Bound 4 36
		 * Quantity Price 4 37 Quantity Lower Bound 5 38 Quantity Price 5 39
		 * merchant-shipping-group
		 * 
		 * @param amazonAuthority
		 * @param input
		 * @throws ServiceException
		 * @throws BaseException
		 */
		StringBuffer log = new StringBuffer();
		StringBuffer bf=new StringBuffer();
		try {
			while(br.ready()) { bf.append(br.readLine());}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bf.length()<=0)return "nodata";
		AmazonAuthMarketPerformance performance = amazonAuthMarketPerformanceMapper.selectById(amazonAuthority.getId());
		if(performance==null) {
			performance=new AmazonAuthMarketPerformance();
		}
		performance.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
		performance.setRefreshtime(new Date());
		performance.setSellerid(amazonAuthority.getSellerid());
		performance.setPerformance(bf.toString());
		if(performance.getAmazonauthid()!=null) {
			amazonAuthMarketPerformanceMapper.updateById(performance);
		}else {
			performance.setAmazonauthid(amazonAuthority.getId());
			amazonAuthMarketPerformanceMapper.insert(performance);
		}
		return log.toString();
	}

	@Override
	public String myReportType() {
		return ReportType.PerformanceReport;
	}

}
