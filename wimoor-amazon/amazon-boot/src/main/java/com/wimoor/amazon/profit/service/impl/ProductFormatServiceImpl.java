package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.ProductFormatMapper;
import com.wimoor.amazon.profit.pojo.entity.ProductFormat;
import com.wimoor.amazon.profit.service.IProductFormatService;

import lombok.RequiredArgsConstructor;

@Service("productFormatService")
@RequiredArgsConstructor
public class ProductFormatServiceImpl  extends ServiceImpl<ProductFormatMapper, ProductFormat> implements IProductFormatService {
 
	
	@Cacheable(value = "productFormatCache")
	public List<Map<String, Object>> findTierFormatByCountry(String country) {
		return this.baseMapper.findByCountry("%" + country + "%");
	}
	
	@Cacheable(value = "smlProductFormatCache")
	public List<Map<String, Object>> findSmlProductTierByCountry(String country) {
		return this.baseMapper.findSmlProductTier("%" + country + "%");
	}
	
	@Override
	@CacheEvict(value={"productFormatCache","smlProductFormatCache"}, allEntries = true)
	public boolean save(ProductFormat entity)  {
		return this.baseMapper.insert(entity)>0?true:false;
	}

	@Override
	@CacheEvict(value = {"productFormatCache","smlProductFormatCache"}, allEntries = true)
	public boolean removeById(Serializable key) {
		return  this.baseMapper.deleteById(key)>0?true:false;
	}

	@Override
	@CacheEvict(value = {"productFormatCache","smlProductFormatCache"}, allEntries = true)
	public boolean updateById(ProductFormat entity)  {
		return this.baseMapper.updateById(entity)>0?true:false;
	}
	
}
