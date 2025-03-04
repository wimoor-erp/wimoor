package com.wimoor.amazon.inboundV2.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.product.pojo.dto.ProductInAftersaleDTO;
 
 

@Mapper
public interface ShipInboundItemV2Mapper extends BaseMapper<ShipInboundItem> {
	List<Map<String, Object>> selectByInboundplanid(String inboundplanid);
	IPage<Map<String, Object>> shipmentReportByType(Page<?> page,@Param("param") Map<String, Object> param);
	List<Map<String, Object>> shipmentReportByType(@Param("param") Map<String, Object> param);
	List<Map<String, Object>> shipmentReportByWarhouseCHType(@Param("param") Map<String, Object> param);
	List<ShipInboundItemVo> selectObjByShipmentsid(@Param("list")List<String> list);
	ShipInboundShipmenSummarytVo summaryShipmentItem(String id);

	Map<String, Object> shipmentReportByTypeTotal(Map<String, Object> param);
	
	List<Map<String, Object>> shipArrivalTimeChart(Map<String, Object> param);
	List<Map<String, Object>> getshotTime(@Param("shopid")String shopid,@Param("groupid")String groupid,
			@Param("marketplaceid")String marketplaceid,@Param("sku")String sku);
	Integer summaryShipmentSku(@Param("groupid")String groupid,@Param("marketplaceid")String marketplaceid,@Param("sku")String sku);
	Map<String, Object> findMaterielByShipPlanId(@Param("id")String id,@Param("sku") String sku);
	
	void updateByOrderFee(Map<String, Object> param);
	IPage<Map<String, Object>> getShipinboundItemBatchCondition(Page<?> page, @Param("param")Map<String, Object> param);
	List<Map<String, Object>> getShipinboundItemBatchCondition(@Param("param")Map<String, Object> param);
	Integer refreshOutbound(@Param("shopid")String shopid,@Param("warehouseid")String warehouseid,@Param("msku")String msku);
	public IPage<Map<String, Object>> findShipList(Page<?> page, @Param("param")ProductInAftersaleDTO dto);
	List<ShipInboundItemVo> selectObjByFormid(String formid);


}