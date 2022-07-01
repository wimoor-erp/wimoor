package com.wimoor.amazon.inbound.service;

 
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.dto.ShipCartShipmentDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.common.user.UserInfo;
 
 

public interface IShipInboundShipmentService extends IService<ShipInboundShipment>{

	IPage<ShipInboundShipmenSummarytVo> findByTraceCondition(Page<Object> page, Map<String, Object> param);
	public String createInboundShipment(ShipInboundShipment shipment) ;
	String updateInboundShipment(ShipInboundShipment shipment);
	String saveCartShipment(UserInfo user, ShipCartShipmentDTO dto);
	public String getPkgLabelUrl(String shipmentid,String pagetype);
 
}
