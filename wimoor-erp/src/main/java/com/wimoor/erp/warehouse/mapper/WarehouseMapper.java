package com.wimoor.erp.warehouse.mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {
	IPage<Warehouse> findByCondition(Page<?> page,@Param("search") String search, @Param("shopid") String shopid,@Param("ftype") String ftype,@Param("parentid")String parentid); 
	
	String selectTypeByName(@Param("fname")String fname);

	List<Warehouse> findByType(@Param("ftype")String ftype, @Param("shopid") String shopid);
	List<Warehouse> findAll(@Param("shopid") String shopid);
	
	
	List<Warehouse> getOverseaWarehouse(@Param("shopid") String shopid,@Param("ftype")String ftype,@Param("groupid") String groupid,@Param("country") String country );
	List<Map<String,Object>> selectFbaSale(@Param("shopid") String shopid,@Param("sku") String sku, @Param("delivery_cycle") String delivery_cycle);
	
	List<Map<String, Object>> selectSelfSale(@Param("shopid") String shopid,@Param("warehouseidList") List<String> warehouseidList,@Param("materialid") String materialid);
	
	Integer saveCheckOut(@Param("wname")String wname,@Param("sernum")String sernum,@Param("parentid")String parentid);
	
	Integer saveQuality(@Param("wname")String wname,@Param("sernum")String sernum,@Param("parentid")String parentid);
	
	Integer saveScrap(@Param("wname")String wname,@Param("sernum")String sernum,@Param("parentid")String parentid);

	LinkedList<Map<String, Object>> selectShipFbaSale(@Param("shopid") String shopid,@Param("sku") String sku,@Param("materialid") String materialid,@Param("planid") String planid,@Param("groupid") String groupid);

	Map<String, Object> getTotalInvAndWorth(String warehouseid);
	
	Warehouse getParentWarehouse(String warehouseid);

	List<Warehouse> findWareDefault(String shopid);
	
	void clearWareDefault(@Param("shopid")String shopid,@Param("ftype") String ftype);
	
	List<Map<String, Object>> selectAllByShopId(@Param("shopid")String shopid);

	String getUUID();
 
}