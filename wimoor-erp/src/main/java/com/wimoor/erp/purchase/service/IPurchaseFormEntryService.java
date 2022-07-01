package com.wimoor.erp.purchase.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;

public interface IPurchaseFormEntryService extends IService<PurchaseFormEntry>{

	Map<String, Object> summaryBySupplier(String shopid,String supplier);

}
