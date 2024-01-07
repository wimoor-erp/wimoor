package com.wimoor.amazon.inbound.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.FbaInboundApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.fulfillmentinbound.Address;
import com.amazon.spapi.model.fulfillmentinbound.BoxContentsFeeDetails;
import com.amazon.spapi.model.fulfillmentinbound.CommonTransportResult;
import com.amazon.spapi.model.fulfillmentinbound.Condition;
import com.amazon.spapi.model.fulfillmentinbound.ConfirmTransportResponse;
import com.amazon.spapi.model.fulfillmentinbound.CreateInboundShipmentPlanRequest;
import com.amazon.spapi.model.fulfillmentinbound.CreateInboundShipmentPlanResponse;
import com.amazon.spapi.model.fulfillmentinbound.CreateInboundShipmentPlanResult;
import com.amazon.spapi.model.fulfillmentinbound.EstimateTransportResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetInboundGuidanceResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetInboundGuidanceResult;
import com.amazon.spapi.model.fulfillmentinbound.GetLabelsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetPrepInstructionsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetPrepInstructionsResult;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentItemsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentItemsResult;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentsResult;
import com.amazon.spapi.model.fulfillmentinbound.GetTransportDetailsResponse;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentHeader;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentInfo;
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.inbound.mapper.ShipInboundItemMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundShipmentMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundBoxService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.amazon.util.AmzUtil;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
@Service
public class FulfillmentInboundServiceImpl implements IFulfillmentInboundService {
	@Autowired
	ApiBuildService apiBuildService;
	@Resource
	private ShipInboundItemMapper shipInboundItemMapper;
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
	ShipInboundShipmentMapper shipInboundShipmentMapper;
	@Autowired
	IShipInboundShipmentRecordService shipInboundShipmentRecordService;
	@Autowired
	ErpClientOneFeignManager erpClientOneFeign;
	@Autowired
	IShipInboundBoxService shipInboundBoxService;
    @Value("${spring.profiles.active}")
    String profile;
 
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
				throw AmzUtil.getException(e);
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
		localShipToAddress.setStateorprovincecode(shipToAddress.getStateOrProvinceCode());
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
	
	public Map<String,SKUPrepInstructions>  getPrepInstructions(AmazonAuthority auth,	Marketplace market ,List<String> mlist) {
		GetPrepInstructionsResponse prepInst;
		auth.setUseApi("getPrepInstructions");
		FbaInboundApi api = apiBuildService.getInboundApi(auth);
		Map<String,SKUPrepInstructions> skuInsMap =new HashMap<String,SKUPrepInstructions>();
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
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
		return skuInsMap;
	}
	
