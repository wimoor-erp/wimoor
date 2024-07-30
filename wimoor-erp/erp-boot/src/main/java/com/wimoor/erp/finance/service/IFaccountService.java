package com.wimoor.erp.finance.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPaymentMethod;

public interface IFaccountService extends IService<FinAccount> {

	BigDecimal getSummary(UserInfo currUser);

	public List<FinanceProject> findProList(String shopid, String search);

	public FinAccount readFinAccount(String shopid);

	public void updateFinAfterChange(FinAccount account, String projectid, Date createtime, BigDecimal amount,String ftype);

	public void updateFinCancelChange(FinAccount account, String projectid, Date createtime, BigDecimal amount, String ftype);
	
	public List<PurchaseFormPaymentMethod> findPurchasePayMethod();

	public List<FinAccount> findPayAccountByMethod(String paymethod, String shopid);

	public FinAccount getAccByMeth(String shopid,String paymethod);

	public List<FinAccount> findAccountAll(String shopid);

	public Boolean saveAccount(FinAccount fin);
	
	public List<FinAccount> findAccountArchiveAll(String shopid);
}
