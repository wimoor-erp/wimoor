package com.wimoor.erp.material.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MaterialListing对象", description = "产品多语言Listing信息表")
@TableName("t_erp_material_listing")
public class MaterialListing {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "产品ID")
    @TableField(value = "materialid")
    private String materialid;

    @ApiModelProperty(value = "公司ID")
    @TableField(value = "shopid")
    private String shopid;

    @ApiModelProperty(value = "语言代码")
    @TableField(value = "lang")
    private String lang;

    @ApiModelProperty(value = "Listing标题")
    @TableField(value = "title")
    private String title;

    @ApiModelProperty(value = "Listing描述(HTML格式)")
    @TableField(value = "description")
    private String description;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "creator")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "最后修改人")
    @TableField(value = "operator")
    private String operator;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
