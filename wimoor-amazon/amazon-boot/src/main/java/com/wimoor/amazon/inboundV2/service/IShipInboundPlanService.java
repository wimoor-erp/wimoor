package com.wimoor.amazon.inboundV2.service;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.model.fulfillmentinboundV20240320.ListInboundPlansResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.InboundPlansDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inboundV2.pojo.vo.SummaryPlanVo;
import com.wimoor.common.user.UserInfo;

public interface IShipInboundPlanService extends IService<ShipInboundPlan> {

	void saveShipInboundPlan(ShipInboundPlan inplan);
	
	public IPage<ShipPlanVo> getPlanList(ShipPlanListDTO dto,UserInfo user);
	
	public ShipPlanVo getPlanBaseInfo(String formid, UserInfo user);
	
	ShipInboundOperation cancelInboundPlan(ShipInboundPlan inplan);
	
	public SummaryPlanVo showPlanListByPlanid(String planid); 
	
	Map<String, Object> listInboundPlanPallets(String planid);

	void getDeliveryChallanDocument(String planid, String shipmentId);

	String generateSelfShipAppointmentSlots(String planid, String shipmentId, String startDate, String endDate);

	void changeShipInboundPlan(ShipInboundPlan inplan, List<ShipInboundItem> list);

	ShipInboundOperation confirmInboundPlan(ShipInboundPlan plan);

	ShipInboundPlan changeAddress(UserInfo user, String formid,String addressid);

	ShipInboundPlan changeShipmentAddress(UserInfo user, String shipmentid, String addressid);

	ListInboundPlansResponse getPlanListSync(InboundPlansDTO dto, UserInfo user);

	Boolean donePacking(UserInfo user, String formid, String type);
	


}
