package com.wimoor.erp.warehouse.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.warehouse.mapper.WarehouseTypeMapper;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseType;
import com.wimoor.erp.warehouse.service.IWarehouseTypeService;

@Service("warehouseTypeService")
public class WarehouseTypeServiceImpl extends  ServiceImpl<WarehouseTypeMapper,WarehouseType> implements IWarehouseTypeService {
 
	 public List<WarehouseType> findByShopid(String shopid){
		 QueryWrapper<WarehouseType> queryWrapper = new QueryWrapper<WarehouseType>();
		 queryWrapper.eq("shopid", shopid);
		 QueryWrapper<WarehouseType> qor = queryWrapper.or();
		 qor.eq("issystem",true);
		return this.list(queryWrapper);
		 
	 }
}
