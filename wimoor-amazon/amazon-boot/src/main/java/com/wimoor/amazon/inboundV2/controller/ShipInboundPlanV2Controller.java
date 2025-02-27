package com.wimoor.amazon.inboundV2.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.amazon.spapi.model.fulfillmentinbound.LabelOwner;
import com.amazon.spapi.model.fulfillmentinboundV20240320.MskuPrepDetailInput;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PrepCategory;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PrepOwner;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PrepType;
import com.wimoor.admin.api.AdminClientOneFeign;
import com.wimoor.amazon.util.LockCheckUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.inboundV2.pojo.dto.InboundPlansDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inboundV2.service.IInboundApiHandlerService;
import com.wimoor.amazon.inboundV2.service.IShipInboundBoxService;
import com.wimoor.amazon.inboundV2.service.IShipInboundCaseService;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.amazon.inboundV2.service.IShipInboundOperationService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentRecordV2Service;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanShipItemService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.ship.pojo.dto.ShipFormDTO;
import com.wimoor.erp.ship.pojo.dto.ShipItemDTO;

import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货单")
@RestController
@RequestMapping("/api/v2/shipInboundPlan")
@SystemControllerLog("发货单")
@RequiredArgsConstructor
public class ShipInboundPlanV2Controller {
    final IShipInboundPlanService shipInboundPlanV2Service;
    final ISerialNumService serialNumService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IAmzProductSalesPlanShipItemService iAmzProductSalesPlanShipItemService;
	final IShipInboundBoxService shipInboundBoxV2Service;
	final IShipInboundCaseService shipInboundCaseV2Service;
	final IShipInboundItemService iShipInboundItemService;
	final IInboundApiHandlerService iInboundApiHandlerService;
	final IShipInboundOperationService  iShipInboundOperationService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IShipInboundShipmentService shipInboundShipmentV2Service;
	final IShipInboundShipmentRecordV2Service shipInboundV2ShipmentRecordService;
	final AdminClientOneFeign adminClientOneFeign;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	 @ApiOperation(value = "获取货件计划")
	 @GetMapping("/getPlanInfo")
	 public Result<ShipPlanVo> getPlanInfoAction(String formid) {
		 UserInfo user=UserInfoContext.get();
		 try {
			 if(StrUtil.isEmptyIfStr(formid)) {
				 throw new BizException("计划ID不能为空");
			 }
			 
			 ShipPlanVo vo = shipInboundPlanV2Service.getPlanBaseInfo(formid,user);
			 return Result.success(vo);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		return Result.success(null);
	 }
	 
	@ApiOperation(value = "提交发货计划")
	@PostMapping("/saveInboundPlan")
	@SystemControllerLog("新增")    
	@Transactional
	public  Result<String> saveInboundPlanAction(@ApiParam("发货计划")@RequestBody ShipInboundPlan inplan){
		 UserInfo user=UserInfoContext.get();
			inplan.setCreatetime(new Date());
			inplan.setCreator(user.getId());
			inplan.setOperator(user.getId());
			inplan.setOpttime(new Date());
			inplan.setShopid(user.getCompanyid());
			inplan.setAuditstatus(1);
			inplan.setInvstatus(0);
			 try {
					inplan.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SF"));
				} catch (Exception e) {
					e.printStackTrace();
					try {
						inplan.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SF"));
					} catch (Exception e1) {
						e1.printStackTrace();
						throw new BizException("编码获取失败,请联系管理员");
					}
				}
			try {
				if(inplan.getInvtype()==null) {
					inplan.setInvtype(0);
				}
				shipInboundPlanV2Service.saveShipInboundPlan(inplan);
				shipInboundV2ShipmentRecordService.saveRecord(inplan);
				if(StrUtil.isNotBlank(inplan.getBatchnumber()) ) {
					iAmzProductSalesPlanShipItemService.moveBatch(user.getCompanyid(),inplan.getBatchnumber());
				}
				return Result.success(inplan.getId());
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
	
	@ApiOperation(value = "配货发货计划")
	@PostMapping("/changeInboundPlan")
	@SystemControllerLog("配货")    
	@Transactional
	public  Result<String> changeInboundPlanAction(@ApiParam("发货计划")@RequestBody ShipInboundPlan inplan){
		    UserInfo user=UserInfoContext.get();
			LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"changeInboundPlan"+user.getCompanyid());
			try {
				ShipInboundPlan old = shipInboundPlanV2Service.getById(inplan.getId());
				old.setOperator(user.getId());
				old.setOpttime(new Date());
				old.setAuditstatus(2);
				shipInboundPlanV2Service.changeShipInboundPlan(old,inplan.getPlanitemlist());
				shipInboundV2ShipmentRecordService.saveRecord(old);
			}finally {
				lock.clear();
			}
			return Result.success(inplan.getId());
	}
	
	@ApiOperation(value = "完成配货发货计划")
	@GetMapping("/confirmInboundPlan")
	@SystemControllerLog("确认配货")
	@Transactional
	public  Result<ShipInboundOperation> confirmInboundPlan(String formid){
		    UserInfo user=UserInfoContext.get();
		    ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
		    if(plan.getAuditstatus()==3) {
		    	throw new BizException("已完成配货发货计划");
		    }
		    ShipInboundOperation result = shipInboundPlanV2Service.confirmInboundPlan(plan);
			return Result.success(result);
	}
	@ApiOperation(value = "完成配货发货计划处理")
	@GetMapping("/doneInboundPlan")
	@SystemControllerLog("完成配货")
	@Transactional
	public  Result<ShipInboundPlan> doneInboundPlan(String formid){
		UserInfo user=UserInfoContext.get();
		ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
		if(plan.getAuditstatus()==2) {
			plan.setOperator(user.getId());
			plan.setOpttime(new Date());
			plan.setAuditstatus(3);
			shipInboundPlanV2Service.updateById(plan);
		}
		return Result.success(plan);
	}


	@ApiOperation(value = "完成物流信息")
	@GetMapping("/confirmTransportation")
	@SystemControllerLog("完成物流信息")
	@Transactional
	public  Result<ShipInboundPlan> confirmTransportation(String formid){
		UserInfo user=UserInfoContext.get();
		ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
		if(plan.getAuditstatus()==6) {
			plan.setAuditstatus(7);
			plan.setOperator(user.getId());
			plan.setOpttime(new Date());
			shipInboundPlanV2Service.updateById(plan);
			shipInboundV2ShipmentRecordService.saveRecord(plan);
		}
		return Result.success(plan);
	}

	@ApiOperation(value = "完成计划")
	@GetMapping("/donePlan")
	@SystemControllerLog("完成计划")
	@Transactional
	public  Result<ShipInboundPlan> donePlan(String formid){
		UserInfo user=UserInfoContext.get();
		ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
		if(plan.getAuditstatus()==7) {
			plan.setAuditstatus(8);
			plan.setOperator(user.getId());
			plan.setOpttime(new Date());
			shipInboundPlanV2Service.updateById(plan);
			shipInboundV2ShipmentRecordService.saveRecord(plan);
			Result<UserInfo> info = adminClientOneFeign.getUserByUserId(user.getId());
			if(Result.isSuccess(info)&&info.getData()!=null&&info.getData().getCompanyid()!=null){
				user = info.getData();
			}
			shipInboundPlanV2Service.syncData(plan,user);

		}
		return Result.success(plan);
	}
	@ApiOperation(value = "更改发货地址")
	@GetMapping("/changeAddress")
	@SystemControllerLog("更改发货地址")    
	@Transactional
	public  Result<ShipInboundPlan> changeAddressAction(String formid,String addressid){
		    UserInfo user=UserInfoContext.get();
			ShipInboundPlan old=shipInboundPlanV2Service.changeAddress(user,formid,addressid);
			return Result.success(old);
	}
	
	@ApiOperation(value = "更改发货地址")
	@GetMapping("/changeShpmentAddress")
	@SystemControllerLog("更改发货地址")    
	@Transactional
	public  Result<ShipInboundPlan> changeShipmentAddressAction(String shipmentid,String addressid){
		    UserInfo user=UserInfoContext.get();
			ShipInboundPlan old=shipInboundPlanV2Service.changeShipmentAddress(user,shipmentid,addressid);
			return Result.success(old);
	}
	
	 @ApiOperation(value = "更新plan的备注")
	 @GetMapping("/updatePlanRemark")
	 public Result<String> updatePlanRemarkAction(String inboundplanid,String remark) {
		 ShipInboundPlan plan = shipInboundPlanV2Service.getById(inboundplanid);
		 if(plan!=null) {
			 plan.setRemark(remark);
			 shipInboundPlanV2Service.updateById(plan);
			 return Result.success("ok");
		 }else {
			 return Result.success(null);
		 }
	 }

	@ApiOperation(value = "更新plan的shipDate")
	@GetMapping("/updatePlanShipDate")
	public Result<String> updatePlanShipDateAction(String formid,String shipdate) {
		ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
		if(plan!=null) {
			if(plan.getAuditstatus()>=7){
				throw new BizException("当前状态不支持修改发货日期!");
			}else{
				plan.setShippingDate(GeneralUtil.getDatez(shipdate));
				shipInboundPlanV2Service.updateById(plan);
				return Result.success("ok");
			}
		}else {
			return Result.success(null);
		}
	}

	 @ApiOperation(value = "刷新发货计划")
	 @GetMapping("/refreshInboundPlan")
	 @Transactional
	 public  Result<?> refreshInboundPlan(String formid){
		    UserInfo user=UserInfoContext.get();
			shipInboundPlanV2Service.syncData(this.shipInboundPlanV2Service.getById(formid),user);
			return Result.success();
	 }
 
	 
	 @ApiOperation(value = "更新plan的SKU 预处理信息")
	 @PostMapping("/updatePlanItem")
	 public Result<Boolean> updatePlanItemAction(@RequestBody ShipInboundItem item) {
		   UserInfo user=UserInfoContext.get();
		   ShipInboundItem olditem = iShipInboundItemService.getById(item.getId());
		   olditem.setExpiration(item.getExpiration());
		   olditem.setLabelOwner(item.getLabelOwner());
		   olditem.setManufacturingLotCode(item.getManufacturingLotCode());
		   olditem.setPrepOwner(item.getPrepOwner());
		   olditem.setOperator(user.getId());
		   olditem.setOpttime(new Date());
		   return Result.judge(iShipInboundItemService.updateById(olditem))  ;
	 }
 
	 
	 
	@ApiOperation(value = "审核发货计划")
	@GetMapping("/approveInboundPlan")
	@SystemControllerLog("审核")    
	@Transactional
	public  Result<String> approveInboundPlan(String id){
		 UserInfo user=UserInfoContext.get();
		 ShipInboundPlan inplan=shipInboundPlanV2Service.getById(id);
			try {
				 List<ShipInboundItemVo> itemlist = iShipInboundItemService.listByFormid(inplan.getId());
				 ShipFormDTO dto=getFormDTO(inplan,itemlist);
				 erpClientOneFeign.outBoundShipInventory(dto);
				 for(ShipInboundItemVo item:itemlist) {
					 ShipInboundItem itemold = iShipInboundItemService.getById(item.getId());
					 itemold.setMsku(item.getMsku());
					 iShipInboundItemService.updateById(itemold);
				 }
				 inplan.setAuditstatus(2);
				 inplan.setInvstatus(1);
				 inplan.setAuditor(user.getId());
				 inplan.setAuditime(new Date());
				 inplan.setOpttime(new Date());
				 inplan.setOperator(user.getId());
				 shipInboundPlanV2Service.updateById(inplan);
				 shipInboundV2ShipmentRecordService.saveRecord(inplan);
				 return Result.success(inplan.getId());
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(BizException e) {
				 throw e;
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}

	@ApiOperation(value = "追加商品到审核后发货计划")
	@PostMapping("/approveInboundPlanAppend")
	@SystemControllerLog("审核")
	@Transactional
	public  Result<String> approveInboundPlanAppend(@RequestBody List<ShipInboundItem> items){
		UserInfo user=UserInfoContext.get();
		if(items==null||items.size()==0){
			return Result.success();
		}
		ShipInboundPlan inplan=shipInboundPlanV2Service.getById(items.get(0).getFormid());
		List<ShipInboundItem> olditems = iShipInboundItemService.lambdaQuery().eq(ShipInboundItem::getFormid, inplan.getId()).list();
		Map<String,ShipInboundItem> oldmap=new HashMap<String,ShipInboundItem>();
		Map<String,ShipInboundItem> newmap=new HashMap<String,ShipInboundItem>();
		for(ShipInboundItem item:olditems) {
			oldmap.put(item.getSku(), item);
		}
		try {
			if(inplan.getAuditstatus()>2||inplan.getInvstatus()==2){
				throw new BizException("当前状态不支持新增");
			}
			for(ShipInboundItem item:items){
				item.setOperator(user.getId());
				item.setOpttime(new Date());
				if(oldmap.containsKey(item.getSku())){
					throw new BizException("SKU:"+item.getSku()+"已存在");
				}
				if(item.getLabelOwner()==null) {
					item.setLabelOwner(LabelOwner.SELLER.getValue());
				}
				if(item.getPrepOwner()==null) {
					item.setPrepOwner(PrepOwner.SELLER.getValue());
				}
				item.setConfirmQuantity(item.getQuantity());
				item.setAppendtime(new Date());
				newmap.put(item.getSku(),item);
				iShipInboundItemService.save(item);
			}

			List<ShipInboundItemVo> itemlist = iShipInboundItemService.listByFormid(inplan.getId());
			ShipInboundItemVo vo=null;
			List<ShipInboundItemVo> itemvoList=new ArrayList<ShipInboundItemVo>();
			 for(ShipInboundItemVo itemvo:itemlist){
				 ShipInboundItem newitem = newmap.get(itemvo.getSku());
				 if(newitem!=null){
					 itemvoList.add(itemvo);
					 newitem.setMsku(itemvo.getMsku());
					 iShipInboundItemService.updateById(newitem);
				 }
			 }
			 if(itemvoList.size()==0){
				 throw new BizException("保存失败");
			 }
			 if(inplan.getAuditstatus()==2&& inplan.getInvstatus()==1){
				 ShipFormDTO dto=getFormDTO(inplan, itemvoList);
				 erpClientOneFeign.outBoundShipInventory(dto);
			 }
			return Result.success(inplan.getId());
		}catch(FeignException e) {
			throw new BizException("提交失败" +e.getMessage());
		}catch(BizException e) {
			throw e;
		}catch(Exception e) {
			throw new BizException("提交失败" +e.getMessage());
		}
	}

	@ApiOperation(value = "审核库存处理")
	@GetMapping("/getInvParam") 
	public  Result<ShipFormDTO> getApproveParamAction(String id){
		     ShipInboundPlan inplan=shipInboundPlanV2Service.getById(id);
			 try {
				 List<ShipInboundItemVo> itemlist = iShipInboundItemService.listByFormid(inplan.getId());
				 ShipFormDTO mydto=getFormDTO(  inplan, itemlist);
				 return Result.success(mydto);
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
	
	@ApiOperation(value = "库存状态处理")
	@GetMapping("/setInvStatus") 
	public  Result<Boolean> setApproveInvDoneAction(String id,Integer status){
		     ShipInboundPlan inplan=shipInboundPlanV2Service.getById(id);
			 try {
				 if(status==null) {
					 throw new BizException("库存状态错误，请联系管理员");
				 }else {
					 inplan.setInvstatus(status);
				 }
				 return Result.success(shipInboundPlanV2Service.updateById(inplan));
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
	

	
	public ShipFormDTO getFormDTO(ShipInboundPlan inplan,List<ShipInboundItemVo> itemlist) {
		ShipFormDTO mydto=new ShipFormDTO();
		mydto.setFormid(inplan.getId());
		mydto.setWarehouseid(inplan.getWarehouseid());
		mydto.setNumber(inplan.getNumber());
		List<ShipItemDTO> list=new ArrayList<ShipItemDTO>();
		for(ShipInboundItemVo item:itemlist) {
			ShipItemDTO dto=new ShipItemDTO();
			dto.setMaterialid(item.getMaterialid());
			dto.setMsku(item.getMsku());
			dto.setQuantity(item.getConfirmQuantity());
			dto.setSku(item.getSku());
			list.add(dto);
		}
		mydto.setList(list);
		return mydto;
	}
	@ApiOperation(value = "驳回发货计划")
	@GetMapping("/rejectInboundPlan")
	@SystemControllerLog("驳回")    
	@Transactional
	public  Result<String> rejectInboundPlan(String id){
		 UserInfo user=UserInfoContext.get();
		 ShipInboundPlan inplan=shipInboundPlanV2Service.getById(id);
		 inplan.setAuditstatus(11);
		 inplan.setAuditime(new Date());
		 inplan.setAuditor(user.getId());
		 shipInboundPlanV2Service.updateById(inplan);
		 shipInboundV2ShipmentRecordService.saveRecord(inplan);
		 return Result.success(inplan.getId());
			 
	}
	
	 @ApiOperation(value = "获取货件计划列表")
	 @PostMapping("/getPlanList")
	 public Result<IPage<ShipPlanVo>> getPlanListAction(@ApiParam("货件计划DTO") @RequestBody ShipPlanListDTO dto) {
		 UserInfo user=UserInfoContext.get();
	     return Result.success(shipInboundPlanV2Service.getPlanList(dto,user));
	 }
	 
    @ApiOperation(value = "获取货件计划列表")
	@PostMapping("/getPlanListSync")
	public Result<?> getPlanListSyncAction(@ApiParam("货件计划DTO") @RequestBody InboundPlansDTO dto) {
		UserInfo user=UserInfoContext.get();
		return Result.success(shipInboundPlanV2Service.getPlanListSync(dto,user));
	}

	@ApiOperation(value = "获取货件计划列表")
	@PostMapping("/refreshPlan")
	public Result<?> getPlanListSyncAction(@ApiParam("货件计划") @RequestBody ShipInboundPlan dto) {
		UserInfo user=UserInfoContext.get();
		return Result.success(shipInboundPlanV2Service.refreshPlan(dto,user));
	}

	@ApiOperation(value = "获取货件计划列表")
	@PostMapping("/syncPlan")
	public Result<?> detailPlanAction(@ApiParam("货件计划") @RequestBody ShipInboundPlan dto) {
		UserInfo user=UserInfoContext.get();
		return Result.success(shipInboundPlanV2Service.detailPlan(dto,user));
	}

	@ApiOperation(value = "取消发货计划")
	@GetMapping("/cancelInboundPlan")
	@SystemControllerLog("取消")    
	@Transactional
	public  Result<ShipInboundOperation> cancelInboundPlanAction(String planid){
		    UserInfo user=UserInfoContext.get();
		    ShipInboundPlan inplan = shipInboundPlanV2Service.getById(planid);
			inplan.setOperator(user.getId());
			inplan.setOpttime(new Date());
		    Integer oldstatus = inplan.getAuditstatus();
			 try {
			     inplan.setAuditstatus(12);
			     inplan.setOperator(user.getId());
			     inplan.setOpttime(new Date());
				 shipInboundPlanV2Service.updateById(inplan);
				 shipInboundV2ShipmentRecordService.saveRecord(inplan);
				 List<ShipInboundShipment> shipments= shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getFormid, inplan.getId()).list();
				 if(shipments!=null&&shipments.size()>0) {
					 for(ShipInboundShipment item:shipments) {
						 item.setStatus(0);
						 item.setOperator(user.getId());
						 item.setShipmentstatus("CANCEL");
						 item.setOpttime(new Date());
						 shipInboundShipmentV2Service.updateById(item);
					 }
				 }
				 if(oldstatus>=3&&inplan.getInvtype()!=1&&inplan.getInboundPlanId()!=null) {
					 ShipInboundOperation result =  shipInboundPlanV2Service.cancelInboundPlan(inplan);
					  if (result != null) {
							 return Result.success(result);
						}else {
							 throw new BizException("取消失败");
						}
				 }
				 return Result.success();
			 }catch(FeignException e) {
				 throw new BizException("取消失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("取消失败" +e.getMessage());
			 }
	}
	

	 
	
	
	@ApiOperation(value = "查看托盘的生成零担信息")
	@GetMapping("/listInboundPlanPallets")
	@Transactional
	public  Result<?> listInboundPlanPalletsAction(String planid){
		 try {
			Map<String, Object> map = shipInboundPlanV2Service.listInboundPlanPallets(planid);
			return Result.success(map);
		 }catch(FeignException e) {
			 throw new BizException("生成零担失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成零担失败" +e.getMessage());
		 }
	}
	
	
	
	@ApiOperation(value = "获取执行状态")
	@GetMapping("/getOperationById/ignoreRepeat")
	@Transactional
	public  Result<ShipInboundOperation> getOperationByIdAction(String operationid){
		 try {
			 ShipInboundOperation opt = iShipInboundOperationService.getById(operationid);
			 if(opt==null) {
				 throw new BizException("无法获取操作进度");
			 }
			 if(opt.getOperationStatus().equals("SUCCESS")) {
				 return Result.success(opt);
			 }
			 ShipInboundPlan plan = shipInboundPlanV2Service.getById(opt.getFormid());
			 AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
			return Result.success(iShipInboundOperationService.getOperation(auth,operationid));
		 }catch(FeignException e) {
			 throw new BizException("获取处理信息失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("获取处理信息失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "货件的完整详细信息")
	@GetMapping("/getOperation/ignoreRepeat")
	@Transactional
	public  Result<ShipInboundOperation> getOperationAction(String formid,String operation){
		 try {
			 if(StrUtil.isBlank(formid)) {
				 throw new BizException("未找到订单");
			 }
			 ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
			 AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
			 return Result.success(iShipInboundOperationService.getOperation(auth,formid,operation));
		 }catch(FeignException e) {
			 throw new BizException("获取处理信息失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("获取处理信息失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "下载印度市场中的 PCP 运输提供送货单文档")
	@GetMapping("/getDeliveryChallanDocument")
	public  Result<?> getDeliveryChallanDocumentAction(String planid,String shipmentId){
		 try {
			 shipInboundPlanV2Service.getDeliveryChallanDocument(planid,shipmentId);
			return Result.success();
		 }catch(FeignException e) {
			 throw new BizException("下载文档失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("下载文档失败" +e.getMessage());
		 }
	}
	
	

	
	
	@ApiOperation(value = "启动生成预约空档列表的过程")
	@GetMapping("/generateSelfShipAppointmentSlots")
	public  Result<String> generateSelfShipAppointmentSlotsAction(String planid,String shipmentId,String startDate,String endDate){
		 try {
			 String optionid = shipInboundPlanV2Service.generateSelfShipAppointmentSlots(planid,shipmentId,startDate,endDate);
			return Result.success(optionid);
		 }catch(FeignException e) {
			 throw new BizException("启动生成预约空档列表的过程失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("启动生成预约空档列表的过程失败" +e.getMessage());
		 }
	}
	
	 @ApiOperation(value = "根据FormId获取itemlist")
	 @GetMapping("/getItemlistByFormId")
	 public Result<Map<String, Object>> getItemlistByFormIdAction(String formid) {
		 ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
		 if(plan!=null) {
			 Map<String, Object> map=new HashMap<String, Object>();
			 List<ShipInboundItemVo> list = iShipInboundItemService.listByFormid(formid);
			 String addressid = plan.getSourceAddress();
			 map.put("itemlist", list);
			 map.put("addressid", addressid);
			 map.put("groupid", plan.getGroupid());
			 map.put("warehouseid", plan.getWarehouseid());
			 map.put("marketplaceid", plan.getMarketplaceid());
			 return Result.success(map);
		 }else {
			 return Result.success(null);
		 }
	 }

	@ApiOperation(value = "获取货件计划列表")
	@PostMapping("/listPrepDetails")
	public Result<?> listPrepDetailsAction(@ApiParam("货件计划") @RequestBody ShipInboundPlan dto) {
		UserInfo user=UserInfoContext.get();
		AmazonAuthority auth = this.amazonAuthorityService.getById(dto.getAmazonauthid());
		ShipInboundPlan plan = this.shipInboundPlanV2Service.getById(dto.getId());
		List<ShipInboundItem> list = this.iShipInboundItemService.lambdaQuery().eq(ShipInboundItem::getFormid, dto.getId()).list();
		List<String> skulist=list.stream().map(item->item.getSku()).collect(Collectors.toList());
		return Result.success(iInboundApiHandlerService.listPrepDetails(auth,plan.getMarketplaceid(),skulist));
	}


	@ApiOperation(value = "获取货件计划列表")
	@PostMapping("/setPrepDetails")
	public Result<?> setPrepDetailsAction(@ApiParam("货件计划") @RequestBody ShipPlanVo dto) {
		UserInfo user=UserInfoContext.get();
		AmazonAuthority auth = this.amazonAuthorityService.getById(dto.getAmazonauthid());
		List<MskuPrepDetailInput> mskuprep = new ArrayList<MskuPrepDetailInput>();
		for(ShipInboundItemVo item:dto.getItemlist()) {
			MskuPrepDetailInput msku=new MskuPrepDetailInput();
			msku.setMsku(item.getSku());
			List<PrepType> prepTypes=new LinkedList<PrepType>();
			Arrays.asList(item.getTypename().split(",")).forEach(type->prepTypes.add(PrepType.fromValue(type)));
			msku.setPrepTypes(prepTypes);
			msku.setPrepCategory(PrepCategory.fromValue(item.getPrepOwner()));
			mskuprep.add(msku);
		}
		return Result.success(iInboundApiHandlerService.setPrepDetails(auth,dto.getMarketplaceid(),mskuprep));
	}


	@ApiOperation(value = "获取货件计划列表")
	@GetMapping("/findNum")
	public Result<?> findNumAction() {
		UserInfo user=UserInfoContext.get();
		return Result.success(shipInboundPlanV2Service.findNum(user.getCompanyid()));
	}

}
