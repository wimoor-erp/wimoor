package com.wimoor.amazon.inbound.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.inbound.pojo.dto.ShipCartShipmentDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmenSummaryDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundItemDTO;
import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.api.erp.ship.pojo.dto.ShipInboundTransDTO;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货订单接口")
@RestController
@RequestMapping("/api/v1/shipForm")
@SystemControllerLog("发货订单")
@RequiredArgsConstructor
public class ShipFormController {
	
	final IShipInboundItemService shipInboundItemService;
	final IShipInboundShipmentService shipInboundShipmentService;
	final ISerialNumService serialNumService;
    final IShipInboundPlanService shipInboundPlanService;
    final IShipInboundTransService shipInboundTransService;
    
	@ApiOperation(value = "获取所有本地货件列表")
	@PostMapping("/list")
	public Result<IPage<ShipInboundShipmenSummarytVo>> findByTraceConditionAction(@ApiParam("配货单列表申请")@RequestBody ShipInboundShipmenSummaryDTO dto) {
		    UserInfo user=UserInfoContext.get();
		    Map<String,Object> param=new HashMap<String,Object>();
		    param.put("auditstatus", StrUtil.isBlank(dto.getAuditstatus())?null:dto.getAuditstatus());
		    param.put("shipmentid", StrUtil.isBlank(dto.getShipmentid())?null:dto.getShipmentid());
		    param.put("marketplaceid", StrUtil.isBlank(dto.getMarketplaceid())?null:dto.getMarketplaceid());
		    param.put("groupid", StrUtil.isBlank(dto.getGroupid())?null:dto.getGroupid());
		    param.put("warehouseid", StrUtil.isBlank(dto.getWarehouseid())?null:dto.getWarehouseid());
		    param.put("company", StrUtil.isBlank(dto.getCompany())?null:dto.getCompany());
		    param.put("channel", StrUtil.isBlank(dto.getChannel())?null:dto.getChannel());
		    param.put("transtype", StrUtil.isBlank(dto.getTranstype())?null:dto.getTranstype());
		    param.put("searchtype", StrUtil.isBlank(dto.getSearchtype())?null:dto.getSearchtype());
		    param.put("search", StrUtil.isBlank(dto.getSearch())?null:dto.getSearch());
		    param.put("fromdate", StrUtil.isBlank(dto.getFromdate())?null:DateUtil.parseDateTime(dto.getFromdate()+" 00:00:00.000"));
			param.put("enddate", StrUtil.isBlank(dto.getEnddate())?null:DateUtil.parseDateTime(dto.getEnddate()+" 23:59:59.999"));
		    param.put("hasexceptionnum", StrUtil.isBlank(dto.getHasexceptionnum())?null:dto.getHasexceptionnum());
		    param.put("shopid", user.getCompanyid());
		    IPage<ShipInboundShipmenSummarytVo> shiplist=shipInboundShipmentService.findByTraceCondition(dto.getPage(), param);
	        return Result.success(shiplist);
	    }
 

	@ApiOperation(value = "获取货件信息")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/info/{shipmentid}")
	public Result<ShipInboundShipmenSummarytVo> infoAction(@PathVariable("shipmentid") String shipmentid) {
		    ShipInboundShipmenSummarytVo data = shipInboundItemService.summaryShipmentItem(shipmentid);
		    ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		    BeanUtil.copyProperties(shipment, data);
	        return Result.success(data);
	}
	
	@ApiOperation(value = "获取URL下载箱子标签")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/getPkgLabelUrl/{shipmentid}/{pagetype}")
	public Result<String> getPkgLabelUrlAction(@PathVariable("shipmentid") String shipmentid,@PathVariable("pagetype") String pagetype) {
		    String data = shipInboundShipmentService.getPkgLabelUrl(shipmentid,pagetype);
	        return Result.success(data);
	}
	
	@ApiOperation(value = "获取货件信息【ERP内部调用,也可以用于显示货件】")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/getShipment/{shipmentid}")
	public Result<ShipInboundShipmentDTO> getShipmentidAction(@PathVariable("shipmentid") String shipmentid) {
		      ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		      ShipInboundPlan inplanv = shipInboundPlanService.getById(shipment.getInboundplanid());
		      ShipInboundShipmentDTO shipmentdto=new ShipInboundShipmentDTO();
		      ShipInboundPlanDTO plandto=new ShipInboundPlanDTO();
			  BeanUtil.copyProperties(shipment, shipmentdto);
			  BeanUtil.copyProperties(inplanv, plandto);
			  shipmentdto.setInboundplan(plandto);
			  List<ShipInboundItem> list = shipInboundItemService.getItemByShipment(shipmentid);
			  List<ShipInboundItemVo> volist = shipInboundItemService.listByShipmentid(shipmentid);
			  Map<String,String> skuMaterialMap=new HashMap<String,String>();
			  for(ShipInboundItemVo voitem:volist) {
				  skuMaterialMap.put(voitem.getSellersku(), voitem.getMaterialid());
			  }
			  List<ShipInboundItemDTO> itemlist=new LinkedList<ShipInboundItemDTO>();
			  list.stream().forEach(item->{
				  ShipInboundItemDTO dto=new ShipInboundItemDTO();
				  if(StrUtil.isBlank(item.getMaterialid())) {
					  item.setMaterialid(skuMaterialMap.get(item.getSellersku()));
				  }
				  BeanUtil.copyProperties(item, dto);
				  itemlist.add(dto);
			  });
			  shipmentdto.setItemList(itemlist);
	          return Result.success(shipmentdto);
	}
	
