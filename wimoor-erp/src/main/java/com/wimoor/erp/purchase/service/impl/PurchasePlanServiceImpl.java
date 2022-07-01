package com.wimoor.erp.purchase.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AdminClientOneFeign;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.common.service.IProductInPresaleService;
import com.wimoor.erp.inventory.service.IStockCycleService;
import com.wimoor.erp.material.mapper.MaterialMarkMapper;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialMark;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialMarkService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanItemMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanModelItemMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanModelItemsubMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanModelMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanSubMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlan;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanModel;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanModelItem;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanModelItemsub;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanSub;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseMaterial;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseStatus;
import com.wimoor.erp.purchase.service.IPurchaseFormService;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.purchase.service.IPurchasePlanModelItemService;
import com.wimoor.erp.purchase.service.IPurchasePlanModelService;
import com.wimoor.erp.purchase.service.IPurchasePlanService;
import com.wimoor.erp.purchase.service.IPurchasePlanSubService;
import com.wimoor.erp.purchase.service.IPurchasePlanWareHouseService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseStatusService;
import com.wimoor.erp.util.UUIDUtil;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;
 
 
@Service("purchasePlanService")
@RequiredArgsConstructor
public class PurchasePlanServiceImpl extends  ServiceImpl<PurchasePlanMapper,PurchasePlan> implements IPurchasePlanService {
	 
	final IWarehouseService warehouseService;
	 
	final IMaterialService materialService;
	 
	final IStepWisePriceService stepWisePriceService;
	 
	final PurchasePlanMapper purchasePlanMapper;
	 
	final PurchasePlanModelItemMapper purchasePlanModelItemMapper;
	 
	final PurchasePlanModelMapper purchasePlanModelMapper;
	 
	final PurchasePlanItemMapper purchasePlanItemMapper;
	 
	final PurchasePlanSubMapper purchasePlanSubMapper;
	 
	final  ISerialNumService serialNumService;
	 
	final IPurchaseFormService purchaseFormService;
	 
	final IAssemblyFormService assemblyFormService;
	 
	final IDimensionsInfoService dimensionsInfoService;
 
	final MaterialMarkMapper materialMarkMapper;
	 
	final IStockCycleService stockCycleService;
	 
	final IPurchasePlanModelService purchasePlanCalModelService;
	 
	final IPurchasePlanModelItemService purchasePlanModelItemService;
	 
	final IPurchasePlanItemService purchasePlanItemService;
	 
	final IPurchasePlanSubService purchasePlanSubService;
	 
	final IMaterialMarkService materialMarkService;
	 
	final IPurchasePlanWareHouseService purchasePlanWareHouseService;
	 
	final IPurchaseWareHouseMaterialService purchaseWareHouseMaterialService;
 
	final ThreadPoolTaskExecutor threadPoolTaskExecutor;
	 
	final IProductInPresaleService productInPresaleService;
	 
	final PurchasePlanModelItemsubMapper purchasePlanModelItemsubMapper;
	 
	final IPurchaseWareHouseStatusService purchaseWareHouseStatusService;
	 
	final PurchaseFormEntryMapper purchaseFormEntryMapper;
	
