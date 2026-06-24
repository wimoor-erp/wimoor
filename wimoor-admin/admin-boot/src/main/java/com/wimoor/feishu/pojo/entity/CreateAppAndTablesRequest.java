package com.wimoor.feishu.pojo.entity;

import com.lark.oapi.service.bitable.v1.model.ReqTable;
import lombok.Data;

@Data
public class CreateAppAndTablesRequest {
    String name;

    String folderToken;

    Iterable<? extends ReqTable> tables;

}
