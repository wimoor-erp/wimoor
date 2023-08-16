package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.entity.ProductInOrder;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 产品信息的订单销售数据 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Mapper
public interface ProductInOrderMapper extends BaseMapper<ProductInOrder> {
	int insertBatch(List<ProductInOrder> list);
	
	Map<String,Object> selectProductOrderWithProfitByPid(@Param("pid")String pid);
	
	Integer getProductOrderSales(Map<String,Object> param);
	
	List<Map<String,Object>> getProductCountrySales(Map<String,Object> param);
	
	List<Map<String,Object>> getProductEUSales(Map<String,Object> param);
	
	Map<String, Object> selectDetialById(@Param("pid")String pid,@Param("shopid")String shopid);
}
