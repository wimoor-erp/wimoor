package com.wimoor.quote.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.quote.purchase.pojo.entity.PurchaseAlibabaOrder;
import com.wimoor.quote.purchase.service.IPurchaseAlibabaOrderService;
import com.wimoor.quote.purchase.mapper.PurchaseAlibabaOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_purchase_alibaba_order】的数据库操作Service实现
* @createDate 2025-06-07 11:30:18
*/
@Service
@RequiredArgsConstructor
public class PurchaseAlibabaOrderServiceImpl extends ServiceImpl<PurchaseAlibabaOrderMapper, PurchaseAlibabaOrder>
    implements IPurchaseAlibabaOrderService {

}




