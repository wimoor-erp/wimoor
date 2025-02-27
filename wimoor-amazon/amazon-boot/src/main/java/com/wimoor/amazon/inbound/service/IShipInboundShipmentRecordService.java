package com.wimoor.amazon.inbound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipmentRecord;

public interface IShipInboundShipmentRecordService  extends IService<ShipInboundShipmentRecord> {
	void saveRecord(ShipInboundShipment shipInboundShipment);
}