	final AdminClientOneFeign adminClientOneFeign;
	public List<Map<String, Object>> calPlanDetail(Map<String, Object> param) throws ERPBizException {
		if(param== null || param.size() == 0) {
			return null;
		}
		UserInfo user = UserInfoContext.get();
		String warehouseid = param.get("warehouseid").toString();
		String materialid = param.get("materialid").toString();
		String sku = param.get("sku").toString(); 
		Material material = materialService.getById(materialid);
		if (material == null) {
			return null;
		}
		
		Map<String,Map<String,Integer>> sumpresaleMap =null;
		Integer delivery_cycle = 0;// 补货周期 + sku组装周期
		Integer delivery_cycle2 = 0;// 供货周期
		int assembly_time = 0;// 组装周期
		if (material.getDeliveryCycle() != null) {
			delivery_cycle = material.getDeliveryCycle();
			delivery_cycle2 = material.getDeliveryCycle();
		}
		if (material.getAssemblyTime() != null) {
			assembly_time = material.getAssemblyTime();
			delivery_cycle = delivery_cycle2 + assembly_time;
		}
		//获取fba仓库的缺货量，销量，库存
		List<Map<String, Object>> fbasale = warehouseService.selectFbaSale(user.getCompanyid(), sku, delivery_cycle.toString());
		if (fbasale == null) {
			return null;
		}
		param.put("assembly_time", assembly_time);
		param.put("delivery_cycle2", delivery_cycle2);
		for (int j = 0; j < fbasale.size(); j++) {
			Map<String, Object> item = fbasale.get(j);
			String pid = item.get("pid").toString();
			//计算日均销量，欧洲站点需要重新计算
			calculationPresales(user, item, fbasale, sumpresaleMap, param);
		}
		//获取所有入库仓库的总库存
		int selfsale_quantity_sum = 0;
		String[] warehouseidArray = warehouseid.split(",");
		List<String> warehouseidList = new ArrayList<String>();
		for(int i = 0; i < warehouseidArray.length; i++) {
			warehouseidList.add(warehouseidArray[i]);
		}
		List<Map<String, Object>> selfsaleList = warehouseService.selectSelfSale(user.getCompanyid(), warehouseidList, materialid);
		for(Map<String, Object> map : selfsaleList) {
			selfsale_quantity_sum += Integer.parseInt(map.get("quantity").toString());
		}
		String planId = purchasePlanWareHouseService.getPlanIdForWareHouseId(warehouseid, user.getCompanyid());
		//获取该sku在该补货规划中选择的入库仓库
		List<Map<String, Object>> choseWareHouse = purchasePlanItemService.getWarehouseForPlanIdAndMaterialId(planId, materialid);
		//获取该sku在该补货规划中的默认入库仓库,如果该默认仓库不在补货规划中，则不纳入计算
		List<PurchaseWareHouseMaterial> defoutWarehouse = null;
		if(choseWareHouse == null || choseWareHouse.size() == 0) {
			defoutWarehouse = purchaseWareHouseMaterialService.getPurchaseForPlanIdAndMaterialId(planId, materialid);
			if(defoutWarehouse != null && defoutWarehouse.size() > 0) {
				String defoutWarehouseid = defoutWarehouse.get(0).getWarehouseid();
				if(!warehouseidList.contains(defoutWarehouseid)) {
					PurchaseWareHouseMaterial d = defoutWarehouse.get(0);
					QueryWrapper<PurchaseWareHouseMaterial> queryWrapper=new QueryWrapper<PurchaseWareHouseMaterial>();
					queryWrapper.eq("planid", d.getPlanid());
					queryWrapper.eq("materialid", d.getMaterialid());
					queryWrapper.eq("warehouseid", d.getWarehouseid());
					purchaseWareHouseMaterialService.remove(queryWrapper);
					defoutWarehouse = null;
				}
			}
		}
		param.put("selfsale_quantity_sum", selfsale_quantity_sum);
		List<Map<String, Object>> salf = selfsaleList;
		if (salf != null) {
			for(Map<String, Object> map : salf) {
				int cycle = Integer.parseInt(map.get("stocking_cycle").toString());
				//计算建议补货量
				int plan = calculationSuggessPlan(map, param, cycle, fbasale, sumpresaleMap);
				//根据仓库选择建议补货量
				calculationPurchasePlan(map, choseWareHouse, defoutWarehouse, param, plan);
				map.put("delivery_cycle", param.get("delivery_cycle2"));
				map.put("assembly_time", param.get("assembly_time"));
				map.put("safeinvdays", cycle);
				map.put("planquantity", plan);
				map.put("materialid", materialid);
				map.put("sales", param.get("salesummary"));
				map.put("sku", sku);
				map.put("ftype", "self");
			}
			Integer nums = 0;
			String selfName = "";
			String allwarehouseid = "";
			int defoutPlan = param.get("defoutPlan") == null ? 0 : (Integer) param.get("defoutPlan");
			int maxPlan = param.get("maxPlan") == null ? 0 : (Integer) param.get("maxPlan");
			String checkedwarehouesid = param.get("checkedwarehouesid") == null ? null : param.get("checkedwarehouesid").toString();
			Warehouse ware = warehouseService.getById(checkedwarehouesid);
			if(ware!=null && ware.getDisabled()==true) {
				checkedwarehouesid=null;
			}
			int temp = 0;
			for(int i = 0; i < salf.size(); i++) {
				if(salf.size() == 1) {
					salf.get(0).put("isdefoutWarehouse", true);
					if(GeneralUtil.isEmpty(checkedwarehouesid)) {
						checkedwarehouesid = salf.get(0).get("id").toString();
					}
				}
				if((Boolean) salf.get(i).get("isdefoutWarehouse") || (checkedwarehouesid != null && checkedwarehouesid.equals(salf.get(i).get("id").toString()))) {
					salf.get(i).put("isdefoutWarehouse", true);
					if(defoutPlan != 0) {
						salf.get(i).put("defoutplan", defoutPlan);
					}else {
						salf.get(i).put("defoutplan", maxPlan);
					}
					temp = i;
				}
				Map<String, Object> obj = salf.get(i);
				if(obj.get("quantity") != null) {
					nums += Integer.parseInt(obj.get("quantity").toString());
				}
				selfName += (obj.get("name").toString() + "+");
				allwarehouseid += (obj.get("warehouseid").toString() + ",");
			}
			selfName = selfName.substring(0, selfName.length() - 1);
			allwarehouseid = allwarehouseid.substring(0,allwarehouseid.length() - 1);
			salf.get(temp).put("allwarehouseid", allwarehouseid);
			salf.get(temp).put("quantity", nums);
			salf.get(temp).put("name", selfName);
			salf.get(temp).put("checkedwareid", checkedwarehouesid);
			fbasale.add(0, salf.get(temp));
			if(fbasale.get(0).get("defoutplan")==null){
				fbasale.get(0).put("defoutplan", defoutPlan);
			}
		}
		return fbasale;
	}
	
	public void calculationPresales(UserInfo user, Map<String, Object> item ,List<Map<String, Object>> fbasale, Map<String, Map<String, Integer>> sumpresaleMap ,Map<String, Object> param) {
		//对于欧洲站点，重新计算日均销量
		Integer salesummary = param.get("salesummary") == null ? 0 : (Integer) param.get("salesummary");
		Integer fbaplannumber = param.get("fbaplannumber") == null ? 0 : (Integer) param.get("fbaplannumber");
		if(item.get("marketplaceid")!=null && item.get("marketplaceid").toString().equals("EU")){
			int summonth = item.get("salesmonth") == null ? 0 : Integer.parseInt(item.get("salesmonth").toString());
			int sumseven = item.get("salesweek") == null ? 0 : Integer.parseInt(item.get("salesweek").toString());
			int sum15 = item.get("salesfifteen") == null ? 0 : Integer.parseInt(item.get("salesfifteen").toString());
			String openDate = item.get("openDate") == null ? null : item.get("openDate").toString();
			//DaysalesFormula formula = daysalesFormulaService.selectByShopid(user.getCompanyid());
			int qty = 0;//DaysalesFormulaServiceImpl.getAvgSales(formula, summonth, sumseven, sum15, GeneralUtil.parseDate(openDate));
			item.put("sales", qty);
		} 
		if(item.get("sales")==null){
			item.put("sales", 0);
		}
		//如果预估销量没有修改过，用系统计算的日均销量
		if(item.get("presales")==null){
			item.put("presales", item.get("sales"));
		}
		int presale = item.get("presales") == null ? 0:Integer.parseInt(item.get("presales").toString());
		salesummary += presale;
		int mincycle = item.get("mincycle") ==null ? 0 :Integer.parseInt(item.get("mincycle").toString());
		int stocking_cycle = item.get("stocking_cycle") == null ? 0:Integer.parseInt(item.get("stocking_cycle").toString());
		int quantity = 0;
		if (item.get("quantity") != null) {
			quantity = Integer.parseInt(item.get("quantity").toString());//FBA库存
		}
		int salesday = 0;
		int aftersalesday = stocking_cycle;
		int planquantity = 0;//计算FBA仓库建议发货货量=备货周期内建议发货库存-FBA库存>最小发货库存？备货周期内建议发货库存-FBA库存：最小发货库存
		Map<String,Integer> presaleMap = productInPresaleService.selectBySKUMarket(item.get("sku").toString(),
				item.get("marketplaceid").toString(),item.get("groupid").toString());
		if (presaleMap != null) {
			item.put("hasfullcalendar", true);
			if (sumpresaleMap == null) {
				sumpresaleMap = new HashMap<String, Map<String, Integer>>();
			}
			sumpresaleMap.put(item.get("marketplaceid").toString(), presaleMap);
			// 计算备货周期内总销量
			int sumpresales_stocking = productInPresaleService.getTotalPreSales(presaleMap, stocking_cycle, presale);
			int sumpresales_mincycle = productInPresaleService.getTotalPreSales(presaleMap, mincycle, presale);
			if(sumpresales_stocking - quantity>0){
				planquantity = sumpresales_stocking - quantity > sumpresales_mincycle ? sumpresales_stocking - quantity : sumpresales_mincycle;
			}
			salesday = productInPresaleService.getSalesday(presaleMap, quantity, presale);// 根据销量分摊库存，求可售天数
		} else {
			if(stocking_cycle * presale - quantity>0){
				planquantity = stocking_cycle * presale - quantity > mincycle * presale ? stocking_cycle * presale - quantity : mincycle * presale;
			}
			if(presale != 0){
				salesday = quantity / presale;
			}
		}
		item.put("planquantity", planquantity);
		fbaplannumber += planquantity;
		if (presale != 0) {
			item.put("salesday", salesday);
			if (salesday > aftersalesday) {
				item.put("aftersalesday", salesday);
			} else {
				item.put("aftersalesday", aftersalesday);
			}
		}
		item.put("ftype", "fba");
		item.put("materialid", param.get("materialid"));
		item.put("assembly_time", param.get("assembly_time"));
		item.put("delivery_cycle", param.get("delivery_cycle2"));
		param.put("salesummary", salesummary);
		param.put("fbaplannumber", fbaplannumber);
	}
	
