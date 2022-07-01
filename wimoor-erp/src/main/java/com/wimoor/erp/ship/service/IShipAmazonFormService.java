package com.wimoor.erp.ship.service;


import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.dto.ShipAmazonShipmentDTO;

public interface IShipAmazonFormService {

	void fulfillableToOutbound(UserInfo user, ShipInboundShipmentDTO shipmentobj);

	String updateDisable(UserInfo user, String shipmentid, boolean isDelAmz, String disableShipment);

	public void updateItemQty(UserInfo user, ShipAmazonShipmentDTO shipmentAmz);

	void marketShipped(UserInfo user, String shipmentid);

}
