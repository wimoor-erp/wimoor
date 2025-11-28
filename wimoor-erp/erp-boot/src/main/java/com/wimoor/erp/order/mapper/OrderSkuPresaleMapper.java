package com.wimoor.erp.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.order.pojo.dto.PresaleListDTO;
import com.wimoor.erp.order.pojo.entity.OrderSkuPresale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-03-16
 */
@Mapper
public interface OrderSkuPresaleMapper extends BaseMapper<OrderSkuPresale> {
    List<OrderSkuPresale> selectDateEvent(@Param("sku") String sku, @Param("warehouseid") String warehouseid, @Param("month")String month);

    List<OrderSkuPresale> selectEvent(@Param("sku") String sku, @Param("warehouseid") String warehouseid );

    List<OrderSkuPresale>  selectByGroup(@Param("sku") String sku, @Param("warehouseid") String warehouseid );

    List<OrderSkuPresale> selectAllDayPresale(@Param("sku") String sku,
                                               @Param("warehouseid") String warehouseid,
                                               @Param("beginDate")String beginDate,
                                               @Param("endDate")String endDate);

    OrderSkuPresale selectDayEvent(@Param("sku") String sku,
                                    @Param("warehouseid") String warehouseid,
                                    @Param("date") String date);

    List<OrderSkuPresale> selectMonthDateEvent(@Param("sku") String sku, @Param("warehouseid") String warehouseid);

    List<Map<String, Object>> findAllByShop(@Param("shopid")String shopid);

    List<Map<String,Object>> selectMonthDateSales(@Param("sku")String sku,
                                                  @Param("warehouseid")String warehouseid,
                                                  @Param("beginDate")String beginDate,
                                                  @Param("endDate")String endDate);

    List<Map<String,Object>> selectDateSales(@Param("sku")String sku,
                                             @Param("warehouseid")String warehouseid,
                                             @Param("beginDate")String beginDate,
                                             @Param("endDate")String endDate);

    List<Map<String, Object>> selectHoliday( @Param("country")String country);
    List<Map<String, Object>> selectEstimated();
    void refreshData(@Param("warehouseid")String warehouseid );

    List<Map<String,Object>>  listProduct(@Param("param") PresaleListDTO dto);
    IPage<Map<String,Object>>  listProduct(Page<?> page,@Param("param")PresaleListDTO dto);
    void replaceBatch(List<OrderSkuPresale> preList);
}


