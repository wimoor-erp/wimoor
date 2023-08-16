package com.wimoor.amazon.orders.service.impl;

import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.entity.OrdersFulfilledShipmentsFee;
import com.wimoor.amazon.orders.pojo.entity.OrdersFulfilledShipmentsReport;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;
import com.wimoor.amazon.orders.service.IOrdersFulfilledShipmentsFeeService;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.mapper.ShipInboundItemMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inventory.mapper.InventoryDetailReportMapper;
import com.wimoor.amazon.inventory.mapper.InventorySummaryReportMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventorySummaryReport;
import com.wimoor.amazon.orders.mapper.AmzOrderReturnsMapper;
import com.wimoor.amazon.orders.mapper.OrdersFulfilledShipmentsFeeMapper;
import com.wimoor.amazon.orders.mapper.OrdersFulfilledShipmentsReportMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-22
 */
@Service
public class OrdersFulfilledShipmentsFeeServiceImpl extends ServiceImpl<OrdersFulfilledShipmentsFeeMapper, OrdersFulfilledShipmentsFee> implements IOrdersFulfilledShipmentsFeeService {
	@Resource
	private OrdersFulfilledShipmentsReportMapper ordersFulfilledShipmentsReportMapper;
	
	@Autowired
	InventorySummaryReportMapper inventorySummaryReportMapper;
	
	@Autowired
	InventoryDetailReportMapper inventoryDetailReportMapper;
	
	@Autowired
	ShipInboundItemMapper shipInboundItemMapper;
	
	@Autowired
	IMarketplaceService marketplaceService;
	
	@Autowired
	IAmazonAuthorityService iAmazonAuthorityService;
	@Autowired
	IAmazonGroupService  iAmazonGroupService;
	@Autowired
	IProfitCfgService iIProfitCfgService;
	@Autowired
	AmzOrderReturnsMapper amzOrderReturnsMapper;
	
