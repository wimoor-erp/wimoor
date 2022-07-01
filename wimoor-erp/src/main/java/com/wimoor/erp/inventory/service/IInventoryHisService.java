package com.wimoor.erp.inventory.service;

import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.inventory.pojo.entity.InventoryHis;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IInventoryHisService extends IService<InventoryHis>  {
	public boolean updateAll(InventoryHis entity) throws BizException ;
	public InventoryHis selectOne(InventoryHis entity) throws BizException ;
}
