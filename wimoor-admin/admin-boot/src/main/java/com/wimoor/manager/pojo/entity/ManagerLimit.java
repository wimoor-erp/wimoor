package com.wimoor.manager.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_manager_limit")
@ApiModel(value="ManagerLimit对象", description="")
public class ManagerLimit extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField("shopId")
    private BigInteger shopId;

    @ApiModelProperty(value = "绑定店铺数量")
    @TableField("maxShopCount")
    private Integer maxShopCount;

    @ApiModelProperty(value = "绑定店铺站点数量")
    @TableField("maxMarketCount")
    private Integer maxMarketCount;

    @ApiModelProperty(value = "商品数量上限")
    @TableField("maxProductCount")
    private Integer maxProductCount;

    @ApiModelProperty(value = "处理订单上限")
    @TableField("maxOrderCount")
    private Integer maxOrderCount;

    @ApiModelProperty(value = "子用户数量上限")
    @TableField("maxMember")
    private Integer maxMember;

    @ApiModelProperty(value = "利润计算方案数量")
    @TableField("maxProfitPlanCount")
    private Integer maxProfitPlanCount;

    @ApiModelProperty(value = "每天开启广告组数量")
    @TableField("maxdayOpenAdvCount")
    private Integer maxdayOpenAdvCount;

    @TableField("existShopCount")
    private Integer existShopCount;

    @TableField("existMarketCount")
    private Integer existMarketCount;

    @TableField("existProductCount")
    private Integer existProductCount;

    @TableField("existOrderCount")
    private Integer existOrderCount;

    @TableField("existMember")
    private Integer existMember;

    @TableField("existProfitPlanCount")
    private Integer existProfitPlanCount;

    @TableField("existdayOpenAdvCount")
    private Integer existdayOpenAdvCount;

    @ApiModelProperty(value = "0-基础版，1-标准版，2-专业版，3-独享版,4-自定义")
    private Integer tariffpackage;

    @ApiModelProperty(value = "失效时间")
    @TableField("losingEffect")
    private LocalDate losingEffect;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime opratetime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "修改人")
    private BigInteger oprate;

    @TableField("logicVersion")
    private Long logicVersion;

    private String saleskey;

    @TableField("neverNoticeShop")
    private Boolean neverNoticeShop;

    @TableField("afterNnoticeTariff")
    private LocalDate afterNnoticeTariff;


}
