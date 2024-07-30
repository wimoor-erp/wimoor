package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
 
 

@Service("reportAmzProductAllListService")
public class ReportAmzProductAllListServiceImpl extends ReportServiceImpl {
	@Resource
	private ProductInfoMapper productInfoMapper;
	@Resource
	private IProductInfoService productInfoService;

	public String treatResponse(AmazonAuthority amazonAuthority,BufferedReader br )  {
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
		StringBuffer mlog = new StringBuffer();
		int lineNumber = 0;
		String line;
		Boolean isJP = "A1VC38T7YXB528".equals(amazonAuthority.getMarketPlace().getMarketplaceid());
		Boolean isFr = "A13V1IB3VIYZZH".equals(amazonAuthority.getMarketPlace().getMarketplaceid());
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				int length = info.length;
				String asin = null;
				String sku = null;
				String name = null;
				String open_date = null;
				Date openDate = null;
				String fulfillment_channel = null;
				String price = null;
				if (lineNumber > 0) {
					if (isJP || isFr) {
						/*
						 * 0 item-name	
						 * 1 item-description	
						 * 2 listing-id	
						 * 3 seller-sku	
						 * 4 price	
						 * 5 quantity	
						 * 6 open-date	
						 * 7 image-url	
						 * 8 item-is-marketplace	
						 * 9 product-id-type	
						 * 10 zshop-shipping-fee	
						 * 11 item-note	
						 * 12 item-condition	
						 * 13 zshop-category1	
						 * 14 zshop-browse-path	
						 * 15 zshop-storefront-feature	
						 * 16 asin1	
						 * 17 asin2	
						 * 18 asin3	
						 * 19 will-ship-internationally	
						 * 20 expedited-shipping	
						 * 21 zshop-boldface	
						 * 22 product-id	
						 * 23 bid-for-featured-placement	
						 * 24 add-delete	
						 * 25 pending-quantity	
						 * 26 fulfillment-channel	
						 * 27 merchant-shipping-group	
						 * 28 status
						 * uk
						 * 29 Minimum order quantity
						 * 30 Sell remainder
						 * 0 item-name 1 listing-id 2 seller-sku 3 price 4 quantity 5 open-date 6
						 * product-id-type 7 item-note 8 item-condition 9 will-ship-internationally 10
						 * expedited-shipping 11 product-id 12 pending-quantity 13 fulfillment-channel
						 * 14 merchant-shipping-group 15 status
						 */

						// 商品名 出品ID 出品者SKU 価格 数量 出品日 商品IDタイプ コンディション説明 コンディション
						// 国外へ配送可 迅速な配送 商品ID 在庫数 フルフィルメント・チャンネル
						// merchant-shipping-group ステータス

						name = GeneralUtil.getIndexString(info, 0);
						sku = GeneralUtil.getIndexString(info, 2);
						price = GeneralUtil.getIndexString(info, 3);
						open_date = GeneralUtil.getIndexString(info, 5);
						asin = GeneralUtil.getIndexString(info,11);
						fulfillment_channel = GeneralUtil.getIndexString(info, 13);
						if (isJP && length >= 15 && "Incomplete".equals(info[14])) {
							continue;
						}
					} else {
						/*
						 * 0 item-name	
						 * 1 item-description	
						 * 2 listing-id	
						 * 3 seller-sku	
						 * 4 price	
						 * 5 quantity	
						 * 6 open-date	
						 * 7 image-url	
						 * 8 item-is-marketplace	
						 * 9 product-id-type	
						 * 10 zshop-shipping-fee	
						 * 11 item-note	
						 * 12 item-condition	
						 * 13 zshop-category1	
						 * 14 zshop-browse-path	
						 * 15 zshop-storefront-feature	
						 * 16 asin1	
						 * 17 asin2	
						 * 18 asin3	
						 * 19 will-ship-internationally	
						 * 20 expedited-shipping	
						 * 21 zshop-boldface	
						 * 22 product-id	
						 * 23 bid-for-featured-placement	
						 * 24 add-delete	
						 * 25 pending-quantity	
						 * 26 fulfillment-channel	
						 * 27 merchant-shipping-group	
						 * 28 status
						 * uk
						 * 29 Minimum order quantity
						 * 30 Sell remainder
						 */
						name = GeneralUtil.getIndexString(info, 0);
						price = GeneralUtil.getIndexString(info, 4);
						if ((price != null && price.trim().length() == 0) || GeneralUtil.isNumericzidai(price)) {
							sku = GeneralUtil.getIndexString(info, 3);
							price = GeneralUtil.getIndexString(info, 4);
							open_date = GeneralUtil.getIndexString(info, 6);
							asin = GeneralUtil.getIndexString(info, 16);
							fulfillment_channel = GeneralUtil.getIndexString(info, 26);
						} else {// 可能出现name被截断，占用2个位置
							sku = GeneralUtil.getIndexString(info, 4);
							price = GeneralUtil.getIndexString(info, 5);
							open_date = GeneralUtil.getIndexString(info, 7);
							asin = GeneralUtil.getIndexString(info, 17);
							fulfillment_channel = GeneralUtil.getIndexString(info, 27);
						}
					}
					if(name!=null && name.length()>=1000){
						name = name.substring(0, 1000);
					} 
					if (StrUtil.isEmpty(sku)) {
						continue;
					}
					if (StrUtil.isNotEmpty(open_date)) {
						SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");// 16/04/2020 03:51:20 BST	
						if (isJP) {
							sdf1 = new SimpleDateFormat("yyyy/MM/dd");
							sdf1.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
						}
						try {
							if(open_date.length()>10){
								 if(open_date.contains(",")) {
										SimpleDateFormat sdf=new SimpleDateFormat("MMM dd, yyyy",Locale.US);
										try {
											openDate=sdf.parse(open_date);
										} catch (ParseException e1) {
											e1.printStackTrace();
										}
								 }else {
									 openDate = sdf1.parse(open_date.substring(0, 10));//16/04/2020
								 }
								
							} else {
								System.out.println(open_date);
							}
						} catch (ParseException e) {
							if(open_date.length()>10){
								 if(open_date.contains(",")) {
										SimpleDateFormat sdf=new SimpleDateFormat("MMM dd, yyyy",Locale.US);
										try {
											openDate=sdf.parse(open_date);
										} catch (ParseException e1) {
											e1.printStackTrace();
										}
								 }else {
									 openDate = GeneralUtil.getDatez(open_date.substring(0, 10));
								 }
							} else {
								System.out.println(open_date);
							}
						}

					}
					ProductInfo productInfo = productInfoService.productOnlyone(amazonAuthority.getId(),sku,
							amazonAuthority.getMarketPlace().getMarketplaceid());
					if (productInfo != null) {
						if (productInfo.getOpenDate() == null || LocalDateTime.now().isBefore(productInfo.getOpenDate())) {
							ZoneId zoneid=ZoneId.of("Asia/Shanghai");
							productInfo.setOpenDate(LocalDateTime.ofInstant(openDate.toInstant(), zoneid));
						}
						if (StrUtil.isNotEmpty(price)) {
							BigDecimal buyprice = new BigDecimal(price.trim().toString());
							productInfo.setPrice(buyprice);
						}
						productInfo.setFulfillChannel(fulfillment_channel);
						productInfo.setInvalid(Boolean.FALSE);
						if(productInfo.getName()==null||!productInfo.getName().equals(name)) {
							productInfo.setName(name);
							productInfo.setRefreshtime(LocalDateTime.now());
						}
						try {
							if(!productInfo.getSku().equals(sku)) {
								productInfo.setSku(sku);
							}
							productInfoMapper.updateById(productInfo);
						} catch (Exception e) {
							e.printStackTrace();
							mlog.append("productInfo 更新失败，" + e.getMessage());
						}
					} else {
						productInfo = new ProductInfo();
						productInfo.setId(amazonAuthorityService.getUUID());
						productInfo.setName(name);
						productInfo.setFulfillChannel(fulfillment_channel);
						productInfo.setSku(sku);
						productInfo.setAsin(asin);
						productInfo.setOpenDate(LocalDateTime.ofInstant(openDate.toInstant(),ZoneId.systemDefault()));
						productInfo.setAmazonAuthId(new BigInteger(amazonAuthority.getId()));
						productInfo.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
						productInfo.setInvalid(Boolean.FALSE);
						productInfo.setRefreshtime(LocalDateTime.now());
						productInfo.setCreatedate(new Date());
						if (StrUtil.isNotEmpty(price)) {
							BigDecimal buyprice = new BigDecimal(price.trim().toString());
							productInfo.setPrice(buyprice);
						}
						try {
							productInfoMapper.insert(productInfo);
						} catch (Exception e) {
							e.printStackTrace();
							mlog.append("productInfo 新增失败，" + e.getMessage());
						}finally {
							productInfo=null;
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
			System.gc();
		}
		return mlog.toString();
	}

	@Override
	public String myReportType() {
		return ReportType.ProductListings;
	}

}
