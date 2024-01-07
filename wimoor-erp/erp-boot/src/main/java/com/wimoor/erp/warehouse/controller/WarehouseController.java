package com.wimoor.erp.warehouse.controller;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.pojo.dto.WarehouseDTO;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@SystemControllerLog(  "仓库")
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
	
	final IWarehouseService warehouseService;
	final ISerialNumService iSerialNumService;

	@ApiOperation("仓库列表[包含子仓位]")
	@GetMapping("/list")
	public Result<List<Warehouse>> findWarehouseList(){
		UserInfo userinfo = UserInfoContext.get();
		List<Warehouse> list=warehouseService.getWarehouseTreeList(userinfo);
		return Result.success(list);
	}
	
	@ApiOperation("仓库列表[包含子仓位]")
	@PostMapping("/listpage")
	public Result<IPage<Warehouse>> findWarehousePageList(@RequestBody WarehouseDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		IPage<Warehouse> list=warehouseService.findByCondition(dto.getPage(),dto.getSearch(),userinfo.getCompanyid(),dto.getFtype(),dto.getParentid());
		return Result.success(list);
	}
	
	@ApiOperation("仓库列表[self_test,self_usable,self_unusable]")
	@GetMapping("/getlist")
	public Result<List<Warehouse>> findWarehouseFtypeList(String ftype){
		UserInfo userinfo = UserInfoContext.get();
		List<Warehouse> list=warehouseService.findByType(ftype,userinfo.getCompanyid());
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
	
	@ApiOperation("海外仓库列表")
	@GetMapping("/getOverseaList")
	public Result<List<Warehouse>> getOverseaListList(String ftype,String groupid,String country){
		UserInfo userinfo = UserInfoContext.get();
 
		List<Warehouse> list=warehouseService.getOverseaWarehouse(userinfo.getCompanyid(),ftype,groupid,country);
				 
		if(ftype==null) {
            for(Warehouse item:list) {
          	  item.setName(item.getName().replace("-正品仓", ""));
            }
		}else if(ftype.contains("unusable")) {
              for(Warehouse item:list) {
            	  item.setName(item.getName().replace("-废品仓", ""));
              }
		}else if(ftype.contains("usable")) {
			 for(Warehouse item:list) {
           	  item.setName(item.getName().replace("-正品仓", ""));
             }
		}else if(ftype.contains("test")) {
			 for(Warehouse item:list) {
	           	  item.setName(item.getName().replace("-测试仓", ""));
	             }
			}
		return Result.success(list);
	}
	
	@ApiOperation("仓库列表[self_test,self_usable,self_unusable]")
	@GetMapping("/getNamelist")
	public Result<List<Warehouse>> findNameWarehouseFtypeList(String ftype){
		UserInfo userinfo = UserInfoContext.get();
		QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
		queryWrapper.eq("ftype", ftype);
		queryWrapper.eq("disabled", false);
		queryWrapper.eq("shopid", userinfo.getCompanyid());
		List<Warehouse> list=warehouseService.list(queryWrapper);
		return Result.success(list);
	}
	
	@ApiOperation("仓库列表[]")
	@GetMapping("/getNamelistByAddressid/{addressid}")
	public Result<List<Warehouse>> getNamelistByAddressid(@PathVariable String  addressid){
		UserInfo userinfo = UserInfoContext.get();
		QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
		List<String> ftypes=new LinkedList<String>();
		ftypes.add("self_test");
		ftypes.add("self_usable");
		ftypes.add("self_unusable");
		queryWrapper.in("ftype", ftypes);
		queryWrapper.eq("disabled", false);
		queryWrapper.eq("addressid", addressid);
		queryWrapper.eq("shopid", userinfo.getCompanyid());
		List<Warehouse> list=warehouseService.list(queryWrapper);
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
	
	@ApiOperation("获取仓库")
	@GetMapping("/detail/{id}")
	public Result<Warehouse> detail(@ApiParam("查询当前仓库") @PathVariable("id") String id){
		Warehouse info =warehouseService.getById(id);
		return Result.success(info);
	}
	// 删除数据(级联删除父亲和孩子的数据)
	//disabled仓库(禁用仓库,数据库字段改为0,先去判断inventory和plan是否有记录,否则不能删除)
	@SystemControllerLog("删除仓库")
	@GetMapping(value = "deleteInfo")
    @Transactional
    @CacheEvict(value = { "warehosueCache" }, allEntries = true)
	Result<?> deleteAction(String ids)   {
		String[] idlist = ids.split(",");
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			if (StrUtil.isNotEmpty(id)) {
				  warehouseService.deleteInfoById(id);
			}
		}
		return Result.success();
	}
	
	@SystemControllerLog(  "更新仓库")
	@PostMapping(value = "updateData/{id}")
	@CacheEvict(value = { "warehosueCache" }, allEntries = true)
	public Result<Warehouse> updateDataAction(@ApiParam("查询当前仓库") @PathVariable("id") String id, @RequestBody Warehouse model) {
		UserInfo userinfo = UserInfoContext.get();
		model.setOperator(userinfo.getId());
		model.setOpttime(new Date());
		if (StrUtil.isEmpty(model.getName())) {
			throw new BizException("仓库名称不能为空!");
		}
		if (model.getStockingCycle() == null || model.getStockingCycle() < 0) {
			throw new BizException("安全库存周期不能为空!");
		}
		Warehouse oldone = warehouseService.getById(id);
		if(oldone.getIshungry()&&model.getIshungry()==false) {
			throw new BizException("修改为负库存后无法在返回正库存状态!");
		}
		warehouseService.updateMyware(model, id);
		return Result.success(model);
	}
	
	@SystemControllerLog(  "更新仓库")
	@PostMapping(value = "updateDefault/{id}")
	public Result<Warehouse> updateDefaultAction(@ApiParam("查询当前仓库") @PathVariable("id") String id) {
		UserInfo userinfo = UserInfoContext.get();
		Warehouse model = warehouseService.getById(id);
		model.setIsdefault(Boolean.TRUE);
		model.setOperator(userinfo.getId());
		model.setOpttime(new Date());
		warehouseService.updateMyware(model, id);
		return Result.success(model);
	}
	
	@SystemControllerLog(  "更新仓库次序")
	@PostMapping(value = "updateIndex")
	@CacheEvict(value = { "warehosueCache" }, allEntries = true)
	public Result<?> updateDataIndexAction(@RequestBody List<Warehouse> list) {
		UserInfo userinfo = UserInfoContext.get();
		if(list!=null&&list.size()>0) {
			for(Warehouse item:list) {
				Warehouse oldone=warehouseService.getById(item.getId());
				if(oldone!=null) {
					oldone.setFindex(item.getFindex());
					oldone.setOperator(userinfo.getId());
					oldone.setOpttime(new Date());
					warehouseService.updateMyware(oldone, oldone.getId());
				}
			}
		}
		return Result.success();
	}
	
	@SystemControllerLog( "新增仓库")
	@PostMapping(value = "saveData")
	@CacheEvict(value = { "warehosueCache" }, allEntries = true)
	public Result<Warehouse> saveDataAction(@RequestBody Warehouse model) throws Exception {
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		//新增仓库下的仓位list
		if (StrUtil.isEmpty(model.getName())) {
			throw new BizException("仓库名称不能为空!");
		}
		model.setOperator(userinfo.getId());
		model.setOpttime(new Date());
		model.setShopid(shopid);
		model.setDisabled(false);
		String sernum = iSerialNumService.readSerialNumber(shopid, "M");
		model.setNumber(sernum);
		Boolean isdefault = false;
		// 判断是否有默认仓库 如果没有 新加仓库为默认
		List<Warehouse> list = warehouseService.findBydefault(shopid);
		if (list == null || list.size() <= 0) {
			isdefault = true;
		} else {
			isdefault = false;
			if (model.getIsdefault() == null) {
				isdefault = false;
			} else {
				isdefault = model.getIsdefault();
			}
		}
		if (isdefault == true) {
			warehouseService.clearDefaultWare(userinfo.getCompanyid(),model.getFtype());
		}
		warehouseService.saveMyware(model);
		return Result.success(model);
	}
	
}

