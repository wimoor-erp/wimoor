package com.wimoor.quote.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName t_purchase_alibaba_buyer
 */
@TableName(value ="t_purchase_alibaba_buyer")
@Data
public class PurchaseAlibabaBuyer {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    @TableField(value="buyerLoginId")
    private String buyerloginid;

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