package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
@Mapper
public interface PurchasePlanItemMapper   extends BaseMapper<PurchasePlanItem> {
	Map<String, Object> getSummary(@Param("shopid")String shopid,@Param("planid")String planid);
	List<Map<String, Object>> listItem(@Param("shopid")String shopid,@Param("planid")String planid);
	List<Map<String, Object>> listConfirmInfo(Map<String,Object> param);
	List<Map<String, Object>> planhis(@Param("shopid")String shopid,@Param("warehouseid")String warehouseid);
	void moveBatch(@Param("shopid")String shopid,@Param("batchnumber")String batchnumber);
	List<Map<String, Object>> listBatchInfo(@Param("shopid")String shopid,@Param("planid")String planid,@Param("batchnumber") String batchnumber);
}