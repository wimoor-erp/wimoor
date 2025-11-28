package com.wimoor.amazon.inboundV2.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.util.StrUtil;
import com.amazon.spapi.model.fulfillmentinboundV20240320.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.threeten.bp.format.DateTimeFormatter;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.api.FbaInboundV20240320Api;
import com.amazon.spapi.client.ApiClient;
import com.amazon.spapi.client.ApiException;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundItemV2Mapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.service.IInboundApiHandlerService;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundBoxService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
@Service
public class InboundApiHandlerServiceImpl implements IInboundApiHandlerService {
	@Autowired
	ApiBuildService apiBuildService;
	@Resource
	private ShipInboundItemV2Mapper shipInboundItemV2Mapper;
	@Resource
	IShipAddressService shipAddressService;
	@Resource
	IShipAddressToService shipAddressToService;
	@Resource
	IProductInfoService iProductInfoService;
	@Autowired
	IShipInboundTransService shipInboundTransService;
	@Autowired
	IMarketplaceService marketplaceService;
	@Resource
	ShipInboundShipmentV2Mapper shipInboundShipmentV2Mapper;
	@Autowired
	IShipInboundShipmentRecordService shipInboundShipmentRecordService;
	@Autowired
	ErpClientOneFeignManager erpClientOneFeign;
	@Autowired
	IShipInboundBoxService shipInboundBoxService;
    @Value("${spring.profiles.active}")
    String profile;
  
