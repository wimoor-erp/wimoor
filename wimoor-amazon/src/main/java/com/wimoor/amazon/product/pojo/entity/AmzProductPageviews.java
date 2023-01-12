package com.wimoor.amazon.product.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 流量报表
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_product_pageviews_download")
@ApiModel(value="AmzProductPageviews对象", description="流量报表")
public class AmzProductPageviews  extends BaseEntity {

    private static final long serialVersionUID=1L;
    @TableField("amazonAuthid")
    private BigInteger amazonAuthid;

    private String marketplaceid;

    @TableField("SKU")
    private String sku;

    private Date byday;

    private String parentAsin;

    private String childAsin;

    @ApiModelProperty(value = "访问量（点击量）")
    @TableField("Sessions")
    private Integer sessions;

    @ApiModelProperty(value = "访问比例")
    @TableField("Session_Percentage")
    private BigDecimal sessionPercentage;

    @ApiModelProperty(value = "浏览量")
    @TableField("Page_Views")
    private Integer pageViews;

    @TableField("Page_Views_Percentage")
    private BigDecimal pageViewsPercentage;

    @TableField("Buy_Box_Percentage")
    private BigDecimal buyBoxPercentage;

    @ApiModelProperty(value = "销量")
    @TableField("Units_Ordered")
    private Integer unitsOrdered;

    @TableField("Units_Ordered_B2B")
    private Integer unitsOrderedB2b;

    @TableField("Unit_Session_Percentage")
    private BigDecimal unitSessionPercentage;

    @TableField("Unit_Session_Percentage_B2B")
    private BigDecimal unitSessionPercentageB2b;

    @TableField("Ordered_Product_Sales")
    private BigDecimal orderedProductSales;

    @TableField("Ordered_Product_Sales_B2B")
    private BigDecimal orderedProductSalesB2b;

    @TableField("Total_Order_Items")
    private Integer totalOrderItems;

    @TableField("Total_Order_Items_B2B")
    private Integer totalOrderItemsB2b;

    private Date opttime;
}
