package com.wimoor.amazon.adv.report.pojo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_sumpdtads")
public class AmzAdvSumProductAds extends AmzAdvSumProductAdsKey {
	@Column(name="impressions")
    private Integer impressions;

	@Column(name="clicks")
    private Integer clicks;

	@Column(name="cost")
    private BigDecimal cost;

	@Column(name="currency")
    private String currency;

	@Column(name="attributedUnitsOrdered")
    private Integer attributedunitsordered;

	@Column(name="attributedConversions")
    private Integer attributedConversions;
	
	@Column(name="attributedSales")
    private BigDecimal attributedsales;

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public Integer getAttributedunitsordered() {
        return attributedunitsordered;
    }

    public void setAttributedunitsordered(Integer attributedunitsordered) {
        this.attributedunitsordered = attributedunitsordered;
    }

    public BigDecimal getAttributedsales() {
        return attributedsales;
    }

    public void setAttributedsales(BigDecimal attributedsales) {
        this.attributedsales = attributedsales;
    }

	public Integer getAttributedConversions() {
		return attributedConversions;
	}

	public void setAttributedConversions(Integer attributedConversions) {
		this.attributedConversions = attributedConversions;
	}
    
}