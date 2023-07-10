package com.wimoor.erp.stock.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.stock.mapper.StocktakingShelfMapper;
import com.wimoor.erp.stock.pojo.entity.StocktakingShelf;
import com.wimoor.erp.stock.service.IStocktakingShelfService;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-05
 */
@Service
public class StocktakingShelfServiceImpl extends ServiceImpl<StocktakingShelfMapper, StocktakingShelf> implements IStocktakingShelfService {
	@Autowired
	IWarehouseShelfService iWarehouseShelfService;
	
	 
  	public List<StocktakingShelf> listData(String stocktakingid){
      	List<StocktakingShelf> list = this.baseMapper.selectList(new LambdaQueryWrapper<StocktakingShelf>().eq(StocktakingShelf::getStocktakingid, stocktakingid));
  		return list;
  	}
	
	    public void saveData(List<StocktakingShelf> list){
	    	for(StocktakingShelf item:list) {
				  WarehouseShelf shelf = iWarehouseShelfService.getById(item.getShelfid());
				if (shelf==null) {
					throw new BizException("该仓库已不存在或者删除！");
				} 
				if (shelf.getIsfrozen()!=null&&shelf.getIsfrozen()==true) {
					throw new BizException("该仓库正在盘点，请稍后再试！");
				}  
				shelf.setIsfrozen(true);
				iWarehouseShelfService.updateById(shelf);
	    	}
	    	if(list!=null&&list.size()>0) {
	    		BigInteger stockid = list.get(0).getStocktakingid();
	    		this.baseMapper.delete(new LambdaQueryWrapper<StocktakingShelf>().eq(StocktakingShelf::getStocktakingid, stockid));
	    	}
	    	for(StocktakingShelf item:list) {
	    		this.baseMapper.insert(item);
	    	}
			 
		}

		@Override
		public void cancel(String id) {
			// TODO Auto-generated method stub
			List<StocktakingShelf> list = this.baseMapper.selectList(new LambdaQueryWrapper<StocktakingShelf>().eq(StocktakingShelf::getStocktakingid, id));
			for(StocktakingShelf item:list) {
				WarehouseShelf shelf = iWarehouseShelfService.getById(item.getShelfid());
				shelf.setIsfrozen(false);
				iWarehouseShelfService.updateById(shelf);
				this.baseMapper.delete(new LambdaQueryWrapper<StocktakingShelf>()
						.eq(StocktakingShelf::getStocktakingid, id)
						.eq(StocktakingShelf::getShelfid, item.getShelfid()));
	    	}
		}

		@Override
		public void end(String id) {
			// TODO Auto-generated method stub
			List<StocktakingShelf> list = this.baseMapper.selectList(new LambdaQueryWrapper<StocktakingShelf>().eq(StocktakingShelf::getStocktakingid, id));
			for(StocktakingShelf item:list) {
				WarehouseShelf shelf = iWarehouseShelfService.getById(item.getShelfid());
				shelf.setIsfrozen(false);
				iWarehouseShelfService.updateById(shelf);
	    	}
		}
}
