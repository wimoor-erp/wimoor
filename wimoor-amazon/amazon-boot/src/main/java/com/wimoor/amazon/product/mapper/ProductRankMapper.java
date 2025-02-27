package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.entity.ProductRank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-07-21
 */
@Mapper
public interface ProductRankMapper extends BaseMapper<ProductRank> {

    int insert(ProductRank record);

    List<ProductRank> selectBySku(Map<String,Object> param);
    
    int updateNewestByProduct(String productid);
    
    ProductRank findRankByPid(String pid);
    
    List<Map<String, Object>> selectCountRankBySku(Map<String,Object> param);
    
    List<Map<String, Object>> selectRankBySku(Map<String,Object> param);
    
    List<Map<String, Object>> findRankByCategoryId(Map<String,Object> param);
    
	List<String> selectBeforeProductRank(@Param("amazonauthid")String amazonauthid, @Param("marketplaceid")String marketplaceid);

	List<Map<String, Object>> findProductRank(@Param("id")String id);
}
