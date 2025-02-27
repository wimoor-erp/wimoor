package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_erp_thirdparty_inventory_ops")
public class ThirdPartyWarehouseInventoryOps {
    @TableId(value= "id")
    private String id;

    @TableField(value= "api")
    private String api;

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "sku")
    private String sku;

    @TableField(value= "cname")
    private String cname;

    @TableField(value= "ename")
    private String ename;

    @TableField(value= "housename")
    private String housename;

    @TableField(value= "houseid")
    private String houseid;

    @TableField(value= "code")
    private String code;

    @TableField(value= "quantity")
    private Integer quantity;

    @TableField(value= "weight")
    private BigDecimal weight;

    @TableField(value= "height")
    private BigDecimal height;

    @TableField(value= "length")
    private BigDecimal length;

    @TableField(value= "width")
    private BigDecimal width;

    @TableField(value= "vol")
    private BigDecimal vol;

    @TableField(value= "packagetype")
    private String packagetype;

    @TableField(value= "mark")
    private String mark;

    @TableField(value= "refreshtime")
    private Date refreshtime;

}