package com.wimoor.quote.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.quote.api.pojo.vo.ShipmentAddressSummaryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="QuoteOrderShipment对象", description="报价订单-货件")
@TableName("t_shipment")
public class Shipment {

    @ApiModelProperty(value = "买家id")
    @TableField(value =  "buyerid")
    private String buyerid;

    @ApiModelProperty(value = "number")
    @TableField(value =  "number")
    private String number;

    @ApiModelProperty(value = "报价店铺")
    @TableField(value =  "groupname")
    private String groupname;

    @ApiModelProperty(value = "货件名称")
    @TableField(value =  "name")
    private String name;

    @ApiModelProperty(value = "采购商名称")
    @TableField(value =  "buyername")
    private String buyername;

    @ApiModelProperty(value = "采购商别名")
    @TableField(value =  "buyerothername")
    private String buyerothername;


    @ApiModelProperty(value = "采购商公司名称")
    @TableField(value =  "buyercompany")
    private String buyercompany;

    @ApiModelProperty(value = "报价店铺")
    @TableField(value =  "warehousename")
    private String warehousename;

    @ApiModelProperty(value = "货件id")
    @TableId(value =  "shipmentid")
    private String shipmentid;

    @ApiModelProperty(value = "目的地")
    @TableField(value =  "destination")
    private String destination;

    @ApiModelProperty(value = "区域")
    @TableField(value =  "area")
    private String area;

    @ApiModelProperty(value = "是否偏远地区")
    @TableField(value =  "isfar")
    private Boolean isfar;

    @ApiModelProperty(value = "国家")
    @TableField(value =  "country")
    private String country;

    @ApiModelProperty(value = "重量")
    @TableField(value =  "weight")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    @TableField(value =  "volume")
    private BigDecimal volume;

    @ApiModelProperty(value = "计算重量")
    @TableField(exist = false)
    private BigDecimal calweight;

    @ApiModelProperty(value = "最小费用")
    @TableField(exist = false)
    private BigDecimal minfee;

    @ApiModelProperty(value = "发货sku数量")
    @TableField(value =  "num")
    private Integer num;

    @ApiModelProperty(value = "状态")
    @TableField(value =  "status")
    private Integer status;

    @ApiModelProperty(value = "询价备注，渠道和材质")
    @TableField(value =  "remark")
    private String remark;

    @ApiModelProperty(value = "添加时间")
    @TableField(value =  "opttime")
    private Date opttime;

    @TableField(exist = false)
    private List<ShipmentBox> boxList;

    @TableField(exist = false)
    private List<ShipmentItem> itemList;

    @TableField(exist = false)
    private List<QuoteOrder> orderList;

    @ApiModelProperty(value = "报价")
    @TableField(exist = false)
    private List<QuotationPrice> quotationPriceList;
    @TableField(exist = false)
    private ShipmentDestinationAddress address;

}
