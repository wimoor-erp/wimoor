package com.wimoor.erp.finance.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.pojo.entity.FinJournalAccount;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPaymentMethod;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IFaccountService extends IService<FinJournalAccount> {
	public Map<String, Object> findSumByCondition(Map<String, Object> param);

	List<Map<String, Object>> findByCondition(Map<String, Object> param);

	List<Map<String, Object>> paymentSum(String shopid);

	List<Map<String, Object>> paymentSumByCondition(Map<String, Object> param);

	void setExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);

	public List<Map<String, Object>> findDetialByCondition(Map<String, Object> param);

	BigDecimal getSummary(UserInfo currUser);

	public boolean saveRecord(FinJournalAccount faccount, FinJournalAccount old) throws Exception;

	public List<FinanceProject> findProject(String shopid);

	public List<FinanceProject> findProList(String shopid, String search);

	public Map<String, Object> saveProject(String name, UserInfo user);

	public Map<String, Object> updateProject(String id, String name, UserInfo user);

	public Map<String, Object> delProject(String id);

	public FinAccount readFinAccount(String shopid);

	public void updateFinAfterChange(FinAccount account, String projectid, Date createtime, BigDecimal amount,
			String ftype);

	public List<Map<String, Object>> findAllAcountByType(UserInfo user, String year, String month);

	public Map<Integer, Map<String, Object>> findLineDataByYear(UserInfo user, String year, String month);

	public boolean saveDate(FinJournalAccount faccount, UserInfo currUser);

	public List<PurchaseFormPaymentMethod> findPurchasePayMethod();
	
}
