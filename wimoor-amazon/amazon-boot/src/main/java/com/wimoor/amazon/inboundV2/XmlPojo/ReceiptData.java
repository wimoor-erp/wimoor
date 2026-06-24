package com.wimoor.amazon.inboundV2.XmlPojo;

import com.wimoor.amazon.inboundV2.util.XmlHelper;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 收款单数据
 */
@Data
public class ReceiptData {
    private String guid;
    private String appType = "1";      // 1-新增
    private String appTime;
    private String appStatus = "2";    // 2-申报
    private String ebpCode;
    private String ebpName;
    private String ebcCode;
    private String ebcName;
    private String orderNo;
    private String payCode;            // 支付企业代码
    private String payName = "现金支付"; // 默认现金支付
    private String payNo;              // 支付交易编号
    private BigDecimal charge;         // 收款金额
    private String currency = "142";   // 人民币
    private String accountingDate;     // 到账时间
    private String note;

    // BaseTransfer信息
    private String copCode;
    private String copName;
    private String dxpId;
    private String baseNote;
    public ReceiptData() {
    }
    // 构造函数
    public ReceiptData(String ebpCode, String ebpName, String ebcCode,
                       String ebcName, String orderNo, BigDecimal charge) {
        this.guid = XmlHelper.generateGuid();
        this.appTime = XmlHelper.getCurrentAppTime();
        this.accountingDate = XmlHelper.getCurrentAppTime();
        this.ebpCode = ebpCode;
        this.ebpName = ebpName;
        this.ebcCode = ebcCode;
        this.ebcName = ebcName;
        this.orderNo = orderNo;
        this.charge = charge;
    }

    // ... getter/setter
}