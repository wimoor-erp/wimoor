package com.wimoor.erp.purchase.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.purchase.mapper.PurchasePlanSubMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanSub;
import com.wimoor.erp.purchase.service.IPurchasePlanSubService;

@Service("purchasePlanSubService")
public class PurchasePlanSubServiceImpl extends  ServiceImpl<PurchasePlanSubMapper,PurchasePlanSub> implements IPurchasePlanSubService {

}
