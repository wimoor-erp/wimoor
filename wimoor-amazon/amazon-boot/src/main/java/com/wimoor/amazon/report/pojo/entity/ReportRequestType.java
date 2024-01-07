package com.wimoor.amazon.report.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 亚马逊报表类型
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_report_request_type")
@ApiModel(value="ReportRequestType对象", description="亚马逊报表类型")
public class ReportRequestType implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      private Integer id;

    @ApiModelProperty(value = "中文名称")
    private String cname;

    @ApiModelProperty(value = "英文名称")
    private String ename;

    @ApiModelProperty(value = "报表编码")
    private String code;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "报表处理类")
    private String bean;

    @ApiModelProperty(value = "报表默认请求天数")
    private Integer day;

    @ApiModelProperty(value = "是否可用")
    private Boolean disabled;

    @ApiModelProperty(value = "报表默认参数")
    @TableField("reportOptions")
    private String reportOptions;


}
