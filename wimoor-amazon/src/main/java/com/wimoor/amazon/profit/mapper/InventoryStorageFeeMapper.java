package com.wimoor.amazon.profit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.InventoryStorageFee;
@Mapper
public interface InventoryStorageFeeMapper  extends BaseMapper<InventoryStorageFee> {

	List<InventoryStorageFee> getInventoryStorageFeeByCountry(String country);

	InventoryStorageFee getPriceByCountry(@Param("country")String country, @Param("month")String month, @Param("isStandard")boolean isStandard);
	
}
