package com.wimoor.amazon.inboundV2.service;

import java.util.List;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.api.FbaInboundV20240320Api;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.fulfillmentinboundV20240320.*;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.common.mvc.BizException;

public interface IInboundApiHandlerService extends IRunAmazonService{
	
	    public ListInboundPlansResponse listInboundPlans(AmazonAuthority amazonAuthority,Integer pageSize,String paginationToken,String status,String sortBy,String sortOrder);
	 
	    public CreateInboundPlanResponse createInboundPlan(AmazonAuthority amazonAuthority,Marketplace market,ShipInboundPlan plan, List<ShipInboundItem> itemlist, ShipAddress address);
	    
	    public InboundPlan getInboundPlan(AmazonAuthority amazonAuthority,String inboundPlanId) ;
	    
	    public ListInboundPlanBoxesResponse listInboundPlanBoxes(AmazonAuthority amazonAuthority,String inboundPlanId,Integer pageSize,	String paginationToken) ;
	    public ListShipmentBoxesResponse listShipmentBoxes(AmazonAuthority amazonAuthority,String inboundPlanId,String shipmentid,Integer pageSize,	String paginationToken) ;
	    public CancelInboundPlanResponse cancelInboundPlan(AmazonAuthority amazonAuthority,String inboundPlanId);
	    
	    public ListInboundPlanItemsResponse listInboundPlanItems(AmazonAuthority amazonAuthority,String inboundPlanId, Integer pageSize, String paginationToken) ;
	    
	    public SetPackingInformationResponse setPackingInformation(AmazonAuthority amazonAuthority,String inboundPlanId, SetPackingInformationRequest body);
	    
	    public ListPackingOptionsResponse listPackingOptions(AmazonAuthority amazonAuthority,String inboundPlanId, Integer pageSize, String paginationToken) ;
	    
	    public GeneratePackingOptionsResponse generatePackingOptions(AmazonAuthority amazonAuthority,String inboundPlanId) ;
	    
	    public ConfirmPackingOptionResponse confirmPackingOption(AmazonAuthority amazonAuthority,String inboundPlanId, String packingOptionId) ;
	    
	    public ListPackingGroupItemsResponse listPackingGroupItems(AmazonAuthority amazonAuthority,String inboundPlanId, String packingOptionId, String packingGroupId, Integer pageSize, String paginationToken) ;
	    
	    public ListInboundPlanPalletsResponse listInboundPlanPallets(AmazonAuthority amazonAuthority,String inboundPlanId, Integer pageSize, String paginationToken) ;
	    
	    public ListPlacementOptionsResponse listPlacementOptions(AmazonAuthority amazonAuthority,String inboundPlanId, Integer pageSize, String paginationToken) ;
	    
	    public GeneratePlacementOptionsResponse generatePlacementOptions(AmazonAuthority amazonAuthority,String inboundPlanId, GeneratePlacementOptionsRequest body);
	    
	    public ConfirmPlacementOptionResponse confirmPlacementOption(AmazonAuthority amazonAuthority,String inboundPlanId, String placementOptionId) ;
	    
	    public Shipment getShipment(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId);
	    
	    public GetDeliveryChallanDocumentResponse getDeliveryChallanDocument(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId);
	    
	    public GetSelfShipAppointmentSlotsResponse getSelfShipAppointmentSlots(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, Integer pageSize, String paginationToken) ;
	    
	    public GenerateSelfShipAppointmentSlotsResponse generateSelfShipAppointmentSlots(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, GenerateSelfShipAppointmentSlotsRequest body) ;
	    
	    public CancelSelfShipAppointmentResponse cancelSelfShipAppointment(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, String slotId, CancelSelfShipAppointmentRequest body) ;
	    
	    public ScheduleSelfShipAppointmentResponse scheduleSelfShipAppointment(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, String slotId, ScheduleSelfShipAppointmentRequest body);
	    
	    public UpdateShipmentTrackingDetailsResponse updateShipmentTrackingDetails(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, UpdateShipmentTrackingDetailsRequest body) ;
	    
	    public GenerateTransportationOptionsResponse generateTransportationOptions(AmazonAuthority amazonAuthority,String inboundPlanId, GenerateTransportationOptionsRequest body) ;
	    
	    public ConfirmTransportationOptionsResponse confirmTransportationOptions(AmazonAuthority amazonAuthority,String inboundPlanId, ConfirmTransportationOptionsRequest body) ;
	    
	    public ListItemComplianceDetailsResponse listItemComplianceDetails(AmazonAuthority amazonAuthority,List<String> mskus, String marketplaceId) ;
	    
	    public UpdateItemComplianceDetailsResponse updateItemComplianceDetails(AmazonAuthority amazonAuthority,String marketplaceId, UpdateItemComplianceDetailsRequest body);
	   
	    public InboundOperationStatus getInboundOperationStatus(AmazonAuthority amazonAuthority,String operationId);
	    public UpdateShipmentSourceAddressResponse updateShipmentSourceAddress(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentid,ShipAddress address);

		ListShipmentItemsResponse listShipmentItems(AmazonAuthority auth, String inboundPlanId, String shipmentId,
				Integer pageSize, String paginationToken);

		public ListTransportationOptionsResponse listTransportationOptions(AmazonAuthority amazonAuthority,String inboundPlanId, String placementOptionId, String shipmentId, Integer pageSize, String paginationToken);

		public GenerateDeliveryWindowOptionsResponse  generateDeliveryWindowOptions(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentid);
        
		public ListDeliveryWindowOptionsResponse listDeliveryWindowOptions(AmazonAuthority amazonAuthority,String inboundPlanId,  String shipmentId, Integer pageSize, String paginationToken);

		public ConfirmDeliveryWindowOptionsResponse confirmDeliveryWindowOptions(AmazonAuthority amazonAuthority, String inboundPlanId, String shipmentid, String deliveryWindowOptionId);

		public ListPrepDetailsResponse listPrepDetails(AmazonAuthority auth, String marketplaceid, List<String> sku) ;

		public SetPrepDetailsResponse setPrepDetails(AmazonAuthority auth, String marketplaceid,List<MskuPrepDetailInput> mskuprep);
}
