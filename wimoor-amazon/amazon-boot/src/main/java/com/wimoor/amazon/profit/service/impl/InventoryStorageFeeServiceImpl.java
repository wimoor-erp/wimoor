package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.InventoryStorageFeeMapper;
import com.wimoor.amazon.profit.pojo.entity.InventoryStorageFee;
import com.wimoor.amazon.profit.service.IInventoryStorageFeeService;

import lombok.RequiredArgsConstructor;
 
 
@Service("inventoryStorageFeeService")
@RequiredArgsConstructor
public class InventoryStorageFeeServiceImpl extends ServiceImpl<InventoryStorageFeeMapper, InventoryStorageFee> implements IInventoryStorageFeeService {
	
	@Cacheable(value = "inventoryStorageFeeCache")
	public InventoryStorageFee getPriceByCountry(String country, String month, boolean isStandard) {
		return this.baseMapper.getPriceByCountry(country, month, isStandard);
	}
	
	@Override
	@CacheEvict(value = "inventoryStorageFeeCache", allEntries = true)
	public boolean save(InventoryStorageFee entity)  {
		return this.baseMapper.insert(entity)>0?true:false;
	}

	@Override
	@CacheEvict(value = "inventoryStorageFeeCache", allEntries = true)
	public boolean removeById(Serializable key) {
		return this.baseMapper.deleteById(key)>0?true:false;
	}

	@Override
	@CacheEvict(value = "inventoryStorageFeeCache", allEntries = true)
	public boolean updateById(InventoryStorageFee entity)  {
		return this.baseMapper.updateById(entity)>0?true:false;
	}


}
