package com.wimoor.amazon.inboundV2.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.OffsetDateTime;

import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmTransportationOptionsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmTransportationOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ContactInformation;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateTransportationOptionsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateTransportationOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListTransportationOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ShipmentTransportationConfiguration;
import com.amazon.spapi.model.fulfillmentinboundV20240320.TransportationSelection;
import com.amazon.spapi.model.fulfillmentinboundV20240320.WindowInput;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransHisMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransMapper;
import com.wimoor.amazon.inbound.mapper.ShipTransDetailMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTransHis;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inbound.service.IShipInboundshipmentTraceuploadService;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundPlanPackingGroupItemMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundPlanPackingOptionsMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentCustomsMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentItemV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.dto.TransportationDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentCustoms;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentItem;
import com.wimoor.amazon.inboundV2.service.IInboundApiHandlerService;
import com.wimoor.amazon.inboundV2.service.IShipInboundCaseService;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.amazon.inboundV2.service.IShipInboundOperationService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentRecordV2Service;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.amazon.inboundV2.service.IShipInboundTransportationService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("shipInboundTransportationService")
@RequiredArgsConstructor
public class ShipInboundTransportationServiceImpl implements IShipInboundTransportationService {
	 @Lazy
	 @Autowired
	 IShipInboundCaseService shipInboundCaseV2Service;
	 @Autowired
	 IInboundApiHandlerService iInboundApiHandlerService;
	 @Autowired
	 @Lazy
     IShipInboundShipmentService shipInboundShipmentService;
	 @Autowired
	 @Lazy
	 IShipInboundItemService iShipInboundItemService;
	 @Autowired
	 @Lazy
	 IShipInboundPlanService iShipInboundPlanService;

	 @Autowired
	 IAmazonAuthorityService amazonAuthorityService;

	 @Autowired
	 IShipInboundOperationService iShipInboundOperationService;
	 @Autowired
	 ShipInboundPlanPackingOptionsMapper shipInboundPlanPackingOptionsMapper;
	 @Autowired
	 ShipInboundPlanPackingGroupItemMapper ShipInboundPlanPackingGroupItemMapper;
	 
	 @Autowired
	 IShipAddressService shipAddressService;
		
	 final ShipInboundShipmentItemV2Mapper shipInboundShipmentItemV2Mapper;
		 
	final ShipInboundTransMapper shipInboundTransMapper;
	final ShipTransDetailMapper shipTransDetailMapper; 
	
	final IShipInboundTransService shipInboundTransService;
	final IMarketplaceService iMarketplaceService;
	
	final ShipInboundTransHisMapper shipInboundTransHisMapper;
	final IShipInboundshipmentTraceuploadService iShipInboundshipmentTraceuploadService;
	@Resource
	private IShipInboundItemService shipInboundItemV2Service;
	final ShipInboundShipmentCustomsMapper shipInboundShipmentCustomsMapper;
	final IShipInboundShipmentRecordV2Service shipInboundV2ShipmentRecordService;
	@Override
	public ShipInboundOperation generateTransportationOptions(ShipInboundPlan plan) {
		// TODO Auto-generated method stub
		GenerateTransportationOptionsRequest request=new GenerateTransportationOptionsRequest();
		request.setPlacementOptionId(plan.getPlacementOptionId());
		List<ShipmentTransportationConfiguration> list=new LinkedList<ShipmentTransportationConfiguration>();
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ShipInboundOperation transportation = iShipInboundOperationService.getOperation(auth, plan.getId(), "confirmTransportationOptions");
		if(transportation!=null&&transportation.getOperationStatus().equals("SUCCESS")) {
			ShipInboundOperation old = iShipInboundOperationService.getOperation(auth, plan.getId(), "generateTransportationOptions");
			return old;
		}
		ShipAddress address = shipAddressService.getById(plan.getSourceAddress());
		for(String shipmentid:plan.getShipmentids()) {
			ShipmentTransportationConfiguration config=new ShipmentTransportationConfiguration();
			config.setShipmentId(shipmentid);
			WindowInput readyToShipWindow=new WindowInput();
			if(plan.getShippingDate()==null) {
				Calendar c=Calendar.getInstance(); 
				c.add(Calendar.DATE, 1);
				plan.setShippingDate(c.getTime());
			}
			OffsetDateTime date = AmzDateUtils.getOffsetDateTimeWithoutHour(plan.getShippingDate());
			readyToShipWindow.setStart(date);
			config.setReadyToShipWindow(readyToShipWindow);
			ContactInformation contact=new ContactInformation();
			contact.setEmail(address.getEmail());
			contact.setName(address.getContact());
			contact.setPhoneNumber(address.getPhone());
			config.setContactInformation(contact);
//			FreightInformation freightClass=new FreightInformation();
//			freightClass.setFreightClass("NONE");
//			Currency declaredValue=new Currency();
//			declaredValue.setAmount(new BigDecimal("0"));
//			Marketplace market = iMarketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
//			declaredValue.setCode(market.getCurrency());
//			freightClass.setDeclaredValue(declaredValue);
//			config.setFreightInformation(freightClass);
//			List<PalletInput> pallets=new LinkedList<PalletInput>();
//			PalletInput e=new PalletInput();
//			Dimensions dim=new Dimensions();
//			dim.setHeight(new BigDecimal("0"));
//			dim.setWidth(new BigDecimal("0"));
//			dim.setLength(new BigDecimal("0"));
//			dim.setUnitOfMeasurement(UnitOfMeasurement.CM);
//			e.setQuantity(1);
//			e.setStackability(Stackability.STACKABLE);
//			Weight weight=new Weight();
//			weight.setValue(new BigDecimal("0"));
//			weight.setUnit(UnitOfWeight.KG);
//			e.setWeight(weight);
//			e.setDimensions(dim);
//			pallets.add(e);
//			config.setPallets(pallets);
			list.add(config);
		}
		request.setShipmentTransportationConfigurations(list);
		GenerateTransportationOptionsResponse response = iInboundApiHandlerService.generateTransportationOptions(auth, plan.getInboundPlanId(), request);
		if(response!=null) {
			return iShipInboundOperationService.setOperationID(auth, plan.getId(), response.getOperationId());
		}else {
			return null;
		}
	}
 

