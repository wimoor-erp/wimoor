package com.wimoor.manager.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;

import java.time.LocalDateTime;
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
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_customer_order_refund")
@ApiModel(value="SysCustomerOrderRefund对象", description=" ")
public class SysCustomerOrderRefund extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "公司ID")
    private Long shopid;

    private Long orderid;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "支付宝交易号")
    private String tradeNo;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "订单名称")
    private String refundReason;

    private String outRequestNo;

    private LocalDateTime opttime;


}
