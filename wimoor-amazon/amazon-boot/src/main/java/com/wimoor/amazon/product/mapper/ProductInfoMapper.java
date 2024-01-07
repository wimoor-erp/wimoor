package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.dto.ProductListDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.pojo.vo.AmzProductListVo;
import com.wimoor.amazon.product.pojo.vo.ProductInfoListVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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

	List<ProductInfo> selectBySku(@Param("sku")String sku, @Param("marketplaceid")String marketplaceid, @Param("amazonAuthId")String amazonAuthId);
	
	List<ProductInfo> selectByMSku(@Param("msku")String sku, @Param("marketplaceid")String marketplaceid, @Param("groupid")String groupid, @Param("shopid")String shopid);
	
	List<Map<String, Object>> findShopSku(@Param("shopid")String shopid, @Param("sku")String sku);
	
	List<Map<String,Object>> selectByAuth(@Param("amazonAuthId")String amazonAuthId);
	
	List<Map<String,Object>> selectByMapByParams(@Param("params")Map<String,Object> params);
	
	String getShopidByPid(@Param("pid")String pid);

	IPage<AmzProductListVo> selectDetialByAuth(Page<?> page,@Param("param") Map<String, Object> param);
	
	IPage<ProductInfoListVo> findByCondition(Page<?> page,@Param("param") ProductListDTO param);

    Map<String, Object> findNameAndPicture(@Param("sku") String sku_p, @Param("marketplaceid") String marketplaceid, @Param("groupid") String groupid);
    
    String findMSKUBySKUMarket(@Param("sku") String sku,@Param("marketplaceid") String marketplaceid,@Param("amazonAuthId") String amazonAuthId);
    
    List<Map<String, Object>> findAllByCondition(@Param("param")Map<String, Object> map);
    
	void clearInSnl(@Param("marketplaceid")String marketplaceid, @Param("amazonAuthId")String amazonAuthId);
	
	IPage<Map<String,Object>> getProductInfoWithFnSKU(Page<?> page,@Param("param") Map<String, Object> param);

	IPage<Map<String, Object>> getAsinList(Page<?> page, @Param("param") Map<String, Object> parameter);

	List<Map<String, Object>> getInfoSimple(Map<String, Object> param);
}
