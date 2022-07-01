package com.wimoor.erp.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.inventory.service.IStockCycleService;
import com.wimoor.erp.warehouse.mapper.StockCycleMapper;
import com.wimoor.erp.warehouse.pojo.entity.StockCycle;

import lombok.RequiredArgsConstructor;

@Service("stockCycleService")
@RequiredArgsConstructor
public class StockCycleServiceImpl extends  ServiceImpl<StockCycleMapper,StockCycle> implements IStockCycleService {
 

	public List<Map<String, Object>> selectByMaterial(String id) {
		return this.baseMapper.selectByMaterial(id);
	}

	public void deleteByMaterial(String id) {
		 this.baseMapper.deleteByMaterial(id);
	}
	
	public boolean updateStockCycle(Map<String, Object> map, UserInfo user) {
		// 更新stockcycle表的天数
		if(map.get("materialid") == null || map.get("warehouseid") == null) {
			return false;
		}
		String materialid = map.get("materialid").toString();
		String warehouseid = map.get("warehouseid").toString();
		Integer num = map.get("num") == null ? null : Integer.parseInt(map.get("num").toString());
        QueryWrapper<StockCycle> queryWrapper = new QueryWrapper<StockCycle>();
		queryWrapper.eq("warehouseid", warehouseid);
		queryWrapper.eq("materialid", materialid);
		StockCycle stockcy =  this.baseMapper.selectOne(queryWrapper);
		if(stockcy == null) {
			stockcy = new StockCycle();
			stockcy.setStockingcycle(num);
			stockcy.setMaterialid(materialid);
			stockcy.setWarehouseid(warehouseid);
			stockcy.setOperator(user.getId());
			stockcy.setOperator(user.getId());
			return this.save(stockcy);
		}else {
			stockcy.setStockingcycle(num);
			stockcy.setOperator(user.getId());
			stockcy.setOperator(user.getId());
			return this.updateById(stockcy);
		}
	}
	
	public boolean updateMinCycle(Map<String, Object> map, UserInfo user) {
		if(map.get("materialid") == null || map.get("warehouseid") == null) {
			return false;
		}
		String materialid = map.get("materialid").toString();
		String warehouseid = map.get("warehouseid").toString();
         QueryWrapper<StockCycle> queryWrapper = new QueryWrapper<StockCycle>();
		queryWrapper.eq("warehouseid", warehouseid);
		queryWrapper.eq("materialid", materialid);
		StockCycle stockcy =this.baseMapper.selectOne(queryWrapper);
		Integer num = map.get("num") == null ? null : Integer.parseInt(map.get("num").toString());
		if (stockcy != null) {
			stockcy.setMincycle(num);
			stockcy.setOperator(user.getId());
			stockcy.setOpttime(new Date());
			return this.updateById(stockcy);
		}else {
			stockcy = new StockCycle();
			stockcy.setMincycle(num);
			stockcy.setMaterialid(materialid);
			stockcy.setWarehouseid(warehouseid);
			stockcy.setOperator(user.getId());
			stockcy.setOpttime(new Date());
			return this.save(stockcy);
		}
	}
	
	public StockCycle findByMaterialAndWarehouse(String materialid, String warehouseid) {
		StockCycle result = null;
		QueryWrapper<StockCycle> queryWrapper = new QueryWrapper<StockCycle>();;
		queryWrapper.eq("materialid", materialid);
		queryWrapper.eq("warehouseid", warehouseid);
		List<StockCycle> cyclelist =  this.baseMapper.selectList(queryWrapper);
		if (cyclelist != null && cyclelist.size() > 0) {
			result = cyclelist.get(0);
		}
		return result;
	}
 
}
