package com.wimoor.erp.common.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.common.pojo.entity.ProductInPresale;
@Mapper
public interface ProductInPresaleMapper extends BaseMapper<ProductInPresale> {

	List<ProductInPresale> selectDateEvent(@Param("sku") String sku, @Param("marketplaceid") String marketplaceid,
			@Param("groupid") String groupid, @Param("month")String month);
	
	List<ProductInPresale> selectEvent(@Param("sku") String sku, 
			@Param("marketplaceid") String marketplaceid,
			@Param("groupid") String groupid );
	
	List<ProductInPresale> selectAllDayPresale(@Param("sku") String sku, 
			@Param("marketplaceid") String marketplaceid,
			@Param("groupid") String groupid, 
            @Param("beginDate")String beginDate,
            @Param("endDate")String endDate);

	ProductInPresale selectDayEvent(@Param("sku") String sku, 
			@Param("marketplaceid") String marketplaceid,
			@Param("groupid") String groupid,
			@Param("date") String date);
	
	List<ProductInPresale> selectMonthDateEvent(@Param("sku") String sku, @Param("marketplaceid") String marketplaceid,@Param("groupid") String groupid);

	List<Map<String, Object>> findAllByShop(@Param("shopid")String shopid);

	List<Map<String,Object>> selectMonthDateSales(@Param("sku")String sku, 
			                                @Param("marketplaceid")String marketplaceid, 
			                                @Param("groupid")String groupid, 
			                                @Param("beginDate")String beginDate,
			                                @Param("endDate")String endDate);
	
	List<Map<String,Object>> selectDateSales(@Param("sku")String sku, 
            @Param("marketplaceid")String marketplaceid, 
            @Param("groupid")String groupid, 
            @Param("beginDate")String beginDate,
            @Param("endDate")String endDate);

	List<Map<String, Object>> selectHoliday( @Param("marketplaceid")String marketplaceid);
}