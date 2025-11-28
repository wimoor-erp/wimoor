package com.wimoor.amazon.adv.sb.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Data
@Table(name = "t_amz_adv_rpt2_hsa_purchaseproduct")
public class AmzAdvReportHsaPurchaseProduct {


    @Column(name = "campaignId", nullable = false)
    private BigInteger campaignId;

    @Column(name = "adGroupId", nullable = false)
    private BigInteger adGroupId;

    @Column(name = "bydate", nullable = false)
    private Date bydate;

    @Column(name = "campaignBudgetCurrencyCode")
    private String campaignBudgetCurrencyCode;

    @Column(name = "campaignName")
    private String campaignName;

    @Column(name = "campaignPriceTypeCode")
    private String campaignPriceTypeCode;

    @Column(name = "adGroupName")
    private String adGroupName;

    @Column(name = "attributionType")
    private String attributionType;

    @Column(name = "purchasedAsin")
    private String purchasedAsin;

    @Column(name = "ordersClicks14d")
    private Integer ordersClicks14d;

    @Column(name = "productName")
    private String productName;

    @Column(name = "productCategory")
    private String productCategory;

    @Column(name = "sales14d")
    private BigDecimal sales14d;

    @Column(name = "salesClicks14d")
    private BigDecimal salesClicks14d;

    @Column(name = "orders14d")
    private Integer orders14d;

    @Column(name = "unitsSold14d")
    private Integer unitsSold14d;

    @Column(name = "newToBrandSales14d")
    private BigDecimal newToBrandSales14d;

    @Column(name = "newToBrandPurchases14d")
    private Integer newToBrandPurchases14d;

    @Column(name = "newToBrandUnitsSold14d")
    private Integer newToBrandUnitsSold14d;

    @Column(name = "newToBrandSalesPercentage14d")
    private BigDecimal newToBrandSalesPercentage14d;

    @Column(name = "newToBrandPurchasesPercentage14d")
    private BigDecimal newToBrandPurchasesPercentage14d;

    @Column(name = "newToBrandUnitsSoldPercentage14d")
    private BigDecimal newToBrandUnitsSoldPercentage14d;

    @Column(name = "unitsSoldClicks14d")
    private Integer unitsSoldClicks14d;

    @Column(name = "kindleEditionNormalizedPagesRead14d")
    private Integer kindleEditionNormalizedPagesRead14d;

    @Column(name = "kindleEditionNormalizedPagesRoyalties14d")
    private BigDecimal kindleEditionNormalizedPagesRoyalties14d;

    @Column(name = "opttime")
    private Date opttime;

}
