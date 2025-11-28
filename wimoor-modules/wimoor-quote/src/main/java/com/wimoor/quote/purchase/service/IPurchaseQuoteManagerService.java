package com.wimoor.quote.purchase.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.purchase.pojo.entity.PurchaseAlibabaOrderItem;
import com.wimoor.quote.ship.pojo.entity.UserBuyer;
import com.wimoor.quote.purchase.pojo.dto.PurchaseQuoteDTO;

import java.util.Map;
import java.util.List;

public interface  IPurchaseQuoteManagerService {

    boolean savePurchase(UserBuyer buyer, PurchaseQuoteDTO dto);

    IPage<Map<String, Object>> listPurchase(UserBuyer buyer, QuoteDTO dto);

    List<PurchaseAlibabaOrderItem> getEntryList(String orderid);
}
