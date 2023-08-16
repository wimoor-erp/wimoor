package com.wimoor.amazon.product.controller;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
    @ApiOperation(value = "计划刷新")
    @GetMapping("/refreshPlanData")
    public Result<?> refreshData() {
    	Set<String> param=new HashSet<String>();
    	iAmzProductSalesPlanService.refreshData(param);
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
			throw new BizException("计算异常，请联系管理员");
		}
    }
    
    @ApiOperation(value = "获取计划")
    @PostMapping("/getPlanModel")
    public Result<IPage<Map<String, Object>>> getPlanModel(@RequestBody PlanDTO dto) {
    	UserInfo user = UserInfoContext.get();
    	dto.setShopid(user.getCompanyid());
    	if(user.isLimit(UserLimitDataType.owner)) {
    		dto.setOwner(user.getId());
    	}
    	 if(StrUtil.isAllBlank(dto.getSearch())) {
			 dto.setSearch(null);
		 }else {
			 dto.setSearch("%"+dto.getSearch().trim()+"%");
		 }
    	 if(StrUtil.isAllBlank(dto.getName())) {
			 dto.setName(null);
		 }else {
			 dto.setName("%"+dto.getName().trim()+"%");
		 }
    	 if(StrUtil.isAllBlank(dto.getRemark())) {
			 dto.setRemark(null);
		 }else {
			 dto.setRemark("%"+dto.getRemark().trim()+"%");
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
			}else {
				dto.setPidlist(null);
			}
		 if(StrUtil.isAllBlank(dto.getStatus())) {
			 dto.setStatus(null);
		 }
		 if(StrUtil.isAllBlank(dto.getCurrentRank())) {
			 dto.setCurrentRank(null);
		 }
		 if(StrUtil.isAllBlank(dto.getShortdays())) {
			 dto.setShortdays(null);
		 }
		 if(dto.getIscheck()!=null&&dto.getIscheck().equals("true")) {
			 dto.setSelected(true);
		 }else if(dto.getIscheck()!=null&&dto.getIscheck().equals("all")){
			 dto.setSelected(true);
			 dto.setIscheck("all");
		 }else {
			 dto.setIscheck(null);
		 }
		 if(StrUtil.isAllBlank(dto.getSkuarray())) {
			 dto.setSkulist(null);
		 }else {
			 dto.setSkulist(Arrays.asList(dto.getSkuarray().split(",")));
		 }
		 if(StrUtil.isAllBlank(dto.getGroupid())) {
			 dto.setGroupid(null);
		 }
		 IPage<Map<String, Object>> page=null;
		 List<Map<String, Object>> list  =iAmzProductSalesPlanService.getPlanModel(dto);
			 if(dto.getPlantype().equals("purchase")) {
				 page =iAmzProductSalesPlanService.handlePurchase(dto,list,true);
			 }else {
				 page =iAmzProductSalesPlanService.handleShip(dto,list,true);
			 }
		if(page!=null&&page.getRecords()!=null) {
			iAmzProductSalesPlanService.handleTags(dto,page); 
		}
    	return Result.success(page) ;
    }

    
}

