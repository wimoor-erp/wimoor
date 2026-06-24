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
 * @TableName t_erp_ship_v2_inboundshipment_customs_xml_item
 */
@Data
@TableName(value ="t_erp_ship_v2_inboundshipment_customs_xml_item")
public class ShipInboundCustomsXmlItem implements Serializable {
    /**
     * 
     */
    @TableId(value = "guid")
    private String guid;

    /**
     * 
     */
    @TableField(value = "`sku`")
    private String sku;


    @TableField(value = "`name`")
    private String name;

    /**
     * 电子订单 CEB303,物流运单 CEB505,物流运抵单 CEB507,收款单 CEB403,出口清单 CEB603
     */
    @TableField(value = "`quantity`")
    private Integer quantity;

    /**
     * 
     */
    @TableField(value = "`price`")
    private BigDecimal price;

    /**
     * 订单金额
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

}