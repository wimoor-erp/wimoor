package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
@Mapper
public interface PurchaseFormEntryMapper  extends BaseMapper<PurchaseFormEntry> {
 
	Integer findSumaryNeedApply(String shopid);
	
	Integer findSumaryNeedin(String shopid);
	
	Integer findSumaryNeedpay(String shopid);
	
	List<PurchaseFormEntry> findByFormId(String formid);

	List<Map<String, Object>> findRecentPurchase(String materialid);
	
	List<Map<String, Object>> purchaseFormReport(Map<String, Object> para);
	
	List<Map<String, Object>> getFormDetail(@Param("formid") String id, @Param("shopid") String shopid);
	
	Map<String, Object> purchaseFormReportTotal(Map<String, Object> param);

	List<Map<String, Object>> selectNeedSendMsgShop();

	List<Map<String, Object>> selectPurchaseNotify(String shopid);

	Map<String, Object> summaryBySupplier(@Param("shopid")String shopid,@Param("supplier")String supplier);
}