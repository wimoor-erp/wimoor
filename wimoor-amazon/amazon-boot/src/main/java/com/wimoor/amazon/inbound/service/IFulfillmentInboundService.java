package com.wimoor.amazon.inbound.service;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.model.fulfillmentinbound.GetInboundGuidanceResult;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentItemsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetTransportDetailsResponse;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentInfo;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentItem;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentList;
import com.amazon.spapi.model.fulfillmentinbound.SKUPrepInstructions;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;

public interface IFulfillmentInboundService extends IRunAmazonService{
	
	GetInboundGuidanceResult getInboundGuidance(AmazonAuthority auth,String marketplaceId,List<String> sellerSKUList);
	Map<String,SKUPrepInstructions>  getPrepInstructions(AmazonAuthority auth,	Marketplace market ,List<String> mlist);
	List<ShipInboundShipment> createInboundShipmentPlan(AmazonAuthority auth,	Marketplace market ,ShipInboundPlan inplan);
	
	void handlerItemResult(AmazonAuthority auth, Marketplace market, GetShipmentItemsResponse result, ShipInboundShipment param_shipment,boolean needshipqty); 
	public InboundShipmentList listShipment(AmazonAuthority auth,Marketplace market,String startdate,String enddate);
	
	public InboundShipmentList listShipment(AmazonAuthority auth,Marketplace market,List<String> shipmentIdsList);
	
	public String updateInboundShipment(AmazonAuthority amazonAuthority,Marketplace marketplace,ShipInboundPlan inboundplan ,ShipInboundShipment shipment);
	
	public String createInboundShipment(AmazonAuthority amazonAuthority,	Marketplace marketplace ,ShipInboundPlan plan, ShipInboundShipment shipment);
 
	public String putTransportDetailsRequest(AmazonAuthority amazonAuthority,	Marketplace marketplace ,ShipInboundPlan inboundplan ,ShipInboundShipment shipment);
	
	public String getLabelDownloadUR(AmazonAuthority amazonAuthority,ShipInboundShipment shipment,String pagetype,String labelType, String pannum);

	void handlerResult(AmazonAuthority auth, Marketplace market, GetShipmentsResponse result);

	
	public List<ShipInboundItem> syncItemsByShipmentId(AmazonAuthority amazonAuthority, Marketplace market,ShipInboundShipment shipment);
	public List<ShipInboundItem> getUnSyncItemsByShipmentId(AmazonAuthority amazonAuthority, Marketplace market,ShipInboundShipment shipment);
	
	public List<InboundShipmentItem> requestInboundShipmentsItem(AmazonAuthority auth, String shipmentid, Marketplace marketPlace);

	public InboundShipmentInfo requestInboundShipments(AmazonAuthority auth, String shipmentid, Marketplace marketPlace);

	void checkCancel(AmazonAuthority auth, ShipInboundPlan plan, ShipInboundShipment shipment);
	public GetTransportDetailsResponse getTransportDetails(AmazonAuthority amazonAuthority,String shipmentId);
	
	String estimateTransportDetailsRequest(AmazonAuthority amazonAuthority, Marketplace marketplace,
			ShipInboundPlan inboundplan, ShipInboundShipment shipment);
	String confirmTransportDetailsRequest(AmazonAuthority amazonAuthority, Marketplace marketplace,
			ShipInboundPlan inboundplan, ShipInboundShipment shipment);
}
