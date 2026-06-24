package com.wimoor.amazon.inboundV2.XmlPojo;

import lombok.Data;

import static com.wimoor.amazon.inboundV2.XmlGenerator.CustomsDeclarationHelper.getCurrentTimestamp;

/**
 * 签名信息类
 */
@Data
public   class SignInfo {
    private String operType = "C";         // 操作类型 C-报关单申报
    private String icCode;                 // 操作员IC卡号
    private String copCode;                // 操作企业组织机构代码
    private String operName;               // 操作员姓名
    private String clientSeqNo;            // 客户端报关单编号
    private String sign;                   // 数字签名信息
    private String signDate;               // 签名日期
    private String certificate;            // 操作员卡的证书号
    private String hostId;                 // 客户端邮箱的HostId
    private String domainId = "1";         // 签名人分类 1-录入人
    private String note;                   // 备注
    private String billSeqNo;              // 对应清单统一编号
    public SignInfo() {
    }
    public SignInfo(String icCode, String operName, String clientSeqNo) {
        this.icCode = icCode;
        this.operName = operName;
        this.clientSeqNo = clientSeqNo;
        this.signDate = getCurrentTimestamp();
    }
}
