package com.wimoor.manager.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

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
@TableName("t_sys_customer_order")
@ApiModel(value="SysCustomerOrder对象", description=" ")
public class SysCustomerOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "公司ID")
    private BigInteger shopid;

    @ApiModelProperty(value = "订单类型  package:套餐  append:附加包")
    private String ftype;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "订单名称")
    private String subject;

    @ApiModelProperty(value = "支付宝交易号")
    private String tradeNo;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal totalAmount;

    private String discountnumber;

    private LocalDateTime paytime;

    @ApiModelProperty(value = "付款状态")
    private String paystatus;

    @ApiModelProperty(value = "开票状态")
    private String ivcstatus;

    private Integer months;

    private Integer tariffpackage;

    private Integer pcs;

    private String paytype;

    private Boolean disable;


}
