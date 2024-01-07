package com.wimoor.erp.purchase.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.mapper.PurchasePlanMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanWareHouseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanWareHouse;
import com.wimoor.erp.purchase.service.IPurchasePlanWareHouseService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;
import lombok.RequiredArgsConstructor;

@Service("purchasePlanWareHouseService")
@RequiredArgsConstructor
public class PurchasePlanWareHouseServiceImpl extends  ServiceImpl<PurchasePlanWareHouseMapper,PurchasePlanWareHouse> implements IPurchasePlanWareHouseService {

	final PurchasePlanMapper purchasePlanMapper;
	final ISerialNumService serialNumService;
	 
	final IPurchaseWareHouseMaterialService purchaseWareHouseMaterialService;
 
	
	public String getPlanIdForWareHouseId(String warehouseid, String shopid){
		if(GeneralUtil.isEmpty(warehouseid)) {
			throw new ERPBizException("请选择正确的入库仓库，或者增加一个入库仓库");
		}
		String[] warehouseidArray = warehouseid.split(",");
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < warehouseidArray.length; i++) {
			list.add(warehouseidArray[i]);
		}
		QueryWrapper<PurchasePlanWareHouse> queryWrapper = new QueryWrapper<PurchasePlanWareHouse>();
		queryWrapper.in("warehouseid", list);
		queryWrapper.eq("shopid", shopid);
		List<PurchasePlanWareHouse> purchasePlanWareHouseList = this.baseMapper.selectList(queryWrapper);
		if(purchasePlanWareHouseList != null && purchasePlanWareHouseList.size() > 0) {
			return purchasePlanWareHouseList.get(0).getPlanid();
		}
		return null;
	}
	public List<PurchasePlanWareHouse> getWareHouseIdForPlanId(String planid,String shopid) {
		return  this.baseMapper.getWareHouseIdForPlanId(planid,shopid);
	}
	public int updatePlanWareHouse(List<PurchasePlanWareHouse> purchasePlanWareHouseList,String planid,String shopid) {
		int result = 0;
		List<String> oldList = new ArrayList<String>();
		List<PurchasePlanWareHouse> warehouseList = this.baseMapper.getWareHouseIdForPlanId(planid, shopid);
		for(PurchasePlanWareHouse map : warehouseList) {
			oldList.add(map.getWarehouseid());
		}
		List<PurchasePlanWareHouse> newList = new ArrayList<PurchasePlanWareHouse>();
		for(PurchasePlanWareHouse item:purchasePlanWareHouseList) {
			if(oldList.contains(item.getWarehouseid())) {
				oldList.remove(item.getWarehouseid());
			}else {
				newList.add(item);
			}
		}
		if(newList.size() > 0) {
		   result += savePlanWareHouse(newList);
		}
		if(oldList.size() > 0) {
			for(String removeWareid : oldList) {
				result += this.baseMapper.deleteById(removeWareid);
			}
		}
		return result;
	}
	
	public List<PurchasePlanWareHouse> getPurchasePlanWareHouseForShopId(String shopid){
		return this.baseMapper.getPurchasePlanWareHouseForShopId(shopid);
	}
	
	public int savePlanWareHouse(List<PurchasePlanWareHouse> purchasePlanWareHouseList) {
		if(purchasePlanWareHouseList== null||purchasePlanWareHouseList.size()==0) {
			throw new ERPBizException("请选择正确的仓库");
		}
		int result=0;
		for(int i = 0; i < purchasePlanWareHouseList.size(); i++) {
			PurchasePlanWareHouse purchasePlanWareHouse = purchasePlanWareHouseList.get(i);
			result += this.baseMapper.insert(purchasePlanWareHouse);
		}
		return result;
	}
	
	 
	public String ArrayToString(String[] array, String split) {
		StringBuffer str = new StringBuffer();
		for (String s : array) {
		    str.append(s + split);
		}
		return str.substring(0, str.length() - 1).toString();
	}
	
}