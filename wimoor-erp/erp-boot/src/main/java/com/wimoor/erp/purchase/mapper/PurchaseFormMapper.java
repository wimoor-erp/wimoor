package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface PurchaseFormMapper extends BaseMapper<PurchaseForm> {

	IPage<Map<String, Object>> selectByCondition(Page<?> page,@Param("param") Map<String, Object> param);

	List<Map<String, Object>> selectByCondition(@Param("param")Map<String, Object> param);
	
	List<Map<String, Object>> purchaseSumReport(Map<String, Object> param);

	List<Map<String, Object>> findeLastByMaterialid(Page<?> page,@Param("materialid") String materialid);

	Map<String, Object> selectSummarytByCondition(Map<String, Object> param);

	List<Map<String, Object>> purchaseRecSumReport(Map<String, Object> param);

	IPage<Map<String, Object>> selectByConditionForm(Page<?> page,Map<String, Object> param);
	
	List<Map<String, Object>> selectByConditionForm(@Param("param")Map<String, Object> param);

	Map<String, Object> getLastPurchaseRecord(@Param("shopid")String shopid, @Param("warehouseid")String warehouseid);

	List<Map<String, Object>> getPurchaseSumReportNew(Map<String, Object> param);

	IPage<Map<String, Object>> getPayRecSumReport(Page<?> page,@Param("param")Map<String, Object> param);
	
	Map<String, Object>  getPayRecSumTotal(Map<String, Object> param);

	List<Map<String, Object>> getPayRecSumReport(@Param("param")Map<String, Object> param);
	
	List<Map<String,Object>> findEntryByIdAndSupplier(@Param("formid")String formid,@Param("supplier") String supplierid);
	
	List<Map<String, Object>> getPurchaseEntryStatus(@Param("shopid")String shopid, @Param("warehouseid")String warehouseid);

	List<Map<String, Object>> getEnteyInfo(Map<String, Object> param);
	
	List<Map<String,Object>> findSupplierByForm(@Param("formid")String formid);

	Map<String, Object> selectPurchaseNumAllStatus(@Param("param")Map<String, Object> param);

	List<Map<String, Object>> findeLastsByMaterialids(@Param("list")List<String> ids);
	 
}