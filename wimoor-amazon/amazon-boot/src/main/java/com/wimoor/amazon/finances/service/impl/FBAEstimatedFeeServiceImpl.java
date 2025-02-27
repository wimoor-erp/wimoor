package com.wimoor.amazon.finances.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.finances.mapper.FBAEstimatedFeeMapper;
import com.wimoor.amazon.finances.pojo.entity.FBAEstimatedFee;
import com.wimoor.amazon.finances.service.IFBAEstimatedFeeService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
@Service
public class FBAEstimatedFeeServiceImpl extends ServiceImpl<FBAEstimatedFeeMapper, FBAEstimatedFee> implements IFBAEstimatedFeeService {

	
	@Override
	@Cacheable(value = "fbaFeeCache")
	public FBAEstimatedFee getOneBySku(String sku, String asin, String amazonauthid, String marketplaceid) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<FBAEstimatedFee> queryFbaFee=new LambdaQueryWrapper<FBAEstimatedFee>();
		queryFbaFee.eq(FBAEstimatedFee::getSku,sku);
		queryFbaFee.eq(FBAEstimatedFee::getAsin,asin );
		queryFbaFee.eq(FBAEstimatedFee::getAmazonauthid, amazonauthid);
		queryFbaFee.eq(FBAEstimatedFee::getMarketplaceid,marketplaceid);
		return this.getOne(queryFbaFee);
	}

	@Override
	public Long selectCount(QueryWrapper<FBAEstimatedFee> query) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectCount(query);
	}

	@Override
	@Caching(evict={@CacheEvict(value = "fbaFeeCache", allEntries = true)})
	public boolean save(FBAEstimatedFee entity) {
		// TODO Auto-generated method stub
		return super.baseMapper.insert(entity)>0;
	}

	@Override
	@Caching(evict={@CacheEvict(value = "fbaFeeCache", allEntries = true)})
	public boolean updateById(FBAEstimatedFee entity) {
		// TODO Auto-generated method stub
		return super.baseMapper.updateById(entity)>0;
	}

	@Override
	@Caching(evict={@CacheEvict(value = "fbaFeeCache", allEntries = true)})
	public boolean update(FBAEstimatedFee entity, Wrapper<FBAEstimatedFee> updateWrapper) {
		// TODO Auto-generated method stub
		return super.baseMapper.update(entity, updateWrapper)>0;
	}
	
 
}
