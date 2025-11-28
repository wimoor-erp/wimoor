package com.wimoor.amazon.adv.sd.pojo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@Table(name="t_amz_adv_rpt2_sd_purchaseproduct")
public class AmzAdvReportSDPurchaseProduct {

    @Column(name = "adGroupId")
    private BigInteger adGroupId;

    @Column(name = "campaignId")
    private BigInteger campaignId;

    @Column(name = "bydate")
    private Date bydate;

    @Column(name = "adGroupName")
    private String adGroupName;

    @Column(name = "asinBrandHalo")
    private String asinBrandHalo;

    @Column(name = "addToList")
    private BigDecimal addToList;

    @Column(name = "addToListFromClicks")
    private BigDecimal addToListFromClicks;

    @Column(name = "qualifiedBorrowsFromClicks")
    private BigDecimal qualifiedBorrowsFromClicks;

    @Column(name = "royaltyQualifiedBorrowsFromClicks")
    private BigDecimal royaltyQualifiedBorrowsFromClicks;

    @Column(name = "addToListFromViews")
    private BigDecimal addToListFromViews;

    @Column(name = "qualifiedBorrows")
    private BigDecimal qualifiedBorrows;

    @Column(name = "qualifiedBorrowsFromViews")
    private BigDecimal qualifiedBorrowsFromViews;

    @Column(name = "royaltyQualifiedBorrows")
    private BigDecimal royaltyQualifiedBorrows;

    @Column(name = "royaltyQualifiedBorrowsFromViews")
    private BigDecimal royaltyQualifiedBorrowsFromViews;

    @Column(name = "campaignBudgetCurrencyCode")
    private String campaignBudgetCurrencyCode;

    @Column(name = "campaignName")
    private String campaignName;

    @Column(name = "conversionsBrandHalo")
    private BigDecimal conversionsBrandHalo;

    @Column(name = "conversionsBrandHaloClicks")
    private BigDecimal conversionsBrandHaloClicks;

    @Column(name = "promotedAsin")
    private String promotedAsin;

    @Column(name = "promotedSku")
    private String promotedSku;

    @Column(name = "salesBrandHalo")
    private BigDecimal salesBrandHalo;

    @Column(name = "salesBrandHaloClicks")
    private BigDecimal salesBrandHaloClicks;

    @Column(name = "unitsSoldBrandHalo")
    private BigDecimal unitsSoldBrandHalo;

    @Column(name = "unitsSoldBrandHaloClicks")
    private BigDecimal unitsSoldBrandHaloClicks;

    @Column(name = "opttime")
    private Date opttime;
}
