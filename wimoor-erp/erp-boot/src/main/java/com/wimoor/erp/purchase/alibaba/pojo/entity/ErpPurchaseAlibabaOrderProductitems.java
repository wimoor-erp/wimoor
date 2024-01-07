package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_erp_purchase_alibaba_order_productitems")
@ApiModel(value="ErpPurchaseAlibabaOrderProductitems对象", description="")
public class ErpPurchaseAlibabaOrderProductitems implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId("skuID")
    private BigInteger skuID;

    private BigInteger orderid;

    private BigInteger entryid;

    @TableField("itemAmount")
    private Integer itemAmount;

    private BigDecimal price;

    @TableField("entryDiscount")
    private BigDecimal entryDiscount;

    private String status;

    @TableField("statusStr")
    private String statusStr;

    @TableField("skuInfos")
    private String skuInfos;

    @TableField("productID")
    private Long productID;

    @TableField("productCargoNumber")
    private String productCargoNumber;

    private String name;

    @TableField("logisticsStatus")
    private Integer logisticsStatus;

    @TableField("productSnapshotUrl")
    private String productSnapshotUrl;

    @TableField("productImgUrl")
    private String productImgUrl;

    private String unit;

    @TableField("refundStatus")
    private String refundStatus;

    @TableField("gmtCreate")
    private LocalDateTime gmtCreate;

    @TableField("gmtModified")
    private LocalDateTime gmtModified;


}
