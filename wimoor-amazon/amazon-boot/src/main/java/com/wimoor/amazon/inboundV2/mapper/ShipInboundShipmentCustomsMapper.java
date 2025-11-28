package com.wimoor.amazon.inboundV2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentCustoms;
@Mapper
public interface ShipInboundShipmentCustomsMapper extends BaseMapper<ShipInboundShipmentCustoms> {
	  @Select({"<script>",
          " SELECT",
          " 	i.*,b.sku ",
          " FROM",
          " 	t_erp_ship_v2_inboundshipment_customs i ",
          " left join	t_erp_ship_v2_inboundshipment_item b on b.id=i.itemid",
          " WHERE b.shipmentid= #{shipmentid} ",
          "</script>"})
	List<ShipInboundShipmentCustoms> listByShipmentid(String shipmentid);


}
