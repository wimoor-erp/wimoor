package com.wimoor.quote.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.quote.purchase.pojo.entity.PurchaseAlibabaReceiver;
import com.wimoor.quote.purchase.service.IPurchaseAlibabaReceiverService;
import com.wimoor.quote.purchase.mapper.PurchaseAlibabaReceiverMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_purchase_alibaba_receiver】的数据库操作Service实现
* @createDate 2025-06-07 11:42:06
*/
@Service
@RequiredArgsConstructor
public class PurchaseAlibabaReceiverServiceImpl extends ServiceImpl<PurchaseAlibabaReceiverMapper, PurchaseAlibabaReceiver>
    implements IPurchaseAlibabaReceiverService {

}




