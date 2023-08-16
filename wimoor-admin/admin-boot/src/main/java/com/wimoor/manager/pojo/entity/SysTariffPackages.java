package com.wimoor.manager.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 套餐表
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_tariff_packages")
@ApiModel(value="SysTariffPackages对象", description="套餐表")
public class SysTariffPackages implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "套餐id 0-基础版，1-标准版，2-专业版，3-独享版,4-自定义")
    @TableId
    private Integer id;

    @ApiModelProperty(value = "套餐名字")
    private String name;
    
    @ApiModelProperty(value = "套餐名字")
    private Boolean isdefault;

    @ApiModelProperty(value = "角色id")
    @TableField("roleId")
    private BigInteger roleId;

    @ApiModelProperty(value = "绑定店铺数量")
    @TableField("maxShopCount")
    private Integer maxShopCount;

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

    @ApiModelProperty(value = "每个店铺绑定站点数量")
    @TableField("maxMarketCount")
    private Integer maxMarketCount;

    @ApiModelProperty(value = "订单存储时间 单位:月  默认基础版是3个月")
    @TableField("orderMemoryCount")
    private String orderMemoryCount;

    @ApiModelProperty(value = "每天开启广告组数量")
    @TableField("dayOpenAdvCount")
    private Integer dayOpenAdvCount;

    @ApiModelProperty(value = "跟卖监控产品数量")
    @TableField("controlProductCount")
    private Integer controlProductCount;

    @ApiModelProperty(value = "商品分析数量")
    @TableField("anysisProductCount")
    private Integer anysisProductCount;

    private BigDecimal yearprice;

    private BigDecimal monthprice;

    @ApiModelProperty(value = "最后更新时间")
    @TableField("lastUpdateTime")
    private LocalDate lastUpdateTime;

    @ApiModelProperty(value = "最后更新的人")
    @TableField("lastUpdateUser")
    private String lastUpdateUser;


}
