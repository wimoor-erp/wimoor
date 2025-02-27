package com.wimoor.erp.stock.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.stock.mapper.StockTakingItemMapper;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.wimoor.erp.stock.pojo.entity.StockTakingItem;
import com.wimoor.erp.stock.service.IStockTakingItemService;

@Service("stockTakingItemService")
public class StockTakingItemServiceImpl extends  ServiceImpl<StockTakingItemMapper,StockTakingItem> implements IStockTakingItemService {
	@Autowired
    @Lazy
	IInventoryService inventoryService;
	
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public void stockTakingInvOperate(StockTaking stocktaking, UserInfo user) throws BizException {
		List<String> skulist = new ArrayList<String>();
		       List<Map<String, Object>> list = this.baseMapper.getItemList(stocktaking.getId(),null,null);
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					Map<String, Object> map = list.get(j);
					int amount = Integer.parseInt(map.get("amount").toString());// 盘点数量
					int outbound = Integer.parseInt(map.get("outbound").toString());
					if (amount < outbound) {// 如果盘点数量<outbound
						skulist.add(map.get("sku").toString());
					}
				}

				if (skulist.size() > 0) {
					throw new BizException(skulist + " 的库存亏损严重（盘亏数量大于可用库存），使得不能正常完成盘点。建议：先撤销待出库的相关单据，然后点击盘点完成。");
				} else {
					for (int j = 0; j < list.size(); j++) {
						Map<String, Object> map = list.get(j);
						int amount = Integer.parseInt(map.get("amount").toString());// 盘点数量
						int fulfillable = Integer.parseInt(map.get("fulfillable").toString());
						int outbound = Integer.parseInt(map.get("outbound").toString());
						int overamount = Integer.parseInt(map.get("overamount").toString());
						int lossamount = Integer.parseInt(map.get("lossamount").toString());

						InventoryParameter invpara = new InventoryParameter();
					 
						invpara.setWarehouse(map.get("warehouseid").toString());
						if (map.get("materialid") == null)
							continue;
						invpara.setMaterial(map.get("materialid").toString());
						invpara.setShopid(stocktaking.getShopid());
						invpara.setAmount(overamount);
						invpara.setFormid(stocktaking.getId());
						invpara.setFormtype("stocktaking");
						invpara.setNumber(stocktaking.getNumber());
						invpara.setStatus(EnumByInventory.isstocktaking);
						invpara.setOperator(stocktaking.getOperator());
						invpara.setOpttime(new Date());
						if (amount >= fulfillable + outbound) {// 如果盘点数量>=账面数量
							///// 库存操作,fulfillable = fulfillable + overamount;
							inventoryService.AddStockByStatus(invpara, Status.fulfillable, Operate.in);

						} else if (amount < fulfillable + outbound && amount >= outbound) {// 如果outbound=<盘点数量<账面数量
							///// 库存操作,fulfillable = fulfillable - lossamount;
							invpara.setAmount(lossamount);
							inventoryService.SubStockByStatus(invpara, Status.fulfillable, Operate.out);
						}
					}
				}
			}
		 
	}

	public List<StockTakingItem> findByContion(String stocktakingid, String materialid, String warehouseid) {
		QueryWrapper<StockTakingItem> queryWrapper = new QueryWrapper<StockTakingItem>();
		queryWrapper.eq("stocktakingid", stocktakingid);
		queryWrapper.eq("materialid", materialid);
		queryWrapper.eq("warehouseid", warehouseid);
		List<StockTakingItem> list = this.baseMapper.selectList(queryWrapper);
		return list;
	}

	@Override
	public List<Map<String, Object>> getItemList(String id, String warehouseid, String search) {
		// TODO Auto-generated method stub
		 return this.baseMapper.getItemList(id,warehouseid,search);
	}

	
	@Override
	public IPage<Map<String, Object>> findLocalInventory(Page<Object> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.baseMapper.findLocalInventory(page, param);
	}
	
}
