package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.ReferralFeeMapper;
import com.wimoor.amazon.profit.pojo.entity.ReferralFee;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.IReferralFeeService;

import lombok.RequiredArgsConstructor;

 
@Service("referralFeeService")
@RequiredArgsConstructor
public class ReferralFeeServiceImpl extends ServiceImpl<ReferralFeeMapper, ReferralFee> implements IReferralFeeService {
    
	@Resource
	@Lazy
	IProfitService profitService;
	
	// Everything Else*的id为41，其他国家的Everything Else类型都是它的子类
	int otherId = 41;
	
	//Amazon Referral Fee,根据售价和商品类别计算 
	
	@Cacheable(value = "referralFeeCache")
	public ReferralFee getReferralFeeByType(int typeId) {
		ReferralFee referralFee = this.baseMapper.selectById(typeId);
		return referralFee;
	}
	
	@Cacheable(value = "referralFeeCache")
	public ReferralFee findCommonOther(String country) {
		return this.baseMapper.findByTypeId(otherId, "%"+country+"%");
	}

	@Cacheable(value = "referralFeeCache")
	public ReferralFee getReferralFeeByTypeCountry(int typeId, String country) {
		ReferralFee referralFee = this.baseMapper.findByTypeId(typeId, "%"+country+"%");
		if (referralFee == null) {// 如果没查到，则按各个国家的Everything Else*类查询
			referralFee = this.baseMapper.findByTypeId(otherId, "%"+country+"%");
		}
		return referralFee;
	}
	
	@Cacheable(value = "referralFeeCache")
	public List<ReferralFee> findAllType() {
		return this.baseMapper.findAllType();
	}
	
	@Cacheable(value = "referralFeeCache")
	public ReferralFee findByPgroup(String group, String country) {
		ReferralFee referralFee = this.baseMapper.findByPgroup("%"+group+ "%", "%"+country+"%");
		if (referralFee == null) {// 如果没查到，则按各个国家的Everything Else*类查询
			referralFee = this.baseMapper.findByTypeId(otherId, "%"+country+"%");
		}
		return referralFee;
	}
	
	
	@Override
	@CacheEvict(value = "referralFeeCache", allEntries = true)
	public boolean save(ReferralFee entity)   {
	 
		return this.baseMapper.insert(entity)>0?true:false;
	}

	@Override
	@CacheEvict(value = "referralFeeCache", allEntries = true)
	public boolean removeById(Serializable key) {
		return this.baseMapper.deleteById(key)>0?true:false;
	}

	@Override
	@CacheEvict(value = "referralFeeCache", allEntries = true)
	public boolean updateById(ReferralFee entity) {
		return this.baseMapper.updateById(entity)>0?true:false;
	}

	
}
