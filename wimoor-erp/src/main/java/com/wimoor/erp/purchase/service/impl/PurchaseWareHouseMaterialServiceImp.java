package com.wimoor.erp.purchase.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.purchase.mapper.PurchaseWareHouseMaterialMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseMaterial;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;

@Service("purchaseWareHouseMaterialService")
public class PurchaseWareHouseMaterialServiceImp extends  ServiceImpl<PurchaseWareHouseMaterialMapper,PurchaseWareHouseMaterial> implements IPurchaseWareHouseMaterialService{

	public void savePurchaseWareHouseMaterial(PurchaseWareHouseMaterial purchaseWareHouseMaterial) {
		QueryWrapper<PurchaseWareHouseMaterial> queryWrapper = new QueryWrapper<PurchaseWareHouseMaterial>();
		queryWrapper.eq("planid", purchaseWareHouseMaterial.getPlanid());
		queryWrapper.eq("materialid", purchaseWareHouseMaterial.getMaterialid());
		List<PurchaseWareHouseMaterial> purchaseList = list(queryWrapper);
		if(purchaseList != null &&purchaseList.size() > 0) {
			PurchaseWareHouseMaterial purchase = purchaseList.get(0);
			if(!purchase.getWarehouseid().equals(purchaseWareHouseMaterial.getWarehouseid())) {
				this.baseMapper.updateById(purchaseWareHouseMaterial);
			}
		}else {
			this.baseMapper.insert(purchaseWareHouseMaterial);
		}
	}
	
	public List<PurchaseWareHouseMaterial> getPurchaseForPlanIdAndMaterialId(String planid, String materialid) {
		QueryWrapper<PurchaseWareHouseMaterial> queryWrapper = new QueryWrapper<PurchaseWareHouseMaterial>();
		queryWrapper.eq("planid", planid);
		queryWrapper.eq("materialid", materialid);
		return list(queryWrapper);
	}
	
	public List<PurchaseWareHouseMaterial> getPurchaseForPlanIdAndWareHouseId(String planid, String warehouseid) {
		QueryWrapper<PurchaseWareHouseMaterial> queryWrapper = new QueryWrapper<PurchaseWareHouseMaterial>();
		queryWrapper.eq("planid", planid);
		queryWrapper.eq("warehouseid", warehouseid);
		return list(queryWrapper);
	}
}
