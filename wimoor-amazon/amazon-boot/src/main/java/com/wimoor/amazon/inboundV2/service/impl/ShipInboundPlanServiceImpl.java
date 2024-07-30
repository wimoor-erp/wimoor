package com.wimoor.amazon.inboundV2.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import com.amazon.spapi.model.fulfillmentinbound.LabelOwner;
import com.amazon.spapi.model.fulfillmentinboundV20240320.CancelInboundPlanResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.CreateInboundPlanResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.DocumentDownload;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateSelfShipAppointmentSlotsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateSelfShipAppointmentSlotsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GetDeliveryChallanDocumentResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListInboundPlanPalletsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListInboundPlansResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Pallet;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PrepOwner;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UpdateShipmentSourceAddressResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IDaysalesFormulaService;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.service.IAmzShipFulfillmentCenterService;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundPlanV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.dto.InboundPlansDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inboundV2.pojo.vo.SummaryPlanVo;
import com.wimoor.amazon.inboundV2.service.IInboundApiHandlerService;
import com.wimoor.amazon.inboundV2.service.IShipInboundBoxService;
import com.wimoor.amazon.inboundV2.service.IShipInboundCaseService;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.amazon.inboundV2.service.IShipInboundOperationService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.service.IProductInPresaleService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.dto.ShipFormDTO;
import com.wimoor.erp.ship.pojo.dto.ShipItemDTO;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("shipInboundPlanV2Service")
@RequiredArgsConstructor
public class ShipInboundPlanServiceImpl extends ServiceImpl<ShipInboundPlanV2Mapper,ShipInboundPlan> implements IShipInboundPlanService { 
	final IAmazonGroupService iAmazonGroupService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IMarketplaceService marketplaceService;
	@Autowired
	@Lazy
    IShipInboundShipmentService shipInboundShipmentService;
	@Autowired
	@Lazy
	IShipInboundItemService iShipInboundItemService;
	final IShipInboundOperationService  iShipInboundOperationService;
	final IShipInboundBoxService shipInboundBoxV2Service;
	final IShipInboundCaseService shipInboundCaseV2Service;
    final IShipInboundShipmentRecordService shipInboundShipmentRecordService;
	final ISerialNumService serialNumService;
	final IShipAddressService shipAddressService;
	final IShipAddressToService shipAddressToService;
	final ProductInfoMapper productInfoMapper;
	final IProductInPresaleService productInPresaleService;
	final IDaysalesFormulaService daysalesFormulaService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IAmzShipFulfillmentCenterService iAmzShipFulfillmentCenterService;
	final IInboundApiHandlerService iInboundApiHandlerService;
	public void saveShipInboundPlan(ShipInboundPlan inplan) {
		// TODO Auto-generated method stub
		 AmazonAuthority auth = this.amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	     if(auth!=null) {
	    	 inplan.setAmazonauthid(auth.getId());
	     }
		 save(inplan);
	     for(ShipInboundItem item:inplan.getPlanitemlist()) {
	    	 item.setFormid(inplan.getId());
	    	 item.setOperator(inplan.getOperator());
	    	 item.setOpttime(inplan.getOpttime());
	    	 item.setId(amazonAuthorityService.getUUID());
	    	 if(item.getLabelOwner()==null) {
	    		 item.setLabelOwner(LabelOwner.SELLER.getValue());
	    	 }
	    	 if(item.getPrepOwner()==null) {
	    		 item.setPrepOwner(PrepOwner.SELLER.getValue());
	    	 }
	    	 item.setConfirmQuantity(item.getQuantity());
	    	 iShipInboundItemService.save(item);
	     }
	   
	}
	
	public ShipFormDTO getFormDTO(ShipInboundPlan inplan,List<ShipInboundItem> itemlist) {
		ShipFormDTO mydto=new ShipFormDTO();
		mydto.setFormid(inplan.getId());
		mydto.setWarehouseid(inplan.getWarehouseid());
		mydto.setNumber(inplan.getNumber());
		List<ShipItemDTO> list=new ArrayList<ShipItemDTO>();
		for(ShipInboundItem item:itemlist) {
			ShipInboundItem old=iShipInboundItemService.getById(item.getId());
			ShipItemDTO dto=new ShipItemDTO();
			dto.setMsku(item.getMsku());
			if(item.getConfirmQuantity()==null) {
				item.setConfirmQuantity(item.getQuantity());
			}
			if(old.getConfirmQuantity()==null) {
				old.setConfirmQuantity(old.getQuantity());
			}
			dto.setQuantity(item.getConfirmQuantity()-old.getConfirmQuantity());
			dto.setSku(item.getSku());
			list.add(dto);
		}
		mydto.setList(list);
		return mydto;
	}
	
