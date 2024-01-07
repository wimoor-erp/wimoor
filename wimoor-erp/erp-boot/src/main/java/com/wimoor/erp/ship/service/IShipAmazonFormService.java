package com.wimoor.erp.ship.service;


import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.dto.ShipAmazonShipmentDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmentDTO;

public interface IShipAmazonFormService {

	void fulfillableToOutbound(UserInfo user, ShipInboundShipmentDTO shipmentobj);

	String updateDisable(UserInfo user, String shipmentid, boolean isDelAmz, String disableShipment);

	public void updateItemQty(UserInfo user, ShipAmazonShipmentDTO shipmentAmz);

	void setPDFDocShipFormDetail(PdfWriter writer,UserInfo user, Document document, ShipInboundShipmenSummarytVo data);

	void setPDFDocShipForm(PdfWriter writer,UserInfo user, Document document, ShipInboundShipmenSummarytVo data);
	public void handleInventoryAction(UserInfo user, ShipInboundShipmentDTO shipmentobj) ;
	
	
	public List<Map<String, Object>> findPlanSubDetail(String planid,String marketplaceid,String warehouseid);
	
	public List<Map<String, Object>> findPlanEuSubDetail(String planid, String marketplaceid, String warehouseid);
	public void addFullfillableToOutboundChange(String userid, ShipInboundShipmentDTO ship, String material, Integer amount);
	public void subFulfillableToOutboundChange(String userid, ShipInboundShipmentDTO ship, String material, Integer amount);
}
