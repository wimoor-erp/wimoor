package com.wimoor.amazon.inboundV2.service.impl;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.amazon.spapi.model.fulfillmentinboundV20240320.Address;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Box;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmDeliveryWindowOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmPlacementOptionResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Currency;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GenerateDeliveryWindowOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GeneratePlacementOptionsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GeneratePlacementOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Incentive;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Item;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListDeliveryWindowOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListPlacementOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentBoxesResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentItemsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PlacementOption;
import com.amazon.spapi.model.fulfillmentinboundV20240320.SelectedDeliveryWindow;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Shipment;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ShipmentDestination;
import com.amazon.spapi.model.fulfillmentinboundV20240320.SpdTrackingDetailInput;
import com.amazon.spapi.model.fulfillmentinboundV20240320.SpdTrackingItemInput;
import com.amazon.spapi.model.fulfillmentinboundV20240320.TrackingDetailsInput;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UpdateShipmentTrackingDetailsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UpdateShipmentTrackingDetailsResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransHisMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransMapper;
import com.wimoor.amazon.inbound.mapper.ShipTransDetailMapper;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmenSummaryDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipTransDetail;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.pojo.vo.SummaryShipmentVo;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inbound.service.IShipInboundshipmentTraceuploadService;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundBoxV2Mapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundCaseV2Mapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundDestinationAddressMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundPlanPlacementOptionsMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentBoxItemMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentBoxMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentDeliveryMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentItemV2Mapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.dto.PlacementDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipmentItemsDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.deliveryWindowDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundDestinationAddress;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlanPlacementOptions;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBox;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBoxItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentDelivery;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentItem;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.service.IInboundApiHandlerService;
import com.wimoor.amazon.inboundV2.service.IShipInboundBoxService;
import com.wimoor.amazon.inboundV2.service.IShipInboundCaseService;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.amazon.inboundV2.service.IShipInboundOperationService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentRecordV2Service;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.inventory.service.IInventorySupplyService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
 

@Service("shipInboundShipmentV2Service")
@RequiredArgsConstructor
public class ShipInboundShipmentServiceImpl extends  ServiceImpl<ShipInboundShipmentV2Mapper,ShipInboundShipment> implements IShipInboundShipmentService { 
	
	final IAmazonGroupService iAmazonGroupService;
	
	final IAmazonAuthorityService amazonAuthorityService;
	
	final IMarketplaceService marketplaceService;
	@Autowired 
	IShipInboundShipmentRecordService shipInboundShipmentRecordService;
	@Resource
	IShipInboundPlanService shipInboundPlanV2Service;
	@Resource
	private IShipInboundItemService shipInboundItemV2Service;
	
	final IInventorySupplyService iInventorySupplyService;
	
	final IShipInboundCaseService iShipInboundCaseService;
	
	final IShipInboundBoxService shipInboundBoxV2Service;
	
	final ISubmitfeedService submitfeedService;
	
	final IProductInfoService iProductInfoService;
	
	final ShipInboundTransMapper shipInboundTransMapper;
	
	final IShipAddressService shipAddressService;
	
	final ShipInboundBoxV2Mapper shipInboundBoxV2Mapper;
	
	final ShipInboundCaseV2Mapper shipInboundCaseV2Mapper;
	
	final ShipTransDetailMapper shipTransDetailMapper; 
	
	final IShipInboundTransService shipInboundTransService;
	
	final ShipInboundTransHisMapper shipInboundTransHisMapper;
	
	final IShipInboundshipmentTraceuploadService iShipInboundshipmentTraceuploadService;
	
	final IInboundApiHandlerService iInboundApiHandlerService;
	
	final IShipInboundOperationService  iShipInboundOperationService;
	
	final ShipInboundPlanPlacementOptionsMapper shipInboundPlanPlacementOptionsMapper;
	
	final IShipAddressToService iShipAddressToService;
	
	final ShipInboundDestinationAddressMapper shipInboundDestinationAddressMapper;
	
	final ShipInboundShipmentItemV2Mapper shipInboundShipmentItemV2Mapper;
	
	 
	final IFulfillmentInboundService iFulfillmentInboundService;
	
	final ShipInboundShipmentBoxMapper shipInboundShipmentBoxMapper;
	
	final ShipInboundShipmentBoxItemMapper shipInboundShipmentBoxItemMapper;
	
	final ShipInboundShipmentDeliveryMapper shipInboundShipmentDeliveryMapper;
	
	final IShipInboundShipmentRecordV2Service shipInboundV2ShipmentRecordService;
	@Resource
	private InventoryReportMapper inventoryReportMapper;
	
	@Autowired
	ErpClientOneFeignManager erpClientOneFeign;

