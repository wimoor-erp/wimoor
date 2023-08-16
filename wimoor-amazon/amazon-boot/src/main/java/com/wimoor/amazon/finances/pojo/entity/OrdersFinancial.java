package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2022-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_orders_financial")
@ApiModel(value="OrdersFinancial对象", description="")
public class OrdersFinancial implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "自增长ID")
      @TableId(value = "id", type = IdType.AUTO)
    private BigInteger id;

    @ApiModelProperty(value = "订单ID")
    @TableField(value="amazon_order_id")
    private String amazonOrderId;

    @TableField(value="order_item_id")
    @ApiModelProperty(value = "订单Item id")
    private String orderItemId;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "货币")
    private String currency;

    @ApiModelProperty(value = "费用类型")
    private String ftype;

    @ApiModelProperty(value = "费用")
    private BigDecimal amount;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime opttime;

    @ApiModelProperty(value = "站点")
    @TableField("marketplaceId")
    private String marketplaceId;

    @ApiModelProperty(value = "出账时间")
    @TableField(value="posted_date")
    private Date postedDate;


}
