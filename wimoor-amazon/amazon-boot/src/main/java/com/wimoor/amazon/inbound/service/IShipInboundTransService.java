package com.wimoor.amazon.inbound.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;

public interface IShipInboundTransService extends IService<ShipInboundTrans> {

	Map<String, Object> shipmentTransFee(String shopId, String marketplaceid, String groupid, String sku);

	IPage<Map<String, Object>> getShipmentFeeReport(Page<?> page, Map<String, Object> param);

	IPage<Map<String, Object>> transSKUFeeShared(Page<?> page, Map<String, Object> param);

	void setShipmentFeeReport(SXSSFWorkbook workbook, Map<String, Object> params);

	IPage<Map<String, Object>> getShipmentFeeDetailReport(Page<?> page, Map<String, Object> param);

	void setShipmentFeeDetailReport(SXSSFWorkbook workbook, Map<String, Object> params);
	
	public  List<Map<String, Object>> getShipmentFeeDetailReport(Map<String, Object> param );

	List<Map<String, Object>> calculateTransFeeSharedDetail(Map<String, Object> param);

}