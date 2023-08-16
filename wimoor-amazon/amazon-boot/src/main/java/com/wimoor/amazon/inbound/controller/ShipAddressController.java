package com.wimoor.amazon.inbound.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.inbound.pojo.dto.AddressListDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
 
@Api(tags = "发货地址接口")
@RestController
@SystemControllerLog("发货地址模块")
@RequestMapping("/api/v1/shipaddress")
@RequiredArgsConstructor
public class ShipAddressController {

	final IShipAddressService shipAddressService;
	
	@ApiOperation("地址列表")
    @PostMapping("/list")
	public Result<IPage<ShipAddress>> getListData(@RequestBody AddressListDTO dto ){
	    UserInfo user = UserInfoContext.get();
	    String shopid =  user.getCompanyid();
		if("null".equals(dto.getIsdisable()) || dto.getIsdisable()==null || ("").equals(dto.getIsdisable())|| ("false").equals(dto.getIsdisable())) {
			dto.setIsdisable(null);
		} 
		if(StrUtil.isEmpty(dto.getSearch())) {
			dto.setSearch(null);
		}
		IPage<ShipAddress> list = shipAddressService.findByCondition(dto.getPage(),shopid,dto.getGroupid(),dto.getIsdisable(),dto.getSearch());
		return Result.success(list);
	}

	@ApiOperation("展示地址信息")
    @GetMapping("/view")
	public Result<ShipAddress> viewAction(@ApiParam("地址ID")@RequestParam String id) throws Exception {
		if (id != null && id != "") {
			ShipAddress data = shipAddressService.getById(id);
			return Result.success(data);
		}else {
			return Result.failed();
		}
		
	}

	@ApiOperation("保存")
	@SystemControllerLog("保存")
	@PostMapping("/saveData")
	public Result<Boolean> saveDataAction(@RequestBody ShipAddress shipaddress) throws BizException {
	    UserInfo user = UserInfoContext.get();
		shipaddress.setIsfrom(true);
		shipaddress.setOperator(user.getId().toString());
		shipaddress.setShopid(user.getCompanyid());
		shipaddress.setOpttime(new Date());
		//判断是否存在已有相同名字的地址
		String adName = shipaddress.getName();
		QueryWrapper<ShipAddress> qr=new QueryWrapper<ShipAddress>();
		qr.eq("name",adName);
		qr.eq("groupid",shipaddress.getGroupid());
		qr.eq("city",shipaddress.getCity());
		List<ShipAddress> adlist = shipAddressService.list(qr);
		if(adlist!=null && adlist.size()>0) {
			throw new BizException("当前存在已有相同名字的地址！");
		}
		// 当选择默认地址时，把当前的店铺里的old的默认地址去除，再给当前对象赋值
	    if (shipaddress.getIsdefault()!=null 
	    		&&shipaddress.getIsdefault()==true
	    		&& StrUtil.isNotEmpty(shipaddress.getGroupid())) {
				shipaddress.setIsdefault(true);
				QueryWrapper<ShipAddress> query=new QueryWrapper<ShipAddress>();
				query.eq("shopid", shipaddress.getShopid());
				query.eq("isdefault", shipaddress.getIsdefault());
				query.eq("isfrom", 1);
				query.eq("groupid", shipaddress.getGroupid());
				List<ShipAddress> oldlist = shipAddressService.list(query);
				if (oldlist != null && oldlist.size() > 0) {
					ShipAddress oldaddress = oldlist.get(0);
					oldaddress.setIsdefault(false);
					shipAddressService.updateById(oldaddress);
				}
			} else {
				shipaddress.setIsdefault(false);
			}
		boolean result = shipAddressService.save(shipaddress);
		return Result.success(result);
	}

