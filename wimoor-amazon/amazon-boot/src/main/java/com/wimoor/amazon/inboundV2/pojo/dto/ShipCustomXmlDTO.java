package com.wimoor.amazon.inboundV2.pojo.dto;

import com.wimoor.amazon.inboundV2.XmlPojo.CustomsDeclaration;
import com.wimoor.amazon.inboundV2.XmlPojo.InventoryData;
import com.wimoor.amazon.inboundV2.XmlPojo.OrderData;
import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipCustomXmlDTO", description="货件计划dto")
public class ShipCustomXmlDTO extends BasePageQuery{
    
    @ApiModelProperty(value = "唯一标识")
    private String guid;

    @ApiModelProperty(value = "公司ID")
    private String shopid;

    @ApiModelProperty(value = "编号")
    private String number;

    @ApiModelProperty(value = "XML类型：电子订单 CEB303,物流运单 CEB505,物流运抵单 CEB507,收款单 CEB403,出口清单 CEB603")
    private String xmlType;

    @ApiModelProperty(value = "分组ID")
    private BigInteger groupid;

    @ApiModelProperty(value = "站点ID")
    private String marketplaceid;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "订单商品数量")
    private Integer totalQuantity;

    @ApiModelProperty(value = "订单商品净重")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "订单商品毛重")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "申请时间")
    private Date appTime;

    @ApiModelProperty(value = "申请类型")
    private String appType;

    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "申请状态")
    private String appStatus;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "返回状态")
    private String returnStatus;

    @ApiModelProperty(value = "操作时间")
    private Date opttime;

    @ApiModelProperty(value = "操作人")
    private BigInteger operator;

    @ApiModelProperty(value = "开始时间")
    private String fromDate;

    @ApiModelProperty(value = "结束时间")
    private String toDate;

    @ApiModelProperty(value = "日期类型：app_time(申报时间), opttime(操作时间)")
    private String dateType;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "订单数据")
    private OrderData orderData;

    @ApiModelProperty(value = "库存数据")
    private InventoryData inventoryData;

    @ApiModelProperty(value = "报关数据")
    private CustomsDeclaration customsDeclaration;

    @ApiModelProperty(value = "操作类型")
    private String optType;
    public ShipCustomXmlDTO getDto() {
        return this;
    }

    private static final long serialVersionUID = 1L;
}