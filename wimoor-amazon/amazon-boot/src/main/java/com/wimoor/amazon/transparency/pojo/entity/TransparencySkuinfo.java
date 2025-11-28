package com.wimoor.amazon.transparency.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName t_amz_transparency_skuinfo
 */
@TableName(value ="t_amz_transparency_skuinfo")
@Data
public class TransparencySkuinfo {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String gtin;

    /**
     * 
     */
    private String groupid;

    /**
     * 
     */
    private String asin;

    /**
     * 
     */
    private String sku;
    private Boolean disabled;
    /**
     * 
     */
    private String authid;

    private Date createtime;
    private Date opttime;

    private String creator;
    private String operator;
}