package com.wimoor.amazon.inbound.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.FBAShipCycleMapper;
import com.wimoor.amazon.inbound.pojo.entity.FBAShipCycle;
import com.wimoor.amazon.inbound.service.IFBAShipCycleService;
import com.wimoor.common.user.UserInfo;

import lombok.RequiredArgsConstructor;

@Service("fBAShipCycleService")
@RequiredArgsConstructor
public class FBAShipCycleServiceImpl extends ServiceImpl<FBAShipCycleMapper,FBAShipCycle> implements IFBAShipCycleService{
	
	@CacheEvict(value = { "FbaCycle" }, allEntries = true)
	public int updateStockCycleTransType(FBAShipCycle cycle, UserInfo user) {
		int result = 0;
		FBAShipCycle stockcy =  this.baseMapper.findShipCycleBySKU(cycle.getSku(), cycle.getMarketplaceid(), cycle.getGroupid().toString());
		if (stockcy == null) {
			stockcy =cycle;
			stockcy.setOperator(new BigInteger(user.getId()));
			stockcy.setOpttime(new Date());
			if( this.save(stockcy)) {
				result++;
			}
		}else {
			stockcy.setStockingcycle(cycle.getStockingcycle());
			stockcy.setTranstype(cycle.getTranstype());
			stockcy.setOperator(new BigInteger(user.getId()));
			stockcy.setOpttime(new Date());
			this.baseMapper.deleteById(stockcy.getId());
		   return this.baseMapper.insert(stockcy);
		}
		return result;
	}
	
 
	@CacheEvict(value = { "FbaCycle" }, allEntries = true)
	public int updateStockCycle(String groupid,String marketplaceid,String sku,Integer stockcycle,Integer mincycle, BigDecimal fee, UserInfo user) {
		int result = 0;
		FBAShipCycle stockcy =  this.baseMapper.findShipCycleBySKU(sku, marketplaceid, groupid);
		if (stockcy == null) {
			stockcy = new FBAShipCycle();
			stockcy.setStockingcycle(stockcycle);
			stockcy.setMinCycle(mincycle);
			stockcy.setFirstLegCharges(fee);
			stockcy.setSku(sku);
			stockcy.setMarketplaceid(marketplaceid);
			stockcy.setGroupid(new BigInteger(groupid));
			stockcy.setOperator(new BigInteger(user.getId()));
			stockcy.setOpttime(new Date());
			if( this.save(stockcy)) {
				result++;
			}
		}else {
			stockcy.setStockingcycle(stockcycle);
			stockcy.setMinCycle(mincycle);
			stockcy.setFirstLegCharges(fee);
			stockcy.setOperator(new BigInteger(user.getId()));
			stockcy.setOpttime(new Date());
			if(this.updateById(stockcy)) {
				result++;
			}
		}
		return result;
	}
	@CacheEvict(value = { "FbaCycle" }, allEntries = true)
	public boolean updateMinCycle(String groupid,String marketplaceid,String type,String sku,Integer num, UserInfo user) {
			FBAShipCycle stockcy =  this.baseMapper.findShipCycleBySKU(sku, marketplaceid, groupid);
			if (stockcy == null) {
				stockcy = new FBAShipCycle();
				stockcy.setMinCycle(num);
				stockcy.setSku(sku);
				stockcy.setMarketplaceid(marketplaceid);
				stockcy.setGroupid(new BigInteger(groupid));
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
	@CacheEvict(value = { "FbaCycle" }, allEntries = true)
	public boolean updateFirstLegCharges(String groupid,String marketplaceid,String type,String sku,Integer num, UserInfo user) {
		FBAShipCycle stockcy =  this.baseMapper.findShipCycleBySKU(sku, marketplaceid, groupid);
		if (stockcy == null) {
			stockcy = new FBAShipCycle();
			stockcy.setFirstLegCharges(new BigDecimal(num));
			stockcy.setSku(sku);
			stockcy.setMarketplaceid(marketplaceid);
			stockcy.setGroupid(new BigInteger(groupid));
			stockcy.setOperator(new BigInteger(user.getId()));
			stockcy.setOpttime(new Date());
			return this.save(stockcy);
		}else {
			stockcy.setFirstLegCharges(new BigDecimal(num));
			stockcy.setOperator(new BigInteger(user.getId()));
			stockcy.setOpttime(new Date());
			return this.updateById(stockcy);
		}
}

	@Override
	@Cacheable(value = "FbaCycle")
	public FBAShipCycle getFbaShipCycle(String groupid,String marketplaceid,String sku) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<FBAShipCycle> query=new LambdaQueryWrapper<FBAShipCycle>();
		query.eq(FBAShipCycle::getMarketplaceid, marketplaceid);
		query.eq(FBAShipCycle::getGroupid, groupid);
		query.eq(FBAShipCycle::getSku, sku);
		return this.getOne(query);
	}

	@Override
	@Cacheable(value = "FbaCycle")
	public Map<String,FBAShipCycle> getFbaShipCycle(String groupid,String marketplaceid) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<FBAShipCycle> query=new LambdaQueryWrapper<FBAShipCycle>();
		query.eq(FBAShipCycle::getMarketplaceid, marketplaceid);
		query.eq(FBAShipCycle::getGroupid, groupid);
		List<FBAShipCycle> list = this.list(query);
		Map<String,FBAShipCycle> result=new HashMap<String,FBAShipCycle>();
		for(FBAShipCycle item:list) {
			result.put(item.getSku(), item);
		}
		return result;
	}


 
}
