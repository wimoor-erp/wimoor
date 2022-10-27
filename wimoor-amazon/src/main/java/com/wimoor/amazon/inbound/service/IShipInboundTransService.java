package com.wimoor.amazon.inbound.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;

public interface IShipInboundTransService extends IService<ShipInboundTrans> {

	Map<String, Object> shipmentTransFee(String shopId, String marketplaceid, String groupid, String sku);

}