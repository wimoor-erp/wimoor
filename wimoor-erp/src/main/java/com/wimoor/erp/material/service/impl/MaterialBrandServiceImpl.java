package com.wimoor.erp.material.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.material.mapper.MaterialBrandMapper;
import com.wimoor.erp.material.pojo.entity.MaterialBrand;
import com.wimoor.erp.material.service.IMaterialBrandService;
import lombok.RequiredArgsConstructor;

@Service("materialBrandService")
@RequiredArgsConstructor
public class MaterialBrandServiceImpl extends  ServiceImpl<MaterialBrandMapper,MaterialBrand> implements IMaterialBrandService{
 
	
	public List<MaterialBrand> findAllBrand(String shopid) {
		QueryWrapper<MaterialBrand> queryWrapper=new QueryWrapper<MaterialBrand>();
		queryWrapper.eq("shopid",shopid);
		return  this.list(queryWrapper);
	}

	public IPage<MaterialBrand> findByCondition(Page<?> page,String shopid, String search) {
		if(GeneralUtil.isEmpty(search))search=null;
		else search=search.trim()+"%";
		return this.baseMapper.findByCondition(page,shopid, search);
	}
	
}
