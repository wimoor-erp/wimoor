package com.wimoor.amazon.inbound.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.FbaInboundApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.fulfillmentinbound.Address;
import com.amazon.spapi.model.fulfillmentinbound.CommonTransportResult;
import com.amazon.spapi.model.fulfillmentinbound.Condition;
import com.amazon.spapi.model.fulfillmentinbound.CreateInboundShipmentPlanRequest;
import com.amazon.spapi.model.fulfillmentinbound.CreateInboundShipmentPlanResponse;
import com.amazon.spapi.model.fulfillmentinbound.CreateInboundShipmentPlanResult;
import com.amazon.spapi.model.fulfillmentinbound.GetInboundGuidanceResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetInboundGuidanceResult;
import com.amazon.spapi.model.fulfillmentinbound.GetLabelsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetPrepInstructionsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetPrepInstructionsResult;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentsResult;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentHeader;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentItem;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentItemList;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentList;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentPlan;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentPlanItem;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentPlanList;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentPlanRequestItem;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentPlanRequestItemList;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentRequest;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentResponse;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentResult;
import com.amazon.spapi.model.fulfillmentinbound.IntendedBoxContentsSource;
import com.amazon.spapi.model.fulfillmentinbound.LabelDownloadURL;
import com.amazon.spapi.model.fulfillmentinbound.LabelPrepPreference;
import com.amazon.spapi.model.fulfillmentinbound.NonPartneredLtlDataInput;
import com.amazon.spapi.model.fulfillmentinbound.NonPartneredSmallParcelDataInput;
import com.amazon.spapi.model.fulfillmentinbound.NonPartneredSmallParcelPackageInput;
import com.amazon.spapi.model.fulfillmentinbound.NonPartneredSmallParcelPackageInputList;
import com.amazon.spapi.model.fulfillmentinbound.PrepDetails;
import com.amazon.spapi.model.fulfillmentinbound.PrepDetailsList;
import com.amazon.spapi.model.fulfillmentinbound.PrepInstruction;
import com.amazon.spapi.model.fulfillmentinbound.PrepOwner;
import com.amazon.spapi.model.fulfillmentinbound.PutTransportDetailsRequest;
import com.amazon.spapi.model.fulfillmentinbound.PutTransportDetailsResponse;
import com.amazon.spapi.model.fulfillmentinbound.SKUPrepInstructions;
import com.amazon.spapi.model.fulfillmentinbound.SKUPrepInstructionsList;
import com.amazon.spapi.model.fulfillmentinbound.ShipmentStatus;
import com.amazon.spapi.model.fulfillmentinbound.ShipmentType;
import com.amazon.spapi.model.fulfillmentinbound.TransportDetailInput;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
@Service
public class FulfillmentInboundServiceImpl implements IFulfillmentInboundService {
	@Autowired
	ApiBuildService apiBuildService;
	@Resource
	private IShipInboundItemService shipInboundItemService;
	@Resource
	IShipAddressService shipAddressService;
	@Resource
	IShipAddressToService shipAddressToService;
	@Resource
	IProductInfoService iProductInfoService;
	@Autowired
	IShipInboundTransService shipInboundTransService;

	
	public GetInboundGuidanceResult getInboundGuidance(AmazonAuthority auth,String marketplaceId,List<String> sellerSKUList){
		 FbaInboundApi api = apiBuildService.getInboundApi(auth); 
		 GetInboundGuidanceResponse result;
		 try {
				result = api.getInboundGuidance(marketplaceId, sellerSKUList, null);
				GetInboundGuidanceResult response = result.getPayload();
				return response;
			 } catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BizException("亚马逊API错误："+e.getMessage());
		     }
	}
	
	private ShipAddressTo getLocalShipToAddress(Address shipToAddress) {
		ShipAddressTo localShipToAddress = new ShipAddressTo();
		localShipToAddress.setOpttime(new Date());
		localShipToAddress.setAddressline1(shipToAddress.getAddressLine1());
		localShipToAddress.setAddressline2(shipToAddress.getAddressLine2());
		localShipToAddress.setCity(shipToAddress.getCity());
		localShipToAddress.setCountrycode(shipToAddress.getCountryCode());
		localShipToAddress.setDistrictorcounty(shipToAddress.getDistrictOrCounty());
		localShipToAddress.setIsfrom(false);
		localShipToAddress.setName(shipToAddress.getName());
		localShipToAddress.setPostalcode(shipToAddress.getPostalCode());
		return localShipToAddress;
	}
	
	private Address getShipFromAddress(ShipAddress localAddress ) {
		Address address = new Address();
		address.setAddressLine1(localAddress.getAddressline1());
		address.setAddressLine2(localAddress.getAddressline2());
		address.setCity(localAddress.getCity());
		address.setCountryCode(localAddress.getCountrycode());
		address.setDistrictOrCounty(localAddress.getDistrictorcounty());
		address.setName(localAddress.getName());
		address.setPostalCode(localAddress.getPostalcode());
		address.setStateOrProvinceCode(localAddress.getStateorprovincecode());
		return address;
	}
	
	public List<ShipInboundShipment> createInboundShipmentPlan(AmazonAuthority auth,	Marketplace market ,ShipInboundPlan inplan) {
			FbaInboundApi api = apiBuildService.getInboundApi(auth);
			ShipAddress localAddress = shipAddressService.getById(inplan.getShipfromaddressid());
			CreateInboundShipmentPlanRequest body=new CreateInboundShipmentPlanRequest();
			InboundShipmentPlanRequestItemList item=new InboundShipmentPlanRequestItemList();
			List<ShipInboundItem> itemlist = inplan.getPlanitemlist();
			List<String> mlist = new ArrayList<String>();
			for(int i=0;i<itemlist.size();i++) {
				 ShipInboundItem itemsku = itemlist.get(i);
				 mlist.add(itemsku.getSellersku());
			}
			GetPrepInstructionsResponse prepInst;
			Map<String,SKUPrepInstructions> skuInsMap =new HashMap<String,SKUPrepInstructions>();
			List<ShipInboundShipment> shipmentList=new ArrayList<ShipInboundShipment>();
			try {
				prepInst = api.getPrepInstructions(market.getMarket(), mlist, null);
				if (prepInst != null && prepInst.getPayload() != null) {
					 GetPrepInstructionsResult prepResult = prepInst.getPayload();
					 SKUPrepInstructionsList skuPreplist = prepResult.getSkUPrepInstructionsList();
					for (int i = 0; i < skuPreplist.size(); i++) {
						  SKUPrepInstructions skupre = skuPreplist.get(i);
						  skuInsMap.put(skupre.getSellerSKU(), skupre);
					}
				}
			} catch (ApiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new BizException("亚马逊API错误："+e1.getMessage());
			}

			for(int i=0;i<itemlist.size();i++) {
				ShipInboundItem itemsku = itemlist.get(i);
				InboundShipmentPlanRequestItem element=new InboundShipmentPlanRequestItem();
				ProductInfo product=iProductInfoService.productOnlyone(auth.getId(),itemsku.getSellersku(),inplan.getMarketplaceid());
				element.sellerSKU(itemsku.getSellersku());
				element.setQuantity(itemsku.getQuantity());
				element.setASIN(product.getAsin());
			    element.setCondition(Condition.NEWITEM);
				SKUPrepInstructions prepIns = skuInsMap.get(itemsku.getSellersku());
				   if(prepIns!=null) {
					   PrepDetailsList prelist=new PrepDetailsList();
					   for(PrepInstruction preins: prepIns.getPrepInstructionList()) {
						   PrepDetails e=new PrepDetails();
						    PrepOwner preowner=PrepOwner.SELLER;
							e.setPrepOwner(preowner);
							PrepInstruction prepinstruction=PrepInstruction.fromValue(preins.getValue());
							e.setPrepInstruction(prepinstruction);
							prelist.add(e);
					   }
					   element.setPrepDetailsList(prelist);
				   }
			}
	
			body.setInboundShipmentPlanRequestItems(item);
			LabelPrepPreference label=LabelPrepPreference.SELLER_LABEL;
			body.setLabelPrepPreference(label);
			Address address = getShipFromAddress(localAddress);
			body.setShipFromAddress(address);
			body.setShipToCountryCode(market.getMarket());
			body.setShipToCountrySubdivisionCode(market.getMarket());
			try {
				CreateInboundShipmentPlanResponse reponse = api.createInboundShipmentPlan(body);
				CreateInboundShipmentPlanResult result = reponse.getPayload();
				InboundShipmentPlanList plan = result.getInboundShipmentPlans();
				for(InboundShipmentPlan shipment:plan) {
					Address shipToAddress = shipment.getShipToAddress();
					ShipAddressTo localShipToAddress = getLocalShipToAddress(shipToAddress);
					localShipToAddress.setOperator(inplan.getOperator());
					localShipToAddress.setShopid(inplan.getShopid());
					shipAddressToService.save(localShipToAddress);
					for(InboundShipmentPlanItem shipitem:shipment.getItems()) {
						ShipInboundItem localSkuItem=new ShipInboundItem();
						localSkuItem.setFulfillmentnetworksku(shipitem.getFulfillmentNetworkSKU());
						localSkuItem.setQuantity(shipitem.getQuantity());
						localSkuItem.setQuantityshipped(shipitem.getQuantity());
						localSkuItem.setSellersku(shipitem.getSellerSKU());
						localSkuItem.setShipmentid(shipment.getShipmentId());
						localSkuItem.setInboundplanid(inplan.getId());
						localSkuItem.setOperator(inplan.getOperator());
						localSkuItem.setOpttime(new Date());
						shipitem.getPrepDetailsList().get(0).getPrepOwner().getValue();
						shipInboundItemService.save(localSkuItem); 
					}
					ShipInboundShipment localShipment = new ShipInboundShipment();
					localShipment.setShiptoaddressid(localShipToAddress.getId());
					localShipment.setShipmentid(shipment.getShipmentId());
					localShipment.setLabelpreptype(shipment.getLabelPrepType().getValue());
					localShipment.setInboundplanid(inplan.getId());
					localShipment.setDestinationfulfillmentcenterid(shipment.getDestinationFulfillmentCenterId());
					localShipment.setStatus(1);
					localShipment.setStatus1date(new Date());
					localShipment.setShipmentstatus(ShipInboundShipment.ShipmentStatus_WORKING);
					localShipment.setInboundplanid(inplan.getId());
					localShipment.setCreator(inplan.getOperator());
					localShipment.setCreatedate(new Date());
					localShipment.setOpttime(new Date());
					localShipment.setOperator(inplan.getOperator());
					shipmentList.add(localShipment);
					ShipInboundTrans oldinfo = inplan.getTransinfo();
					if(oldinfo!=null) {
						ShipInboundTrans transInfo = new ShipInboundTrans();
						transInfo.setShipmentid(shipment.getShipmentId());
						transInfo.setChannel(oldinfo.getChannel());
						transInfo.setCompany(oldinfo.getCompany());
						transInfo.setOperator(oldinfo.getOperator());
						transInfo.setOpttime(oldinfo.getOpttime());
						shipInboundTransService.save(transInfo);
					}
				}
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BizException("亚马逊API错误："+e.getMessage());
			}
			return shipmentList;
	   }
	 
	 
	public String createInboundShipment(AmazonAuthority amazonAuthority,	Marketplace marketplace ,ShipInboundPlan plan, ShipInboundShipment shipment)  {// 点击审核通过
		ShipInboundPlan inboundplan = plan;
		if (inboundplan == null) {
			return null;// 此处要加入异常逻辑
		}
		FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		ShipAddress localAddress = shipAddressService.getById(inboundplan.getShipfromaddressid());
		InboundShipmentRequest body=new InboundShipmentRequest();
		InboundShipmentHeader header=new InboundShipmentHeader();
		header.setAreCasesRequired(inboundplan.getArecasesrequired());
		header.setDestinationFulfillmentCenterId(shipment.getDestinationfulfillmentcenterid());
		header.setLabelPrepPreference(LabelPrepPreference.SELLER_LABEL);
		header.setShipmentName(shipment.getName());
		Address address = getShipFromAddress(localAddress);
		header.setShipFromAddress(address);
		if(shipment.getIntendedBoxContentsSource()!=null) {
			header.setIntendedBoxContentsSource(IntendedBoxContentsSource.fromValue(shipment.getIntendedBoxContentsSource()));
		}else {
			header.setIntendedBoxContentsSource(IntendedBoxContentsSource.FEED) ;
		}
	 
		shipment.setShipmentstatus(ShipmentStatus.WORKING.getValue());
	    header.setShipmentStatus(ShipmentStatus.fromValue(shipment.getShipmentstatus()));
		body.setInboundShipmentHeader(header);
		body.setMarketplaceId(marketplace.getMarketplaceid());
		List<ShipInboundItem> itemlist = shipment.getItemList();
		InboundShipmentItemList items=new InboundShipmentItemList();
	    for(int i=0;i<10;i++) {
	    	ShipInboundItem itemsku = itemlist.get(i);
			InboundShipmentItem element=new InboundShipmentItem();
			element.sellerSKU(itemsku.getSellersku());
			element.setShipmentId(shipment.getShipmentid());
			element.setQuantityShipped(itemsku.getQuantityshipped());
			element.setFulfillmentNetworkSKU(itemsku.getFulfillmentnetworksku());
		    items.add(element);
		 }
		body.setInboundShipmentItems(items);

		shipment.setStatus(2);
		shipment.setStatus2date(new Date());
		if (shipment.getName() == null) {
			Calendar calender = Calendar.getInstance();
			String newship = "FBA" + "(" + (calender.get(Calendar.MONTH) + 1) + "/" + (calender.get(Calendar.DATE))
					+ "/" + calender.get(Calendar.YEAR) + " " + calender.get(Calendar.HOUR) + ":" + calender.get(Calendar.MINUTE) + ")-1";
			shipment.setName(newship);
		}
		InboundShipmentResponse response = null;
		try {
			response = api.createInboundShipment(body, shipment.getShipmentid());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			if (e.getMessage() != null && e.getMessage().contains("Invalid plannedShipmentId")) {
				throw new BizException("审核失败，请保证订单是在48小时内被审核。");
			} else if (e.getMessage() != null && e.getMessage().contains("SellerSKU")) {
				throw new BizException("审核失败，请确保亚马逊后台SKU信息正确。");
			} else {
				throw new BizException(e.getMessage());
			}
		}
		if(response!=null&&response.getPayload()!=null) {
			InboundShipmentResult result = response.getPayload();
			if(result.getShipmentId().equals(shipment.getShipmentid())) {
				return result.getShipmentId();
			}
		}
	
		return "";
	}

	
	public String updateInboundShipment(AmazonAuthority amazonAuthority,Marketplace marketplace,ShipInboundPlan inboundplan ,ShipInboundShipment shipment)  {
			FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
			ShipAddress localAddress = shipAddressService.getById(inboundplan.getShipfromaddressid());
			InboundShipmentRequest body=new InboundShipmentRequest();
			InboundShipmentHeader header=new InboundShipmentHeader();
			header.setAreCasesRequired(inboundplan.getArecasesrequired());
			header.setDestinationFulfillmentCenterId(shipment.getDestinationfulfillmentcenterid());
			header.setLabelPrepPreference(LabelPrepPreference.SELLER_LABEL);
			header.setShipmentName(shipment.getName());
			Address address = getShipFromAddress(localAddress);
			header.setShipFromAddress(address);
			if(shipment.getIntendedBoxContentsSource()!=null) {
				header.setIntendedBoxContentsSource(IntendedBoxContentsSource.fromValue(shipment.getIntendedBoxContentsSource()));
			}else {
				header.setIntendedBoxContentsSource(IntendedBoxContentsSource.FEED) ;
			}
			if (shipment.getStatus() == 0) {
				shipment.setShipmentstatus(ShipmentStatus.CANCELLED.getValue());
			} 
		    header.setShipmentStatus(ShipmentStatus.fromValue(shipment.getShipmentstatus()));
			body.setInboundShipmentHeader(header);
			body.setMarketplaceId(marketplace.getMarketplaceid());
			List<ShipInboundItem> itemlist = shipment.getItemList();
			InboundShipmentItemList items=new InboundShipmentItemList();
		    for(int i=0;i<10;i++) {
		    	ShipInboundItem itemsku = itemlist.get(i);
				InboundShipmentItem element=new InboundShipmentItem();
				element.sellerSKU(itemsku.getSellersku());
				element.setShipmentId(shipment.getShipmentid());
				element.setQuantityShipped(itemsku.getQuantityshipped());
				element.setFulfillmentNetworkSKU(itemsku.getFulfillmentnetworksku());
			    items.add(element);
			 }
			body.setInboundShipmentItems(items);
			InboundShipmentResponse response;
			try {
				response = api.updateInboundShipment(body, shipment.getShipmentid());
				InboundShipmentResult result = response.getPayload();
				return result.getShipmentId();
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BizException("亚马逊API错误："+e.getMessage());
			}
	}
	
	public InboundShipmentList listShipment(AmazonAuthority auth,Marketplace market,String startdate,String enddate){
		 FbaInboundApi api = apiBuildService.getInboundApi(auth);
	     try {
	    	   Date start = GeneralUtil.getDatez(startdate);
	    	   Calendar cstart=Calendar.getInstance();
	    	   cstart.setTime(start);
	    	   Date end = GeneralUtil.getDatez(enddate);
	    	   Calendar cend=Calendar.getInstance();
	    	   cend.setTime(end);
			   GetShipmentsResponse response = api.getShipments("DATE_RANGE", market.getMarketplaceid(), null, null,
					   AmzDateUtils.getOffsetDateTime(cstart), AmzDateUtils.getOffsetDateTime(cend), null);
			   GetShipmentsResult result = response.getPayload();
			   InboundShipmentList data = result.getShipmentData();
			   while(result.getNextToken()!=null) {
					response=api.getShipments("NEXT_TOKEN", market.getMarketplaceid(), null, null, null, null, result.getNextToken());
					result=response.getPayload();
					data.addAll(result.getShipmentData());
				}
			  return data;
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("亚马逊API错误："+e.getMessage());
		}
	}

	@Override
	public String putTransportDetailsRequest(AmazonAuthority amazonAuthority, Marketplace marketplace,ShipInboundPlan inboundplan ,
			ShipInboundShipment shipment) {
		 // TODO Auto-generated method stub
		 FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		 PutTransportDetailsRequest body=new PutTransportDetailsRequest();
		 body.setIsPartnered(false);
		 body.setShipmentType(ShipmentType.fromValue(shipment.getTranstyle()));
		 TransportDetailInput detail=new TransportDetailInput();
		 if(ShipmentType.LTL.getValue().equals(shipment.getTranstyle())) {
			 NonPartneredLtlDataInput nonpart=new NonPartneredLtlDataInput();
			 nonpart.setCarrierName(shipment.getCarrier());
			 nonpart.setProNumber("123456");
			detail.setNonPartneredLtlData(nonpart);
		 }else {
			 NonPartneredSmallParcelDataInput nonpart=new NonPartneredSmallParcelDataInput();
			 nonpart.setCarrierName(shipment.getCarrier());
			 NonPartneredSmallParcelPackageInputList pkglist=new NonPartneredSmallParcelPackageInputList();
			 NonPartneredSmallParcelPackageInput element=new NonPartneredSmallParcelPackageInput();
			 element.setTrackingId("123456");
			 pkglist.add(element);
			 nonpart.setPackageList(pkglist);
			 detail.setNonPartneredSmallParcelData(nonpart);
		 }
		 body.setTransportDetails(detail);
		 try {
			PutTransportDetailsResponse response = api.putTransportDetails(shipment.getShipmentid(), body);
			CommonTransportResult result = response.getPayload();
			shipment.setTransportStatus(result.getTransportResult().getTransportStatus().getValue());
			return result.getTransportResult().getTransportStatus().getValue();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("亚马逊API错误："+e.getMessage());
		}
	}
	
	public String getLabelDownloadUR(AmazonAuthority amazonAuthority,ShipInboundShipment shipment,String pagetype){
		 FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		 String labelType="UNIQUE";
		 if(shipment.getTranstyle().equals("LTL")) {
			 labelType="PALLET";
		 }
		 try {
			GetLabelsResponse response = api.getLabels(shipment.getShipmentid(),pagetype , labelType, null, null, null, null, null);
			LabelDownloadURL url = response.getPayload();
			return url.getDownloadURL();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("亚马逊API错误："+e.getMessage());
		}
		 
	}
	
}
