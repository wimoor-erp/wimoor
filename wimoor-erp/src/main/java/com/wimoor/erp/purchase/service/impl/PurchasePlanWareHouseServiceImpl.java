package com.wimoor.erp.purchase.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.mapper.PurchasePlanMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanModelMapper;
import com.wimoor.erp.purchase.mapper.PurchasePlanWareHouseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlan;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanModel;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanWareHouse;
import com.wimoor.erp.purchase.service.IPurchasePlanWareHouseService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;

import lombok.RequiredArgsConstructor;
 

@Service("purchasePlanWareHouseService")
@RequiredArgsConstructor
public class PurchasePlanWareHouseServiceImpl extends  ServiceImpl<PurchasePlanWareHouseMapper,PurchasePlanWareHouse> implements IPurchasePlanWareHouseService {

	final PurchasePlanMapper purchasePlanMapper;
	final PurchasePlanModelMapper purchasePlanModelMapper;
	final ISerialNumService serialNumService;
	 
	final IPurchaseWareHouseMaterialService purchaseWareHouseMaterialService;
	
	public Boolean getPlanIsRunForModel(String planid) {
		PurchasePlanModel purchasePlanModel = purchasePlanModelMapper.selectById(planid);
		if(purchasePlanModel == null) {
			return false;
		}else {
			return purchasePlanModel.isIsrun();
		}
	}
	
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
	
	public List<Map<String, Object>> getWareHouseIdForPlanId(String planid, String shopid){
		return this.baseMapper.getWareHouseIdForPlanId(planid, shopid);
	}
	
	public int updatePlanWareHouse(String warehouseid, String planid, UserInfo user) {
		int result = 0;
		if(GeneralUtil.isEmpty(warehouseid)) {
			throw new ERPBizException("请选择正确的仓库");
		}
		String[] warehouseidArray = warehouseid.split(",");
		List<String> oldList = new ArrayList<String>();
		List<Map<String, Object>> warehouseList = this.baseMapper.getWareHouseIdForPlanId(planid, user.getCompanyid());
		for(Map<String, Object> map : warehouseList) {
			oldList.add(map.get("warehouseid").toString());
		}
		List<String> newList = new ArrayList<String>();
		for(int i = 0; i < warehouseidArray.length; i++) {
			if(oldList.contains(warehouseidArray[i])) {
				oldList.remove(warehouseidArray[i]);
			}else {
				newList.add(warehouseidArray[i]);
			}
		}
		if(newList.size() > 0) {
			for(String addWareid : newList) {
				String addPlanid = getPlanIdForWareHouseId(addWareid, user.getCompanyid());
				if(addPlanid == null) {
					Map<String, Object> mapDate = new HashMap<String, Object>();
					mapDate.put("planid", planid);
					mapDate.put("warehouseid", addWareid);
					result += savaPlanWareHouse(mapDate, user);
				}else {
					throw new ERPBizException("该仓库已存在其他补货规划中");
				}
			}
		}
		if(oldList.size() > 0) {
			for(String removeWareid : oldList) {
				result += this.baseMapper.deleteById(removeWareid);
			}
		}
		return result;
	}
	
	public List<Map<String, Object>> getPurchasePlanWareHouseForShopId(String shopid){
		return this.baseMapper.getPurchasePlanWareHouseForShopId(shopid);
	}
	
