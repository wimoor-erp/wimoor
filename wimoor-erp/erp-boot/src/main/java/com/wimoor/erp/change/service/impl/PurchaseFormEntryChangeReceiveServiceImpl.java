package com.wimoor.erp.change.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.change.mapper.PurchaseFormEntryChangeMapper;
import com.wimoor.erp.change.mapper.PurchaseFormEntryChangeReceiveMapper;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChange;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChangeReceive;
import com.wimoor.erp.change.service.IPurchaseFormEntryChangeReceiveService;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
@Service("purchaseFormEntryChangeReceiveService")
@RequiredArgsConstructor
public class PurchaseFormEntryChangeReceiveServiceImpl extends ServiceImpl<PurchaseFormEntryChangeReceiveMapper, PurchaseFormEntryChangeReceive> implements IPurchaseFormEntryChangeReceiveService {

	@Resource
	PurchaseFormEntryChangeMapper purchaseFormEntryChangeMapper;
	
	final IInventoryService iInventoryService;
	
	@Override
	public Map<String, Object> saveMineAndinStock(PurchaseFormEntryChangeReceive purchaseChangeReceive, UserInfo user) {
		Map<String, Object> map=new HashedMap<String, Object>();
		//每次入库操作 增加可用库存 减少待入库
		PurchaseFormEntryChange entry = purchaseFormEntryChangeMapper.selectById(purchaseChangeReceive.getEntrychangeid());
		InventoryParameter parameter = new InventoryParameter();
		parameter.setShopid(user.getCompanyid());
		parameter.setFormid(purchaseChangeReceive.getEntrychangeid());
		parameter.setFormtype("change");
		parameter.setNumber(entry.getNumber());
		parameter.setWarehouse(entry.getWarehouseid());
		parameter.setOperator(purchaseChangeReceive.getOperator());
		parameter.setMaterial(entry.getMaterialid());
		parameter.setAmount(purchaseChangeReceive.getAmount());
		//已入库
		int oldin=entry.getTotalin()!=null?entry.getTotalin():0;
		//总退货量，继续要操作的数量
		int totalNeedAmount=entry.getAmount();
		//还需要入的库存数量
		int needAmount=totalNeedAmount-oldin;
		//本次要入库的数量
		int nowin=purchaseChangeReceive.getAmount();
		int nowNeedAmountSubNum=0;
		if(needAmount>0) {
			if(needAmount>=nowin) {
				nowNeedAmountSubNum=nowin;
			}else {
				nowNeedAmountSubNum=needAmount;
			}
			
		}
		entry.setTotalin(purchaseChangeReceive.getAmount()+entry.getTotalin());
		//可用库存增加
		iInventoryService.inStockByDirect(parameter);
		//把待入库库存减少 
		parameter.setStatus(EnumByInventory.Ready);
		parameter.setAmount(nowNeedAmountSubNum);
		iInventoryService.SubStockByStatus(parameter, Status.inbound, Operate.in) ;
		this.baseMapper.insert(purchaseChangeReceive);
		if(entry.getTotalin()>=entry.getAmount()) {
			entry.setAuditstatus(0);
		}
		purchaseFormEntryChangeMapper.updateById(entry);
		map.put("msg", "操作成功");
		return map;
	}

	@Override
	public void cancelInstock(PurchaseFormEntryChangeReceive receive, UserInfo user) {
		PurchaseFormEntryChange entry = purchaseFormEntryChangeMapper.selectById(receive.getEntrychangeid());
		LambdaQueryWrapper<PurchaseFormEntryChangeReceive> queryWrapper=new LambdaQueryWrapper<PurchaseFormEntryChangeReceive>();
		queryWrapper.eq(PurchaseFormEntryChangeReceive::getEntrychangeid, receive.getEntrychangeid());
		List<PurchaseFormEntryChangeReceive> list = this.baseMapper.selectList(queryWrapper);
		int summaryOldIn = 0;
		int summaryCancel = 0;
		for(PurchaseFormEntryChangeReceive item:list) {
			if(item.getDisable()==null||item.getDisable()==false) {
				summaryOldIn=summaryOldIn+item.getAmount();
			}else {
				summaryCancel=summaryCancel+item.getAmount();
			}
			
		}
		InventoryParameter parameter = new InventoryParameter();
		parameter.setShopid(user.getCompanyid());
		parameter.setFormid(receive.getEntrychangeid());
		parameter.setFormtype("change");
		parameter.setNumber(entry.getNumber());
		parameter.setWarehouse(entry.getWarehouseid());
		parameter.setOperator(user.getId());
		parameter.setMaterial(entry.getMaterialid());
		parameter.setAmount(receive.getAmount());
		//已入库 11
		int oldin=entry.getTotalin()!=null?entry.getTotalin():0;
		if(oldin!=summaryOldIn) {
			entry.setTotalin(summaryOldIn);
			oldin=summaryOldIn;
		}
		//总退货量，继续要操作的数量 10
		int needOut=entry.getAmount();
		//还需要入的库存数量 1
		int hasInbound=oldin>needOut?needOut:oldin;
		//本次要入库的数量 10
		int nowout=receive.getAmount();
		int nowNeedAmountSubNum=0;
		if(hasInbound>0&&oldin-nowout<=hasInbound) {
			nowNeedAmountSubNum=hasInbound-(oldin-nowout);
		} 
		//可用库存减少
		iInventoryService.SubStockByStatus(parameter, Status.fulfillable, Operate.cancel) ;
		//把待入库库存增加 
		if(nowNeedAmountSubNum>0) {
			    parameter.setStatus(EnumByInventory.alReady);
			    parameter.setAmount(nowNeedAmountSubNum);
				iInventoryService.AddStockByStatus(parameter, Status.inbound, Operate.cancel) ;
		}
	  
	    entry.setTotalin(entry.getTotalin()-receive.getAmount());
	    receive.setDisable(true);
	    receive.setOpttime(new Date());
	    receive.setOperator(user.getId());
	    this.baseMapper.updateById(receive);
	    purchaseFormEntryChangeMapper.updateById(entry);
	}

}
