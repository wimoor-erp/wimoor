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
@TableName("t_erp_purchase_alibaba_order_baseinfo")
@ApiModel(value="ErpPurchaseAlibabaOrderBaseinfo对象", description="")
public class ErpPurchaseAlibabaOrderBaseinfo implements Serializable {

    private static final long serialVersionUID=1L;

    private BigInteger id;

    @TableField("idOfStr")
    private String idOfStr;

    @TableField("alipayTradeId")
    private String alipayTradeId;

    @TableField("allDeliveredTime")
    private LocalDateTime allDeliveredTime;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("modifyTime")
    private LocalDateTime modifyTime;

    @TableField("payTime")
    private LocalDateTime payTime;

    @TableField("businessType")
    private String businessType;

    private String status;

    @TableField("buyerFeedback")
    private String buyerFeedback;

    private String remark;

    @TableField("closeOperateType")
    private String closeOperateType;

    @TableField("tradeType")
    private String tradeType;

    private Integer discount;

    private Integer refund;

    @TableField("overSeaOrder")
    private Boolean overSeaOrder;

    @TableField("refundPayment")
    private BigDecimal refundPayment;

    @TableField("shippingFee")
    private BigDecimal shippingFee;

    @TableField("totalAmount")
    private BigDecimal totalAmount;

    @TableField("sumProductPayment")
    private BigDecimal sumProductPayment;

    @TableField("flowTemplateCode")
    private String flowTemplateCode;

    @TableField("buyerID")
    private String buyerID;

    @TableField("sellerID")
    private String sellerID;


}
