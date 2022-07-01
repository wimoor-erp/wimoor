package com.wimoor.erp.purchase.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.purchase.mapper.PurchasePlanModelItemMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanModelItem;
import com.wimoor.erp.purchase.service.IPurchasePlanModelItemService;

@Service("purchasePlanModelItemService")
public class PurchasePlanModelItemServiceImpl extends  ServiceImpl<PurchasePlanModelItemMapper,PurchasePlanModelItem> implements IPurchasePlanModelItemService {

}
