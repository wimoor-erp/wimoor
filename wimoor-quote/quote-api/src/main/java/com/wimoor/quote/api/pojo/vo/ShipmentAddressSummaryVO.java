package com.wimoor.quote.api.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.quote.api.entity.QuotationPrice;
import com.wimoor.quote.api.entity.Shipment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@ApiModel(value="t_order_shipment_destination_address对象", description="地址表")
@TableName("t_shipment_destination_address")
public class ShipmentAddressSummaryVO {

    @ApiModelProperty(value = "code")
    @TableId(value =  "code")
    private String  code;


    @ApiModelProperty(value = "destinationType")
    @TableField(value =  "destinationType")
    private String  destinationType;

    @ApiModelProperty(value = "名称")
    @TableField(value =  "name")
    private String  name;


    @ApiModelProperty(value = "地址1")
    @TableField(value =  "addressLine1")
    private String  addressLine1;

    @ApiModelProperty(value = "地址2")
    @TableField(value =  "addressLine2")
    private String  addressLine2;


    @ApiModelProperty(value = "城市")
    @TableField(value =  "city")
    private String  city;

    @ApiModelProperty(value = "公司名")
    @TableField(value =  "companyName")
    private String  companyName;

    @ApiModelProperty(value = "手机号码")
    @TableField(value =  "phoneNumber")
    private String  phoneNumber;

    @ApiModelProperty(value = "国家编码")
    @TableField(value =  "countryCode")
    private String  countryCode;

    @ApiModelProperty(value = "邮箱")
    @TableField(value =  "email")
    private String  email;

    @ApiModelProperty(value = "街道")
    @TableField(value =  "stateOrProvinceCode")
    private String  stateOrProvinceCode;

    @ApiModelProperty(value = "邮政编码")
    @TableField(value =  "postalCode")
    private String  postalCode;

    @ApiModelProperty(value = "操作时间")
    @TableField(value =  "opttime")
    private Date opttime;

    @ApiModelProperty(value = "重量")
    @TableField(exist = false)
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    @TableField(exist = false)
    private BigDecimal volume;

    @ApiModelProperty(value = "计算重量")
    @TableField(exist = false)
    private BigDecimal calweight;

    @ApiModelProperty(value = "发货sku数量")
    @TableField(exist = false)
    private Integer num;

    @ApiModelProperty(value = "货件数量")
    @TableField(exist = false)
    private Integer shipnum;

    @TableField(exist = false)
    private List<Shipment> shipmentList;


    @ApiModelProperty(value = "报价")
    @TableField(exist = false)
    private List<QuotationPrice> quotationPriceList;


}
