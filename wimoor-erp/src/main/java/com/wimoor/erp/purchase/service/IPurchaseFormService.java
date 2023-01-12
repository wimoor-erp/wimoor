package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormReceive;
import com.wimoor.erp.purchase.pojo.vo.PurchaseFormReceiveVo;

public interface IPurchaseFormService extends IService<PurchaseForm> {

	IPage<Map<String, Object>> getPurchaseForm(Page<?> page,UserInfo user, Map<String, Object> param );
	public Map<String, Object> savePurchaseDataAction(HttpServletRequest request) throws ERPBizException ;

	boolean updatePurchaseFormEntry(PurchaseFormEntry item, String warehouseid) throws ERPBizException;

	PurchaseFormEntry updatePrice(UserInfo user,String id, Float itemprice, Integer amount,Float orderprice) throws ERPBizException;
    
    int approvals(UserInfo user,String ids) throws ERPBizException;

	Map<String, Object> getDetailMap(String id,String shopid) throws ERPBizException;
 
	Map<String, Object> getTraceDetailMap(String id, String shopid,String ftype);
	
	public int updatePayment(List<PurchaseFormPayment> paymentlist,PurchaseFormEntry entry, UserInfo user,String logisiter) throws ERPBizException;

	void saveReceive(UserInfo user,PurchaseFormReceive rec, PurchaseFormEntry entry) throws ERPBizException;

	int removePay(String id);

	int removeRec(String id, UserInfo user) throws ERPBizException ;
	
	public int closeRec(UserInfo user,PurchaseFormEntry entry) throws ERPBizException;
	
    public int restartRec(UserInfo user,PurchaseFormEntry entry);
	
    boolean restartPay(PurchaseFormEntry entry);
    
	int closePay(PurchaseFormEntry entry) throws ERPBizException;
	
	int sysAutoRec(UserInfo user, String ids);

	int sysAutoPay(UserInfo user, String ids);
	
    int findSumaryNeedApply(String shopid);
    
	int findSumaryNeedin(String shopid);
	
	int findSumaryNeedpay(String shopid);
    
	void purchaseReturn(UserInfo user, String id,  String remark) throws ERPBizException;

	Map<String, Object> updateNotice(String id, String notice, String shopid, String userid) throws ERPBizException;

	Map<String,Object> updateWarehouse(UserInfo user, String id, String warehouseid) throws ERPBizException;

	List<Map<String, Object>> getLastFormByMaterial(Object id,int i);
	
	Map<String, Object> getLastOneFormByMaterial(Object id);
	
	List<Map<String, Object>> getPaymentReport(Map<String, Object> param);

	IPage<PurchaseFormReceiveVo> getReceiveReport(Page<?> page,Map<String, Object> param);

	List<Map<String, Object>> purchaseFormReport(Map<String, Object> param);
    
	List<Map<String,Object>> getPurchaseSumReport(Map<String,Object> param);
	
	List<Map<String,Object>> getPurchaseRecSumReport(  Map<String, Object> param);
	
	public Map<String,Object> getViewData(String id, String shopid);

	Map<String, Object> getPurchaseFormSummary(UserInfo user, Map<String, Object> param);

	IPage<Map<String, Object>> getPurchaseFormNew(Page<?> page,Map<String, Object> param);

	List<Map<String, Object>> getFormDetail(String id, String shopid);


	Map<String, Object> purchaseFormReportTotal(Map<String, Object> param);

	Map<String, Object> getLastPurchaseRecord(String shopid, String warehouseid);

	List<Map<String, Object>> getPurchaseSumReportNew(Map<String, Object> param);

	IPage<Map<String, Object>> getPayRecSumReport(Page<?> page,Map<String, Object> param);

	void setExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);

	PurchaseFormEntry clearReceive(String entryid, String warehouseid, UserInfo user);

	void setPurchaseSkuItemExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
	
	public List<Map<String, Object>> loadPuechaseFormDate(UserInfo user, String planid, String warehouseid, String ftype, List<String> item_material_list);

	void getPurchaseRecInfoExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);

	List<Map<String, Object>> findSupplierByForm(String formid);
	
	int updatePaymentStatus(String paymentid, String status, String remark, UserInfo user, String deliverydate);
	
	void downloadPurchasePaymentWord(HttpServletRequest request, HttpServletResponse response, String recid);
	
	Map<String, Object> getPurchaseNumAllStatus(Map<String, Object> param);
	
	Object uploadPurchaseListByExcel(Sheet sheet, Map<String, Object> map);
	void setReceiveReportExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
	
	Map<String, Object> getPurchaseRecordInfo(String reciveId);
	void setPaymentReportExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
	void setPurchaseFormReportExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
	public boolean updateDeliveryDate(String entryid,String deliverydate) ;
	List<Map<String, Object>> selectNeedSendMsgShop();
	List<Map<String, Object>> selectPurchaseNotify(String shopid);
	public int savePurchaseForm(UserInfo user,List<PurchaseForm> formList, String planwarehouseid) throws ERPBizException;
	
	PurchaseFormEntry deleteReceive(String entryid,  UserInfo userinfo);

}
