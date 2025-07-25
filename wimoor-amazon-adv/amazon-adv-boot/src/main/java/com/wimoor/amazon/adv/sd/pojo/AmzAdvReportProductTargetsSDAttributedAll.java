package com.wimoor.amazon.adv.sd.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 
 * @TableName t_amz_adv_rpt2_sd_product_targets_attributed_all
 */
@TableName(value ="t_amz_adv_rpt2_sd_product_targets_attributed_all")
@Data
public class AmzAdvReportProductTargetsSDAttributedAll implements Serializable {
    /**
     * 
     */
    @TableId(value = "targetingId")
    private String targetingId;

    /**
     * 
     */
    @TableField(value = "bydate")
    private Date bydate;

    /**
     * 
     */
    @TableField(value = "addToCart")
    private Integer addToCart;

    /**
     * 
     */
    @TableField(value = "addToCartClicks")
    private Integer addToCartClicks;

    /**
     * 
     */
    @TableField(value = "addToCartRate")
    private BigDecimal addToCartRate;

    /**
     * 
     */
    @TableField(value = "addToCartViews")
    private Integer addToCartViews;

    /**
     * 
     */
    @TableField(value = "addToList")
    private Integer addToList;

    /**
     * 
     */
    @TableField(value = "addToListFromClicks")
    private Integer addToListFromClicks;

    /**
     * 
     */
    @TableField(value = "addToListFromViews")
    private Integer addToListFromViews;

    /**
     * 
     */
    @TableField(value = "qualifiedBorrows")
    private Integer qualifiedBorrows;

    /**
     * 
     */
    @TableField(value = "qualifiedBorrowsFromClicks")
    private Integer qualifiedBorrowsFromClicks;

    /**
     * 
     */
    @TableField(value = "qualifiedBorrowsFromViews")
    private Integer qualifiedBorrowsFromViews;

    /**
     * 
     */
    @TableField(value = "royaltyQualifiedBorrows")
    private Integer royaltyQualifiedBorrows;

    /**
     * 
     */
    @TableField(value = "royaltyQualifiedBorrowsFromClicks")
    private Integer royaltyQualifiedBorrowsFromClicks;

    /**
     * 
     */
    @TableField(value = "royaltyQualifiedBorrowsFromViews")
    private Integer royaltyQualifiedBorrowsFromViews;

    /**
     * 
     */
    @TableField(value = "brandedSearches")
    private Integer brandedSearches;

    /**
     * 
     */
    @TableField(value = "brandedSearchesClicks")
    private Integer brandedSearchesClicks;

    /**
     * 
     */
    @TableField(value = "brandedSearchesViews")
    private Integer brandedSearchesViews;

    /**
     * 
     */
    @TableField(value = "brandedSearchRate")
    private BigDecimal brandedSearchRate;

    /**
     * 
     */
    @TableField(value = "campaignBudgetCurrencyCode")
    private String campaignBudgetCurrencyCode;

    /**
     * 
     */
    @TableField(value = "detailPageViews")
    private Integer detailPageViews;

    /**
     * 
     */
    @TableField(value = "detailPageViewsClicks")
    private Integer detailPageViewsClicks;

    /**
     * 
     */
    @TableField(value = "eCPAddToCart")
    private BigDecimal eCPAddToCart;

    /**
     * 
     */
    @TableField(value = "eCPBrandSearch")
    private BigDecimal eCPBrandSearch;

    /**
     * 
     */
    @TableField(value = "impressionsViews")
    private Integer impressionsViews;

    /**
     * 
     */
    @TableField(value = "newToBrandDetailPageViewRate")
    private BigDecimal newToBrandDetailPageViewRate;

    /**
     * 
     */
    @TableField(value = "newToBrandDetailPageViews")
    private Integer newToBrandDetailPageViews;

    /**
     * 
     */
    @TableField(value = "newToBrandDetailPageViewsClicks")
    private Integer newToBrandDetailPageViewsClicks;

    /**
     * 
     */
    @TableField(value = "newToBrandECPDetailPageView")
    private Integer newToBrandECPDetailPageView;

