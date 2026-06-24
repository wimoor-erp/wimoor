package com.wimoor.amazon.inboundV2.XmlPojo;


import lombok.Data;

/**
 * 企业资质信息类
 */
@Data
public class CopLimit {
    private String entQualifNo;            // 企业资质编号
    private String entQualifTypeCode;      // 企业资质类别代码
}