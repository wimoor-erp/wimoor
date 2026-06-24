package com.wimoor.feishu.pojo.entity;

import lombok.Data;

@Data
public class CopyAndPasteByRangeRequest {
    String spreadsheetToken;
    String sheetName;
    String srcRange;
}
