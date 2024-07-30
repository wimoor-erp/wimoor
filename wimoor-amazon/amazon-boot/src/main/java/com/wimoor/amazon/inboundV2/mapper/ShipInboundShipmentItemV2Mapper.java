package com.wimoor.amazon.inboundV2.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.pojo.vo.SummaryShipmentVo;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentItem;

@Mapper
public interface ShipInboundShipmentItemV2Mapper extends BaseMapper<ShipInboundShipmentItem> {

	ShipInboundShipmenSummarytVo summaryShipmentItem(String shipmentid);
	  @Select({"<script>",
          " SELECT",
          " 	i.* ",
          " FROM",
          " 	t_erp_ship_v2_inboundshipment_boxitem i ",
          " left join	t_erp_ship_v2_inboundshipment_box b on b.id=i.boxid",
          " WHERE b.shipmentid= #{shipmentid} ",
          "</script>"})
	List<Map<String, Object>> selectByShipmentid(String shipmentid);
	  
	SummaryShipmentVo selectitemList( @Param("shipmentid") String shipmentid);
	  
	void updateByInventoryDetail(@Param("authid")String authid);
}
