package com.wimoor.sys.gc.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TableFieldVO", description = "数据库表字段")
public class TableFieldVO {

    @ApiModelProperty(value = "字段名")
    private String name;

    @ApiModelProperty(value = "字段类型")
    private String type;

    @ApiModelProperty(value = "字段描叙")
    private String desc;

    @ApiModelProperty(value = "字段详情")
    private String typeDetail;

    @ApiModelProperty(value = "是否可用为空")
    private String isNull;

    /**
     * 空串代码判断替换
     */
    @ApiModelProperty(value = "默认数据")
    private String defaultVal;

    /**
     * 代码判断
     */
    @ApiModelProperty(value = "是否未通用字段")
    Boolean isChecked;
}
