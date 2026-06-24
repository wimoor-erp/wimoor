package com.wimoor.feishu.pojo.entity;

import lombok.Data;

@Data
public class SpreadsheetResp {
    boolean success;
    int code;
    String msg;
    String requestId;
    String dstRange;
    String data;
    public boolean success() {
        return success;
    }
}
