package com.wimoor.erp.purchase.alibaba.service;

import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlement;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementPayReturn;

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
public interface IPurchaseAlibabaSettlementPayReturnService extends IService<PurchaseAlibabaSettlementPayReturn> {
	Boolean uploadSettlementSheet(Workbook workbook, PurchaseAlibabaSettlement settlement);
}
