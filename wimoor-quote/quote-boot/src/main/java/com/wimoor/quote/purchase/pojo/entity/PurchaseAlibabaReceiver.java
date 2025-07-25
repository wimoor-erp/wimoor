package com.wimoor.quote.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

/**
 * 
 * @TableName t_purchase_alibaba_receiver
 */
@TableName(value ="t_purchase_alibaba_receiver")
@Data
public class PurchaseAlibabaReceiver {
    /**
     * 
     */
    @TableId
    private BigInteger orderid;

    /**
     * 
     */
    private String area;

    /**
     * 
     */
    private String division;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String mobile;

    /**
     * 
     */
    private String post;

    /**
     * 
     */
    private String town;
}