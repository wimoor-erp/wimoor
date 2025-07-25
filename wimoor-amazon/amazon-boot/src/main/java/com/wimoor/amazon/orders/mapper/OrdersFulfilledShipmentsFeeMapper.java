package com.wimoor.amazon.orders.mapper;

import com.wimoor.amazon.orders.pojo.entity.OrdersFulfilledShipmentsFee;

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
 * @since 2023-05-22
 */
@Mapper
public interface OrdersFulfilledShipmentsFeeMapper extends BaseMapper<OrdersFulfilledShipmentsFee> {
	Integer exists(Map<String, Object> param);

	void delete(Map<String, Object> param);
	
	List<Map<String, Object>> findDetailByShipment(@Param("shipmentid") String shipmentid,@Param("sku") String sku);
	
	void updateFeeByShipment(@Param("shipmentid") String shipmentid,@Param("sku") String sku);
	
	List<Map<String,Object>> findSku_InOutShipment(Map<String,Object> param);
}
