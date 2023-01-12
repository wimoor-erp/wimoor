package com.wimoor.erp.ship.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.ship.mapper.ShipPlanModelItemMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlanModelItem;
import com.wimoor.erp.ship.service.IShipPlanModelItemService;

import lombok.RequiredArgsConstructor;

@Service("shipPlanModelItemService")
@RequiredArgsConstructor
public class ShipPlanModelItemServiceImpl extends  ServiceImpl<ShipPlanModelItemMapper,ShipPlanModelItem> implements IShipPlanModelItemService {
	
	public int deleteByModelid(String modelid){
		QueryWrapper<ShipPlanModelItem> queryWrapper = new QueryWrapper<ShipPlanModelItem>();
		queryWrapper.eq("modelid",modelid);
	   return this.baseMapper.delete(queryWrapper);
    }
	
	 
	public ShipPlanModelItem getModelItem(String modelid,String materialid) {
		   LambdaQueryWrapper<ShipPlanModelItem> queryModelItem=new LambdaQueryWrapper<ShipPlanModelItem>();
 	   queryModelItem.eq(ShipPlanModelItem::getModelid, modelid);
 	   queryModelItem.eq(ShipPlanModelItem::getMaterialid, materialid);
 	   return  this.baseMapper.selectOne(queryModelItem);
	}
}
