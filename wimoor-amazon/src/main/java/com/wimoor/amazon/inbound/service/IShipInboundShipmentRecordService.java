package com.wimoor.amazon.inbound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipmentRecord;
import com.wimoor.common.user.UserInfo;

public interface IShipInboundShipmentRecordService  extends IService<ShipInboundShipmentRecord> {
	void saveRecord(ShipInboundShipment shipInboundShipment);
}