	public void calculationPurchasePlan(Map<String, Object> map, List<Map<String, Object>> choseWareHouse, List<PurchaseWareHouseMaterial> defoutWarehouse,Map<String, Object> param,int plan) {
		Integer defoutPlan = param.get("defoutPlan") == null ? 0 : (Integer) param.get("defoutPlan");
		Integer maxPlan = param.get("maxPlan") == null ? 0 : (Integer) param.get("maxPlan");
		String checkedwarehouesid = param.get("checkedwarehouesid") == null ? null : param.get("checkedwarehouesid").toString();
		//如果该sku有默认入库仓库，则建议补货量以默认入库仓库为准，否则取补货规划中所有入库仓库的建议补货量最大值
		map.put("isdefoutWarehouse", false);
		String wid = map.get("id").toString();
		//如果该产品选中了入库仓库并加入了计划，则以选中的入库仓库计算建议补货值
		if(choseWareHouse != null && choseWareHouse.size() > 0) {
			String choseWarehouseid = choseWareHouse.get(0).get("warehouseid").toString();
			if(choseWarehouseid.equals(wid)) {
				defoutPlan = plan;
				map.put("isdefoutWarehouse", true);
				checkedwarehouesid = wid;
			}
		} else {
			//如果当前产品有默认仓库则用默认仓库来计算建议补货值，否则取当前计划里所有的仓库建议补货值中的最大值来计算
			if(defoutWarehouse != null && defoutWarehouse.size() > 0) {
				PurchaseWareHouseMaterial mydefoutWarehouse = defoutWarehouse.get(0);
				String defoutWarehouseid = mydefoutWarehouse.getWarehouseid();
				if(defoutWarehouseid.equals(wid)) {
					defoutPlan = plan;
					map.put("isdefoutWarehouse", true);
					checkedwarehouesid = wid;
				}
			} else {
				if(plan >= maxPlan) {
					maxPlan = plan;
					defoutPlan = plan;
					//checkedwarehouesid = wid;
				}
			}
		}
		param.put("defoutPlan", defoutPlan);
		param.put("maxPlan", maxPlan);
		param.put("checkedwarehouesid", checkedwarehouesid);
	}
	
	public int calculationSuggessPlan(Map<String, Object> map, Map<String, Object> param, int cycle, List<Map<String, Object>> fbasale,Map<String, Map<String, Integer>> sumpresaleMap) {
		Integer salesummary = param.get("salesummary") == null ? 0 : (Integer) param.get("salesummary");
		Integer fbaplannumber = param.get("fbaplannumber") == null ? 0 : (Integer) param.get("fbaplannumber");
		Integer selfsale_quantity_sum = param.get("selfsale_quantity_sum") == null ? 0 : (Integer) param.get("selfsale_quantity_sum");
		int plan = 0;
		int min_cycle = Integer.parseInt(map.get("min_cycle").toString());
		int sumplan = fbaplannumber;// 各个站点在FBA备货周期后的建议发货量之和 
		// 本地仓库缺货量 = 各个站点在FBA备货周期后的建议发货量之和 - (本地库存数量 + inbound库存数量)
		int salfplan = sumplan - selfsale_quantity_sum;
		// 本地仓库安全库存
		int safeplan = cycle * salesummary;
		// 最小补货周期 * 销量
		int min_plan = min_cycle * salesummary;
		if(sumpresaleMap != null){
			safeplan = productInPresaleService.getLocalTotalPreSales(sumpresaleMap, cycle, fbasale);
			min_plan = productInPresaleService.getLocalTotalPreSales(sumpresaleMap, min_cycle, fbasale);
		}
		if (salfplan > 0) {
			if ((salfplan + safeplan) > min_plan) {
				plan = salfplan + safeplan;
			} else {
				plan = min_plan;
			}
		} else {
			if (salfplan + safeplan > 0) {
				plan = min_plan;
			} else {
				plan = 0;
			}
		}
		//可售天数
		int salesday = 0;
		//发货后可售天数
		int aftersalesday = 0;
		if (sumpresaleMap != null) {//根据销量分摊库存，求可售天数
			salesday = productInPresaleService.getLocalSalesday(sumpresaleMap,selfsale_quantity_sum,fbasale);
			aftersalesday = productInPresaleService.getLocalSalesday(sumpresaleMap,selfsale_quantity_sum + plan,fbasale);
		} else if (salesummary != 0) {
			salesday = selfsale_quantity_sum / salesummary;
			aftersalesday = (selfsale_quantity_sum + plan) / salesummary;
		}
		if (salesummary != 0) {
			map.put("salesday", salesday);
			map.put("aftersalesday", aftersalesday);
		} else {
			map.put("salesday", "--");
			map.put("aftersalesday", "--");
		}
		return plan;
	}

