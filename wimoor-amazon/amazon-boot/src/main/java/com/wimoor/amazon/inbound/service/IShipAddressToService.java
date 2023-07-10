package com.wimoor.amazon.inbound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;

public interface IShipAddressToService extends IService<ShipAddressTo> {

	ShipAddressTo getToAddress(ShipInboundShipment ship);
	
}
