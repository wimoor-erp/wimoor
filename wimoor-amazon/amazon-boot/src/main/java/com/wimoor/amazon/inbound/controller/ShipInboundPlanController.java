package com.wimoor.amazon.inbound.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanShipItemService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货单")
@RestController
@RequestMapping("/api/v1/shipInboundPlan")
@SystemControllerLog("发货单")
@RequiredArgsConstructor
public class ShipInboundPlanController {
    final IShipInboundPlanService shipInboundPlanService;
    final ISerialNumService serialNumService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IAmzProductSalesPlanShipItemService iAmzProductSalesPlanShipItemService;
	public ShipInboundPlan saveInboundPlan(@ApiParam("发货计划")@RequestBody ShipInboundPlanDTO inplan){
		  try {
			    ShipInboundPlan inplanparam = new ShipInboundPlan();
				BeanUtil.copyProperties(inplan, inplanparam);
				List<ShipInboundItemVo> list = inplan.getPlanitemlist();
				List<ShipInboundItem> itemlist = new ArrayList<ShipInboundItem>();
				for(ShipInboundItemVo itemvo:list) {
					ShipInboundItem item=new ShipInboundItem();
					BeanUtil.copyProperties(itemvo, item);
					itemlist.add(item);
				}
				inplanparam.setPlansubid(inplan.getBatchnumber());
				inplanparam.setPlanitemlist(itemlist);
				ShipInboundTrans trans=new ShipInboundTrans();
				BeanUtil.copyProperties(inplan.getTransinfo(),trans);
				inplanparam.setTransinfo(trans);
				inplanparam.setId(null);
				inplanparam.setOpttime(new Date());
				inplanparam.setAuditstatus(1);
				inplanparam.setCreatedate(new Date());
				shipInboundPlanService.saveShipInboundPlan(inplanparam);
				return inplanparam;
		} catch (Exception e) {
			e.printStackTrace();
		    throw new BizException(e.getMessage());
		}
	}
	
	@ApiOperation(value = "提交发货计划")
	@PostMapping("/saveInboundPlan")
	@SystemControllerLog("新增")    
	@Transactional
	public  Result<String> saveInboundPlanAction(@ApiParam("发货计划")@RequestBody ShipInboundPlanDTO inplan){
		 UserInfo user=UserInfoContext.get();
			inplan.setCreatedate(new Date());
			inplan.setCreator(user.getId());
			inplan.setLabelpreptype("SELLER_LABEL");
			inplan.setOperator(user.getId());
			inplan.setOpttime(new Date());
			inplan.setShopid(user.getCompanyid());
			inplan.setAuditstatus(1);
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
				 ShipInboundPlan myinplan =  saveInboundPlan(inplan);
				  if (myinplan.getId() != null&&myinplan.getPlanitemlist()!=null&&myinplan.getPlanitemlist().size()>0) {
							if(StrUtil.isNotBlank(inplan.getPlanid()) ) {
								List<String> list=new ArrayList<String>();
								for(ShipInboundItemVo item:inplan.getPlanitemlist()) {
									list.add(item.getSellersku());
								}
								Map<String,Object> param=new HashMap<String,Object>();
								param.put("planid", inplan.getPlanid());
								param.put("planmarketplaceid", inplan.getPlanmarketplaceid());
								param.put("issplit",inplan.getIssplit());
								param.put("list",list);
								try {
									erpClientOneFeign.afterShipInboundPlanSaveAction(param);
								}catch(Exception e) {
									e.printStackTrace();
								}
							}
							if(StrUtil.isNotBlank(inplan.getBatchnumber()) ) {
								iAmzProductSalesPlanShipItemService.moveBatch(user.getCompanyid(),inplan.getBatchnumber());
							}
						return Result.success(myinplan.getId());
					}else {
						 throw new BizException("提交失败");
					}
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
	
	
	@GetMapping(value = "getShipRecordByMarket")
	public Result<List<Map<String, Object>>> getShipRecordByMarket(String marketplaceid,String groupid)   {
		return Result.success(shipInboundPlanService.getShipRecordByMarket(marketplaceid, groupid));
	}
	
	@GetMapping(value = "getShipRecord")
	public Result<IPage<Map<String, Object>>> getShipRecordAction(String groupid,String marketplaceid,String sku)   {
		UserInfo user=UserInfoContext.get();
		List<Map<String, Object>> shipRecord = shipInboundPlanService.getShipRecord(user.getCompanyid(),groupid, marketplaceid, sku);
		List<Map<String, Object>> shipBadRecord = shipInboundPlanService.getShipBadRecord(user.getCompanyid(), marketplaceid, sku);
		if(shipRecord==null||shipRecord.size()<=0) {
			shipRecord=shipBadRecord;
		}else {
			if(shipBadRecord!=null&&shipBadRecord.size()>0) {
				shipRecord.addAll(shipBadRecord);
			}
		}
		BasePageQuery query=new BasePageQuery();
		return Result.success(query.getListPage(shipRecord));
	}
 
	
}
