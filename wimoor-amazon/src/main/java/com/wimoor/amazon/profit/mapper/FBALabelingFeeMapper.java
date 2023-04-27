package com.wimoor.amazon.profit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.FBALabelingFee;
@Mapper
public interface FBALabelingFeeMapper  extends BaseMapper<FBALabelingFee> {
	   FBALabelingFee getPriceByProductTierId(@Param("isStandard")Boolean isStandard, @Param("country")String country);
}