	@ApiOperation("更新")
	@SystemControllerLog("更新")
	@PostMapping("/updateData")
	public Result<Boolean> updateDataAction(@RequestBody ShipAddress shipaddress) throws BizException {
		UserInfo user = UserInfoContext.get();
		shipaddress.setOperator(user.getId());
		shipaddress.setOpttime(new Date());
		shipaddress.setIsfrom(true);
		shipaddress.setShopid(user.getCompanyid());
		//判断是否存在已有相同名字的地址
		String adName = shipaddress.getName();
		QueryWrapper<ShipAddress> qr=new QueryWrapper<ShipAddress>();
		qr.eq("name",adName);
		qr.eq("groupid",shipaddress.getGroupid());
		qr.eq("city",shipaddress.getCity());
		List<ShipAddress> adlist = shipAddressService.list(qr);
		if(adlist!=null && adlist.size()>0) {
			if(!adlist.get(0).getId().equals(shipaddress.getId())) {
				throw new BizException("当前存在已有相同名字的地址！");
			}
		}
		// 当选择默认地址时，把old的默认地址去除，再给当前对象赋值
			if (shipaddress.getIsdefault() && StrUtil.isNotEmpty(shipaddress.getGroupid())) {
				shipaddress.setIsdefault(true);
				QueryWrapper<ShipAddress> query=new QueryWrapper<ShipAddress>();
				query.eq("shopid", shipaddress.getShopid());
				query.eq("isdefault", 1);
				query.eq("isfrom", 1);
				query.eq("groupid", shipaddress.getGroupid());
				List<ShipAddress> oldaddresslist = shipAddressService.list(query);
				for (ShipAddress oldaddress : oldaddresslist) {
					oldaddress.setIsdefault(false);
					shipAddressService.updateById(oldaddress);
				}
			} else {
				shipaddress.setIsdefault(false);
			}
		  
		boolean result = shipAddressService.updateById(shipaddress);
		return Result.success(result);
	}

	@ApiOperation("获取地址")
	@GetMapping("/getAddress")
	public Result<List<ShipAddress>> getAddressAction()  {
		UserInfo user = UserInfoContext.get();
		QueryWrapper<ShipAddress> query=new QueryWrapper<ShipAddress>();
		query.eq("shopid",user.getCompanyid());
		query.eq("isfrom", 1);
		List<ShipAddress> list = shipAddressService.list(query);
		return Result.success(list);
	}

	@ApiOperation("根据ID获取地址")
	@GetMapping("/getAddressByid")
	public Result<List<ShipAddress>> getAddreObjAction(@ApiParam("店铺ID")@RequestParam String groupid,
			                                           @ApiParam("地址ID")@RequestParam String addressid)  {
		UserInfo user = UserInfoContext.get();
		if (StrUtil.isNotEmpty(addressid)) {
			List<ShipAddress> list =new ArrayList<ShipAddress>();
			ShipAddress address = shipAddressService.getById(addressid);
			list.add(address);
			return	Result.success(list);
		} else {
			if (StrUtil.isNotEmpty(groupid)) {
				String shopid =user.getCompanyid();
				QueryWrapper<ShipAddress> query=new QueryWrapper<ShipAddress>();
				query.eq("shopid", shopid);
				query.eq("isfrom", 1);
				query.eq("disable",false);
				query.eq("groupid", groupid);
				query.orderByDesc("isdefault");
				List<ShipAddress> list = shipAddressService.list(query);
				return Result.success(list);
			} else {
				return Result.failed();
			}
		}
	}


	
	@ApiOperation("归档")
	@SystemControllerLog("归档")
	@GetMapping("/disable")
	public Result<Boolean> disableAction(@ApiParam("归档的ID多个逗号分隔")@RequestParam String ids)  {
		if(StrUtil.isNotBlank(ids)) {
			String[] array = ids.split(",");
			for(int i=0;i<array.length;i++) {
				String id=array[i];
				ShipAddress entity = shipAddressService.getById(id);
				entity.setDisable(true);
				shipAddressService.updateById(entity);
			}
		}
		return Result.success();
	}

	@ApiOperation("设置默认")
	@SystemControllerLog("默认")
	@GetMapping("/updateDefault")
	public Result<Boolean> updateAddressDefaultAction(@ApiParam("店铺ID")@RequestParam String groupid,
                                                          @ApiParam("地址ID")@RequestParam String addressid) {
		UserInfo user = UserInfoContext.get();
		ShipAddress shipaddress = shipAddressService.getById(addressid);
		// 当选择默认地址时，把old的默认地址去除，再给当前对象赋值
		if (StrUtil.isNotEmpty(groupid)) {
			shipaddress.setIsdefault(true);
			QueryWrapper<ShipAddress> query=new QueryWrapper<ShipAddress>();
			query.eq("shopid", user.getCompanyid());
			query.eq("isdefault", 1);
			query.eq("isfrom", 1);
			query.eq("groupid", groupid);
			List<ShipAddress> oldaddresslist = shipAddressService.list(query);
			for (ShipAddress oldaddress : oldaddresslist) {
				oldaddress.setIsdefault(false);
				shipAddressService.updateById(oldaddress);
			}
		} else {
			shipaddress.setIsdefault(false);
		}
		boolean result = shipAddressService.updateById(shipaddress);
		return Result.success(result);
	}

}
