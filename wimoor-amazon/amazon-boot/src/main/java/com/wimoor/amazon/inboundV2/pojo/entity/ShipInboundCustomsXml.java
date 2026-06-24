package com.wimoor.amazon.inboundV2.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.inboundV2.XmlPojo.CustomsDeclaration;
import com.wimoor.amazon.inboundV2.XmlPojo.InventoryData;
import com.wimoor.amazon.inboundV2.XmlPojo.OrderData;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 
 * @TableName t_erp_ship_v2_inboundshipment_customs_xml
 */
@Data
@TableName(value ="t_erp_ship_v2_inboundshipment_customs_xml")
public class ShipInboundCustomsXml implements Serializable {
    /**
     * 
     */
    @TableId(value = "guid")
    private String guid;

    /**
     * 
     */
    @TableField(value = "`number`")
    private String number;


    @TableField(value = "`order_number`")
    private String orderNumber;

    /**
     * 电子订单 CEB303,物流运单 CEB505,物流运抵单 CEB507,收款单 CEB403,出口清单 CEB603 

     */
    @TableField(value = "`xml_type`")
    private String xmlType;

    /**
     * 
     */
    @TableField(value = "`groupid`")
    private BigInteger groupid;

    /**
     * 订单金额
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 订单商品数量
     */
    @TableField(value = "total_quantity")
    private Integer totalQuantity;

    /**
     * 订单商品总重量
     */
    @TableField(value = "net_weight")
    private BigDecimal netWeight;

    /**
     * 订单商品总重量
     */
    @TableField(value = "gross_weight")
    private BigDecimal grossWeight;

    /**
     * 
     */
    @TableField(value = "app_time")
    private Date appTime;

    /**
     * 
     */
    @TableField(value = "app_type")
    private String appType;

    /**
     *
     */
    @TableField(value = "disabled")
    private Boolean disabled;

    /**
     * 
     */
    @TableField(value = "app_status")
    private String appStatus;

    /**
     * 
     */
    @TableField(value = "file_path")
    private String filePath;

    /**
     * 
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 
     */
    @TableField(value = "return_status")
    private String returnStatus;

    /**
     * 
     */
    @TableField(value = "return_time")
    private Date returnTime;

    /**
     * 
     */
    @TableField(value = "return_info")
    private String returnInfo;

    /**
     * 
     */
    @TableField(value = "opttime")
    private Date opttime;

    /**
     * 
     */
    @TableField(value = "operator")
    private BigInteger operator;

    /**
     *
     */
    @TableField(value = "content")
    private String content;

    /**
     *
     */
    @TableField(exist = false)
    private OrderData orderData;

    @TableField(exist = false)
    private InventoryData inventoryData;

    @TableField(exist = false)
    private CustomsDeclaration customsDeclaration;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String optType;


}