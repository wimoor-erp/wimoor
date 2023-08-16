package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.pojo.dto.PlanDetailDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlanShipItem;

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
 * @since 2022-12-10
 */
@Mapper
public interface AmzProductSalesPlanShipItemMapper extends BaseMapper<AmzProductSalesPlanShipItem> {
   Map<String,Object> getSummary(@Param("shopid")String shopid,@Param("groupid")String groupid,@Param("warehouseid") String warehouseid);
   int updateBatch(@Param("id")String id,@Param("batchnumber")String batchnumber);
   int moveBatch(@Param("shopid")String shopid,@Param("batchnumber")String batchnumber);
   List<Map<String,Object>> getPlanedItem(@Param("dto")PlanDTO dto);
   List<Map<String,Object>> hasplanItem(PlanDetailDTO dto);
   List<Map<String,Object>> hasplanItemEu(PlanDetailDTO dto);
}
