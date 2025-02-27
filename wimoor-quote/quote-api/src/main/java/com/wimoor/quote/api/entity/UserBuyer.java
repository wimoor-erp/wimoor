package com.wimoor.quote.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@ApiModel(value="t_user_buyer对象", description="买家")
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_buyer")
public class UserBuyer extends BaseEntity{


    @ApiModelProperty(value = "名称")
    @TableField(value =  "name")
    private String name;

    @ApiModelProperty(value = "公司id")
    @TableField(value =  "company")
    private String company;

    @ApiModelProperty(value = "地址")
    @TableField(value =  "address")
    private String address;

    @ApiModelProperty(value = "联系人")
    @TableField(value =  "contact")
    private String contact;

    @ApiModelProperty(value = "手机号")
    @TableField(value =  "mobile")
    private String mobile;

    @ApiModelProperty(value = "token")
    @TableField(value =  "token")
    private String token;

    @ApiModelProperty(value = "授权时间")
    @TableField(value =  "tokentime")
    private Date tokentime;

    @ApiModelProperty(value = "创建时间")
    @TableField(value =  "createtime")
    private Date createtime;


}
