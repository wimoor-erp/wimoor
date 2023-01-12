package com.wimoor.erp.ship.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.entity.ShipPlan;

public interface  IShipPlanService extends IService<ShipPlan> {
	void afterShipInboundPlanSave(String plansubid, String planmarketplaceid, Boolean issplit, List<String> list);
}
