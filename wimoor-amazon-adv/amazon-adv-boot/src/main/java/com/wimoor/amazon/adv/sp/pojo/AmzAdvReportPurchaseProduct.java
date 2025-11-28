package com.wimoor.amazon.adv.sp.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "t_amz_adv_rpt2_sp_purchaseproduct")
public class AmzAdvReportPurchaseProduct {

    @Id
    @Column(name = "keywordId")
    private BigInteger keywordId;

    @Column(name = "adGroupId")
    private BigInteger adGroupId;

    @Column(name = "campaignId")
    private BigInteger campaignId;

    @Column(name = "bydate")
    private Date bydate;

    @Column(name = "addToList")
    private Integer addToList;

    @Column(name = "addToListFromClicks")
    private Integer addToListFromClicks;

    @Column(name = "qualifiedBorrows")
    private Integer qualifiedBorrows;

    @Column(name = "qualifiedBorrowsFromClicks")
    private Integer qualifiedBorrowsFromClicks;

    @Column(name = "royaltyQualifiedBorrows")
    private Integer royaltyQualifiedBorrows;

    @Column(name = "royaltyQualifiedBorrowsFromClicks")
    private Integer royaltyQualifiedBorrowsFromClicks;

    @Column(name = "portfolioId")
    private BigInteger portfolioId;

    @Column(name = "campaignName")
    private String campaignName;

    @Column(name = "adGroupName")
    private String adGroupName;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "keywordType")
    private String keywordType;

    @Column(name = "advertisedAsin")
    private String advertisedAsin;

    @Column(name = "purchasedAsin")
    private String purchasedAsin;

    @Column(name = "advertisedSku")
    private String advertisedSku;

    @Column(name = "campaignBudgetCurrencyCode")
    private String campaignBudgetCurrencyCode;

    @Column(name = "matchType")
    private String matchType;

    @Column(name = "unitsSoldClicks1d")
    private Integer unitsSoldClicks1d;

    @Column(name = "unitsSoldClicks7d")
    private Integer unitsSoldClicks7d;

    @Column(name = "unitsSoldClicks14d")
    private Integer unitsSoldClicks14d;

    @Column(name = "unitsSoldClicks30d")
    private Integer unitsSoldClicks30d;

    @Column(name = "sales1d")
    private BigDecimal sales1d;

    @Column(name = "sales7d")
    private BigDecimal sales7d;

    @Column(name = "sales14d")
    private BigDecimal sales14d;

    @Column(name = "sales30d")
    private BigDecimal sales30d;

    @Column(name = "purchases1d")
    private Integer purchases1d;

    @Column(name = "purchases7d")
    private Integer purchases7d;

    @Column(name = "purchases14d")
    private Integer purchases14d;

    @Column(name = "purchases30d")
    private Integer purchases30d;

    @Column(name = "unitsSoldOtherSku1d")
    private Integer unitsSoldOtherSku1d;

    @Column(name = "unitsSoldOtherSku7d")
    private Integer unitsSoldOtherSku7d;

    @Column(name = "unitsSoldOtherSku14d")
    private Integer unitsSoldOtherSku14d;

    @Column(name = "unitsSoldOtherSku30d")
    private Integer unitsSoldOtherSku30d;

    @Column(name = "salesOtherSku1d")
    private BigDecimal salesOtherSku1d;

    @Column(name = "salesOtherSku7d")
    private BigDecimal salesOtherSku7d;

    @Column(name = "salesOtherSku14d")
    private BigDecimal salesOtherSku14d;

    @Column(name = "salesOtherSku30d")
    private BigDecimal salesOtherSku30d;

    @Column(name = "purchasesOtherSku1d")
    private Integer purchasesOtherSku1d;

    @Column(name = "purchasesOtherSku7d")
    private Integer purchasesOtherSku7d;

    @Column(name = "purchasesOtherSku14d")
    private Integer purchasesOtherSku14d;

    @Column(name = "purchasesOtherSku30d")
    private Integer purchasesOtherSku30d;

    @Column(name = "kindleEditionNormalizedPagesRead14d")
    private Integer kindleEditionNormalizedPagesRead14d;

    @Column(name = "kindleEditionNormalizedPagesRoyalties14d")
    private BigDecimal kindleEditionNormalizedPagesRoyalties14d;

    @Column(name = "opttime")
    private Date opttime;

}
