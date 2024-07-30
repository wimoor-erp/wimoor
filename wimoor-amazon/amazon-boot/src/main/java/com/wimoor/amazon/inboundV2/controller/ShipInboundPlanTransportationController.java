package com.wimoor.amazon.inboundV2.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.spapi.model.fulfillmentinboundV20240320.ListTransportationOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.TransportationSelection;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransHisMapper;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundTransDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipTransDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTransHis;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inboundV2.pojo.dto.TransportationDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentCustoms;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.amazon.inboundV2.service.IShipInboundTransportationService;
import com.wimoor.amazon.util.LockCheckUtils;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货单")
@RestController
@RequestMapping("/api/v2/shipInboundPlan/transportation")
@SystemControllerLog("发货单物流")
@RequiredArgsConstructor
public class ShipInboundPlanTransportationController {
	 final IShipInboundPlanService shipInboundPlanV2Service;
	 final IShipInboundTransportationService iShipInboundTransportationService;
	 final IShipInboundShipmentService shipInboundShipmentV2Service;	
     @Autowired
     private StringRedisTemplate stringRedisTemplate;
     final IShipInboundTransService shipInboundTransService;	
     final ShipInboundTransHisMapper shipInboundTransHisMapper;
    
    @ApiOperation(value = "提交物流信息")
 	@PostMapping("/saveTransCompany")
 	public Result<?> saveTransCompanyData(@ApiParam("物流信息")@RequestBody ShipInboundTransDTO dto) {
 		int result=0;
 		UserInfo user=UserInfoContext.get();
 		QueryWrapper<ShipInboundTrans> queryWrapper=new QueryWrapper<ShipInboundTrans>();
 		queryWrapper.eq("shipmentid", dto.getShipmentid());
 		List<ShipInboundTrans> list = shipInboundTransService.list(queryWrapper);
 		if (list.size() > 0) {
 			ShipInboundTrans item = list.get(0);
 			item.setChannel(dto.getChannel());
 			item.setCompany(dto.getCompany());
 			item.setOperator(user.getId());
 			item.setOpttime(new Date());
 			item.setTranstype(dto.getTranstype());
 			shipInboundTransService.updateById(item);
 			result+=shipInboundTransHisMapper.insert(new ShipInboundTransHis(item));
 		} else {
 			ShipInboundTrans ship=new ShipInboundTrans();
 			ship.setShipmentid(dto.getShipmentid());
 			ship.setChannel(dto.getChannel());
 			ship.setCompany(dto.getCompany());
 			ship.setOperator(user.getId());
 			ship.setOpttime(new Date());
 			ship.setTranstype(dto.getTranstype());
 			shipInboundTransService.save(ship);
 			result+=shipInboundTransHisMapper.insert(new ShipInboundTransHis(ship));
 		}
 		return Result.success(result);
 	}
     
		@ApiOperation(value = "生成不同收货地址数对应方案")
		@PostMapping("/generateTransportationOptions")
		@Transactional
		public  Result<ShipInboundOperation> generateTransportationOptionsAction(@ApiParam("发货计划")@RequestBody ShipInboundPlan inplan){
			 try {
				    UserInfo user=UserInfoContext.get();
				    ShipInboundPlan old = shipInboundPlanV2Service.getById(inplan.getId());
				    old.setOperator(user.getId());
				    old.setOpttime(new Date());
				    old.setShippingDate(inplan.getShippingDate());
				    old.setShippingSolution(inplan.getShippingSolution());
				    old.setTranstyle(inplan.getTranstyle());
				    old.setPlacementOptionId(inplan.getPlacementOptionId());				    
				    old.setShipmentids(inplan.getShipmentids());	
				    shipInboundPlanV2Service.updateById(old);
				return Result.success(iShipInboundTransportationService.generateTransportationOptions(old));
			 }catch(FeignException e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }
		}
		@ApiOperation(value = "保存用于生成配送商的参数")
		@PostMapping("/saveTransportationNeedInfo")
		@Transactional
		public  Result<?> saveTransportationNeedInfoAction(@ApiParam("发货计划")@RequestBody ShipInboundPlan inplan){
			 try {
				    UserInfo user=UserInfoContext.get();
				    ShipInboundPlan old = shipInboundPlanV2Service.getById(inplan.getId());
				    old.setOperator(user.getId());
				    old.setOpttime(new Date());
				    old.setShippingDate(inplan.getShippingDate());
				    old.setShippingSolution(inplan.getShippingSolution());
				    old.setTranstyle(inplan.getTranstyle());
				    old.setPlacementOptionId(inplan.getPlacementOptionId());				    
				    old.setShipmentids(inplan.getShipmentids());	
				    return Result.success(shipInboundPlanV2Service.updateById(old));
			 }catch(FeignException e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }
		}
		
		@ApiOperation(value = "生成不同收货地址数对应方案")
		@PostMapping("/listTransportationOptions")
		@Transactional
		public  Result<ListTransportationOptionsResponse> listTransportationOptionsAction(@ApiParam("发货计划")@RequestBody TransportationDTO dto){
			 try {
				return Result.success(iShipInboundTransportationService.listTransportationOptions(dto));
			 }catch(FeignException e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }
		}
		
