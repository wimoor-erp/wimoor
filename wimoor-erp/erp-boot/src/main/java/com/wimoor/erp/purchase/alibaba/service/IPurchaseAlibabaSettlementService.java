package com.wimoor.erp.purchase.alibaba.service;

import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlement;

import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-11-01
 */
public interface IPurchaseAlibabaSettlementService extends IService<PurchaseAlibabaSettlement> {
	public Boolean setSettlementSheet(Workbook workbook, PurchaseAlibabaSettlement settlement);
}