	@Override
	public IPage<ShipInboundShipmenSummarytVo> findByTraceCondition(ShipInboundShipmenSummaryDTO dto) {
		// TODO Auto-generated method stub
				Page<ShipInboundShipmenSummarytVo> page=dto.getPage();
		   	    if(StrUtil.isNotBlank(dto.getSearchtype())&&StrUtil.isNotBlank(dto.getSearch())) {
		   	    	 if(dto.getSearchtype().equals("remark")) {
		   	    		 dto.setSearch("%"+dto.getSearch().trim()+"%");
		   	    	 }else if(dto.getSearchtype().equals("name")) {
		    	    	 dto.setSearch("%"+dto.getSearch().trim()+"%");
			    	 }else {
			    		 dto.setSearch("%"+dto.getSearch().trim()+"%");
			    	 }
		   	    }else {
		   	    	     dto.setSearch(null);
		   	    }

				IPage<ShipInboundShipmenSummarytVo> list = this.baseMapper.findByTraceCondition(page,dto);
				Map<String,String> centnumber=new HashMap<String,String>();
				if(dto.getAuditstatus()!=null&&dto.getAuditstatus().equals("7")) {
					List<Map<String, Object>> centlist = this.baseMapper.findByTraceConditionCenter(dto);
					for(Map<String, Object> cent:centlist) {
						centnumber.put(cent.get("centerId").toString(), cent.get("num")!=null?cent.get("num").toString():"0");
					}
				}
				
			   	 for(ShipInboundShipmenSummarytVo item:list.getRecords()) {
						String company = item.getCompanyid();
						if(centnumber.get(item.getCenterId())!=null) {
							item.setCenterNumber(centnumber.get(item.getCenterId()));
						}
						if(company!=null) {
							Result<Object> stcompany = erpClientOneFeign.getTransCompanyAPI(company);
								if(Result.isSuccess(stcompany)&&stcompany.getData()!=null) {
									Map<String, Object> map = BeanUtil.beanToMap(stcompany.getData());
									item.setApiSystem(map.get("system").toString());
								}
							}
					    if(item.getArrivalTime()!=null) {
					    	if(item.getArrivalTime().after(new Date())) {
					    		Integer delayDays=(Double.valueOf(GeneralUtil.distanceOfDay(item.getArrivalTime(), new Date())).intValue()*-1);
					    		item.setDelayDays(delayDays);
					    	}else {
					    		Integer delayDays=Double.valueOf(GeneralUtil.distanceOfDay(item.getArrivalTime(), new Date())).intValue();
					    		item.setDelayDays(delayDays);
					    	}
					    } 
					}
			   	 return list;
	}




	@Override
	@Cacheable(value = "PkgPaperCache")
	public Map<String, String> getPkgPaper(String transtyle) {
		Map<String, String> map = new TreeMap<String, String>();
		if ("LTL".equals(transtyle)) {
			map.put("PackageLabel_Plain_Paper", "1-1/1\"×4\"美国信纸");
			map.put("PackageLabel_Letter_6", "3-3/1\"×4\"美国信纸");
			map.put("PackageLabel_A4_4", "A4纸可显示4个标签");
		} else {
			map.put("PackageLabel_Plain_Paper", "1-1/1\"×4\"美国信纸");
			map.put("PackageLabel_Letter_2", "2-2/1\"×4\"美国信纸");
			map.put("PackageLabel_Letter_6", "3-3/1\"×4\"美国信纸");
			map.put("PackageLabel_A4_2", "A4纸可显示2个标签");
			map.put("PackageLabel_A4_4", "A4纸可显示4个标签");
		}
		return map;
	}

	
@Override
public ShipInboundOperation generatePlacementOptions(String planid) {
	ShipInboundPlan plan = shipInboundPlanV2Service.getById(planid);
	if(plan!=null) {
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ShipInboundOperation transportation = iShipInboundOperationService.getOperation(auth, plan.getId(), "confirmPlacementOption");
		if(transportation!=null&&transportation.getOperationStatus().equals("SUCCESS")) {
			ShipInboundOperation old = iShipInboundOperationService.getOperation(auth, plan.getId(), "generatePlacementOptions");
			return old;
		}
		GeneratePlacementOptionsRequest body=new GeneratePlacementOptionsRequest();
		if(StrUtil.isBlankOrUndefined(plan.getInboundPlanId())) {
			return null;
		}
		GeneratePlacementOptionsResponse response = iInboundApiHandlerService.generatePlacementOptions(auth, plan.getInboundPlanId(),body);
		if(response!=null) {
			return iShipInboundOperationService.setOperationID(auth, plan.getId(), response.getOperationId());
		}else {
			return null;
		}
	}else {
		return null;
	}
}

private void saveOptions(ShipInboundPlan plan,List<PlacementOption> options) {
	for(PlacementOption option:options) {
		LambdaQueryWrapper<ShipInboundPlanPlacementOptions> query=new LambdaQueryWrapper<ShipInboundPlanPlacementOptions>();
		if(plan.getInboundPlanId()!=null) {
			query.eq(ShipInboundPlanPlacementOptions::getInboundPlanId, plan.getInboundPlanId());
		}else {
			query.eq(ShipInboundPlanPlacementOptions::getInboundPlanId, plan.getId());
		}
		query.eq(ShipInboundPlanPlacementOptions::getPlacementOptionId, option.getPlacementOptionId());
		query.eq(ShipInboundPlanPlacementOptions::getFormid, plan.getId());
		ShipInboundPlanPlacementOptions old = shipInboundPlanPlacementOptionsMapper.selectOne(query);
		if(old!=null) {
			old.setFormid(plan.getId());
			old.setContents(JSONUtil.toJsonStr(option));
			old.setStatus(option.getStatus());
			shipInboundPlanPlacementOptionsMapper.update(old, query);
		}else {
			ShipInboundPlanPlacementOptions one=new ShipInboundPlanPlacementOptions();
			if(plan.getInboundPlanId()!=null) {
				one.setInboundPlanId( plan.getInboundPlanId());
			}else {
				one.setInboundPlanId(  plan.getId());  
			}
			one.setFormid(plan.getId());
			one.setPlacementOptionId(option.getPlacementOptionId());
			one.setContents(JSONUtil.toJsonStr(option));
			one.setStatus(option.getStatus());
			shipInboundPlanPlacementOptionsMapper.insert(one);
		}
		
	}
}

public static String generateUniqueKey() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[6]; // 6 bytes = 12 hex characters
    random.nextBytes(bytes);
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
            hexString.append('0'); // 确保每个字节都是两位16进制数
        }
        hexString.append(hex);
    }
    return hexString.toString().toUpperCase();
}

