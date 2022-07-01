package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.FBAFormatMapper;
import com.wimoor.amazon.profit.pojo.entity.FBAFormat;
import com.wimoor.amazon.profit.service.IFbaFormatService;

import lombok.RequiredArgsConstructor;

 
	 
@Service("fbaFormatService")
@RequiredArgsConstructor
public class FbaFormatServiceImpl extends ServiceImpl<FBAFormatMapper, FBAFormat> implements IFbaFormatService {
		
		
	@Resource
	FBAFormatMapper fbaFormatMapper;
	
	@Cacheable(value = "fbaFormatCache")
	public FBAFormat findByProductTierIdNew(String productTierId, boolean isClothing,String country) {
		if(!country.contains("%")) {
			country="%"+country+"%";
		}
		return this.baseMapper.findByProductTierIdNew(productTierId, isClothing,country);
	}

	@Cacheable(value = "fbaFormatCache")
	public FBAFormat findByProductTierIdAndWeight(String productTierId, BigDecimal outboundWeight,String country) {
		if(!country.contains("%")) {
			country="%"+country+"%";
		}
		return this.baseMapper.findByProductTierIdAndWeight(productTierId, outboundWeight,country);
	}
	
	@Cacheable(value = "fbaFormatCache")
	public FBAFormat findByProductTierIdAndWeightSL(String productTierId, BigDecimal outboundWeight,String fenpeiType,String country) {
		if(!country.contains("%")) {
			country="%"+country+"%";
		}
		return this.baseMapper.findByProductTierIdAndWeightSL(productTierId, outboundWeight,fenpeiType,country);
	}
	
	@Cacheable(value = "fbaFormatCache")
	public FBAFormat findByProductTierIdAndIsMedia2(String productTierId, boolean media,String country) {
		if(!country.contains("%")) {
			country="%"+country+"%";
		}
		return this.baseMapper.findByProductTierIdAndIsMedia2(productTierId, media,country);
	}

	@Cacheable(value = "fbaFormatCache")
	public FBAFormat findByProductTierAndType(String productTierId, String shipmentType,String country) {
		if(!country.contains("%")) {
			country="%"+country+"%";
		}
		return this.baseMapper.findByProductTierAndType(productTierId, shipmentType,country);
	}

	@Cacheable(value = "fbaFormatCache")
	public FBAFormat findEUfbaFormat(String fenpeiType, String productTierId, String country, BigDecimal weight) {
		if(!country.contains("%")) {
			country="%"+country+"%";
		}
		return this.baseMapper.findEUfbaFormat(fenpeiType, productTierId, country, weight);
	}

	
	@Override
	@CacheEvict(value = "fbaFormatCache", allEntries = true)
	public boolean save(FBAFormat entity)  {
		return super.save(entity);
	}

	@Override
	@CacheEvict(value = "fbaFormatCache", allEntries = true)
	public boolean removeById(Serializable key) {
		return super.removeById(key);
	}

	@Override
	@CacheEvict(value = "fbaFormatCache", allEntries = true)
	public boolean updateById(FBAFormat entity)  {
		return super.updateById(entity);
	}

}
