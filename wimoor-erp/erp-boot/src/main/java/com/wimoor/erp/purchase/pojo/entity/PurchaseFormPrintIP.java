package com.wimoor.erp.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_erp_purchase_form_print_ip")
public class PurchaseFormPrintIP{
    @TableId(value= "id")
    private String id;

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "ftype")
    private Integer ftype;

    @TableField(value= "ip")
    private String ip;

    @TableField(value= "addressid")
    private String addressid;

    @TableField(value= "paper")
    private String paper;

    @TableField(value= "operator")
    private String operator;

    @TableField(value= "opttime")
    private Date opttime;
}
