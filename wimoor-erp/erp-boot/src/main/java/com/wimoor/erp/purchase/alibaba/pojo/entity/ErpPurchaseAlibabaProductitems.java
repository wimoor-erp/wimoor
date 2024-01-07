package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

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
@TableName("t_erp_purchase_alibaba_productitems")
@ApiModel(value="ErpPurchaseAlibabaProductitems对象", description="")
public class ErpPurchaseAlibabaProductitems implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField("specId")
    private String specId;

    @TableId("productID")
    private BigInteger productID;
    
    @TableField("offerid")
    private String offerid ;
    
    @TableField("skuInfos")
    private String skuInfos;

    @TableField("price")
    private BigDecimal price;

    @TableField("name")
    private String name;
    
    @TableField("productSnapshotUrl")
    private String productSnapshotUrl;

    @TableField("productImgUrl")
    private String productImgUrl;

    @TableField("unit")
    private String unit;


}
