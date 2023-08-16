package com.wimoor.erp.ship.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.ship.pojo.entity.ShipPlanModelItem;

public interface IShipPlanModelItemService extends IService<ShipPlanModelItem>{
	public int deleteByModelid(String modelid);
	public ShipPlanModelItem getModelItem(String modelid,String materialid) ;
}
