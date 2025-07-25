package com.wimoor.quote.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.quote.purchase.pojo.entity.PurchaseAlibabaSeller;
import com.wimoor.quote.purchase.service.IPurchaseAlibabaSellerService;
import com.wimoor.quote.purchase.mapper.PurchaseAlibabaSellerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_purchase_alibaba_seller】的数据库操作Service实现
* @createDate 2025-06-07 11:34:07
*/
@Service
@RequiredArgsConstructor
public class PurchaseAlibabaSellerServiceImpl extends ServiceImpl<PurchaseAlibabaSellerMapper, PurchaseAlibabaSeller>
    implements IPurchaseAlibabaSellerService {

}




