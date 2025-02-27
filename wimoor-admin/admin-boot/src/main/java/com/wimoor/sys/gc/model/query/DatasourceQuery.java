package com.wimoor.sys.gc.model.query;

import com.wimoor.sys.gc.model.core.BaseQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 代码生成数据源维护表
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DatasourceDTO 对象", description = "代码生成数据源维护表")
public class DatasourceQuery extends BaseQuery {

    private static final long serialVersionUID = 0L;
    
    @ApiModelProperty(notes = "数据库标题" ,position = 0)
    private String dbTitle;

    @ApiModelProperty(notes = "数据库名" ,position = 1)
    private String dbName;

}

