package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.OutboundWeightFormatMapper;
import com.wimoor.amazon.profit.pojo.entity.OutboundWeightFormat;
import com.wimoor.amazon.profit.service.IOutBoundWeightFormatService;

import lombok.RequiredArgsConstructor;
 
@Service("outboundWeightFormatService")
@RequiredArgsConstructor
public class OutBoundWeightFormatServiceImpl  extends ServiceImpl<OutboundWeightFormatMapper, OutboundWeightFormat> implements IOutBoundWeightFormatService {
 
 
	@Cacheable(value = "outboundWeightCache")
	public OutboundWeightFormat findByProductTierId(String productTierId, boolean media) {
		return this.baseMapper.findByProductTierId(productTierId, media);
	}
	
	@Override
	@CacheEvict(value = "outboundWeightCache", allEntries = true)
	public boolean save(OutboundWeightFormat entity) {
		return this.baseMapper.insert(entity)>0?true:false;
	}

	@Override
	@CacheEvict(value = "outboundWeightCache", allEntries = true)
	public boolean removeById(Serializable key) {
		return this.baseMapper.deleteById(key)>0?true:false;
	}

	@Override
	@CacheEvict(value = "outboundWeightCache", allEntries = true)
	public boolean updateById(OutboundWeightFormat entity) {
		return this.baseMapper.updateById(entity)>0?true:false;
	}




}
