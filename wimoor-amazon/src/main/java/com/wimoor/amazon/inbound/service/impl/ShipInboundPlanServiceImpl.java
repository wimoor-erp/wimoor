package com.wimoor.amazon.inbound.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentInfo;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentList;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.mapper.ShipInboundPlanMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.vo.AmazonShipmentVo;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentService;
import com.wimoor.common.mvc.BizException;

import lombok.RequiredArgsConstructor;

@Service("shipInboundPlanService")
@RequiredArgsConstructor
public class ShipInboundPlanServiceImpl extends ServiceImpl<ShipInboundPlanMapper,ShipInboundPlan> implements IShipInboundPlanService { 
	 
	final IFulfillmentInboundService iFulfillmentInboundService;
	final IAmazonGroupService iAmazonGroupService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IMarketplaceService marketplaceService;
    final IShipInboundShipmentService shipInboundShipmentService;
    final IShipInboundShipmentRecordService shipInboundShipmentRecordService;
	
	public void saveShipInboundPlan(ShipInboundPlan inplan) {
		// TODO Auto-generated method stub
	       AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
	       Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
	       List<ShipInboundShipment> shipmentList = iFulfillmentInboundService.createInboundShipmentPlan(auth,market,inplan);
	       if(shipmentList!=null&&shipmentList.size()>0) {
	    	   for(ShipInboundShipment localShipment:shipmentList) {
		    	   shipInboundShipmentService.save(localShipment);
				   shipInboundShipmentRecordService.saveRecord(localShipment);
		       }
	    	   save(inplan);
	       }else {
	    	   throw new BizException("计划提交失败,请重新尝试或联系客服");
	       }
	       
			
		  
	}
	 
	
	
	List<AmazonShipmentVo> listShipment(String groupid,String marketplaceid,String begin,String end){
	    AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	    Marketplace market = marketplaceService.selectByPKey(marketplaceid);
		AmazonGroup group = iAmazonGroupService.getById(groupid);
		InboundShipmentList list = iFulfillmentInboundService.listShipment(auth, market, begin, end);
		List<AmazonShipmentVo> result=new LinkedList<AmazonShipmentVo>();
		for(InboundShipmentInfo item:list) {
			AmazonShipmentVo resultitem=new AmazonShipmentVo();
			resultitem.setInfo(item);
			resultitem.setGroupid(groupid);
			resultitem.setGroupname(group.getName());
			resultitem.setMarketname(market.getName());
			resultitem.setMarketplaceid(market.getMarketplaceid());
			result.add(resultitem);
		}
		return result;
	} 
	
	
}
