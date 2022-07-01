package com.wimoor.erp.purchase.service.impl;


import java.util.Map;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;

import lombok.RequiredArgsConstructor;

@Service("purchaseFormEntryService")
@RequiredArgsConstructor
public class PurchaseFormEntryServiceImpl extends  ServiceImpl<PurchaseFormEntryMapper,PurchaseFormEntry> implements IPurchaseFormEntryService {
 
	
	@Override
	public Map<String, Object> summaryBySupplier(String shopid,String supplier) {
		// TODO Auto-generated method stub
		return this.baseMapper.summaryBySupplier(shopid,supplier);
	}


}
