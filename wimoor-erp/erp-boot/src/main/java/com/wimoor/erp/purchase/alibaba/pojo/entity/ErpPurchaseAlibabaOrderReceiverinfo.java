package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.io.Serializable;
import java.math.BigInteger;

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
@TableName("t_erp_purchase_alibaba_order_receiverinfo")
@ApiModel(value="ErpPurchaseAlibabaOrderReceiverinfo对象", description="")
public class ErpPurchaseAlibabaOrderReceiverinfo implements Serializable {

    private static final long serialVersionUID=1L;

    private BigInteger orderid;

    @TableField("toArea")
    private String toArea;

    @TableField("toDivisionCode")
    private String toDivisionCode;

    @TableField("toFullName")
    private String toFullName;

    @TableField("toMobile")
    private String toMobile;

    @TableField("toPost")
    private String toPost;


}
