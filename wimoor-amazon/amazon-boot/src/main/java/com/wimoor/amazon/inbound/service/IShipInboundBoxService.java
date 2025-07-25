package com.wimoor.amazon.inbound.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;

public interface IShipInboundBoxService  extends IService<ShipInboundBox>{

	public List<ShipInboundBox> findListByShipmentId(String shipmentid);
	public List<Map<String, Object>> findShipInboundBox(String shipmentid) ;
}
