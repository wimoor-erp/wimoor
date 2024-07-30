package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_purchase_alibaba_order_tradeterms")
@ApiModel(value="ErpPurchaseAlibabaOrderTradeterms对象", description="")
public class ErpPurchaseAlibabaOrderTradeterms implements Serializable {

    private static final long serialVersionUID=1L;

    private BigInteger orderid;

    @TableField("payStatus")
    private String payStatus;

    @TableField("payTime")
    private LocalDateTime payTime;

    private String payway;

    @TableField("phasAmount")
    private BigDecimal phasAmount;

    @TableField("cardPay")
    private Boolean cardPay;

    @TableField("expressPay")
    private Boolean expressPay;

    @TableField("payWayDesc")
    private String payWayDesc;


}
