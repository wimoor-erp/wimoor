package com.wimoor.amazon.inbound.service;

import java.util.List;

import com.amazon.spapi.model.fulfillmentinbound.GetInboundGuidanceResult;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentList;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;

public interface IFulfillmentInboundService {
	
	GetInboundGuidanceResult getInboundGuidance(AmazonAuthority auth,String marketplaceId,List<String> sellerSKUList);
	
	List<ShipInboundShipment> createInboundShipmentPlan(AmazonAuthority auth,Marketplace market,ShipInboundPlan inplan);
	
	public InboundShipmentList listShipment(AmazonAuthority auth,Marketplace market,String startdate,String enddate);
	
	public String updateInboundShipment(AmazonAuthority amazonAuthority,Marketplace marketplace,ShipInboundPlan inboundplan ,ShipInboundShipment shipment);
	
	public String createInboundShipment(AmazonAuthority amazonAuthority,	Marketplace marketplace ,ShipInboundPlan plan, ShipInboundShipment shipment);
 
	public String putTransportDetailsRequest(AmazonAuthority amazonAuthority,	Marketplace marketplace ,ShipInboundPlan inboundplan ,ShipInboundShipment shipment);
	
	public String getLabelDownloadUR(AmazonAuthority amazonAuthority,ShipInboundShipment shipment,String pagetype);
}
