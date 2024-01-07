package com.wimoor.amazon.product.controller;


import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.finances.service.IAmzFinEmailService;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.pojo.dto.PlanDetailDTO;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Api(tags = "产品销量计算后的补货和发货规划接口")
@RestController
@RequestMapping("/api/v1/product/salesplan")
@RequiredArgsConstructor
public class AmzProductSalesPlanController {
	final IAmzProductSalesPlanService iAmzProductSalesPlanService;
    final IProductInfoService iProductInfoService;
    final IAmzFinEmailService iAmzFinEmailService;
    @ApiOperation(value = "计划刷新")
    @GetMapping("/refreshPlanData")
    public Result<?> refreshData() {
    	Set<String> param=new HashSet<String>();
    	iAmzProductSalesPlanService.refreshData(param);
    	if(param.size()==0) {
    		this.iAmzFinEmailService.sendMonthEmailDetailTask();
        	this.iAmzFinEmailService.sendWeekEmailDetailTask();	
    	}
    	return Result.success();
    }
    
    @ApiOperation(value = "计划刷新")
    @GetMapping("/refreshDataByGroup")
    public Result<?> refreshDataByGroup(String groupid) {
		//UserInfo user = UserInfoContext.get();
		try {
			iAmzProductSalesPlanService.refreshData(groupid);
		}catch(Exception e) {
			e.printStackTrace();
			throw new BizException("计算异常，请联系管理员");
		}
    	return Result.success();
    }
    @ApiOperation(value = "计划刷新")
    @GetMapping("/refreshDataBySKU")
    public Result<?> refreshDataBySKU(String groupid,String marketplaceid,String sku) {
		//UserInfo user = UserInfoContext.get();
		try {
			iAmzProductSalesPlanService.refreshData( groupid, marketplaceid, sku);
		}catch(Exception e) {
			e.printStackTrace();
			throw new BizException("计算异常，请联系管理员");
		}
    	return Result.success();
    }
	 
	
    @ApiOperation(value = "展开的计划")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupid", value = "店铺ID", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "warehouseid", value = "仓库ID", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "msku", value = "本地SKU", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "plantype", value = "计划类型ship,purchase", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "iseu", value = "是否EU", paramType = "query", dataType = "Boolean"),
    })
    @PostMapping("/getExpandCountryData")
    public Result<?> getExpandCountryData(@RequestBody PlanDetailDTO dto) {
		UserInfo user = UserInfoContext.get();
		try {
			if(StrUtil.isBlankOrUndefined(dto.getGroupid())) {
				dto.setGroupid(null);
			}
			if(StrUtil.isBlankOrUndefined(dto.getWarehouseid())) {
               dto.setWarehouseid(null);
			}
			if(dto.getPlansimple()==null) {
				dto.setPlansimple(false);
			}
			 if(dto.getMarketplaceids()!=null&&dto.getMarketplaceids().size()>0) {
				 for(int i=0;i<dto.getMarketplaceids().size();i++) {
					   if(dto.getMarketplaceids().get(i).equals("IEU")||dto.getMarketplaceids().get(i).equals("EU")) {
						   dto.getMarketplaceids().set(i, "EU");
						   dto.getMarketplaceids().add("A13V1IB3VIYZZH");
						   dto.getMarketplaceids().add("A17E79C6D8DWNP");
						   dto.getMarketplaceids().add("A1805IZSGTT6HS");
						   dto.getMarketplaceids().add("A1C3SOZRARQ6R3");
						   dto.getMarketplaceids().add("A1PA6795UKMFR9");
						   dto.getMarketplaceids().add("A1RKKUPIHCS9HS");
						   dto.getMarketplaceids().add("A2NODRKZP88ZB9");
						   dto.getMarketplaceids().add("A33AVAJ2PDY3EV");
						   dto.getMarketplaceids().add("APJ6JRA9NG5V4");
						   dto.getMarketplaceids().add("ARBP9OOSHTCHU");
						   break;
					   }
				 }
			 }else {
				 dto.setMarketplaceids(null);
			 }
			dto.setShopid(user.getCompanyid());
			return Result.success(iAmzProductSalesPlanService.ExpandCountryDataByGroup(dto));
		}catch(Exception e) {
			e.printStackTrace();
			throw new BizException("获取信息异常，请联系管理员【"+e.getMessage()+"】");
		}
    }
    
    @ApiOperation(value = "获取计划")
    @PostMapping("/getPlanModel")
    public Result<IPage<Map<String, Object>>> getPlanModel(@RequestBody PlanDTO dto) {
    	UserInfo user = UserInfoContext.get();
    	dto.setShopid(user.getCompanyid());
    	boolean hassearch=false;
    	if(user.isLimit(UserLimitDataType.owner)) {
    		dto.setOwner(user.getId());
    		 hassearch=true;
    	}
    	 if(StrUtil.isAllBlank(dto.getSearch())) {
			 dto.setSearch(null);
		 }else {
			 dto.setSearch("%"+dto.getSearch().trim()+"%");
			 hassearch=true;
		 }
    	 if(StrUtil.isNotBlank(dto.getOwner())) {
    		 hassearch=true;
    	 }
    	 if(StrUtil.isAllBlank(dto.getName())) {
			 dto.setName(null);
		 }else {
			 dto.setName("%"+dto.getName().trim()+"%");
			 hassearch=true;
		 }
    	 if(dto.getSelected()==null||dto.getSelected()==false) {
    		 dto.setSelected(false);
    	 }else {
    		 hassearch=true;
    	 }
    	 if(StrUtil.isAllBlank(dto.getRemark())) {
			 dto.setRemark(null);
		 }else {
			 dto.setRemark("%"+dto.getRemark().trim()+"%");
			 hassearch=true;
		 }
    	 if(StrUtil.isAllBlank(dto.getGroupid())) {
			 dto.setGroupid(null);
		 }else {
			 dto.setGroupid(dto.getGroupid().trim());
		 }
		 if(dto.getMarketplaceids()!=null&&dto.getMarketplaceids().size()>0) {
			 for(int i=0;i<dto.getMarketplaceids().size();i++) {
				   if(dto.getMarketplaceids().get(i).equals("IEU")||dto.getMarketplaceids().get(i).equals("EU")) {
					   dto.getMarketplaceids().set(i, "EU");
					   dto.getMarketplaceids().add("A13V1IB3VIYZZH");
					   dto.getMarketplaceids().add("A17E79C6D8DWNP");
					   dto.getMarketplaceids().add("A1805IZSGTT6HS");
					   dto.getMarketplaceids().add("A1C3SOZRARQ6R3");
					   dto.getMarketplaceids().add("A1PA6795UKMFR9");
					   dto.getMarketplaceids().add("A1RKKUPIHCS9HS");
					   dto.getMarketplaceids().add("A2NODRKZP88ZB9");
					   dto.getMarketplaceids().add("A33AVAJ2PDY3EV");
					   dto.getMarketplaceids().add("APJ6JRA9NG5V4");
					   dto.getMarketplaceids().add("ARBP9OOSHTCHU");
					   break;
				   }
			 }
		 }else {
			 dto.setMarketplaceids(null);
		 }
		 if(dto.getTags()!=null&&dto.getTags().size()>0) {
				List<String> pidlist = iProductInfoService.getPidListByTagList(dto.getTags(),dto.getShopid(),null,dto.getGroupid(),null,dto.getMarketplaceids());
				dto.setPidlist(pidlist);
				hassearch=true;
			}else {
				dto.setPidlist(null);
			}
		 if(StrUtil.isAllBlank(dto.getStatus())) {
			 dto.setStatus(null);
		 }else {
			 hassearch=true;
		 }
		 if(StrUtil.isBlank(dto.getCategoryid())) {
			 dto.setCategoryid(null);
		 }else {
			 hassearch=true;
		 }
		 if(dto.getIssfg()!=null) {
			 hassearch=true;
		 }
		 if(StrUtil.isBlank(dto.getHasAddFee())) {
			 dto.setHasAddFee(null);
		 }else {
			 hassearch=true;
		 }
		 if(StrUtil.isBlank(dto.getStatus2())) {
			 dto.setStatus2(null);
		 }else if(!dto.getStatus2().equals("shownormal")){
			 hassearch=true;
		 }
		 if(StrUtil.isAllBlank(dto.getCurrentRank())) {
			 dto.setCurrentRank(null);
		 }
		 if(StrUtil.isAllBlank(dto.getShortdays())) {
			 dto.setShortdays(null);
		 }
		 if(dto.getIscheck()!=null&&dto.getIscheck().equals("true")) {
			 dto.setSelected(true);
			 dto.setIscheck("all");
			 hassearch=true;
		 }else if(dto.getIscheck()!=null&&dto.getIscheck().equals("all")){
			 dto.setSelected(true);
			 dto.setIscheck("all");
			 hassearch=true;
		 }else {
			 dto.setIscheck(null);
		 }
		 if(StrUtil.isAllBlank(dto.getSkuarray())) {
			 dto.setSkulist(null);
		 }else {
			 dto.setSkulist(Arrays.asList(dto.getSkuarray().split(",")));
			 hassearch=true;
		 }
		 if(StrUtil.isAllBlank(dto.getGroupid())) {
			 dto.setGroupid(null);
		 }
	    if(StrUtil.isBlank(dto.getSmall())) {
	    	dto.setSmall(null);
	    }
	 
		 IPage<Map<String, Object>> page=null;
			 if(dto.getPlantype().equals("purchase")) {
				 List<Map<String, Object>> list  =iAmzProductSalesPlanService.getPlanModel(dto);
				 if(dto.getSort()!=null&&dto.getSort().equals("marketneedpurchase")&&hassearch==false) {
					 page = dto.getListPage(list);
					 List<Map<String, Object>> result = iAmzProductSalesPlanService.handlePurchase(dto,page.getRecords());
					 if(result!=null&&result.size()>0) {
						 iAmzProductSalesPlanService.setPurchaseRecord(result);
					 }
					 dto.sort(result);
	    			 page.setRecords(result);
	    		}else {
					 List<Map<String, Object>> result = iAmzProductSalesPlanService.handlePurchase(dto,list);
					 page = dto.getListPage(result);
					 if(page!=null&&page.getSize()>0) {
						 iAmzProductSalesPlanService.setPurchaseRecord(page.getRecords());
					 }
	    		}
				
				 
			 }else {
				 List<Map<String, Object>> list  =iAmzProductSalesPlanService.getPlanModel(dto);
				 if(dto.getSort()!=null&&dto.getSort().equals("marketneedship")&&hassearch==false) {
					 page = dto.getListPage(list);
					 List<Map<String, Object>> result = iAmzProductSalesPlanService.handleShip(dto,page.getRecords());
					 dto.sort(result);
					 page.setRecords(result);
				 }else {
					 List<Map<String, Object>> result = iAmzProductSalesPlanService.handleShip(dto,list);
				     page = dto.getListPage(result);
				 }
				 
			 }
		if(page!=null&&page.getRecords()!=null) {
			iAmzProductSalesPlanService.handleTags(dto,page); 
		    if(dto.getExpendall()!=null&&dto.getExpendall()) {
		    	List<String> skus=new LinkedList<String>();
		    	for(Map<String, Object> item:page.getRecords()) {
		    		skus.add(item.get("msku").toString());
		    	}
	    		if(skus.size()>0) {
	    			PlanDetailDTO countrydto=new PlanDetailDTO();
		    		countrydto.setGroupid(dto.getGroupid());
		    		countrydto.setGroupids(dto.getGroupids());
		    		countrydto.setMarketplaceids(dto.getMarketplaceids());
		    		countrydto.setIseu(false);
		    		countrydto.setPlantype(dto.getPlantype());
		    		countrydto.setWarehouseid(dto.getWarehouseid());
		    		countrydto.setPlansimple(dto.getPlansimple());
		    		countrydto.setMskus(skus);
		    		countrydto.setShopid(user.getCompanyid());
		    		countrydto.setMsku(null);
		    		 Map<String, List<Map<String, Object>>> expendDatas = iAmzProductSalesPlanService.ExpandCountrysDataByGroup(countrydto);
						for(Map<String, Object> item:page.getRecords()) {
				    		String msku=item.get("msku").toString();
				    		List<Map<String, Object>> expendData=expendDatas.get(msku);
				    		if(expendData!=null) {
				    			item.put("expendData", expendData);
				    		}
				    	}
	    		   }
		    }
		}
    	return Result.success(page) ;
    }

    
}