    /**
     * 
     */
    @TableField(value = "newToBrandPurchasesPercentage")
    private BigDecimal newToBrandPurchasesPercentage;

    /**
     * 
     */
    @TableField(value = "newToBrandPurchases")
    private Integer newToBrandPurchases;

    /**
     * 
     */
    @TableField(value = "newToBrandPurchasesClicks")
    private Integer newToBrandPurchasesClicks;

    /**
     * 
     */
    @TableField(value = "newToBrandPurchasesRate")
    private BigDecimal newToBrandPurchasesRate;

    /**
     * 
     */
    @TableField(value = "newToBrandSales")
    private BigDecimal newToBrandSales;

    /**
     * 
     */
    @TableField(value = "newToBrandSalesClicks")
    private Integer newToBrandSalesClicks;

    /**
     * 
     */
    @TableField(value = "newToBrandSalesPercentage")
    private BigDecimal newToBrandSalesPercentage;

    /**
     * 
     */
    @TableField(value = "newToBrandUnitsSold")
    private Integer newToBrandUnitsSold;

    /**
     * 
     */
    @TableField(value = "newToBrandUnitsSoldClicks")
    private Integer newToBrandUnitsSoldClicks;

    /**
     * 
     */
    @TableField(value = "newToBrandUnitsSoldPercentage")
    private BigDecimal newToBrandUnitsSoldPercentage;

    /**
     * attributedConversions14d
     */
    @TableField(value = "purchases")
    private Integer purchases;

    /**
     * 
     */
    @TableField(value = "purchasesClicks")
    private Integer purchasesClicks;

    /**
     * 
     */
    @TableField(value = "purchasesPromoted")
    private Integer purchasesPromoted;

    /**
     * 
     */
    @TableField(value = "purchasesPromotedClicks")
    private Integer purchasesPromotedClicks;

    /**
     * attributedSales14d
     */
    @TableField(value = "sales")
    private BigDecimal sales;

    /**
     * 
     */
    @TableField(value = "salesClicks")
    private BigDecimal salesClicks;

    /**
     * 
     */
    @TableField(value = "salesPromoted")
    private BigDecimal salesPromoted;

    /**
     * 
     */
    @TableField(value = "salesPromotedClicks")
    private BigDecimal salesPromotedClicks;

    /**
     * attributedUnitsOrdered1d
     */
    @TableField(value = "unitsSold")
    private Integer unitsSold;

    /**
     * 
     */
    @TableField(value = "unitsSoldClicks")
    private Integer unitsSoldClicks;

    /**
     * 
     */
    @TableField(value = "video5SecondViewRate")
    private BigDecimal video5SecondViewRate;

    /**
     * 
     */
    @TableField(value = "video5SecondViews")
    private Integer video5SecondViews;

    /**
     * 
     */
    @TableField(value = "videoCompleteViews")
    private Integer videoCompleteViews;

    /**
     * 
     */
    @TableField(value = "videoFirstQuartileViews")
    private Integer videoFirstQuartileViews;

    /**
     * 
     */
    @TableField(value = "videoMidpointViews")
    private Integer videoMidpointViews;

    /**
     * 
     */
    @TableField(value = "videoThirdQuartileViews")
    private Integer videoThirdQuartileViews;

    /**
     * 
     */
    @TableField(value = "videoUnmutes")
    private Integer videoUnmutes;

    /**
     * 
     */
    @TableField(value = "viewabilityRate")
    private BigDecimal viewabilityRate;

    /**
     * 
     */
    @TableField(value = "viewClickThroughRate")
    private BigDecimal viewClickThroughRate;

