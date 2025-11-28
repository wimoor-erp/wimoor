package com.wimoor.quote.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.quote.purchase.pojo.entity.PurchaseAlibabaBuyer;
import com.wimoor.quote.purchase.service.IPurchaseAlibabaBuyerService;
import com.wimoor.quote.purchase.mapper.PurchaseAlibabaBuyerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_purchase_alibaba_buyer】的数据库操作Service实现
* @createDate 2025-06-07 11:17:05
*/
@Service
@RequiredArgsConstructor
public class PurchaseAlibabaBuyerServiceImpl extends ServiceImpl<PurchaseAlibabaBuyerMapper, PurchaseAlibabaBuyer>
implements IPurchaseAlibabaBuyerService {

}
