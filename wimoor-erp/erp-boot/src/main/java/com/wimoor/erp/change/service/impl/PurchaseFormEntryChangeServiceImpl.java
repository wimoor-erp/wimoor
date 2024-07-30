package com.wimoor.erp.change.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.change.mapper.PurchaseFormEntryChangeMapper;
import com.wimoor.erp.change.pojo.dto.PurchaseFormEntryChangeVo;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChange;
import com.wimoor.erp.change.service.IPurchaseFormEntryChangeService;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryLogisticsMapper;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
@Service("purchaseFormEntryChangeService")
@RequiredArgsConstructor
public class PurchaseFormEntryChangeServiceImpl extends ServiceImpl<PurchaseFormEntryChangeMapper, PurchaseFormEntryChange> implements IPurchaseFormEntryChangeService {
	
	final IInventoryFormAgentService inventoryFormAgentService;

	final ISerialNumService serialNumService;
	
	final IWarehouseService warehouseService;
	
	final PurchaseFormEntryLogisticsMapper purchaseFormEntryLogisticsMapper;
	
	final IInventoryService iInventoryService;
	
	
	public IPage<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.baseMapper.findByCondition(page,map);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveAction(PurchaseFormEntryChangeVo inWarehouseForm, String sku, UserInfo user) throws BizException {
		Map<String, Object> skuMap = null;
		if (GeneralUtil.isNotEmpty(sku)) {
			skuMap = (Map<String, Object>) JSON.parse(sku);
		}
		return saveForm(inWarehouseForm, skuMap, user);
	}
	
	public Map<String, Object> saveForm(PurchaseFormEntryChangeVo inWarehouseForm, Map<String, Object> skuMap, UserInfo user) throws BizException {
		int result = 0;
		String msg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (skuMap != null && skuMap.size() > 0) {
			// 入库，变动库存
			InventoryParameter parameter = new InventoryParameter();
			parameter.setShopid(inWarehouseForm.getShopid());
			parameter.setFormid(inWarehouseForm.getId());
			parameter.setFormtype("change");
			parameter.setNumber(inWarehouseForm.getNumber());
			parameter.setWarehouse(inWarehouseForm.getWarehouseid());
			parameter.setOperator(inWarehouseForm.getOperator());

			for (String skuId : skuMap.keySet()) {
				PurchaseFormEntryChange inWarehouseFormEntry = new PurchaseFormEntryChange();
				try {
					inWarehouseFormEntry.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SO"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				inWarehouseFormEntry.setMaterialid(skuId);
				inWarehouseFormEntry.setId(warehouseService.getUUID());
				inWarehouseFormEntry.setAmount(Integer.parseInt(skuMap.get(skuId).toString()));
				inWarehouseFormEntry.setSupplierid(inWarehouseForm.getSupplierid());
				inWarehouseFormEntry.setAuditstatus(1);
				inWarehouseFormEntry.setCreatetime(new Date());
				inWarehouseFormEntry.setCreator(user.getId());
				inWarehouseFormEntry.setOperator(user.getId());
				inWarehouseFormEntry.setOpttime(new Date());
				inWarehouseFormEntry.setRemark(inWarehouseForm.getRemark());
				inWarehouseFormEntry.setShopid(user.getCompanyid());
				inWarehouseFormEntry.setWarehouseid(inWarehouseForm.getWarehouseid());
				inWarehouseFormEntry.setTotalin(0);
				if(StrUtil.isNotEmpty(inWarehouseForm.getEntryid())) {
					inWarehouseFormEntry.setEntryid(inWarehouseForm.getEntryid());
				}
				this.baseMapper.insert(inWarehouseFormEntry);// 在前端传来map，在后端保存入库清单
				parameter.setMaterial(skuId);
				parameter.setAmount(Integer.parseInt(skuMap.get(skuId).toString()));
				//可用库存减少
				iInventoryService.outStockByDirect(parameter);
				//把库存回到待入库 
				parameter.setStatus(EnumByInventory.Ready);
				iInventoryService.AddStockByStatus(parameter, Status.inbound, Operate.in) ;
				result++;
			}
			 
		}
		if (result > 0) {
			msg += "操作成功！";
		} else {
			msg += "操作失败！";
		}
		map.put("msg", msg);
		return map;
	}

	@Override
	public void examineChange(PurchaseFormEntryChange entry, int banlance, UserInfo user) {
		//直接完成换货单  把待入库的差值扣减
		InventoryParameter parameter = new InventoryParameter();
		parameter.setShopid(entry.getShopid());
		parameter.setFormid(entry.getId());
		parameter.setFormtype("change");
		parameter.setNumber(entry.getNumber());
		parameter.setWarehouse(entry.getWarehouseid());
		parameter.setOperator(user.getId());
		parameter.setMaterial(entry.getMaterialid());
		parameter.setAmount(banlance);
		parameter.setStatus(EnumByInventory.Ready);
		iInventoryService.SubStockByStatus(parameter, Status.inbound, Operate.stop) ;
	}

	//重启单据
	@Override
	public void resetForm(PurchaseFormEntryChange entry) {
		int amount=entry.getAmount();//单据操作量
		int totalin=entry.getTotalin();//总共入的量
		if(totalin<amount) {
			int need = amount-totalin;
			InventoryParameter parameter = new InventoryParameter();
			parameter.setShopid(entry.getShopid());
			parameter.setFormid(entry.getId());
			parameter.setFormtype("change");
			parameter.setNumber(entry.getNumber());
			parameter.setWarehouse(entry.getWarehouseid());
			parameter.setOperator(entry.getOperator());
			parameter.setMaterial(entry.getMaterialid());
			parameter.setAmount(need);
			parameter.setStatus(EnumByInventory.Ready);
			iInventoryService.AddStockByStatus(parameter, Status.inbound, Operate.readyin) ;
		} 
		entry.setAuditstatus(1);
		this.updateById(entry);
	}
	
 


}
