package com.wimoor.erp.purchase.service.impl;

import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.mapper.PurchaseWareHouseMaterialMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseMaterial;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

@Service("purchaseWareHouseMaterialService")
public class PurchaseWareHouseMaterialServiceImp extends  ServiceImpl<PurchaseWareHouseMaterialMapper,PurchaseWareHouseMaterial> implements IPurchaseWareHouseMaterialService{
    @Autowired
    IMaterialService iMaterialService;
    @Autowired
    IWarehouseService iWarehouseService;
    @Autowired
    IPurchasePlanItemService iPurchasePlanItemService;
	public void savePurchaseWareHouseMaterial(PurchaseWareHouseMaterial purchaseWareHouseMaterial) {
		QueryWrapper<PurchaseWareHouseMaterial> queryWrapper = new QueryWrapper<PurchaseWareHouseMaterial>();
		queryWrapper.eq("planid", purchaseWareHouseMaterial.getPlanid());
		queryWrapper.eq("materialid", purchaseWareHouseMaterial.getMaterialid());
		List<PurchaseWareHouseMaterial> purchaseList = list(queryWrapper);
		if(purchaseList != null &&purchaseList.size() > 0) {
			PurchaseWareHouseMaterial purchase = purchaseList.get(0);
			if(!purchase.getWarehouseid().equals(purchaseWareHouseMaterial.getWarehouseid())) {
				LambdaQueryWrapper<PurchasePlanItem> query=new LambdaQueryWrapper<PurchasePlanItem>();
				query.eq(PurchasePlanItem::getMaterialid, purchaseWareHouseMaterial.getMaterialid());
				query.eq(PurchasePlanItem::getPlanid, purchaseWareHouseMaterial.getPlanid());
				query.eq(PurchasePlanItem::getWarehouseid, purchase.getWarehouseid());
				List<PurchasePlanItem> itemlist = iPurchasePlanItemService.list(query);
				if(itemlist!=null&&itemlist.size()>0) {
					for(PurchasePlanItem item:itemlist) {
						item.setWarehouseid(purchaseWareHouseMaterial.getWarehouseid());
						iPurchasePlanItemService.updateById(item);
					}
				}
				this.baseMapper.update(purchaseWareHouseMaterial,queryWrapper);
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

	@Override
	public void uploadWarhouseSKUFile(UserInfo user, Row info, String planid) {
		// TODO Auto-generated method stub
		String sku = null;
		if (info.getCell(0) != null) {
			info.getCell(0).setCellType(CellType.STRING);
			sku = info.getCell(0).getStringCellValue();
		}
		String warehousename = null;
		if (info.getCell(1) != null) {
			info.getCell(1).setCellType(CellType.STRING);
			warehousename = info.getCell(1).getStringCellValue();
		}
		Material material = this.iMaterialService.getBySku(user.getCompanyid(), sku);
		Warehouse warehouse = this.iWarehouseService.getWarehouseByName(user.getCompanyid(), warehousename);
		if(material!=null&&warehouse!=null) {
			PurchaseWareHouseMaterial item=new PurchaseWareHouseMaterial();
			item.setMaterialid(material.getId());
			item.setWarehouseid(warehouse.getId());
			item.setPlanid(planid);
			this.savePurchaseWareHouseMaterial(item);
		}
	}
}
