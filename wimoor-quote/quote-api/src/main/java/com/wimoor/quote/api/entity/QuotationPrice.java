package com.wimoor.quote.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value="t_quotation_price对象", description="询价价格表")
@EqualsAndHashCode(callSuper = true)
@TableName("t_supplier_quotation_price")
public class QuotationPrice extends BaseEntity{

    @ApiModelProperty(value = "订单id")
    @TableField(value =  "orderid")
    private String  orderid;

    @ApiModelProperty(value = "供应商id")
    @TableField(value =  "supplierid")
    private String  supplierid;

    @ApiModelProperty(value = "地址ID")
    @TableField(value =  "destination")
    private String  destination;

    @ApiModelProperty(value = "货件ID")
    @TableField(value =  "shipmentid")
    private String  shipmentid;

    @ApiModelProperty(value = "报价类型kg,cbm")
    @TableField(value =  "ftype")
    private String  ftype;

    @ApiModelProperty(value = "单位报价")
    @TableField(value =  "unitprice")
    private BigDecimal unitprice;

    @ApiModelProperty(value = "重量")
    @TableField(value =  "weight")
    private BigDecimal weight;

    @ApiModelProperty(value = "材积基数")
    @TableField(value =  "base")
    private BigDecimal base;

    @ApiModelProperty(value = "税")
    @TableField(value =  "tax")
    private BigDecimal tax;

    @ApiModelProperty(value = "其他费用")
    @TableField(value =  "otherfee")
    private BigDecimal otherfee;

    @ApiModelProperty(value = "总价")
    @TableField(value =  "totalfee")
    private BigDecimal totalfee;

    @ApiModelProperty(value = "运输费用")
    @TableField(value =  "shipfee")
    private BigDecimal shipfee;

    @ApiModelProperty(value = "最小费用")
    @TableField(exist = false)
    private BigDecimal minfee;

    @ApiModelProperty(value = "操作时间")
    @TableField(value =  "pricetime")
    private Date pricetime;

    @ApiModelProperty(value = "操作时间")
    @TableField(value =  "opttime")
    private Date opttime;

    @ApiModelProperty(value = "选定报价")
    @TableField(value =  "confirm")
    private Boolean confirm;

    @ApiModelProperty(value = "供应商")
    @TableField(exist = false)
    private UserSupplier supplier;
}
