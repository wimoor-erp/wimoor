package com.wimoor.erp.purchase.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseMaterial;

public interface IPurchaseWareHouseMaterialService extends IService<PurchaseWareHouseMaterial>{

	public void savePurchaseWareHouseMaterial(PurchaseWareHouseMaterial purchaseWareHouseMaterial);
	
	public List<PurchaseWareHouseMaterial> getPurchaseForPlanIdAndWareHouseId(String planid, String warehouseid);
	
	public List<PurchaseWareHouseMaterial> getPurchaseForPlanIdAndMaterialId(String planid, String materialid);

	public void uploadWarhouseSKUFile(UserInfo user, Row info, String planid);
}
