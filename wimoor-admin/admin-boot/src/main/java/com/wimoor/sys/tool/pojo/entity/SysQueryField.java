package com.wimoor.sys.tool.pojo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_query_field")
@ApiModel(value="SysQueryField对象", description="")
public class SysQueryField implements Serializable {

    private static final long serialVersionUID=1L;

      private String fquery;

    private String ffield;

    private String title;

    @TableField("titleTooltip")
    private String titleTooltip;

    private String width;

    private Integer findex;

    private String formatter;

    @TableField("footerFormatter")
    private String footerFormatter;

    private Boolean sortable;

    private String valign;

    private String align;

    private LocalDateTime createdate;


}
