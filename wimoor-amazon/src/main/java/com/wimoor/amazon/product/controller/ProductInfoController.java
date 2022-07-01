package com.wimoor.amazon.product.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.util.MysqlGenerator;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 产品信息 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Api(tags = "产品接口")
@RestController
@RequestMapping("/api/v1/report/product/productInfo")
public class ProductInfoController {
	
	    @ApiOperation(value = "创建对象")
	    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库的表明", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pkg", value = "对应业务的包如product,orders", paramType = "query", dataType = "String")
        })
	    @GetMapping("/createpojo")
	    public Result<String> createPojoAction(String table,String pkg) {
	    	MysqlGenerator.autoGenerator(table, pkg);
	        return Result.success("true");
	    }
	    
	    
}