	public List<ShipInboundShipment> createInboundShipmentPlan(AmazonAuthority auth,	Marketplace market ,ShipInboundPlan inplan) {
		    auth.setUseApi("createInboundShipmentPlan");
			FbaInboundApi api = apiBuildService.getInboundApi(auth);
			ShipAddress localAddress = shipAddressService.getById(inplan.getShipfromaddressid());
			CreateInboundShipmentPlanRequest body=new CreateInboundShipmentPlanRequest();
			InboundShipmentPlanRequestItemList item=new InboundShipmentPlanRequestItemList();
			List<ShipInboundItem> itemlist = inplan.getPlanitemlist();
			List<String> mlist = new ArrayList<String>();
			int skunum=0;
			for(int i=0;i<itemlist.size();i++) {
				 ShipInboundItem itemsku = itemlist.get(i);
				 mlist.add(itemsku.getSellersku());
				 skunum+=1;
			}
			inplan.setSkunum(skunum);
			List<ShipInboundShipment> shipmentList=new ArrayList<ShipInboundShipment>();
			Map<String, SKUPrepInstructions> skuInsMap = getPrepInstructions(auth,market,mlist);
			Map<String,ShipInboundItem> itemmap=new HashMap<String,ShipInboundItem>();
			for(int i=0;i<itemlist.size();i++) {
				ShipInboundItem itemsku = itemlist.get(i);
				InboundShipmentPlanRequestItem element=new InboundShipmentPlanRequestItem();
				ProductInfo product=iProductInfoService.productOnlyone(auth.getId(),itemsku.getSellersku(),inplan.getMarketplaceid());
				if(product==null) {
					throw new BizException("["+itemsku.getSellersku()+"]无法找到平台SKU,请确认已经同步到系统中");
				}
				element.sellerSKU(itemsku.getSellersku());
				itemmap.put(itemsku.getSellersku(), itemsku);
				element.setQuantity(itemsku.getQuantity());
				if(itemsku.getQuantityincase()!=null&&itemsku.getQuantityincase()>0) {
					element.setQuantityInCase(itemsku.getQuantityincase());
				}
				element.setASIN(product.getAsin());
			    element.setCondition(Condition.NEWITEM);
				if (skuInsMap!=null&&skuInsMap.size()>0) {
					SKUPrepInstructions prepIns = skuInsMap.get(itemsku.getSellersku());
					   if(prepIns!=null&&prepIns.getPrepInstructionList().size()>0) {
						   PrepDetailsList prelist=new PrepDetailsList();
						   for(PrepInstruction preins: prepIns.getPrepInstructionList()) {
							   PrepDetails e=new PrepDetails();
							   PrepOwner preowner=PrepOwner.SELLER;
								e.setPrepOwner(preowner);
								if(preins!=null&&StrUtil.isNotBlank(preins.getValue())) {
									PrepInstruction prepinstruction=PrepInstruction.fromValue(preins.getValue());
									if(prepinstruction!=null) {
										e.setPrepInstruction(prepinstruction);
										prelist.add(e);
									}
								}
						   }
						   element.setPrepDetailsList(prelist);
					   }else {
						   PrepDetailsList prelist=new PrepDetailsList();
						   PrepDetails e=new PrepDetails();
						   PrepOwner preowner=PrepOwner.SELLER;
						   e.setPrepOwner(preowner);
						   PrepInstruction prepinstruction=PrepInstruction.NONE;
						   e.setPrepInstruction(prepinstruction);
						   prelist.add(e);
						   element.setPrepDetailsList(prelist);
					   }
				 }
				 item.add(element);   
			}
			body.setInboundShipmentPlanRequestItems(item);
			LabelPrepPreference label=LabelPrepPreference.SELLER_LABEL;
			body.setLabelPrepPreference(label);
			Address address = getShipFromAddress(localAddress);
			body.setShipFromAddress(address);
			body.setShipToCountryCode(market.getMarket());
			//body.setShipToCountrySubdivisionCode(market.getMarket());
			try {
				CreateInboundShipmentPlanResponse reponse = api.createInboundShipmentPlan(body);
				CreateInboundShipmentPlanResult result = reponse.getPayload();
				InboundShipmentPlanList plan = result.getInboundShipmentPlans();
				int i=0;
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
						ShipInboundItem myitem = itemmap.get(shipitem.getSellerSKU());
						if(myitem!=null&&myitem.getQuantityincase()!=null) {
							localSkuItem.setQuantityincase(myitem.getQuantityincase());
						}
						if(shipitem.getPrepDetailsList()!=null&&shipitem.getPrepDetailsList().size()>0) {
							String preOwner=null;
							String preInstruction=null;
							for(PrepDetails pre:shipitem.getPrepDetailsList()) {
								if(pre==null||pre.getPrepInstruction()==null||pre.getPrepOwner()==null) {
									continue;
								}
								String owner=PrepOwner.SELLER.getValue();
								if(preOwner==null) {
									preOwner=owner;
								}else {
									preOwner=preOwner+","+owner;
								}
								String instruction=pre.getPrepInstruction().getValue();
								if(preInstruction==null) {
									preInstruction=instruction;
								}else {
									preInstruction=preInstruction+","+instruction;
								}
							}
							if(preOwner!=null) {
								localSkuItem.setPrepOwner(preOwner);
								localSkuItem.setPrepInstruction(preInstruction);
							}
						}
						shipInboundItemMapper.insert(localSkuItem); 
					}
					ShipInboundShipment localShipment = new ShipInboundShipment();
					Calendar calender = Calendar.getInstance();
					String newship = "FBA" + "(" + (calender.get(Calendar.MONTH) + 1) + "/" + (calender.get(Calendar.DATE))
							+ "/" + calender.get(Calendar.YEAR) + " " + calender.get(Calendar.HOUR) + ":" + calender.get(Calendar.MINUTE) + ")-" + (i + 1);
					localShipment.setName(newship);
					i++;
					localShipment.setShiptoaddressid(localShipToAddress.getId());
					localShipment.setShipmentid(shipment.getShipmentId());
					localShipment.setLabelpreptype(shipment.getLabelPrepType().getValue());
					localShipment.setInboundplanid(inplan.getId());
					localShipment.setDestinationfulfillmentcenterid(shipment.getDestinationFulfillmentCenterId());
					localShipment.setStatus(1);
					localShipment.setStatus1date(new Date());
					localShipment.setShipmentstatus(ShipInboundShipment.ShipmentStatus_WORKING);
					localShipment.setInboundplanid(inplan.getId());
					localShipment.setCreator(inplan.getCreator());
					localShipment.setCreatedate(new Date());
					localShipment.setOpttime(new Date());
					localShipment.setOperator(inplan.getOperator());
					shipmentList.add(localShipment);
					ShipInboundTrans oldinfo = inplan.getTransinfo();
					if(oldinfo!=null&&StrUtil.isNotBlank(oldinfo.getCompany())&&StrUtil.isNotBlank(oldinfo.getChannel())) {
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
				throw AmzUtil.getException(e);
			}
			return shipmentList;
	   }
	 
	 
	public String createInboundShipment(AmazonAuthority amazonAuthority,	Marketplace marketplace ,ShipInboundPlan plan, ShipInboundShipment shipment)  {// 点击审核通过
		ShipInboundPlan inboundplan = plan;
		if (inboundplan == null) {
			return null;// 此处要加入异常逻辑
		}
		if(!"prod".equals(this.profile)){return shipment.getShipmentid();}
		amazonAuthority.setUseApi("createInboundShipment");
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
	    if(shipment.getShipmentstatus()==null) {
	    	shipment.setShipmentstatus(ShipmentStatus.WORKING.getValue());
	    }
	    header.setShipmentStatus(ShipmentStatus.fromValue(shipment.getShipmentstatus()));
		body.setInboundShipmentHeader(header);
		body.setMarketplaceId(marketplace.getMarketplaceid());
		List<ShipInboundItem> itemlist = shipment.getItemList();
		InboundShipmentItemList items=new InboundShipmentItemList();
		Boolean haspre=false;
		for(int i=0;i<itemlist.size();i++) {
			ShipInboundItem itemsku = itemlist.get(i);
			if(StrUtil.isNotBlank(itemsku.getPrepInstruction())) {
				haspre=true;break;
			}
		}
	    for(int i=0;i<itemlist.size();i++) {
	    	ShipInboundItem itemsku = itemlist.get(i);
			InboundShipmentItem element=new InboundShipmentItem();
			element.sellerSKU(itemsku.getSellersku());
			element.setShipmentId(shipment.getShipmentid());
			element.setQuantityShipped(itemsku.getQuantityshipped());
			element.setFulfillmentNetworkSKU(itemsku.getFulfillmentnetworksku());
			if(itemsku.getQuantityincase()!=null&&itemsku.getQuantityincase()>0) {
				element.setQuantityInCase(itemsku.getQuantityincase());
			}
			if(StrUtil.isNotBlank(itemsku.getPrepInstruction())) {
				   String[] oldPrelist = itemsku.getPrepInstruction().split(",");
				   if(oldPrelist!=null&&oldPrelist.length>0) {
					   PrepDetailsList prelist=new PrepDetailsList();
					   for(String instruction:oldPrelist) {
						   PrepDetails e=new PrepDetails();
						   PrepOwner preowner=PrepOwner.SELLER;
						   e.setPrepOwner(preowner);
						   PrepInstruction prepinstruction=PrepInstruction.fromValue(instruction);
						   e.setPrepInstruction(prepinstruction);
						   prelist.add(e);
					   }
					   element.setPrepDetailsList(prelist);
				   }
			}else if(haspre) {
				  PrepDetails e=new PrepDetails();
				   PrepOwner preowner=PrepOwner.SELLER;
				   e.setPrepOwner(preowner);
				   PrepInstruction prepinstruction=PrepInstruction.NONE;
				   e.setPrepInstruction(prepinstruction);
				   PrepDetailsList prelist=new PrepDetailsList();
				   prelist.add(e);
				   element.setPrepDetailsList(prelist);
			}
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
				amazonAuthority.setApiRateLimit(null, e);
				throw AmzUtil.getException(e);
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
		   if(!"prod".equals(this.profile)){return shipment.getShipmentid();}
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
			if (shipment.getStatus() == 0||shipment.getStatus() == -1) {
				shipment.setShipmentstatus(ShipmentStatus.CANCELLED.getValue());
			} 
		    header.setShipmentStatus(ShipmentStatus.fromValue(shipment.getShipmentstatus()));
			body.setInboundShipmentHeader(header);
			body.setMarketplaceId(marketplace.getMarketplaceid());
			List<ShipInboundItem> itemlist = shipment.getItemList();
			InboundShipmentItemList items=new InboundShipmentItemList();
			Boolean haspre=false;
			for(int i=0;i<itemlist.size();i++) {
				ShipInboundItem itemsku = itemlist.get(i);
				if(StrUtil.isNotBlank(itemsku.getPrepInstruction())) {
					haspre=true;break;
				}
			}
		    for(int i=0;i<itemlist.size();i++) {
		    	ShipInboundItem itemsku = itemlist.get(i);
				InboundShipmentItem element=new InboundShipmentItem();
				element.sellerSKU(itemsku.getSellersku());
				element.setShipmentId(shipment.getShipmentid());
				element.setQuantityShipped(itemsku.getQuantityshipped());
				element.setFulfillmentNetworkSKU(itemsku.getFulfillmentnetworksku());
				if(itemsku.getQuantityincase()!=null) {
					element.setQuantityInCase(itemsku.getQuantityincase());
				}
				if(StrUtil.isNotBlank(itemsku.getPrepInstruction())) {
					   String[] oldPrelist = itemsku.getPrepInstruction().split(",");
					   if(oldPrelist!=null&&oldPrelist.length>0) {
						   PrepDetailsList prelist=new PrepDetailsList();
						   for(String instruction:oldPrelist) {
							   PrepDetails e=new PrepDetails();
							   PrepOwner preowner=PrepOwner.SELLER;
							   e.setPrepOwner(preowner);
							   PrepInstruction prepinstruction=PrepInstruction.fromValue(instruction);
							   e.setPrepInstruction(prepinstruction);
							   prelist.add(e);
						   }
						   element.setPrepDetailsList(prelist);
					   }
				}else if(haspre) {
					  PrepDetails e=new PrepDetails();
					   PrepOwner preowner=PrepOwner.SELLER;
					   e.setPrepOwner(preowner);
					   PrepInstruction prepinstruction=PrepInstruction.NONE;
					   e.setPrepInstruction(prepinstruction);
					   PrepDetailsList prelist=new PrepDetailsList();
					   prelist.add(e);
					   element.setPrepDetailsList(prelist);
				}
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
				if(e!=null&&e.getResponseBody()!=null&&e.getResponseBody().contains("INVALID_PREP")) {
					throw new BizException("亚马逊预备信息错误，请重新查看预备信息");
				}
			    throw AmzUtil.getException(e);
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
	    	   List<String> shipmentStatusList=new ArrayList<String>();
			   shipmentStatusList.add("CLOSED");
			   shipmentStatusList.add("SHIPPED");
			   shipmentStatusList.add("RECEIVING");
			   shipmentStatusList.add("IN_TRANSIT");
			   shipmentStatusList.add("DELIVERED");
			   shipmentStatusList.add("CHECKED_IN");
			   shipmentStatusList.add("WORKING");
			   shipmentStatusList.add("READY_TO_SHIP");
			   GetShipmentsResponse response = api.getShipments("DATE_RANGE", market.getMarketplaceid(), shipmentStatusList, null,
					   AmzDateUtils.getOffsetDateTimeUTC(cstart), AmzDateUtils.getOffsetDateTimeUTC(cend), null);
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
			 throw AmzUtil.getException(e);
		}
	}
	
	
	
	public InboundShipmentList listShipment(AmazonAuthority auth,Marketplace market,List<String> shipmentIdsList){
		 FbaInboundApi api = apiBuildService.getInboundApi(auth);
	     try {
			   GetShipmentsResponse response = api.getShipments("SHIPMENT", market.getMarketplaceid(), null, shipmentIdsList,
					   null, null, null);
			   if(response==null)return null;
			   GetShipmentsResult result = response.getPayload();
			   if(result==null)return null;
			   InboundShipmentList data = result.getShipmentData();
			  return data;
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
	}

	@Override
	public String putTransportDetailsRequest(AmazonAuthority amazonAuthority, Marketplace marketplace,ShipInboundPlan inboundplan ,
			ShipInboundShipment shipment) {
		 // TODO Auto-generated method stub
		 if(!"prod".equals(this.profile)){return  null;}
		 FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		 PutTransportDetailsRequest body=new PutTransportDetailsRequest();
		 body.setIsPartnered(false);
		 String shiptype=shipment.getTranstyle();
		 if(StrUtil.isEmpty(shiptype)) {
			 shiptype=ShipmentType.SP.getValue();
		 }
		 body.setShipmentType(ShipmentType.fromValue(shiptype));
		 TransportDetailInput detail=new TransportDetailInput();
		 if(ShipmentType.LTL.getValue().equals(shipment.getTranstyle())) {
			 NonPartneredLtlDataInput nonpart=new NonPartneredLtlDataInput();
			 List<ShipInboundBox> boxs = shipInboundBoxService.findListByShipmentId(shipment.getShipmentid());
			 for(ShipInboundBox box:boxs) {
                  if(StrUtil.isNotBlank(box.getTrackingId())) {
                	 nonpart.setCarrierName(shipment.getCarrier());
     				 nonpart.setProNumber(box.getTrackingId());
     				 break;
                  }
			 }
			 if(nonpart.getProNumber()==null) {
				 nonpart.setCarrierName(shipment.getCarrier());
				 nonpart.setProNumber(shipment.getShipmentid()+"U000001");
			 }
			detail.setNonPartneredLtlData(nonpart);
		 }else {
			 NonPartneredSmallParcelDataInput nonpart=new NonPartneredSmallParcelDataInput();
			 nonpart.setCarrierName(shipment.getCarrier());
			 NonPartneredSmallParcelPackageInputList pkglist=new NonPartneredSmallParcelPackageInputList();
			 List<ShipInboundBox> boxs = shipInboundBoxService.findListByShipmentId(shipment.getShipmentid());
			 int i=1;
			 for(ShipInboundBox box:boxs) {
                	  NonPartneredSmallParcelPackageInput element=new NonPartneredSmallParcelPackageInput();
                	  if(StrUtil.isNotBlank(box.getTrackingId())) {
                		  element.setTrackingId(box.getTrackingId());
                	  }else {
                		  element.setTrackingId(shipment.getShipmentid()+"U"+String.format("%06d", i));
                	  }
     				  pkglist.add(element);
     				  i++;
			 }
			 if(pkglist.size()==0) {
				 NonPartneredSmallParcelPackageInput element=new NonPartneredSmallParcelPackageInput();
				 element.setTrackingId(shipment.getShipmentid()+"U000001");
				 pkglist.add(element);
			 }
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
			throw AmzUtil.getException(e);
		}
	}
	
	@Override
	public String estimateTransportDetailsRequest(AmazonAuthority amazonAuthority, 
			Marketplace marketplace,
			ShipInboundPlan inboundplan ,
			ShipInboundShipment shipment) {
		 // TODO Auto-generated method stub
		 if(!"prod".equals(this.profile)){return  null;}
		 FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		 try {
			EstimateTransportResponse response = api.estimateTransport(shipment.getShipmentid());
			CommonTransportResult result = response.getPayload();
			shipment.setTransportStatus(result.getTransportResult().getTransportStatus().getValue());
			return result.getTransportResult().getTransportStatus().getValue();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
	}
	
	@Override
	public String confirmTransportDetailsRequest(AmazonAuthority amazonAuthority, 
			Marketplace marketplace,
			ShipInboundPlan inboundplan ,
			ShipInboundShipment shipment) {
		 // TODO Auto-generated method stub
		 if(!"prod".equals(this.profile)){return  null;}
		 FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		 try {
			ConfirmTransportResponse  response = api.confirmTransport(shipment.getShipmentid());
			CommonTransportResult result = response.getPayload();
			shipment.setTransportStatus(result.getTransportResult().getTransportStatus().getValue());
			return result.getTransportResult().getTransportStatus().getValue();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
	}
	
	public GetTransportDetailsResponse getTransportDetails(AmazonAuthority amazonAuthority,String shipmentId) {
		 // TODO Auto-generated method stub
		 FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		 GetTransportDetailsResponse response;
			try {
				response = api.getTransportDetails(shipmentId);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw AmzUtil.getException(e);
			}
		  return response;
	}
	
	public String getLabelDownloadUR(AmazonAuthority amazonAuthority,ShipInboundShipment shipment,String pagetype,String labelType,String pannum){
		 FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		 //labelType="SELLER_LABEL";
		 try {
			List<String> cartonIdList=new LinkedList<String>();
			cartonIdList.add("1");
//			for(int i=1;i<=shipment.getBoxnum();i++) {
//				cartonIdList.add(String.format("%d", i));
//			}
			//cartonIdList.add(shipment.getShipmentid()+"U"+String.format("%06d", i));
			GetLabelsResponse response =null;
			if("PALLET".equals(labelType)) {
				Integer pansize=1;
				if(StrUtil.isNotEmpty(pannum)) {
					pansize=Integer.parseInt(pannum);
				}
				response=api.getLabels(shipment.getShipmentid(),pagetype , labelType, shipment.getBoxnum(), null, pansize, shipment.getBoxnum(), null);
			}else {
				response=api.getLabels(shipment.getShipmentid(),pagetype , labelType, shipment.getBoxnum(), null, null, shipment.getBoxnum(), null);
			}
			LabelDownloadURL url = response.getPayload();
			return url.getDownloadURL();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
		 
	}
	
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		List<Marketplace> awsregion = marketplaceService.findbyauth(amazonAuthority.getId());
		for(Marketplace market:awsregion) {
			try {
				List<Map<String, Object>> plist = shipInboundShipmentMapper.findProblemShip(amazonAuthority.getGroupid(), market.getMarketplaceid());
				if(plist!=null&&plist.size()>1) {
					List<String> shipmentidList=new ArrayList<String>();
					plist.stream().forEach(item->{
			    		if(item!=null&&item.get("ShipmentId")!=null) {
			    			shipmentidList.add(item.get("ShipmentId").toString());
			    		}
			    	});
					listShipmentAsync(amazonAuthority,market,shipmentidList);
				}else {
					List<Map<String, Object>> list = shipInboundShipmentMapper.findAllShiped(amazonAuthority.getGroupid(), market.getMarketplaceid());
				    if(list!=null&&list.size()>0) {
				    	List<String> shipmentidList=new ArrayList<String>();
				    	list.stream().forEach(item->{
				    		if(item!=null&&item.get("ShipmentId")!=null) {
				    			shipmentidList.add(item.get("ShipmentId").toString());
				    		}
				    	});
				    	listShipmentAsync(amazonAuthority,market,shipmentidList);
				    }
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			listShipmentAsync(amazonAuthority,market);//屏蔽此功能因为时常发生子SKU没有同步的情况
			listShipmentItemsAsync(amazonAuthority,market);
		}
	}
	
	public List<ShipInboundItem> syncItemsByShipmentId(AmazonAuthority amazonAuthority, Marketplace market,ShipInboundShipment shipment) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("getShipmentItemsByShipmentId");
		FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		  try {
			 GetShipmentItemsResponse response = api.getShipmentItemsByShipmentId(shipment.getShipmentid(),market.getMarketplaceid());
			 if(response!=null&&response.getPayload()!=null) {
				 GetShipmentItemsResult shipmentItemsResult = response.getPayload();
				 return handlerItemResultData(amazonAuthority,shipmentItemsResult,shipment,true);
			 }
		  } catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				amazonAuthority.setApiRateLimit( null, e);
				 
			}
		  return null;
	}

	public List<ShipInboundItem> getUnSyncItemsByShipmentId(AmazonAuthority amazonAuthority, Marketplace market,ShipInboundShipment shipment) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("getShipmentItemsByShipmentId");
		FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		  try {
			 GetShipmentItemsResponse response = api.getShipmentItemsByShipmentId(shipment.getShipmentid(),shipment.getInboundplan().getMarketplaceid());
			 if(response!=null&&response.getPayload()!=null) {
				 GetShipmentItemsResult shipmentItemsResult = response.getPayload();
				 return getItemResultData(amazonAuthority,shipmentItemsResult,shipment);
			 }
		  } catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				amazonAuthority.setApiRateLimit(null, e);
				 
			}
		  return null;
	}
	
	public void listShipmentAsync(AmazonAuthority auth,Marketplace market){
		 auth.setUseApi("getShipments");
		 FbaInboundApi api = apiBuildService.getInboundApi(auth);
	     try {
	    	   Calendar cstart=Calendar.getInstance();
	    	   cstart.add(Calendar.DATE, -3);
	    	   Calendar cend=Calendar.getInstance();
			   List<String> shipmentStatusList=new ArrayList<String>();
			   shipmentStatusList.add("CLOSED");
			   shipmentStatusList.add("SHIPPED");
			   shipmentStatusList.add("RECEIVING");
			   shipmentStatusList.add("IN_TRANSIT");
			   shipmentStatusList.add("DELIVERED");
			   shipmentStatusList.add("CHECKED_IN");
			   shipmentStatusList.add("WORKING");
			   ApiCallback<GetShipmentsResponse> callback=new ApiCallbackGetShipments(this,auth,market);
			   api.getShipmentsAsync("DATE_RANGE", market.getMarketplaceid(), shipmentStatusList, null,
					   AmzDateUtils.getOffsetDateTimeUTC(cstart), AmzDateUtils.getOffsetDateTimeUTC(cend), null,callback);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			auth.setApiRateLimit(null, e);
		}
	}
	
	private void listShipmentItemsAsync(AmazonAuthority amazonAuthority, Marketplace market) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("getShipmentItems");
		FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		  try {
	    	 Calendar cstart=Calendar.getInstance();
	    	 cstart.add(Calendar.DATE, -3);
	    	 Calendar cend=Calendar.getInstance();
			 ApiCallback<GetShipmentItemsResponse> callback=new ApiCallbackGetShipmentItems(this,amazonAuthority,market,null);
			 api.getShipmentItemsAsync("DATE_RANGE", market.getMarketplaceid(),
					   AmzDateUtils.getOffsetDateTimeUTC(cstart), AmzDateUtils.getOffsetDateTimeUTC(cend),null, callback);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				amazonAuthority.setApiRateLimit(null, e);
				 
			}
	}

	private void getItemsByShipmentIdAsync(AmazonAuthority amazonAuthority, Marketplace market,ShipInboundShipment shipment) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("getShipmentItems");
		FbaInboundApi api = apiBuildService.getInboundApi(amazonAuthority);
		  try {
			 ApiCallback<GetShipmentItemsResponse> callback=new ApiCallbackGetShipmentItems(this,amazonAuthority,market,shipment);
			api.getShipmentItemsByShipmentIdAsync(shipment.getShipmentid(),market.getMarketplaceid(),callback);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				amazonAuthority.setApiRateLimit( null, e);
				 
			}
	}
 
	
	public void listShipmentAsync(AmazonAuthority auth,Marketplace market,List<String> shipmentIdList){
		 auth.setUseApi("getShipments");
		 FbaInboundApi api = apiBuildService.getInboundApi(auth);
	     try {
    		 ApiCallback<GetShipmentsResponse> callback=new ApiCallbackGetShipments(this,auth,market);
             api.getShipmentsAsync("SHIPMENT", market.getMarketplaceid(), null, shipmentIdList,
	    				 null, null, null,callback);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			auth.setApiRateLimit(null, e);
		}
	}

