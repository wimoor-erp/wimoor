package com.wimoor.amazon.inboundV2.XmlPojo;


import lombok.Data;

/**
 * 随附单证信息类
 */
@Data
public class LicenseDocu {
    private String docuCode;               // 单证代码
    private String certCode;               // 单证编号
    public LicenseDocu() {
    }
    public LicenseDocu(String docuCode, String certCode) {
        this.docuCode = docuCode;
        this.certCode = certCode;
    }
}