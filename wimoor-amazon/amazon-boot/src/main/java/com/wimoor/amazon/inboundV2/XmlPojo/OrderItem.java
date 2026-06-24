package com.wimoor.amazon.inboundV2.XmlPojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 订单商品项
 */
@Data
public class OrderItem implements Serializable {

    private int gnum;           // 序号
    private String itemNo;      // 企业商品货号
    private String itemName;    // 企业商品名称
    private String itemDescribe;// 企业商品描述
    private String barCode;     // 条形码
    private String unit;        // 计量单位
    private String currency;    // 币制
    private BigDecimal qty;     // 数量
    private BigDecimal price;   // 单价
    private BigDecimal totalPrice; // 总价
    private String note;        // 备注
    private String image;
    private Map<String, String> itemInfoExt;
    public OrderItem() {
    }
    // 构造函数、getter和setter省略
    public OrderItem(int gnum, String itemNo, String itemName, String unit,
                     String currency, BigDecimal qty, BigDecimal price,String image) {
        this.gnum = gnum;
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.unit = unit;
        this.currency = currency;
        this.qty = qty;
        this.price = price;
        this.totalPrice = qty.multiply(price);
        this.note=itemNo;
        this.image=image;
    }

    // ... 其他getter/setter
}