	public int savaPlanWareHouse(Map<String, Object> map, UserInfo user) {
		if(map == null || map.get("warehouseid") == null) {
			throw new ERPBizException("请选择正确的仓库");
		}
		Object planid = map.get("planid");
		String warehouseid = map.get("warehouseid").toString();
		if(planid == null || planid == "" || "newPlanId".equals(planid.toString())) {
			try {
				String pnumber = serialNumService.readSerialNumber(user.getCompanyid(), "PP");
				PurchasePlan plan = new PurchasePlan();
				plan.setCreator(user.getId());
				plan.setOperator(user.getId());
				plan.setNumber(pnumber);
				plan.setShopid(user.getCompanyid());
				plan.setStatus((byte) 1);
				plan.setCreatetime(new Date());
				plan.setTotalbuyqty(0);
				plan.setTotalnum(0);
				plan.setOpttime(new Date());
				planid = plan.getId();
				map.put("planid", planid);
				purchasePlanMapper.insert(plan);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String[] warehouseidArray = warehouseid.split(",");
		int result = 0;
		for(int i = 0; i < warehouseidArray.length; i++) {
			PurchasePlanWareHouse purchasePlanWareHouse = new PurchasePlanWareHouse();
			purchasePlanWareHouse.setPlanid(planid.toString());
			purchasePlanWareHouse.setShopid(user.getCompanyid());
			purchasePlanWareHouse.setWarehouseid(warehouseidArray[i]);
			result += this.baseMapper.insert(purchasePlanWareHouse);
		}
		return result;
	}
	
	public List<Map<String, Object>> refreshPlanWareHouse(List<Map<String, Object>> list, UserInfo user) {
		List<Map<String, Object>> addPlanWarehouseList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> updatePlanWarehouseList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> selfPlanWarehouseList = purchasePlanMapper.getPurchasePlanForShopId(user.getCompanyid());
		if(list==null || list.size()==0) {
			throw new ERPBizException("请正确选择仓库，计划下必须包含一个仓库！");
		}
		for(Map<String, Object> map : list) {
			Object planid = map.get("planid");
			Object warehouseid = map.get("warehouseid");
			if(planid == null || planid == "" || "newPlanId".equals(planid.toString())) {
				if(warehouseid != null && warehouseid != "") {
					addPlanWarehouseList.add(map);
				}
			}else {
				for(Map<String, Object> selfMap : selfPlanWarehouseList) {
					Object ftype = selfMap.get("ftype");
					if(ftype != null) {
						continue;
					}
					String selfPlanid = selfMap.get("planid").toString();
					if(planid.toString().equals(selfPlanid)) {
						String[] selfWarehouseidArray = selfMap.get("warehouseid").toString().split(",");
						String[] warehouseidArray = warehouseid.toString().split(",");
						Arrays.sort(selfWarehouseidArray);
						Arrays.sort(warehouseidArray);
						if(ArrayToString(selfWarehouseidArray, ",").equals(ArrayToString(warehouseidArray, ","))) {
							selfMap.put("ftype", "same");
							break;
						}else {
							selfMap.put("ftype", "update");
							updatePlanWarehouseList.add(map);
							break;
						}
					}
				}
			}
		}
		
		for(Map<String, Object> selfMap : selfPlanWarehouseList) {
			Object ftype = selfMap.get("ftype");
			if(ftype == null) {
				if(getPlanIsRunForModel(selfMap.get("planid").toString())) {
					throw new ERPBizException("该补货规划正在计算，不能操作！");
				}else {
					this.baseMapper.deletePlanWarehouseForPlanId(selfMap.get("planid").toString());
				}
			} 
		}
		for(Map<String, Object> updateMap : updatePlanWarehouseList) {
			if(getPlanIsRunForModel(updateMap.get("planid").toString())) {
				throw new ERPBizException("该补货规划正在计算，不能操作！");
			}else {
				updatePlanWareHouse(updateMap.get("warehouseid").toString(), updateMap.get("planid").toString(), user);
				//purchasePlanService.refreshPlanModel(user, updateMap.get("planid").toString(), updateMap.get("warehouseid").toString());
			}
		}
		for(Map<String, Object> addMap : addPlanWarehouseList) {
			savaPlanWareHouse(addMap, user);
			//purchasePlanService.refreshPlanModel(user, addMap.get("planid").toString(), addMap.get("warehouseid").toString());
		}

		return list;
	}
	
	public String ArrayToString(String[] array, String split) {
		StringBuffer str = new StringBuffer();
		for (String s : array) {
		    str.append(s + split);
		}
		return str.substring(0, str.length() - 1).toString();
	}
	
}