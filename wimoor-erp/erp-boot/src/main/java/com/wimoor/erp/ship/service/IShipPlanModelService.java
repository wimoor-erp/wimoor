package com.wimoor.erp.ship.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.ship.pojo.entity.ShipPlanModel;


public interface IShipPlanModelService  extends IService<ShipPlanModel>{
	public boolean deleteByPlanid(String planid);

	public ShipPlanModel findOne(String planid, String groupid, String warehouseid);

}