    public void orderTransFee(String authid) {
    	Calendar c=Calendar.getInstance();
    	c.add(Calendar.DATE, -3);
    	ProfitConfig cfg =null;
		boolean ispaneu=true;
		AmazonAuthority authority = iAmazonAuthorityService.getById(authid);
    	LambdaQueryWrapper<InventorySummaryReport> query =new LambdaQueryWrapper<InventorySummaryReport>();
    	query.eq(InventorySummaryReport::getAuthid, authid);
    	query.eq(InventorySummaryReport::getByday, GeneralUtil.formatDate(c.getTime()));
    	query.eq(InventorySummaryReport::getDisposition, "SELLABLE");
		List<InventorySummaryReport> list = inventorySummaryReportMapper.selectList(query);
		List<String> eumarketidlist=null;
    	if(list==null||list.size()==0) {
    		return;
    	}
		for(InventorySummaryReport item:list) {
    		String sku=item.getMsku();
    		String country=item.getLocation();
    		Integer ships = item.getCustomerShipments();
    	    Integer balance = item.getStartingWarehouseBalance();
    		Marketplace marketPlace = marketplaceService.findMarketplaceByCountry(country);
    		if(marketPlace==null) {
    			continue;
    		}
    		Calendar crec=Calendar.getInstance();
    		crec.setTime(item.getByday());
    		crec.add(Calendar.DATE, 1);
    		//查询处理货件的SKU
    	    LambdaQueryWrapper<ShipInboundItem> queryitem=new LambdaQueryWrapper<ShipInboundItem>();
    	    queryitem.eq(ShipInboundItem::getAmazonauthid, item.getAuthid());
    	    queryitem.eq(ShipInboundItem::getSellersku, sku);
    	    if(marketPlace.getRegion().equals("EU")) {
    	    	    if(eumarketidlist==null) {
    	    	    	
    	    	    	eumarketidlist=new ArrayList<String>();
    	    			if(authority.getAWSRegion().equals("eu-west-1")) {
    	    				List<Marketplace> eulist = marketplaceService.findMarketplaceByRegion("EU");
    	    				for(Marketplace euitem:eulist) {
    	    					if(euitem.getRegion().equals("EU")) {
    	    						eumarketidlist.add(euitem.getMarketplaceid());
    	    					}
    	    				}
    	    			}
    	    			AmazonGroup group = iAmazonGroupService.getById(authority.getGroupid());
    	    			if(group.getProfitcfgid()!=null) {
    	    				  cfg = iIProfitCfgService.findConfigAction(group.getProfitcfgid());
    	    			}
    	    	    }
    	    		if(cfg!=null) {
    	    			ProfitConfigCountry cfgcountry = cfg.getProfitConfigCountry(marketPlace.getMarket());
    	    		    if(cfg.getIssystem()==false&&cfgcountry!=null&&cfgcountry.getFenpeiType()!=null&&cfgcountry.getFenpeiType().equals("EFN")) {
    	    		    	ispaneu=false;
    	    		    }
    	    		} 
    	    		if(ispaneu) {
    	    			  queryitem.in(ShipInboundItem::getMarketplaceid,eumarketidlist);
    	    		}else {
    	    			  queryitem.eq(ShipInboundItem::getMarketplaceid, marketPlace.getMarketplaceid());
    	    		}
    	    }else {
    	    	   queryitem.eq(ShipInboundItem::getMarketplaceid, marketPlace.getMarketplaceid());
    	    }
    	    queryitem.lt(ShipInboundItem::getReceivedDate, GeneralUtil.formatDate(crec.getTime()));
    	    queryitem.orderByDesc(ShipInboundItem::getReceivedDate);
			List<ShipInboundItem> itemlist = shipInboundItemMapper.selectList(queryitem);
	        
	
			//查询出没有处理的订单
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("amazonauthid", item.getAuthid());
			param.put("sku", sku);
			param.put("marketplaceid", marketPlace.getMarketplaceid());
			param.put("salesChannel", marketPlace.getPointName());
			param.put("shipmentStartDate", GeneralUtil.formatDate(item.getByday()));
			param.put("shipmentEndDate", GeneralUtil.formatDate(crec.getTime()));
			AmazonOrdersReturnDTO condition=new AmazonOrdersReturnDTO();
			condition.setSellerid(authority.getSellerid());
			condition.setAmazonauthid(authority.getId());
			condition.setStartDate(GeneralUtil.formatDate(item.getByday()));
			condition.setEndDate(GeneralUtil.formatDate(crec.getTime()));
			condition.setSku(sku);
			List<AmazonOrdersReturnVo> relist=amzOrderReturnsMapper.selectReturnsList(condition);
			if(relist!=null&&relist.size()>0) {
				for(AmazonOrdersReturnVo reitem:relist) {
					String reorderid=reitem.getOrderId();
					String resku=reitem.getSku();
					LambdaQueryWrapper<OrdersFulfilledShipmentsFee> refeequery=new LambdaQueryWrapper<OrdersFulfilledShipmentsFee>();
					refeequery.eq(OrdersFulfilledShipmentsFee::getAmazonOrderId, reorderid);
					refeequery.eq(OrdersFulfilledShipmentsFee::getSku, resku);
					List<OrdersFulfilledShipmentsFee> refeelist = this.list(refeequery);
					if(refeelist!=null&&refeelist.size()>0) {
						for(OrdersFulfilledShipmentsFee fee:refeelist) {
							LambdaQueryWrapper<ShipInboundItem> reitemQuery=new LambdaQueryWrapper<ShipInboundItem>();
							reitemQuery.eq(ShipInboundItem::getShipmentid, fee.getShipmentid());
							reitemQuery.eq(ShipInboundItem::getSellersku, fee.getSku());
							ShipInboundItem reinbound = shipInboundItemMapper.selectOne(reitemQuery);
							Integer oldsub=reinbound.getQuantityReceivedSub()!=null?reinbound.getQuantityReceivedSub():0;
							reinbound.setQuantityReceivedSub(oldsub-fee.getQuantity());
							Integer oldbalance=reinbound.getQuantityReceivedBalance()!=null?reinbound.getQuantityReceivedBalance():0;
							reinbound.setQuantityReceivedBalance(oldbalance-reinbound.getQuantityReceivedSub());
							shipInboundItemMapper.updateById(reinbound);
						}
						this.remove(refeequery);
					}
				}
			}
			List<OrdersFulfilledShipmentsReport> orderslist = ordersFulfilledShipmentsReportMapper.list(param);
			Integer count = this.baseMapper.exists(param);
			if(count!=null&&count>0) {
				shipInboundItemMapper.updateByOrderFee(param);
				this.baseMapper.delete(param);
			}
			int orderindex=0;
		    boolean needupdate=false;
			for(ShipInboundItem shipitem:itemlist) {
				if(shipitem.getQuantityreceived()!=null) {
					int  cansub=0;//本次可以被扣除的数量 can sub
					if(balance!=null&&balance>0) {
						if(balance!=null&&balance-shipitem.getQuantityReceivedBalance()>0) {
							balance=balance-shipitem.getQuantityReceivedBalance();
							shipitem.setStatus(1);
							cansub=0;
							needupdate=true;
						}else if(balance!=null&&balance>0) {
							shipitem.setStatus(2);
							cansub=shipitem.getQuantityReceivedBalance()-balance;
							needupdate=true;
						}
					}else {
						cansub=shipitem.getQuantityReceivedBalance();
						shipitem.setStatus(2);
						needupdate=true;
					}
					
					int n =0 ;//处理N个  n必须小于等cansub
					int i=orderindex;
					while(i<orderslist.size() &&n < cansub) {
						OrdersFulfilledShipmentsReport order = orderslist.get(i);
						n=n+order.getQuantityShipped();
						OrdersFulfilledShipmentsFee fee=new OrdersFulfilledShipmentsFee();
						fee.setAmazonauthid(authid);
						fee.setAmazonOrderId(order.getAmazonOrderId());
						fee.setMarketplaceid(marketPlace.getMarketplaceid());
						fee.setPaymentsDate(order.getPaymentsDate());
						fee.setReportingDate(order.getReportingDate());
						fee.setShipmentDate(order.getShipmentDate());
						fee.setPurchaseDate(order.getPurchaseDate());
						fee.setQuantity(order.getQuantityShipped());
						fee.setItemid(order.getShipmentItemId());
						fee.setSku(order.getSku());
						fee.setShipmentid(shipitem.getShipmentid());
						fee.setUnitcost(shipitem.getUnitcost());
						fee.setUnittransfee(shipitem.getUnittransfee());
						this.baseMapper.insert(fee);
					    i++;
					}
					orderindex=i;
					int oldsub=shipitem.getQuantityReceivedSub()!=null?shipitem.getQuantityReceivedSub():0;
					shipitem.setQuantityReceivedSub(n+oldsub);
					int oldbalance=shipitem.getQuantityReceivedBalance()!=null?shipitem.getQuantityReceivedBalance():0;
					shipitem.setQuantityReceivedBalance(oldbalance-n);
					ships=ships-n;
					if(n>0||needupdate) {
						if(shipitem.getQuantityReceivedBalance()==0) {
							shipitem.setStatus(3);
						}
						shipInboundItemMapper.updateById(shipitem);
					}
					if(ships==0) {
						break;
					}
				}
				
			}
    	}
    	
    }
}
