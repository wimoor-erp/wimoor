package com.wimoor.amazon.inboundV2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.amazon.spapi.model.fulfillmentinbound.*;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Box;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmPlacementOptionResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Currency;
import com.amazon.spapi.model.fulfillmentinboundV20240320.*;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GeneratePlacementOptionsRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GeneratePlacementOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Incentive;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Item;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListPlacementOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.LtlTrackingDetailInput;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PlacementOption;
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
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.api.QuoteClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransHisMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransMapper;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmenSummaryDTO;
import com.wimoor.amazon.inbound.pojo.entity.AmzShipFulfillmentCenter;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundshipmentTraceupload;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.pojo.vo.SummaryShipmentVo;
import com.wimoor.amazon.inbound.service.*;
import com.wimoor.amazon.inboundV2.mapper.*;
import com.wimoor.amazon.inboundV2.pojo.dto.*;
import com.wimoor.amazon.inboundV2.pojo.entity.*;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.service.IShipInboundBoxService;
import com.wimoor.amazon.inboundV2.service.IShipInboundCaseService;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.amazon.inboundV2.service.*;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.inventory.service.IInventorySupplyService;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.api.entity.ShipmentBox;
import com.wimoor.quote.api.entity.ShipmentDestinationAddress;
import com.wimoor.quote.api.entity.ShipmentItem;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


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
	@Lazy
	@Autowired
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

	final IAmzShipFulfillmentCenterService iAmzShipFulfillmentCenterService;

	final ShipInboundPlanDeliveryOptionsMapper shipInboundPlanDeliveryOptionsMapper;
	final FileUpload fileUpload;
	@Resource
	private InventoryReportMapper inventoryReportMapper;

	@Autowired
	ErpClientOneFeignManager erpClientOneFeign;
	@Autowired
	QuoteClientOneFeignManager quoteClientOneFeign;
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
		if(transportation!=null&&transportation.isRunOrSucces()&&plan.getAuditstatus()>6) {
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
public AmzShipmentDTO getShipment(String planid, String shipmentId) {
	ShipInboundPlan plan = shipInboundPlanV2Service.getById(planid);
	if(plan!=null) {
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ShipInboundShipment ship = this.getById(shipmentId);
		if(ship!=null) {
			AmzShipmentDTO response=new AmzShipmentDTO();
			response.setName(ship.getName());
			AmzShipmentDestinationDTO dest=new AmzShipmentDestinationDTO();
			dest.setWarehouseId(ship.getDestination());
			response.setShipmentId(ship.getShipmentid());
			response.setShipmentConfirmationId(ship.getShipmentConfirmationId());
			response.setPlacementOptionId(ship.getPlacementOptionId());
			ShipInboundDestinationAddress mydest = shipInboundDestinationAddressMapper.selectById(ship.getDestination());
			AmzAddressDTO address=new AmzAddressDTO();
			address.setAddressLine1(mydest.getAddressLine1());
			address.setAddressLine2(mydest.getAddressLine2());
			address.setCity(mydest.getCity());
			address.setCompanyName(mydest.getCompanyName());
			address.setCountryCode(mydest.getCountryCode());
			address.setEmail(mydest.getEmail());
			address.setPhoneNumber(mydest.getPhoneNumber());
			address.setPostalCode(mydest.getPostalCode());
			address.setPhoneNumber(mydest.getPhoneNumber());
			AmzShipFulfillmentCenter center = iAmzShipFulfillmentCenterService.getByCode(ship.getDestination());
			if(center!=null){
				address.setArea(center.getArea());
				address.setIsfar(center.getIsfar());
			}
			dest.setAddress(address);
			dest.setWarehouseId(ship.getDestination());
			response.setDestination(dest);
			if(response.getShipmentConfirmationId()!=null){
				Map<String, Object> map = getSelfTransDataView(response.getShipmentConfirmationId());
				if(map!=null){
					StringBuffer channame= new StringBuffer();
					channame.append(map.get("transtyle")!=null?map.get("transtyle").toString().replace("\t\r",""):"");
					channame.append(map.get("subarea")!=null?"-"+map.get("subarea").toString().replace("\t\r",""):"");
					channame.append(map.get("channame")!=null?"-"+map.get("channame").toString().replace("\t\r",""):"");
					channame.append(map.get("channeltype")!=null?"-"+map.get("channeltype").toString().replace("\t\r",""):"");
					map.put("channame",channame.toString());
					response.setTransinfo(map);
				}
			}
			return response;
		}
		Shipment response = iInboundApiHandlerService.getShipment(auth, plan.getInboundPlanId(), shipmentId);
		AmzShipmentDTO result=new AmzShipmentDTO();
		BeanUtil.copyProperties(response,result );
		if(response!=null&&response.getDestination()!=null) {
			BeanUtil.copyProperties(response.getDestination(),result.getDestination() );
			if(response.getDestination().getAddress()!=null){
				BeanUtil.copyProperties(response.getDestination().getAddress(),result.getDestination().getAddress() );
			}
			AmzShipFulfillmentCenter center = iAmzShipFulfillmentCenterService.getByCode(response.getDestination().getWarehouseId());
			if(center!=null){
				result.getDestination().getAddress().setArea(center.getArea());
				result.getDestination().getAddress().setIsfar(center.getIsfar());
			}
			if(result.getShipmentConfirmationId()!=null){
				Map<String, Object> map = getSelfTransDataView(response.getShipmentConfirmationId());
				if(map!=null){
					StringBuffer channame= new StringBuffer();
					channame.append(map.get("transtyle")!=null?map.get("transtyle").toString().replace("\t\r",""):"");
					channame.append(map.get("subarea")!=null?"-"+map.get("subarea").toString().replace("\t\r",""):"");
					channame.append(map.get("channame")!=null?"-"+map.get("channame").toString().replace("\t\r",""):"");
					channame.append(map.get("channeltype")!=null?"-"+map.get("channeltype").toString().replace("\t\r",""):"");
					map.put("channame",channame.toString());
					result.setTransinfo(map);
				}
			}

			return result;
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
		ShipInboundShipment ship = this.getById(dto.getShipmentid());
		LambdaQueryWrapper<ShipInboundShipmentItem> query=new LambdaQueryWrapper<ShipInboundShipmentItem>();
		query.eq(ShipInboundShipmentItem::getShipmentid, dto.getShipmentid());
		List<ShipInboundShipmentItem> itemlist = shipInboundShipmentItemV2Mapper.selectList(query);
		if(ship!=null&&itemlist.size()>0) {
			ListShipmentItemsResponse response =new ListShipmentItemsResponse();
            for(ShipInboundShipmentItem item:itemlist){
				Item entity=new Item();
				entity.setAsin(item.getAsin());
				entity.setMsku(item.getSku());
				entity.setQuantity(item.getQuantity());
				entity.setLabelOwner(item.getLabelOwner());
				response.addItemsItem(entity);
			}
          return response;
		}
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
public String saveShipment(ShipInboundPlan inplan, List<String> shipmentids) {
	// TODO Auto-generated method stub
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	String placementOptionId=null;
	for(String shipmentid:shipmentids) {
        Shipment shipment=asyncShipmentList(auth,inplan,shipmentid);
		ShipmentItemsDTO dto=new ShipmentItemsDTO();
		dto.setFormid(inplan.getId());
		dto.setGroupid(inplan.getGroupid());
		dto.setMarketplaceid(inplan.getMarketplaceid());
		dto.setShipmentid(shipment.getShipmentId());
		dto.setPageSize(1000);
		asyncShipmentItem(dto);
		ShipmentItemsDTO boxdto=new ShipmentItemsDTO();
		boxdto.setFormid(inplan.getId());
		boxdto.setShipmentid(shipmentid);
		boxdto.setPageSize(1000);
		asyncShipmentBox( boxdto,shipment);
		placementOptionId=shipment.getPlacementOptionId();
	}
	if(inplan.getSubmitbox()){
		syncBoxId(inplan);
	}
	return placementOptionId;
}
private void asyncShipmentBox(ShipmentItemsDTO boxdto,Shipment shipment){
	ListShipmentBoxesResponse boxs = this.listShipmentBoxes(boxdto);
	List<ShipInboundShipmentBox> boxlist = shipInboundShipmentBoxMapper.selectList(new LambdaQueryWrapper<ShipInboundShipmentBox>().eq(ShipInboundShipmentBox::getShipmentid, boxdto.getShipmentid()));
	int i=0;
	Map<String,ShipInboundShipmentBox> boxMap=new HashMap<String,ShipInboundShipmentBox>();
	for(ShipInboundShipmentBox item:boxlist) {
		boxMap.put(item.getId(), item);
	}
	int  boxnum=1;
	for(Box box:boxs.getBoxes()) {
		ShipInboundShipmentBox entity=new ShipInboundShipmentBox();
		if(box.getBoxId()==null){
			String boxid=shipment.getShipmentConfirmationId()+"U"+String.format("%06d", boxnum);
			box.setBoxId(boxid);
		}
		boxnum++;
		entity.setId(box.getBoxId());
		entity.setPackingGroupId(box.getPackageId());
		entity.setFormid(boxdto.getFormid());
		entity.setPlacementOptionId(shipment.getPlacementOptionId());
		entity.setShipmentid(boxdto.getShipmentid());
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
			entity.setBoxnum(oldbox.getBoxnum());
			entity.setPackageStatus(oldbox.getPackageStatus());
			shipInboundShipmentBoxMapper.updateById(entity);
			boxMap.remove(box.getBoxId());
		}else {
			entity.setBoxnum(++i);
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
private void asyncShipmentItem(ShipmentItemsDTO dto){
	List<ShipInboundItemVo> itemsVo = shipInboundItemV2Service.listByFormid(dto.getFormid());
	Map<String,ShipInboundItemVo> map=new HashMap<String,ShipInboundItemVo>();
	for(ShipInboundItemVo item:itemsVo) {
		map.put(item.getSku(), item);
	}
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
		ShipInboundItemVo planitem = map.get(item.getMsku());
		entity.setQuantity(item.getQuantity());
		entity.setShipmentid(dto.getShipmentid());
		entity.setMsku(planitem.getMsku());
		LambdaQueryWrapper<ShipInboundShipmentItem> queryWrapper=new LambdaQueryWrapper<ShipInboundShipmentItem>();
		queryWrapper.eq(ShipInboundShipmentItem::getShipmentid, dto.getShipmentid());
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
			oldone.setShipmentid(dto.getShipmentid());
			shipInboundShipmentItemV2Mapper.updateById(oldone);
		}else {
			shipInboundShipmentItemV2Mapper.insert(entity);
		}
	}
}
private String saveDestinationAddress(Shipment shipment){
	if(shipment!=null&&shipment.getDestination()!=null) {
		ShipmentDestination dest = shipment.getDestination();
		ShipInboundDestinationAddress address = new ShipInboundDestinationAddress(dest);
		ShipInboundDestinationAddress old = shipInboundDestinationAddressMapper.selectById(address.getCode());
		if (old != null) {
			shipInboundDestinationAddressMapper.updateById(address);
		} else {
			shipInboundDestinationAddressMapper.insert(address);
		}
		return dest.getWarehouseId();
	}
	return null;
}
private String saveShipmentDelivery(Shipment shipment){
	if(shipment!=null&&shipment.getSelectedDeliveryWindow()!=null) {
		SelectedDeliveryWindow window = shipment.getSelectedDeliveryWindow();
		shipInboundShipmentDeliveryMapper.deleteById(window.getDeliveryWindowOptionId());
		ShipInboundShipmentDelivery deliveryWindow=new ShipInboundShipmentDelivery();
		deliveryWindow.setAvailabilityType(window.getAvailabilityType());
		deliveryWindow.setDeliveryWindowOptionId(window.getDeliveryWindowOptionId());
		deliveryWindow.setEditableUntil(AmzDateUtils.getDate(window.getEditableUntil()));
		deliveryWindow.setEndDate(AmzDateUtils.getDate(window.getEndDate()));
		deliveryWindow.setStartDate(AmzDateUtils.getDate(window.getStartDate()));
		shipInboundShipmentDeliveryMapper.insert(deliveryWindow);
		return deliveryWindow.getDeliveryWindowOptionId();
	}
	return null;
}
	public Shipment asyncShipmentList(AmazonAuthority auth,ShipInboundPlan inplan,String shipmentid) {
		Shipment shipment = iInboundApiHandlerService.getShipment(auth, inplan.getInboundPlanId(), shipmentid);
		String addressCode=saveDestinationAddress(shipment);
		String deliveryWindowOptionId=saveShipmentDelivery(shipment);
		ShipInboundShipment oldOne = this.getById(shipmentid);
		if(oldOne==null) {
			ShipInboundShipment ship=new ShipInboundShipment();
			ship.setCarrier(null);
			ship.setDestination(addressCode);
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
			if(shipment.getStatus()!=null&&shipment.getStatus().equals("CLOSED")){
				ship.setStatus(8);
			}
			if(shipment.getStatus()!=null&&shipment.getStatus().equals("CANCEL")){
				ship.setStatus(0);
			}
			ship.setPlacementOptionId(shipment.getPlacementOptionId());
			ship.setTransportationOptionId(shipment.getSelectedTransportationOptionId());
			ship.setFormid(inplan.getId());
			ship.setTranstyle(inplan.getTranstyle());
			ship.setCarrier(inplan.getShippingSolution());
			ship.setShipedDate(inplan.getShippingDate());
			ship.setDeliveryWindowOptionId(deliveryWindowOptionId);
			ship.setInboundplanid(inplan.getInboundPlanId());
			ship.setRefreshtime(new Date());
			if(inplan.getInvtype()==2 && ship.getStatus()!=8&& ship.getStatus()!=0){
				ship.setStatus(7);
			}
			this.save(ship);
		}else {
			oldOne.setDestination(addressCode);
			oldOne.setReferenceid(shipment.getAmazonReferenceId());
			oldOne.setShipmentConfirmationId(shipment.getShipmentConfirmationId());
			oldOne.setName(shipment.getName());
			oldOne.setOpttime(new Date());
			oldOne.setShipmentstatus(shipment.getStatus());
			oldOne.setPlacementOptionId(shipment.getPlacementOptionId());
			oldOne.setTransportationOptionId(shipment.getSelectedTransportationOptionId());
			oldOne.setFormid(inplan.getId());
			oldOne.setInboundplanid(inplan.getInboundPlanId());
			oldOne.setTransportationOptionId(shipment.getSelectedTransportationOptionId());
			oldOne.setDeliveryWindowOptionId(deliveryWindowOptionId);
			oldOne.setRefreshtime(new Date());
			if(shipment.getStatus()!=null&&shipment.getStatus().equals("CLOSED")){
				oldOne.setStatus(8);
			}
			this.updateById(oldOne);
		}

        return shipment;
	}

	@Override
	public List<ShipInboundShipment> listRefreshShipmentId(String id) {
		 return this.baseMapper.listRefreshShipment(id);
	}



	void syncBoxId(ShipInboundPlan inplan){
	LambdaQueryWrapper<ShipInboundBox> querybox=new LambdaQueryWrapper<ShipInboundBox>();
	querybox.eq(ShipInboundBox::getFormid, inplan.getId());
	List<ShipInboundBox> planboxlist = shipInboundBoxV2Mapper.selectList(querybox);
	if(planboxlist!=null){
		Map<String, HashSet<String>> boxIdMap=new HashMap<String,HashSet<String>>();
		LambdaQueryWrapper<ShipInboundShipmentBox> queryShipmentbox=new LambdaQueryWrapper<ShipInboundShipmentBox>();
		queryShipmentbox.eq(ShipInboundShipmentBox::getFormid, inplan.getId());
		List<ShipInboundShipmentBox> shipmentboxlist = shipInboundShipmentBoxMapper.selectList(queryShipmentbox);
		for(ShipInboundShipmentBox entity:shipmentboxlist){
			String key=entity.getHeight().setScale(2, RoundingMode.HALF_UP).toPlainString()
					+entity.getLength().setScale(2, RoundingMode.HALF_UP).toPlainString()
					+entity.getWidth().setScale(2, RoundingMode.HALF_UP).toPlainString()
					+entity.getWeight().setScale(2, RoundingMode.HALF_UP).toPlainString();
			LambdaQueryWrapper<ShipInboundShipmentBoxItem> boxitemQuery=new LambdaQueryWrapper<ShipInboundShipmentBoxItem>();
			boxitemQuery.eq(ShipInboundShipmentBoxItem::getBoxid, entity.getId());
			boxitemQuery.orderByAsc(ShipInboundShipmentBoxItem::getSku);
			List<ShipInboundShipmentBoxItem> shipmentBoxItemList = shipInboundShipmentBoxItemMapper.selectList(boxitemQuery);
			for(ShipInboundShipmentBoxItem itementity:shipmentBoxItemList){
				key=key+itementity.getSku()+itementity.getQuantity().toString();
			}
			key=key.replace(".","");
			HashSet<String> boxidlist = boxIdMap.get(key);
			if(boxidlist==null){
				boxidlist=new HashSet<String>();
				boxIdMap.put(key, boxidlist);
			}
			boxidlist.add(entity.getId());
			boxIdMap.put(key, boxidlist);
		}
		for(ShipInboundBox planbox:planboxlist){
			LambdaQueryWrapper<ShipInboundCase> querycase=new LambdaQueryWrapper<ShipInboundCase>();
			querycase.eq(ShipInboundCase::getBoxid, planbox.getId());
			querycase.orderByAsc(ShipInboundCase::getSku);
			List<ShipInboundCase> plancaselist = shipInboundCaseV2Mapper.selectList(querycase);
			String key=planbox.getHeight().setScale(2, RoundingMode.HALF_UP).toPlainString()
					+planbox.getLength().setScale(2, RoundingMode.HALF_UP).toPlainString()
					+planbox.getWidth().setScale(2, RoundingMode.HALF_UP).toPlainString()
					+planbox.getWeight().setScale(2, RoundingMode.HALF_UP).toPlainString();
			for(ShipInboundCase plancase:plancaselist){
				key=key+plancase.getSku()+plancase.getQuantity().toString();
			}
			key=key.replace(".","");
			HashSet<String> boxidlist = boxIdMap.get(key);
			if(boxidlist!=null){
				Iterator<String> iterator = boxidlist.iterator();
				String shipboxid=null;
				while(iterator.hasNext()) {
					String boxid=iterator.next();
					shipboxid=boxid;
					if(planbox.getShipmentid()==null){
						boxidlist.remove(boxid);
						break;
					}else{
						boxidlist.remove(planbox.getShipmentid());
						break;
					}

				}
				if(planbox.getShipmentid()==null){
					planbox.setShipmentid(shipboxid);
					shipInboundBoxV2Mapper.updateById(planbox);
					ShipInboundShipmentBox shipbox = shipInboundShipmentBoxMapper.selectById(shipboxid);
					if(shipbox!=null){
						shipbox.setBoxnum(planbox.getBoxnum());
						shipInboundShipmentBoxMapper.updateById(shipbox);
					}
				}
			}

		}
	}







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
	shipInboundPlanDeliveryOptionsMapper.delete(new LambdaQueryWrapper<ShipInboundPlanDeliveryOptions>().eq(ShipInboundPlanDeliveryOptions::getShipmentid,shipmentId));
	GenerateDeliveryWindowOptionsResponse response = this.iInboundApiHandlerService.generateDeliveryWindowOptions(auth, inplan.getInboundPlanId(), shipmentId);
	if(response!=null) {
		return iShipInboundOperationService.setOperationID(auth, inplan.getId(), response.getOperationId());
	}else {
		return null;
	}
}





@Override
public ListDeliveryWindowOptionsResponse listDeliveryWindowOptions(DeliveryWindowDTO dto) {
	// TODO Auto-generated method stub
	ShipInboundPlan inplan = shipInboundPlanV2Service.getById(dto.getFormid());
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	List<ShipInboundPlanDeliveryOptions> list = shipInboundPlanDeliveryOptionsMapper.selectList(new LambdaQueryWrapper<ShipInboundPlanDeliveryOptions>().eq(ShipInboundPlanDeliveryOptions::getShipmentid, dto.getShipmentid()));
	if(list!=null&&list.size()>0){
		ListDeliveryWindowOptionsResponse response = new ListDeliveryWindowOptionsResponse();
		List<DeliveryWindowOption> mylist=new LinkedList<DeliveryWindowOption>();
		for(ShipInboundPlanDeliveryOptions item:list){
			DeliveryWindowOption deliveryWindowOption=new DeliveryWindowOption();
			deliveryWindowOption.setDeliveryWindowOptionId(item.getDeliveryWindowOptionId());
			deliveryWindowOption.setAvailabilityType(item.getAvailabilityType());
			deliveryWindowOption.setStartDate(AmzDateUtils.getOffsetDateTimeUTC(item.getStartDate()));
			deliveryWindowOption.setEndDate(AmzDateUtils.getOffsetDateTimeUTC(item.getEndDate()));
			deliveryWindowOption.setValidUntil(AmzDateUtils.getOffsetDateTimeUTC(item.getEditableUntil()));
			mylist.add(deliveryWindowOption);
		}
		response.setDeliveryWindowOptions(mylist);
		return response;
	}
	ListDeliveryWindowOptionsResponse response = this.iInboundApiHandlerService.listDeliveryWindowOptions(auth,inplan.getInboundPlanId(), dto.getShipmentid(),dto.getPageSize(),dto.getPaginationToken());

	if(response!=null) {
		if(dto.getShipmentid()!=null) {
			shipInboundPlanDeliveryOptionsMapper.delete(new LambdaQueryWrapper<ShipInboundPlanDeliveryOptions>().eq(ShipInboundPlanDeliveryOptions::getShipmentid,dto.getShipmentid()));
			for(DeliveryWindowOption deliveryWindowOption:response.getDeliveryWindowOptions()){
				ShipInboundPlanDeliveryOptions option=new ShipInboundPlanDeliveryOptions();
				option.setShipmentid(dto.getShipmentid());
				option.setDeliveryWindowOptionId(deliveryWindowOption.getDeliveryWindowOptionId());
				option.setEditableUntil(AmzDateUtils.getDate(deliveryWindowOption.getValidUntil()));
				option.setEndDate(AmzDateUtils.getDate(deliveryWindowOption.getEndDate()));
				option.setStartDate(AmzDateUtils.getDate(deliveryWindowOption.getStartDate()));
                option.setAvailabilityType(deliveryWindowOption.getAvailabilityType());
				shipInboundPlanDeliveryOptionsMapper.insert(option);
			}
		}
	}
	return response;
}





@Override
public ShipInboundOperation confirmDeliveryWindowOptions(DeliveryWindowDTO dto) {
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
	ShipInboundDestinationAddress address = shipInboundDestinationAddressMapper.selectById(destination);
	AmzShipFulfillmentCenter center=iAmzShipFulfillmentCenterService.getByCode(destination);
	if(address != null&&center!=null){
		address.setArea(center.getArea());
		address.setIsfar(center.getIsfar());
	}
	return address;
}





@Override
@Cacheable(value = "ShipmentStatusCache")
public String getShipmentStatusName(String shipmentstatus) {
	// TODO Auto-generated method stub
	return this.baseMapper.getShipmentStatusName(shipmentstatus);
}



@Override
public List<Map<String, Object>> findInboundItemByShipmentId(String shipmentid) {
	List<Map<String, Object>> itemvoList=shipInboundShipmentItemV2Mapper.selectByShipmentid(shipmentid);
	List<String> skulist=itemvoList.stream().map(itemvo->itemvo.get("msku").toString()).collect(Collectors.toList());
	PlanDTO dto = new PlanDTO();
	dto.setMskulist(skulist);
	Result<List<Map<String, Object>>> listResult = erpClientOneFeign.getMskuInventory(dto);
	Map<String,Map<String,Object>> skuInfo=listResult.getData().stream().collect(Collectors.toMap(item->item.get("sku").toString(), item->item));
	for(Map<String,Object> itemvo:itemvoList) {
		Map<String, Object> mskuinfo = skuInfo.get(itemvo.get("msku").toString());
		if(mskuinfo!=null) {
			if(mskuinfo.get("image")!=null){
				itemvo.put("image",mskuinfo.get("image").toString());
			}
		}
	}
	return itemvoList;
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
public Map<String,Object> getTransChannelInfo(String id) {
	return erpClientOneFeign.getShipTransChannelDetial(id);
}

@Override
public List<ShipInboundShipmentBox> findShipInboundBoxByShipment(String shipmentid) {
	// TODO Auto-generated method stub
	QueryWrapper<ShipInboundShipmentBox> queryWrapper=new QueryWrapper<ShipInboundShipmentBox>();
	queryWrapper.eq("shipmentid", shipmentid);
	queryWrapper.orderByAsc("id");
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



public ShipInboundOperation handleUpdateTraceInfo(ShipInboundShipment ship,List<ShipInboundShipmentBox> boxinfo){
		ShipInboundPlan inplan = shipInboundPlanV2Service.getById(ship.getFormid());
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
		UpdateShipmentTrackingDetailsRequest body=new UpdateShipmentTrackingDetailsRequest();
		TrackingDetailsInput tracking=new TrackingDetailsInput();
	   if(ship.getTranstyle().equals("SP")){
		SpdTrackingDetailInput sptracking=new SpdTrackingDetailInput();
		List<SpdTrackingItemInput> spdTrackingItems=new ArrayList<SpdTrackingItemInput>();
		for(ShipInboundShipmentBox box:boxinfo) {
			SpdTrackingItemInput input=new SpdTrackingItemInput();
			String boxid=box.getId();
			String trackingid=box.getTrackingId();
			input.setBoxId(boxid);
			if(StrUtil.isNotBlank(trackingid) && !trackingid.equals("null")){
				input.setTrackingId(trackingid);
			}else{
				input.setTrackingId(ship.getShipmentConfirmationId());
			}
			spdTrackingItems.add(input);
		}
		sptracking.setSpdTrackingItems(spdTrackingItems);
		tracking.setSpdTrackingDetail(sptracking);
	}
	if (ship.getTranstyle().equals("LTL")) {
		LtlTrackingDetailInput ltlTrackingDetailInput=new LtlTrackingDetailInput();
		ltlTrackingDetailInput.setBillOfLadingNumber(ship.getShipmentConfirmationId());
		String trackingid=boxinfo!=null&&boxinfo.size()>0?boxinfo.get(0).getTrackingId():null;
		if(StrUtil.isNotBlank(trackingid) && !trackingid.equals("null")){
			ltlTrackingDetailInput.setFreightBillNumber(Arrays.asList(trackingid));
		}else{
			ltlTrackingDetailInput.setFreightBillNumber(Arrays.asList(ship.getShipmentConfirmationId()));
		}
		tracking.setLtlTrackingDetail(ltlTrackingDetailInput);
	}

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
public ShipInboundOperation saveTransTrance(UserInfo user, ShipInboundShipment ship, List<Map<String, Object>> boxinfo) {
	// TODO Auto-generated method stub
	List<ShipInboundShipmentBox> boxInfParam=new ArrayList<ShipInboundShipmentBox>();
	for(Map<String, Object> box:boxinfo) {
		SpdTrackingItemInput input=new SpdTrackingItemInput();
		String boxid=box.get("id").toString();
		String trackingid=box.get("value")!=null?box.get("value").toString():null;
		if(StrUtil.isNotBlank(trackingid) && !trackingid.equals("null")){
			ShipInboundShipmentBox shipmentbox = shipInboundShipmentBoxMapper.selectById(boxid);
			shipmentbox.setTrackingId(trackingid);
			shipInboundShipmentBoxMapper.updateById(shipmentbox);
			boxInfParam.add(shipmentbox);
		}else{
			ShipInboundShipmentBox shipmentbox = shipInboundShipmentBoxMapper.selectById(boxid);
			shipmentbox.setTrackingId(ship.getShipmentConfirmationId());
			boxInfParam.add(shipmentbox);
		}
	}
	return handleUpdateTraceInfo(ship,boxInfParam);
}

	public void setExcelBox(UserInfo user, SXSSFWorkbook workbook, String shipmentid) {
		Sheet sheet = workbook.createSheet("sheet1");
		List<LinkedHashMap<String, Object>> boxlist = this.baseMapper.findBoxByShipmentId(shipmentid);
		Row row = sheet.createRow(0);
		ShipInboundShipment shipment = this.getById(shipmentid);
		Map<String, Object> transinfo = getSelfTransDataView(shipment.getShipmentConfirmationId());
		if (transinfo != null) {
			StringBuilder bf = new StringBuilder();
			if (transinfo.get("company") != null) {
				bf.append("["+transinfo.get("company").toString()+"]");
			}
			if (transinfo.get("channeltype") != null) {
				bf.append(transinfo.get("channeltype").toString());
			}
			if (transinfo.get("transtyle") != null) {
				bf.append(transinfo.get("transtyle").toString());
			}
			if (transinfo.get("channame") != null) {
				bf.append(transinfo.get("channame").toString());
			}
			if (transinfo.get("subarea") != null) {
				bf.append(transinfo.get("subarea").toString());
			}
			Cell cell = row.createCell(0);
			cell.setCellValue(bf.toString());
		}

		row = sheet.createRow(1);
		int index = 0;
		if (boxlist == null || boxlist.size() == 0) {
			return;
		}
		Cell cell = row.createCell(0);
		cell.setCellValue("箱子ID");
		cell = row.createCell(1);
		cell.setCellValue("箱号");
		cell = row.createCell(2);
		cell.setCellValue("重量(Kg)");
		cell = row.createCell(3);
		cell.setCellValue("长度(cm)");
		cell = row.createCell(4);
		cell.setCellValue("宽度(cm)");
		cell = row.createCell(5);
		cell.setCellValue("高度(cm)");

		for (int i = 0; i < boxlist.size(); i++) {
			Row skurow = sheet.createRow(i + 2);
			Map<String, Object> skumap = boxlist.get(i);
			index = 0;
			for (String key : skumap.keySet()) {
				cell = skurow.createCell(index++);
				cell.setCellValue(skumap.get(key).toString());
			}
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
	cell.setCellValue("重量(Kg)");
	cell = row.createCell(4);
	cell.setCellValue("长度(cm)");
	cell = row.createCell(5);
	cell.setCellValue("宽度(cm)");
	cell = row.createCell(6);
	cell.setCellValue("高度(cm)");

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


	@Override
	public void toQuote(UserInfo user,String token,String shipmentId) {
		ShipInboundShipment shipment = this.getById(shipmentId);
		if(StrUtil.isBlank(token)){
			return ;
		}
		if(shipment.getIsquote()==null|| !shipment.getIsquote()) {
			shipment.setIsquote(true);
		}
			ShipInboundShipmenSummarytVo data = summaryShipmentItemWithoutItem(shipment.getShipmentid());
			ShipInboundPlan plan = shipInboundPlanV2Service.getById(shipment.getFormid());
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
			String marketName =  market.getName();
			QuoteDTO dto=new QuoteDTO();
			dto.setToken(token);
			com.wimoor.quote.api.entity.Shipment shipmentdto=new com.wimoor.quote.api.entity.Shipment();
			shipmentdto.setShipmentid(shipment.getShipmentConfirmationId());
			shipmentdto.setName(shipment.getName());
		    shipmentdto.setNumber(plan.getNumber());
			shipmentdto.setDestination(shipment.getDestination());
		    AmzShipFulfillmentCenter center = this.iAmzShipFulfillmentCenterService.getByCode(shipment.getDestination());
			if(center!=null){
				if(center.getArea()!=null){
					shipmentdto.setArea(center.getArea());
				}
				if(center.getIsfar()!=null&&center.getIsfar()){
					shipmentdto.setIsfar(true);
				}
			}
		    shipmentdto.setStatus(0);
			shipmentdto.setRemark(shipment.getRemark());
			shipmentdto.setGroupname(data.getGroupname());
			if(user.getUserinfo()!=null){
				if(user.getUserinfo().get("name")!=null){
					shipmentdto.setBuyername(user.getUserinfo().get("name").toString());
				}
				if(user.getUserinfo().get("companyname")!=null){
					shipmentdto.setBuyercompany(URLDecoder.decode(user.getUserinfo().get("companyname").toString(), Charset.defaultCharset()) );
				}
			}
			shipmentdto.setWarehousename(data.getWarehouse());
			shipmentdto.setCountry(marketName);
			List<ShipInboundShipmentBox> boxlist = findShipInboundBoxByShipment(shipment.getShipmentid());
			List<ShipmentBox> dtoBoxlist=new ArrayList<ShipmentBox>();
			BigDecimal weight=BigDecimal.ZERO;
			BigDecimal volume=BigDecimal.ZERO;
			for(ShipInboundShipmentBox box:boxlist) {
				ShipmentBox dtoBox = new ShipmentBox();
				dtoBox.setBoxid(box.getId());
				dtoBox.setShipmentid(shipmentdto.getShipmentid());
				dtoBox.setLength(box.getLength());
				dtoBox.setWidth(box.getWidth());
				dtoBox.setHeight(box.getHeight());
				dtoBox.setWeight(box.getWeight());
				dtoBox.setWunit(box.getWunit());
				dtoBox.setUnit(box.getUnit());
				dtoBoxlist.add(dtoBox);
				if (box.getWeight() != null) {
					weight = weight.add(box.getWeight());
				}
				if (box.getHeight() != null) {
					BigDecimal length = box.getLength();
					BigDecimal width = box.getWidth();
					BigDecimal height = box.getHeight();
					BigDecimal singlevolume=length.multiply(width.multiply(height));
					volume = volume.add(singlevolume);
				}
			}
			shipmentdto.setWeight(weight);
			shipmentdto.setVolume(volume);
			List<Map<String, Object>> itemlist =  findInboundItemByShipmentId(shipmentId);
				List<String> mskulist=new LinkedList<String>();
				for (int i = 0; i < itemlist.size(); i++) {
					Map<String, Object> tempmap = itemlist.get(i);
					mskulist.add(tempmap.get("msku").toString());
				}
				Result<Map<String, Object>> mskuInfoResult = erpClientOneFeign.findMaterialMapBySku(mskulist);
				Map<String,Object> mskuInfo=null;
				if(Result.isSuccess(mskuInfoResult)&&mskuInfoResult.getData()!=null) {
					mskuInfo=mskuInfoResult.getData();
				}
				shipmentdto.setNum(itemlist.size());
				List<ShipmentItem> dtoitemlist=new ArrayList<ShipmentItem>();
				for (int i = 0; i < itemlist.size(); i++) {
					Map<String, Object> tempmap = itemlist.get(i);
					Integer quantity = 0;
					String msku=tempmap.get("msku").toString();
					if(mskuInfo!=null&&mskuInfo.get(msku)!=null) {
						Map<String, Object> mskuInfoItem = (Map<String,Object>)mskuInfo.get(msku);
						if(mskuInfoItem.get("image")!=null) {
							tempmap.put("image",fileUpload.getPictureImage(mskuInfoItem.get("image")) );
						}
					}
					if (tempmap.get("Quantity") != null) {
						quantity = Integer.parseInt(tempmap.get("Quantity").toString());
					}
					ShipmentItem dtoitem=new ShipmentItem();
					dtoitem.setId(tempmap.get("id").toString());
					dtoitem.setSku(tempmap.get("sku").toString());
					dtoitem.setQuantity(quantity);
					if(tempmap.get("image")!=null) {
						dtoitem.setImage(tempmap.get("image").toString());
					}
					Map<String, Object> customs = erpClientOneFeign.getCustomsAction(msku, market.getMarket());
					if(customs!=null) {
						dtoitem.setName(customs.get("cname")!=null?customs.get("cname").toString():null);
						dtoitem.setCode(customs.get("code")!=null?customs.get("code").toString():null);
						dtoitem.setEname(customs.get("ename")!=null?customs.get("ename").toString():null);
						dtoitem.setMaterial(customs.get("material")!=null?customs.get("material").toString():null);
					}
					dtoitemlist.add(dtoitem);
				 }
			shipmentdto.setItemList(dtoitemlist);
			shipmentdto.setBoxList(dtoBoxlist);
			ShipmentDestinationAddress dtoaddress=new ShipmentDestinationAddress();
			ShipInboundDestinationAddress toaddress = this.getToAddress(shipment.getDestination());
			dtoaddress.setCode(shipment.getDestination());
			dtoaddress.setCompanyName(toaddress.getCompanyName());
			dtoaddress.setCountryCode(toaddress.getCountryCode());
			dtoaddress.setEmail(toaddress.getEmail());
			dtoaddress.setName(toaddress.getName());
			dtoaddress.setPhoneNumber(toaddress.getPhoneNumber());
			dtoaddress.setPostalCode(toaddress.getPostalCode());
			dtoaddress.setStateOrProvinceCode(toaddress.getStateOrProvinceCode());
			dtoaddress.setAddressLine1(toaddress.getAddressLine1());
			dtoaddress.setAddressLine2(toaddress.getAddressLine2());
			dtoaddress.setCity(toaddress.getCity());
			shipmentdto.setAddress(dtoaddress);
			if(shipmentdto.getArea() ==null&&dtoaddress.getCountryCode().equals("US")){
	              if(dtoaddress.getPostalCode()!=null){
					  if(dtoaddress.getPostalCode().indexOf("8")==0
					  ||dtoaddress.getPostalCode().indexOf("9")==0){
						  shipmentdto.setArea("美西");
					  }
					  if(dtoaddress.getPostalCode().indexOf("4")==0
							  ||dtoaddress.getPostalCode().indexOf("5")==0
							  ||dtoaddress.getPostalCode().indexOf("6")==0
							  ||dtoaddress.getPostalCode().indexOf("7")==0){
						  shipmentdto.setArea("美中");
					  }
					  if(dtoaddress.getPostalCode().indexOf("0")==0
							  ||dtoaddress.getPostalCode().indexOf("1")==0
							  ||dtoaddress.getPostalCode().indexOf("2")==0
							  ||dtoaddress.getPostalCode().indexOf("3")==0){
						  shipmentdto.setArea("美东");
					  }

				  }
			}
			dto.setShipments(Arrays.asList(shipmentdto));
			quoteClientOneFeign.saveAction(dto);
			updateById(shipment);
		}

	@Override
	public void handleShipmentV2Trace(ShipInboundShipment shipmentV2) {
		   List<ShipInboundShipmentBox> boxinfo = shipInboundShipmentBoxMapper.selectList(new LambdaQueryWrapper<ShipInboundShipmentBox>().eq(ShipInboundShipmentBox::getShipmentid, shipmentV2.getShipmentid()));
		  handleUpdateTraceInfo(shipmentV2,boxinfo);
	}

	@Override
	public void handlerResult(AmazonAuthority auth, Marketplace market, GetShipmentsResponse result) {
		if(result==null||result.getPayload()==null)return ;
		GetShipmentsResult shipmentsResult = result.getPayload();
		InboundShipmentList data = shipmentsResult.getShipmentData();
		for (InboundShipmentInfo item:data) {
			String shipmentid = item.getShipmentId();
			 ShipInboundShipment shipment = this.baseMapper.selectOne(new LambdaQueryWrapper<ShipInboundShipment>().eq(ShipInboundShipment::getShipmentConfirmationId, shipmentid));
			if(shipment!=null) {
				if(item.getShipmentStatus()!=null) {
					shipment.setShipmentstatus(item.getShipmentStatus().getValue());
				}
				BoxContentsFeeDetails boxfee = item.getEstimatedBoxContentsFee();
				if (boxfee != null) {
					if(boxfee.getTotalFee()!=null) {
						shipment.setTotalfee(boxfee.getTotalFee().getValue());
						if(boxfee.getTotalFee().getCurrencyCode()!=null) {
							shipment.setCurrency(boxfee.getTotalFee().getCurrencyCode().getValue());
						}
					}
					shipment.setTotalunits(boxfee.getTotalUnits());
				}
				if ("CLOSED".equals(item.getShipmentStatus().getValue())) {
					if(shipment.getStatus()!=8){
						shipment.setStatus(8);
						shipment.setClosedDate(new Date());
						shipInboundV2ShipmentRecordService.saveRecord(shipment);
					}
				}else if ("CANCELLED".equals(item.getShipmentStatus().getValue())||
						"DELETED".equals(item.getShipmentStatus().getValue())) {
					if(shipment.getStatus()!=0){
						shipment.setStatus(0);
						shipment.setClosedDate(new Date());
						shipInboundV2ShipmentRecordService.saveRecord(shipment);
					}
				}else if("IN_TRANSIT".equals(item.getShipmentStatus().getValue())&&shipment.getStatus()<5){
						shipment.setStatus(5);
						shipInboundV2ShipmentRecordService.saveRecord(shipment);
				}else if("RECEIVING".equals(item.getShipmentStatus().getValue())&&shipment.getStatus()<7){
						shipment.setStatus(7);
						shipInboundV2ShipmentRecordService.saveRecord(shipment);
				}else if("SHIPPED".equals(item.getShipmentStatus().getValue())&&shipment.getStatus()<5){
					shipment.setStatus(5);
					shipInboundV2ShipmentRecordService.saveRecord(shipment);
				}

				shipment.setRefreshtime(new Date());
				this.baseMapper.updateById(shipment);
			}
		}

	}
}




