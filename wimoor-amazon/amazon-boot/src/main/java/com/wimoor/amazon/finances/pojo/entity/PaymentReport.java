package com.wimoor.amazon.finances.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 亚马逊支付报告表
 * @TableName t_amz_payment_report
 */
@Data
@TableName(value ="t_amz_payment_report")
public class PaymentReport implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 报告类型
     */
    private String reportType;

    /**
     * 报告日期时间
     */
    private Date dateTime;

    /**
     * 结算ID
     */
    private String settlementId;

    /**
     * 交易类型
     */
    private String transactionType;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 商品SKU
     */
    private String sku;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 店铺
     */
    private String store;

    /**
     * 履约方式
     */
    private String fulfillment;

    /**
     * 订单城市
     */
    private String orderCity;

    /**
     * 订单州/省
     */
    private String orderState;

    /**
     * 订单邮政编码
     */
    private String orderPostCode;

    /**
     * 征税模型
     */
    private String taxCollectionModel;

    /**
     * 产品销售金额
     */
    private BigDecimal productSales;

    /**
     * 产品销售税
     */
    private BigDecimal productSalesTax;

    /**
     * 运费抵扣
     */
    private BigDecimal shippingCredits;

    /**
     * 运费抵扣税
     */
    private BigDecimal shippingCreditsTax;

    /**
     * 礼品包装抵扣
     */
    private BigDecimal giftWrapCredits;

    /**
     * 礼品包装抵扣税
     */
    private BigDecimal giftWrapCreditsTax;

    /**
     * 监管费
     */
    private BigDecimal regulatoryFee;

    /**
     * 监管费税费
     */
    private BigDecimal taxOnRegulatoryFee;

    /**
     * 促销返利
     */
    private BigDecimal promotionalRebate;

    /**
     * 促销返利税
     */
    private BigDecimal promotionalRebateTax;

    /**
     * 市场预扣税
     */
    private BigDecimal marketplaceWithheldTax;

    /**
     * 销售费用
     */
    private BigDecimal sellingFees;

    /**
     * FBA费用
     */
    private BigDecimal fbaFees;

    /**
     * 其他交易费用
     */
    private BigDecimal otherTransactionFees;

    /**
     * 其他费用
     */
    private BigDecimal otherFees;

    /**
     * 总计金额
     */
    private BigDecimal total;

    /**
     * 税费总额
     */
    private BigDecimal taxAmount;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date opttime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}