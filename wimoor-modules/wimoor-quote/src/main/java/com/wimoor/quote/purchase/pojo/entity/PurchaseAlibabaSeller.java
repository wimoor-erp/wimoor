package com.wimoor.quote.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName t_purchase_alibaba_seller
 */
@TableName(value ="t_purchase_alibaba_seller")
@Data
public class PurchaseAlibabaSeller {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    @TableField(value="sellerLoginId")
    private String sellerloginid;

    /**
     * 
     */
    private String phone;

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
    private String loginname;

    /**
     * 
     */
    @TableField(value="shopName")
    private String shopname;

    /**
     * 
     */
    @TableField(value="companyName")
    private String companyname;

    /**
     * 
     */
    private String email;
}