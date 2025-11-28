package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.FBASippMapper;
import com.wimoor.amazon.profit.pojo.entity.FBASipp;
import com.wimoor.amazon.profit.service.IFBASippService;

import lombok.RequiredArgsConstructor;
@Service("fBASippService")
@RequiredArgsConstructor
public class FBASippServiceImpl extends ServiceImpl<FBASippMapper, FBASipp> implements IFBASippService {
	
	@Cacheable(value = "fbaSippCache")
	public FBASipp findByProductTierIdNew(String productTierId, boolean isClothing,String country,BigDecimal outboundWeight) {
		if(!country.contains("%")) {
			country="%"+country+"%";
		}
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("productTierId", productTierId);
		param.put("isClothing", isClothing);
		param.put("country", country);
		param.put("weight", outboundWeight);
		return this.baseMapper.findFormat( param);
	}
	
	@Override
	@CacheEvict(value = "fbaSippCache", allEntries = true)
	public boolean save(FBASipp entity)  {
		return this.baseMapper.insert(entity)>0;
	}

	@Override
	@CacheEvict(value = "fbaSippCache", allEntries = true)
	public boolean removeById(Serializable key) {
		return this.baseMapper.deleteById(key)>0;
	}

	@Override
	@CacheEvict(value = "fbaSippCache", allEntries = true)
	public boolean updateById(FBASipp entity)  {
		return this.baseMapper.updateById(entity)>0;
	}
}
