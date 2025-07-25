package com.wimoor.amazon.inbound.controller;
 

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundItemDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
 
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.inventory.pojo.dto.InventoryParameter;

import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货订单接口")
@RestController
@RequestMapping("/api/v1/shipFormSync")
@SystemControllerLog("发货货件同步")
@RequiredArgsConstructor
public class ShipFormSyncController {
	
	final IShipInboundItemService shipInboundItemService;
	final IShipInboundShipmentService shipInboundShipmentService;
	final ISerialNumService serialNumService;
    final IShipInboundPlanService shipInboundPlanService;
    final IShipInboundTransService shipInboundTransService;
    final IProductInfoService productInfoService;
    final IMarketplaceService marketplaceService;
	final IFulfillmentInboundService iFulfillmentInboundService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IShipAddressToService shipAddressToService;
	final IShipAddressService shipAddressService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IProductInOptService productInOptService;
	final IAmazonGroupService iAmazonGroupService;
	
	@ApiOperation(value = "货件同步获取列表")
	@GetMapping("/getSyncList")
	public Result<List<ShipInboundShipmenSummarytVo>> getSyncListAction(@ApiParam("店铺ID") @RequestParam String groupid,
			                                                             @ApiParam("站点ID") @RequestParam String marketplaceid,
			                                                             @ApiParam("货件ID") @RequestParam String search,
			                                                             @ApiParam("开始时间") @RequestParam String fromDate,
			                                                             @ApiParam("结束时间") @RequestParam String endDate
			                                                             ) {
		    UserInfo user=UserInfoContext.get();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Map<String,Object> param=new HashMap<String, Object>();
			if (StrUtil.isNotEmpty(fromDate)) {
				fromDate = fromDate.trim();
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			}
			if (StrUtil.isNotEmpty(endDate)) {
				endDate = endDate.trim();
			} else {
				endDate = GeneralUtil.formatDate(new Date(), sdf);
			}
			param.put("shopid", user.getCompanyid());
			param.put("search", search.trim());
			param.put("marketplaceid", marketplaceid);
			param.put("groupid", groupid);
			param.put("fromdate", fromDate);
			param.put("enddate", endDate );
			try {
				
				List<ShipInboundShipmenSummarytVo> list = shipInboundShipmentService.getShipmentSyncList(param);
				return Result.success(list);
			}catch(Exception e) {
				e.printStackTrace();
				throw new BizException(e.getMessage());
			}
	    }
	
	@ApiOperation(value = "货件同步")
	@GetMapping("/syncShipment")
	@Transactional
	//@GlobalTransactional
	public Result<ShipInboundShipmentDTO> syncShipmentAction(@ApiParam("店铺ID") @RequestParam String groupid, 
			                                            @ApiParam("站点ID") @RequestParam String marketplaceid, 
			                                            @ApiParam("货件ID") @RequestParam String shipmentid,
			                                            @ApiParam("仓库ID") @RequestParam String warehouseid){
		return Result.success(shipInboundShipmentService.syncShipment(groupid, marketplaceid, shipmentid,warehouseid));
	}
	
	@ApiOperation(value = "货件同步")
	@GetMapping("/confirmSyncShipment")
	@Transactional
	//@GlobalTransactional
	public Result<ShipInboundShipment> confirmSyncShipment(   @ApiParam("货件ID") @RequestParam String shipmentid ){
		ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		shipment.setSyncInv(2);
		shipInboundShipmentService.updateById(shipment);
		return Result.success(shipment);
	}
	
	@ApiOperation(value = "货件同步")
	@GetMapping("/refreshShipmentRec")
	public Result<Integer> refreshShipmentRec(  @ApiParam("货件ID") @RequestParam String shipmentid  ){
		return Result.success(shipInboundShipmentService.refreshShipmentRec( shipmentid));
	}
	
	
	@ApiOperation(value = "获取未同步货件")
	@GetMapping("/getUnSyncShipment")
	public Result<ShipInboundShipmenSummarytVo> getUnSyncShipmentAction(@ApiParam("店铺ID") @RequestParam String groupid, 
			                                            @ApiParam("站点ID") @RequestParam String marketplaceid, 
			                                            @ApiParam("货件ID") @RequestParam String shipmentid,
			                                            @ApiParam("仓库ID") @RequestParam String warehouseid){
		return Result.success(shipInboundShipmentService.getUnSyncShipment(groupid, marketplaceid, shipmentid,warehouseid));
	}
	private List<InventoryParameter> fulfillableOut(UserInfo user, ShipInboundPlanDTO plan, ShipInboundShipmentDTO shipment) throws BizException {
		    List<ShipInboundItemDTO> listmap = shipment.getItemList();
		    List<InventoryParameter> list=new ArrayList<InventoryParameter>();
			for (int i = 0; i < listmap.size(); i++) {
				ShipInboundItemDTO item = listmap.get(i);
				if(item.getMaterialid()!=null && item.getQuantityshipped()!=null) {
					list.add( subFulfillableOut(user, plan, item));
				}
			}
		return list;
	}
	
	private InventoryParameter subFulfillableOut(UserInfo user, ShipInboundPlanDTO plan, ShipInboundItemDTO item) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(item.getQuantityshipped());
		para.setMaterial(item.getMaterialid());
		para.setSku(item.getMsku());
		para.setFormid(plan.getId());
		para.setOpttime(new Date());
		para.setOperator(user.getId());
		para.setWarehouse(plan.getWarehouseid());
		para.setFormtype("outstockform");
		para.setShopid(plan.getShopid());
		para.setNumber(plan.getNumber());
	    return para;
	}
	
	@SystemControllerLog("同步并保存货件")
	@RequestMapping("/saveShipmentItemAndPlanBath")
	@Transactional
	//@GlobalTransactional
	public Result<ShipInboundShipmentDTO> saveShipmentItemAndPlanBathAction(
			@ApiParam("店铺ID") @RequestParam String groupid,
            @ApiParam("站点ID") @RequestParam String marketplaceid,
            @ApiParam("货件ID") @RequestParam String shipmentid,
            @ApiParam("仓库ID") @RequestParam String warehouseid,
            @ApiParam("是否减少库存字符串（true,false)") @RequestParam String needsubinv  ) {
 
	    UserInfo user=UserInfoContext.get();
		Result<ShipInboundShipmentDTO> shipmentResult =  syncShipmentAction(groupid,marketplaceid,shipmentid,warehouseid);
		List<InventoryParameter> optlist =null;
		if(shipmentResult!=null&&shipmentResult.getData()!=null&&StrUtil.isNotBlank(warehouseid)) {
			ShipInboundShipmentDTO shipment = shipmentResult.getData();
			if("true".equals(needsubinv)) {
			   optlist = fulfillableOut( user,  shipment.getInboundplan(),  shipment);
			}
		}
		Result<?> result =null;
		if(optlist!=null) {
			try {
				 result = erpClientOneFeign.out(optlist);
			 }catch(FeignException e) {
				 throw new BizException("扣除库存失败，" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("扣除库存失败，" +e.getMessage());
			 }
		}
	    try {
            if(result!=null&&Result.isSuccess(result)) {
            	confirmSyncShipment(shipmentid);
            }else {
            	  throw new BizException("扣除库存失败");
            }
        }catch (Exception e) {
			// TODO: handle exception
        	erpClientOneFeign.undoOut(optlist);
            throw new BizException(e.getMessage());
		} 
        return shipmentResult;
	}  
	    
	 
}