	@Transactional
	public void changePurchasePlanModelItem(Map<String, Object> param) {
		String userId = param.get("userId").toString();
		Object modelId = param.get("modelId");
		String materialid = param.get("materialid").toString();
		int planamount = param.get("planamount")==null?0:Integer.parseInt(param.get("planamount").toString());
		BigDecimal itemprice = (BigDecimal)param.get("itemprice");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> fbasale = (List<Map<String, Object>>) param.get("fbasale");
		if (modelId != null&&fbasale!=null&&fbasale.size()>0) {
			PurchasePlanModelItem item = null;
			QueryWrapper<PurchasePlanModelItem> queryWrapper = new QueryWrapper<PurchasePlanModelItem>();
			queryWrapper.eq("materialid", materialid);
			queryWrapper.eq("modelid", modelId.toString());
			List<PurchasePlanModelItem> itemlist = purchasePlanModelItemMapper.selectList(queryWrapper);
			if (itemlist != null && itemlist.size() > 0) {
				item = itemlist.get(0);
				if(item.getPlanamount() != planamount || item.getItemprice().compareTo(itemprice)!= 0) {
					item.setItemprice(itemprice);
					item.setPlanamount(planamount);
					item.setOpttime(new Date());
					item.setOperator(userId);
					purchasePlanModelItemService.updateById(item);
				}
			} else {
				item = new PurchasePlanModelItem();
				item.setModelid(modelId.toString());
				item.setMaterialid(materialid);
				item.setItemprice(itemprice);
				item.setPlanamount(planamount);
				item.setOpttime(new Date());
				item.setOperator(userId);
				purchasePlanModelItemService.save(item);
			}
			for (Map<String, Object> itemsub : fbasale) {
				String sku = itemsub.get("sku").toString();
				String marketplaceid = (String) itemsub.get("marketplaceid");
				String groupid = itemsub.get("groupid").toString();
				if("self".equals(groupid)|| marketplaceid==null){
					continue;
				}
				String itemid = item.getId();
				int needship = Integer.parseInt(itemsub.get("planquantity").toString());
				int salesday = itemsub.get("salesday")==null?0:Integer.parseInt(itemsub.get("salesday").toString());
				int aftersalesday = itemsub.get("aftersalesday")==null?0:Integer.parseInt(itemsub.get("aftersalesday").toString());

				PurchasePlanModelItemsub itemsubobj = new PurchasePlanModelItemsub();
				itemsubobj.setItemid(itemid);
				itemsubobj.setSku(sku);
				itemsubobj.setMarketplaceid(marketplaceid);
				itemsubobj.setGroupid(groupid);
				itemsubobj.setNeedship(needship);
				itemsubobj.setSalesday(salesday);
				itemsubobj.setAftersalesday(aftersalesday);
				QueryWrapper<PurchasePlanModelItemsub> queryWrappersub = new QueryWrapper<PurchasePlanModelItemsub>();
				queryWrappersub.eq("itemid", itemid);
				queryWrappersub.eq("sku", sku);
				queryWrappersub.eq("marketplaceid", marketplaceid);
				queryWrappersub.eq("groupid", groupid);
				PurchasePlanModelItemsub itemsub_db = purchasePlanModelItemsubMapper.selectOne(queryWrappersub);
				if (itemsub_db != null) {
					if (itemsub_db.getNeedship() == null || itemsub_db.getSalesday() == null
							|| itemsub_db.getAftersalesday() == null || itemsub_db.getNeedship() != needship
							|| itemsub_db.getSalesday() != salesday || itemsub_db.getAftersalesday() != aftersalesday) {
						purchasePlanModelItemsubMapper.updateById(itemsubobj);
					}
				} else {
					purchasePlanModelItemsubMapper.insert(itemsubobj);
				}
			}
		}
	}
	
	public PurchasePlan getPlanByShopid(String shopid, String warehouseid) throws ERPBizException {
		String planid = purchasePlanWareHouseService.getPlanIdForWareHouseId(warehouseid, shopid);
		if(planid != null) {
			QueryWrapper<PurchasePlan> queryWrapper = new QueryWrapper<PurchasePlan>();
			queryWrapper.eq("shopid", shopid);
			queryWrapper.eq("id", planid);
			List<PurchasePlan> list = purchasePlanMapper.selectList(queryWrapper);
			if (list != null && list.size() > 0) {
				PurchasePlan plan = list.get(0);
				return plan;
			}
		}
		return null;
	}

	public PurchasePlan readWorkStatusByPlanId(UserInfo user,String planid) throws ERPBizException {
		PurchasePlan plan = purchasePlanMapper.selectById(planid);
		if (plan != null) {
			List<Map<String, Object>> listmap = getSummaryPlan(plan.getId());
			BigDecimal totalpayprice = new BigDecimal("0");
			Integer totalnum = 0;
			Integer totalbuyqty = 0;
			for (int i = 0; i < listmap.size(); i++) {
				Map<String, Object> map = listmap.get(i);
				if ("1".equals(map.get("issfg").toString())) {
					continue;
				}
				Object number = map.get("number");
				Object amount = map.get("amount");
				Object orderprice = map.get("orderprice");
				if (number != null) {
					totalnum += Integer.parseInt(number.toString().trim());
				}
				if (amount != null) {
					totalbuyqty += Integer.parseInt(amount.toString().trim());
				}
				if (orderprice != null) {
					totalpayprice = totalpayprice.add(new BigDecimal(orderprice.toString().trim()));
				}
			}
			plan.setTotalbuyqty(totalbuyqty);
			plan.setTotalnum(totalnum);
			super.updateById(plan);
			PurchasePlanModel model = purchasePlanModelMapper.selectById(plan.getId());
			if(model!=null) {
				plan.setIsrun(model.isIsrun());
			}
			QueryWrapper<PurchasePlanSub> queryWrapper = new QueryWrapper<PurchasePlanSub>();
			queryWrapper.eq("planid", plan.getId());
			queryWrapper.eq("status", 1);
			List<PurchasePlanSub> sublist = purchasePlanSubMapper.selectList(queryWrapper);
			for (int i = 0; i < sublist.size(); i++) {
				if ("po".equals(sublist.get(i).getFtype())) {
					plan.setPo(sublist.get(i));
				}
				if ("ao".equals(sublist.get(i).getFtype())) {
					plan.setAo(sublist.get(i));
				}
			}
			this.setPOAO(plan);
			return plan;
		}
		return null;
	}

