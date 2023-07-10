package com.wimoor.erp.ship.pojo.entity;

import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_ship_plansub_euitem")
public class ShipPlanSubEuItem {
 
    @TableId(value= "id")
    private BigInteger id;

    @TableField(value= "planid")
    private String planid;

    @TableField(value= "materialid")
    private String materialid;

    @TableField(value= "plansubid")
    private String plansubid;

    @TableField(value= "marketplaceid")
    private String marketplaceid;
    
    @TableField(value= "sku")
    private String sku;

    @TableField(value= "amount")
    private Integer amount;
 
}