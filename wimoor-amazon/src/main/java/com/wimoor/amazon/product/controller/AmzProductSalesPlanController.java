package com.wimoor.amazon.product.controller;


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
import com.wimoor.amazon.api.ErpClientOneFeign;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

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
	  final ErpClientOneFeign erpClientOneFeign;
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
    
    @ApiOperation(value = "展开的计划")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupid", value = "店铺ID", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "warehouseid", value = "仓库ID", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "msku", value = "本地SKU", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "plantype", value = "计划类型ship,purchase", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "iseu", value = "是否EU", paramType = "query", dataType = "Boolean"),
    })
    @GetMapping("/getExpandCountryData")
    public Result<?> getExpandCountryData(String groupid,String warehouseid,String msku,String plantype,Boolean iseu,Integer amount) {
		UserInfo user = UserInfoContext.get();
		try {
			if(StrUtil.isBlankOrUndefined(groupid)) {
				groupid=null;
			}
			if(StrUtil.isBlankOrUndefined(warehouseid)) {
				warehouseid=null;
			} 
			return Result.success(iAmzProductSalesPlanService.ExpandCountryDataByGroup(user.getCompanyid(),groupid,warehouseid,msku,plantype,iseu,amount));
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
    	List<Map<String, Object>> list = iAmzProductSalesPlanService.getPlanModel(dto);
    	IPage<Map<String, Object>> page = dto.getListPage(list);
    	if(dto.getPlantype().equals("purchase")&&page!=null&&page.getRecords()!=null&&page.getRecords().size()>0) {
    		 for(Map<String, Object> item:page.getRecords()) {
				 try{
					   Result<?> resultLast = erpClientOneFeign.getLastRecordAction(item.get("id").toString());
					   if(Result.isSuccess(resultLast)&&resultLast.getData()!=null) {
									item.put("last", resultLast.getData());
								}
				 }catch(Exception e) {
					 e.printStackTrace();
				 }
    		 }
		 }
    	return Result.success(page);
    }
   

    
}

