package com.wimoor.amazon.adv.sb.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 亚马逊广告HSA广告活动位置归因报告
 * @TableName t_amz_adv_rpt2_hsa_campaigns_place_attributed_all
 */
@TableName(value ="t_amz_adv_rpt2_hsa_campaigns_place_attributed_all")
@Data
public class AmzAdvReportCampaignsPlaceHsaAttributedAll implements Serializable {
    /**
     * 广告活动ID
     */
    @TableId(value = "campaignId")
    private String campaignId;

    /**
     * 日期
     */
    @TableField(value = "bydate")
    private Date bydate;


    @TableField(value = "placementid")
    private Integer placementid;
    /**
     * 加入购物车次数
     */
    @TableField(value = "addToCart")
    private Integer addToCart;

    /**
     * 加入购物车点击次数
     */
    @TableField(value = "addToCartClicks")
    private Integer addToCartClicks;

    /**
     * 加入购物车率
     */
    @TableField(value = "addToCartRate")
    private BigDecimal addToCartRate;

    /**
     * 加入列表次数
     */
    @TableField(value = "addToList")
    private Integer addToList;

    /**
     * 从点击加入列表次数
     */
    @TableField(value = "addToListFromClicks")
    private Integer addToListFromClicks;

    /**
     * 合格借阅次数
     */
    @TableField(value = "qualifiedBorrows")
    private Integer qualifiedBorrows;

    /**
     * 从点击合格借阅次数
     */
    @TableField(value = "qualifiedBorrowsFromClicks")
    private Integer qualifiedBorrowsFromClicks;

    /**
     * 版税合格借阅次数
     */
    @TableField(value = "royaltyQualifiedBorrows")
    private Integer royaltyQualifiedBorrows;

    /**
     * 从点击版税合格借阅次数
     */
    @TableField(value = "royaltyQualifiedBorrowsFromClicks")
    private Integer royaltyQualifiedBorrowsFromClicks;

    /**
     * 品牌搜索次数
     */
    @TableField(value = "brandedSearches")
    private Integer brandedSearches;

    /**
     * 品牌搜索点击次数
     */
    @TableField(value = "brandedSearchesClicks")
    private Integer brandedSearchesClicks;

    /**
     * 广告活动预算金额
     */
    @TableField(value = "campaignBudgetAmount")
    private BigDecimal campaignBudgetAmount;

    /**
     * 广告活动预算货币代码
     */
    @TableField(value = "campaignBudgetCurrencyCode")
    private String campaignBudgetCurrencyCode;

    /**
     * 详情页浏览量
     */
    @TableField(value = "detailPageViews")
    private Integer detailPageViews;

    /**
     * 详情页浏览点击次数
     */
    @TableField(value = "detailPageViewsClicks")
    private Integer detailPageViewsClicks;

    /**
     * 加入购物车平均成本
     */
    @TableField(value = "eCPAddToCart")
    private BigDecimal eCPAddToCart;

    /**
     * 新品牌详情页浏览率
     */
    @TableField(value = "newToBrandDetailPageViewRate")
    private BigDecimal newToBrandDetailPageViewRate;

    /**
     * 新品牌详情页浏览量
     */
    @TableField(value = "newToBrandDetailPageViews")
    private Integer newToBrandDetailPageViews;

    /**
     * 新品牌详情页浏览点击次数
     */
    @TableField(value = "newToBrandDetailPageViewsClicks")
    private Integer newToBrandDetailPageViewsClicks;

    /**
     * 新品牌详情页浏览平均成本
     */
    @TableField(value = "newToBrandECPDetailPageView")
    private BigDecimal newToBrandECPDetailPageView;

    /**
     * 新品牌购买次数
     */
    @TableField(value = "newToBrandPurchases")
    private Integer newToBrandPurchases;

    /**
     * 新品牌购买点击次数
     */
    @TableField(value = "newToBrandPurchasesClicks")
    private Integer newToBrandPurchasesClicks;

    /**
     * 新品牌购买百分比
     */
    @TableField(value = "newToBrandPurchasesPercentage")
    private BigDecimal newToBrandPurchasesPercentage;

    /**
     * 新品牌购买率
     */
    @TableField(value = "newToBrandPurchasesRate")
    private BigDecimal newToBrandPurchasesRate;

    /**
     * 新品牌销售额
     */
    @TableField(value = "newToBrandSales")
    private BigDecimal newToBrandSales;

    /**
     * 新品牌销售点击次数
     */
    @TableField(value = "newToBrandSalesClicks")
    private Integer newToBrandSalesClicks;

    /**
     * 新品牌销售百分比
     */
    @TableField(value = "newToBrandSalesPercentage")
    private BigDecimal newToBrandSalesPercentage;

    /**
     * 新品牌销售单位数
     */
    @TableField(value = "newToBrandUnitsSold")
    private Integer newToBrandUnitsSold;

    /**
     * 新品牌销售单位点击次数
     */
    @TableField(value = "newToBrandUnitsSoldClicks")
    private Integer newToBrandUnitsSoldClicks;

