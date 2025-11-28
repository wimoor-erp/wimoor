package com.wimoor.quote.ship.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.quote.ship.pojo.vo.ShipmentAddressSummaryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="QuoteOrder对象", description="报价订单")
@EqualsAndHashCode(callSuper = true)
@TableName("t_order")
public class QuoteOrder extends BaseEntity{

    @ApiModelProperty(value = "买家id")
    @TableField(value =  "buyerid")
    private String buyerid;

    @ApiModelProperty(value = "订单类型")
    @TableField(value =  "ftype")
    private Integer ftype;

    @ApiModelProperty(value = "是否拼团")
    @TableField(value =  "isgroupbuy")
    private Boolean isgroupbuy;

    @ApiModelProperty(value = "是否竞价")
    @TableField(value =  "isbidding")
    private Boolean isbidding;

    @ApiModelProperty(value = "订单状态")
    @TableField(value =  "status")
    private Integer status;

    @ApiModelProperty(value = "订单重量")
    @TableField(value =  "weight")
    private BigDecimal weight;

    @ApiModelProperty(value = "订单体积")
    @TableField(value =  "volume")
    private BigDecimal volume;

    @ApiModelProperty(value = "订单重量")
    @TableField(value =  "weight")
    private BigDecimal calweight;

    @ApiModelProperty(value = "订单关闭时间")
    @TableField(value =  "closetime")
    private Date closetime;

    @ApiModelProperty(value = "报价接受时间")
    @TableField(value =  "pricetime")
    private Date pricetime;

    @ApiModelProperty(value = "创建时间")
    @TableField(value =  "createtime")
    private Date createtime;

    @ApiModelProperty(value = "remark")
    @TableField(value =  "remark")
    private String remark;

    @ApiModelProperty(value = "number")
    @TableField(value =  "number")
    private String number;

    @ApiModelProperty(value = "transchannel")
    @TableField(value =  "transchannel")
    private String transchannel;

    @ApiModelProperty(value = "材积基数")
    @TableField(exist = false)
    private BigDecimal base;

    @ApiModelProperty(value = "到货时效")
    @TableField(value =  "days")
    private Integer days;

    @ApiModelProperty(value = "最低费用")
    @TableField(exist = false)
    private BigDecimal minfee;

    @ApiModelProperty(value = "次低费用")
    @TableField(exist = false)
    private BigDecimal minfeeSecond;

    @ApiModelProperty(value = "货件ID")
    @TableField(exist = false)
    private List<String> shipmentids;

    @ApiModelProperty(value = "shipmentTranschannel")
    @TableField(exist = false)
    private ShipmentTranschannel shipmentTranschannel;

    @ApiModelProperty(value = "货件")
    @TableField(exist = false)
    private List<Shipment> shipmentList;

    @ApiModelProperty(value = "地址")
    @TableField(exist = false)
    private List<ShipmentAddressSummaryVO> addressList;

    @ApiModelProperty(value = "报价")
    @TableField(exist = false)
    private List<QuotationPrice> quotationPriceList;
}
