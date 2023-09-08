package com.wimoor.erp.purchase.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.mapper.MaterialMarkMapper;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialMarkService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.material.service.IStockCycleService;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanItemMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlan;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanWareHouse;
import com.wimoor.erp.purchase.service.IPurchaseFormService;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.purchase.service.IPurchasePlanService;
import com.wimoor.erp.purchase.service.IPurchasePlanWareHouseService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseStatusService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;
 
 
@Service("purchasePlanService")
@RequiredArgsConstructor
public class PurchasePlanServiceImpl extends  ServiceImpl<PurchasePlanMapper,PurchasePlan> implements IPurchasePlanService {
	 
	final IWarehouseService warehouseService;
	 
	final IMaterialService materialService;
	 
	final IStepWisePriceService stepWisePriceService;
	 
	final PurchasePlanMapper purchasePlanMapper;
	 
	final PurchasePlanItemMapper purchasePlanItemMapper;
	 
	final  ISerialNumService serialNumService;
	 
	final IPurchaseFormService purchaseFormService;
	 
	final IAssemblyFormService assemblyFormService;
	 
	final IDimensionsInfoService dimensionsInfoService;
 
	final MaterialMarkMapper materialMarkMapper;
	 
	final IStockCycleService stockCycleService;
	 
	final IPurchasePlanItemService purchasePlanItemService;
	 
	final IMaterialMarkService materialMarkService;
	 
	final IPurchasePlanWareHouseService purchasePlanWareHouseService;
	 
	final IPurchaseWareHouseMaterialService purchaseWareHouseMaterialService;
 
	final ThreadPoolTaskExecutor threadPoolTaskExecutor;
	 
	final IPurchaseWareHouseStatusService purchaseWareHouseStatusService;
	 
	final PurchaseFormEntryMapper purchaseFormEntryMapper;
	
	final AdminClientOneFeignManager adminClientOneFeign;
	
	
	public List<PurchasePlan> getPlanByShopid(String shopid) throws ERPBizException {
		QueryWrapper<PurchasePlan> queryWrapper = new QueryWrapper<PurchasePlan>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("disable", false);
		List<PurchasePlan> list = purchasePlanMapper.selectList(queryWrapper);
		if (list != null && list.size() > 0) {
			for(PurchasePlan item:list) {
				List<PurchasePlanWareHouse> warehouselist = purchasePlanWareHouseService.getWareHouseIdForPlanId(item.getId(), shopid);
				item.setWarehouseList(warehouselist);
			}
		}
		return list;
	}
 
