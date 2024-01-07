package com.wimoor.amazon.profit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.InplaceFeeFormat;
@Mapper
public interface InplaceFeeFormatMapper  extends BaseMapper<InplaceFeeFormat> {
	
	InplaceFeeFormat findByInvplaceFeeId(@Param("invplaceFee")String invplaceFee, @Param("isStandard")boolean isStandard, @Param("country")String country);
	
	InplaceFeeFormat findByProductTierId(String productTierId);
}
