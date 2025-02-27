package com.wimoor.erp.ship.service;


import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.dto.ShipFormDTO;

import java.util.Map;

public interface IShipFormService {

	void fulfillableToOutbound(UserInfo user, ShipFormDTO dto);
	void fulfillableOut(UserInfo user,ShipFormDTO dto) ;
	String updateDisable(UserInfo user, ShipFormDTO dto);
	void updateItemQty(UserInfo user, ShipFormDTO dto) ;
	void subOutbound(UserInfo user, ShipFormDTO dto);
}
