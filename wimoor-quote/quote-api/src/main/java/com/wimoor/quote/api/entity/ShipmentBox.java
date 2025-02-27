package com.wimoor.quote.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value="QuoteOrderShipment对象", description="报价订单-货件")
@TableName("t_shipment_box")
public class ShipmentBox {

    @ApiModelProperty(value = "货件id")
    @TableField(value =  "shipmentid")
    private String shipmentid;

    @ApiModelProperty(value = "箱子id")
    @TableId(value =  "boxid")
    private String boxid;

    @ApiModelProperty(value = "尺寸单位")
    @TableField(value =  "unit")
    private String unit;

    @ApiModelProperty(value = "长")
    @TableField(value =  "length")
    private BigDecimal length;

    @ApiModelProperty(value = "宽")
    @TableField(value =  "width")
    private BigDecimal width;

    @ApiModelProperty(value = "高")
    @TableField(value =  "height")
    private BigDecimal height;

    @ApiModelProperty(value = "重量")
    @TableField(value =  "weight")
    private BigDecimal weight;

    @ApiModelProperty(value = "重量单位")
    @TableField(value =  "wunit")
    private String wunit;

    @ApiModelProperty(value = "操作时间")
    @TableField(value =  "opttime")
    private Date opttime;

    @ApiModelProperty(value = "操作人")
    @TableField(value =  "operator")
    private String operator;


    @ApiModelProperty(value = "材积重量")
    @TableField(exist = false)
    private BigDecimal dimweight;

    @ApiModelProperty(value = "计算重量")
    @TableField(exist = false)
    private BigDecimal calweight;
}
