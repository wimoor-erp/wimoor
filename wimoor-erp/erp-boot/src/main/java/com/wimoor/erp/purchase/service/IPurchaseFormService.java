package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.dto.PurchaseSaveDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;

public interface IPurchaseFormService extends IService<PurchaseForm> {

	IPage<Map<String, Object>> getPurchaseFormEntry(Page<?> page,  Map<String, Object> param );
	public Map<String, Object> savePurchaseDataAction(PurchaseSaveDTO dto) throws ERPBizException ;

	boolean updatePurchaseFormEntry(PurchaseFormEntry item, String warehouseid) throws ERPBizException;

	PurchaseFormEntry updatePrice(UserInfo user,String id, Float itemprice, Integer amount,Float orderprice) throws ERPBizException;
    
    int approvals(UserInfo user,String ids) throws ERPBizException;

	Map<String, Object> getDetailMap(String id,String shopid) throws ERPBizException;
 
	Map<String, Object> getTraceDetailMap(String id, String shopid,String ftype,String paytype);


	
    int findSumaryNeedApply(String shopid);
    
	int findSumaryNeedin(String shopid);
	
	int findSumaryNeedpay(String shopid);
    
	void purchaseReturn(UserInfo user, String id,  String remark) throws ERPBizException;

	Map<String, Object> updateNotice(String id, String notice, String shopid, String userid) throws ERPBizException;

	boolean updateWarehouse(UserInfo user, String id, String warehouseid) throws ERPBizException;

	List<Map<String, Object>> getLastFormByMaterial(Object id,int i);
	
	Map<String, Object> getLastOneFormByMaterial(Object id);

	List<Map<String,Object>> getPurchaseRecSumReport(  Map<String, Object> param);

	List<Map<String, Object>> purchaseFormReport(Map<String, Object> param);
	public IPage<Map<String, Object>> purchaseFormReport(Page<?>page,Map<String, Object> param) ;
	List<Map<String,Object>> getPurchaseSumReport(Map<String,Object> param);
	
	

	
	public Map<String,Object> getViewData(String id, String shopid);

	Map<String, Object> getPurchaseFormSummary(UserInfo user, Map<String, Object> param);

	IPage<Map<String, Object>> getPurchaseForm(Page<?> page,Map<String, Object> param);

	List<Map<String, Object>> getFormDetail(String id, String shopid);


	Map<String, Object> purchaseFormReportTotal(Map<String, Object> param);

	Map<String, Object> getLastPurchaseRecord(String shopid, String warehouseid);

	List<Map<String, Object>> getPurchaseSumReportNew(Map<String, Object> param);

	IPage<Map<String, Object>> getPayRecSumReport(Page<?> page,Map<String, Object> param);

	void setExcelBook(Workbook workbook, Map<String, Object> param);
	
	void setPurchaseSkuItemExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
	
	public List<Map<String, Object>> loadPurchaseFormDate(UserInfo user, String planid, String warehouseid, String ftype, List<String> item_material_list);

	void getPurchaseRecInfoExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);

	List<Map<String, Object>> findSupplierByForm(String formid);
	
	void downloadPurchasePaymentWord(HttpServletRequest request, HttpServletResponse response, String recid);
	
	Map<String, Object> getPurchaseNumAllStatus(Map<String, Object> param);
	
	Map<String, Object> uploadPurchaseListByExcel(Sheet sheet, Map<String, Object> map);
	
	Map<String, Object> getPurchaseRecordInfo(String reciveId);
	void setPurchaseFormReportExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
	public boolean updateDeliveryDate(String entryid,String deliverydate) ;
	List<Map<String, Object>> selectNeedSendMsgShop();
	List<Map<String, Object>> selectPurchaseNotify(String shopid);
	public int savePurchaseForm(UserInfo user,List<PurchaseForm> formList, String planwarehouseid) throws ERPBizException;

	PurchaseFormEntry recallEntry(UserInfo user,String id);
	List<Map<String, Object>> getEntryData(String id);
	Map<String, Object> deleteEntry(UserInfo user, String id);
	List<Map<String, Object>> getLastFormsByMaterials(List<String> ids);


	
}
