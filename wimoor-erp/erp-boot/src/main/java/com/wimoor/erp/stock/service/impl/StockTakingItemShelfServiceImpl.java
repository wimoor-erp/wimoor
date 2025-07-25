package com.wimoor.erp.stock.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.mapper.StockTakingItemShelfMapper;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.wimoor.erp.stock.pojo.entity.StockTakingItemShelf;
import com.wimoor.erp.stock.service.IStockTakingItemShelfService;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventory;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-03
 */
@Service
public class StockTakingItemShelfServiceImpl extends ServiceImpl<StockTakingItemShelfMapper, StockTakingItemShelf> implements IStockTakingItemShelfService {
 @Autowired
 @Lazy
 IWarehouseShelfInventoryService iWarehouseShelfInventoryService;

@Override
public void stockTakingInvOperate(StockTaking stocktaking, UserInfo user) {
	// TODO Auto-generated method stub
	    List<StockTakingItemShelf> list = this.lambdaQuery().eq(StockTakingItemShelf::getStocktakingid, stocktaking.getId()).list();
		if (list != null && list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				 StockTakingItemShelf map = list.get(j);
				int amount = map.getAmount();// 盘点数量
				WarehouseShelfInventory inv = iWarehouseShelfInventoryService.lambdaQuery()
				.eq(WarehouseShelfInventory::getMaterialid,map.getMaterialid())
				.eq(WarehouseShelfInventory::getWarehouseid, map.getWarehouseid())
				.eq(WarehouseShelfInventory::getShopid, stocktaking.getShopid())
				.eq(WarehouseShelfInventory::getShelfid, map.getShelfid()).one();
				 WarehouseShelfInventoryOptRecord invopt=new WarehouseShelfInventoryOptRecord();
				 invopt.setMaterialid(map.getMaterialid());
				 invopt.setFormtype("stocktaking");
				 invopt.setFormid(stocktaking.getId());
				 invopt.setOperator(new BigInteger(user.getId()));
				 invopt.setShelfid(map.getShelfid());
				 invopt.setWarehouseid(map.getWarehouseid());
				 invopt.setShopid(new BigInteger(user.getCompanyid()));
				 if(inv!=null) {
					 if(amount>inv.getQuantity()) {
						  invopt.setOpt(1);
						  invopt.setQuantity(amount-inv.getQuantity());
						  iWarehouseShelfInventoryService.add(invopt);
					 }else if(amount<inv.getQuantity()){
						    invopt.setOpt(0);
							invopt.setQuantity(inv.getQuantity()-amount);
							iWarehouseShelfInventoryService.sub(invopt);
					 }
 
				}else {
					  invopt.setOpt(1);
					  invopt.setQuantity(amount);
					  iWarehouseShelfInventoryService.add(invopt);
				}
			}
		}
}
 
}
