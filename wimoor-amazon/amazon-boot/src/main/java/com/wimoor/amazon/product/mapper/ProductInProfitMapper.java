package com.wimoor.amazon.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInProfit;

@Mapper
public interface ProductInProfitMapper extends BaseMapper<ProductInProfit>{
	int insertBatch(List<ProductInProfit> list);
	
	int deleteProductInOrderAndProfitByDate();
}