		@ApiOperation(value = "生成不同收货地址数对应方案")
		@PostMapping("/confirmTransportationOptions/{formid}")
		@Transactional
		public  Result<ShipInboundOperation> confirmTransportationOptionsAction(@PathVariable("formid")String formid,@RequestBody List<TransportationSelection> transportationSelections){
			 try {
				return Result.success(iShipInboundTransportationService.confirmTransportationOptions(formid,transportationSelections));
			 }catch(FeignException e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }
		}
	
		@ApiOperation(value = "生成不同收货地址数对应方案")
		@GetMapping("/confirmTransportationOptionsByForm")
		@Transactional
		public  Result<ShipInboundOperation> confirmTransportationOptionsByFormAction(String formid){
			 try {
				 List<ShipInboundShipment> shipmentlist=shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getFormid, formid).list();
				 List<TransportationSelection> transportationSelections=new LinkedList<TransportationSelection>();
				 for(ShipInboundShipment item:shipmentlist) {
					 TransportationSelection entity=new TransportationSelection();
					 entity.setShipmentId(item.getShipmentid());
					 entity.setTransportationOptionId(item.getTransportationOptionId());
					 transportationSelections.add(entity);
				 }
				return Result.success(iShipInboundTransportationService.confirmTransportationOptions(formid,transportationSelections));
			 }catch(FeignException e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }
		}
		
		@ApiOperation(value = "保存物流商")
		@GetMapping("/saveConfirmTransportationOptionsInfo")
		@Transactional
		public  Result<?> confirmTransportationOptionsAction(String shipmentid,String transportationOptionId){
			 try {
				 ShipInboundShipment shipment = shipInboundShipmentV2Service.getById(shipmentid);
				 if(shipment==null) {
					 throw new BizException("没有找到货件");
				 }
				 shipment.setTransportationOptionId(transportationOptionId);
				return Result.success(shipInboundShipmentV2Service.updateById(shipment));
			 }catch(FeignException e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("生成方案失败" +e.getMessage());
			 }
		}
		
		 @ApiOperation(value = "保存物流信息")
		 @SystemControllerLog("保存物流信息")
		 @PostMapping("/saveSelfTrans")
		 public	Result<String> saveSelfTransAction(@ApiParam("货件物流信息") @RequestBody ShipTransDTO dto){
		    	UserInfo user=UserInfoContext.get();
		    	ShipInboundShipment shipment = shipInboundShipmentV2Service.getById(dto.getShipmentid());
				ShipInboundTrans ship = new ShipInboundTrans();
				ship.setCompany(dto.getCompany());
				if(StrUtil.isNotBlank(dto.getTranstype())) {
					ship.setTranstype(new BigInteger(dto.getTranstype()));
				}
				ship.setChannel(dto.getChannel());
				ship.setOrdernum(dto.getOrdernum());
				ship.setShipmentid(shipment.getShipmentConfirmationId());
				ship.setWunit(dto.getUnit());
				ship.setOperator(user.getId());
				ship.setOpttime(new Date());
				if(StrUtil.isNotBlank(dto.getWtype())) {
					ship.setWtype(Integer.parseInt(dto.getWtype()));
				}else {
					ship.setWtype(0);
				}
				ship.setArrivalTime(dto.getArrive());
				ship.setOutarrtime(dto.getOutarrivaldate());
				ship.setInarrtime(dto.getInarrivaldate());
				ship.setRemark(dto.getRemark());
				ship.setOtherfee(dto.getOtherfee());
				ship.setSingleprice(dto.getSingleprice());
				ship.setTransweight(dto.getRweight());
				LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"Markshiped"+user.getCompanyid());
				try {
					iShipInboundTransportationService.saveSelfTransData(user, ship, dto.getShipdate());
				}finally {
					lock.clear();
				}
				return Result.success("success");
			}
		 
		 @ApiOperation(value = "获取物流保存历史记录")
		 @GetMapping("/getSelfTransHis")
		 public Result<List<Map<String, Object>>> getSelfTransHisAction(@ApiParam("货件ID")@RequestParam String shipmentid) {
			     UserInfo user=UserInfoContext.get();
			     List<Map<String, Object>> list = iShipInboundTransportationService.getSelfTransHis(user.getCompanyid(),shipmentid);
		    	 return Result.success(list);
		 }
		
		 @ApiOperation(value = "获取海关信息")
		 @GetMapping("/getCustomsList")
		 public Result<Map<String, ShipInboundShipmentCustoms>> getCustomsListAction(@ApiParam("货件ID")@RequestParam String shipmentid) {
			       List<ShipInboundShipmentCustoms> list = iShipInboundTransportationService.getCustoms(shipmentid);
			       Map<String,ShipInboundShipmentCustoms> result=null;
			       if(list!=null&&list.size()>0) {
			    	   result=new HashMap<String,ShipInboundShipmentCustoms>();
			    	   for(ShipInboundShipmentCustoms item:list) {
			    		   result.put(item.getItemid(), item);
			    	   }
			    	  
			       }
		    	 return Result.success(result);
		 }
		 
		 @ApiOperation(value = "获取海关信息")
		 @PostMapping("/saveCustomsList")
		 public Result<Map<String, ShipInboundShipmentCustoms>> saveCustomsListAction(@RequestBody List<ShipInboundShipmentCustoms> list) {
			       UserInfo user=UserInfoContext.get();
			       iShipInboundTransportationService.saveCustoms(user,list);
		    	 return Result.success();
		 }
}
