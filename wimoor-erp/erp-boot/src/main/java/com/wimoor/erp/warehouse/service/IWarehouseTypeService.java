package com.wimoor.erp.warehouse.service;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseType;

public interface IWarehouseTypeService extends IService<WarehouseType> {

	public List<WarehouseType> findByShopid(String shopid);
}