    /**
     * 新品牌销售单位百分比
     */
    @TableField(value = "newToBrandUnitsSoldPercentage")
    private BigDecimal newToBrandUnitsSoldPercentage;

    /**
     * 购买次数
     */
    @TableField(value = "purchases")
    private Integer purchases;

    /**
     * 购买点击次数
     */
    @TableField(value = "purchasesClicks")
    private Integer purchasesClicks;

    /**
     * 促销购买次数
     */
    @TableField(value = "purchasesPromoted")
    private Integer purchasesPromoted;

    /**
     * 销售额
     */
    @TableField(value = "sales")
    private BigDecimal sales;

    /**
     * 销售点击次数
     */
    @TableField(value = "salesClicks")
    private Integer salesClicks;

    /**
     * 促销销售额
     */
    @TableField(value = "salesPromoted")
    private BigDecimal salesPromoted;

    /**
     * 销售单位数
     */
    @TableField(value = "unitsSold")
    private Integer unitsSold;

    /**
     * 销售单位点击次数
     */
    @TableField(value = "unitsSoldClicks")
    private Integer unitsSoldClicks;

    /**
     * 5秒视频观看率
     */
    @TableField(value = "video5SecondViewRate")
    private BigDecimal video5SecondViewRate;

    /**
     * 5秒视频观看次数
     */
    @TableField(value = "video5SecondViews")
    private Integer video5SecondViews;

    /**
     * 完整视频观看次数
     */
    @TableField(value = "videoCompleteViews")
    private Integer videoCompleteViews;

    /**
     * 视频第一四分位观看次数
     */
    @TableField(value = "videoFirstQuartileViews")
    private Integer videoFirstQuartileViews;

    /**
     * 视频中点观看次数
     */
    @TableField(value = "videoMidpointViews")
    private Integer videoMidpointViews;

    /**
     * 视频第三四分位观看次数
     */
    @TableField(value = "videoThirdQuartileViews")
    private Integer videoThirdQuartileViews;

    /**
     * 视频取消静音次数
     */
    @TableField(value = "videoUnmutes")
    private Integer videoUnmutes;

    /**
     * 可视率
     */
    @TableField(value = "viewabilityRate")
    private BigDecimal viewabilityRate;

    /**
     * 可视展示次数
     */
    @TableField(value = "viewableImpressions")
    private Integer viewableImpressions;

    /**
     * 可视点击通过率
     */
    @TableField(value = "viewClickThroughRate")
    private BigDecimal viewClickThroughRate;

    /**
     * 操作时间
     */
    @TableField(value = "opttime")
    private Date opttime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public boolean isZero() {
        // 检查所有字段是否为零或空值
        return (addToCart == null || addToCart == 0) &&
                (addToCartClicks == null || addToCartClicks == 0) &&
                (addToCartRate == null || addToCartRate.floatValue() < 0.001f) &&
                (addToList == null || addToList == 0) &&
                (addToListFromClicks == null || addToListFromClicks == 0) &&
                (qualifiedBorrows == null || qualifiedBorrows == 0) &&
                (qualifiedBorrowsFromClicks == null || qualifiedBorrowsFromClicks == 0) &&
                (royaltyQualifiedBorrows == null || royaltyQualifiedBorrows == 0) &&
                (royaltyQualifiedBorrowsFromClicks == null || royaltyQualifiedBorrowsFromClicks == 0) &&
                (brandedSearches == null || brandedSearches == 0) &&
                (brandedSearchesClicks == null || brandedSearchesClicks == 0) &&
                (campaignBudgetAmount == null || campaignBudgetAmount.floatValue() < 0.001f) &&
                (campaignBudgetCurrencyCode == null || campaignBudgetCurrencyCode.isEmpty()) &&
                (detailPageViews == null || detailPageViews == 0) &&
                (detailPageViewsClicks == null || detailPageViewsClicks == 0) &&
                (eCPAddToCart == null || eCPAddToCart.floatValue() < 0.001f) &&
                (newToBrandDetailPageViewRate == null || newToBrandDetailPageViewRate.floatValue() < 0.001f) &&
                (newToBrandDetailPageViews == null || newToBrandDetailPageViews == 0) &&
                (newToBrandDetailPageViewsClicks == null || newToBrandDetailPageViewsClicks == 0) &&
                (newToBrandECPDetailPageView == null || newToBrandECPDetailPageView.floatValue() < 0.001f) &&
                (newToBrandPurchases == null || newToBrandPurchases == 0) &&
                (newToBrandPurchasesClicks == null || newToBrandPurchasesClicks == 0) &&
                (newToBrandPurchasesPercentage == null || newToBrandPurchasesPercentage.floatValue() < 0.001f) &&
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
                (sales == null || sales.floatValue() < 0.001f) &&
                (salesClicks == null || salesClicks == 0) &&
                (salesPromoted == null || salesPromoted.floatValue() < 0.001f) &&
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
                (viewableImpressions == null || viewableImpressions == 0) &&
                (viewClickThroughRate == null || viewClickThroughRate.floatValue() < 0.001f);
    }
}