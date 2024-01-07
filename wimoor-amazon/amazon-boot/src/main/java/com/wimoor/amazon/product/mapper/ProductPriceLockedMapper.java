package com.wimoor.amazon.product.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.ProductPriceLocked;

@Mapper
public interface ProductPriceLockedMapper extends BaseMapper<ProductPriceLocked>{
    
}