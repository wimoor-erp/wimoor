package com.wimoor.feishu.pojo.entity;

import com.lark.oapi.core.response.BaseResponse;
import com.lark.oapi.service.bitable.v1.model.CreateAppRespBody;
import com.lark.oapi.service.bitable.v1.model.CreateAppTableRespBody;
import lombok.Data;

import java.util.List;

@Data
public class CreateAppAndTablesResponse extends BaseResponse<CreateAppRespBody>{
    int code;
    String msg;
    List<CreateAppTableRespBody>  tables ;
   CreateAppRespBody createAppRespBody;

    public void setCreateAppResponse(CreateAppRespBody data) {
        this.createAppRespBody = data;
    }

    public void setCreateAppTableResponse(List<CreateAppTableRespBody> tables) {
        this.tables = tables;
    }
}
