package com.wimoor.amazon.inboundV2.XmlPojo;

import lombok.Data;

/**
 * 自由文本信息类
 */
@Data
public   class FreeText {
    private String relId;                  // 关联报关单号
    private String relManNo;               // 关联备案号
    private String bonNo;                  // 监管仓号
    private String voyNo;                  // 航次号
    private String decBpNo;                // 报关员联系方式
    private String cusFie;                 // 货场代码
    private String decNo;                  // 申报人员证号
}