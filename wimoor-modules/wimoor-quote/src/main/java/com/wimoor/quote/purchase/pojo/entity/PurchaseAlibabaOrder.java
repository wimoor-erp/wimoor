package com.wimoor.quote.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_purchase_alibaba_order
 */
@TableName(value ="t_purchase_alibaba_order")
@Data
public class PurchaseAlibabaOrder {
    /**
     * 
     */
    @TableId
    private BigInteger id;

    /**
     * 
     */
    @TableField(value="sellerID")
    private String sellerid;
    /**
     *
     */
    @TableField(value="buyerID")
    private String buyerid;

    /**
     *
     */
    @TableField(value="buyername")
    private String buyername;


    /**
     *
     */
    @TableField(value="buyercompany")
    private String buyercompany;

    /**
     *
     */
    @TableField(value="buyerothername")
    private String buyerothername;


    /**
     * 
     */
    @TableField(value="price")
    private BigDecimal price;

    /**
     * 
     */
    @TableField(value="remark")
    private String remark;

    @TableField(value="sysbuyerid")
    private String sysbuyerid;

    /**
     *
     */
    @TableField(value="createtime")
    private Date createtime;
}