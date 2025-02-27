package com.wimoor.amazon.inboundV2.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.inboundV2.pojo.dto.BoxAnalysisDTO;
import com.wimoor.amazon.inboundV2.service.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inboundV2.pojo.dto.PackingDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCartDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanShipItemService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货单")
@RestController
@RequestMapping("/api/v2/shipInboundPlan/box")
@SystemControllerLog("发货单装修")
@RequiredArgsConstructor
public class ShipInboundPlanBoxV2Controller {
    final IShipInboundPlanService shipInboundPlanV2Service;
    final IShipInboundItemService iShipInboundItemService;
    final ISerialNumService serialNumService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IAmzProductSalesPlanShipItemService iAmzProductSalesPlanShipItemService;
	final IShipInboundBoxService shipInboundBoxV2Service;
	final IShipInboundCaseService shipInboundCaseV2Service;
	final IFulfillmentInboundService iFulfillmentInboundService;
	final IShipInboundOperationService  iShipInboundOperationService;
	final IShipInboundBoxAnalysisService shipInboundBoxAnalysis1Service;
	final IShipInboundBoxAnalysisService shipInboundBoxAnalysis2Service;
	final IShipInboundBoxAnalysisService shipInboundBoxAnalysis3Service;
	@ApiOperation(value = "获取箱子信息")
	@PostMapping("/getBoxDetail")
	public Result<Map<String, Object>> getBoxDetailAction(@RequestBody PackingDTO dto) {
		if(dto.getShipmentid()!=null) {
			return Result.success(shipInboundBoxV2Service.getShipmentBoxDetial(dto));
		}else {
			return Result.success(shipInboundBoxV2Service.getBoxDetial(dto));
		}
	}
	
	@ApiOperation(value = "获取箱子信息")
	@GetMapping("/getAllBoxDim")
	public Result<List<Map<String, Object>>> getAllBoxDimAction(String formid) {
	    return Result.success(shipInboundBoxV2Service.findAllBoxDim(formid));
	}
	
	@ApiOperation(value = "查询当前箱子分组是否填写装箱信息")
	@GetMapping("/selectPackgroupInfo")
	public  Result<?> selectPackgroupInfoAction(String packingGroupId){
		Map<String,Object> boxMap=new HashMap<String, Object>();
		LambdaQueryWrapper<ShipInboundBox> queryWrapper=new LambdaQueryWrapper<ShipInboundBox>();
		queryWrapper.eq(ShipInboundBox::getPackingGroupId, packingGroupId);
		List<ShipInboundBox> list = shipInboundBoxV2Service.list(queryWrapper);
		if(list!=null && list.size()>0) {
			boxMap.put("msg", "success");
			boxMap.put("num", list.size());
			return Result.success(boxMap);
		}else {
			boxMap.put("msg", "none");
			return Result.success(boxMap);
		}
	}
	 

	@ApiOperation(value = "提交箱子信息")
	@PostMapping("/savePackingInformation")
	@SystemControllerLog("新增")    
	@Transactional
	public  Result<?> savePackingInformationAction(@ApiParam("发货计划")@RequestBody ShipCartDTO dto){
		     UserInfo user=UserInfoContext.get();
			 try {
				shipInboundBoxV2Service.savePackingInformation(dto,user);
				return Result.success();
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
		
	@ApiOperation(value = "提交箱子信息")
	@GetMapping("/submitPackingInformation")
	@SystemControllerLog("新增")    
	@Transactional
	public  Result<ShipInboundOperation> submitPackingInformationAction(String formid){
		     UserInfo user=UserInfoContext.get();
			 try {
				 ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
				List<ShipInboundItem> itemlist = iShipInboundItemService.lambdaQuery().eq(ShipInboundItem::getFormid, formid).list();
				return Result.success(shipInboundBoxV2Service.setPackingInformation(plan,user,itemlist));
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
	
	@ApiOperation(value = "完成装箱")
	@GetMapping("/donePlanBox")
	@SystemControllerLog("完成装箱")    
	@Transactional
	public  Result<Boolean> donePackingAction(String formid,String type){
		     UserInfo user=UserInfoContext.get();
			 try {
				return Result.success(shipInboundPlanV2Service.donePacking(user,formid,type));
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
		
	@ApiOperation(value = "生成箱子分组")
	@GetMapping("/generatePackingOptions")
	@Transactional
	public  Result<ShipInboundOperation> generatePackingOptionsAction(String formid){
		 try {
			return Result.success(shipInboundBoxV2Service.generatePackingOptions(formid));
		 }catch(FeignException e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }
	}
		
	@ApiOperation(value = "查看分组信息")
	@PostMapping("/listPackingOptions")
	@Transactional
	public  Result<?> listPackingOptionsAction(@RequestBody PackingDTO dto){
		 try {
			Map<String, Object> map = shipInboundBoxV2Service.listPackingOptions(dto);
			return Result.success(map);
		 }catch(FeignException e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }
	}


	@ApiOperation(value = "查看分组Group详细信息")
	@PostMapping("/listPackingGroupItems")
	@Transactional
	public  Result<?> listPackingGroupItemsAction(@RequestBody PackingDTO dto){
		 try {
			  Object result = shipInboundBoxV2Service.listPackingGroupItems(dto);
			return Result.success(result);
		 }catch(FeignException e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "查看分组Group详细信息")
	@PostMapping("/confirmPackingOption")
	@Transactional
	public  Result<ShipInboundOperation> confirmPackingOptionAction(@RequestBody PackingDTO dto){
		 try {
			 ShipInboundOperation result = shipInboundBoxV2Service.confirmPackingOption(dto);
			return Result.success(result);
		 }catch(FeignException e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }
	}


	@ApiOperation(value = "分析箱子")
	@PostMapping("/boxAnalysis")
	public Result<?> boxAnalysisAction(@RequestBody BoxAnalysisDTO dto) {
		UserInfo user=UserInfoContext.get();
		if(dto.getType().equals("v1")){
			return Result.success( shipInboundBoxAnalysis1Service.boxAnalysis(user,dto));
		}else if( dto.getType().equals("v2")){
			return Result.success( shipInboundBoxAnalysis2Service.boxAnalysis(user,dto));
		}else  {
			return Result.success( shipInboundBoxAnalysis3Service.boxAnalysis(user,dto));
		}
	}
	
		
}


