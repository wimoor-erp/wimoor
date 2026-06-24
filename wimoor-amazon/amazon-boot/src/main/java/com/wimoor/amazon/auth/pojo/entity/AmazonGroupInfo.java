package com.wimoor.amazon.auth.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value="店铺AmazonGroup对象", description="亚马逊店铺")
@EqualsAndHashCode(callSuper = false)
@TableName("t_amazon_group_info")
public class AmazonGroupInfo {

    @ApiModelProperty(value = "店铺ID")
    @TableId(value = "groupid")
    private String groupid;

    @ApiModelProperty(value = "显示名称")
    @TableField(value =  "display_name")
    private String displayName;

    @ApiModelProperty(value = "公司名称")
    @TableField(value =  "legal_name")
    private String legalName;

}
