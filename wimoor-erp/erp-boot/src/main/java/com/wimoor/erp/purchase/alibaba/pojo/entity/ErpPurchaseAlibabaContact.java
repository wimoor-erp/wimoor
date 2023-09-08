package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.io.Serializable;

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
@TableName("t_erp_purchase_alibaba_contact")
@ApiModel(value="ErpPurchaseAlibabaContact对象", description="")
public class ErpPurchaseAlibabaContact implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @TableField("companyName")
    private String companyName;

    private String email;
    
    private String customer;
    
    @TableField("imInPlatform")
    private String imInPlatform;

    private String mobile;

    private String name;

    private String phone;


}
