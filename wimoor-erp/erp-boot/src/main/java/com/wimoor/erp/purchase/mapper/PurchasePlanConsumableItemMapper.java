package com.wimoor.erp.purchase.mapper;

import com.wimoor.erp.purchase.pojo.entity.PurchasePlanConsumableItem;

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
 * @since 2023-05-08
 */
@Mapper
public interface PurchasePlanConsumableItemMapper extends BaseMapper<PurchasePlanConsumableItem> {
	Map<String, Object> getSummary(@Param("shopid")String shopid,@Param("warehouseid")String warehouseid);
	List<Map<String, Object>> listInfo(@Param("shopid")String shopid,@Param("warehouseid")String warehouseid);
}