	public void changeShipInboundPlan(ShipInboundPlan inplan, List<ShipInboundItem> list) {
		// TODO Auto-generated method stub
		ShipFormDTO mydto=getFormDTO( inplan, list) ;
		this.erpClientOneFeign.updateItemQty(mydto);
		this.updateById(inplan);
	     for(ShipInboundItem item:list) {
	    	 ShipInboundItem old=iShipInboundItemService.getById(item.getId());
	    	 if(item.getLabelOwner()!=null) {
	    		 old.setLabelOwner(item.getLabelOwner());
	    	 }else {
	    		 old.setLabelOwner(LabelOwner.SELLER.getValue());
	    	 }
	    	 if(item.getPrepOwner()!=null) {
	    		 old.setPrepOwner(item.getPrepOwner());
	    	 }else {
	    		 old.setPrepOwner(PrepOwner.SELLER.getValue());
	    	 }
	    	 if(item.getExpiration()!=null) {
	    		 old.setExpiration(item.getExpiration());
	    	 }
	    	 if(item.getManufacturingLotCode()!=null) {
	    		 old.setManufacturingLotCode(item.getManufacturingLotCode());
	    	 }
	    	 old.setOperator(inplan.getOperator());
	    	 old.setOpttime(inplan.getOpttime());
	    	 old.setConfirmQuantity(item.getConfirmQuantity());
	    	 iShipInboundItemService.updateById(old);
	     }
	   
	}
	
	@Override
	public IPage<ShipPlanVo> getPlanList(ShipPlanListDTO dto,UserInfo user) {
		dto.setShopid(user.getCompanyid());
		if(StrUtil.isNotEmpty(dto.getSearchtype())&&StrUtil.isNotEmpty(dto.getSearch())) {
			if(dto.getSearchtype().equals("remark")||dto.getSearchtype().equals("sku")) {
				dto.setSearch("%"+dto.getSearch().trim()+"%");
			}
		}
		IPage<ShipPlanVo> list = this.baseMapper.findByCondition(dto.getPage(),dto);
		return list;
	}
	
 
	@Override
	public ShipInboundOperation cancelInboundPlan(ShipInboundPlan inplan) {
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
		CancelInboundPlanResponse response = iInboundApiHandlerService.cancelInboundPlan(auth, inplan.getInboundPlanId());
		if(response!=null) {
			return iShipInboundOperationService.setOperationID(auth, inplan.getId(), response.getOperationId());
		}
		return null;
	}


