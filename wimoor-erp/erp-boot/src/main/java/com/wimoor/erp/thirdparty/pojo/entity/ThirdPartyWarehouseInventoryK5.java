package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_erp_thirdparty_inventory_k5")
public class ThirdPartyWarehouseInventoryK5 {
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

    @TableField(value= "refreshtime")
    private Date refreshtime;

    @TableField(value= "locknum")
    private Integer locknum;

    @TableField(value= "num")
    private Integer num;

    @TableField(value= "outnum")
    private Integer outnum;
}