package com.wimoor.amazon.inboundV2.XmlPojo;

import lombok.Data;

/**
 * 申请单证信息类
 */
@Data
public class RequestCert {
    private String appCertCode;            // 申请单证代码
    private String applOri;                // 申请单证正本数
    private String applCopyQuan;           // 申请单证副本数
}