	private ShipInboundShipment transShipmentDTO2Entity(ShipInboundShipmentDTO dto) {
		ShipInboundPlan inplanparam = new ShipInboundPlan();
		BeanUtil.copyProperties(dto.getInboundplan(), inplanparam);
		ShipInboundShipment shipment=new ShipInboundShipment();
		BeanUtil.copyProperties(dto, shipment);
		List<ShipInboundItemDTO> list = dto.getItemList();
		List<ShipInboundItem> itemlist = new ArrayList<ShipInboundItem>();
		for(ShipInboundItemDTO itemdto:list) {
			ShipInboundItem item=new ShipInboundItem();
			BeanUtil.copyProperties(itemdto, item);
			itemlist.add(item);
		}
		shipment.setItemList(itemlist);
		shipment.setInboundplan(inplanparam);
	    return shipment;
	}
	
	@ApiOperation(value = "创建货件【ERP内部调用】")
	@PostMapping("/create")
	public Result<String> createShipmentAction(@ApiParam("货件信息")@RequestBody ShipInboundShipmentDTO dto) {
	        String result=shipInboundShipmentService.createInboundShipment(transShipmentDTO2Entity(dto));
	        return Result.success(result);
	}
	
	@ApiOperation(value = "更新货件【ERP内部调用】")
	@PostMapping("/update")
	public Result<String> updateShipmentAction(@ApiParam("货件信息")@RequestBody ShipInboundShipmentDTO dto) {
		    String result=shipInboundShipmentService.updateInboundShipment(transShipmentDTO2Entity(dto));
	        return Result.success(result);
	}
	
	@ApiOperation(value = "提交箱子信息")
	@PostMapping("/cart")
	public Result<String> saveCartShipmentAction(@ApiParam("货件信息")@RequestBody ShipCartShipmentDTO dto) {
		    UserInfo user=UserInfoContext.get();
		    String result=shipInboundShipmentService.saveCartShipment(user,dto);
	        return Result.success(result);
	}
	
	@ApiOperation(value = "提交物流信息")
	@PostMapping("/trans")
	public Result<String> saveTransAction(@ApiParam("货件信息")@RequestBody ShipInboundTransDTO dto) {
		    ShipInboundTrans trans = new ShipInboundTrans();
			BeanUtil.copyProperties(dto, trans);
			ShipInboundTrans oldtrans = shipInboundTransService.getOne(new QueryWrapper<ShipInboundTrans>().eq("shipmentid",dto.getShipmentid()));
			boolean flog = false;
			if(oldtrans!=null) {
				trans.setId(oldtrans.getId());
				flog=shipInboundTransService.updateById(trans);
			}else {
				flog=shipInboundTransService.save(trans);
			}
	        return Result.judge(flog);
	}
	
	@ApiOperation(value = "提交发货计划【ERP内部调用】")
	@PostMapping("/saveInboundPlan")
	@SystemControllerLog("新增")
	public Result<String> saveInboundPlanAction(@ApiParam("发货计划")@RequestBody ShipInboundPlanDTO inplan){
			ShipInboundPlan inplanparam = new ShipInboundPlan();
			BeanUtil.copyProperties(inplan, inplanparam);
			List<ShipInboundItemVo> list = inplan.getPlanitemlist();
			List<ShipInboundItem> itemlist = new ArrayList<ShipInboundItem>();
			for(ShipInboundItemVo itemvo:list) {
				ShipInboundItem item=new ShipInboundItem();
				BeanUtil.copyProperties(itemvo, item);
				itemlist.add(item);
			}
			inplanparam.setPlanitemlist(itemlist);
			ShipInboundTrans trans=new ShipInboundTrans();
			BeanUtil.copyProperties(inplan.getTransinfo(),trans);
			inplanparam.setTransinfo(trans);
		    shipInboundPlanService.saveShipInboundPlan(inplanparam);
		    return Result.success();
	}

}
