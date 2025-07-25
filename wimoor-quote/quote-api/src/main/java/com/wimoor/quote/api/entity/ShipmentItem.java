package com.wimoor.quote.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="QuoteOrderShipment对象", description="报价订单-货件")
@TableName("t_shipment_item")
public class ShipmentItem extends BaseEntity {

    @ApiModelProperty(value = "货件id")
    @TableField(value =  "shipmentid")
    private String shipmentid;

    @ApiModelProperty(value = "sku")
    @TableField(value =  "sku")
    private String sku;

    @ApiModelProperty(value = "名称")
    @TableField(value =  "name")
    private String name;

    @ApiModelProperty(value = "英文名称")
    @TableField(value =  "ename")
    private String ename;

    @ApiModelProperty(value = "material")
    @TableField(value =  "material")
    private String material;

    @ApiModelProperty(value = "quantity")
    @TableField(value =  "quantity")
    private Integer quantity;

    @ApiModelProperty(value = "海关编码")
    @TableField(value =  "code")
    private String code;

    @ApiModelProperty(value = "图片")
    @TableField(value =  "image")
    private String image;

}
