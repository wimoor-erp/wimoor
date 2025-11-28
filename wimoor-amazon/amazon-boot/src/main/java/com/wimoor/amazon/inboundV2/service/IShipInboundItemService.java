package com.wimoor.amazon.inboundV2.service;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.model.fulfillmentinbound.GetShipmentItemsResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundItemReceiveDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.common.user.UserInfo;

 

public interface IShipInboundItemService extends IService<ShipInboundItem> {
	   public int updateItem(ShipInboundItem item);
	   public ShipInboundShipmenSummarytVo summaryShipmentItemWithoutItem(String shipmentid);
	   public ShipInboundItem findItem(String shipmentid, String inboundplanid, String sellersku);
	   public ShipInboundShipmenSummarytVo summaryShipmentItem(String shipmentid) ;
	   public List<Map<String, Object>> getshotTime(String companyid, String groupid, String marketplaceid, String sku);
	   public Integer summaryShipmentSku(String groupid, String marketplaceid, String sku);
	   public Map<String, Object> shipmentReportByLoisticsTotal(Map<String, Object> param);
	   IPage<Map<String, Object>> shipmentReportByLoistics(Page<?> page, Map<String, Object> param);
	   public void setShipmentReportByLoisticsExcelBook(SXSSFWorkbook workbook, Map<String, Object> param,List<String> groupby);
	   public void saveShipmentsFee(ShipInboundShipment shipment,UserInfo user) ;
	  public IPage<Map<String, Object>> getShipinboundItemBatchCondition(Page<?> page, Map<String, Object> param);
	  List<Map<String, Object>> findDetailByShipment(String shipmentid,String sku);
	  public void downloadBatchListList(SXSSFWorkbook workbook, Map<String, Object> param);
	  public void updateFeeByShipment(List<ShipInboundItem> list);
	  public Map<String, List<Map<String, Object>>> shipmentReportByWarhouseCHType(Map<String, Object> param);
	  public Integer refreshOutbound(String shopid,String warehouseid,String msku);
	  public ShipInboundShipmenSummarytVo summaryShipmentItem(List<String> shipmentids);
	  void updateReceiveDate(ShipInboundItemReceiveDTO dto, UserInfo user);
	  public List<ShipInboundItemVo> listByFormid(String formid);

      void handlerItemResult(AmazonAuthority auth, Marketplace market, GetShipmentItemsResponse result,String shipmentid, boolean needshipqty);
}
