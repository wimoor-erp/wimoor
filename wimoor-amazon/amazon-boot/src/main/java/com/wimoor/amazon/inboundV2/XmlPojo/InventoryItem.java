package com.wimoor.amazon.inboundV2.XmlPojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 清单商品项
 */
@Data
public  class InventoryItem {
    private int gnum;           // 商品序号
    private String itemNo;      // 企业商品编号
    private String itemRecordNo;// 账册备案料号
    private String itemName;    // 企业商品名称
    private String gcode;       // 海关商品编码
    private String gname;       // 商品名称
    private String gmodel;      // 规格型号
    private String barCode;     // 条形码
    private String country;     // 最终目的国代码
    private String currency;    // 币制代码
    private BigDecimal qty;     // 申报数量
    private BigDecimal qty1;    // 法定数量
    private BigDecimal qty2;    // 第二数量
    private String unit;        // 申报计量单位
    private String unit1;       // 法定计量单位
    private String unit2;       // 第二计量单位
    private BigDecimal price;   // 单价
    private BigDecimal totalPrice; // 总价
    private String note;        // 备注

    public InventoryItem() {
    }
    // 构造函数
    public InventoryItem(int gnum, String itemNo, String gcode, String gname,
                         String country, String currency, BigDecimal qty,
                         String unit, BigDecimal price) {
        this.gnum = gnum;
        this.itemNo = itemNo;
        this.gcode = gcode;
        this.gname = gname;
        this.country = country;
        this.currency = currency;
        this.qty = qty;
        this.qty1 = qty; // 默认相同
        this.unit = unit;
        this.unit1 = unit; // 默认相同
        this.price = price;
        this.totalPrice = qty.multiply(price);
    }

    // ... getter/setter
}
