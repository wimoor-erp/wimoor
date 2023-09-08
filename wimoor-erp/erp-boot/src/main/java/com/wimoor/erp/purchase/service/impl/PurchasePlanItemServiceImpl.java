package com.wimoor.erp.purchase.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
 
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.mapper.PurchasePlanItemMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.util.DownloadExcelUtil;

import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service("purchasePlanItemService")
@RequiredArgsConstructor
public class PurchasePlanItemServiceImpl extends  ServiceImpl<PurchasePlanItemMapper,PurchasePlanItem> implements IPurchasePlanItemService {
     final IWarehouseService iWarehouseService;
     final IStepWisePriceService iStepWisePriceService;
     final IAssemblyService assemblyService;
     final IInventoryService inventoryService;
     final IAssemblyFormService assemblyFormService;
     final AmazonClientOneFeignManager amazonClientOneFeignManager;
     @Autowired
 	 @Lazy
     IMaterialService materialService;
	@Override
	public Map<String, Object> getSummary(String shopid, String planid) {
		// TODO Auto-generated method stub
		return this.baseMapper.getSummary(shopid, planid);
	}
	
	@Override
	public List<Map<String, Object>> listItem(String shopid, String planid) {
		// TODO Auto-generated method stub
		return this.baseMapper.listItem(shopid, planid);
	}
	
 
	@Override
	public List<Map<String, Object>> getHisList(String shopid, String warehouseid) {
		return this.baseMapper.planhis(shopid, warehouseid);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getList(String shopid, String planid, List<String> materialidList) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		if(materialidList!=null&&materialidList.size()>0) {
			param.put("materialidList",materialidList);
		}
		param.put("shopid",shopid);
		param.put("planid",planid);
		List<Map<String,Object>> list = this.baseMapper.listConfirmInfo(param);
		Map<String,Map<String,Object>> warehouiseMap =new HashMap<String,Map<String,Object>>();
		for(Map<String,Object> item:list) {
			String key=item.get("warehouseid").toString();
			if(item.get("groupid")!=null) {
				key=key+item.get("groupid").toString();
			}
			Map<String,Object> vitem = warehouiseMap.get(key);
			if(vitem==null) {
				vitem=new HashMap<String,Object>();
			}
			vitem.put("name", item.get("warehousename"));
			vitem.put("groupid", item.get("groupid"));
			vitem.put("id", item.get("warehouseid"));
			if(item.get("groupid")!=null) {
				try {
					Result<?> result = amazonClientOneFeignManager.getAmazonGroupByIdAction(item.get("groupid").toString());
					if(Result.isSuccess(result)&&result.getData()!=null) {
						Map<String, Object> data = (Map<String,Object>) result.getData();
						vitem.put("groupname", data.get("name"));
					}
				}catch(FeignException e) {
				   e.printStackTrace();
				}
				
			}
			
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
			warehouiseMap.put(key, vitem);
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
			Map<String, Object> pricemap = iStepWisePriceService.getMaterialPriceByAmount(item.get("mid").toString(), Integer.parseInt(item.get("amount").toString()));
			if(pricemap!=null&&pricemap.get("itemprice")!=null) {
				item.put("price", pricemap.get("itemprice"));
			}
			String mid=item.get("mid").toString();
			String warehouseid=item.get("warehouseid").toString();
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

	@Override
	public void moveBatch(String shopid,String batchnumber) {
		// TODO Auto-generated method stub
		this.baseMapper.moveBatch(shopid,batchnumber);
	}

}
