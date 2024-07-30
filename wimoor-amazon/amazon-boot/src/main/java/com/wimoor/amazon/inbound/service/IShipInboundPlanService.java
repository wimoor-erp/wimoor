package com.wimoor.amazon.inbound.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.common.pojo.vo.ChartLine;
import com.wimoor.amazon.inbound.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inbound.pojo.vo.SummaryShipmentVo;
import com.wimoor.common.user.UserInfo;

public interface IShipInboundPlanService extends IService<ShipInboundPlan> {

	void saveShipInboundPlan(ShipInboundPlan inplan);

	public ShipInboundShipment createShipment(AmazonAuthority auth, Marketplace market, InboundShipmentInfo item,String warehouseid,Integer skunum);

	ShipInboundShipment convertShipmentPojo(AmazonAuthority auth, Marketplace market, InboundShipmentInfo item, String warehouseid, Integer skunum);

	List<SummaryShipmentVo> showPlanListByPlanid(String planid, String shipmentid);

	IPage<ShipPlanVo> getPlanList(ShipPlanListDTO dto,UserInfo user);

	ShipPlanVo getPlanBaseInfo(String inboundplanid, UserInfo user);

	Map<String,Object> uploadShipListByExcel(Sheet sheet, Map<String, Object> map);
	
	public ChartLine shipArrivalTimeChart(String groupid,String amazonAuthId, String sku,String marketplaceid, int daysize,UserInfo userinfo);

	List<Map<String, Object>> getShipRecordByMarket(String marketplaceid, String 	groupid);
	
	public List<Map<String, Object>> getShipRecord(String shopid,String groupid, String marketplaceid, String sku);

	List<Map<String, Object>> getShipBadRecord(String companyid, String marketplaceid, String sku);

	IPage<Map<String, Object>> getShipmentReport(Page<?> page, Map<String, Object> param);

	void setExcelBookByType(SXSSFWorkbook workbook, Map<String, Object> params);

	IPage<Map<String, Object>> getShipmentDetailReport(Page<Object> page, Map<String, Object> param);
	
}
