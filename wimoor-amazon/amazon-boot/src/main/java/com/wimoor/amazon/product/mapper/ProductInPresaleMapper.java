package com.wimoor.amazon.product.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.dto.ProductPresaleListDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInPresale;
@Mapper
public interface ProductInPresaleMapper extends BaseMapper<ProductInPresale> {

	List<ProductInPresale> selectDateEvent(@Param("sku") String sku, @Param("marketplaceid") String marketplaceid,
			@Param("groupid") String groupid, @Param("month")String month);
	
	List<ProductInPresale> selectEvent(@Param("sku") String sku, 
			@Param("marketplaceid") String marketplaceid,
			@Param("groupid") String groupid );
	
	List<ProductInPresale>  selectByGroup(@Param("sku") String sku, 
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
	List<Map<String, Object>> selectEstimated();
	void refreshData(AmazonAuthority auth);
	
	List<Map<String,Object>>  listProduct(@Param("param")ProductPresaleListDTO dto);
	IPage<Map<String,Object>>  listProduct(Page<?> page,@Param("param")ProductPresaleListDTO dto);
	void replaceBatch(List<ProductInPresale> preList);
}