	@Override
	public ListTransportationOptionsResponse listTransportationOptions(TransportationDTO dto) {
		// TODO Auto-generated method stub
		ShipInboundPlan plan = iShipInboundPlanService.getById(dto.getFormid());
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ListTransportationOptionsResponse response = iInboundApiHandlerService.listTransportationOptions(auth, plan.getInboundPlanId(), 
				dto.getPlacementOptionId(),dto.getShipmentid(),dto.getPageSize(),dto.getPaginationToken());
		return response;
	}


	@Override
	public ShipInboundOperation confirmTransportationOptions(String formid,List<TransportationSelection> transportationSelections) {
		// TODO Auto-generated method stub
		ShipInboundPlan plan = iShipInboundPlanService.getById(formid);
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ShipInboundOperation operation = iShipInboundOperationService.getOperation(auth, plan.getId(), "confirmTransportationOptions");
		if(operation!=null&&operation.getOperationStatus().equals("SUCCESS")) {
			return operation;
		}
		ConfirmTransportationOptionsRequest body=new ConfirmTransportationOptionsRequest();
		List<TransportationSelection> translist=new ArrayList<TransportationSelection>();
		ShipAddress address = shipAddressService.getById(plan.getSourceAddress());
		ContactInformation contact=new ContactInformation();
		contact.setEmail(address.getEmail());
		contact.setName(address.getContact());
		contact.setPhoneNumber(address.getPhone());
		for(TransportationSelection item:transportationSelections) {
			item.setContactInformation(contact);
			translist.add(item);
		}
		
		body.setTransportationSelections(translist);
		ConfirmTransportationOptionsResponse response = iInboundApiHandlerService.confirmTransportationOptions(auth, plan.getInboundPlanId(), body);
		if(response!=null) {
			return iShipInboundOperationService.setOperationID(auth, plan.getId(), response.getOperationId());
		}else {
			return null;
		}
	}


	@Override
	@Transactional
	public void saveSelfTransData(UserInfo user, ShipInboundTrans ship, Date shipsdate) {
		ShipInboundShipment shipment = shipInboundShipmentService.getById(ship.getShipmentid());
		QueryWrapper<ShipInboundTrans> query=new QueryWrapper<ShipInboundTrans>();
		query.eq("shipmentid", shipment.getShipmentConfirmationId());
		List<ShipInboundTrans> list = shipInboundTransMapper.selectList(query);
		if(StrUtil.isBlank(ship.getCompany())) {
			ship.setCompany(null);
		}
		if(StrUtil.isBlank(ship.getChannel())) {
			ship.setChannel(null);
		}
		ship.setShipmentid(shipment.getShipmentConfirmationId());
		if (list.size() > 0) {
			ShipInboundTrans item = list.get(0);
			ship.setId(item.getId());
			shipInboundTransService.updateById(ship);
			shipInboundTransHisMapper.insert(new ShipInboundTransHis(ship));
		} else {
			shipInboundTransService.save(ship);
			shipInboundTransHisMapper.insert(new ShipInboundTransHis(ship));
		}
		//trans费用分摊
		saveShipmentsFee(shipment,ship,user);
	}
	