    /**
     * 
     */
    @TableField(value = "opttime")
    private Date opttime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public boolean isZero() {
        return (addToCart == null || addToCart == 0) &&
                (addToCartClicks == null || addToCartClicks == 0) &&
                (addToCartRate == null || addToCartRate.floatValue() < 0.001f) &&
                (addToCartViews == null || addToCartViews == 0) &&
                (addToList == null || addToList == 0) &&
                (addToListFromClicks == null || addToListFromClicks == 0) &&
                (addToListFromViews == null || addToListFromViews == 0) &&
                (qualifiedBorrows == null || qualifiedBorrows == 0) &&
                (qualifiedBorrowsFromClicks == null || qualifiedBorrowsFromClicks == 0) &&
                (qualifiedBorrowsFromViews == null || qualifiedBorrowsFromViews == 0) &&
                (royaltyQualifiedBorrows == null || royaltyQualifiedBorrows == 0) &&
                (royaltyQualifiedBorrowsFromClicks == null || royaltyQualifiedBorrowsFromClicks == 0) &&
                (royaltyQualifiedBorrowsFromViews == null || royaltyQualifiedBorrowsFromViews == 0) &&
                (brandedSearches == null || brandedSearches == 0) &&
                (brandedSearchesClicks == null || brandedSearchesClicks == 0) &&
                (brandedSearchesViews == null || brandedSearchesViews == 0) &&
                (brandedSearchRate == null || brandedSearchRate.floatValue() < 0.001f) &&
                (detailPageViews == null || detailPageViews == 0) &&
                (detailPageViewsClicks == null || detailPageViewsClicks == 0) &&
                (eCPAddToCart == null || eCPAddToCart.floatValue() < 0.001f) &&
                (eCPBrandSearch == null || eCPBrandSearch.floatValue() < 0.001f) &&
                (impressionsViews == null || impressionsViews == 0) &&
                (newToBrandDetailPageViewRate == null || newToBrandDetailPageViewRate.floatValue() < 0.001f) &&
                (newToBrandDetailPageViews == null || newToBrandDetailPageViews == 0) &&
                (newToBrandDetailPageViewsClicks == null || newToBrandDetailPageViewsClicks == 0) &&
                (newToBrandECPDetailPageView == null || newToBrandECPDetailPageView == 0) &&
                (newToBrandPurchasesPercentage == null || newToBrandPurchasesPercentage.floatValue() < 0.001f) &&
                (newToBrandPurchases == null || newToBrandPurchases == 0) &&
                (newToBrandPurchasesClicks == null || newToBrandPurchasesClicks == 0) &&
                (newToBrandPurchasesRate == null || newToBrandPurchasesRate.floatValue() < 0.001f) &&
                (newToBrandSales == null || newToBrandSales.floatValue() < 0.001f) &&
                (newToBrandSalesClicks == null || newToBrandSalesClicks == 0) &&
                (newToBrandSalesPercentage == null || newToBrandSalesPercentage.floatValue() < 0.001f) &&
                (newToBrandUnitsSold == null || newToBrandUnitsSold == 0) &&
                (newToBrandUnitsSoldClicks == null || newToBrandUnitsSoldClicks == 0) &&
                (newToBrandUnitsSoldPercentage == null || newToBrandUnitsSoldPercentage.floatValue() < 0.001f) &&
                (purchases == null || purchases == 0) &&
                (purchasesClicks == null || purchasesClicks == 0) &&
                (purchasesPromoted == null || purchasesPromoted == 0) &&
                (purchasesPromotedClicks == null || purchasesPromotedClicks == 0) &&
                (sales == null || sales.floatValue() < 0.001f) &&
                (salesClicks == null || salesClicks.floatValue() < 0.001f) &&
                (salesPromoted == null || salesPromoted.floatValue() < 0.001f) &&
                (salesPromotedClicks == null || salesPromotedClicks.floatValue() < 0.001f) &&
                (unitsSold == null || unitsSold == 0) &&
                (unitsSoldClicks == null || unitsSoldClicks == 0) &&
                (video5SecondViewRate == null || video5SecondViewRate.floatValue() < 0.001f) &&
                (video5SecondViews == null || video5SecondViews == 0) &&
                (videoCompleteViews == null || videoCompleteViews == 0) &&
                (videoFirstQuartileViews == null || videoFirstQuartileViews == 0) &&
                (videoMidpointViews == null || videoMidpointViews == 0) &&
                (videoThirdQuartileViews == null || videoThirdQuartileViews == 0) &&
                (videoUnmutes == null || videoUnmutes == 0) &&
                (viewabilityRate == null || viewabilityRate.floatValue() < 0.001f) &&
                (viewClickThroughRate == null || viewClickThroughRate.floatValue() < 0.001f);
    }
}