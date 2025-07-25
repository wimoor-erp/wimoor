package com.wimoor.erp.material.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
@Mapper
public interface MaterialConsumableMapper extends BaseMapper<MaterialConsumable>{

	List<MaterialConsumableVO> selectConsumableByMainMmid(@Param("id")String id);
	
	List<MaterialConsumableVO> selectConsumableBySubmid(@Param("id")String id);

	List<Map<String, Object>> findConsumableDetailByShipment(Map<String,Object> map);
     
	List<Map<String, Object>> findConsumableDetailList(Map<String,Object> maps);
	
	List<Map<String, Object>> findConsumableDetailBySkuList(Map<String,Object> maps);
	
	Integer findCanConsumableByInventory(@Param("materialid")String materialid,@Param("warehouseid") String warehouseid,@Param("shopid") String shopid);
	
}