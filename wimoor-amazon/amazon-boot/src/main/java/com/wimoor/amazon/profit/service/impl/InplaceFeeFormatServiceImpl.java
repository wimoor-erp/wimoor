package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.InplaceFeeFormatMapper;
import com.wimoor.amazon.profit.pojo.entity.InplaceFeeFormat;
import com.wimoor.amazon.profit.service.IInplaceFeeFormatService;

import lombok.RequiredArgsConstructor;
 
	
@Service("inplaceFeeFormatService")
@RequiredArgsConstructor
public class InplaceFeeFormatServiceImpl extends ServiceImpl<InplaceFeeFormatMapper, InplaceFeeFormat> implements IInplaceFeeFormatService {
		
		
	@Resource
	InplaceFeeFormatMapper inplaceFeeFormatMapper;

	@Cacheable(value = "inplaceFeeCache")
	public InplaceFeeFormat findByInvplaceFeeId(String invplaceFee, boolean isStandard, String country) {
		return this.baseMapper.findByInvplaceFeeId(invplaceFee, isStandard, country);
	}

	@Cacheable(value = "inplaceFeeCache")
	public InplaceFeeFormat findByProductTierId(String productTierId) {
		return  this.baseMapper.findByProductTierId(productTierId);
	}
	
	@Override
	@CacheEvict(value = "inplaceFeeCache", allEntries = true)
	public boolean save(InplaceFeeFormat entity)  {
		return this.save(entity);
	}

	@Override
	@CacheEvict(value = "inplaceFeeCache", allEntries = true)
	public boolean removeById(Serializable key) {
		return this.removeById(key);
	}

	@Override
	@CacheEvict(value = "inplaceFeeCache", allEntries = true)
	public boolean updateById(InplaceFeeFormat entity) {
		return this.updateById(entity);
	}


}
