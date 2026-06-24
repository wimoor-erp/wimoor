package com.wimoor.amazon.inboundV2.XmlPojo;

import com.wimoor.amazon.inboundV2.util.XmlHelper;
import lombok.Data;

/**
 * 企业信息
 */
@Data
public class EnterpriseInfo {
    private String guid;
    private String orgCode;           // 18位信用代码
    private String orgName;           // 企业名称
    private String cusCode;           // 10位海关备案号
    private String bizCategories;     // 企业经营类别
    private String contactPerson;     // 联系人
    private String contactTelephone;  // 联系电话
    private String contactEmail;      // 联系邮箱

    // 构造函数
    public EnterpriseInfo() {
    }
    public EnterpriseInfo(String orgCode, String orgName, String cusCode,
                          String bizCategories) {
        this.guid = XmlHelper.generateGuid();
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.cusCode = cusCode;
        this.bizCategories = bizCategories;
    }

}
