package com.wimoor.amazon.product.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.ProductTypes;

@Mapper
public interface ProductTypeMapper extends BaseMapper<ProductTypes>{

}
