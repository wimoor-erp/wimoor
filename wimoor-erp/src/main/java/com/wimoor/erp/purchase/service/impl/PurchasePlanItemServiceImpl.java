package com.wimoor.erp.purchase.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.purchase.mapper.PurchasePlanItemMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;

import lombok.RequiredArgsConstructor;
 

@Service("purchasePlanItemService")
@RequiredArgsConstructor
public class PurchasePlanItemServiceImpl extends  ServiceImpl<PurchasePlanItemMapper,PurchasePlanItem> implements IPurchasePlanItemService {
	 
	 
	public List<Map<String, Object>> getWarehouseForPlanIdAndMaterialId(String planid, String materialid) {
		// TODO Auto-generated method stub
		return this.baseMapper.getWarehouseForPlanIdAndMaterialId(planid, materialid);
	}

	@Override
	public List<Map<String, Object>> getShipInventory(String groupid, String pidlist) {
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		if(GeneralUtil.isNotEmpty(pidlist)) {
			String[] pidArr = pidlist.split(",");
			for(int i=0;i<pidArr.length;i++) {
				String pid = pidArr[i];
				String marketplaceid =pid.split(";")[1];
				pid=pid.split(";")[0];
				//Product_Info info = productInfoService.getById(pid);
				String info =null;
				if(info!=null) {
					String authid = null;//info.getAmazonauthid();
					String sku=null;//info.getSku();
					Map<String,Object> map1=this.baseMapper.getPlanInventoryInbound(groupid,marketplaceid,sku);
					Map<String,Object> map2=this.baseMapper.getShipInventoryInbound(authid,marketplaceid,sku);
					 Map<String,Object> map=new HashMap<String,Object>(); 
					 map.put("sku", sku);
					 map.put("marketplaceid", marketplaceid);
					 if(map1==null || map1.get("inbound")==null) {
						 map.put("planinbound", 0);
					 }else {
						 map.put("planinbound", map1.get("inbound"));
					 }
					 if(map2==null || map2.get("inbound")==null) {
						 map.put("shipinbound", 0);
					 }else {
						 map.put("shipinbound", map2.get("inbound"));
					 }
					 map.put("pid", pid);
					 list.add(map);
					 
				}
			}
		}
		return list;
	}

}