	public Map<String,Object> getLastForms(List<String> ids) {
	    Map<String,Object> result=new HashMap<String,Object>();
		if(ids==null||ids.size()==0) {
			return result;
		}
		String lastform = "";
		SimpleDateFormat sdf4 = new SimpleDateFormat("MM-dd");
		List<Map<String, Object>> datas = purchaseFormService.getLastFormsByMaterials(ids);
		for(Map<String, Object> data:datas) {
			if (data != null) {
				String materialid=data.get("materialid").toString();
				Object creatdate = data.get("createdate");
				if (creatdate != null) {
					Date time = GeneralUtil.getDate(creatdate);
					lastform =GeneralUtil.formatDate(time)  ;
				}
				lastform = lastform + "  " + data.get("amount");
				Object auditstatus = data.get("auditstatus");
				if (auditstatus != null) {
					String entryid = data.get("entryid").toString();
					PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
					lastform = lastform + " <br> " + PurchaseFormEntry.getAuditstatusName(entry);
				}
				result.put(materialid, lastform);
			}
		}
		
		List<AssemblyForm> forms = assemblyFormService.getLastFormsByMaterials(ids);
		for(AssemblyForm form:forms) {
			if (form != null) {
				lastform = sdf4.format(form.getCreatedate());
				lastform = lastform + " " + form.getAmount();
				lastform = lastform + "  <br> " + form.getAuditstatusName();
				result.put(form.getMainmid(), lastform);
			}
		}
			
		return result;
	}

	
	public String getLastForm(Map<String, Object> map) {
		Object id = map.get("id");
		Object issfg = map.get("issfg");
		String lastform = "";
		SimpleDateFormat sdf4 = new SimpleDateFormat("MM-dd");
		if ("1".equals(issfg.toString())) {// 组装成品
			AssemblyForm form = assemblyFormService.getLastOneFormByMaterial(id);
			if (form != null) {
				lastform = sdf4.format(form.getCreatedate());
				lastform = lastform + " " + form.getAmount();
				lastform = lastform + "  <br> " + form.getAuditstatusName();
			}
		} else if ("0".equals(issfg.toString()) || "2".equals(issfg.toString())) {// 单独成品,半成品
			Map<String, Object> data = purchaseFormService.getLastOneFormByMaterial(id);
			if (data != null) {
				Object creatdate = data.get("createdate");
				if (creatdate != null) {
					Date time = GeneralUtil.getDate(creatdate);
					lastform =GeneralUtil.formatDate(time)  ;
				}
				lastform = lastform + "  " + data.get("amount");
				Object auditstatus = data.get("auditstatus");
				if (auditstatus != null) {
					String entryid = data.get("entryid").toString();
					PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
					lastform = lastform + " <br> " + PurchaseFormEntry.getAuditstatusName(entry);
				}
			}
		}
		return lastform;
	}

	public Map<String, Object> getLast3Form(Map<String, Object> map) {
		Object id = map.get("id");
		Object issfg = map.get("issfg");
		Map<String, Object> result = new HashMap<String, Object>();
		if ("1".equals(issfg.toString())) {
			List<AssemblyForm> formlist = assemblyFormService.getLastFormByMaterial(id, 3);
			result.put("list", formlist);
			return result;
		}
		if ("0".equals(issfg.toString()) || "2".equals(issfg.toString())) {
			List<Map<String, Object>> datalist = purchaseFormService.getLastFormByMaterial(id, 3);
			for (Map<String, Object> data : datalist) {
				Object auditstatus = data.get("auditstatus");
				if (auditstatus != null) {
					String entryid = data.get("entryid").toString();
					PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
					data.put("auditstatusname", PurchaseFormEntry.getAuditstatusName(entry));
				}
			}
			result.put("list", datalist);
			return result;
		}
		return null;
	}


	public List<Map<String, Object>> getPurchasePlanForShopId(String shopid) {
		List<Map<String, Object>> list = purchasePlanMapper.getPurchasePlanForShopId(shopid);
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		Map<String, Map<String, Object>> planMap = new HashMap<String, Map<String,Object>>();
		if(list != null && list.size() > 0) {
			for(Map<String, Object> map : list) {
				String planid = map.get("planid").toString();
				if(map.get("warehouseid")!=null) {
					String warehouseid =map.get("warehouseid").toString();
					String name = map.get("name").toString();
					if(planMap.get(planid) == null) {
						Map<String, Object> plan = new HashMap<String, Object>();
						plan.put("planid", planid);
						plan.put("warehouseid", warehouseid);
						plan.put("name", name);
						planMap.put(planid, plan);
					}else {
						Map<String, Object> plan = planMap.get(planid);
						plan.put("warehouseid", plan.get("warehouseid").toString() + "," + warehouseid);
						plan.put("name", plan.get("name").toString() + "," + name);
					}
				}
			}
			for(Map<String, Object> value: planMap.values()) {
				returnList.add(value);
			}
			return returnList;
		} 
		return list;
	}
	
