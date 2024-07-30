package com.wimoor.amazon.product.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.service.IProductProductTypeService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
@Api(tags = "产品类型接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product/amzProductType")
public class AmzProductTypeController {

	final IAmazonAuthorityService amazonAuthorityService;
	final IProductProductTypeService iProductProductTypeService;
	
    
    @ApiOperation(value = "获取产品类型")
    @GetMapping("/refreshProductType")
    public Result<?> captureProductTypeAction(String groupid,String marketplaceid) {
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	    iProductProductTypeService.captureProductType(auth, marketplaceid);
        return Result.success("");
    }  
    
    
    @ApiOperation(value = "根据类型名称获取信息")
    @GetMapping("/getProductDefinition")
    public Result<?> getProductDefinitionAction(String groupid,String marketplaceid,String name) {
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	    ProductTypeDefinition res = iProductProductTypeService.captureTypeDefinition(auth, marketplaceid,name);
        return Result.success(res);
    } 
    
     

}

