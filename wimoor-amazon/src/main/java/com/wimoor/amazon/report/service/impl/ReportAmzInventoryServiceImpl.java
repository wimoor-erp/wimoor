package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.report.mapper.InventoryReportMapper;
import com.wimoor.amazon.report.pojo.entity.InventoryReport;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
 
 
@Slf4j
@Service("reportAmzInventoryService")
public class ReportAmzInventoryServiceImpl extends ReportServiceImpl{
 
	@Resource
	private IProductInfoService iProductInfoService;
	@Resource
	private InventoryReportMapper inventoryReportMapper;
	@Resource
	private IProductInOptService iProductInOptService;
	
	/**
	 * afn-researching-quantity	afn-reserved-future-supply	afn-future-supply-buyable
	 */
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String mlog="";
		int lineNumber = 0;
		InventoryReport record = null;
		String line;
		boolean hasremote=false;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				int length = info.length;
				if(lineNumber==0) {
					if(line.contains("afn-fulfillable-quantity-remote")) {
						hasremote=true;
					}
				}
				if (lineNumber != 0) {
					if(lineNumber==1){
						QueryWrapper<InventoryReport> query =new QueryWrapper<InventoryReport>();
						query.eq("amazonAuthId",amazonAuthority.getId());
						if ("EU".equals(amazonAuthority.getMarketPlace().getRegion())) {
							query.eq("marketplaceid","EU");
						}else {
							query.eq("marketplaceid",amazonAuthority.getMarketPlace().getMarketplaceid());
						}
						inventoryReportMapper.delete(query);
					}
					record = new InventoryReport();
					int i = 0;
					String sku = i < length ? info[i++] : null;
					if(sku!=null && sku.length()>50){
						sku = sku.substring(0, 50);
					}
					String fnsku = i < length ? info[i++] : null;
					String asin = i < length ? info[i++] : null;
					@SuppressWarnings("unused")
					String product_name = i < length ? info[i++] : null;
					String condition = i < length ? info[i++] : null;
					String your_price = i < length ? info[i++] : null;
					String mfn_listing_exists = i < length ? info[i++] : null;
					String mfn_fulfillable_quantity = i < length ? info[i++] : null;
					String afn_listing_exists = i < length ? info[i++] : null;
					String afn_warehouse_quantity = i < length ? info[i++] : null;
					String afn_fulfillable_quantity = i < length ? info[i++] : null;
					String afn_unsellable_quantity = i < length ? info[i++] : null;
					String afn_reserved_quantity = i < length ? info[i++] : null;
					String afn_total_quantity = i < length ? info[i++] : null;
					String per_unit_volume = i < length ? info[i++] : null;
					String afn_inbound_working_quantity = i < length ? info[i++] : null;
					String afn_inbound_shipped_quantity = i < length ? info[i++] : null;
					String afn_inbound_receiving_quantity = i < length ? info[i++] : null;
					String afn_researching_quantity = i < length ? info[i++] : null;
					String afn_reserved_future_supply = i < length ? info[i++] : null;
					String afn_future_supply_buyable = i < length ? info[i++] : null;
					String afn_fulfillable_quantity_remote=hasremote && i < length ? info[i++] : null;
					if(StrUtil.isNotBlank(afn_fulfillable_quantity_remote)) {
						//log.info(afn_fulfillable_quantity_remote);
					}
					record.setAsin(asin);
					record.setSku(sku);
					record.setFnsku(fnsku);
					if(condition.length()>19)condition.substring(0, 18);
					record.setPcondition(condition);
					if(StrUtil.isEmpty(your_price)||"Unknown".equals(your_price)) {
						if((StrUtil.isEmpty(condition)||"Unknown".equals(condition))&&(afn_total_quantity==null||"0".equals(afn_total_quantity))) {
							lineNumber++;
							continue;
						}
					}
					
					if(condition.contains("No Listing")) {
						lineNumber++;
					    continue;
					}
					record.setYourPrice(GeneralUtil.getBigDecimal(your_price));
					record.setMfnListingExists(mfn_listing_exists);
					record.setMfnFulfillableQuantity(GeneralUtil.getInteger(mfn_fulfillable_quantity));
					record.setAfnListingExists(afn_listing_exists);
					record.setAfnWarehouseQuantity(GeneralUtil.getInteger(afn_warehouse_quantity));
					record.setAfnFulfillableQuantity(GeneralUtil.getInteger(afn_fulfillable_quantity));
					record.setAfnUnsellableQuantity(GeneralUtil.getInteger(afn_unsellable_quantity));
					record.setAfnReservedQuantity(GeneralUtil.getInteger(afn_reserved_quantity));
					record.setAfnTotalQuantity(GeneralUtil.getInteger(afn_total_quantity));
					record.setPerUnitVolume(GeneralUtil.getBigDecimal(per_unit_volume));
					record.setAfnInboundWorkingQuantity(GeneralUtil.getInteger(afn_inbound_working_quantity));
					record.setAfnInboundShippedQuantity(GeneralUtil.getInteger(afn_inbound_shipped_quantity));
					record.setAfnInboundReceivingQuantity(GeneralUtil.getInteger(afn_inbound_receiving_quantity));
					record.setAfnResearchingQuantity(GeneralUtil.getInteger(afn_researching_quantity));
					record.setAfnReservedFutureSupply(GeneralUtil.getInteger(afn_reserved_future_supply));
					record.setAfnFutureSupplyBuyable(GeneralUtil.getInteger(afn_future_supply_buyable));
					record.setAmazonAuthId(amazonAuthority.getId());
					if ("EU".equals(amazonAuthority.getMarketPlace().getRegion())) {
						record.setMarketplaceid(amazonAuthority.getMarketPlace().getRegion());
					} else {
						record.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
					}
					record.setByday(amazonAuthority.getCaptureDateTime());
					try {
						List<ProductInfo> infolist = iProductInfoService.selectBySku(record.getSku(), record.getMarketplaceid(), record.getAmazonAuthId());
						if(infolist!=null && infolist.size()>0) {
							ProductInfo infos = infolist.get(0);
							if(infos!=null) {
								String pid = infos.getId();
								ProductInOpt opt = iProductInOptService.getById(pid);
								if(opt!=null) {
									opt.setFnsku(fnsku);
									iProductInOptService.updateById(opt);
								}else {
									ProductInOpt opts =new ProductInOpt(); 
									opts.setPid(new BigInteger(pid));
									opts.setDisable(false);
									opts.setFnsku(fnsku);
									opts.setLastupdate(LocalDateTime.now());
									iProductInOptService.save(opts);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						mlog+="SKU:"+sku+","+e.getMessage();
					} 
					try {
						record.setIsnewest(true);
						inventoryReportMapper.newestInsert(record);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				lineNumber++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
				if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
	}
      return mlog;
	}
	
 

	@Override
	public String myReportType() {
		return "GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA";
	}

 
	
}
