package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
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
public interface ProductInOptMapper extends BaseMapper<ProductInOpt> {
	
	int updateByAdvReport(@Param("marketplaceid")String marketplaceid, @Param("sellerid")String sellerid);
	
	int updateAllOpt();

	void updateBySessionRpt(@Param("marketplaceid")String marketplaceid, @Param("amazonauthid")String amazonauthid);
	
	List<Map<String,Object>> findProductSaleOrder(@Param("param")Map<String, Object> params);

	List<Map<String, Object>> selectMaterialSize(@Param("param")Map<String, Object> param);

	List<Map<String, Object>> findMSKUByShopid(Map<String, Object> param);
	
	ProductInfo findProductInfoByUpload(@Param("sku")String sku,@Param("marketname") String marketname, @Param("shopid")String shopid, @Param("groupname")String groupname);

	List<Map<String,Object>> findAdvert(Map<String,Object> paramter);
	
	List<Map<String, Object>> getMonthSumNum(Map<String,Object> param);
	
	
}
