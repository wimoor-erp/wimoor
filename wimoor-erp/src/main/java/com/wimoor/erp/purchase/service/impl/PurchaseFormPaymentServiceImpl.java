package com.wimoor.erp.purchase.service.impl;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.purchase.mapper.PurchaseFormPaymentMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
import com.wimoor.erp.purchase.service.IPurchaseFormPaymentService;

import lombok.RequiredArgsConstructor;

@Service("purchaseFormPaymentService")
@RequiredArgsConstructor
public class PurchaseFormPaymentServiceImpl extends  ServiceImpl<PurchaseFormPaymentMapper,PurchaseFormPayment> implements IPurchaseFormPaymentService {

	 
}
