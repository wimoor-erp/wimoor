package com.wimoor.amazon.inboundV2.service;

import java.util.List;

import com.amazon.spapi.model.fulfillmentinboundV20240320.CancelInboundPlanResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.CancelSelfShipAppointmentRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.CancelSelfShipAppointmentResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmDeliveryWindowOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmPackingOptionResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmPlacementOptionResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmTransportationOptionsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmTransportationOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.CreateInboundPlanResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateDeliveryWindowOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GeneratePackingOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GeneratePlacementOptionsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GeneratePlacementOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateSelfShipAppointmentSlotsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateSelfShipAppointmentSlotsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateTransportationOptionsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateTransportationOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GetDeliveryChallanDocumentResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GetSelfShipAppointmentSlotsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.InboundOperationStatus;
import com.amazon.spapi.model.fulfillmentinboundV20240320.InboundPlan;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListDeliveryWindowOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListInboundPlanBoxesResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListInboundPlanItemsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListInboundPlanPalletsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListInboundPlansResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListItemComplianceDetailsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListPackingGroupItemsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListPackingOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListPlacementOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentBoxesResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentItemsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListTransportationOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ScheduleSelfShipAppointmentRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ScheduleSelfShipAppointmentResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.SetPackingInformationRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.SetPackingInformationResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Shipment;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UpdateItemComplianceDetailsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UpdateItemComplianceDetailsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UpdateShipmentSourceAddressResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UpdateShipmentTrackingDetailsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UpdateShipmentTrackingDetailsResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;

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

}
