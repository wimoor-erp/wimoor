package com.wimoor.amazon.adv.common.dao;

import java.util.List;

import com.wimoor.amazon.adv.common.pojo.AmzAdvBrand;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvBrandMapper extends BaseMapper<AmzAdvBrand>{
	void insertBatch(List<AmzAdvBrand> list);
}