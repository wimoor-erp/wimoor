package com.wimoor.amazon.inbound.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
 

public interface IShipInboundItemService extends IService<ShipInboundItem> {
	   public int updateItem(ShipInboundItem item);
	   public ShipInboundItem findItem(String shipmentid, String inboundplanid, String sellersku);
	   public ShipInboundShipmenSummarytVo summaryShipmentItem(String shipmentid) ;
	   public List<ShipInboundItem> getItemByShipment(String shipmentid);
	   public List<ShipInboundItemVo> listByShipmentid(String shipmentid);
	   public List<Map<String, Object>> getshotTime(String companyid, String groupid, String marketplaceid, String sku);
	   public Integer summaryShipmentSku(String groupid, String marketplaceid, String sku);
	   public Map<String, Object> shipmentReportByLoisticsTotal(Map<String, Object> param);
	   IPage<Map<String, Object>> shipmentReportByLoistics(Page<?> page, Map<String, Object> param);
	   public void setShipmentReportByLoisticsExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
}
