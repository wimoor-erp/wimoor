package com.wimoor.amazon.inboundV2.XmlPojo;

import lombok.Data;

/**
 * 导入系统响应
 */
@Data
public class ImportResponse {
    private String responseCode;           // 响应代码 0-成功 其他-失败
    private String errorMessage;           // 错误信息
    private String clientSeqNo;            // 客户端报关单编号
    private String seqNo;                  // 数据中心统一编号
    private String trnPreId;               // 转关单统一编号

    public boolean isSuccess() {
        return "0".equals(responseCode);
    }

    // Getter和Setter方法
    // ... (为节省篇幅，这里省略)
}
