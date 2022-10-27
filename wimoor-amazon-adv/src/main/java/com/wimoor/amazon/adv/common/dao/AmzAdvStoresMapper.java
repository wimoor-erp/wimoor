package com.wimoor.amazon.adv.common.dao;

import java.util.List;

import com.wimoor.amazon.adv.common.pojo.AmzAdvStores;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvStoresMapper extends BaseMapper<AmzAdvStores>{
	
	void insertBatch(List<AmzAdvStores> list);
}