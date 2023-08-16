package com.wimoor.amazon.profit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
@Mapper
public interface ProfitConfigCountryMapper  extends BaseMapper<ProfitConfigCountry> {
	List<ProfitConfigCountry> findByProfitId(String id);
}