	public PurchasePlanModel getRefreshModelTime(String planid) {
		return purchasePlanModelMapper.selectById(planid);
	}
	
	public Map<String, Object> refreshPlanModel(UserInfo user, String planid, String warehouseid) throws ERPBizException {
		PurchasePlanModel mymodel = getRefreshModelTime(planid);
		if (mymodel != null && mymodel.isIsrun()) {
			throw new ERPBizException("已有用户正在计算建议采购量，请稍后再试！");
		} else if (mymodel == null) {
			mymodel = new PurchasePlanModel();
			mymodel.setPlanid(planid);
			mymodel.setModelid(UUIDUtil.getUUIDshort());
			mymodel.setIsrun(true);
			mymodel.setOpeartor(user.getId());
			purchasePlanCalModelService.save(mymodel);
		} else if (mymodel != null && !mymodel.isIsrun()) {
			mymodel.setOpeartor(user.getId());
			mymodel.setIsrun(true);
			purchasePlanCalModelService.updateById(mymodel);
		}
		try {
			refreshModel(user, planid, warehouseid, mymodel.getModelid());
			//deletePlanNew(user, planid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ERPBizException("出错啦，建议采购量计算失败！");
		} finally {
			if (mymodel == null) {
				mymodel = new PurchasePlanModel();
				mymodel.setPlanid(planid);
				mymodel.setModelid(UUIDUtil.getUUIDshort());
				mymodel.setRefreshtime(new Date());
				mymodel.setIsrun(false);
				mymodel.setOpeartor(user.getId());
				purchasePlanCalModelService.save(mymodel);
			} else if (mymodel != null && mymodel.isIsrun()) {
				mymodel.setRefreshtime(new Date());
				mymodel.setIsrun(false);
				mymodel.setOpeartor(user.getId());
				purchasePlanCalModelService.updateById(mymodel);
			}
		}
	 
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("caltime", sdf4.format(mymodel.getRefreshtime()));
		if (user != null) {
			result.put("caloperator", user.getUserinfo().get("name"));
		}
		return result;
	}

	public void refreshModel(UserInfo user, String planid, String warehouseid, String modelid) throws ERPBizException {
		String shopid = user.getCompanyid();
		List<Map<String, Object>> list = materialService.getForSum(shopid,null);
	    calPlanDetail(user, planid, warehouseid,modelid, list);
	}
	
	public void calPlanDetail(UserInfo user, String planid,  String warehouseid, String modelid, List<Map<String, Object>> paramList) {
		      for(int i = 0; i < paramList.size(); i++) {
		    	    Double process=((i+1.0)*100.0)/paramList.size();
					paramList.get(i).put("user", user);
					paramList.get(i).put("warehouseid", warehouseid);
					String materialid = paramList.get(i).get("materialid").toString();
					List<Map<String, Object>> fbasale = calPlanDetail(paramList.get(i));
					Map<String, Object> param2 = new HashMap<String, Object>();
					Integer defoutPlan = 0;
					if(fbasale!=null&&fbasale.size()>0&&fbasale.get(0).get("defoutplan")!=null) {
						 defoutPlan = (Integer) fbasale.get(0).get("defoutplan");
					}
					Map<String, Object> tmap = stepWisePriceService.getMaterialPriceByAmount(materialid, defoutPlan);
					BigDecimal itemprice = null;
					if (tmap.get("itemprice") == null || new BigDecimal(tmap.get("itemprice").toString()).compareTo(BigDecimal.ZERO) == 0) {
						Material material = materialService.getById(materialid);
						itemprice =(material==null||material.getPrice() == null) ? new BigDecimal("0") : material.getPrice();
					} else {
						itemprice = new BigDecimal(tmap.get("itemprice").toString());
					}
					param2.put("userId", user.getId());
					param2.put("modelId", modelid);	
					param2.put("materialid", materialid);
					param2.put("planamount", defoutPlan);
					param2.put("itemprice", itemprice);
					param2.put("fbasale", fbasale);
					//更新model上面的建议补货量
					changePurchasePlanModelItem(param2);
				}
	}
	