    public ListInboundPlansResponse listInboundPlans(AmazonAuthority amazonAuthority,
    		Integer pageSize,String paginationToken,String status,String sortBy,String sortOrder) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListInboundPlansResponse response = null;
		try {
			response = api.listInboundPlans(pageSize, paginationToken, status, sortBy, sortOrder);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
    }
    public AddressInput getAddress(ShipAddress address) {
    	AddressInput sourceAddress = new AddressInput();
		sourceAddress.setAddressLine1(address.getAddressline1());
		sourceAddress.setAddressLine2(address.getAddressline2());
		sourceAddress.setCity(address.getCity());
		sourceAddress.setCompanyName(address.getCompanyName());
		sourceAddress.setCountryCode(address.getCountrycode());
		sourceAddress.setEmail(address.getEmail());
		sourceAddress.setName(address.getName());
		sourceAddress.setPhoneNumber(address.getPhone());
		sourceAddress.setPostalCode(address.getPostalcode());
		sourceAddress.setStateOrProvinceCode(address.getStateorprovincecode());
		return sourceAddress;
    }
    public CreateInboundPlanResponse createInboundPlan(AmazonAuthority amazonAuthority,Marketplace market,ShipInboundPlan plan, List<ShipInboundItem> itemlist, ShipAddress address) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	CreateInboundPlanRequest body=new CreateInboundPlanRequest();
		CreateInboundPlanResponse response = null;
		try {
			body.setName(plan.getName());
			List<ItemInput> items=new ArrayList<ItemInput>();
			for(ShipInboundItem myitem:itemlist) {
				if(myitem.getConfirmQuantity()<=0){
					continue;
				}
				ItemInput item=new ItemInput();
				item.setLabelOwner(LabelOwner.valueOf(myitem.getLabelOwner()));
				item.setPrepOwner(PrepOwner.valueOf(myitem.getPrepOwner()));
				item.setMsku(myitem.getSku());
				if(myitem.getConfirmQuantity()!=null) {
					item.setQuantity(myitem.getConfirmQuantity());
				}else {
					item.setQuantity(myitem.getQuantity());
				}
				item.setExpiration(GeneralUtil.formatDate(myitem.getExpiration()));
				item.setManufacturingLotCode(myitem.getManufacturingLotCode());
				items.add(item);
			}
			body.setSourceAddress(getAddress(address) );
			body.setItems(items);
			List<String> destinationMarketplaces=new ArrayList<String>();
			destinationMarketplaces.add(market.getMarketplaceid());
			body.setDestinationMarketplaces(destinationMarketplaces);
			response = api.createInboundPlan(body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
		return response;
    }
    public InboundPlan getInboundPlan(AmazonAuthority amazonAuthority,String inboundPlanId) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	InboundPlan response = null;
		try {
			response = api.getInboundPlan(inboundPlanId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    public ListInboundPlanBoxesResponse listInboundPlanBoxes(AmazonAuthority amazonAuthority,String inboundPlanId,Integer pageSize,	String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListInboundPlanBoxesResponse response = null;
		try {
			response = api.listInboundPlanBoxes(inboundPlanId, pageSize, paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public ListShipmentBoxesResponse listShipmentBoxes(AmazonAuthority amazonAuthority,String inboundPlanId,String shipmentid,Integer pageSize,	String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	  ListShipmentBoxesResponse response = null;
		try {
			response = api.listShipmentBoxes(inboundPlanId,shipmentid, pageSize, paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    public CancelInboundPlanResponse cancelInboundPlan(AmazonAuthority amazonAuthority,String inboundPlanId) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	CancelInboundPlanResponse response = null;
		try {
			response = api.cancelInboundPlan(inboundPlanId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public ListInboundPlanItemsResponse listInboundPlanItems(AmazonAuthority amazonAuthority,String inboundPlanId, Integer pageSize, String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListInboundPlanItemsResponse response = null;
		try {
			response = api.listInboundPlanItems(inboundPlanId,pageSize,paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public SetPackingInformationResponse setPackingInformation(AmazonAuthority amazonAuthority,String inboundPlanId, SetPackingInformationRequest body) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	SetPackingInformationResponse response = null;
		try {
			response = api.setPackingInformation(inboundPlanId, body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public ListPackingOptionsResponse listPackingOptions(AmazonAuthority amazonAuthority,String inboundPlanId, Integer pageSize, String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListPackingOptionsResponse response = null;
		try {
			response = api.listPackingOptions(inboundPlanId, pageSize, paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public GeneratePackingOptionsResponse generatePackingOptions(AmazonAuthority amazonAuthority,String inboundPlanId) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	GeneratePackingOptionsResponse response = null;
		try {
			response = api.generatePackingOptions(inboundPlanId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public ConfirmPackingOptionResponse confirmPackingOption(AmazonAuthority amazonAuthority,String inboundPlanId, String packingOptionId) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ConfirmPackingOptionResponse response = null;
		try {
			response = api.confirmPackingOption(inboundPlanId, packingOptionId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
 
    
    public ListInboundPlanPalletsResponse listInboundPlanPallets(AmazonAuthority amazonAuthority,String inboundPlanId, Integer pageSize, String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListInboundPlanPalletsResponse response = null;
		try {
			response = api.listInboundPlanPallets(inboundPlanId, pageSize, paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public ListPlacementOptionsResponse listPlacementOptions(AmazonAuthority amazonAuthority,String inboundPlanId, Integer pageSize, String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListPlacementOptionsResponse response = null;
		try {
			response = api.listPlacementOptions(inboundPlanId, pageSize, paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public GeneratePlacementOptionsResponse generatePlacementOptions(AmazonAuthority amazonAuthority,String inboundPlanId, GeneratePlacementOptionsRequest body) {
    	   FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	   GeneratePlacementOptionsResponse response = null;
		try {
			response = api.generatePlacementOptions(inboundPlanId,body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	   return response;
    }
    @Override
	public ConfirmDeliveryWindowOptionsResponse confirmDeliveryWindowOptions(AmazonAuthority amazonAuthority, String inboundPlanId, String shipmentid, String deliveryWindowOptionId) {
		// TODO Auto-generated method stub
    	 FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	 ConfirmDeliveryWindowOptionsResponse response=null;
    	 try {
 			response = api.confirmDeliveryWindowOptions( inboundPlanId,  shipmentid,  deliveryWindowOptionId);
 		} catch (ApiException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			throw new BizException("平台错误："+e.getResponseBody());
 		} catch (LWAException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			throw new BizException("授权错误："+e.getErrorMessage());
 		}
     	   return response;
	}
    
    public ConfirmPlacementOptionResponse confirmPlacementOption(AmazonAuthority amazonAuthority,String inboundPlanId, String placementOptionId) {
    	   FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	   ConfirmPlacementOptionResponse response = null;
		try {
			response = api.confirmPlacementOption(inboundPlanId,placementOptionId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	   return response;
    }
    
    public UpdateShipmentSourceAddressResponse updateShipmentSourceAddress(AmazonAuthority amazonAuthority,
			String inboundPlanId, String shipmentid, ShipAddress shipAddress) {
    	 FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
 	     UpdateShipmentSourceAddressResponse response = null;
		try {
			UpdateShipmentSourceAddressRequest mybody=new UpdateShipmentSourceAddressRequest();
			mybody.setAddress(this.getAddress(shipAddress));
			response = api.updateShipmentSourceAddress(inboundPlanId,shipmentid,mybody);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
 	   return response;
 }
    
    public Shipment getShipment(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	Shipment response = null;
		try {
			response = api.getShipment(inboundPlanId,shipmentId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public GetDeliveryChallanDocumentResponse getDeliveryChallanDocument(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	GetDeliveryChallanDocumentResponse response = null;
		try {
			response = api.getDeliveryChallanDocument(inboundPlanId,shipmentId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
 
    public GetSelfShipAppointmentSlotsResponse getSelfShipAppointmentSlots(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, Integer pageSize, String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	GetSelfShipAppointmentSlotsResponse response = null;
		try {
			response = api.getSelfShipAppointmentSlots(inboundPlanId,shipmentId,pageSize,paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
        return response;
    }
    
    public GenerateSelfShipAppointmentSlotsResponse generateSelfShipAppointmentSlots(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, GenerateSelfShipAppointmentSlotsRequest body) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	GenerateSelfShipAppointmentSlotsResponse response = null;
		try {
			response = api.generateSelfShipAppointmentSlots(inboundPlanId,shipmentId,body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
 
    
    public ScheduleSelfShipAppointmentResponse scheduleSelfShipAppointment(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, String slotId, ScheduleSelfShipAppointmentRequest body) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ScheduleSelfShipAppointmentResponse response = null;
		try {
			response = api.scheduleSelfShipAppointment(inboundPlanId,shipmentId,slotId,body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
        return response;
    }
    
    public UpdateShipmentTrackingDetailsResponse updateShipmentTrackingDetails(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, UpdateShipmentTrackingDetailsRequest body) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	UpdateShipmentTrackingDetailsResponse response = null;
		try {
			response = api.updateShipmentTrackingDetails(inboundPlanId,shipmentId,body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public ListTransportationOptionsResponse listTransportationOptions(AmazonAuthority amazonAuthority,String inboundPlanId, String placementOptionId, String shipmentId, Integer pageSize, String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListTransportationOptionsResponse response = null;
		try {
			if(StrUtil.isNotBlank(inboundPlanId)&&(StrUtil.isNotBlank(shipmentId)||StrUtil.isNotBlank(placementOptionId))){
				response = api.listTransportationOptions(inboundPlanId,pageSize,paginationToken,placementOptionId,shipmentId);
			}else{
				throw new BizException("listTransportationOptions参数错误");
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
        return response;
    }
    
    public ListDeliveryWindowOptionsResponse listDeliveryWindowOptions(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentId, Integer pageSize, String paginationToken) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListDeliveryWindowOptionsResponse response = null;
		try {
			response = api.listDeliveryWindowOptions( inboundPlanId,  shipmentId,  pageSize,  paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
        return response;
    }
    
    public GenerateTransportationOptionsResponse generateTransportationOptions(AmazonAuthority amazonAuthority,String inboundPlanId, GenerateTransportationOptionsRequest body) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	GenerateTransportationOptionsResponse response = null;
		try {
			ApiClient client = api.getApiClient();
		    DateTimeFormatter format= DateTimeFormatter.ISO_INSTANT;
			client.setOffsetDateTimeFormat(format);
			response = api.generateTransportationOptions(inboundPlanId,body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public GenerateDeliveryWindowOptionsResponse  generateDeliveryWindowOptions(AmazonAuthority amazonAuthority,String inboundPlanId, String shipmentid) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	GenerateDeliveryWindowOptionsResponse response = null;
		try {
			ApiClient client = api.getApiClient();
		    DateTimeFormatter format= DateTimeFormatter.ISO_INSTANT;
			client.setOffsetDateTimeFormat(format);
			response = api.generateDeliveryWindowOptions(inboundPlanId,shipmentid);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public ConfirmTransportationOptionsResponse confirmTransportationOptions(AmazonAuthority amazonAuthority,String inboundPlanId, ConfirmTransportationOptionsRequest body) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ConfirmTransportationOptionsResponse response = null;
		try {
			response = api.confirmTransportationOptions(inboundPlanId,body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public ListItemComplianceDetailsResponse listItemComplianceDetails(AmazonAuthority amazonAuthority,List<String> mskus, String marketplaceId) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	ListItemComplianceDetailsResponse response = null;
		try {
			response = api.listItemComplianceDetails(mskus,marketplaceId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
    public UpdateItemComplianceDetailsResponse updateItemComplianceDetails(AmazonAuthority amazonAuthority,String marketplaceId, UpdateItemComplianceDetailsRequest body) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	UpdateItemComplianceDetailsResponse response =null;
    	try {
			  response = api.updateItemComplianceDetails(marketplaceId,body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return response;
    }
    
    public InboundOperationStatus getInboundOperationStatus(AmazonAuthority amazonAuthority,String operationId) {
    	FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	InboundOperationStatus response = null;
		try {
			response = api.getInboundOperationStatus(operationId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
    }
    
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ListPackingGroupItemsResponse listPackingGroupItems(AmazonAuthority amazonAuthority, String inboundPlanId,
			String packingOptionId, String packingGroupId, Integer pageSize, String paginationToken) {
		// TODO Auto-generated method stub
		FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
    	  ListPackingGroupItemsResponse response = null;
		try {
			response = api.listPackingGroupItems(  inboundPlanId,   packingGroupId,   pageSize,   paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
    	return response;
	}
	@Override
	public CancelSelfShipAppointmentResponse cancelSelfShipAppointment(AmazonAuthority amazonAuthority,
			String inboundPlanId, String shipmentId, String slotId, CancelSelfShipAppointmentRequest body) {
		// TODO Auto-generated method stub
		FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(amazonAuthority);
		CancelSelfShipAppointmentResponse response = null;
		try {
			response = api.cancelSelfShipAppointment(inboundPlanId,shipmentId, body );
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
  	return response;
	}
	
	@Override
	public ListShipmentItemsResponse  listShipmentItems(AmazonAuthority auth,String inboundPlanId, String shipmentId,Integer pageSize,String paginationToken) {
		// TODO Auto-generated method stub
		FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(auth);
		ListShipmentItemsResponse response=null;
		try {
			response = api.listShipmentItems(inboundPlanId, shipmentId, pageSize, paginationToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
  	return response;
	}

	public ListPrepDetailsResponse listPrepDetails(AmazonAuthority auth, String marketplaceid, List<String> sku)  {
		FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(auth);
		ListPrepDetailsResponse result=new ListPrepDetailsResponse();
		try {
			ListPrepDetailsResponse response=null;
			for(int i=0;i<sku.size();i++){
				response =api.listPrepDetails(marketplaceid, Arrays.asList(sku.get(i)));
				if(response!=null&&response.getMskuPrepDetails().size()>0){
					for(MskuPrepDetail item:response.getMskuPrepDetails()){
						result.addMskuPrepDetailsItem(item);
					}
				}
			}

		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
		return result;
	}

	public SetPrepDetailsResponse setPrepDetails(AmazonAuthority auth, String marketplaceid,List<MskuPrepDetailInput> mskuprep)  {
		FbaInboundV20240320Api api = apiBuildService.getInboundV20240320Api(auth);
		SetPrepDetailsResponse response=null;
		try {
			SetPrepDetailsRequest body=new SetPrepDetailsRequest();
			body.setMarketplaceId(marketplaceid);
			body.setMskuPrepDetails(mskuprep);
			response =api.setPrepDetails(body);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("平台错误："+e.getResponseBody());
		} catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("授权错误："+e.getErrorMessage());
		}
		return response;
	}
}
