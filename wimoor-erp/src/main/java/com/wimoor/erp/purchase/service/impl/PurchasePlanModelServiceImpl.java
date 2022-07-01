package com.wimoor.erp.purchase.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.purchase.mapper.PurchasePlanModelMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanModel;
import com.wimoor.erp.purchase.service.IPurchasePlanModelService;

@Service("purchasePlanModelService")
public class PurchasePlanModelServiceImpl extends  ServiceImpl<PurchasePlanModelMapper,PurchasePlanModel> implements IPurchasePlanModelService {

}
