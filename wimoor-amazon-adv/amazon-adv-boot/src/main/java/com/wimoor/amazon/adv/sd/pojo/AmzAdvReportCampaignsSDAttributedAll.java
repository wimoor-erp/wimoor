package com.wimoor.amazon.adv.sd.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


@Entity
@Table(name="t_amz_adv_rpt2_sd_campaigns_attributed_all")
@Data
public class AmzAdvReportCampaignsSDAttributedAll {
	
	@Id
	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Id
	@Column(name="bydate")
	private Date bydate;

	@Column(name="addToCart")
	private Integer addToCart;
	@Column(name="addToCartClicks")
	private Integer addToCartClicks;
	@Column(name="addToCartRate")
	private BigDecimal addToCartRate;
	@Column(name="addToCartViews")
	private Integer addToCartViews;
	@Column(name="addToList")
	private Integer addToList;
	@Column(name="addToListFromClicks")
	private Integer addToListFromClicks;
	@Column(name="addToListFromViews")
	private Integer addToListFromViews;
	@Column(name="qualifiedBorrows")
	private Integer qualifiedBorrows;
	@Column(name="qualifiedBorrowsFromClicks")
	private Integer qualifiedBorrowsFromClicks;
	@Column(name="qualifiedBorrowsFromViews")
	private Integer qualifiedBorrowsFromViews;
	@Column(name="royaltyQualifiedBorrows")
	private Integer royaltyQualifiedBorrows;
	@Column(name="royaltyQualifiedBorrowsFromClicks")
	private Integer royaltyQualifiedBorrowsFromClicks;
	@Column(name="royaltyQualifiedBorrowsFromViews")
	private Integer royaltyQualifiedBorrowsFromViews;
	@Column(name="brandedSearches")
	private Integer brandedSearches;
	@Column(name="brandedSearchesClicks")
	private Integer brandedSearchesClicks;
	@Column(name="brandedSearchesViews")
	private Integer brandedSearchesViews;
	@Column(name="clickThroughRate")
	private BigDecimal clickThroughRate;
	@Column(name="campaignBudgetCurrencyCode")
	private String campaignBudgetCurrencyCode;
	@Column(name="detailPageViews")
	private Integer detailPageViews;
	@Column(name="detailPageViewsClicks")
	private Integer detailPageViewsClicks;
	@Column(name="eCPAddToCart")
	private BigDecimal eCPAddToCart;
	@Column(name="eCPBrandSearch")
	private BigDecimal eCPBrandSearch;
	@Column(name="impressionsViews")
	private Integer impressionsViews;
	@Column(name="newToBrandPurchases")
	private Integer newToBrandPurchases;
	@Column(name="newToBrandPurchasesClicks")
	private Integer newToBrandPurchasesClicks;
	@Column(name="newToBrandSalesClicks")
	private Integer newToBrandSalesClicks;
	@Column(name="newToBrandUnitsSold")
	private Integer newToBrandUnitsSold;
	@Column(name="newToBrandUnitsSoldClicks")
	private Integer newToBrandUnitsSoldClicks;
	@Column(name="purchases")
	private Integer purchases;
	@Column(name="purchasesClicks")
	private Integer purchasesClicks;
	@Column(name="purchasesPromotedClicks")
	private Integer purchasesPromotedClicks;
	@Column(name="purchasesViews")
	private Integer purchasesViews;
	@Column(name="sales")
	private BigDecimal sales;
	@Column(name="salesClicks")
	private BigDecimal salesClicks;
	@Column(name="salesPromotedClicks")
	private BigDecimal salesPromotedClicks;
	@Column(name="unitsSold")
	private Integer unitsSold;
	@Column(name="unitsSoldClicks")
	private Integer unitsSoldClicks;
	@Column(name="unitsSoldViews")
	private Integer unitsSoldViews;
	@Column(name="videoCompleteViews")
	private Integer videoCompleteViews;
	@Column(name="videoFirstQuartileViews")
	private Integer videoFirstQuartileViews;
	@Column(name="videoMidpointViews")
	private Integer videoMidpointViews;
	@Column(name="videoThirdQuartileViews")
	private Integer videoThirdQuartileViews;
	@Column(name="videoUnmutes")
	private Integer videoUnmutes;
	@Column(name="viewabilityRate")
	private BigDecimal viewabilityRate;
	@Column(name="viewClickThroughRate")
	private BigDecimal viewClickThroughRate;
	@Column(name="opttime")
	private Date opttime;
	public boolean iszero() {
		return (videoFirstQuartileViews == null || videoFirstQuartileViews == 0)
				&& (videoMidpointViews == null || videoMidpointViews == 0)
				&& (videoThirdQuartileViews == null || videoThirdQuartileViews == 0)
				&& (videoCompleteViews == null || videoCompleteViews == 0)
				&& (videoUnmutes == null || videoUnmutes == 0)
				&& (addToCart == null || addToCart == 0)
				&& (addToCartClicks == null || addToCartClicks == 0)
				&& (addToCartViews == null || addToCartViews == 0)
				&& (addToList == null || addToList == 0)
				&& (addToListFromClicks == null || addToListFromClicks == 0)
				&& (addToListFromViews == null || addToListFromViews == 0)
				&& (qualifiedBorrows == null || qualifiedBorrows == 0)
				&& (qualifiedBorrowsFromClicks == null || qualifiedBorrowsFromClicks == 0)
				&& (qualifiedBorrowsFromViews == null || qualifiedBorrowsFromViews == 0)
				&& (royaltyQualifiedBorrows == null || royaltyQualifiedBorrows == 0)
				&& (royaltyQualifiedBorrowsFromClicks == null || royaltyQualifiedBorrowsFromClicks == 0)
				&& (royaltyQualifiedBorrowsFromViews == null || royaltyQualifiedBorrowsFromViews == 0)
				&& (brandedSearches == null || brandedSearches == 0)
				&& (brandedSearchesClicks == null || brandedSearchesClicks == 0)
				&& (brandedSearchesViews == null || brandedSearchesViews == 0)
				&& (clickThroughRate == null || clickThroughRate.floatValue() <= 0.001)
				&& (detailPageViews == null || detailPageViews == 0)
				&& (detailPageViewsClicks == null || detailPageViewsClicks == 0)
				&& (eCPAddToCart == null || eCPAddToCart.floatValue() <= 0.001)
				&& (eCPBrandSearch == null || eCPBrandSearch.floatValue() <= 0.001)
				&& (impressionsViews == null || impressionsViews == 0)
				&& (newToBrandPurchases == null || newToBrandPurchases == 0)
				&& (newToBrandPurchasesClicks == null || newToBrandPurchasesClicks == 0)
				&& (newToBrandSalesClicks == null || newToBrandSalesClicks == 0)
				&& (newToBrandUnitsSold == null || newToBrandUnitsSold == 0)
				&& (newToBrandUnitsSoldClicks == null || newToBrandUnitsSoldClicks == 0)
				&& (purchases == null || purchases == 0)
				&& (purchasesClicks == null || purchasesClicks == 0)
				&& (purchasesPromotedClicks == null || purchasesPromotedClicks == 0)
				&& (purchasesViews == null || purchasesViews == 0)
				&& (sales == null || sales.floatValue() <= 0.001)
				&& (salesClicks == null || salesClicks.floatValue() <= 0.001)
				&& (salesPromotedClicks == null || salesPromotedClicks.floatValue() <= 0.001)
				&& (unitsSold == null || unitsSold == 0)
				&& (unitsSoldClicks == null || unitsSoldClicks == 0)
				&& (unitsSoldViews == null || unitsSoldViews == 0)
				&& (viewabilityRate == null || viewabilityRate.floatValue() <= 0.001)
				&& (viewClickThroughRate == null || viewClickThroughRate.floatValue() <= 0.001);
	}

}