	@Transactional
	public void deletePlanNew(UserInfo user, String planid) {
		QueryWrapper<PurchasePlanSub> queryWrapper = new QueryWrapper<PurchasePlanSub>();
		queryWrapper.eq("planid", planid);
		List<PurchasePlanSub> sublist = purchasePlanSubMapper.selectList(queryWrapper);
		for (int step = 0; step < sublist.size(); step++) {
			PurchasePlanSub plansub = sublist.get(step);
			deletePlanItem(plansub);
		}
		PurchasePlan plan = getById(planid);
		String pnumber = null;
		try {
			pnumber = serialNumService.readSerialNumber(user.getCompanyid(), "PP");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ERPBizException("编码失败！");
		}
		plan.setNumber(pnumber);
		plan.setCreator(user.getId());
		plan.setOperator(user.getId());
		plan.setShopid(user.getCompanyid());
		plan.setStatus((byte) 1);
		plan.setTotalnum(0);
		plan.setTotalbuyqty(0);
		plan.setCreatetime(new Date());
		plan.setOpttime(new Date());
		this.updateById(plan);
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
				lastform = lastform + "  " + form.getAmount();
				lastform = lastform + "  <br> " + form.getAuditstatusName();
			}
		} else if ("0".equals(issfg.toString()) || "2".equals(issfg.toString())) {// 单独成品,半成品
			Map<String, Object> data = purchaseFormService.getLastOneFormByMaterial(id);
			if (data != null) {
				Object creatdate = data.get("createdate");
				if (creatdate != null) {
					lastform = sdf4.format((Date) creatdate);
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

	public IPage<Map<String, Object>> getPlan(Page<?> page,Map<String, Object> param) {
		IPage<Map<String, Object>> list = getSale(page,param);
		for (int i = 0; i < list.getRecords().size(); i++) {
			Map<String, Object> map = list.getRecords().get(i);
			 map.put("lastorder", getLastForm(map));
		}
		return list;
	}

	public int savePlanItem(PurchasePlanItem item, String oldplanitemid, String oldplanitemamount) throws ERPBizException {
		if (item == null) {
			return 0;
		}
		PurchasePlan plan = null;
		int changeCount = 0;
		item.setOpttime(new Date());
		QueryWrapper<PurchasePlanItem> queryWrapper = new QueryWrapper<PurchasePlanItem>();
		queryWrapper.eq("subplanid", item.getSubplanid());
		queryWrapper.eq("materialid", item.getMaterialid());
		List<PurchasePlanItem> oldplanitemlist = purchasePlanItemMapper.selectList(queryWrapper);
		PurchasePlanSub subplan = purchasePlanSubMapper.selectById(item.getSubplanid());
		if (subplan != null) {
			plan = purchasePlanMapper.selectById(subplan.getPlanid());
		}
		if (plan != null) {
			PurchaseWareHouseMaterial purchaseWareHouseMaterial = new PurchaseWareHouseMaterial();
			purchaseWareHouseMaterial.setPlanid(plan.getId());
			purchaseWareHouseMaterial.setMaterialid(item.getMaterialid());
			purchaseWareHouseMaterial.setWarehouseid(item.getWarehouseid());
			purchaseWareHouseMaterialService.savePurchaseWareHouseMaterial(purchaseWareHouseMaterial);
			if (oldplanitemlist != null && oldplanitemlist.size() > 0) {
				PurchasePlanItem old = oldplanitemlist.get(0);
				item.setId(old.getId());
				item.setStatus((byte) 1);
				if (old.getStatus() != 1) {
					plan.setTotalnum(plan.getTotalnum() + 1);
					plan.setTotalbuyqty(plan.getTotalbuyqty() + item.getAmount());
				} else {
					plan.setTotalbuyqty(plan.getTotalbuyqty() + item.getAmount() - old.getAmount());
				}
				changeCount += purchasePlanItemService.updateById(item)?1:0;
			} else {
				item.setStatus((byte) 1);
				plan.setTotalnum(plan.getTotalnum() + 1);
				plan.setTotalbuyqty(plan.getTotalbuyqty() + item.getAmount());
				changeCount += purchasePlanItemService.save(item)?1:0;
			}
			super.updateById(plan);
		}
		return changeCount;
	}

	public Integer deletePlanItem(PurchasePlanSub subplan) throws ERPBizException {
		QueryWrapper<PurchasePlanItem> queryWrapper = new QueryWrapper<PurchasePlanItem>();
		queryWrapper.eq("subplanid", subplan.getId());
		int result = purchasePlanItemMapper.delete(queryWrapper);
		if (result > 0) {
			result += purchasePlanSubMapper.deleteById(subplan.getId());
		}
		return result;
	}

	@Transactional
	public int disablePlanItem(UserInfo user, String subplanid, String materialid, String warehouseid) throws ERPBizException {
		int count = 0;
		PurchasePlanSub subplan = purchasePlanSubMapper.selectById(subplanid);
		String[] warehouseidArray = warehouseid.split(",");
		List<String> warehouseidList = new ArrayList<String>();
		for(int i = 0; i < warehouseidArray.length; i++) {
			warehouseidList.add(warehouseidArray[i]);
		}
		QueryWrapper<PurchasePlanItem> queryWrapper = new QueryWrapper<PurchasePlanItem>();
		queryWrapper.eq("subplanid", subplanid);
		queryWrapper.in("warehouseid", warehouseidList);
		if (GeneralUtil.isNotEmpty(materialid)) {
			queryWrapper.eq("materialid", materialid);
		}
		List<PurchasePlanItem> oldplanitemlist = purchasePlanItemMapper.selectList(queryWrapper);
		Integer totalnum = 0;
		Integer totalbuy = 0;
		BigDecimal totalprice = new BigDecimal("0.0000");
		if (oldplanitemlist != null && oldplanitemlist.size() > 0) {
			for (int i = 0; i < oldplanitemlist.size(); i++) {
				PurchasePlanItem oldplanitem = oldplanitemlist.get(i);
				oldplanitem.setStatus((byte) 0);
				totalnum = totalnum + 1;
				totalbuy = totalbuy + oldplanitem.getAmount();
				totalprice = totalprice.add(oldplanitem.getOrderprice());
				count += purchasePlanItemService.updateById(oldplanitem) ? 1:0;
			}
			PurchasePlan plan = purchasePlanMapper.selectById(subplan.getPlanid());
			if (plan != null) {
				if (plan.getTotalnum() != null) {
					if (plan.getTotalnum() - totalnum >= 0) {
						plan.setTotalnum(plan.getTotalnum() - totalnum);
					} else {
						plan.setTotalnum(0);
					}
				}
				if (plan.getTotalbuyqty() != null) {
					if (plan.getTotalbuyqty() - totalbuy >= 0) {
						plan.setTotalbuyqty(plan.getTotalbuyqty() - totalbuy);
					} else {
						plan.setTotalbuyqty(0);
					}
				}
				plan.setOperator(user.getId());
				plan.setOpttime(new Date());
				count += super.updateById(plan)?1:0;
			}
		}
		return count;
	}

	public IPage<Map<String, Object>> getSale(Page<?> page,Map<String, Object> map) {
		Object sortparam = map.get("sortparam");
		Object orderparam = map.get("orderparam");
		Object defoutwarehouseid = map.get("defoutwarehouseid");
		String planid = map.get("planid").toString();
		if (sortparam != null && GeneralUtil.checkParamSql(sortparam.toString()) == false) {
			map.put("sortparam", null);
		}
		if (orderparam != null && GeneralUtil.checkParamSql(orderparam.toString()) == false) {
			map.put("orderparam", null);
		}
		if(defoutwarehouseid != null && GeneralUtil.isNotEmpty(defoutwarehouseid.toString())) {
			List<String> materialList = new ArrayList<String>();
			List<PurchaseWareHouseMaterial> list = purchaseWareHouseMaterialService.getPurchaseForPlanIdAndWareHouseId(planid, defoutwarehouseid.toString());
			if(list != null && list.size() > 0) {
				for(PurchaseWareHouseMaterial purchaseWareHouseMaterial : list) {
					materialList.add(purchaseWareHouseMaterial.getMaterialid());
				}
				map.put("materialList", materialList);
			}else {
				return null;
			}
		}
		return purchasePlanMapper.getSale(page,map);
	}

	public Map<String, String> vaildPlan(List<PurchasePlanItem> oldplanitemlist) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < oldplanitemlist.size(); i++) {
			PurchasePlanItem item = oldplanitemlist.get(i);
			if (GeneralUtil.isEmpty(item.getSupplier()) || "undefined".equals(item.getSupplier())) {
				map.put("success", "false");
				String mymsg = "加入计划的SKU必须指定供应商";
				map.put("msg", mymsg);
				return map;
			}
			if (item.getAmount() == null || item.getAmount() == 0) {
				map.put("success", "false");
				String mymsg = "加入计划的SKU建议采购量不能为0";
				map.put("msg", mymsg);
				return map;
			}
		}
		map.put("success", "true");
		return map;
	}

	public List<Map<String, Object>> getSummaryPlan(String planid) {
		return purchasePlanItemMapper.summaryPlan(planid);
	}
	
	public List<Map<String, Object>> getSummaryPlanByWarehouse(String planid, String warehouseid,List<String> item_material_list) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("planid", planid);
		params.put("warehouseid", warehouseid);
		params.put("item_material_list", item_material_list);
		return purchasePlanItemMapper.getSummaryPlanByWarehouse(params);
	}

	public List<Map<String, Object>> getSummaryPlanOrder(String planid, String warehouseid,List<String> item_material_list) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("planid", planid);
		params.put("warehouseid", warehouseid);
		params.put("item_material_list", item_material_list);
		return purchasePlanItemMapper.summaryPlanOrder(params);
	}

	public void setPO(PurchasePlan plan) throws ERPBizException {
		if (plan.getPo() == null) {
			PurchasePlanSub po = new PurchasePlanSub();
			po.setFtype("po");
			po.setOperator(plan.getOperator());
			po.setOpttime(new Date());
			po.setPlanid(plan.getId());
			po.setStatus(1);// 0:放弃，1使用，2是提交
			purchasePlanSubService.save(po);
			plan.setPo(po);
		}
	}

	public void setAO(PurchasePlan plan) throws ERPBizException {
		if (plan.getAo() == null) {
			PurchasePlanSub ao = new PurchasePlanSub();
			ao.setFtype("ao");
			ao.setOperator(plan.getOperator());
			ao.setOpttime(new Date());
			ao.setPlanid(plan.getId());
			ao.setStatus(1);// 0:放弃，1使用，2是提交
			purchasePlanSubService.save(ao);
			plan.setAo(ao);
		}
	}

	public void setPOAO(PurchasePlan plan) throws ERPBizException {
		setPO(plan);
		setAO(plan);
	}

	@Override
	public boolean save(PurchasePlan entity) throws ERPBizException {
		int count = this.baseMapper.insert(entity);
		setPOAO(entity);
		return count>0;
	}

	public void afterSavePOForm(String planitem, String warehouseid) throws ERPBizException {
		purchasePlanItemMapper.updatePOStatusBySubPlan(planitem, warehouseid);
	}
	
	public void afterSavePOSubPlan(UserInfo user,String warehouseid) throws ERPBizException {
		List<Map<String, Object>> list = purchasePlanItemMapper.findPurchaseitemForWarehouseid(warehouseid, "po");
		if(list == null || list.size() == 0) {
			List<Map<String, Object>> subPlanList = purchasePlanItemMapper.findPurchaseSubForWarehouseid(warehouseid,"po");
			for(Map<String, Object> map : subPlanList) {
				String subPlanid = map.get("subid").toString();
				PurchasePlanSub plansub = purchasePlanSubMapper.selectById(subPlanid);
				plansub.setStatus(3);
				plansub.setOpttime(new Date());
				purchasePlanSubService.updateById(plansub);
			}
			Warehouse warehouse = warehouseService.getSelfWarehouse(warehouseid);
			PurchaseWareHouseStatus warehouseStatus = purchaseWareHouseStatusService.getById(warehouse.getId());
			if(warehouseStatus == null) {
				warehouseStatus = new PurchaseWareHouseStatus();
			}
			warehouseStatus.setOpptime(new Date());
			warehouseStatus.setUserid(user.getId());
			warehouseStatus.setPurchaseStatus(2);
			if(warehouseStatus.getWarehouseid() == null) {
				warehouseStatus.setAssblyStatus(0);
				warehouseStatus.setWarehouseid(warehouse.getId());
				purchaseWareHouseStatusService.save(warehouseStatus);
			}else {
				purchaseWareHouseStatusService.updateById(warehouseStatus);
			}
		  
		}
	}
	
	public void afterSaveASForm(String planitemid, String warehouseid) throws ERPBizException {
		purchasePlanItemMapper.updateAOStatusBySubPlan(planitemid, warehouseid);
	}
	
	public void afterSaveASSubplan(UserInfo user,String warehouseid) throws ERPBizException {
		//purchasePlanItemMapper.updateAOStatusByWarehouse(warehouseid);
		List<Map<String, Object>> list = purchasePlanItemMapper.findPurchaseitemForWarehouseid(warehouseid, "ao");
		if(list == null || list.size() == 0) {
			List<Map<String, Object>> subPlanList = purchasePlanItemMapper.findPurchaseSubForWarehouseid(warehouseid,"ao");
			for(Map<String, Object> map : subPlanList) {
				String subPlanid = map.get("subid").toString();
				PurchasePlanSub plansub = purchasePlanSubMapper.selectById(subPlanid);
				plansub.setStatus(3);
				purchasePlanSubService.updateById(plansub);
			}
			Warehouse warehouse = warehouseService.getSelfWarehouse(warehouseid);
			PurchaseWareHouseStatus warehouseStatus = purchaseWareHouseStatusService.getById(warehouse.getId());
			if (warehouseStatus == null) {
				warehouseStatus = new PurchaseWareHouseStatus();
			}
			warehouseStatus.setOpptime(new Date());
			warehouseStatus.setUserid(user.getId());
			warehouseStatus.setAssblyStatus(2);
			if (warehouseStatus.getWarehouseid() == null) {
				warehouseStatus.setPurchaseStatus(0);
				warehouseStatus.setWarehouseid(warehouse.getId());
				purchaseWareHouseStatusService.save(warehouseStatus);
			} else {
				purchaseWareHouseStatusService.updateById(warehouseStatus);
			}
		}
	}

	public List<Map<String, Object>> getSummaryPlanAssembly(String planid, String warehouseid, List<String> item_material_list) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("planid", planid);
		params.put("warehouseid", warehouseid);
		params.put("item_material_list", item_material_list);
		return purchasePlanItemMapper.summaryPlanAssembly(params);
	}

	public void hideMaterilItem(String materialid,HttpServletRequest request) throws ERPBizException {
		UserInfo user =UserInfoContext.get();
		MaterialMark materialMark = new MaterialMark();
		materialMark.setMaterialid(materialid);
		materialMark.setFtype("phide");
		materialMark.setOpttime(new Date());
		if(user!=null)materialMark.setOperator(user.getId());
		QueryWrapper<MaterialMark> queryWrapper = new QueryWrapper<MaterialMark>();
		queryWrapper.eq("ftype", "phide");
		queryWrapper.eq("materialid", materialid);
		MaterialMark old = materialMarkMapper.selectOne(queryWrapper);
		if (old == null) {
			materialMarkService.save(materialMark);
		}
	}

	public void showMaterilItem(String materialid) {
	 
		QueryWrapper<MaterialMark> queryWrapper = new QueryWrapper<MaterialMark>();
		queryWrapper.eq("ftype", "phide");
		queryWrapper.eq("materialid", materialid);
		MaterialMark old = materialMarkMapper.selectOne(queryWrapper);
		if (old != null) {
			materialMarkMapper.delete(queryWrapper);
		}
	}

	public PurchasePlan cancelPO(UserInfo user, String poid) throws ERPBizException {
		PurchasePlanSub po = purchasePlanSubMapper.selectById(poid);
		po.setStatus(0);
		po.setOperator(user.getId());
		po.setOpttime(new Date());
		purchasePlanSubService.updateById(po);
		return readWorkStatusByPlanId(user,po.getPlanid());
	}

	public PurchasePlan cancelAO(UserInfo user, String aoid) throws ERPBizException {
		PurchasePlanSub ao = purchasePlanSubMapper.selectById(aoid);
		ao.setStatus(0);
		ao.setOperator(user.getId());
		ao.setOpttime(new Date());
		purchasePlanSubService.updateById(ao);
		return readWorkStatusByPlanId(user,ao.getPlanid());
	}

	public Map<String, Object> changePlanDetailByPlan(UserInfo user,PurchasePlan oldplan) throws ERPBizException {
		Map<String, Object> maps = new HashMap<String, Object>();
		PurchasePlan plan = this.readWorkStatusByPlanId(user,oldplan.getId());
		QueryWrapper<PurchasePlanModel> queryWrapper = new QueryWrapper<PurchasePlanModel>();
		queryWrapper.eq("planid", oldplan.getId());
		List<PurchasePlanModel> purchasePlanCalModelList = purchasePlanModelMapper.selectList(queryWrapper);
		if (purchasePlanCalModelList.size() > 0 && purchasePlanCalModelList != null) {
			PurchasePlanModel purchasePlanCalModel = purchasePlanCalModelList.get(0);
			plan.setCaltime(purchasePlanCalModel.getRefreshtime());
			plan.setIsrun(purchasePlanCalModel.isIsrun());
			maps.put("modelid", purchasePlanCalModel.getModelid());
			String operator = purchasePlanCalModel.getOpeartor();
			if (operator != null) {
				  Result<Map<String, Object>> info = adminClientOneFeign.getUserByUserId(operator);
				if (info != null&&Result.isSuccess(info)&&info.getData()!=null) {
					maps.put("caloperator",info.getData().get("name"));
				}
			}
		}

		maps.put("plan", plan);
		return maps;
	}

	public Map<String, Object> ChangeMainGroup(String planid, String groupid) throws ERPBizException {
		Map<String, Object> maps = new HashMap<String, Object>();
		PurchasePlan plan = purchasePlanMapper.selectById(planid);
		PurchasePlanModel purchasePlanCalModel = new PurchasePlanModel();
		purchasePlanCalModel.setPlanid(plan.getId());
		PurchasePlanModel old = purchasePlanModelMapper.selectById(purchasePlanCalModel.getPlanid());
		plan.setCaltime(old.getRefreshtime());
		String calOperator = old.getOpeartor();
		maps.put("plan", plan);
		if (calOperator != null) {
			Result<Map<String, Object>> userInfo = adminClientOneFeign.getUserByUserId(calOperator);
			if(userInfo!=null&&Result.isSuccess(userInfo)&&userInfo.getData()!=null)
			maps.put("caloperator", userInfo.getData().get("name"));
		}
		return maps;
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
	
	public Map<String, Object> getNotPlanWarehouse(String shopid) {
		Map<String, Object> map=new HashMap<String, Object>();
		List<Map<String, Object>> uselist = purchasePlanMapper.getUsePlanWarehouse(shopid);
		List<Map<String, Object>> nolist = purchasePlanMapper.getNotPlanWarehouse(shopid);
		map.put("uselist", uselist);
		map.put("nolist", nolist);
		return map;  
	}
	
	
	
}
