package com.wimoor.sys.gc.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TableVO", description = "数据库表")
public class TableVO {

    @ApiModelProperty(value = "表名")
    private String name;

    @ApiModelProperty(value = "表描叙")
    private String comment;

}
