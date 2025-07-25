package com.wimoor.quote.purchase.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_purchase_order_entry
 */
@TableName(value ="t_purchase_order_entry")
@Data
public class PurchaseOrderEntry {
    /**
     * 
     */
    @TableId
    private BigInteger id;

    private String orderid;
    /**
     * 
     */
    private String number;

    /**
     * 
     */
    private BigInteger materialid;

    /**
     * 
     */
    private BigDecimal itemprice;

    /**
     * 
     */
    private BigDecimal orderprice;

    /**
     * 
     */
    private String sku;

    /**
     * 
     */
    private String image;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String warehousename;

    private String suppliername;
    /**
     * 
     */
    private BigDecimal length;

    /**
     * 
     */
    private BigDecimal width;

    /**
     * 
     */
    private BigDecimal height;

    /**
     * 
     */
    private BigDecimal weight;

    /**
     * 
     */
    private Date audittime;
}