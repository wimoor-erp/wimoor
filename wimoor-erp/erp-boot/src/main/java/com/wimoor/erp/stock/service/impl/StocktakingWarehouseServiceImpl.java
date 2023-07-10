package com.wimoor.erp.stock.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.stock.mapper.StocktakingWarehouseMapper;
import com.wimoor.erp.stock.pojo.entity.StocktakingWarehouse;
import com.wimoor.erp.stock.service.IStocktakingWarehouseService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-05
 */
@Service
public class StocktakingWarehouseServiceImpl extends ServiceImpl<StocktakingWarehouseMapper, StocktakingWarehouse> implements IStocktakingWarehouseService {
	@Autowired
	@Lazy
	IWarehouseService iWarehouseService;
	
	  	public List<StocktakingWarehouse> listData(String stocktakingid){
	      	List<StocktakingWarehouse> list = this.baseMapper.selectList(new LambdaQueryWrapper<StocktakingWarehouse>().eq(StocktakingWarehouse::getStocktakingid, stocktakingid));
	  		return list;
	  	}
	
	public void saveData(List<StocktakingWarehouse> list){
		for(StocktakingWarehouse item:list) {
			Warehouse warehouse = iWarehouseService.getById(item.getWarehouseid());
			if (warehouse==null) {
				throw new BizException("该仓库已不存在或者删除！");
			} 
			if (warehouse.getIsstocktaking()) {
				throw new BizException("该仓库正在盘点，请稍后再试！");
			}  
			warehouse.setIsstocktaking(true);
			iWarehouseService.updateById(warehouse);
    	}
    	if(list!=null&&list.size()>0) {
    		BigInteger stockid = list.get(0).getStocktakingid();
    		this.baseMapper.delete(new LambdaQueryWrapper<StocktakingWarehouse>().eq(StocktakingWarehouse::getStocktakingid, stockid));
    	}
    	for(StocktakingWarehouse item:list) {
    		this.baseMapper.insert(item);
    	}
    	
	  }

	@Override
	public void cancel(String id) {
		// TODO Auto-generated method stub
		List<StocktakingWarehouse> list = this.baseMapper.selectList(new LambdaQueryWrapper<StocktakingWarehouse>().eq(StocktakingWarehouse::getStocktakingid, id));
		for(StocktakingWarehouse item:list) {
			Warehouse warehouse = iWarehouseService.getById(item.getWarehouseid());
			warehouse.setIsstocktaking(false);
			iWarehouseService.updateById(warehouse);
			this.baseMapper.delete(new LambdaQueryWrapper<StocktakingWarehouse>()
					.eq(StocktakingWarehouse::getStocktakingid, id)
					.eq(StocktakingWarehouse::getWarehouseid, item.getWarehouseid()));
    	}
	}

	@Override
	public void end(String id) {
		// TODO Auto-generated method stub
		List<StocktakingWarehouse> list = this.baseMapper.selectList(new LambdaQueryWrapper<StocktakingWarehouse>().eq(StocktakingWarehouse::getStocktakingid, id));
		for(StocktakingWarehouse item:list) {
			Warehouse warehouse = iWarehouseService.getById(item.getWarehouseid());
			warehouse.setIsstocktaking(false);
			iWarehouseService.updateById(warehouse);
    	}
	}
}
