package com.wimoor.erp.purchase.service.impl;

import com.wimoor.erp.purchase.pojo.entity.PurchasePlanConsumableItem;
import com.wimoor.erp.purchase.service.IPurchasePlanConsumableItemService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.mapper.PurchasePlanConsumableItemMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-08
 */
@Service
@RequiredArgsConstructor
public class PurchasePlanConsumableItemServiceImpl extends ServiceImpl<PurchasePlanConsumableItemMapper, PurchasePlanConsumableItem> implements IPurchasePlanConsumableItemService {
	
	final IStepWisePriceService iStepWisePriceService;
	final IMaterialService materialService;
	final IAssemblyService assemblyService;
	final IWarehouseService iWarehouseService;
	final IInventoryService inventoryService;
	final IAssemblyFormService assemblyFormService;
	
	@Override
	public Map<String, Object> getSummary(String shopid, String planid) {
		// TODO Auto-generated method stub
		return this.baseMapper.getSummary(shopid, planid);
	}
	
	@Override
	public List<Map<String, Object>> getList(String shopid, String warehouseid) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list =  this.baseMapper.listInfo(shopid,warehouseid);
		for(Map<String,Object> item:list) {
			Map<String, Object> pricemap = iStepWisePriceService.getMaterialPriceByAmount(item.get("mid").toString(), Integer.parseInt(item.get("amount").toString()));
			if(pricemap!=null&&pricemap.get("itemprice")!=null) {
				item.put("price", pricemap.get("itemprice"));
			}
			String mid=item.get("mid").toString();
			List<Map<String, Object>> historylist = materialService.selectProPriceHisById(mid);
			 String pricestr="";
			 if(historylist!=null && historylist.size()>0) {
				 for(int j=0;j<historylist.size();j++) {
					 Map<String,Object> history = historylist.get(j);
					 if(history!=null) {
						 if(history.get("price")!=null) {
							 String price=history.get("price").toString();
							 int len = price.indexOf(".");
							 price=price.substring(0, len+3);
							 pricestr+="历史价格("+
				    				   GeneralUtil.formatDate(GeneralUtil.getDate(history.get("opttime")))+
				    				   "): "+price+"<br/>";
						 }
					 }
				 }
				 if(GeneralUtil.isEmpty(pricestr)) {
					 pricestr="暂无价格历史!";
				 }
			 }else {
				 pricestr="暂无价格历史!";
			 }
			 item.put("pricestr", pricestr);
			 if(item.get("issfg")!=null && item.get("issfg").toString().equals("1")) {
				 List<AssemblyVO> sublist = assemblyService.selectByMainmid(mid);
				 item.put("subsku", sublist);
				 if(sublist!=null && sublist.size()>0) {
					 for (int i = 0; i < sublist.size(); i++) {
							AssemblyVO sub = sublist.get(i);
							String submid = sub.getSubmid();
							Map<String, Object> map = null;
							if (StrUtil.isNotEmpty(warehouseid)) {
								Map<String, Object> inv = inventoryService.findInvDetailById(submid, warehouseid, shopid);
								map = new HashMap<String, Object>();
								map.put("warehouseid", warehouseid);
								map.put("warehouse", iWarehouseService.getById(warehouseid).getName());
								if (inv == null) {
									map.put("amount", 0);
									map.put("inbound", 0);
								} else {
									map.put("amount", inv.get("fulfillable"));
									map.put("inbound", inv.get("inbound"));
								}
							} else {
								map = assemblyFormService.selectSubASList(null, submid == null ? null : submid.toString(), shopid);
								if (map != null && map.get("warehouseid") != null) {
									Map<String, Object> inv = 
											inventoryService.findInvDetailById(submid == null ? null : submid.toString(), map.get("warehouseid").toString(), shopid);
									if (inv != null)
										map.put("inbound", inv.get("inbound"));
								}
							}
							sub.setSubmap(map);
						}
					 item.put("checkdsub",true);
				 }else {
					 item.put("checkdsub",false);
				 }
			 }
		}
		return list;
	}
}
