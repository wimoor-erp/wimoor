package com.wimoor.erp.warehouse.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 仓库货柜 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Api(tags = "仓库接口")
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
	
	final IWarehouseService warehouseService;
	

	@ApiOperation("仓库列表[包含子仓位]")
	@GetMapping("/list")
	public Result<List<Warehouse>> findWarehouseList(){
		UserInfo userinfo = UserInfoContext.get();
		List<Warehouse> list=warehouseService.getWarehouseTreeList(userinfo);
		return Result.success(list);
	}
	
	@ApiOperation("仓库列表[self_test,self_usable,self_unusable]")
	@GetMapping("/getlist")
	public Result<List<Warehouse>> findWarehouseFtypeList(String ftype){
		UserInfo userinfo = UserInfoContext.get();
		QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
		queryWrapper.eq("ftype", ftype);
		queryWrapper.eq("disabled", false);
		queryWrapper.eq("shopid", userinfo.getCompanyid());
		List<Warehouse> list=warehouseService.list(queryWrapper);
		if("self_unusable".equals(ftype)) {
              for(Warehouse item:list) {
            	  item.setName(item.getName().replace("-废品仓", ""));
              }
		}else if("self_usable".equals(ftype)) {
			 for(Warehouse item:list) {
           	  item.setName(item.getName().replace("-正品仓", ""));
             }
		}else if("self_test".equals(ftype)) {
			 for(Warehouse item:list) {
	           	  item.setName(item.getName().replace("-测试仓", ""));
	             }
			}
		return Result.success(list);
	}
	
	@ApiOperation("获取仓库默认的usable仓位")
	@GetMapping("/getSelfWarehouseById")
	public Result<String> getSelfWarehouseByIdAction(String id){
		UserInfo userinfo = UserInfoContext.get();
		QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
		queryWrapper.eq("ftype", "self_usable");
		queryWrapper.eq("disabled", false);
		queryWrapper.eq("shopid", userinfo.getCompanyid());
		queryWrapper.eq("parentid", id);
		queryWrapper.eq("isdefault",true);
		List<Warehouse> list=warehouseService.list(queryWrapper);
		if(list!=null && list.size()>0) {
			String wid=list.get(0).getId();
			return Result.success(wid);
		}else {
			return Result.success(null);
		}
	}
	
	
}

