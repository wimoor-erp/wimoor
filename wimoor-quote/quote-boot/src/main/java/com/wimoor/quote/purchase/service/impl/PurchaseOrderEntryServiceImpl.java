package com.wimoor.quote.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.quote.purchase.pojo.entity.PurchaseOrderEntry;
import com.wimoor.quote.purchase.service.IPurchaseOrderEntryService;
import com.wimoor.quote.purchase.mapper.PurchaseOrderEntryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_purchase_order_entry】的数据库操作Service实现
* @createDate 2025-06-07 11:50:43
*/
@Service
@RequiredArgsConstructor
public class PurchaseOrderEntryServiceImpl extends ServiceImpl<PurchaseOrderEntryMapper, PurchaseOrderEntry>
    implements IPurchaseOrderEntryService {

}




