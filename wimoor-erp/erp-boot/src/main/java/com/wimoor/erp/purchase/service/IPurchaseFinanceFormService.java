package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.purchase.pojo.dto.FinanceFormPayMethDTO;
import com.wimoor.erp.purchase.pojo.dto.PaymentSaveDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFinanceForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;

public interface IPurchaseFinanceFormService extends IService<PurchaseFinanceForm>{
	
	IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> param);

	Map<String, Object> applyPayment(UserInfo user, PurchaseFormEntry entry, PaymentSaveDTO dto);

	Map<String, Object> getDetailMap(String id, String shopid);

	Map<String, Object> approve(List<String> ids, UserInfo userinfo);

	Map<String, Object> updatePay(FinanceFormPayMethDTO dto, UserInfo userinfo);

	Map<String, Object> updateRemark(String id, String remark, UserInfo userinfo);

	Map<String, Object> approveReturn(List<String> ids, UserInfo userinfo);

	List<Map<String, Object>> getDetailData(List<String> ids, String shopid);

	Map<String, Object> paymentForm(List<String> ids, UserInfo user);

}
