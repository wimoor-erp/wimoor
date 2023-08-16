package com.wimoor.erp.ship.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.mapper.ShipPlanModelMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlanModel;
import com.wimoor.erp.ship.service.IShipPlanModelItemService;
import com.wimoor.erp.ship.service.IShipPlanModelService;
import com.wimoor.erp.ship.service.IShipPlanService;

import lombok.RequiredArgsConstructor;
@Service("shipPlanModelService")
@RequiredArgsConstructor
public class ShipPlanModelServiceImpl extends  ServiceImpl<ShipPlanModelMapper,ShipPlanModel> implements IShipPlanModelService {
	final IShipPlanModelItemService iShipPlanModelItemService;
	final IShipPlanService iShipPlanService;
    final IMaterialService iMaterialService;
    final AmazonClientOneFeignManager amazonClientOneFeign;
    
	public boolean deleteByPlanid(String planid){
		QueryWrapper<ShipPlanModel> queryWrapper = new QueryWrapper<ShipPlanModel>();
		queryWrapper.eq("planid",planid);
	   return this.remove(queryWrapper);
  }

	public ShipPlanModel getModel(UserInfo user,String planid,String groupid,String warehouseid) {
		LambdaQueryWrapper<ShipPlanModel> query=new LambdaQueryWrapper<ShipPlanModel>() ;
		query.eq(ShipPlanModel::getPlanid,planid);
		query.eq(ShipPlanModel::getGroupid, groupid);
		query.eq(ShipPlanModel::getWarehouseid, warehouseid);
		ShipPlanModel model=this.baseMapper.selectOne(query);
		if(model==null) {
			model=new ShipPlanModel();
			model.setGroupid(groupid);
			model.setPlanid(planid);
			model.setWarehouseid(warehouseid);
			model.setOperator(user.getId());
			model.setIsrun(false);
			this.baseMapper.insert(model);
		}
		return model;
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
