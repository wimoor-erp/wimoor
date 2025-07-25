package com.wimoor.quote.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.api.entity.UserBuyer;
import com.wimoor.quote.purchase.pojo.dto.PurchaseQuoteDTO;

import java.util.List;

public interface  IPurchaseQuoteManagerService {

    boolean savePurchase(UserBuyer buyer, PurchaseQuoteDTO dto);

    Page<?> listPurchase(UserBuyer buyer, QuoteDTO dto);

}
