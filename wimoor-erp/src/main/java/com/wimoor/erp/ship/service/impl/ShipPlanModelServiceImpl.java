package com.wimoor.erp.ship.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.ship.mapper.ShipPlanModelMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlanModel;
import com.wimoor.erp.ship.service.IShipPlanModelService;
@Service("shipPlanModelService")
public class ShipPlanModelServiceImpl extends  ServiceImpl<ShipPlanModelMapper,ShipPlanModel> implements IShipPlanModelService {
	
	public boolean deleteByPlanid(String planid){
		QueryWrapper<ShipPlanModel> queryWrapper = new QueryWrapper<ShipPlanModel>();
		queryWrapper.eq("planid",planid);
	   return this.remove(queryWrapper);
  }

	public ShipPlanModel findOne(String planid, String groupid, String warehouseid) {
		// TODO Auto-generated method stub
		QueryWrapper<ShipPlanModel> queryWrapper = new QueryWrapper<ShipPlanModel>();
		queryWrapper.eq("planid",planid);
		queryWrapper.eq("warehouseid",warehouseid);
		queryWrapper.eq("groupid",groupid);
		  List<ShipPlanModel> list = list(queryWrapper);
		if(list.size()>0)return list.get(0);
		return null;
	}
}
