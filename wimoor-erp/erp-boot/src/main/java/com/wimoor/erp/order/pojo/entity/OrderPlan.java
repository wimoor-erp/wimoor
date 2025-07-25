package com.wimoor.erp.order.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value="OrderPlan", description="计划")
@TableName("t_erp_order_plan")
public class OrderPlan {

    @TableField(value = "shopid")
    private String shopid;
    @TableField(value = "sku")
    private String sku;
    @TableField(value = "warehouseid")
    private String warehouseid;
    @TableField(value = "operator")
    private String operator;
    @TableField(value = "opttime")
    private Date opttime;
    @TableField(value = "ftype")
    private Integer ftype;
    @TableField(value = "quantity")
    private Integer quantity;

}
