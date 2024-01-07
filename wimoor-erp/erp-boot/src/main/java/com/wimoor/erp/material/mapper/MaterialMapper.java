package com.wimoor.erp.material.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.material.pojo.dto.PlanDTO;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface MaterialMapper extends BaseMapper<Material> {

	IPage<Map<String, Object>> findMaterial(Page<?> page,@Param("param")Map<String,Object> map);
	
	IPage<Map<String, Object>> findConsumable(Page<?> page,@Param("param")Map<String,Object> map);

	List<Map<String, Object>> findConsumable(@Param("param")Map<String,Object> map);
	
	IPage<Map<String, Object>> findPackage(Page<?> page,@Param("param") Map<String, Object> params);
	
	List<Map<String, Object>> findPackage(@Param("param") Map<String, Object> params);
	
	List<Map<String, Object>> findMaterial(@Param("param")Map<String,Object> map);

	MaterialVO findMaterialById(String id);

	List<Material> selectAllSKUForSelect(@Param("sku")String sku, @Param("shopid")String shopid);
	
	Map<String,Object> findDimAndAsinBymid(@Param("sku")String sku,@Param("shopid")String shopid,@Param("marketplaceid")String marketplaceid,@Param("groupid")String groupid);
	
	List<Map<String, Object>> getForSum(@Param("shopid") String shopid,@Param("groupid") String groupid);

	List<Map<String, Object>> selectAllSKUForLabel(@Param("sku")String sku, @Param("shopid")String shopid);

	Map<String, BigDecimal> findDimensionsInfoBySKU(@Param("sku")String sku, @Param("shopid")String shopid);
	
	List<Map<String, Object>> findMaterialMapBySku(@Param("param")Map<String,Object> map);
	
	List<Map<String,Object>> getOwnerList(String shopid);
	
	Integer getCountbyShopId(@Param("shopid")String shopid);
	
	List<String> findMarterialForColorOwner(Map<String,Object> param);
	
	List<Map<String, Object>> findSKUImageForProduct(@Param("maps")Map<String, Object> param);

	List<Map<String, Object>> selectAllMaterialByShop(Map<String, Object> parammap);

	List<Map<String, Object>> selectCommonImage();

	List<Material> selectByImage(@Param("image")String image);
	
	Map<String, Object> getRealityPrice(@Param("materialid")String materialid);

	List<Map<String, Object>> findAllByCondition(Map<String, Object> map);

	List<Map<String, Object>> selectProPriceHisById(String string);

	List<Map<String, Object>> findInventoryByMsku(PlanDTO dto);

	Material getMaterailBySku(@Param("shopid")String shopid, @Param("sku")String sku);
	
	List<MaterialVO> getMaterialInfoBySkuList(PlanDTO dto);

	void syncProductList(@Param("shopid")String shopid);

}