package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.entity.ProductInAftersale;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Mapper
public interface ProductInAftersaleMapper extends BaseMapper<ProductInAftersale> {

	void insertBatch(List<ProductInAftersale> salesafter);
}
