package com.wimoor.amazon.auth.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.auth.pojo.entity.WareHouseFBA;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface WareHouseFBAMapper extends BaseMapper<WareHouseFBA>{
	
	WareHouseFBA getDaysByMarketplaceid(@Param("shopid") String shopid,@Param("marketplaceid") String marketplaceid);
	
	List<Map<String,Object>> findByCondition(@Param("shopid")String shopid,@Param("search")String search);
	
	IPage<Map<String,Object>> findBySerch(Page<?> page,@Param("search")String search, @Param("shopid")String shopid);
	
	List<Map<String,Object>> getWarehouseFBA(@Param("shopid")String shopid, @Param("marketplaceid") String marketplaceid);
	
	List<Map<String, Object>> selectAllByShopId(@Param("shopid")String shopid);

	Map<String, Object> selectFbaWareSelfById(String fbawareid);
	
	List<WareHouseFBA> findFbaMarket(@Param("shopid")String shopid,@Param("groupid")String groupid);
	
	List<WareHouseFBA> findFbaWarehouseByShop(@Param("shopid")String shopid);
	 
	Integer saveFBA(@Param("shopid") String shopid, @Param("operator") String operator,@Param("sernum")String sernum);

	Integer deleteFBA(@Param("shopid") String shopid);
}