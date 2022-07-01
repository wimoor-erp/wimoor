package com.wimoor.erp.material.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.material.mapper.MaterialConsumableMapper;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.service.IMaterialConsumableService;
@Service("materialConsumableService")
public class MaterialConsumableServiceImpl extends ServiceImpl<MaterialConsumableMapper,MaterialConsumable> implements IMaterialConsumableService {

	public List<MaterialConsumable> selectByMainmid(String materialid) {
		QueryWrapper<MaterialConsumable> queryWrapper=new QueryWrapper<MaterialConsumable>();
		queryWrapper.eq("materialid",materialid);
		return this.list(queryWrapper);
	}

	public int deleteByMainmid(String materialid) {
		// TODO Auto-generated method stub
		QueryWrapper<MaterialConsumable> queryWrapper=new QueryWrapper<MaterialConsumable>();
		queryWrapper.eq("materialid",materialid);
		return this.baseMapper.delete(queryWrapper);
	}

	 
}
