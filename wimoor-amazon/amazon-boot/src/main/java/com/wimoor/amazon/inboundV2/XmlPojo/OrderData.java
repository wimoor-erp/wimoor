package com.wimoor.amazon.inboundV2.XmlPojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单数据
 */
@Data
public class OrderData implements Serializable {
    // 订单表头
    private String guid;
    private String appType = "1";      // 1-新增
    private String appTime;
    private String appStatus = "2";    // 2-申报
    private String orderType = "W";    // E-B2C出口
    private String orderNo;
    private String ebpCode;
    private String ebpName;
    private String ebcCode;
    private String ebcName;
    private BigDecimal goodsValue;
    private BigDecimal freight = BigDecimal.ZERO;
    private String currency = "142";   // 人民币
    private String note;
    private Map<String, String> formInfoExt;
    // 订单商品列表
    private List<OrderItem> orderItems;

    // BaseTransfer信息
    private String copCode;
    private String copName;
    private String dxpId;
    private String baseNote;


    // ... getter/setter
}
