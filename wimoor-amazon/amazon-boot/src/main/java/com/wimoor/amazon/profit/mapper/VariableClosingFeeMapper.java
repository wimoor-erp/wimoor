package com.wimoor.amazon.profit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.VariableClosingFee;
@Mapper
public interface VariableClosingFeeMapper  extends BaseMapper<VariableClosingFee> {
 
	VariableClosingFee findByLogisticsId(@Param("logistics")String logistics, @Param("isMedia")String isMedia);

	VariableClosingFee findByLogisticsAndType(@Param("logistics")String logistics, @Param("typeId")int typeId);

	VariableClosingFee findByTypeId( @Param("country")String country, @Param("typeId")int typeId);
}
