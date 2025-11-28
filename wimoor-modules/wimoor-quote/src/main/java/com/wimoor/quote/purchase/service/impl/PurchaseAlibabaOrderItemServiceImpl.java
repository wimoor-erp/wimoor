package com.wimoor.quote.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.quote.purchase.pojo.entity.PurchaseAlibabaOrderItem;
import com.wimoor.quote.purchase.service.IPurchaseAlibabaOrderItemService;
import com.wimoor.quote.purchase.mapper.PurchaseAlibabaOrderItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_purchase_alibaba_order_item】的数据库操作Service实现
* @createDate 2025-06-07 11:47:36
*/
@Service
@RequiredArgsConstructor
public class PurchaseAlibabaOrderItemServiceImpl extends ServiceImpl<PurchaseAlibabaOrderItemMapper, PurchaseAlibabaOrderItem>
    implements IPurchaseAlibabaOrderItemService {

}




