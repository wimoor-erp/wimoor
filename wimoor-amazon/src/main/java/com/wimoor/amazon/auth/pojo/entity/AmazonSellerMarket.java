package com.wimoor.amazon.auth.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 授权对应区域客户所有绑定的站点
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amazonseller_market")
@ApiModel(value="AmazonSellerMarket对象", description="授权对应区域客户所有绑定的站点")
public class AmazonSellerMarket implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "卖家Sellerid")
    private String sellerid;

    @ApiModelProperty(value = "站点ID")
    @TableField(value="marketplace_id")
    private String marketplaceId;

    @ApiModelProperty(value = "国家编码")
    private String country;

    @ApiModelProperty(value = "站点英文名称")
    private String name;

    @ApiModelProperty(value = "对应语言编码")
    private String language;

    @ApiModelProperty(value = "对应币种")
    private String currency;

    @ApiModelProperty(value = "对应域名")
    private String domain;

    @ApiModelProperty(value = "授权对应ID等同于Sellerid")
    private BigInteger amazonauthid;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime opttime;

    @ApiModelProperty(value = "操作人")
    private Boolean disable;


}
