package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_erp_thirdparty_inventory_yc")
public class ThirdPartyWarehouseInventoryYC {
    @TableId(value= "id")
    private String id;

    @TableField(value= "api")
    private String api;

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "sku")
    private String sku;

    @TableField(value= "name")
    private String name;

    @TableField(value= "housename")
    private String housename;

    @TableField(value= "houseid")
    private String houseid;

    @TableField(value= "code")
    private String code;

    @TableField(value= "barcode")
    private String barcode;

    @TableField(value= "quality")
    private String quality;

    @TableField(value= "quantity")
    private Integer quantity;

    @TableField(value= "inspection")
    private Integer inspection;

    @TableField(value= "available")
    private Integer available;

    @TableField(value= "processing")
    private Integer processing;

    @TableField(value= "transportation")
    private Integer transportation;

    @TableField(value= "refreshtime")
    private Date refreshtime;

}