	@Override
	public void handlerResult(AmazonAuthority auth, Marketplace market, GetShipmentsResponse result) {
		// TODO Auto-generated method stub
		if(result==null||result.getPayload()==null)return ;
		GetShipmentsResult shipmentsResult = result.getPayload();
		InboundShipmentList data = shipmentsResult.getShipmentData();
		for (InboundShipmentInfo item:data) {
			String shipmentid = item.getShipmentId();
			 ShipInboundShipment shipment = shipInboundShipmentMapper.selectById(shipmentid);
			 if(shipment!=null) {
				 if(item.getShipmentStatus()!=null) {
					 shipment.setShipmentstatus(item.getShipmentStatus().getValue());
				 }
				  BoxContentsFeeDetails boxfee = item.getEstimatedBoxContentsFee();
				if (boxfee != null) {
					if(boxfee.getFeePerUnit()!=null) {
						shipment.setFeeperunit(boxfee.getFeePerUnit().getValue());
					}
					if(boxfee.getTotalFee()!=null) {
						shipment.setTotalfee(boxfee.getTotalFee().getValue());
						if(boxfee.getTotalFee().getCurrencyCode()!=null) {
							shipment.setCurrency(boxfee.getTotalFee().getCurrencyCode().getValue());
						}
					}									
					shipment.setTotalunits(boxfee.getTotalUnits());
				}
				if (item.getShipmentStatus()!=null&&(shipment.getStatus() >= 5||shipment.getSyncInv()==1||shipment.getSyncInv()==2)) {
					if(shipment.getSyncInv()==1||shipment.getSyncInv()==2) {
						if ("CLOSED".equals(item.getShipmentStatus().getValue())) {
							if(shipment.getStatus()!=6) {
								shipInboundShipmentRecordService.saveRecord(shipment);
							}
							shipment.setStatus(6);
							shipment.setStatus6date(new Date());
						}else if ("WORKING".equals(item.getShipmentStatus().getValue())) {
							shipment.setStatus(4);
							shipment.setStatus4date(new Date());
						}else if ("CANCELLED".equals(item.getShipmentStatus().getValue())||
								"DELETED".equals(item.getShipmentStatus().getValue())) {
							shipment.setStatus(0);
							shipment.setStatus0date(new Date());
						}else if("IN_TRANSIT".equals(item.getShipmentStatus().getValue())&&shipment.getStatus()!=5){
							shipment.setStatus(5);
							if(shipment.getStatus5date()==null) {
								shipment.setStatus5date(new Date());
							}
							if(shipment.getShipedDate()==null) {
								shipment.setShipedDate(new Date());
							}
						}else if("RECEIVING".equals(item.getShipmentStatus().getValue())&&shipment.getStatus()!=5){
							shipment.setStatus(5);
							if(shipment.getStatus5date()==null) {
								shipment.setStatus5date(new Date());
							}
							if(shipment.getShipedDate()==null) {
								shipment.setShipedDate(new Date());
							}
						}else if("SHIPPED".equals(item.getShipmentStatus().getValue())&&shipment.getStatus()!=5){
							shipment.setStatus(5);
							if(shipment.getStatus5date()==null) {
								shipment.setStatus5date(new Date());
							}
							if(shipment.getShipedDate()==null) {
								shipment.setShipedDate(new Date());
							}
						}else  {
							if(shipment.getStatus5date()==null) {
								shipment.setStatus5date(new Date());
							}
							if(shipment.getShipedDate()==null) {
								shipment.setShipedDate(new Date());
							}
						}
					}else {
						if ("CLOSED".equals(item.getShipmentStatus().getValue())&&shipment.getStatus()==5) {
							if(shipment.getStatus()!=6) {
								shipInboundShipmentRecordService.saveRecord(shipment);
							}
							shipment.setStatus(6);
							shipment.setStatus6date(new Date());
						}
						if (!"CLOSED".equals(item.getShipmentStatus().getValue()) && shipment.getStatus() == 6) {
							shipment.setStatus(5);
							if(shipment.getStatus5date()==null) {
								shipment.setStatus5date(new Date());
							}
							if(shipment.getShipedDate()==null) {
								shipment.setShipedDate(new Date());
							}
						}
					}
				}
				shipment.setRefreshtime(new Date());
				shipInboundShipmentMapper.updateById(shipment);
				getItemsByShipmentIdAsync(auth,market,shipment);
			 }else {
				 IShipInboundPlanService shipInboundPlanService=SpringUtil.getBean("shipInboundPlanService");
				 shipment=shipInboundPlanService.createShipment(auth,market,item,null,null);
				 getItemsByShipmentIdAsync(auth,market,shipment);
			 }
		}
		
		if(shipmentsResult.getNextToken()!=null) {
			FbaInboundApi api = apiBuildService.getInboundApi(auth);
		    ApiCallback<GetShipmentsResponse> callback=new ApiCallbackGetShipments(this,auth,market);
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     api.getShipmentsAsync("NEXT_TOKEN", market.getMarketplaceid(), null, null, null, null, shipmentsResult.getNextToken(),callback);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	private List<ShipInboundItem> getItemResultData(AmazonAuthority auth,GetShipmentItemsResult itemResult, ShipInboundShipment param_shipment) {
	    String shipmentid=param_shipment.getShipmentid();
	    List<ShipInboundItem> myResult=new ArrayList<ShipInboundItem>();
		for (InboundShipmentItem  item:itemResult.getItemData()) {
			LambdaQueryWrapper<ShipInboundItem> query = new LambdaQueryWrapper<ShipInboundItem>()
					   .eq(ShipInboundItem::getShipmentid, item.getShipmentId())
					   .eq(ShipInboundItem::getSellersku, item.getSellerSKU());
			ShipInboundItem inbounditem = shipInboundItemMapper.selectOne(query);
		    if(inbounditem!=null) {
		    	String msku=iProductInfoService.getMSKU(auth.getId(), param_shipment.getInboundplan().getMarketplaceid(), item.getSellerSKU());
				try {
					if(msku==null) {
						msku=item.getSellerSKU();
					}
					Result<String> materialidResult = erpClientOneFeign.getIdBySku(auth.getShopId(),msku);
					if(Result.isSuccess(materialidResult)) {
						String materialid=materialidResult.getData();
						inbounditem.setMaterialid(materialid);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			 	inbounditem.setInboundplanid(param_shipment.getInboundplanid());
		    	inbounditem.setQuantityreceived(item.getQuantityReceived());
				shipInboundItemMapper.updateById(inbounditem);
				myResult.add(inbounditem);
		    }else if(param_shipment!=null)  {
		    	  inbounditem=new ShipInboundItem();
		    	  inbounditem.setShipmentid(shipmentid);
		    	  inbounditem.setFulfillmentnetworksku(item.getFulfillmentNetworkSKU());
		    	  inbounditem.setInboundplanid(param_shipment.getInboundplanid());
		    	  inbounditem.setSellersku(item.getSellerSKU());
		    	  inbounditem.setQuantity(item.getQuantityShipped());
		    	  inbounditem.setQuantityshipped(item.getQuantityShipped());
		    	  inbounditem.setQuantityincase(item.getQuantityInCase());
		    	  inbounditem.setQuantityreceived(item.getQuantityReceived());
					if(param_shipment!=null&&	 param_shipment.getInboundplan() !=null) {
						String msku=iProductInfoService.getMSKU(auth.getId(), param_shipment.getInboundplan().getMarketplaceid(), item.getSellerSKU());
						try {
							if(msku==null) {
								msku=item.getSellerSKU();
							}
							Result<String> materialidResult = erpClientOneFeign.getIdBySku(auth.getShopId(),msku);
							if(Result.isSuccess(materialidResult)) {
								String materialid=materialidResult.getData();
								inbounditem.setMaterialid(materialid);
							}
						}catch(Exception e) {
							e.printStackTrace();
						}
						
					}
					myResult.add(inbounditem);
					shipInboundItemMapper.insert(inbounditem);
		    }
			
		}
		return myResult;
}
	
	private List<ShipInboundItem> handlerItemResultData(AmazonAuthority auth,GetShipmentItemsResult itemResult, ShipInboundShipment param_shipment,boolean needshipqty) {
		    Boolean hasReceived=false;
		    Boolean errorReceived=false;
		    String shipmentid="";
		    List<ShipInboundItem> myResult=new ArrayList<ShipInboundItem>();
		    Set<String> hasSku=new HashSet<String>();
			for (InboundShipmentItem  item:itemResult.getItemData()) {
				LambdaQueryWrapper<ShipInboundItem> query = new LambdaQueryWrapper<ShipInboundItem>()
						   .eq(ShipInboundItem::getShipmentid, item.getShipmentId())
						   .eq(ShipInboundItem::getSellersku, item.getSellerSKU());
				ShipInboundItem inbounditem = shipInboundItemMapper.selectOne(query);
		    	hasSku.add(item.getSellerSKU());
			    if(inbounditem!=null) {
			    	inbounditem.setQuantityreceived(item.getQuantityReceived());
					if (item.getQuantityReceived() != null && item.getQuantityReceived() > 0) {
						hasReceived = true;
					}
					shipmentid= item.getShipmentId();
					int rec=0;
					if(item.getQuantityReceived()!=null) {
						rec=item.getQuantityReceived();
					}
					int shipped=item.getQuantityShipped();
					int amount = rec-shipped;
					
					if(errorReceived==true) {
						if (amount>=10 || amount<=-10) {
							errorReceived = false;
						}
					}
					inbounditem.setQuantityshipped(shipped);
					inbounditem.setShipmentid(shipmentid);
					if(param_shipment!=null&&param_shipment.getInboundplanid()!=null) {
						inbounditem.setInboundplanid(param_shipment.getInboundplanid());
					}
					shipInboundItemMapper.updateById(inbounditem);
					myResult.add(inbounditem);
			    }else if(param_shipment!=null)  {
			    	  inbounditem=new ShipInboundItem();
			    	  inbounditem.setShipmentid(shipmentid);
			    	  inbounditem.setFulfillmentnetworksku(item.getFulfillmentNetworkSKU());
			    	  if(param_shipment.getInboundplanid()!=null) {
						 inbounditem.setInboundplanid(param_shipment.getInboundplanid());
					  }
			    	  inbounditem.setSellersku(item.getSellerSKU());
			    	  inbounditem.setQuantity(item.getQuantityShipped());
			    	  inbounditem.setQuantityshipped(item.getQuantityShipped());
			    	  inbounditem.setQuantityincase(item.getQuantityInCase());
			    	  inbounditem.setQuantityreceived(item.getQuantityReceived());
						if (item.getQuantityReceived() != null && item.getQuantityReceived() > 0) {
							hasReceived = true;
						}
						shipmentid= item.getShipmentId();
						int rec=0;
						if(item.getQuantityReceived()!=null) {
							rec=item.getQuantityReceived();
						}
						int shipped=item.getQuantityShipped();
						int amount = rec-shipped;
						
						if(errorReceived==true) {
							if (amount>=10 || amount<=-10) {
								errorReceived = false;
							}
						}
						if(param_shipment!=null&&	 param_shipment.getInboundplan() !=null) {
							String msku=iProductInfoService.getMSKU(auth.getId(), param_shipment.getInboundplan().getMarketplaceid(), item.getSellerSKU());
							inbounditem.setMsku(msku);
							try {
								Result<String> materialidResult = erpClientOneFeign.getIdBySku(auth.getShopId(),msku);
								if(Result.isSuccess(materialidResult)) {
									String materialid=materialidResult.getData();
									inbounditem.setMaterialid(materialid);
								}
							}catch(Exception e) {
								e.printStackTrace();
							}
							
						}
						inbounditem.setShipmentid(shipmentid);
						shipInboundItemMapper.insert(inbounditem);
						myResult.add(inbounditem);
			    
			    }
				
			}
			if (StrUtil.isNotEmpty(shipmentid)) {
				 ShipInboundShipment shipment = shipInboundShipmentMapper.selectById(shipmentid);
				if (shipment!=null&&shipment.getStart_receive_date() == null && hasReceived) {
					shipment.setStart_receive_date(new Date());
					shipInboundShipmentMapper.updateById(shipment);
				}
				if(needshipqty) {
					LambdaQueryWrapper<ShipInboundItem> queryShipmentItem = new LambdaQueryWrapper<ShipInboundItem>()
							   .eq(ShipInboundItem::getShipmentid, shipmentid);
					List<ShipInboundItem> inbounditemList = shipInboundItemMapper.selectList(queryShipmentItem);
					for(ShipInboundItem item:inbounditemList) {
						if(hasSku.contains(item.getSellersku())) {
							continue;
						}else {
							LambdaQueryWrapper<ShipInboundItem> query = new LambdaQueryWrapper<ShipInboundItem>()
									   .eq(ShipInboundItem::getShipmentid, item.getShipmentid())
									   .eq(ShipInboundItem::getSellersku, item.getSellersku());
							item.setQuantityshipped(0);
							shipInboundItemMapper.update(item, query);
						}
					}
				}
			}
			return myResult;
	}
	
	@Override
	public void handlerItemResult(AmazonAuthority auth, Marketplace market, GetShipmentItemsResponse result, ShipInboundShipment param_shipment,boolean needshipqty) {
		// TODO Auto-generated method stub
		auth.setUseApi("getShipmentItems");
		GetShipmentItemsResult itemResult = result.getPayload();
		if(itemResult.getNextToken()!=null) {
			needshipqty=false;
		}
		handlerItemResultData(auth,itemResult,param_shipment,needshipqty);
		if(itemResult.getNextToken()!=null) {
			FbaInboundApi api = apiBuildService.getInboundApi(auth);
			 ApiCallback<GetShipmentItemsResponse> callback=new ApiCallbackGetShipmentItems(this,auth,market,param_shipment);
			try {
			     api.getShipmentItemsAsync("NEXT_TOKEN", market.getMarketplaceid(), null, null,  itemResult.getNextToken(),callback);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	@Override
	public List<InboundShipmentItem> requestInboundShipmentsItem(AmazonAuthority auth, String shipmentid, Marketplace marketPlace) {
		FbaInboundApi api = apiBuildService.getInboundApi(auth);
		List<InboundShipmentItem> list=null;
		try {
			GetShipmentItemsResponse response = api.getShipmentItemsByShipmentId(shipmentid, marketPlace.getMarketplaceid());
			if(response!=null && response.getPayload()!=null) {
				list=response.getPayload().getItemData();
			}
		} catch (ApiException e) {
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
		return list;
	}

	@Override
	public InboundShipmentInfo requestInboundShipments(AmazonAuthority auth, String shipmentid, Marketplace marketPlace) {
		FbaInboundApi api = apiBuildService.getInboundApi(auth);
		String querytype="SHIPMENT";
		GetShipmentsResponse response=null;
		try {
			List<String> shipmentIdList=new ArrayList<String>();
			shipmentIdList.add(shipmentid);
			response = api.getShipments(querytype, marketPlace.getMarketplaceid(),null,shipmentIdList,null,null,null);
		} catch (ApiException e) {
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
		if(response!=null && response.getPayload()!=null) {
			if(response.getPayload().getShipmentData()!=null&&response.getPayload().getShipmentData().size()>0) {
				InboundShipmentInfo info = response.getPayload().getShipmentData().get(0);
				return info;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}

	@Override
	public void checkCancel(AmazonAuthority auth,ShipInboundPlan plan,ShipInboundShipment shipment) {
		// TODO Auto-generated method stub
		Marketplace market = marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
		try {
			InboundShipmentList response = this.listShipment(auth, market, Arrays.asList(shipment.getShipmentid()));
			if(response!=null&&response.size()>0) {
				InboundShipmentInfo result = response.get(0);
				if(result.getShipmentId().equals(shipment.getShipmentid()) ) {
					if(shipment.getShipmentstatus().equals("CANCELLED")) {
						if( (!result.getShipmentStatus().equals(ShipmentStatus.CANCELLED))
								&&
							(!result.getShipmentStatus().equals(ShipmentStatus.DELETED))
							) {
							this.updateInboundShipment(auth, market, plan, shipment);
						}
					}
					else if(!shipment.getShipmentstatus().equals(result.getShipmentStatus().getValue())) {
						this.updateInboundShipment(auth, market, plan, shipment);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
