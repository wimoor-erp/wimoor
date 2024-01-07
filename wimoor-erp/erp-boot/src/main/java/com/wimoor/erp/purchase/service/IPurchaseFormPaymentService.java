package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;

public interface IPurchaseFormPaymentService extends IService<PurchaseFormPayment> {
	int updatePaymentStatus(String paymentid, String status, String remark, UserInfo user, String deliverydate);
    boolean restartPay(PurchaseFormEntry entry);
	int closePay(PurchaseFormEntry entry) throws ERPBizException;
	void setPaymentReportExcel(SXSSFWorkbook workbook, Map<String, Object> param);
	void cancelPayment(PurchaseFormPayment payment, String paytype, UserInfo user);
	public IPage<Map<String, Object>> getPaymentReport(Page<?> page, Map<String, Object> param);
	public int updatePayment(List<PurchaseFormPayment> paymentlist,PurchaseFormEntry entry,FinAccount account, UserInfo user)  throws ERPBizException;
	public List<PurchaseFormPayment> getPaymentByEntryid(String id,String paytype) ;
	public void handleAvgPrice(PurchaseFormEntry entry);
	int removePay(String id);
}
