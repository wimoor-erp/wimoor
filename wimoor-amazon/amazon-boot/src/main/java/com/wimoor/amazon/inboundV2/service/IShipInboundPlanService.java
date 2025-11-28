package com.wimoor.amazon.inboundV2.service;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.model.fulfillmentinboundV20240320.InboundPlan;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.InboundPlansDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inboundV2.pojo.vo.SummaryPlanVo;
import com.wimoor.common.user.UserInfo;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public interface IShipInboundPlanService extends IService<ShipInboundPlan>, IRunAmazonService {

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

	InboundPlansDTO getPlanListSync(InboundPlansDTO dto, UserInfo user);

	Boolean donePacking(UserInfo user, String formid, String type);

    void syncData(ShipInboundPlan plan, UserInfo user);

	ShipInboundPlan refreshPlan(ShipInboundPlan dto, UserInfo user);

	ShipInboundPlan detailPlan(ShipInboundPlan dto, UserInfo user);

	void savePlanItemSync(InboundPlan planResult, ShipPlanListDTO dto, UserInfo user, AmazonAuthority auth);

	Map<String,Object> findNum(String shopid);

    IPage<Map<String, Object>> getShipmentDetailReport(Page<Object> page, Map<String, Object> param);

	IPage<Map<String, Object>> getShipmentReport(Page<Object> page, Map<String, Object> param);

	void setExcelBookByType(SXSSFWorkbook workbook, Map<String, Object> params);
}
