package com.wimoor.amazon.inboundV2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentCustom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShipInboundShipmentCustomMapper extends BaseMapper<ShipInboundShipmentCustom> {
	  @Select({"<script>",
          " SELECT",
          " 	i.*,b.sku ",
          " FROM",
          " 	t_erp_ship_v2_inboundshipment_custom i ",
          " left join	t_erp_ship_v2_inboundshipment_item b on b.id=i.itemid",
          " WHERE b.shipmentid= #{shipmentid} ",
          "</script>"})
	List<ShipInboundShipmentCustom> listByShipmentid(String shipmentid);


}
