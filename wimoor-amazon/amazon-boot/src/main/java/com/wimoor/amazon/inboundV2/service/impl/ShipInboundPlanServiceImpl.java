package com.wimoor.amazon.inboundV2.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.amazon.spapi.model.fulfillmentinboundV20240320.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inboundV2.pojo.entity.*;
import com.wimoor.amazon.inventory.service.IInventorySupplyService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import com.amazon.spapi.model.fulfillmentinbound.LabelOwner;
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
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentRecordV2Service;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundPlanV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.dto.InboundPlansDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipPlanListDTO;
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
    final IShipInboundShipmentRecordV2Service shipInboundShipmentRecordV2Service;
	final ISerialNumService serialNumService;
	final IShipAddressService shipAddressService;
	final IShipAddressToService shipAddressToService;
	final ProductInfoMapper productInfoMapper;
	final IProductInPresaleService productInPresaleService;
	final IDaysalesFormulaService daysalesFormulaService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IAmzShipFulfillmentCenterService iAmzShipFulfillmentCenterService;
	final IInboundApiHandlerService iInboundApiHandlerService;
	final IFulfillmentInboundService iFulfillmentInboundService;
	final IInventorySupplyService iInventorySupplyService;
	public void saveShipInboundPlan(ShipInboundPlan inplan) {
		// TODO Auto-generated method stub
		if(inplan.getSourceAddress()==null){
			throw new BizException("发货地址不能为空");
		}
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
			shipInboundShipmentRecordV2Service.saveRecord(plan);
			return null;
		}
		AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
		Marketplace market = marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
		List<ShipInboundItem> itemlist = iShipInboundItemService.lambdaQuery().eq(ShipInboundItem::getFormid, plan.getId()).list();
		ShipAddress address = shipAddressService.getById(plan.getSourceAddress());
		if(address==null){
			throw new BizException("未选择发货地址");
		}
		CreateInboundPlanResponse response = iInboundApiHandlerService.createInboundPlan(auth, market, plan,itemlist,address);
		if(response!=null) {
			plan.setInboundPlanId(response.getInboundPlanId());
			this.updateById(plan);
			shipInboundShipmentRecordV2Service.saveRecord(plan);
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
			if(address==null){
				throw new BizException("未选择发货地址");
			}
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
	public InboundPlansDTO getPlanListSync(InboundPlansDTO dto, UserInfo user) {
		// TODO Auto-generated method stub
		if(dto.getSortBy()==null) {
			dto.setSortBy("CREATION_TIME");
			dto.setSortOrder("DESC");
		}
		if(dto.getStatus()==null) {
			dto.setStatus("SHIPPED");
		}
		if(dto.getPageSize()==null) {
			dto.setPageSize(30);
		}
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
		ListInboundPlansResponse response = iInboundApiHandlerService.listInboundPlans(auth, dto.getPageSize(),
				dto.getPaginationToken(), dto.getStatus(), dto.getSortBy()	, dto.getSortOrder());
		if(response!=null&&response.getInboundPlans()!=null){
			List<ShipInboundPlan> resultlist=new LinkedList<ShipInboundPlan>();
			for(InboundPlanSummary item:response.getInboundPlans()){
				if(item.getInboundPlanId()!=null) {
					ShipInboundPlan plan = this.lambdaQuery().eq(ShipInboundPlan::getInboundPlanId, item.getInboundPlanId()).one();
					if(plan==null){
						plan=new ShipInboundPlan();
						plan.setSource(getAddress(item.getSourceAddress()));
						plan.setName(item.getName());
						plan.setMarketplaceid(item.getMarketplaceIds().get(0));
						plan.setAmazonauthid(auth.getId());
						plan.setInboundPlanId(item.getInboundPlanId());
						plan.setInvtype(5);
						Calendar c= Calendar.getInstance();
						if(item.getCreatedAt()!=null){
							plan.setCreatetime(AmzDateUtils.getDate(item.getCreatedAt().toLocalDateTime()));
						}
						plan.setNumber(item.getStatus());
						resultlist.add(plan) ;
					} else{
						plan.setSource(getAddress(item.getSourceAddress()));
						resultlist.add(plan) ;
					}
				}
			}
			dto.setList(resultlist);
			if(response.getPagination()!=null){
				dto.setPaginationToken(response.getPagination().getNextToken());
			}
			return dto;
		}
		return null;
	}

	private ShipAddress getAddress(Address address){
		if(address!=null){
			ShipAddress shipaddress = new ShipAddress();
			shipaddress.setCity(address.getCity());
			shipaddress.setCompanyName(address.getCompanyName());
			shipaddress.setCountrycode(address.getCountryCode());
			shipaddress.setEmail(address.getEmail());
			shipaddress.setName(address.getName());
			shipaddress.setPhone(address.getPhoneNumber());
			shipaddress.setPostalcode(address.getPostalCode());
			shipaddress.setStateorprovincecode(address.getStateOrProvinceCode());
			shipaddress.setAddressline1(address.getAddressLine1());
			shipaddress.setAddressline2(address.getAddressLine2());
			return shipaddress;
		}else{
			return null;
		}

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
				boolean result = this.updateById(inplan);
				shipInboundShipmentRecordV2Service.saveRecord(inplan);
				return result;
			} else if(shipInboundBoxV2Service.hasSubmitPackage(inplan)) {
				if(inplan.getAuditstatus()==3) {
					inplan.setAuditstatus(4);
					inplan.setOperator(user.getId());
					inplan.setSubmitbox(true);
					inplan.setOpttime(new Date());
				}
				boolean result = this.updateById(inplan);
				shipInboundShipmentRecordV2Service.saveRecord(inplan);
				return result;
			}else {
				throw new BizException("未找到箱子提交记录，请确认您已经正确提交箱子信息");
			}
			
	}

	@Override
	public void syncData(ShipInboundPlan plan, UserInfo user) {
		Marketplace marketplace = this.marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
		AmazonAuthority auth = this.amazonAuthorityService.getById(plan.getAmazonauthid());
		auth.setMarketPlace(marketplace);
		try{
			new Thread(
				()->{
					try{
						List<String> skulist= iShipInboundItemService.lambdaQuery().eq(ShipInboundItem::getFormid, plan.getId()).list().stream().map(item->item.getSku()).collect(Collectors.toList());
						iInventorySupplyService.syncInventorySupply(auth, skulist);
					}catch (Exception e){
						e.printStackTrace();
					}

					try{
						List<String> shipmentids= shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getFormid, plan.getId()).list().stream().map(item->item.getShipmentConfirmationId()).collect(Collectors.toList());
						iFulfillmentInboundService.listShipmentAsync(auth, marketplace,shipmentids);
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			).start();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String mytoken = erpClientOneFeign.getQuoteToken();
			List<String> shipmentids= shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getFormid, plan.getId()).list().stream().map(item->item.getShipmentid()).collect(Collectors.toList());
			if(StrUtil.isNotBlank(mytoken)){
				for(String shipmentId:shipmentids){
					shipInboundShipmentService.toQuote(user,mytoken,shipmentId);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}


	}

	@Override
	public ShipInboundPlan refreshPlan(ShipInboundPlan dto, UserInfo user) {
		AmazonAuthority auth = amazonAuthorityService.getById(dto.getAmazonauthid());
		InboundPlan response = iInboundApiHandlerService.getInboundPlan(auth, dto.getInboundPlanId());
		if(response!=null){
			ShipInboundPlan inboundplan = this.lambdaQuery().eq(ShipInboundPlan::getInboundPlanId,response.getInboundPlanId()).one();
			if(inboundplan!=null){
				inboundplan.setName(response.getName());
				if(response.getPlacementOptions()!=null) {
					List<PlacementOptionSummary> placementOptions = response.getPlacementOptions();
					if (placementOptions != null && placementOptions.size() > 0) {
						PlacementOptionSummary placementItem = placementOptions.get(0);
						inboundplan.setPlacementOptionId(placementItem.getPlacementOptionId());
					}
					inboundplan.setSource(getAddress(response.getSourceAddress()));
				}
				if(response.getMarketplaceIds()!=null&&response.getMarketplaceIds().size()>0){
					inboundplan.setMarketplaceid(response.getMarketplaceIds().get(0));
				}
				this.updateById(inboundplan);
			}
			return inboundplan;
		}
		return null;
	}

	@Override
	public ShipInboundPlan detailPlan(ShipInboundPlan dto, UserInfo user) {
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
		InboundPlan response = iInboundApiHandlerService.getInboundPlan(auth, dto.getInboundPlanId());
		if(response!=null){
			ShipInboundPlan inboundplan = new ShipInboundPlan();
			if(inboundplan!=null){
				inboundplan.setName(response.getName());
				if(response.getPlacementOptions()!=null) {
					List<PlacementOptionSummary> placementOptions = response.getPlacementOptions();
					if (placementOptions != null && placementOptions.size() > 0) {
						PlacementOptionSummary placementItem = placementOptions.get(0);
						inboundplan.setPlacementOptionId(placementItem.getPlacementOptionId());
					}
					inboundplan.setSource(getAddress(response.getSourceAddress()));
				}
				if(response.getMarketplaceIds()!=null&&response.getMarketplaceIds().size()>0){
					inboundplan.setMarketplaceid(response.getMarketplaceIds().get(0));
				}
				inboundplan.setInboundPlanId(dto.getInboundPlanId());
				ListInboundPlanItemsResponse itemsresponse = iInboundApiHandlerService.listInboundPlanItems(auth, dto.getInboundPlanId(), 1000, null);
				List<ShipInboundItem> list=new LinkedList<ShipInboundItem>();
				for(Item item:itemsresponse.getItems()){
					ShipInboundItem e=new ShipInboundItem();
					e.setQuantity(item.getQuantity());
					e.setConfirmQuantity(item.getQuantity());
					e.setSku(item.getMsku());
					e.setPrepOwner(item.getPrepInstructions().get(0).getPrepOwner().toString());
					list.add(e);
				}
				try {
					inboundplan.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SF"));
				} catch (Exception e) {
					e.printStackTrace();
					try {
						inboundplan.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SF"));
					} catch (Exception e1) {
						e1.printStackTrace();
						throw new BizException("编码获取失败,请联系管理员");
					}
				}
				inboundplan.setPlanitemlist(list);
			}

		}
		return null;
	}

	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		    List<ShipInboundShipment> mapList = shipInboundShipmentService.listRefreshShipmentId(amazonAuthority.getId());
			for(ShipInboundShipment item:mapList){
				ShipInboundPlan inplan = this.getById(item.getFormid());
				shipInboundShipmentService.asyncShipmentList( amazonAuthority,  inplan,  item.getShipmentid());
			}
	}
	@Override
	public void savePlanItemSync(InboundPlan planResult, ShipPlanListDTO dto, UserInfo user,AmazonAuthority auth) {
		//保存inboundPlan
		ShipInboundPlan plan=new ShipInboundPlan();
		plan.setOpttime(AmzDateUtils.getDate(planResult.getLastUpdatedAt()));
		plan.setOperator(user.getId());
		plan.setCreator(user.getId());
		plan.setCreatetime(AmzDateUtils.getDate(planResult.getCreatedAt()));
		plan.setGroupid(dto.getGroupid());
		plan.setPlacementOptionId(planResult.getPlacementOptions().get(0).getPlacementOptionId());
		if(StrUtil.isNotBlank(planResult.getName())){
			plan.setName(planResult.getName());
		}else{
			Calendar c=Calendar.getInstance();
			String date_str = GeneralUtil.formatDate(c.getTime(), "MM/dd/yyyy HH:mm");
			String names="PLN"+"("+date_str+")";
			plan.setName(names);
		}
		plan.setRemark("从后台同步计划");
		plan.setWarehouseid(dto.getWarehouseid());
		plan.setSource(getAddress(planResult.getSourceAddress()));
		plan.setShopid(user.getCompanyid());
		plan.setMarketplaceid(dto.getMarketplaceid());
		plan.setInvstatus(0);
		//查询address
		ShipAddress address = shipAddressService.lambdaQuery().eq(ShipAddress::getShopid, user.getCompanyid())
				.eq(ShipAddress::getName, planResult.getSourceAddress().getName()).eq(ShipAddress::getGroupid, plan.getGroupid()).one();
		if(address!=null){
			plan.setSourceAddress(address.getId());
		}
		String status = planResult.getStatus();
		if("SHIPPED".equals(status)){
			plan.setAuditstatus(8);
		}
		if("ERRORED".equals(status)){
			throw new BizException("该货件计划已产生错误！！不支持同步。");
		}
		if(planResult.getShipments()!=null && planResult.getShipments().size()>0){
			List<String> shipmentList=new ArrayList<String>();
			//iInboundApiHandlerService.listPlacementOptions
			for (ShipmentSummary shipment:planResult.getShipments()){
				shipmentList.add(shipment.getShipmentId());
			}
			plan.setShipmentids(shipmentList);
		}
		plan.setInboundPlanId(planResult.getInboundPlanId());
		plan.setInvtype(2);
		try {
			plan.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SF"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				plan.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SF"));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
		}
		String planid = amazonAuthorityService.getUUID();
		plan.setId(planid);
		plan.setSubmitbox(true);
		//保存plan
		this.save(plan);
		//保存planItem
		saveSyncPlanItem(plan,dto.getItemlist());
		//保存plan箱子信息
		ListInboundPlanBoxesResponse boxResult = iInboundApiHandlerService.listInboundPlanBoxes(auth, plan.getInboundPlanId(), 1000, null);
		savePlanBox(plan,boxResult);
		//保存shipment
		shipInboundShipmentService.saveShipment(plan,plan.getShipmentids());
	}


	//保存planItem
	private void saveSyncPlanItem(ShipInboundPlan plan,List<ShipInboundItemVo> itemList) {
		for(ShipInboundItemVo vo:itemList){
			ShipInboundItem item=new ShipInboundItem();
			item.setFormid(plan.getId());
			item.setSku(vo.getSku());
			item.setQuantity(vo.getQuantity());
			item.setConfirmQuantity(vo.getConfirmQuantity());
			item.setPrepOwner(vo.getPrepOwner());
			item.setMsku(vo.getMsku());
			item.setLabelOwner(vo.getLabelOwner());
			item.setOpttime(new Date());
			item.setOperator(plan.getOperator());
			item.setManufacturingLotCode(vo.getManufacturingLotCode());
			item.setExpiration(vo.getExpiration());
			iShipInboundItemService.save(item);
		}
	}

	//保存plan箱子信息
	private void savePlanBox(ShipInboundPlan plan, ListInboundPlanBoxesResponse boxResult) {
		if(boxResult.getBoxes()!=null&&boxResult.getBoxes().size()>0){
			int num=0;
			for (Box box:boxResult.getBoxes()){
				num++;
				String boxid=amazonAuthorityService.getUUID();
				ShipInboundBox item=new ShipInboundBox();
				item.setFormid(plan.getId());
				item.setOpttime(new Date());
				item.setOperator(plan.getOperator());
				item.setPackingGroupId(box.getPackageId());
				Dimensions dim = box.getDimensions();
				item.setLength(dim.getLength());
				item.setHeight(dim.getHeight());
				item.setWidth(dim.getWidth());
				item.setWeight(box.getWeight().getValue());
				item.setWunit(box.getWeight().getUnit().getValue());
				item.setSumsku(box.getItems().size());
				item.setUnit(box.getDimensions().getUnitOfMeasurement().getValue());
				item.setId(boxid);
				item.setBoxnum(num);
				shipInboundBoxV2Service.save(item);
				for (Item caseItem:box.getItems()){
					ShipInboundCase itemcase=new ShipInboundCase();
					itemcase.setBoxid(boxid);
					itemcase.setSku(caseItem.getMsku());
					itemcase.setQuantity(caseItem.getQuantity());
					shipInboundCaseV2Service.save(itemcase);
				}
			}

		}

	}

  public Map<String,Object> findNum(String shopid){
		return this.baseMapper.findNum(shopid);
  }


	@Override
	public IPage<Map<String, Object>> getShipmentDetailReport(Page<Object> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		IPage<Map<String, Object>> result = this.baseMapper.getShipmentDetailReport(page,param);
		if (result != null && result.getRecords().size() > 0&&page.getCurrent()==1) {
			Map<String, Object> summary = this.baseMapper.getShipmentDetailReportTotal(param);
			Map<String, Object> map = result.getRecords().get(0);
			map.put("summary", summary);
		}
		return result;
	}

	@Override
	public IPage<Map<String, Object>> getShipmentReport(Page<Object> page, Map<String, Object> param) {
		return this.baseMapper.getShipmentReport(page,param);
	}

	@Override
	public void setExcelBookByType(SXSSFWorkbook workbook, Map<String, Object> params) {
		String type = params.get("ftype").toString();
		// 货件发货明细
		if ("shipment".equals(type)) {
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			titlemap.put("ShipmentId", "货件编码");
			titlemap.put("groupname", "发货店铺");
			titlemap.put("market", "收货站点");
			titlemap.put("center", "配送中心");
			titlemap.put("warehouse", "发货仓库");
			titlemap.put("createdate", "创建日期");
			titlemap.put("shiped_date", "发货日期");
			titlemap.put("arrivalTime", "预计到货日期");
			titlemap.put("start_receive_date", "开始接受日期");
			titlemap.put("status6date", "完成日期");
			titlemap.put("company", "承运商");
			titlemap.put("transtype", "运输方式");
			titlemap.put("channame", "发货渠道");

			titlemap.put("qtyshipped", "发货数量");
			titlemap.put("qtyreceived", "到货数量");
			titlemap.put("price", "运输费用");
			titlemap.put("otherfee", "其它费用");
			titlemap.put("totalprice", "物流费用统计");
			titlemap.put("singleprice", "物流单价");
			titlemap.put("transweight", "运输重量");
			titlemap.put("transweight", "实际计费重量");
			titlemap.put("weightkg", "预估发货重量");
			titlemap.put("boxweight", "装箱实际重量(kg)");
			titlemap.put("boxvolume", "装箱材积重量");
			titlemap.put("wunit", "重量单位");
			titlemap.put("shipmentstatus", "状态");
			List<Map<String, Object>> list = this.baseMapper.getShipmentReport(params);
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						if ("totalprice".equals(titlearray[j].toString())) {
							if (StrUtil.isEmpty(value.toString()) || value == null) {
								value = "";
							}
						}
						cell.setCellValue(value.toString());
					}
				}
			}
		} else if("mainlist".equals(type)){
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			titlemap.put("sku", "SKU");
			titlemap.put("ShipmentId", "货件编码");
			titlemap.put("groupname", "发货店铺");
			titlemap.put("market", "收货站点");
			titlemap.put("center", "配送中心");
			titlemap.put("warehouse", "发货仓库");
			titlemap.put("shiped_date", "发货日期");
			titlemap.put("arrivalTime", "预计到货日期");
			titlemap.put("channame", "发货渠道");
			titlemap.put("quantity", "发货数量");
			titlemap.put("received", "到货数量");
			titlemap.put("shipmentstatus", "状态");
			titlemap.put("createtime", "创建日期");
			List<Map<String, Object>> list = this.baseMapper.getShipmentDetailReport(params);
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}else {
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			titlemap.put("mainsku", "本地SKU");
			titlemap.put("subsku", "本地子SKU");
			titlemap.put("name", "产品名称");
			titlemap.put("subnumber", "子SKU单位量");
			titlemap.put("subskuout", "子SKU消耗量");
			titlemap.put("QuantityShipped", "发货数量");
			titlemap.put("sku", "平台SKU");
			titlemap.put("ShipmentId", "货件编码");
			titlemap.put("groupname", "发货店铺");
			titlemap.put("market", "收货站点");
			titlemap.put("center", "配送中心");
			titlemap.put("warehouse", "发货仓库");
			titlemap.put("shiped_date", "发货日期");
			titlemap.put("arrivalTime", "预计到货日期");
			titlemap.put("channame", "发货渠道");
			titlemap.put("QuantityReceived", "到货数量");
			titlemap.put("shipmentstatus", "状态");
			titlemap.put("createdate", "创建日期");
			List<Map<String, Object>> list = this.baseMapper.getShipmentDetailAssReport(params);
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	}

}
