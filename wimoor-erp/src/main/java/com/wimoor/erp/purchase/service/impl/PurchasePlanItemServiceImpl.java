package com.wimoor.erp.purchase.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.mapper.PurchasePlanItemMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

@Service("purchasePlanItemService")
@RequiredArgsConstructor
public class PurchasePlanItemServiceImpl extends  ServiceImpl<PurchasePlanItemMapper,PurchasePlanItem> implements IPurchasePlanItemService {
     final IWarehouseService iWarehouseService;
     final IStepWisePriceService iStepWisePriceService;
	@Override
	public Map<String, Object> getSummary(String shopid, String planid) {
		// TODO Auto-generated method stub
		return this.baseMapper.getSummary(shopid, planid);
	}

	@Override
	public List<Map<String, Object>> getHisList(String shopid, String warehouseid) {
		return this.baseMapper.planhis(shopid, warehouseid);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getList(String shopid, String planid) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.baseMapper.listConfirmInfo(shopid,planid);
		Map<String,Map<String,Object>> warehouiseMap =new HashMap<String,Map<String,Object>>();
		for(Map<String,Object> item:list) {
			Map<String,Object> vitem = warehouiseMap.get(item.get("warehouseid").toString());
			if(vitem==null) {
				vitem=new HashMap<String,Object>();
			}
			vitem.put("name", item.get("warehousename"));
			vitem.put("id", item.get("warehouseid"));
			List<Map<String,Object>> vlist=null;
			if(vitem.get("list")!=null) {
				vlist=(List<Map<String, Object>>) vitem.get("list");
			}else {
				vlist=new ArrayList<Map<String, Object>>();
			}
			Map<String, Object> pricemap = iStepWisePriceService.getMaterialPriceByAmount(item.get("materialid").toString(), Integer.parseInt(item.get("amount").toString()));
			if(pricemap!=null&&pricemap.get("itemprice")!=null) {
				item.put("price", pricemap.get("itemprice"));
			}
			vlist.add(item);
			vitem.put("list", vlist);    
			warehouiseMap.put(item.get("warehouseid").toString(), vitem);
		}
		List<Map<String,Object>> result= new LinkedList<Map<String,Object>>();
		for(Entry<String, Map<String, Object>> entry:warehouiseMap.entrySet()) {
			result.add(entry.getValue());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getList(String shopid, String planid, String batchnumber) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list =  this.baseMapper.listBatchInfo(shopid,planid,batchnumber);
		for(Map<String,Object> item:list) {
			Map<String, Object> pricemap = iStepWisePriceService.getMaterialPriceByAmount(item.get("materialid").toString(), Integer.parseInt(item.get("amount").toString()));
			if(pricemap!=null&&pricemap.get("itemprice")!=null) {
				item.put("price", pricemap.get("itemprice"));
			}
		}
		return list;
	}

	@Override
	public void moveBatch(String shopid,String batchnumber) {
		// TODO Auto-generated method stub
		this.baseMapper.moveBatch(shopid,batchnumber);
	}

}
