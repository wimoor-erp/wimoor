package com.wimoor.quote.purchase.pojo.dto;

import com.wimoor.quote.purchase.pojo.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseQuoteDTO {
    PurchaseAlibabaBuyer buyer;
    PurchaseAlibabaSeller seller;
    PurchaseAlibabaReceiver receiver;
    PurchaseAlibabaOrder order;
    List<PurchaseAlibabaOrderItem> orderItems;
    PurchaseOrderEntry entry;
    private String token;
    private String othername;

}
