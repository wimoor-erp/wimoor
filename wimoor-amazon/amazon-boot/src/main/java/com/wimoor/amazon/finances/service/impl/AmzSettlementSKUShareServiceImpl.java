package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryDayMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementSummarySkuMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementSummarySku;
import com.wimoor.amazon.finances.service.IAmzSettlementSKUShareService;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.service.IProfitCfgCountryService;
import com.wimoor.amazon.profit.service.IProfitCfgService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AmzSettlementSKUShareServiceImpl implements IAmzSettlementSKUShareService {
  final AmzSettlementReportSummaryDayMapper amzSettlementReportSummaryDayMapper;
  final AmzSettlementSummarySkuMapper amzSettlementSummarySkuMapper;
  final IProfitCfgService profitCfgService;
  final IProfitCfgCountryService profitCfgCountryService;
  private class ShopFee{
	   public  BigDecimal shop_othershare=new BigDecimal("0");//其他费用
	   public  BigDecimal shop_long_storage_fee=new BigDecimal("0");//长期仓储费
	   public  BigDecimal shop_storage_fee=new BigDecimal("0");//仓储费
	   public  BigDecimal shop_advspend_fee=new BigDecimal("0");//广告费
	   public  BigDecimal shop_coupon_redemption_fee=new BigDecimal("0");//折扣券
	   public  BigDecimal shop_reserve_amount=new BigDecimal("0");//预留金
	   public  BigDecimal shop_in_principal=new BigDecimal("0");
	   public  BigDecimal shop_reimbursement_fee=new BigDecimal("0");//赔偿金
	  // public  BigDecimal shop_disposal_complete_fee=new BigDecimal("0");//库存弃置费
	   public  Integer shop_quantity_orders=0;
	   public  Integer shop_sales=0;
  }
  
  
  
  private ShopFee getShopFee(String settlementid) {
	  List<Map<String, Object>> list = amzSettlementReportSummaryDayMapper.summaryFeeBySettment(settlementid);
	    ShopFee shopfee=new ShopFee();
	    for(Map<String, Object> item:list) {
	    	   String transaction_type=item.get("transaction_type")!=null?item.get("transaction_type").toString():"#";
	    	if(transaction_type.equals("sku")) {
	    		shopfee.shop_in_principal= item.get("principal")!=null?new BigDecimal(item.get("principal").toString()):shopfee.shop_in_principal;
	    		shopfee.shop_quantity_orders= item.get("quantity_orders")!=null?  Integer.parseInt(item.get("quantity_orders").toString()):shopfee.shop_quantity_orders;
	    		shopfee.shop_sales= item.get("sales")!=null?  Integer.parseInt(item.get("sales").toString()):shopfee.shop_sales;
	    	}else if(item.get("amount")!=null){
	    			String amount_description=item.get("amount_description")!=null?item.get("amount_description").toString():"#";
	    			String amount_type=item.get("amount_type")!=null?item.get("amount_type").toString():"#";
	    			BigDecimal amount= new BigDecimal(item.get("amount").toString());
	    			if("Storage Fee".equals(amount_description)) {
	    				shopfee.shop_storage_fee=shopfee.shop_storage_fee.add(amount);
					}else if("FBA Long-Term Storage".equals(amount_description)) {
						shopfee.shop_long_storage_fee=shopfee.shop_long_storage_fee.add(amount);
					}else if("Previous Reserve Amount Balance".equals(amount_description)||"Current Reserve Amount".equals(amount_description)){
						shopfee.shop_reserve_amount=shopfee.shop_reserve_amount.add(amount);
					}else if("Cost of Advertising".equals(amount_type)) {
						shopfee.shop_advspend_fee=shopfee.shop_advspend_fee.add(amount);
				    }else if("CouponRedemptionFee".equals(amount_type)) {
				    	shopfee.shop_coupon_redemption_fee=shopfee.shop_coupon_redemption_fee.add(amount);
				    }else if("FBA Inventory Reimbursement".equals(amount_type)||"Manual Processing Fee Reimbursement".equals(amount_type)){
				    	shopfee.shop_reimbursement_fee=shopfee.shop_reimbursement_fee.add(amount);
				    }else  {
				    	shopfee.shop_othershare=shopfee.shop_othershare.add(amount);
				   }
	    	}
	    }
	    

	    return shopfee;
  }
  private BigDecimal setValue(ShopFee shopfeesub  ,BigDecimal value,String type) {
	  if(type.equals("shop_storage_fee")) {
		  if(shopfeesub.shop_storage_fee.compareTo(new BigDecimal("0"))==0) {
			  return new BigDecimal("0");
		  } else if(shopfeesub.shop_storage_fee.floatValue()>0) {
			  if(shopfeesub.shop_storage_fee.floatValue()-value.floatValue()>0) {
				  shopfeesub.shop_storage_fee=shopfeesub.shop_storage_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_storage_fee;
				  shopfeesub.shop_storage_fee=new BigDecimal("0");
				  return value;
			  }
		  }else {
			  if(shopfeesub.shop_storage_fee.floatValue()-value.floatValue()<0) {
				  shopfeesub.shop_storage_fee=shopfeesub.shop_storage_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_storage_fee;
				  shopfeesub.shop_storage_fee=new BigDecimal("0");
				  return value;
			  }
		  }
		 
	  }else if(type.equals("itemlongStoragefee")) {
		  if(shopfeesub.shop_long_storage_fee.compareTo(new BigDecimal("0"))==0) {
			  return new BigDecimal("0");
		  } else if(shopfeesub.shop_long_storage_fee.floatValue()>0) {
			  if(shopfeesub.shop_long_storage_fee.floatValue()-value.floatValue()>0) {
				  shopfeesub.shop_long_storage_fee=shopfeesub.shop_long_storage_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_long_storage_fee;
				  shopfeesub.shop_long_storage_fee=new BigDecimal("0");
				  return value;
			  }
		  }else {
			  if(shopfeesub.shop_long_storage_fee.floatValue()-value.floatValue()<0) {
				  shopfeesub.shop_long_storage_fee=shopfeesub.shop_long_storage_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_long_storage_fee;
				  shopfeesub.shop_long_storage_fee=new BigDecimal("0");
				  return value;
			  }
		  }
	  }else if(type.equals("itemadvspend")) {
		  if(shopfeesub.shop_advspend_fee.compareTo(new BigDecimal("0"))==0) {
			  return new BigDecimal("0");
		  } else if(shopfeesub.shop_advspend_fee.floatValue()>0) {
			  if(shopfeesub.shop_advspend_fee.floatValue()-value.floatValue()>0) {
				  shopfeesub.shop_advspend_fee=shopfeesub.shop_advspend_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_advspend_fee;
				  shopfeesub.shop_advspend_fee=new BigDecimal("0");
				  return value;
			  }
		  }else {
			  if(shopfeesub.shop_advspend_fee.floatValue()-value.floatValue()<0) {
				  shopfeesub.shop_advspend_fee=shopfeesub.shop_advspend_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_advspend_fee;
				  shopfeesub.shop_advspend_fee=new BigDecimal("0");
				  return value;
			  }
		  }
	  }else if(type.equals("itemcoupon_redemption_fee")) {
		  if(shopfeesub.shop_coupon_redemption_fee.compareTo(new BigDecimal("0"))==0) {
			  return new BigDecimal("0");
		  } else if(shopfeesub.shop_coupon_redemption_fee.floatValue()>0) {
			  if(shopfeesub.shop_coupon_redemption_fee.floatValue()-value.floatValue()>0) {
				  shopfeesub.shop_coupon_redemption_fee=shopfeesub.shop_coupon_redemption_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_coupon_redemption_fee;
				  shopfeesub.shop_coupon_redemption_fee=new BigDecimal("0");
				  return value;
			  }
		  }else {
			  if(shopfeesub.shop_coupon_redemption_fee.floatValue()-value.floatValue()<0) {
				  shopfeesub.shop_coupon_redemption_fee=shopfeesub.shop_coupon_redemption_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_coupon_redemption_fee;
				  shopfeesub.shop_coupon_redemption_fee=new BigDecimal("0");
				  return value;
			  }
		  }
	  }else if(type.equals("itemreserve_amount")) {
		  if(shopfeesub.shop_reserve_amount.compareTo(new BigDecimal("0"))==0) {
			  return new BigDecimal("0");
		  } else if(shopfeesub.shop_reserve_amount.floatValue()>0) {
			  if(shopfeesub.shop_reserve_amount.floatValue()-value.floatValue()>0) {
				  shopfeesub.shop_reserve_amount=shopfeesub.shop_reserve_amount.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_reserve_amount;
				  shopfeesub.shop_reserve_amount=new BigDecimal("0");
				  return value;
			  }
		  }else {
			  if(shopfeesub.shop_reserve_amount.floatValue()-value.floatValue()<0) {
				  shopfeesub.shop_reserve_amount=shopfeesub.shop_reserve_amount.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_reserve_amount;
				  shopfeesub.shop_reserve_amount=new BigDecimal("0");
				  return value;
			  }
		  }
	  }else if(type.equals("itemreimbursement_fee")) {
		  if(shopfeesub.shop_reimbursement_fee.compareTo(new BigDecimal("0"))==0) {
			  return new BigDecimal("0");
		  } else if(shopfeesub.shop_reimbursement_fee.floatValue()>0) {
			  if(shopfeesub.shop_reimbursement_fee.floatValue()-value.floatValue()>0) {
				  shopfeesub.shop_reimbursement_fee=shopfeesub.shop_reimbursement_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_reimbursement_fee;
				  shopfeesub.shop_reimbursement_fee=new BigDecimal("0");
				  return value;
			  }
		  }else {
			  if(shopfeesub.shop_reimbursement_fee.floatValue()-value.floatValue()<0) {
				  shopfeesub.shop_reimbursement_fee=shopfeesub.shop_reimbursement_fee.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_reimbursement_fee;
				  shopfeesub.shop_reimbursement_fee=new BigDecimal("0");
				  return value;
			  }
		  }
	  }else if(type.equals("itemshopother")) {
		  if(shopfeesub.shop_othershare.compareTo(new BigDecimal("0"))==0) {
			  return new BigDecimal("0");
		  } else if(shopfeesub.shop_othershare.floatValue()>0) {
			  if(shopfeesub.shop_othershare.floatValue()-value.floatValue()>0) {
				  shopfeesub.shop_othershare=shopfeesub.shop_othershare.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_othershare;
				  shopfeesub.shop_othershare=new BigDecimal("0");
				  return value;
			  }
		  }else {
			  if(shopfeesub.shop_othershare.floatValue()-value.floatValue()<0) {
				  shopfeesub.shop_othershare=shopfeesub.shop_othershare.subtract(value);
				  return value;
			  }else {
				  value= shopfeesub.shop_othershare;
				  shopfeesub.shop_othershare=new BigDecimal("0");
				  return value;
			  }
		  }  
	  } else {
		  return new BigDecimal("0");
	  }
	 
  }
  private void shareShopFeeToSku(ShopFee shopfee,ShopFee shopfeesub,AmzSettlementSummarySku item) {
	    BigDecimal rate = null;
	    if(shopfee.shop_in_principal!=null&&shopfee.shop_in_principal.floatValue()>0.0001) {
	    	rate=item.getPrincipal().divide(shopfee.shop_in_principal,30,RoundingMode.HALF_UP);
	    }else if(shopfee.shop_sales!=null&&shopfee.shop_sales>0) {
	    	rate=new BigDecimal(item.getSales()).divide(new BigDecimal(shopfee.shop_sales),30,RoundingMode.HALF_UP);
	    }else {
	    	rate=new BigDecimal(0);
	    }
  	    BigDecimal itemStoragefee = shopfee.shop_storage_fee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
		BigDecimal itemlongStoragefee = shopfee.shop_long_storage_fee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
	    BigDecimal itemadvspend = shopfee.shop_advspend_fee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
	    BigDecimal itemcoupon_redemption_fee =shopfee.shop_coupon_redemption_fee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
	    BigDecimal itemreserve_amount =shopfee.shop_reserve_amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
	    BigDecimal itemreimbursement_fee=shopfee.shop_reimbursement_fee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
	    BigDecimal itemshopother = shopfee.shop_othershare.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    	item.setShareStorageFee(setValue(shopfeesub,itemStoragefee,"shop_storage_fee"));
    	item.setShareLongStorageFee(setValue(shopfeesub,itemlongStoragefee,"itemlongStoragefee"));
    	item.setShareAdvSpendFee(setValue(shopfeesub,itemadvspend,"itemadvspend"));
    	item.setShareCouponRedemptionFee(setValue(shopfeesub,itemcoupon_redemption_fee,"itemcoupon_redemption_fee"));
    	item.setShareReserveFee( setValue(shopfeesub,itemreserve_amount,"itemreserve_amount"));
    	item.setShareReimbursementFee( setValue(shopfeesub,itemreimbursement_fee,"itemreimbursement_fee"));
    	item.setShareShopOtherFee( setValue(shopfeesub,itemshopother,"itemshopother"));
  }
  
 
  
  
  private void addLocalFeeToSku(AmzSettlementSummarySku item,Map<String,Object> localitem) {
			  /*</span>
		        <br/>进口GST税 
				<br/>销售GST税 
				<br/>企业所得税
				<br/>VAT增值税(不含英国)
				<br/>进口关税
				<br/>汇率损耗
				<br/>市场营销费用
				<br/>每单固定成本
				<br/>其他每单销售固定费用
				<br/>本地SKU信息中维护的其他采购成本
		        */
	            if(localitem==null||localitem.size()==0) {
	            	item.setLocalPrice(new BigDecimal(0)); 
					item.setLocalOtherCost(new BigDecimal(0)); 
					item.setProfitLocalShipmentfee(new BigDecimal(0)); 
					item.setProfitCompanytax(new BigDecimal(0)); 
					item.setProfitCustomstax(new BigDecimal(0)); 
					item.setProfitExchangelost(new BigDecimal(0)); 
					item.setProfitMarketfee(new BigDecimal(0)); 
					item.setProfitLostrate(new BigDecimal(0)); 
					item.setProfitOtherfee(new BigDecimal(0)); 
					item.setProfitVat(new BigDecimal(0)); 
					item.setMid(null);
					item.setPid(null);
					item.setOwner(null);
					return ;
	            }
				String groupid=localitem.get("groupid").toString();
				BigDecimal volume=localitem.get("volume")!=null?(BigDecimal)localitem.get("volume"):new BigDecimal(0);
				String configid = profitCfgService.findDefaultPlanIdByGroup(groupid);
			
				BigDecimal principal = item.getPrincipal();
				List<ProfitConfigCountry> list = profitCfgCountryService.findByProfitId(configid);
				ProfitConfigCountry mycountryconfig=null;
		
				String mycountry=localitem.get("country").toString();
				String style=null;
				for(ProfitConfigCountry mpitem:list) {
					if(mycountry.equals(mpitem.getCountry())) {
						mycountryconfig=mpitem;break;
					}
				}
				ProfitConfig config=profitCfgService.getById(configid);
				BigDecimal salenum =new BigDecimal(item.getSales());
				BigDecimal shipmentfee=new BigDecimal("0");
				BigDecimal vat =new BigDecimal(0);
				BigDecimal companytax=new BigDecimal(0);
				BigDecimal customstax=new BigDecimal(0);
				BigDecimal exchangelost=new BigDecimal(0);
				BigDecimal marketfee=new BigDecimal(0);
				BigDecimal costrate=new BigDecimal(0);
				BigDecimal otherfee=new BigDecimal(0);
				if(mycountryconfig!=null) {
					  if(mycountryconfig.getVatRate()!=null) {
						  vat = principal.multiply(mycountryconfig.getVatRate()).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP);
					  }
					  if(mycountryconfig.getLostrate()!=null) {
						  exchangelost = principal.multiply(mycountryconfig.getLostrate()).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP);
					  }
					  if(mycountryconfig.getOtherfee()!=null) {
						  otherfee=mycountryconfig.getOtherfee().multiply(salenum);
					  }
					  if(mycountryconfig.getCostrate()!=null) {
						 costrate = principal.multiply(mycountryconfig.getCostrate()).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP);
					  }
					  if(mycountryconfig.getSellerrate()!=null) {
						  marketfee = principal.multiply(mycountryconfig.getSellerrate()).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP);
					  }
					  if(mycountryconfig.getCorporateInRate()!=null) {
						  companytax = principal.multiply(mycountryconfig.getCorporateInRate()).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP);
					  }
					  if(mycountryconfig.getTaxrate()!=null) {
						  customstax = principal.multiply(mycountryconfig.getTaxrate()).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP);
					  }
					  if(config!=null){
						  style=config.getShipmentstyle();
					  }  
					  BigDecimal weight=localitem.get("weight")!=null?(BigDecimal)localitem.get("weight"):new BigDecimal(0);
					  BigDecimal basedim=mycountryconfig.getConstantd();
					  BigDecimal baseweight = mycountryconfig.getConstantw();
					  //BigDecimal basem1 = mycountryconfig.getConstantm();
					  if("weight".equals(style)) {
						  // 重量
						  shipmentfee=weight.multiply(baseweight).multiply(salenum);
					  }else if("volume".equals(style)) {
						   //材积
						  shipmentfee=volume.divide(basedim,8,RoundingMode.HALF_UP).multiply(baseweight).multiply(salenum);
					  }else if(style==null||"dim_weight".equals(style) || "manually".equals(style)) {
						  // 重量材积 取大者
						  BigDecimal value1 = volume.divide(basedim,8,RoundingMode.HALF_UP);
						  BigDecimal value2 = weight;
						  if(value1.compareTo(value2)>0) {
							  shipmentfee=value1.multiply(baseweight).multiply(salenum);
						  }else {
							  shipmentfee=value2.multiply(baseweight).multiply(salenum);
						  }
					  } 
				}
				
				BigDecimal realySales = salenum;
				realySales=realySales.subtract(item.getRefundsales()==null?new BigDecimal("0"):new BigDecimal(item.getRefundsales()));
				item.setLocalPrice(localitem.get("itemprice")!=null?new BigDecimal(localitem.get("itemprice").toString()).multiply(realySales):new BigDecimal(0)); 
				item.setLocalOtherCost(localitem.get("otherCost")!=null?new BigDecimal(localitem.get("otherCost").toString()).multiply(realySales):new BigDecimal(0));
				item.setLocalReturnTax(item.getLocalPrice().multiply(localitem.get("vatrate")==null?new BigDecimal("0"):new BigDecimal(localitem.get("vatrate").toString())));
				item.setProfitLocalShipmentfee(shipmentfee);
				
				item.setProfitCompanytax(companytax);
				item.setProfitCustomstax(customstax);
				item.setProfitExchangelost(exchangelost);
				item.setProfitMarketfee(marketfee);
				item.setProfitLostrate(costrate);
				item.setProfitOtherfee(otherfee);
				item.setProfitVat(vat);
				item.setMid(localitem.get("mid")!=null?localitem.get("mid").toString():null);
				item.setPid(localitem.get("pid")!=null?localitem.get("pid").toString():null);
				item.setOwner(localitem.get("owner")!=null?localitem.get("owner").toString():null);
}
  
  public void  shareFee(String settlementid){
	    ShopFee shopfee=getShopFee(settlementid);
		LambdaQueryWrapper<AmzSettlementSummarySku> query=new LambdaQueryWrapper<AmzSettlementSummarySku>();
		query.eq(AmzSettlementSummarySku::getSettlementId, settlementid);
		List<AmzSettlementSummarySku> skulist = amzSettlementSummarySkuMapper.selectList(query);
		List<Map<String,Object>> listlocalsku=amzSettlementSummarySkuMapper.findSettlementLocalSkuInfo(settlementid);
		Map<String,Map<String,Object>> localSkuMap=new HashMap<String,Map<String,Object>>();
		for(Map<String,Object> item:listlocalsku) {
			String sku=item.get("sku").toString();
			localSkuMap.put(sku, item);
		}
		ShopFee shopfeesub=new ShopFee();
		shopfeesub.shop_storage_fee= shopfee.shop_storage_fee;
		shopfeesub.shop_long_storage_fee = shopfee.shop_long_storage_fee;
		shopfeesub.shop_advspend_fee = shopfee.shop_advspend_fee;
		shopfeesub.shop_coupon_redemption_fee =shopfee.shop_coupon_redemption_fee;
		shopfeesub.shop_reserve_amount =shopfee.shop_reserve_amount;
		shopfeesub.shop_reimbursement_fee=shopfee.shop_reimbursement_fee;
		shopfeesub.shop_othershare = shopfee.shop_othershare;
		AmzSettlementSummarySku maxitem=null;
	    for(AmzSettlementSummarySku item:skulist) {
	    	if(maxitem==null||maxitem.getPrincipal().compareTo(item.getPrincipal())<0) {
	    		maxitem=item;
	    	}
	    	shareShopFeeToSku(shopfee,shopfeesub,item);  //分摊店铺费用
	    	addLocalFeeToSku(item,localSkuMap.get(item.getSku())); //构造本地费用
	    	if(StrUtil.isBlank(item.getOwner())) {
	    		item.setOwner(null);
	    	}
	    	if(StrUtil.isBlank(item.getMid())) {
	    		item.setMid(null);
	    	}
	    	if(StrUtil.isBlank(item.getPid())) {
	    		item.setPid(null);
	    	}
	     
	    }
	    if(maxitem!=null&&(shopfeesub.shop_storage_fee.compareTo(new BigDecimal("0"))!=0
	    		||shopfeesub.shop_long_storage_fee.compareTo(new BigDecimal("0"))!=0
	    		||shopfeesub.shop_advspend_fee.compareTo(new BigDecimal("0"))!=0
	    		||shopfeesub.shop_coupon_redemption_fee.compareTo(new BigDecimal("0"))!=0
	    		||shopfeesub.shop_reserve_amount.compareTo(new BigDecimal("0"))!=0
	    		||shopfeesub.shop_reimbursement_fee.compareTo(new BigDecimal("0"))!=0
	    		||shopfeesub.shop_othershare.compareTo(new BigDecimal("0"))!=0
	    		)) {
	    	maxitem.setShareStorageFee(shopfeesub.shop_storage_fee.add(maxitem.getShareStorageFee()));
	 	    maxitem.setShareLongStorageFee(shopfeesub.shop_long_storage_fee.add(maxitem.getShareLongStorageFee()));
	 	    maxitem.setShareAdvSpendFee(shopfeesub.shop_advspend_fee.add(maxitem.getShareAdvSpendFee()));
	 	    maxitem.setShareCouponRedemptionFee(shopfeesub.shop_coupon_redemption_fee.add(maxitem.getShareCouponRedemptionFee()));
	 	    maxitem.setShareReimbursementFee(shopfeesub.shop_reimbursement_fee.add(maxitem.getShareReimbursementFee()));
	 	    maxitem.setShareReserveFee( shopfeesub.shop_reserve_amount.add(maxitem.getShareReserveFee()));
	 	    maxitem.setShareShopOtherFee( shopfeesub.shop_othershare.add(maxitem.getShareShopOtherFee()));
	    }
	    if(skulist!=null&&skulist.size()>0) {	    	
	    	amzSettlementSummarySkuMapper.replaceBatch(skulist);
	    }
	    localSkuMap.clear();
	    localSkuMap=null;
    }
  

}
