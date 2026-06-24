package com.wimoor.amazon.inboundV2.XmlPojo;


import lombok.Data;

/**
 * 使用人信息类
 */
@Data
public  class UserInfo {
    private String useOrgPersonCode;       // 使用单位联系人
    private String useOrgPersonTel;        // 使用单位联系电话
}
