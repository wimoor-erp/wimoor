package com.wimoor.amazon.follow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.common.mybatisplus.MysqlGenerator;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
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
@Api(tags = "公共接口")
@RestController
@SystemControllerLog("公共类")
@RequestMapping("/api/v1/follow/common")
public class CommonController  {
	 
	    @ApiOperation(value = "创建对象")
	    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库的表明", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pkg", value = "对应业务的包如product,orders", paramType = "query", dataType = "String")
        })
	    @GetMapping("/createpojo")
	    public Result<String> createPojoAction(String table,String pkg) {
	    	MysqlGenerator.autoGenerator(table, pkg,"t_amz_v2_");
	        return Result.success("true");
	    }
		
	    
}


