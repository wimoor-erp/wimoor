package com.wimoor.erp.ship.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.mapper.FBAShipCycleMapper;
import com.wimoor.erp.ship.pojo.entity.FBAShipCycle;
import com.wimoor.erp.ship.service.IFBAShipCycleService;

import lombok.RequiredArgsConstructor;

@Service("fBAShipCycleService")
@RequiredArgsConstructor
public class FBAShipCycleServiceImpl extends ServiceImpl<FBAShipCycleMapper,FBAShipCycle> implements IFBAShipCycleService{
	 
	IMaterialService materialService;
	
	public int updateStockCycle(Map<String, Object> map, UserInfo user) {
		// 更新stockcycle表的天数
		if(map.get("materialid") == null || map.get("marketplaceid") == null) {
			return 0;
		}
		String materialid = map.get("materialid").toString();
		String shopid = map.get("shopid").toString();
		String marketplaceid = map.get("marketplaceid").toString();
		String type = map.get("type") == null ? null : map.get("type").toString();
		Integer num = map.get("num") == null ? null : Integer.parseInt(map.get("num").toString());
		String sku = map.get("sku") == null ? null : map.get("sku").toString();
		String groupid = map.get("groupid") == null ? null : map.get("groupid").toString();
		List<Map<String, Object>> skuList = null;
		if(sku == null) {
			skuList = this.baseMapper.findGroupByMaterilId(shopid, materialid, marketplaceid);
		}else {
			skuList = new ArrayList<Map<String, Object>>();
			Map<String, Object> skuMap = new HashMap<String, Object>();
			skuMap.put("sku", sku);
			if(groupid == null) {
				Map<String, Object> groupMap =  this.baseMapper.findGroupBySKU(shopid, sku, marketplaceid);
				skuMap.put("groupid", groupMap.get("groupid"));
			}else {
				skuMap.put("groupid", groupid);
			}
			skuList.add(skuMap);
		}
		int result = 0;
		for(Map<String,Object> maps : skuList) {
			String skus = maps.get("sku").toString();
			String groupids = maps.get("groupid").toString();
			FBAShipCycle stockcy =  this.baseMapper.findShipCycleBySKU(skus, marketplaceid, groupids);
			if (stockcy == null) {
				stockcy = new FBAShipCycle();
				if ("cycle".equals(type)) {
					stockcy.setStockingcycle(num);
				} 
				stockcy.setSku(sku);
				stockcy.setMarketplaceid(marketplaceid);
				stockcy.setGroupid(new BigInteger(groupids));
				stockcy.setOperator(new BigInteger(user.getId()));
				stockcy.setOpttime(new Date());
				if( this.save(stockcy)) {
					result++;
				}
			}else {
				if("cycle".equals(type)) {
					stockcy.setStockingcycle(num);
				} 
				stockcy.setOperator(new BigInteger(user.getId()));
				stockcy.setOpttime(new Date());
				if(this.updateById(stockcy)) {
					result++;
				}
			}
		}
		return result;
	}

	public boolean updateMinCycle(Map<String, Object> map, UserInfo user) {
		if(map.get("materialid") == null || map.get("marketplaceid") == null || map.get("sku") == null) {
			return false;
		}
		String shopid = map.get("shopid").toString();
		String marketplaceid = map.get("marketplaceid").toString();
		Integer num = map.get("num") == null ? null : Integer.parseInt(map.get("num").toString());
		String sku = map.get("sku") == null ? null : map.get("sku").toString();
		Map<String, Object> groupMap =  this.baseMapper.findGroupBySKU(shopid, sku, marketplaceid);
		if(groupMap != null && groupMap.get("groupid") != null) {
			FBAShipCycle stockcy =  this.baseMapper.findShipCycleBySKU(sku, marketplaceid, groupMap.get("groupid").toString());
			if (stockcy == null) {
				stockcy = new FBAShipCycle();
				stockcy.setMinCycle(num);
				stockcy.setSku(sku);
				stockcy.setMarketplaceid(marketplaceid);
				stockcy.setGroupid(new BigInteger(groupMap.get("groupid").toString()));
				stockcy.setOperator(new BigInteger(user.getId()));
				stockcy.setOpttime(new Date());
				return this.save(stockcy);
			}else {
				stockcy.setMinCycle(num);
				stockcy.setOperator(new BigInteger(user.getId()));
				stockcy.setOpttime(new Date());
				return this.updateById(stockcy);
			}
		}
		return false;
	}

	public boolean updateFirstLegCharges(Map<String, Object> map, UserInfo user) {
		if(map.get("materialid") == null || map.get("marketplaceid") == null || map.get("sku") == null) {
			return false;
		}
		String shopid = map.get("shopid").toString();
		String marketplaceid = map.get("marketplaceid").toString();
		BigDecimal num = map.get("num") == null ? new BigDecimal("0") : new BigDecimal(map.get("num").toString());
		String sku = map.get("sku") == null ? null : map.get("sku").toString();
		Map<String, Object> groupMap =  this.baseMapper.findGroupBySKU(shopid, sku, marketplaceid);
		if(groupMap != null && groupMap.get("groupid") != null) {
			FBAShipCycle stockcy =  this.baseMapper.findShipCycleBySKU(sku, marketplaceid, groupMap.get("groupid").toString());
			if (stockcy == null) {
				stockcy = new FBAShipCycle();
				stockcy.setFirstLegCharges(num);
				stockcy.setSku(sku);
				stockcy.setMarketplaceid(marketplaceid);
				stockcy.setGroupid(new BigInteger(groupMap.get("groupid").toString()));
				stockcy.setOperator(new BigInteger(user.getId()));
				stockcy.setOpttime(new Date());
				return this.save(stockcy);
			}else {
				stockcy.setFirstLegCharges(num);
				stockcy.setOperator(new BigInteger(user.getId()));
				stockcy.setOpttime(new Date());
				return this.updateById(stockcy);
			}
		}
		return false;
	}

}
