package com.wimoor.amazon.inboundV2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentRecord;

public interface IShipInboundShipmentRecordV2Service  extends IService<ShipInboundShipmentRecord> {
	void saveRecord(ShipInboundShipment shipInboundShipment);
	void saveRecord(ShipInboundPlan plan);
}
