package com.wimoor.amazon.inboundV2.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.annotation.Resource;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.amazon.spapi.model.fulfillmentinboundV20240320.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.inboundV2.mapper.*;
import com.wimoor.amazon.inboundV2.pojo.entity.*;
import com.wimoor.common.mvc.BizException;
import com.wimoor.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.OffsetDateTime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransHisMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTransHis;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inbound.service.IShipInboundshipmentTraceuploadService;
import com.wimoor.amazon.inboundV2.pojo.dto.TransportationDTO;
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

	final IShipInboundTransService shipInboundTransService;
	final IMarketplaceService iMarketplaceService;
	
	final ShipInboundTransHisMapper shipInboundTransHisMapper;
	final IShipInboundshipmentTraceuploadService iShipInboundshipmentTraceuploadService;
	@Resource
	private IShipInboundItemService shipInboundItemV2Service;
	final ShipInboundShipmentCustomsMapper shipInboundShipmentCustomsMapper;
	final IShipInboundShipmentRecordV2Service shipInboundV2ShipmentRecordService;
	final ShipInboundPlanTransportationOptionsMapper shipInboundPlanTransportationOptionsMapper;
	final ErpClientOneFeignManager erpClientOneFeignManager;
	@Override
	public ShipInboundOperation generateTransportationOptions(ShipInboundPlan plan) {
		// TODO Auto-generated method stub
		shipInboundPlanTransportationOptionsMapper.delete(new LambdaQueryWrapper<ShipInboundPlanTransportationOptions>().eq(ShipInboundPlanTransportationOptions::getFormid,plan.getId()));
		GenerateTransportationOptionsRequest request=new GenerateTransportationOptionsRequest();
		request.setPlacementOptionId(plan.getPlacementOptionId());
		List<ShipmentTransportationConfiguration> list=new LinkedList<ShipmentTransportationConfiguration>();
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ShipInboundOperation transportation = iShipInboundOperationService.getOperation(auth, plan.getId(), "confirmTransportationOptions");
		if(transportation!=null&&transportation.isRunOrSucces()) {
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
		LambdaQueryWrapper<ShipInboundPlanTransportationOptions> query=new LambdaQueryWrapper<ShipInboundPlanTransportationOptions>();
		query.eq(ShipInboundPlanTransportationOptions::getFormid, plan.getId());
		shipInboundPlanTransportationOptionsMapper.delete(query);
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
		ListTransportationOptionsResponse response =null;
	    if(dto.getPaginationToken()==null&&plan.getTransportationToken()==null){
			LambdaQueryWrapper<ShipInboundPlanTransportationOptions> query=new LambdaQueryWrapper<ShipInboundPlanTransportationOptions>();
			query.eq(ShipInboundPlanTransportationOptions::getFormid, plan.getId());
			List<ShipInboundPlanTransportationOptions> translist=shipInboundPlanTransportationOptionsMapper.selectList(query);
			if(translist!=null&&translist.size()>0){
				response = new ListTransportationOptionsResponse();
				for(ShipInboundPlanTransportationOptions item:translist){
					response.addTransportationOptionsItem(JSONUtil.toBean(item.getContents(), TransportationOption.class, true));
				}
				return response;
			}
		}
		try{
			if(plan.getTransportationToken()!=null&&dto.getPaginationToken()==null){
				dto.setPaginationToken(plan.getTransportationToken());
			}
			response = iInboundApiHandlerService.listTransportationOptions(auth,
					                                                       plan.getInboundPlanId(),
					                                                       dto.getPlacementOptionId(),
					                                                       dto.getShipmentid(),
					                                                       dto.getPageSize(),
					                                                       dto.getPaginationToken());
			if(response==null){
				throw new BizException("无法获取到物流信息");
			}
			for(TransportationOption option:response.getTransportationOptions()){
				ShipInboundPlanTransportationOptions o=new ShipInboundPlanTransportationOptions();
				o.setContents(JSONUtil.toJsonStr(option));
				o.setShipmentid(option.getShipmentId());
				o.setFormid(plan.getId());
				o.setOpttime(new Date());
				o.setTransportationOptionId(option.getTransportationOptionId());
				o.setPlacementOptionId(plan.getPlacementOptionId());
				shipInboundPlanTransportationOptionsMapper.insert(o);
			}
			plan.setTransportationToken(response!=null&&response.getPagination()!=null?response.getPagination().getNextToken():null);
			iShipInboundPlanService.lambdaUpdate()
					               .eq(ShipInboundPlan::getId, plan.getId())
					               .set(ShipInboundPlan::getTransportationToken,plan.getTransportationToken())
					               .update();

			return response;
		}catch(Exception e) {
			if(e instanceof BizException){
				BizException bize=(BizException)e;
				if(bize.getMessage().contains("There is an issue with the input")||
						bize.getMessage().contains("Unable to parse the provided paginationToken")){
					shipInboundPlanTransportationOptionsMapper.delete(new LambdaQueryWrapper<ShipInboundPlanTransportationOptions>().eq(ShipInboundPlanTransportationOptions::getFormid,plan.getId()));
					if(plan.getTransportationToken()!=null){
						iShipInboundPlanService.lambdaUpdate()
								               .eq(ShipInboundPlan::getId, plan.getId())
								               .set(ShipInboundPlan::getTransportationToken,null)
								               .update();
						dto.setPaginationToken(null);
						return listTransportationOptions(dto);
					}
				}
			}
			ShipInboundOperation operation = iShipInboundOperationService.getOperation(auth, plan.getId(), "confirmTransportationOptions");
			if (operation != null && operation.getOperationStatus().equals("SUCCESS") && plan.getAuditstatus() >= 7) {
				LambdaQueryWrapper<ShipInboundShipment> query = new LambdaQueryWrapper<ShipInboundShipment>();
				query.eq(ShipInboundShipment::getFormid, plan.getId());
				List<ShipInboundShipment> shipmentlist = shipInboundShipmentService.list(query);
				if (shipmentlist != null && shipmentlist.size() > 0) {
					response = new ListTransportationOptionsResponse();
					for (ShipInboundShipment item : shipmentlist) {
						String trans = item.getTransactionName();
						TransportationOption optitem = new TransportationOption();
						optitem.setTransportationOptionId(item.getTransportationOptionId());
						optitem.setShipmentId(item.getShipmentid());
						if (trans != null && trans.contains("---")) {
							String[] tarray = trans.split("---");
							optitem.setShippingMode(tarray[0]);
							Carrier carrer = new Carrier();
							carrer.setName(tarray[1]);
							optitem.setCarrier(carrer);
						}
						response.addTransportationOptionsItem(optitem);
					}
					Pagination pagination = new Pagination();
					pagination.setNextToken(null);
					response.setPagination(pagination);
				}
				return response;
			} else {
				shipInboundPlanTransportationOptionsMapper.delete(new LambdaQueryWrapper<ShipInboundPlanTransportationOptions>().eq(ShipInboundPlanTransportationOptions::getFormid,plan.getId()));
				throw e;
			}
		}

	}


	@Override
	public ShipInboundOperation confirmTransportationOptions(String formid,List<TransportationDTO> transportationSelections) {
		// TODO Auto-generated method stub
		ShipInboundPlan plan = iShipInboundPlanService.getById(formid);
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());

		ShipInboundOperation operation = iShipInboundOperationService.getOperation(auth, plan.getId(), "confirmTransportationOptions");
		if(operation!=null&&operation.isRunOrSucces()) {
			return operation;
		}
		try {
			ConfirmTransportationOptionsRequest body=new ConfirmTransportationOptionsRequest();
			List<TransportationSelection> translist=new ArrayList<TransportationSelection>();
			ShipAddress address = shipAddressService.getById(plan.getSourceAddress());
			ContactInformation contact=new ContactInformation();
			contact.setEmail(address.getEmail());
			contact.setName(address.getContact());
			contact.setPhoneNumber(address.getPhone());
			for(TransportationDTO dto:transportationSelections) {
				TransportationSelection item=new TransportationSelection();
				item.setContactInformation(contact);
				item.setTransportationOptionId(dto.getTransportationOptionId());
				item.setShipmentId(dto.getShipmentid());
				translist.add(item);
			}

			body.setTransportationSelections(translist);
			ConfirmTransportationOptionsResponse response = iInboundApiHandlerService.confirmTransportationOptions(auth, plan.getInboundPlanId(), body);
			if(response!=null) {
				operation= iShipInboundOperationService.setOperationID(auth, plan.getId(), response.getOperationId());
				if(operation.getOperationStatus()!=null&&!operation.getOperationStatus().equals("FAILED")){
					for(TransportationDTO dto:transportationSelections) {
						ShipInboundShipment shipment = shipInboundShipmentService.getById(dto.getShipmentid());
						if(shipment!=null) {
							shipment.setTransactionName(dto.getTransactionName());
							if(dto.getTransactionName()!=null){
								shipment.setTransactionName(dto.getTransactionName());
								if(dto.getTransactionName().contains("PARCEL")){
									shipment.setTranstyle("SP");
								}
								else if(dto.getTransactionName().contains("LTL")){
									shipment.setTranstyle("LTL");
								}
								else{
									shipment.setTranstyle("SP");
								}
								shipInboundShipmentService.updateById(shipment);
							}
						}
					}

				}
				return operation;
			}else {
				return null;
			}
		}catch (BizException e){
			if(e.getMessage().contains("option has been confirmed for shipment")){
				if(plan.getAuditstatus()==6) {
					 operation =new ShipInboundOperation();
					 operation.setFormid(plan.getId());
					 operation.setOpttime(new Date());
					 operation.setOperation("confirmTransportationOptions");
					 operation.setOperationStatus("SUCCESS");
					 operation.setOperationid(UUID.randomUUID().toString());
					 iShipInboundOperationService.save(operation);
				     return operation;
				}
			}else{
				throw e;
			}

		}
		return null;
	}


	@Override
	@Transactional
	public void saveSelfTransData(UserInfo user, ShipInboundTrans ship ,ShipInboundShipment shipment,Date shipdate ) {
		QueryWrapper<ShipInboundTrans> query=new QueryWrapper<ShipInboundTrans>();
		query.eq("shipmentid", ship.getShipmentid());
		ShipInboundTrans item = shipInboundTransMapper.selectOne(query);
		if(StrUtil.isBlank(ship.getCompany())) {
			ship.setCompany(null);
		}
		if(StrUtil.isBlank(ship.getChannel())) {
			ship.setChannel(null);
		}else{
			Map<String, Object> map = this.erpClientOneFeignManager.getShipTransChannelDetial(ship.getChannel());

			if(map!=null){
				if(map.get("pretime")!=null && ship.getArrivalTime()==null){
					Calendar c=Calendar.getInstance();
					c.add(Calendar.DATE, Integer.parseInt(map.get("pretime").toString()));
					ship.setArrivalTime(c.getTime());
				}
			}
			if(shipment.getShipedDate()==null && shipdate==null){
				shipment.setShipedDate(new Date());
				this.shipInboundShipmentService.updateById(shipment);
			}else if(shipdate!=null){
				shipment.setShipedDate(shipdate);
				this.shipInboundShipmentService.updateById(shipment);
			}


		}
		ship.setShipmentid(shipment.getShipmentConfirmationId());
		ShipInboundPlan plan = iShipInboundPlanService.getById(shipment.getFormid());
		plan.setTranstype(ship.getTranstype()!=null?ship.getTranstype().toString():null);

		iShipInboundPlanService.updateById(plan);
		if (item!=null) {
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
		if(transfee==null||transfee.getSingleprice()==null||transfee.getTransweight()==null) {
	    return;
		}

		  List<Map<String, Object>> itemlist = shipInboundShipmentItemV2Mapper.selectByShipmentid(shipment.getShipmentid());
		  BigDecimal totalWeight=new BigDecimal("0");
		  BigDecimal totalFee=new BigDecimal("0");
		  for(Map<String,Object> item:itemlist) {
			  BigDecimal weight =item.get("weight")!=null?new BigDecimal(item.get("weight").toString()):new BigDecimal("0");
			  BigDecimal dimweight =item.get("dimweight")!=null?new BigDecimal(item.get("dimweight").toString()):new BigDecimal("0");
			  BigDecimal maxWeight=weight.compareTo(dimweight)>0?weight:dimweight;
			  if(maxWeight.floatValue()<0.000001){
				  throw new BizException(item.get("sku").toString()+"未找到重量，请确认正确维护了本地SKU信息");
			  }
			  item.put("maxWeight", maxWeight);
			  totalWeight=totalWeight.add(maxWeight.multiply(new BigDecimal(item.get("QuantityShipped").toString())));
		  }
		  if(transfee.getOtherfee()!=null) {
			  totalFee=totalFee.add(transfee.getOtherfee());
		  }
		  if(transfee.getSingleprice()!=null&&transfee.getTransweight()!=null) {
			  totalFee=totalFee.add(transfee.getSingleprice().multiply(transfee.getTransweight()));
		  }
		  int i=0;
		  BigDecimal subPrecent=new BigDecimal("1");
		  BigDecimal subPrice=new BigDecimal("0");
		  for(Map<String,Object> feeitem:itemlist) {
			  BigDecimal maxWeight=feeitem.get("maxWeight")!=null?new BigDecimal(feeitem.get("maxWeight").toString()):new BigDecimal("0");
			  BigDecimal price=feeitem.get("price")!=null?new BigDecimal(feeitem.get("price").toString()):new BigDecimal("0");
			  BigDecimal priceWavg=feeitem.get("price_wavg")!=null?new BigDecimal(feeitem.get("price_wavg").toString()):price;
			  BigDecimal precent = maxWeight.multiply(new BigDecimal(feeitem.get("QuantityShipped").toString())).divide(totalWeight,6,RoundingMode.FLOOR) ;
			  subPrecent=subPrecent.subtract(precent);
			  i++;
			  if(i==itemlist.size()&&subPrecent.floatValue()>0.000001) {
				  precent=precent.add(subPrecent);
			  }
			  BigDecimal itemfee=  totalFee.multiply(precent).setScale(2,RoundingMode.FLOOR);
			  subPrice=subPrice.add(itemfee.subtract(itemfee.setScale(0,RoundingMode.FLOOR)).divide(new BigDecimal("100"),6,RoundingMode.FLOOR));
			  if(i==itemlist.size()&&subPrice.floatValue()>0.000001) {
				  itemfee=itemfee.add(subPrice);
				  itemfee=itemfee.setScale(2,RoundingMode.FLOOR);
			  }
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
