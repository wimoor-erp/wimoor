package com.wimoor.sys.tool.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("记事本查询DTO")
public class SysNotepadQueryDTO extends BasePageQuery {
    @ApiModelProperty(value = "店铺ID", required = true)
    private String shopid;

    @ApiModelProperty("search")
    private String search;

    @ApiModelProperty("searchtype")
    private String searchtype;

}