	@Override
	public void changePurchasePlanModelItem(Map<String, Object> param2) {
		// TODO Auto-generated method stub
		
	}
    void checkPlan(PurchasePlan plan){
    	Set<String> warehouseid=new HashSet<String>();
    	for(PurchasePlanWareHouse warehouse:plan.getWarehouseList()) {
        	warehouseid.add(warehouse.getWarehouseid());
		}
    	List<PurchasePlan> oldplanlist = this.getPlanByShopid(plan.getShopid());
    	for(PurchasePlan oldplan:oldplanlist) {
    		if(!oldplan.getId().equals(plan.getId())) {
    			boolean isdifferent=true;
    			if(plan.getWarehouseList().size()==oldplan.getWarehouseList().size()) {
	    				isdifferent=false;
    	    			for(PurchasePlanWareHouse warehouse:oldplan.getWarehouseList()) {
    	    				if(!warehouseid.contains(warehouse.getWarehouseid())) {
    	    					isdifferent=true;break;
    	    				}
    	    			}
    			 }
              if(isdifferent==false) {
            	  throw new BizException("已经存在相同的计划无法保存");
              }
    		}
    	}
    }
	@Override
	public PurchasePlan savePlan(PurchasePlan plan) {
		// TODO Auto-generated method stub
		checkPlan(plan);
		plan.setDisable(false);
		this.baseMapper.insert(plan);
		for(PurchasePlanWareHouse item:plan.getWarehouseList()) {
			item.setPlanid(plan.getId());
			item.setShopid(plan.getShopid());
		}
		purchasePlanWareHouseService.savePlanWareHouse(plan.getWarehouseList());
		return plan;
	}

	@Override
	public PurchasePlan updatePlan(PurchasePlan plan) {
		// TODO Auto-generated method stub
		checkPlan(plan);
		PurchasePlan oldone = this.baseMapper.selectById(plan.getId());
		if(oldone==null)return savePlan(plan);
		else {
			plan.setCreatetime(oldone.getCreatetime());
			plan.setCreator(oldone.getCreator());
		}
		plan.setDisable(false);
		this.baseMapper.updateById(plan);
		for(PurchasePlanWareHouse item:plan.getWarehouseList()) {
			item.setPlanid(plan.getId());
			item.setShopid(plan.getShopid());
		}
		purchasePlanWareHouseService.updatePlanWareHouse(plan.getWarehouseList(),plan.getId(),plan.getShopid());
		return plan;
	}

	@Override
	public void afterSavePOSubPlan(UserInfo user,String warehouseid) {
//		List<Map<String, Object>> list = purchasePlanItemMapper.findPurchaseitemForWarehouseid(warehouseid, "po");
//		if(list == null || list.size() == 0) {
//			List<Map<String, Object>> subPlanList = purchasePlanItemMapper.findPurchaseSubForWarehouseid(warehouseid,"po");
//			for(Map<String, Object> map : subPlanList) {
//				String subPlanid = map.get("subid").toString();
//				PurchasePlanSub plansub = purchasePlanSubMapper(subPlanid);
//				plansub.setStatus(3);
//				plansub.setOpttime(new Date());
//				purchasePlanSubService.updateAll(plansub);
//			}
//			Warehouse warehouse = warehouseService.getSelfWarehouse(warehouseid);
//			PurchaseWareHouseStatus warehouseStatus = purchaseWareHouseStatusService.selectByKey(warehouse.getId());
//			if(warehouseStatus == null) {
//				warehouseStatus = new PurchaseWareHouseStatus();
//			}
//			warehouseStatus.setOpptime(new Date());
//			warehouseStatus.setUserid(user.getId());
//			warehouseStatus.setPurchaseStatus(2);
//			if(warehouseStatus.getWarehouseid() == null) {
//				warehouseStatus.setAssblyStatus(0);
//				warehouseStatus.setWarehouseid(warehouse.getId());
//				purchaseWareHouseStatusService.save(warehouseStatus);
//			}else {
//				purchaseWareHouseStatusService.updateAll(warehouseStatus);
//			}
//		  
//		}
	}

 
	
	
	
}
