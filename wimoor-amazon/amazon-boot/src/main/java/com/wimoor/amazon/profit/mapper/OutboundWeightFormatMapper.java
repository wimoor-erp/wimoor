package com.wimoor.amazon.profit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.OutboundWeightFormat;
@Mapper
public interface OutboundWeightFormatMapper extends BaseMapper<OutboundWeightFormat> {
	   OutboundWeightFormat findByProductTierId(@Param("productTierId")String productTierId, @Param("isMedia")boolean isMedia);
}
