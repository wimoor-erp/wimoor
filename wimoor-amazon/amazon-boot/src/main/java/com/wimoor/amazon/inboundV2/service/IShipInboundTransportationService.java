package com.wimoor.amazon.inboundV2.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazon.spapi.model.fulfillmentinboundV20240320.ListTransportationOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.TransportationSelection;
import com.wimoor.amazon.inbound.pojo.dto.ShipTransDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inboundV2.pojo.dto.TransportationDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentCustoms;
import com.wimoor.common.user.UserInfo;
import org.springframework.transaction.annotation.Transactional;

public interface IShipInboundTransportationService {

	ShipInboundOperation generateTransportationOptions(ShipInboundPlan inplan);
	ListTransportationOptionsResponse listTransportationOptions(TransportationDTO dto);
	ShipInboundOperation confirmTransportationOptions(String formid,List<TransportationDTO> transportationSelections);
	void saveSelfTransData(UserInfo user, ShipInboundTrans ship, ShipInboundShipment shipment,Date shipdate);
    List<Map<String, Object>> getSelfTransHis(String companyid, String shipmentid);
	List<ShipInboundShipmentCustoms> getCustoms(String shipmentid);
	void saveCustoms(UserInfo user, List<ShipInboundShipmentCustoms> list);
	 
}
