package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.entity.ProductInOpt;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 产品信息 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Mapper
public interface ProductInOptMapper extends BaseMapper<ProductInOpt> {
	void updateBySessionRpt(@Param("marketplaceid")String marketplaceid, @Param("amazonauthid")String amazonauthid);
}