	public void saveShipmentsFee(ShipInboundShipment shipment,ShipInboundTrans transfee,UserInfo user) {
		//获取分摊费用 存起来
		  List<Map<String, Object>> itemlist = shipInboundShipmentItemV2Mapper.selectByShipmentid(shipment.getShipmentid());
		  BigDecimal totalWeight=new BigDecimal("0");
		  BigDecimal totalFee=new BigDecimal("0");
		  for(Map<String,Object> item:itemlist) {
			  BigDecimal weight =item.get("weight")!=null?new BigDecimal(item.get("weight").toString()):new BigDecimal("0");
			  BigDecimal volume =item.get("volume")!=null?new BigDecimal(item.get("volume").toString()):new BigDecimal("0");
			  BigDecimal maxWeight=weight.compareTo(volume)>0?weight:volume;
			  item.put("maxWeight", maxWeight);
			  totalWeight=totalWeight.add(maxWeight); 
		  }
		  if(transfee.getOtherfee()!=null) {
			  totalFee=totalFee.add(transfee.getOtherfee());
		  }
		  if(transfee.getSingleprice()!=null&&transfee.getTransweight()!=null) {
			  totalFee=totalFee.add(transfee.getSingleprice().multiply(transfee.getTransweight()));
		  }
		  int i=0;
		  BigDecimal subPrecent=new BigDecimal("1");
		  for(Map<String,Object> feeitem:itemlist) {
			  BigDecimal maxWeight=feeitem.get("maxWeight")!=null?new BigDecimal(feeitem.get("maxWeight").toString()):new BigDecimal("0");
			  BigDecimal price=feeitem.get("price")!=null?new BigDecimal(feeitem.get("price").toString()):new BigDecimal("0");
			  BigDecimal priceWavg=feeitem.get("price_wavg")!=null?new BigDecimal(feeitem.get("price_wavg").toString()):price;
			  BigDecimal precent = maxWeight.divide(totalWeight,2,RoundingMode.FLOOR) ;
			  subPrecent=subPrecent.subtract(precent);
			  i++;
			  if(i==itemlist.size()&&subPrecent.floatValue()>0.000001) {
				  precent=subPrecent;
			  }
			  BigDecimal itemfee=  totalFee.multiply(precent);
			  ShipInboundShipmentItem item = shipInboundShipmentItemV2Mapper.selectById(feeitem.get("id").toString());
			  item.setUnittransfee(itemfee.divide(new BigDecimal(item.getQuantity()),2,RoundingMode.FLOOR));
			  item.setTotaltransfee(itemfee);
			  item.setUnitcost(priceWavg);
			  item.setTotalcost(priceWavg.multiply(new BigDecimal(item.getQuantity())));
			  shipInboundShipmentItemV2Mapper.updateById(item);
				
		}
	}


	@Override
	public List<Map<String, Object>> getSelfTransHis(String shopid, String shipmentid) {
		ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		return shipInboundTransHisMapper.getSelfTransHis(shopid,  shipment.getShipmentConfirmationId());
	}


	@Override
	public List<ShipInboundShipmentCustoms> getCustoms(String shipmentid) {
		// TODO Auto-generated method stub
		return shipInboundShipmentCustomsMapper.listByShipmentid(shipmentid);
	}


	@Override
	public void saveCustoms(UserInfo user,List<ShipInboundShipmentCustoms> list) {
		// TODO Auto-generated method stub
		String itemid=null;
		for(ShipInboundShipmentCustoms item:list) {
			ShipInboundShipmentCustoms old=shipInboundShipmentCustomsMapper.selectById(item.getItemid());
			if(old!=null) {
				item.setOperator(user.getId());
				item.setOpttime(new Date());
				shipInboundShipmentCustomsMapper.updateById(item);
			}else {
				item.setCreator(user.getId());
				item.setCreatetime(new Date());
				item.setOperator(user.getId());
				item.setOpttime(new Date());
				shipInboundShipmentCustomsMapper.insert(item);
			}
			if(itemid==null) {
				itemid=item.getItemid();
			}
		}
		if(itemid!=null) {
			ShipInboundShipmentItem item = shipInboundShipmentItemV2Mapper.selectById(itemid);
			if(item!=null) {
				ShipInboundShipment shipment = shipInboundShipmentService.getById(item.getShipmentid());
				if(shipment!=null&&shipment.getStatus()==6) {
					shipment.setOperator(user.getId());
					shipment.setOpttime(new Date());
					shipment.setStatus(7);
					shipInboundShipmentService.updateById(shipment);
					shipInboundV2ShipmentRecordService.saveRecord(shipment);
				}
			}
			
		}
		
		
	}
	
 

}
