package com.wimoor.amazon.inboundV2.XmlPojo;

import lombok.Data;

/**
 * 电子随附单据关联关系类
 */
@Data
public class EdocRelation {
    private String edocId;                 // 文件名
    private String edocCode;               // 随附单证类别
    private String edocFomatType = "US";   // 随附单据格式类型
    private String opNote;                 // 操作说明
    private String edocCopId;              // 随附单据文件企业名
    private String edocOwnerCode;          // 所属单位海关编号
    private String signUnit;               // 签名单位代码
    private String signTime;               // 签名时间
    private String edocOwnerName;          // 所属单位名称
    private String edocSize;               // 随附单据文件大小
}
