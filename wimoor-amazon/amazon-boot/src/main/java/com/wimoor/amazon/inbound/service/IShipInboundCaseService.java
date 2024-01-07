package com.wimoor.amazon.inbound.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundCase;

public interface IShipInboundCaseService  extends IService<ShipInboundCase>{

	List<ShipInboundCase> findByShipmentBox(String shipmentid, Integer boxnum);

}
