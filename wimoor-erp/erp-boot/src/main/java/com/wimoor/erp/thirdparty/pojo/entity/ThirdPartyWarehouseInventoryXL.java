package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_erp_thirdparty_inventory_xl")
public class ThirdPartyWarehouseInventoryXL {
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

    @TableField(value= "fba_quantity")
    private Integer fbaQuantity;

    @TableField(value= "fba_available")
    private Integer fbaAvailable;

    @TableField(value= "fba_lock")
    private Integer fbaLock;

    @TableField(value= "fba_transport")
    private Integer fbaTransport;

    @TableField(value= "stock_quantity")
    private Integer stockQuantity;

    @TableField(value= "stock_available")
    private Integer stockAvailable;

    @TableField(value= "stock_lock")
    private Integer stockLock;

    @TableField(value= "stock_transport")
    private Integer stockTransport;

    @TableField(value= "price")
    private BigDecimal price;

    @TableField(value= "packagetype")
    private String packagetype;

    @TableField(value= "customer_code")
    private String customerCode;

    @TableField(value= "refreshtime")
    private Date refreshtime;

}