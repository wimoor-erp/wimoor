package com.wimoor.erp.purchase.alibaba.service;

import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlement;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementPay;

import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 还款明细列表 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-11-01
 */
public interface IPurchaseAlibabaSettlementPayService extends IService<PurchaseAlibabaSettlementPay> {
	Boolean uploadSettlementSheet(Workbook workbook, PurchaseAlibabaSettlement settlement);
}