	@Override
	public Map<String, Object> listInboundPlanPallets(String planid) {
		ShipInboundPlan plan = this.baseMapper.selectById(planid);
		if(plan!=null) {
			AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
			ListInboundPlanPalletsResponse response = iInboundApiHandlerService.listInboundPlanPallets(auth, plan.getInboundPlanId(), null, null);
			if(response!=null) {
				Map<String,Object> map=new HashMap<String,Object>();
				List<Pallet> pallets = response.getPallets();
				map.put("options", pallets);
				map.put("pagination", response.getPagination());
				return map;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}


	@Override
	public void getDeliveryChallanDocument(String planid, String shipmentId) {
		ShipInboundPlan plan = this.baseMapper.selectById(planid);
		if(plan!=null) {
			AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
			GetDeliveryChallanDocumentResponse response = iInboundApiHandlerService.getDeliveryChallanDocument(auth, plan.getInboundPlanId(), shipmentId);
			if(response!=null) {
				DocumentDownload document = response.getDocumentDownload();
				document.setDownloadType("PDF_BASE64");
				document.setUri("");
			} 
		} 
	}



	@Override
	public String generateSelfShipAppointmentSlots(String planid, String shipmentId, String startDate, String endDate) {
		ShipInboundPlan plan = this.baseMapper.selectById(planid);
		if(plan!=null) {
			AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
			GenerateSelfShipAppointmentSlotsRequest dateitem=new GenerateSelfShipAppointmentSlotsRequest();
			OffsetDateTime desiredStartDate=OffsetDateTime.now();
			dateitem.setDesiredStartDate(desiredStartDate);
			OffsetDateTime desiredEndDate=OffsetDateTime.now();
			dateitem.setDesiredEndDate(desiredEndDate);
			GenerateSelfShipAppointmentSlotsResponse response = iInboundApiHandlerService.generateSelfShipAppointmentSlots(auth,plan.getInboundPlanId(),shipmentId,dateitem);
			if(response!=null) {
				String optionid=response.getOperationId();
				return optionid;
			}else {
				return null;
			} 
		}else {
			return null;
		} 
	}
	
	@Override
	public SummaryPlanVo showPlanListByPlanid(String formid) {
		return this.baseMapper.selectitemListByPlanid(formid);
	}

	
	@Override
	public ShipPlanVo getPlanBaseInfo(String formid, UserInfo user) {
		ShipPlanListDTO dto=new ShipPlanListDTO();
		dto.setFormid(formid);
		IPage<ShipPlanVo> list = this.getPlanList(dto, user);
		if(list!=null&&list.getTotal()>0) {
			ShipPlanVo vo=list.getRecords().get(0);
			ShipAddress address = shipAddressService.getById(vo.getAddressid());
			vo.setAddress(address);
			SummaryPlanVo summary = showPlanListByPlanid(formid);
		    List<ShipInboundItemVo> itemlist = iShipInboundItemService.listByFormid(formid);
			vo.setItemlist(itemlist);
			vo.setPlansummary(summary);
            return vo;
		}
		return null;
	}

	@Override
	public ShipInboundOperation confirmInboundPlan(ShipInboundPlan plan) {
		// TODO Auto-generated method stub
		if(plan.getInvtype()==1) {
			this.updateById(plan);
			return null;
		}
		AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
		Marketplace market = marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
		List<ShipInboundItem> itemlist = iShipInboundItemService.lambdaQuery().eq(ShipInboundItem::getFormid, plan.getId()).list();
		ShipAddress address = shipAddressService.getById(plan.getSourceAddress());
		CreateInboundPlanResponse response = iInboundApiHandlerService.createInboundPlan(auth, market, plan,itemlist,address);
		if(response!=null) {
			plan.setInboundPlanId(response.getInboundPlanId());
			this.updateById(plan);
			return iShipInboundOperationService.setOperationID(auth, plan.getId(), response.getOperationId());
		}
		return null;
	}

	@Override
	public ShipInboundPlan changeAddress(UserInfo user, String formid,String addressid) {
		// TODO Auto-generated method stub
		ShipInboundPlan plan = this.getById(formid);
		AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
		plan.setOperator(user.getId());
		plan.setOpttime(new Date());
		plan.setSourceAddress(addressid);
		this.updateById(plan);
		if(plan.getAuditstatus()>=3) {
			ShipAddress address = shipAddressService.getById(plan.getSourceAddress());
			List<ShipInboundShipment> shipments = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getInboundplanid, plan.getInboundPlanId()).list();
			for(ShipInboundShipment item:shipments) {
				UpdateShipmentSourceAddressResponse response = iInboundApiHandlerService.updateShipmentSourceAddress(auth, plan.getInboundPlanId(), item.getShipmentid(), address);
				iShipInboundOperationService.setOperationID(auth, plan.getId(), response.getOperationId());
			}
		}
		return plan;
	}

	@Override
	public ShipInboundPlan changeShipmentAddress(UserInfo user, String shipmentid,String addressid) {
		// TODO Auto-generated method stub
		ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		ShipInboundPlan plan =this.lambdaQuery().eq(ShipInboundPlan::getInboundPlanId,shipment.getInboundplanid()).one();
		AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
		plan.setOperator(user.getId());
		plan.setOpttime(new Date());
		plan.setSourceAddress(addressid);
		this.updateById(plan);
		ShipAddress address = shipAddressService.getById(plan.getSourceAddress());
		UpdateShipmentSourceAddressResponse response = iInboundApiHandlerService.updateShipmentSourceAddress(auth, plan.getInboundPlanId(), shipmentid, address);
		iShipInboundOperationService.setOperationID(auth, plan.getId(), response.getOperationId());
		return plan;
	}

	@Override
	public ListInboundPlansResponse getPlanListSync(InboundPlansDTO dto, UserInfo user) {
		// TODO Auto-generated method stub
		if(dto.getSortBy()==null) {
			dto.setSortBy("CREATION_TIME");
			dto.setSortOrder("DESC");
		}
		if(dto.getStatus()==null) {
			dto.setStatus("ACTIVE");
		}
		if(dto.getPageSize()==null) {
			dto.setPageSize(30);
		}
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
		ListInboundPlansResponse response = iInboundApiHandlerService.listInboundPlans(auth, dto.getPageSize(),
				dto.getPaginationToken(), dto.getStatus(), dto.getSortBy()	, dto.getSortOrder());
		return response;
	}

	@Override
	public Boolean donePacking(UserInfo user, String formid,String type) {
		// TODO Auto-generated method stub
			ShipInboundPlan inplan = this.getById(formid);
			if(StrUtil.isNotBlank(type)&&type.equals("skip")) {
				if(inplan.getAuditstatus()==3) {
					inplan.setAuditstatus(4);
					inplan.setOperator(user.getId());
					inplan.setSubmitbox(false);
					inplan.setOpttime(new Date());
				}
			    return this.updateById(inplan);
			} else if(shipInboundBoxV2Service.hasSubmitPackage(inplan)) {
				if(inplan.getAuditstatus()==3) {
					inplan.setAuditstatus(4);
					inplan.setOperator(user.getId());
					inplan.setSubmitbox(true);
					inplan.setOpttime(new Date());
				}
			    return this.updateById(inplan);
			}else {
				throw new BizException("未找到箱子提交记录，请确认您已经正确提交箱子信息");
			}
			
	}

	 
}
