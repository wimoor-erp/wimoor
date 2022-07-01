package com.wimoor.erp.inventory.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.inventory.mapper.InventoryHisMapper;
import com.wimoor.erp.inventory.pojo.entity.InventoryHis;
import com.wimoor.erp.inventory.service.IInventoryHisService;

import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
 
 

@Service("inventoryHisService")
@RequiredArgsConstructor
public class InventoryHisServiceImpl  extends ServiceImpl<InventoryHisMapper,InventoryHis> implements IInventoryHisService {

	@Override
	public boolean updateAll(InventoryHis entity) throws BizException {
		// TODO Auto-generated method stub
		QueryWrapper<InventoryHis> updateWrapper = new QueryWrapper<InventoryHis>();
		updateWrapper.eq("warehouseid",entity.getWarehouseid());
		updateWrapper.eq("shopid",entity.getShopid());
		updateWrapper.eq("materialid",entity.getMaterialid());
		updateWrapper.eq("status",entity.getStatus());
		updateWrapper.eq("modifyday",GeneralUtil.formatDate(entity.getModifyday()));
		return   this.update(entity, updateWrapper);
		
	}
	
 
	public InventoryHis selectOne(InventoryHis entity) throws BizException {
		// TODO Auto-generated method stub
		QueryWrapper<InventoryHis> queryWrapper = new QueryWrapper<InventoryHis>();
		queryWrapper.eq("warehouseid",entity.getWarehouseid());
		queryWrapper.eq("shopid",entity.getShopid());
		queryWrapper.eq("materialid",entity.getMaterialid());
		queryWrapper.eq("status",entity.getStatus());
		queryWrapper.eq("modifyday",GeneralUtil.formatDate(entity.getModifyday()));
		List<InventoryHis> invhis = super.baseMapper.selectList(queryWrapper);
		if(invhis!=null&&invhis.size()>0)return invhis.get(0);
		return   null;
	}


 
}
