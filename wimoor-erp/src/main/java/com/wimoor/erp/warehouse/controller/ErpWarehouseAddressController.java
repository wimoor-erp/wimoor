package com.wimoor.erp.warehouse.controller;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.pojo.dto.ErpWarehouseAddressDTO;
import com.wimoor.erp.warehouse.pojo.entity.ErpWarehouseAddress;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IErpWarehouseAddressService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-29
 */
@Api(tags = "仓库地址接口")
@RestController
@RequestMapping("/api/v1/warehouse/address")
@RequiredArgsConstructor
public class ErpWarehouseAddressController {
	final IErpWarehouseAddressService iErpWarehouseAddressService;
	final IWarehouseService warehouseService;

@ApiOperation(value = "查询地址")
@PostMapping("list")
public Result<Page<ErpWarehouseAddress>> list(@RequestBody ErpWarehouseAddressDTO dto) {
	 UserInfo userinfo = UserInfoContext.get();
	 LambdaQueryWrapper<ErpWarehouseAddress> queryName=new LambdaQueryWrapper<ErpWarehouseAddress>();
	 queryName.eq(ErpWarehouseAddress::getShopid, userinfo.getCompanyid());
	 if(StrUtil.isNotBlank(dto.getName())) {
		 queryName.eq(ErpWarehouseAddress::getName, dto.getName());
	 }
	 if(StrUtil.isEmpty(dto.getStatus())) {
		 queryName.eq(ErpWarehouseAddress::getDisabled, false);
	 }else if(dto.getStatus().equals("1")) {
		 queryName.eq(ErpWarehouseAddress::getDisabled, false);
	 }else {
		 queryName.eq(ErpWarehouseAddress::getDisabled, true);
	 }
	 Page<ErpWarehouseAddress> page = iErpWarehouseAddressService.page(dto.getPage(),queryName);
	 if(page!=null&&page.getRecords().size()>0) {
		 for(ErpWarehouseAddress item:page.getRecords()) {
			 item.setBoundWarehouseList(boundWarehouse(userinfo.getCompanyid(),item.getId().toString()));
		 }
	 }
     return Result.success(page);
}

private List<Warehouse> boundWarehouse(String shopid,String addressid){
	LambdaQueryWrapper<Warehouse> query=new LambdaQueryWrapper<Warehouse>();
	query.eq(Warehouse::getShopid, shopid);
	query.eq(Warehouse::getAddressid, addressid);
	query.eq(Warehouse::getDisabled, Boolean.FALSE);
	return warehouseService.list(query);
}

@ApiOperation(value = "新增地址")
@PostMapping
public Result<String> add(@RequestBody ErpWarehouseAddress address) {
	 UserInfo userinfo = UserInfoContext.get();
	 LambdaQueryWrapper<ErpWarehouseAddress> queryName=new LambdaQueryWrapper<ErpWarehouseAddress>();
	 queryName.eq(ErpWarehouseAddress::getShopid, userinfo.getCompanyid());
	 queryName.eq(ErpWarehouseAddress::getName, address.getName());
	 long count = iErpWarehouseAddressService.count(queryName);
	 if(count>0) {
		 throw new BizException("地址名称不能重复");
	 }
	 LambdaQueryWrapper<ErpWarehouseAddress> queryNumber=new LambdaQueryWrapper<ErpWarehouseAddress>();
	 queryNumber.eq(ErpWarehouseAddress::getShopid, userinfo.getCompanyid());
	 queryNumber.eq(ErpWarehouseAddress::getNumber, address.getNumber());
	 count = iErpWarehouseAddressService.count(queryNumber);
	 if(count>0) {
		 throw new BizException("地址编码不能重复");
	 }
	 address.setShopid(new BigInteger(userinfo.getCompanyid()));
	 address.setCreattime(LocalDateTime.now());
	 address.setOpttime(LocalDateTime.now());
	 address.setOperator(new BigInteger(userinfo.getId()));
	 address.setCreator(new BigInteger(userinfo.getId()));
	 address.setDisabled(false);
     iErpWarehouseAddressService.save(address);
    return Result.success(address.getId().toString());
}

@ApiOperation(value = "修改地址")
@PutMapping(value = "/{id}")
public Result<String> update(@PathVariable String id, @RequestBody ErpWarehouseAddress address) {
	UserInfo userinfo = UserInfoContext.get();
	 LambdaQueryWrapper<ErpWarehouseAddress> queryName=new LambdaQueryWrapper<ErpWarehouseAddress>();
	 queryName.eq(ErpWarehouseAddress::getShopid, userinfo.getCompanyid());
	 queryName.eq(ErpWarehouseAddress::getName, address.getName());
	 ErpWarehouseAddress old = iErpWarehouseAddressService.getOne(queryName);
	 if(old!=null&&!old.getId().toString().equals(id)) {
		 throw new BizException("地址名称不能重复");
	 }
	 LambdaQueryWrapper<ErpWarehouseAddress> queryNumber=new LambdaQueryWrapper<ErpWarehouseAddress>();
	 queryNumber.eq(ErpWarehouseAddress::getShopid, userinfo.getCompanyid());
	 queryNumber.eq(ErpWarehouseAddress::getNumber, address.getNumber());
	 old = iErpWarehouseAddressService.getOne(queryNumber);
	 if(old!=null&&!old.getId().toString().equals(id)) {
		 throw new BizException("地址编码不能重复");
	 }
	 address.setShopid(new BigInteger(userinfo.getCompanyid()));
	 address.setCreattime(LocalDateTime.now());
	 address.setOpttime(LocalDateTime.now());
	 address.setOperator(new BigInteger(userinfo.getId()));
	 address.setCreator(new BigInteger(userinfo.getId()));
	 address.setDisabled(false);
     iErpWarehouseAddressService.updateById(address);
     return Result.success(address.getId().toString());
 }

@ApiOperation(value = "获取地址")
@GetMapping(value = "/{id}")
public Result<ErpWarehouseAddress> detail(@PathVariable String id) {
	 ErpWarehouseAddress address = iErpWarehouseAddressService.getById(id);
     return Result.success(address);
 }

@ApiOperation(value = "删除地址")
@GetMapping(value = "/disabled/{ids}")
public Result<ErpWarehouseAddress> detail(@PathVariable String ids, Boolean disable) {
	 if(StrUtil.isNotEmpty(ids)) {
		 if(disable==null) {
			 throw new BizException("状态不能为空");
		 }
		 String[] idarray=ids.split(",");
		 for(String iditem:idarray) {
			 ErpWarehouseAddress address = iErpWarehouseAddressService.getById(iditem);
			 address.setDisabled(disable);
			 iErpWarehouseAddressService.updateById(address);
		 }
	 }else {
		 throw new BizException("id不能为空");
	 }
	 return Result.judge(true);
	
 }

@ApiOperation(value = "绑定地址")
@GetMapping(value = "/bind/{id}")
public Result<ErpWarehouseAddress> bind(@PathVariable String id, String warehouseids) {
	 if(StrUtil.isNotEmpty(id)&&StrUtil.isNotEmpty(warehouseids)) {
		 ErpWarehouseAddress address = iErpWarehouseAddressService.getById(id);
		 if(address==null) {
			 throw new BizException("地址ID为空");
		 }
		 String[] idarray=warehouseids.split(",");
		 for(String iditem:idarray) {
			 Warehouse warehouse = warehouseService.getById(iditem);
			 warehouse.setAddressid(id);
			 warehouseService.updateById(warehouse);
		 }
	 }else {
		 throw new BizException("id不能为空");
	 }
	 return Result.judge(true);
	
 }

@ApiOperation(value = "接除绑定")
@GetMapping(value = "/unbind/{id}")
public Result<?> unbind(@PathVariable String id, String warehouseids) {
	 if(StrUtil.isNotEmpty(id)&&StrUtil.isNotEmpty(warehouseids)) {
		 ErpWarehouseAddress address = iErpWarehouseAddressService.getById(id);
		 if(address==null) {
			 throw new BizException("地址ID为空");
		 }
		 String[] idarray=warehouseids.split(",");
		 for(String iditem:idarray) {
			 Warehouse warehouse = warehouseService.getById(iditem);
			 warehouse.setAddressid("0");
			 warehouseService.updateById(warehouse);
		 }
	 }else {
		 throw new BizException("id不能为空");
	 }
	 return Result.judge(true);
   }

}

