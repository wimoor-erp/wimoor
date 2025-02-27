package com.wimoor.amazon.profit.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.ProfitConfigCountryMapper;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.service.IProfitCfgCountryService;

import lombok.RequiredArgsConstructor;



@Service("profitCfgCountryService")
@RequiredArgsConstructor
public class ProfitCfgCountryServiceImpl extends ServiceImpl<ProfitConfigCountryMapper, ProfitConfigCountry> implements IProfitCfgCountryService {
    
	
	
	@Cacheable(value = "profitCfgCountryCache", key = "#profitCfgId")
	public List<ProfitConfigCountry> findByProfitId(String profitCfgId) {
		return this.baseMapper.findByProfitId(profitCfgId);
	}
	
	@CacheEvict(value = "profitCfgCountryCache", allEntries = true)
	public int insert(ProfitConfigCountry entity) {
		entity.setId(null);
		return this.baseMapper.insert(entity);
	}

	@CacheEvict(value = "profitCfgCountryCache", allEntries = true)
	public boolean remove(ProfitConfigCountry entity) {
		return this.baseMapper.deleteById(entity.getId())>0?true:false;
	}

	@CacheEvict(value = "profitCfgCountryCache", allEntries = true)
	public int update(ProfitConfigCountry entity) {
		return this.baseMapper.updateById(entity);
	}

}
