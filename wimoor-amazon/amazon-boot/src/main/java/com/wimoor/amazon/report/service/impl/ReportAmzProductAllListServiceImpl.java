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
import java.util.List;
import java.util.ArrayList;
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
	 */
	 public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
		StringBuffer mlog = new StringBuffer();
		int lineNumber = 0;
		String line;
		Boolean isJP = "A1VC38T7YXB528".equals(amazonAuthority.getMarketPlace().getMarketplaceid());
		Boolean isFr = "A13V1IB3VIYZZH".equals(amazonAuthority.getMarketPlace().getMarketplaceid());

		/*  isJP or isFr
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

		//==============================================================================================
		/* other
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

		List<ProductInfo> batchList = new ArrayList<ProductInfo>();
		int batchSize = 500;
		int totalProcessed = 0;

		try {
			while ((line = br.readLine()) != null) {
				if (lineNumber++ == 0) {
					continue; // 跳过标题行
				}

				ProductInfo productInfo = parseLineToProductInfo(line, amazonAuthority, isJP, isFr);
				if (productInfo != null) {
					batchList.add(productInfo);

					// 批量执行
					if (batchList.size() >= batchSize) {
						int processed = executeBatch(batchList, mlog);
						totalProcessed += processed;
						batchList.clear();
					}
				}
			}

			// 处理剩余批次
			if (!batchList.isEmpty()) {
				int processed = executeBatch(batchList, mlog);
				totalProcessed += processed;
			}



		} catch (Exception e) {
			e.printStackTrace();
			mlog.append("处理过程中发生异常: " + e.getMessage());
		} finally {
			closeResource(br);
		}

		return mlog.toString();
	}

	/**
	 * 将一行数据解析为ProductInfo对象
	 */
	private ProductInfo parseLineToProductInfo(String line, AmazonAuthority amazonAuthority,
											   boolean isJP, boolean isFr) {
		String[] info = line.split("\t");

		String name, sku, price, openDateStr, asin, fulfillmentChannel;

		try {
			if (isJP || isFr) {
				name = getIndexString(info, 0);
				sku = getIndexString(info, 2);
				price = getIndexString(info, 3);
				openDateStr = getIndexString(info, 5);
				asin = getIndexString(info, 11);
				fulfillmentChannel = getIndexString(info, 13);

				// 日本站点跳过Incomplete记录
				if (isJP && info.length >= 15 && "Incomplete".equals(info[14])) {
					return null;
				}
			} else {
				name = getIndexString(info, 0);
				price = getIndexString(info, 4);

				if ((price == null || price.trim().isEmpty()) || isNumericzidai(price)) {
					sku = getIndexString(info, 3);
					price = getIndexString(info, 4);
					openDateStr = getIndexString(info, 6);
					asin = getIndexString(info, 16);
					fulfillmentChannel = getIndexString(info, 26);
				} else {
					sku = getIndexString(info, 4);
					price = getIndexString(info, 5);
					openDateStr = getIndexString(info, 7);
					asin = getIndexString(info, 17);
					fulfillmentChannel = getIndexString(info, 27);
				}
			}

			// 验证必要字段
			if (StrUtil.isEmpty(sku)) {
				return null;
			}

			// 构建ProductInfo对象
			ProductInfo productInfo = new ProductInfo();
			productInfo.setId(amazonAuthorityService.getUUID());

			// 处理名称长度限制
			if (name != null && name.length() >= 1000) {
				productInfo.setName(name.substring(0, 1000));
			} else {
				productInfo.setName(name);
			}

			productInfo.setSku(sku);
			productInfo.setAsin(asin);
			productInfo.setFulfillChannel(fulfillmentChannel);

			// 解析价格
			if (StrUtil.isNotEmpty(price)) {
				try {
					productInfo.setPrice(new BigDecimal(price.trim()));
				} catch (Exception e) {
					// 价格格式错误，可以记录日志或设为null
					productInfo.setPrice(null);
				}
			}

			// 解析日期
			if (StrUtil.isNotEmpty(openDateStr)) {
				LocalDateTime openDate = parseOpenDate(openDateStr, isJP);
				productInfo.setOpenDate(openDate);
			}

			productInfo.setAmazonAuthId(new BigInteger(amazonAuthority.getId()));
			productInfo.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
			productInfo.setInvalid(Boolean.FALSE);
			productInfo.setRefreshtime(LocalDateTime.now());
			productInfo.setCreatedate(new Date());

			return productInfo;

		} catch (Exception e) {
			// 记录解析错误但继续处理其他行
			System.err.println("解析行数据失败: " + line);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 执行批量操作
	 */
	private int executeBatch(List<ProductInfo> batchList, StringBuffer mlog) {
		if (batchList.isEmpty()) {
			return 0;
		}

		try {
			// 使用XML配置的批量插入或更新方法
			productInfoMapper.batchInsertOrUpdate(batchList);
			return batchList.size();

		} catch (Exception e) {
			e.printStackTrace();
			mlog.append("批量操作失败: " + e.getMessage() + "; ");

			// 降级处理：单条操作
			return fallbackToSingleOperations(batchList, mlog);
		}
	}

	/**
	 * 降级处理：单条操作
	 */
	private int fallbackToSingleOperations(List<ProductInfo> batchList, StringBuffer mlog) {
		int successCount = 0;

		for (ProductInfo productInfo : batchList) {
			try {
				// 检查是否已存在
				ProductInfo existing = productInfoService.productOnlyone(
						productInfo.getAmazonAuthId().toString(),
						productInfo.getSku(),
						productInfo.getMarketplaceid()
				);

				if (existing != null) {
					// 更新现有记录
					updateExistingRecord(existing, productInfo);
					productInfoMapper.updateById(existing);
				} else {
					// 插入新记录
					productInfoMapper.insert(productInfo);
				}
				successCount++;

			} catch (Exception e) {
				e.printStackTrace();
				mlog.append("SKU: " + productInfo.getSku() + " 处理失败: " + e.getMessage() + "; ");
			}
		}

		return successCount;
	}

	/**
	 * 更新已存在的记录（保持原有业务逻辑）
	 */
	private void updateExistingRecord(ProductInfo existing, ProductInfo newInfo) {
		// 保持原有的openDate更新逻辑
		if (existing.getOpenDate() == null ||
				LocalDateTime.now().isBefore(existing.getOpenDate())) {
			existing.setOpenDate(newInfo.getOpenDate());
		}

		// 更新其他字段
		existing.setPrice(newInfo.getPrice());
		existing.setFulfillChannel(newInfo.getFulfillChannel());
		existing.setInvalid(Boolean.FALSE);

		// 名称变化时更新时间
		if (existing.getName() == null || !existing.getName().equals(newInfo.getName())) {
			existing.setName(newInfo.getName());
			existing.setRefreshtime(LocalDateTime.now());
		} else {
			existing.setRefreshtime(newInfo.getRefreshtime());
		}

		// 更新SKU和ASIN
		if (!existing.getSku().equals(newInfo.getSku())) {
			existing.setSku(newInfo.getSku());
		}
		existing.setAsin(newInfo.getAsin());
	}

	/**
	 * 解析日期
	 */
	private LocalDateTime parseOpenDate(String dateStr, boolean isJP) {
		try {
			Date date = null;
			if (dateStr.contains(",")) {
				// 格式: MMM dd, yyyy
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
				date = sdf.parse(dateStr);
			} else if (isJP) {
				// 日本格式: yyyy/MM/dd
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
				date = dateStr.length() > 10 ?
						sdf.parse(dateStr.substring(0, 10)) :
						sdf.parse(dateStr);
			} else {
				// 其他格式: dd/MM/yyyy
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				date = dateStr.length() > 10 ?
						sdf.parse(dateStr.substring(0, 10)) :
						sdf.parse(dateStr);
			}

			return date != null ?
					LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) :
					null;

		} catch (ParseException e) {
			// 如果标准格式解析失败，尝试备用方法
			try {
				Date date = GeneralUtil.getDatez(dateStr.substring(0, Math.min(dateStr.length(), 10)));
				return date != null ?
						LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) :
						null;
			} catch (Exception ex) {
				System.err.println("日期解析失败: " + dateStr);
				return null;
			}
		}
	}

	/**
	 * 关闭资源
	 */
	private void closeResource(BufferedReader br) {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 辅助方法（保持与原有代码一致）
	private String getIndexString(String[] array, int index) {
		return array != null && array.length > index ? array[index] : null;
	}

	private boolean isNumericzidai(String str) {
		// 原有逻辑
		return GeneralUtil.isNumericzidai(str);
	}


	@Override
	public String myReportType() {
		return ReportType.ProductListings;
	}

}
