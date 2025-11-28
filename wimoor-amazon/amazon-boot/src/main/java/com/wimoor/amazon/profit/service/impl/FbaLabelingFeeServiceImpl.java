package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.FBALabelingFeeMapper;
import com.wimoor.amazon.profit.pojo.entity.FBALabelingFee;
import com.wimoor.amazon.profit.service.IFbaLabelingFeeService;

import lombok.RequiredArgsConstructor;

 
@Service("fbaLabelingFeeService")
@RequiredArgsConstructor
public class FbaLabelingFeeServiceImpl extends ServiceImpl<FBALabelingFeeMapper, FBALabelingFee> implements IFbaLabelingFeeService {
		
	@Cacheable(value = "fbaLabelingFeeCache")
	public FBALabelingFee getPriceByProductTierId(boolean isStandard, String country) {
		return this.baseMapper.getPriceByProductTierId(isStandard, country);
	}
	
	@Override
	@CacheEvict(value = "fbaLabelingFeeCache", allEntries = true)
	public boolean save(FBALabelingFee entity)  {
		return this.save(entity);
	}

	@Override
	@CacheEvict(value = "fbaLabelingFeeCache", allEntries = true)
	public boolean removeById(Serializable key) {
		return this.removeById(key);
	}

	@Override
	@CacheEvict(value = "fbaLabelingFeeCache", allEntries = true)
	public boolean updateById(FBALabelingFee entity){
		return this.updateById(entity);
	}
	

}
