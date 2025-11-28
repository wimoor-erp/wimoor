package com.wimoor.quote.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @TableName t_purchase_alibaba_order_item
 */
@TableName(value ="t_purchase_alibaba_order_item")
@Data
public class PurchaseAlibabaOrderItem {
    /**
     * 
     */
    @TableId
    private BigInteger id;

    /**
     * 
     */
    private BigInteger orderid;

    /**
     * 
     */
    @TableField(value="skuID")
    private BigInteger skuid;

    /**
     * 
     */
    @TableField(value="productID")
    private BigInteger productid;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    @TableField(value="productCargoNumber")
    private String productcargonumber;

    /**
     * 
     */
    private String image;

    /**
     * 
     */
    private Integer quantity;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    @TableField(value="statusStr")
    private String statusstr;

    /**
     * 
     */
    private BigDecimal price;

    /**
     * 
     */
    private BigDecimal amount;
}