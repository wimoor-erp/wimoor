package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.entity.ProductInfo;

import java.util.List;
import java.util.Map;

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
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

	List<ProductInfo> selectBySku(String sku, String marketplaceid, String id);

	List<Map<String, Object>> findShopSku(@Param("shopid")String shopid, @Param("sku")String sku);
	
	List<Map<String,Object>> selectByAuth(@Param("amazonAuthId")String amazonAuthId);
	
	Map<String,Object> selectByMapByParams(@Param("params")Map<String,Object> params);
	
	String getShopidByPid(@Param("pid")String pid);

}
