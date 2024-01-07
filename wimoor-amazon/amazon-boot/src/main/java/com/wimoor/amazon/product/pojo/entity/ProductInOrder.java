package com.wimoor.amazon.product.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 产品信息的订单销售数据
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_in_order")
@ApiModel(value="ProductInOrder对象", description="产品信息的订单销售数据")
public class ProductInOrder implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "产品ID")
    @TableId
    private BigInteger pid;

    @ApiModelProperty(value = "平均销量")
    private Integer avgsales;

    @ApiModelProperty(value = "旧平均销量")
    private Integer oldavgsales;

    @ApiModelProperty(value = "天数")
    private Integer daynum;

    @ApiModelProperty(value = "sales_week,往前推2天之后的7日销量")
    private Integer salesWeek;

    @ApiModelProperty(value = "销售额")
    private BigDecimal priceWeek;

    @ApiModelProperty(value = "30日销量")
    private Integer salesMonth;

    @ApiModelProperty(value = "周订单量")
    private Integer orderWeek;

    @ApiModelProperty(value = "月订单量")
    private Integer orderMonth;

    @ApiModelProperty(value = "销量上升或者下降比率")
    @TableField("changeRate")
    private BigDecimal changeRate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime lastupdate;

    @ApiModelProperty(value = "sales_seven,7日销量")
    private Integer salesSeven;

    @ApiModelProperty(value = "sales_fifteen,15日销量")
    private Integer salesFifteen;

    @ApiModelProperty(value = "排名")
    @TableField(value= "`rank`")
    private Integer rank;


}
