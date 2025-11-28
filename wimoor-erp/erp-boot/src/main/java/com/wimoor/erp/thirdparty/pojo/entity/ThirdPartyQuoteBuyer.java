package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_thirdparty_quote_buyer")
public class ThirdPartyQuoteBuyer extends BaseEntity {


    @ApiModelProperty(value = "token验证码")
    @TableField(value =  "buyertoken")
    private String buyertoken;

    @ApiModelProperty(value = "别名")
    @TableField(value =  "name")
    private String name;

    @ApiModelProperty(value = "isowner验证码")
    @TableField(value =  "isowner")
    private Boolean isowner;

    @ApiModelProperty(value = "公司id")
    @TableField(value =  "shopid")
    private String shopid;
}