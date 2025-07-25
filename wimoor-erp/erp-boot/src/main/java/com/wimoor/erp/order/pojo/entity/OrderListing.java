package com.wimoor.erp.order.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="t_erp_order_list", description="列表")
@TableName("t_erp_order_listing")
public class OrderListing extends BaseEntity {

    @TableField("shopid")
    String shopid;

    @TableField("warehouseid")
    String warehouseid;

    @TableField("country")
    String country;

    @TableField("sku")
    String sku;

    @TableField("name")
    String name;

    @TableField("ename")
    String ename;

    @TableField("msalesavg")
    Integer msalesavg;

    @TableField("salesavg")
    Integer salesavg;

    @TableField("sales7")
    Integer sales7;

    @TableField("sales15")
    Integer sales15;

    @TableField("sales30")
    Integer sales30;

    @TableField("qty")
    Integer qty;

    @TableField("shipqty")
    Integer shipqty;

    @TableField("purchaseqty")
    Integer purchaseqty;

    @TableField("msku")
    String msku;

}
