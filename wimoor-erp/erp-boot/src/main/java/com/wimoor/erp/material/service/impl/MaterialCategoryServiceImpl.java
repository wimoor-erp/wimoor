package com.wimoor.erp.material.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.material.mapper.MaterialCategoryMapper;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.wimoor.erp.material.service.IMaterialCategoryService;

import lombok.RequiredArgsConstructor;

@Service("materialCategoryService")
@RequiredArgsConstructor
public class MaterialCategoryServiceImpl extends  ServiceImpl<MaterialCategoryMapper,MaterialCategory> implements IMaterialCategoryService{
 
	
	public List<MaterialCategory> findAllCategory(String shopid) {
		QueryWrapper<MaterialCategory> queryWrapper=new QueryWrapper<MaterialCategory>();
		queryWrapper.eq("shopid",shopid);
		return  this.list(queryWrapper);
	}

	public IPage<MaterialCategory> findByCondition(Page<?> page,String shopid, String search) {
		if(GeneralUtil.isEmpty(search))search=null;
		else search=search.trim()+"%";
		return this.baseMapper.findByCondition(page,shopid, search);
	}

	@Override
	public List<MaterialCategory> findCategory(String shopid, String type) {
		// TODO Auto-generated method stub
		if("product".equals(type)) {
			type="0";
		}else if("consumable".equals(type)) {
			type="1";
		}else if("package".equals(type)) {
			type="2";
		}else {
			type="3";
		}
		return this.baseMapper.findCategory(shopid, type);
	}
	
}