@Override
public Map<String, Object> listPlacementOptions(PlacementDTO dto) {
	ShipInboundPlan plan = shipInboundPlanV2Service.getById(dto.getFormid());
	if(plan!=null&&StrUtil.isNotBlank(plan.getInboundPlanId())) {
		if(plan.getAuditstatus()>4&&plan.getAuditstatus()<=12) {
			Map<String,Object> map=new HashMap<String,Object>();
			LambdaQueryWrapper<ShipInboundPlanPlacementOptions> query=new LambdaQueryWrapper<ShipInboundPlanPlacementOptions>();
			if(plan.getInboundPlanId()!=null) {
				query.eq(ShipInboundPlanPlacementOptions::getInboundPlanId, plan.getInboundPlanId());
			}else {
				query.eq(ShipInboundPlanPlacementOptions::getInboundPlanId, plan.getId());
			}
			if(plan.getPlacementOptionId()!=null) {
				query.eq(ShipInboundPlanPlacementOptions::getPlacementOptionId, plan.getPlacementOptionId());
			}
			query.eq(ShipInboundPlanPlacementOptions::getFormid, plan.getId());
			List<ShipInboundPlanPlacementOptions> list = shipInboundPlanPlacementOptionsMapper.selectList(query);
			List<PlacementOption> options = new ArrayList<PlacementOption>();
			for(ShipInboundPlanPlacementOptions optionitem:list) {
				JSONObject json = JSONUtil.parseObj(optionitem.getContents());
				PlacementOption option=JSONUtil.toBean(json, PlacementOption.class, true);
				options.add(option);
			}
			saveOptions(plan, options);
			map.put("options", options);
			map.put("pagination", null);
			return map;
		}
		if(plan.getInvtype()==1) {
			Map<String,Object> map=new HashMap<String,Object>();
			List<PlacementOption> options = new ArrayList<PlacementOption>();
			PlacementOption	option=new PlacementOption();
			option.setShipmentIds(Arrays.asList("FBA"+generateUniqueKey() ));
			option.setStatus("OFFERED");
			Incentive inv = new Incentive();
			inv.setType("");
			Currency currency=new Currency();
			currency.setAmount(new BigDecimal("0"));
			currency.setCode("RMB");
			inv.setValue(currency);
			List<Incentive> fee=Arrays.asList(inv);
			option.setFees(fee);
			option.setPlacementOptionId(plan.getId());
			options.add(option);
			
			saveOptions(plan, options);
			map.put("options", options);
			map.put("pagination", null);
			return map;
		}
		if(dto.getPageSize()==null) {
		   dto.setPageSize(20);
		}
		
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ListPlacementOptionsResponse response = iInboundApiHandlerService.listPlacementOptions(auth, plan.getInboundPlanId(), null, null);
		if(response!=null) {
			Map<String,Object> map=new HashMap<String,Object>();
			List<PlacementOption> options = response.getPlacementOptions();
			map.put("options", options);
			saveOptions(plan, options);
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
public ShipInboundOperation confirmPlacementOption(UserInfo user,String planid, String placementOptionId) {
	ShipInboundPlan plan = shipInboundPlanV2Service.getById(planid);
	if(plan!=null) {
		if(plan.getInvtype()==1) {
	    	LambdaQueryWrapper<ShipInboundPlanPlacementOptions> query=new LambdaQueryWrapper<ShipInboundPlanPlacementOptions>();
			if(plan.getInboundPlanId()!=null) {
				query.eq(ShipInboundPlanPlacementOptions::getInboundPlanId, plan.getInboundPlanId());
			}else {
				query.eq(ShipInboundPlanPlacementOptions::getInboundPlanId, plan.getId());
			}
			query.eq(ShipInboundPlanPlacementOptions::getPlacementOptionId,placementOptionId);
			query.eq(ShipInboundPlanPlacementOptions::getFormid, plan.getId());
			ShipInboundPlanPlacementOptions old = shipInboundPlanPlacementOptionsMapper.selectOne(query);
			old.setStatus("ACCEPTED");
			shipInboundPlanPlacementOptionsMapper.update(old, query);
			ShipInboundOperation result=new ShipInboundOperation();
			result.setOperationStatus("SUCCESS");
			result.setFormid(plan.getId());
	    	return result;
	    }
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ShipInboundOperation operation = iShipInboundOperationService.getOperation(auth,plan.getId() , "confirmPlacementOption");
		if(operation!=null) {
			return operation;
		}
		ConfirmPlacementOptionResponse response = iInboundApiHandlerService.confirmPlacementOption(auth,plan.getInboundPlanId(),placementOptionId);
		if(response!=null) {
			return iShipInboundOperationService.setOperationID(auth, plan.getId(), response.getOperationId());
		}else {
			return null;
		}
	}else {
		return null;
	}
}

@Override
public Shipment getShipment(String planid, String shipmentId) {
	ShipInboundPlan plan = shipInboundPlanV2Service.getById(planid);
	if(plan!=null) {
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ShipInboundShipment ship = this.getById(shipmentId);
		if(ship!=null) {
			Shipment response = new Shipment();
			response.setName(ship.getName());
			ShipmentDestination dest=new ShipmentDestination();
			dest.setWarehouseId(ship.getDestination());
			ShipInboundDestinationAddress mydest = shipInboundDestinationAddressMapper.selectById(ship.getDestination());
			Address address=new Address();
			address.setAddressLine1(mydest.getAddressLine1());
			address.setAddressLine2(mydest.getAddressLine2());
			address.setCity(mydest.getCity());
			address.setCompanyName(mydest.getCompanyName());
			address.setCountryCode(mydest.getCountryCode());
			address.setEmail(mydest.getEmail());
			address.setPhoneNumber(mydest.getPhoneNumber());
			address.setPostalCode(mydest.getPostalCode());
			address.setPhoneNumber(mydest.getPhoneNumber());
			dest.setAddress(address);
			response.setDestination(dest);
		}
		Shipment response = iInboundApiHandlerService.getShipment(auth, plan.getInboundPlanId(), shipmentId);
		if(response!=null) {
			return response;
		}else {
			return null;
		}
	}else {
		return null;
	}
}





@Override
public ListShipmentItemsResponse getshipmentItems(ShipmentItemsDTO dto) {
	// TODO Auto-generated method stub
	ShipInboundPlan plan = shipInboundPlanV2Service.getById(dto.getFormid());
	if(plan!=null) {
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ListShipmentItemsResponse response = iInboundApiHandlerService.listShipmentItems(auth, plan.getInboundPlanId(),dto.getShipmentid(),dto.getPageSize(),dto.getPaginationToken());
		if(response!=null) {
			return response;
		}else {
			return null;
		}
	}else {
		return null;
	}
}





@Override
public ListShipmentBoxesResponse listShipmentBoxes(ShipmentItemsDTO dto) {
	// TODO Auto-generated method stub
	ShipInboundPlan plan = shipInboundPlanV2Service.getById(dto.getFormid());
	if(plan!=null) {
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		  ListShipmentBoxesResponse response = iInboundApiHandlerService.listShipmentBoxes(auth, plan.getInboundPlanId(),dto.getShipmentid(),dto.getPageSize(),dto.getPaginationToken());
		if(response!=null) {
			return response;
		}else {
			return null;
		}
	}else {
		return null;
	}
}





@Override
public String saveShipment(UserInfo user, ShipInboundPlan inplan, List<String> shipmentids) {
	// TODO Auto-generated method stub
	List<ShipInboundItemVo> itemsVo = shipInboundItemV2Service.listByFormid(inplan.getId());
	Map<String,ShipInboundItemVo> map=new HashMap<String,ShipInboundItemVo>();
	for(ShipInboundItemVo item:itemsVo) {
		map.put(item.getSku(), item);
	}
	String placementOptionId=null;
	for(String shipmentid:shipmentids) {
		Shipment shipment = this.getShipment(inplan.getId(), shipmentid);
		ShipmentItemsDTO dto=new ShipmentItemsDTO();
		dto.setFormid(inplan.getId());
		dto.setGroupid(inplan.getGroupid());
		dto.setMarketplaceid(inplan.getMarketplaceid());
		dto.setShipmentid(shipment.getShipmentId());
		dto.setPageSize(1000);
		placementOptionId=shipment.getPlacementOptionId();
		ListShipmentItemsResponse items = this.getshipmentItems(dto);
		for(Item item:items.getItems()) {
			ShipInboundShipmentItem entity=new ShipInboundShipmentItem();
			entity.setId(amazonAuthorityService.getUUID());
			entity.setAsin(item.getAsin());
			entity.setExpiration(GeneralUtil.parseDate(item.getExpiration()));
			entity.setFnsku(item.getFnsku());
			entity.setLabelOwner(item.getLabelOwner());
			entity.setManufacturingLotCode(item.getManufacturingLotCode());
			entity.setSku(item.getMsku());
//			String prepinstructions="";
//			if(item.getPrepInstructions()!=null) {
//				for(PrepInstruction prep:item.getPrepInstructions()) {
//					entity.setPrepOwner(prep.getPrepOwner());
//					prepinstructions=prepinstructions+prep.toString();
//				}
//			}
			ShipInboundItemVo planitem = map.get(item.getMsku());
			entity.setQuantity(item.getQuantity());
			entity.setShipmentid(shipmentid);
			entity.setMsku(planitem.getMsku());
			LambdaQueryWrapper<ShipInboundShipmentItem> queryWrapper=new LambdaQueryWrapper<ShipInboundShipmentItem>();
			queryWrapper.eq(ShipInboundShipmentItem::getShipmentid, shipmentid);
			queryWrapper.eq(ShipInboundShipmentItem::getSku, entity.getSku());
			ShipInboundShipmentItem oldone = shipInboundShipmentItemV2Mapper.selectOne(queryWrapper);
			if(oldone!=null) {
				oldone.setAsin(item.getAsin());
				oldone.setExpiration(GeneralUtil.parseDate(item.getExpiration()));
				oldone.setFnsku(item.getFnsku());
				oldone.setLabelOwner(item.getLabelOwner());
				oldone.setManufacturingLotCode(item.getManufacturingLotCode());
				oldone.setSku(item.getMsku());
				oldone.setQuantity(item.getQuantity());
				oldone.setShipmentid(shipmentid);
				shipInboundShipmentItemV2Mapper.updateById(oldone);
			}else {
				shipInboundShipmentItemV2Mapper.insert(entity);
			}
		}
		ShipmentDestination dest = shipment.getDestination();
		ShipInboundDestinationAddress address=new ShipInboundDestinationAddress(dest);
		ShipInboundDestinationAddress old = shipInboundDestinationAddressMapper.selectById(address.getCode());
		if(old!=null) {
			shipInboundDestinationAddressMapper.updateById(address);
		}else {
			shipInboundDestinationAddressMapper.insert(address);
		}
	    ShipInboundShipment oldone = this.getById(shipmentid);
	    if(oldone==null) {
	    	ShipInboundShipment ship=new ShipInboundShipment();
			ship.setCarrier(null);
	    	ship.setDestination(address.getCode());
			ship.setReferenceid(shipment.getAmazonReferenceId());
			ship.setShipmentConfirmationId(shipment.getShipmentConfirmationId());
			ship.setShipmentid(shipment.getShipmentId());
			ship.setInboundplanid(inplan.getInboundPlanId());
			ship.setOpttime(new Date());
			ship.setTranstyle(inplan.getTranstype());
			ship.setName(shipment.getName());
			ship.setShipmentstatus(shipment.getStatus());
			ship.setAddressid(inplan.getSourceAddress());
			if(inplan.getSubmitbox()) {
				ship.setStatus(4);
			}else {
				ship.setStatus(3);	
			}
			ship.setPlacementOptionId(shipment.getPlacementOptionId());
			ship.setTransportationOptionId(shipment.getSelectedTransportationOptionId());
			ship.setFormid(inplan.getId());
			ship.setTranstyle(inplan.getTranstyle());
			ship.setCarrier(inplan.getShippingSolution());
			ship.setShipedDate(inplan.getShippingDate());
			if(shipment.getSelectedDeliveryWindow()!=null) {
			    	SelectedDeliveryWindow window = shipment.getSelectedDeliveryWindow();
			    	ship.setDeliveryWindowOptionId(window.getDeliveryWindowOptionId());
			 }
			ship.setInboundplanid(inplan.getInboundPlanId());
	    	this.save(ship);
	    }else {
	    	oldone.setDestination(address.getCode());
	    	oldone.setReferenceid(shipment.getAmazonReferenceId());
	    	oldone.setShipmentConfirmationId(shipment.getShipmentConfirmationId());
	    	oldone.setName(shipment.getName());
	    	oldone.setOpttime(new Date());
	    	oldone.setShipmentstatus(shipment.getStatus());
	    	oldone.setPlacementOptionId(shipment.getPlacementOptionId());
	    	oldone.setTransportationOptionId(shipment.getSelectedTransportationOptionId());
	    	oldone.setFormid(inplan.getId());
	    	oldone.setInboundplanid(inplan.getInboundPlanId());
	    	oldone.setTransportationOptionId(shipment.getSelectedTransportationOptionId());
	    	if(shipment.getSelectedDeliveryWindow()!=null) {
		    	SelectedDeliveryWindow window = shipment.getSelectedDeliveryWindow();
		    	oldone.setDeliveryWindowOptionId(window.getDeliveryWindowOptionId());
		     }
	    	this.updateById(oldone);
	    }
	    
	    if(shipment.getSelectedDeliveryWindow()!=null) {
	    	SelectedDeliveryWindow window = shipment.getSelectedDeliveryWindow();
	    	shipInboundShipmentDeliveryMapper.deleteById(window.getDeliveryWindowOptionId());
	    	ShipInboundShipmentDelivery deliveryWindow=new ShipInboundShipmentDelivery();
	    	deliveryWindow.setAvailabilityType(window.getAvailabilityType());
	    	deliveryWindow.setDeliveryWindowOptionId(window.getDeliveryWindowOptionId());
	    	deliveryWindow.setEditableUntil(AmzDateUtils.getDate(window.getEditableUntil()));
	    	deliveryWindow.setEndDate(AmzDateUtils.getDate(window.getEndDate()));
	    	deliveryWindow.setStartDate(AmzDateUtils.getDate(window.getStartDate()));
			shipInboundShipmentDeliveryMapper.insert(deliveryWindow);
	    }
	    ShipmentItemsDTO boxdto=new ShipmentItemsDTO();
	    boxdto.setFormid(inplan.getId());
	    boxdto.setShipmentid(shipmentid);
	    boxdto.setPageSize(1000);
		ListShipmentBoxesResponse boxs = this.listShipmentBoxes(boxdto);
		List<ShipInboundShipmentBox> boxlist = shipInboundShipmentBoxMapper.selectList(new LambdaQueryWrapper<ShipInboundShipmentBox>().eq(ShipInboundShipmentBox::getShipmentid, shipmentid));
		int i=0;
		Map<String,ShipInboundShipmentBox> boxMap=new HashMap<String,ShipInboundShipmentBox>();
		for(ShipInboundShipmentBox item:boxlist) {
			boxMap.put(item.getId(), item);
		}
		 
		for(Box box:boxs.getBoxes()) {
			ShipInboundShipmentBox entity=new ShipInboundShipmentBox();
			entity.setBoxnum(++i);
			entity.setId(box.getBoxId());
			entity.setPackingGroupId(box.getPackageId());
			entity.setFormid(inplan.getId());
			entity.setPlacementOptionId(shipment.getPlacementOptionId());
			entity.setShipmentid(shipmentid);
			entity.setHeight(box.getDimensions().getHeight());
			entity.setLength(box.getDimensions().getLength());
			entity.setWidth(box.getDimensions().getWidth());
			entity.setWeight(box.getWeight().getValue());
			entity.setUnit(box.getDimensions().getUnitOfMeasurement().getValue());
			entity.setWunit(box.getWeight().getUnit().getValue());
			if(boxMap.containsKey(box.getBoxId())) {
				ShipInboundShipmentBox oldbox=boxMap.get(box.getBoxId());
				entity.setTrackingId(oldbox.getTrackingId());
				entity.setSumsku(oldbox.getSumsku());
				entity.setPackageStatus(oldbox.getPackageStatus());
				shipInboundShipmentBoxMapper.updateById(entity);
				boxMap.remove(box.getBoxId());
			}else {
				shipInboundShipmentBoxMapper.insert(entity);
			}
			LambdaQueryWrapper<ShipInboundShipmentBoxItem> boxitemRemoveQuery=new LambdaQueryWrapper<ShipInboundShipmentBoxItem>();
			boxitemRemoveQuery.eq(ShipInboundShipmentBoxItem::getBoxid, entity.getId());
			shipInboundShipmentBoxItemMapper.delete(boxitemRemoveQuery);
			for(Item boxitem:box.getItems()) {
				ShipInboundShipmentBoxItem itementity=new ShipInboundShipmentBoxItem();
				itementity.setBoxid(entity.getId());
				itementity.setQuantity(boxitem.getQuantity());
				itementity.setLabelOwner(boxitem.getLabelOwner());
				itementity.setSku(boxitem.getMsku());
				itementity.setManufacturingLotCode(boxitem.getManufacturingLotCode());
				shipInboundShipmentBoxItemMapper.insert(itementity);
			}
		}
	    if(boxMap.size()>0) {
	       for(Entry<String, ShipInboundShipmentBox> entry:boxMap.entrySet()) {
	    	   String boxid=entry.getKey();
	    	   shipInboundShipmentBoxMapper.deleteById(boxid);
	       }
	    }
	}
	return placementOptionId;
	
}
	


@Override
public String getLabelUrl(UserInfo user,String shipmentid,String pagetype,String labelType,String pannum) {
	// TODO Auto-generated method stub
	ShipInboundShipment shipment=this.getById(shipmentid); 
	ShipInboundPlan inplan = shipInboundPlanV2Service.getById(shipment.getFormid());
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	if(inplan.getAuditstatus()==7) {
		if(!shipInboundBoxV2Service.hasSubmitPackage(inplan)) {
			throw new BizException("您还没有提交箱子信息，无法操作");
		}
	}
	if(shipment.getStatus()==4) {
		shipment.setStatus(5);
		shipment.setOperator(user.getId());
		shipment.setOpttime(new Date());
		this.updateById(shipment);
		shipInboundV2ShipmentRecordService.saveRecord(shipment);
	}
	List<ShipInboundShipmentBox> list = shipInboundShipmentBoxMapper.selectList(new LambdaQueryWrapper<ShipInboundShipmentBox>().eq(ShipInboundShipmentBox::getShipmentid, shipmentid));
	return iFulfillmentInboundService.getLabelDownloadUR(auth, shipment.getShipmentConfirmationId(),list.size(), pagetype,labelType,pannum);
}





@Override
public ShipInboundOperation generateDeliveryWindowOptions(String formid, String shipmentId) {
	// TODO Auto-generated method stub
	ShipInboundPlan inplan = shipInboundPlanV2Service.getById(formid);
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	ShipInboundShipment ship = this.getById(shipmentId);
	if(ship!=null&&ship.getDeliveryWindowOptionId()!=null) {
		return iShipInboundOperationService.getOperation(auth, formid, "generateDeliveryWindowOptions");
	}
	GenerateDeliveryWindowOptionsResponse response = this.iInboundApiHandlerService.generateDeliveryWindowOptions(auth, inplan.getInboundPlanId(), shipmentId);
	if(response!=null) {
		return iShipInboundOperationService.setOperationID(auth, inplan.getId(), response.getOperationId());
	}else {
		return null;
	}
}





@Override
public ListDeliveryWindowOptionsResponse listDeliveryWindowOptions(deliveryWindowDTO dto) {
	// TODO Auto-generated method stub
	ShipInboundPlan inplan = shipInboundPlanV2Service.getById(dto.getFormid());
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	ListDeliveryWindowOptionsResponse response = this.iInboundApiHandlerService.listDeliveryWindowOptions(auth,inplan.getInboundPlanId(), dto.getShipmentid(),dto.getPageSize(),dto.getPaginationToken());
	return response;
}





@Override
public ShipInboundOperation confirmDeliveryWindowOptions(deliveryWindowDTO dto) {
	// TODO Auto-generated method stub
	ShipInboundPlan inplan = shipInboundPlanV2Service.getById(dto.getFormid());
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	ConfirmDeliveryWindowOptionsResponse response = this.iInboundApiHandlerService.confirmDeliveryWindowOptions(auth,inplan.getInboundPlanId(), dto.getShipmentid(),dto.getDeliveryWindowOptionId());
	if(response!=null) {
		if(dto.getShipmentid()!=null) {
			ShipInboundShipment shipment = this.getById(dto.getShipmentid());
			if(shipment!=null) {
				shipment.setDeliveryWindowOptionId(dto.getDeliveryWindowOptionId());
				this.updateById(shipment);
			}
		}
		return iShipInboundOperationService.setOperationID(auth, inplan.getId(), response.getOperationId());
	}else {
		return null;
	}
}





@Override
public ShipInboundDestinationAddress getToAddress(String destination) {
	// TODO Auto-generated method stub
	return shipInboundDestinationAddressMapper.selectById(destination);
}





@Override
@Cacheable(value = "ShipmentStatusCache")
public String getShipmentStatusName(String shipmentstatus) {
	// TODO Auto-generated method stub
	return this.baseMapper.getShipmentStatusName(shipmentstatus);
}



@Override
public List<Map<String, Object>> findInboundItemByShipmentId(String shipmentid) {
	return shipInboundShipmentItemV2Mapper.selectByShipmentid(shipmentid);
}


@Override
public ShipInboundShipmenSummarytVo summaryShipmentItemWithoutItem(String shipmentid) {
	// TODO Auto-generated method stub
	ShipInboundShipmenSummarytVo itemsum = shipInboundShipmentItemV2Mapper.summaryShipmentItem(shipmentid);
	return itemsum;
}





@Override
public SummaryShipmentVo showPlanListByPlanid(String shipmentid) {
	// TODO Auto-generated method stub
	return this.shipInboundShipmentItemV2Mapper.selectitemList(shipmentid);
}


@Override
public ShipTransDetail getTransChannelInfo(String id) {
	return shipTransDetailMapper.selectById(id);
}

@Override
public List<ShipInboundShipmentBox> findShipInboundBoxByShipment(String shipmentid) {
	// TODO Auto-generated method stub
	QueryWrapper<ShipInboundShipmentBox> queryWrapper=new QueryWrapper<ShipInboundShipmentBox>();
	queryWrapper.eq("shipmentid", shipmentid);
	queryWrapper.orderByAsc("boxnum");
	List<ShipInboundShipmentBox> list = shipInboundShipmentBoxMapper.selectList(queryWrapper);
	return list;
}




@Override
public List<ShipInboundShipmentBoxItem> findShipInboundCaseByShipment(String shipmentid) {
	return shipInboundShipmentBoxItemMapper.findByShipment(shipmentid);
}


@Override
public List<Map<String, Object>> findShipInboundBox(String shipmentid) {
	return shipInboundShipmentBoxMapper.findShipInboundBox(shipmentid);
}


@Override
public Map<String, Object> getSelfTransDataView(String shipmentid) {
	return shipInboundTransMapper.getSelfTransDataView(shipmentid);
}




@Override
public ShipInboundOperation saveTransTrance(UserInfo user, ShipInboundShipment ship, List<Map<String, Object>> boxinfo) {
	// TODO Auto-generated method stub
	ShipInboundPlan inplan = shipInboundPlanV2Service.getById(ship.getFormid());
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	UpdateShipmentTrackingDetailsRequest body=new UpdateShipmentTrackingDetailsRequest();
	TrackingDetailsInput tracking=new TrackingDetailsInput();
	SpdTrackingDetailInput sptracking=new SpdTrackingDetailInput();
	List<SpdTrackingItemInput> spdTrackingItems=new ArrayList<SpdTrackingItemInput>();
	for(Map<String, Object> box:boxinfo) {
		SpdTrackingItemInput input=new SpdTrackingItemInput();
		String boxid=box.get("id").toString();
		String trackingid=box.get("value")!=null?box.get("value").toString():boxid;
		input.setBoxId(boxid);
		input.setTrackingId(trackingid);
		ShipInboundShipmentBox shipmentbox = shipInboundShipmentBoxMapper.selectById(boxid);
		shipmentbox.setTrackingId(trackingid);
		shipInboundShipmentBoxMapper.updateById(shipmentbox);
		spdTrackingItems.add(input);
	}
	sptracking.setSpdTrackingItems(spdTrackingItems);
	tracking.setSpdTrackingDetail(sptracking);
	body.setTrackingDetails(tracking);
	UpdateShipmentTrackingDetailsResponse response = this.iInboundApiHandlerService.updateShipmentTrackingDetails(auth,
			inplan.getInboundPlanId(), 
			ship.getShipmentid(),
			body);
	if(response!=null) {
		return iShipInboundOperationService.setOperationID(auth, inplan.getId(), response.getOperationId());
	}else {
		return null;
	}
}



@Override
public void setExcelBoxDetail(UserInfo user, SXSSFWorkbook workbook, String shipmentid) {
	Sheet sheet = workbook.createSheet("sheet1");
	List<LinkedHashMap<String, Object>> boxlist = this.baseMapper.findBoxDetailByShipmentId(shipmentid);
	Row row = sheet.createRow(0);
	int index = 0;
	if (boxlist == null || boxlist.size() == 0) {
		return;
	}
	Cell cell = row.createCell(0);
	cell.setCellValue("SKU");
	cell = row.createCell(1);
	cell.setCellValue("装箱数量");
	cell = row.createCell(2);
	cell.setCellValue("箱号");
	cell = row.createCell(3);
	cell.setCellValue("长度(cm)");
	cell = row.createCell(4);
	cell.setCellValue("宽度(cm)");
	cell = row.createCell(5);
	cell.setCellValue("高度(cm)");
	cell = row.createCell(6);
	cell.setCellValue("重量(Kg)");
	for (int i = 0; i < boxlist.size(); i++) {
		Row skurow = sheet.createRow(i + 1);
		Map<String, Object> skumap = boxlist.get(i);
		index = 0;
		for (String key : skumap.keySet()) {
			cell = skurow.createCell(index++);
			cell.setCellValue(skumap.get(key).toString());
		}
	}
}




@Override
public void updateFeeByShipment(List<ShipInboundShipmentItem> list) {
	// TODO Auto-generated method stub
	if(list!=null&&list.size()>0) {
		for(ShipInboundShipmentItem item:list) {
			ShipInboundShipmentItem old=shipInboundShipmentItemV2Mapper.selectById(item.getId());
			if(old!=null) {
				old.setUnittransfee(item.getUnittransfee());
				old.setUnitcost(item.getUnitcost());
				old.setTotalcost(item.getTotalcost());
				old.setTotaltransfee(item.getTotaltransfee());
				old.setOperator(item.getOperator());
				old.setOpttime(item.getOpttime());
				shipInboundShipmentItemV2Mapper.updateById(old);
			}
		}
	}
}

}




